from pathlib import Path


def replace_once(path: Path, old: str, new: str) -> None:
    text = path.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"Expected one match in {path}, found {count}")
    path.write_text(text.replace(old, new), encoding="utf-8", newline="\n")


app = Path("app/build.gradle")
replace_once(
    app,
    '''        release {
            minifyEnabled true
            shrinkResources true
            if (hasReleaseSigning) {
                signingConfig signingConfigs.release
            }
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"

            dependenciesInfo {
                includeInApk = false
                includeInBundle = false
            }
        }
''',
    '''        release {
            minifyEnabled true
            shrinkResources true
            if (hasReleaseSigning) {
                signingConfig signingConfigs.release
            }
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"

            dependenciesInfo {
                includeInApk = false
                includeInBundle = false
            }
        }

        benchmark {
            initWith release
            signingConfig signingConfigs.debug
            debuggable false
            shrinkResources false
            matchingFallbacks = ["release"]
        }
''',
)

benchmark = Path("startup-benchmark/build.gradle")
replace_once(
    benchmark,
    '''    targetProjectPath = ":app"

    defaultConfig {
''',
    '''    targetProjectPath = ":app"
    targetBuildType = "benchmark"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    defaultConfig {
''',
)
replace_once(
    benchmark,
    '''    implementation "junit:junit:4.13.2"
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
''',
    '''    implementation "junit:junit:4.13.2"
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"
''',
)
