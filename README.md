# TestBalloon Isolated Projects Reproducer

> [!NOTE] 
> This has been an issue with Gradle 9.5.1 and is fixed with 9.6.0
>
> I keep this repository for context.

This repo is a small reproducer for a [TestBalloon](https://github.com/infix-de/testBalloon/) issue with [Gradle Isolated Projects](https://docs.gradle.org/current/userguide/isolated_projects.html).

### What breaks

With isolated projects enabled, `:sample:jvmTest` fails while Gradle is storing the configuration cache.

The key error is:

```text
Plugin 'de.infix.testBalloon': Project ':sample' cannot dynamically look up a property in the parent project ':'
```

You might also see related entries under `org.jetbrains.kotlin.multiplatform` or `Unknown location` in the problems’ 
report that could be attributed to the TestBalloon plugin too.

### Project shape

- Kotlin Multiplatform project with `jvm()`
- Nested module path `:parent:sample`
- TestBalloon applied to that nested module
- One `commonTest` test via `testSuite`
- Isolated projects + configuration cache enabled

### Reproduce

From the repo root:

```bash
./gradlew :sample:jvmTest
```

This will fail while storing the configuration cache. For full details, review:

`build/reports/problems/problems-report.html`

### Likely fix direction in TestBalloon

To stay compatible with isolated projects, one safe approach is to:

- Resolve config from the current project only, and/or
- Use explicit extension/property defaults without walking up to parent projects.

The Gradle migration guidance for isolated projects should help when implementing the fix:
https://docs.gradle.org/current/userguide/isolated_projects.html#sec:migration

### Temporary mitigation

`gradle.properties.mitigation` includes explicit TestBalloon defaults that avoid the problem:

```bash
cat gradle.properties.mitigation >> gradle.properties
./gradlew :sample:jvmTest
```

With that mitigation in place, the configuration cache is stored without errors.

### Versions

| Tool                        | Version                                                            |
|-----------------------------|--------------------------------------------------------------------|
| Gradle                      | `9.5.1`                                                            |
| Gradle wrapper distribution | `gradle-9.5.1-all.zip`                                             |
| Wrapper SHA-256             | `c72fb9991f6025cbe337d52ba77e531b3faf62bdd3e348fe1ccee9f51c71adb0` |
| Kotlin Gradle plugin        | `2.4.0`                                                            |
| TestBalloon                 | `1.0.1-K2.4.0`                                                     |

### Relevant Gradle Properties

```properties
org.gradle.unsafe.isolated-projects=true
org.gradle.configuration-cache=true
```
