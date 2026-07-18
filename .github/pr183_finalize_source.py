from pathlib import Path


def replace_once(path: Path, old: str, new: str) -> None:
    text = path.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"Expected one match in {path}, found {count}")
    path.write_text(text.replace(old, new), encoding="utf-8", newline="\n")


exo = Path("app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt")
replace_once(
    exo,
    """            val audioRenderer = RenderersFactory { handler, _, audioListener, _, _ ->
                arrayOf<BaseRenderer>(
                    FfmpegAudioRenderer(handler, audioListener, replayGainProcessor),
                    MediaCodecAudioRenderer(
                        context,
                        MediaCodecSelector.DEFAULT,
                        handler,
                        audioListener,
                        DefaultAudioSink.Builder(context)
                            .setAudioProcessors(arrayOf(replayGainProcessor))
                            .build(),
                    ),
                )
            }
""",
    """            val audioRenderer = RenderersFactory { handler, _, audioListener, _, _ ->
                // Prefer Android's platform decoder for normal formats. FFmpeg remains a fallback
                // compatibility renderer instead of loading first for every supported track.
                val platformRenderer =
                    MediaCodecAudioRenderer(
                        context,
                        MediaCodecSelector.DEFAULT,
                        handler,
                        audioListener,
                        DefaultAudioSink.Builder(context)
                            // Keep one processor available for runtime ReplayGain setting changes;
                            // it remains at unity gain while disabled or when metadata is absent.
                            .setAudioProcessors(arrayOf(replayGainProcessor))
                            .build(),
                    )
                arrayOf<BaseRenderer>(
                    platformRenderer,
                    FfmpegAudioRenderer(handler, audioListener, replayGainProcessor),
                )
            }
""",
)

doc = Path("docs/architecture/INCREMENTAL_LIBRARY_PIPELINE.md")
replace_once(
    doc,
    "FFmpeg remains a playback compatibility renderer rather than an indexing/enrichment component; this PR does not add a second renderer pipeline or player.",
    "Android's platform MediaCodec renderer is ordered first for normal formats. FFmpeg remains a fallback playback compatibility renderer rather than an indexing/enrichment component; this PR does not add a second renderer pipeline or player.",
)
