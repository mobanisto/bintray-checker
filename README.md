# Bintray Checker

This is a tool for finding out which Maven artifacts a project depends on
that can only be found on Bintray / JCenter.

## How does it work?

The tool starts a Maven proxy server that you can route all your
dependency-resolving traffic through. It fetches artifacts from a list of
configured real Maven repositories, taking notes in the process what can
be fetched where and especially looks for alternatives for artifacts from
Bintray / JCenter.

## How to use it

[Download the standalone jar
file](https://github.com/mobanisto/bintray-checker/releases/download/0.0.1/bintray-checker-0.0.1.jar)
and run it like this:

    java -jar bintray-checker-0.0.1.jar --port 9090

Go to the project that you want to examine and change the repositories.
For example replace this:

    repositories {
        jcenter()
        google()
    }

with this:

    repositories {
        maven { url 'http://localhost:9090/' }
    }

Then build your project and refresh all depdendencies. For example:

    ./gradlew build --refresh-depenencies
    ./gradlew assembleRelease --refresh-depenencies

## Results

Visit `http://localhost:9090/` in the browser and you will find a report.
The top-most section contains an overview of the configured Maven servers
and lists all the artifacts that have been requested while running the build:

![Example](/images/example.png)

What follows is a list of all configured servers with a list of artifacts
resolved to that server for each of them.

Next, there's a list of all artifacts fetched from Bintray. It shows two
sublists:

1. the artifacts that could also be fetched from one of the other
   repositories
2. the artifacts that we could not find an alternative for.

If you lucky, the second list is empty:

![All dependencies found](/images/all-found.png)

If you're in bad luck, the second list contains some artifacts:

![One dependency missing](/images/one-missing.png)

In that case you know you need to take action otherwise your build will fail
once the sunset of Bintray is complete.

## Hacking

The list of servers the tool checks for alternatives and the order in which
they are checked is currently hard-coded in this file:

    core/src/main/java/de/mobanisto/bintray/checker/maven/Data.java

There's a list of servers defined at the top:

    servers.add(bintray);
    servers.add(new Server("https://dl.google.com/android/maven2/"));
    servers.add(new Server("https://jitpack.io"));
    servers.add(new Server("https://plugins.gradle.org/m2/"));
    servers.add(new Server("https://repo1.maven.org/maven2/"));

Reorder the items or add new ones to fit your setup. Then either rebuild
and run the fat jar using this command:

    ./gradlew fatJar
    java -jar cli/build/libs/bintray-checker-0.0.1.jar

Or build and run like this:

    ./gradle clean create
    ./scripts/run-web-app-embedded

## License

bintray-checker is free software: you can redistribute it and/or modify
it under the terms of the [GNU Affero General Public License](AGPL.md)
as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

bintray-checker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with bintray-checker. If not, see <http://www.gnu.org/licenses/>.
