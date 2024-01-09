## Intro
This is an Android app to test and improve your typing speed and accuracy.

## Installation
To run the project in Android studio, add a `local.properties` file in root with single line - path to Android Sdk such as `sdk.dir=/Users/nuru/Library/Android/sdk`

Or install directly release version from app/release/app-release.apk:
[app-release.apk](app%2Frelease%2Fapp-release.apk)

## Test
There is a single test AnalyticsTest.kt. More info on how to run and what to expect is in code doc

## Technical details
This is a simple app yet 
1. I utilised MVVM with some Clean Architecture patterns (such as Use Cases)
2. Added some tests that mimic user input
3. Use abstraction (Calculators)
4. Use Singleton (for database)
5. Hilt is used for Dependency injection
6. Coroutines, Flows, Jetpack Compose States
7. For UI a Jetpack compose is used
8. SOLID and composition principles
9. Domain Driven Design, with separation from Database classes, such as User.kt and UserDb.kt; domain transfer objects
10. Repository Patterns
11. KISS and DRY principles are applied
12. Compose Navigation for navigating screens

## What is missing or could be improved
1. Since this is an offline app, I skipped separating Repository internals into DatabaseSource and Network source (since only DB is present)
2. Testing could be fine granular, but I decided to implement 'user-centric' test cases
3. UI testing was not implemented in favor of previous point