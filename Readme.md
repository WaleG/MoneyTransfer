# [Revolut Backend Test](https://drive.google.com/file/d/1Rhk07_MT5WP_5f-lF0LxkJKt5pPM8SKd/view)
Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

## Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

## Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

## Additional comments:
1. Used Java for implementation
2.1 Used libraries:
    - H2 - in-memory database
    - JDBI - improved database interface library 
    - Javalin - a simple web framework
    - Guice - lightweight dependency injection framework
    - Slf4j - simple logging library
    - FasterXML/jackson - standard JSON library for Java 
2.2 Testing libraries:
    - Junit 5 - framework for unit testing
    - Mockito - a simple mocking framework for unit tests
    - REST Assured - library for testing RESTFul APIs

## How to run:
1. Build `./gradlew clean build` 
2. Run `./gradlew run`