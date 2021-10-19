# soundlibs-module

## Before

 For some reason I need to play ogg music in my project, and the project uses `jpackage` to bundle.
 As we know `jpackage` works poor with automatic modules (modules without `module-info.java`).
 Unfortunately, packages in `com.google.soundlibs` is not modular because they weren't updated anymore
 from 2016 (actually some source code wasn't updated from 2000). In the meantime, I don't want to
 use `jdeps` and `jlink` to generate custom runtime image. So I cloned the repo, added `module-info.java`
 and re-complied the source.

 Then I got modular soundlibs.

## After

 The source code of soundlibs is so old that I got many warnings while compiling. So I am rewriting
 them all in JDK11 for stability and studying the principles of sound-processing.
 
 So we got soundlibs-module.
