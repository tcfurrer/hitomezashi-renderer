# Hitomezashi Pattern Renderer

A simple JavaFX application for drawing Hitomezashi stitch patterns.

To learn about Hitomezashi patterns, watch this [Numberphile Video](https://youtu.be/JbfhzlMk2eY?si=AVzGKizFhZOK0Jwj).

<img src="hitomezashi_example.png" width="200" height="150">

To build an installable package for Windows, clone this repo and run:

&nbsp;&nbsp;&nbsp;&nbsp;`./gradlew jpackage`

Or run the `Package for Release` run configuration in IntelliJ IDEA.

In either case, an `.msi` file should appear in the `dist` folder.

Building from IDEA is slightly easier, because the gradle cmdline requires a JDK
to be installed locally and pointed to by `JAVA_HOME` (at least
until [Gradle issue 2508](https://github.com/gradle/gradle/issues/2508) has been resolved).
