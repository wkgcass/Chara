# core

The base lib of the Chara application.

## build

```
./gradlew clean jar jlink jpackage jpackagePost
```

## compatibility

Everything works well on macos, but on windows there are several issues:

See [JDK-8254920](https://bugs.openjdk.java.net/browse/JDK-8254920), you must add `zip.dll` along with jpackage generated `.exe` on the windows platform. The `jpackageZipDLL` task will move `zip.dll` to the correct place. After that `jpackageExecInstaller` task will run `jpackage` to generate the installers. These two tasks are wrapped into the `jpackagePost` task.

~~See [badass-runtime-issue-71](https://github.com/beryx-gist/badass-runtime-issue-71), using `JDK15` jpackage will fail on windows platform with exit code 311, you have to use `JDK16` with a custom wxl configuration. The `JDK16` should be placed in the directory `../../jdk+16/`.~~

The above wxl 311 issue can be fixed with [this article](https://ravenxrz.ink/archives/421e5ad2.html) by adding `--vendor raven` into jpackage args.  
The article also says `--win-menu` will also raise exit code 311 if `--win-menu-group` not specified, which I verified myself that this is true.
