#!/usr/bin/env python3
"""Create deterministic Markdown evidence from AndroidX Macrobenchmark JSON outputs."""

from __future__ import annotations

import argparse
import json
import math
import statistics
import tempfile
from pathlib import Path
from typing import Any, Iterable


def parser() -> argparse.ArgumentParser:
    value = argparse.ArgumentParser(description=__doc__)
    value.add_argument("--input-root", type=Path)
    value.add_argument("--context", type=Path)
    value.add_argument("--output", type=Path)
    value.add_argument("--require-results", action="store_true")
    value.add_argument("--self-test", action="store_true")
    return value


def finite_numbers(values: Iterable[Any]) -> list[float]:
    result: list[float] = []
    for value in values:
        if isinstance(value, bool) or not isinstance(value, (int, float)):
            continue
        number = float(value)
        if math.isfinite(number):
            result.append(number)
    return result


def percentile(values: list[float], fraction: float) -> float:
    ordered = sorted(values)
    if len(ordered) == 1:
        return ordered[0]
    position = (len(ordered) - 1) * fraction
    lower = math.floor(position)
    upper = math.ceil(position)
    if lower == upper:
        return ordered[lower]
    return ordered[lower] + (ordered[upper] - ordered[lower]) * (position - lower)


def metric_samples(metric: Any) -> list[float]:
    if isinstance(metric, list):
        return finite_numbers(metric)
    if not isinstance(metric, dict):
        return []
    for key in ("runs", "values", "measurements", "samples"):
        values = metric.get(key)
        if isinstance(values, list):
            numbers = finite_numbers(values)
            if numbers:
                return numbers
    return []


def metric_summary(metric: Any) -> tuple[float, float, float, float, int] | None:
    samples = metric_samples(metric)
    if samples:
        median = statistics.median(samples)
        p90 = percentile(samples, 0.90)
        p95 = percentile(samples, 0.95)
        variance = statistics.pvariance(samples) if len(samples) > 1 else 0.0
        return median, p90, p95, variance, len(samples)
    if isinstance(metric, dict) and isinstance(metric.get("median"), (int, float)):
        median = float(metric["median"])
        maximum = float(metric.get("maximum", median))
        variance = float(metric.get("variance", metric.get("standardDeviation", 0.0)))
        return median, maximum, maximum, variance, int(metric.get("repeatIterations", 0))
    return None


def benchmark_entries(document: Any) -> Iterable[dict[str, Any]]:
    if isinstance(document, dict):
        benchmarks = document.get("benchmarks")
        if isinstance(benchmarks, list):
            yield from (entry for entry in benchmarks if isinstance(entry, dict))
        for value in document.values():
            yield from benchmark_entries(value)
    elif isinstance(document, list):
        for value in document:
            yield from benchmark_entries(value)


def load_context(path: Path | None) -> dict[str, str]:
    if path is None or not path.is_file():
        return {}
    result: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8", errors="replace").splitlines():
        key, separator, value = line.partition("=")
        if separator and key.strip():
            result[key.strip()] = value.strip()
    return result


def build_report(input_root: Path, context_path: Path | None) -> tuple[str, int]:
    rows: list[tuple[str, str, float, float, float, float, int]] = []
    for path in sorted(input_root.rglob("*.json")):
        try:
            document = json.loads(path.read_text(encoding="utf-8"))
        except (OSError, json.JSONDecodeError):
            continue
        for benchmark in benchmark_entries(document):
            name = str(benchmark.get("name") or benchmark.get("testName") or "unnamed")
            metrics = benchmark.get("metrics")
            if not isinstance(metrics, dict):
                continue
            for metric_name, metric in metrics.items():
                summary = metric_summary(metric)
                if summary is not None:
                    rows.append((name, str(metric_name), *summary))

    context = load_context(context_path)
    lines = ["# Startup benchmark evidence", ""]
    if context:
        lines.extend(["## Context", ""])
        lines.extend(f"- **{key}:** `{value}`" for key, value in sorted(context.items()))
        lines.append("")
    lines.extend(
        [
            "## Metrics",
            "",
            "| Benchmark | Metric | Median | P90 | P95 | Variance | Samples |",
            "|---|---|---:|---:|---:|---:|---:|",
        ]
    )
    for name, metric, median, p90, p95, variance, samples in rows:
        lines.append(
            f"| {name} | {metric} | {median:.3f} | {p90:.3f} | {p95:.3f} | "
            f"{variance:.3f} | {samples} |"
        )
    if not rows:
        lines.append("| _No Macrobenchmark metric JSON found_ | — | — | — | — | — | — |")
    lines.extend(
        [
            "",
            "Repository/emulator evidence is **Observed** only for the recorded environment. ",
            "Exact TS18 latency remains **Requires device validation**.",
            "",
        ]
    )
    return "\n".join(lines), len(rows)


def self_test() -> None:
    fixture = {
        "benchmarks": [
            {
                "name": "coldStartupWithBaselineProfile",
                "metrics": {"timeToInitialDisplayMs": {"runs": [100.0, 120.0, 140.0]}},
            }
        ]
    }
    with tempfile.TemporaryDirectory(prefix="auxio-benchmark-summary-") as temporary:
        root = Path(temporary)
        (root / "result.json").write_text(json.dumps(fixture), encoding="utf-8")
        report, count = build_report(root, None)
        if count != 1 or "120.000" not in report or "138.000" not in report:
            raise RuntimeError(report)


def main() -> int:
    args = parser().parse_args()
    if args.self_test:
        self_test()
        print("startup benchmark summarizer: PASS")
        return 0
    if args.input_root is None or args.output is None:
        raise SystemExit("--input-root and --output are required unless --self-test is used")
    report, count = build_report(args.input_root, args.context)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(report, encoding="utf-8", newline="\n")
    print(f"startup benchmark summary: {count} metric rows -> {args.output}")
    return 1 if args.require_results and count == 0 else 0


if __name__ == "__main__":
    raise SystemExit(main())
