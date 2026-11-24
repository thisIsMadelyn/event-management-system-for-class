# Event Management System

A JavaFX desktop application for the Human-Computer Interaction course (deliverable 3).

The app implements one complete scenario:

> **S1:** an attendee (Yorikas) browses events, opens one, registers for it and sees it in *My registrations*.

It has four screens, which follow the low-fidelity wireframes. W1 to W3 cover S1:

| Screen | Purpose |
|---|---|
| W1 Browse events | List of upcoming events, with search by keyword, category and date |
| W2 Event details and registration | Event details and the registration form, with input validation |
| W3 My registrations | The attendee's registrations, with cancel and confirmation |
| W4 Create event | Form with which an organizer publishes an event, with input validation |

All data is held in memory and starts from sample events. Nothing is saved between runs.

> **Status:** in development. See [PLAN.md](PLAN.md) for the steps and their progress.

## Requirements

- **JDK 21.** Any distribution works (Oracle, Temurin, etc.).
- The `JAVA_HOME` environment variable must point to the JDK folder, for example `C:\Program Files\Java\jdk-21`.

You don't need to install Maven or JavaFX. The included Maven Wrapper (`mvnw`) downloads both the first time you build.

## Run

From the project folder:

**Windows**

```
mvnw.cmd clean javafx:run
```

**macOS / Linux**

```
./mvnw clean javafx:run
```

The first run needs an internet connection to download dependencies.

### Setting `JAVA_HOME` for one terminal session

If the wrapper reports `JAVA_HOME not found`:

**Windows (PowerShell)**

```
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
```

**Windows (Command Prompt)**

```
set JAVA_HOME=C:\Program Files\Java\jdk-21
```

**macOS / Linux**

```
export JAVA_HOME=/path/to/jdk-21
```

### Running from IntelliJ IDEA

Open the project folder. IntelliJ detects the Maven project. Then run `org.example.demo.Launcher`.

## Project structure

```
src/main/java/org/example/demo/      Java source (application and controllers)
src/main/resources/org/example/demo/ FXML layouts and styles
pom.xml                              Maven build (Java 21, JavaFX 21.0.6)
```
