# core

The base lib of the Chara application.

## build

```
./gradlew clean jar jlink jpackage jpackagePost
```

## compatibility

Everything works well on macos, but on windows there are several issues:

See [JDK-8254920](https://bugs.openjdk.java.net/browse/JDK-8254920), you must add `zip.dll` along with jpackage generated `.exe` on the windows platform. The `jpackagePost` task will move `zip.dll` to the correct place, but installers won't work so they will not be generated on the windows platform.

See [badass-runtime-issue-71](https://github.com/beryx-gist/badass-runtime-issue-71), using `JDK15` jpackage will fail on windows platform with exit code 311, you have to use `JDK16` with a custom wxl configuration. The `JDK16` should be placed in the directory `../../jdk+16/`.
