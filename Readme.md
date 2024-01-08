### Intro
This is an Android app to test and improve your typing speed and accuracy. Made for interview process

### Installation
To run the project in Android studio, add a 'local.properties' file in root with single line - path to Android Sdk such as 'sdk.dir=/Users/nuru/Library/Android/sdk'

### Test
There is a single test AnalyticsTest.kt. More info on how to run and what to expect is in code doc

### Technical details
This is a simple app yet 
1. I utilised MVVM with some Clean Architecture patterns (such as Use Cases)
2. Added some tests that mimic user input
3. Use abstraction (Calculators)
4. Use Singleton (for database)
5. Coroutines, Flows, States (from jetpack compose)
6. For UI a Jetpack compose is used
7. SOLID principles
8. Domain Driven Design, with separation from Database classes, such as User.kt and UserDb.kt; domain transfer objects
9. Repository Patterns
10. KISS and DRY principles are 

### What is missing or could be improved
1. Since this is an offline app, I skipped separating Repository internals into DatabaseSource and Network source (since only DB is present)
2. Dependency injection for viewmodels is handled by Jetpack Compose, but for complex applications a DI library is needed
3. Testing could be fine granular, but I decided to implement 'user-centric' test cases
4. UI testing was not implemented in favor of previous point
5. UI during landscape orientation could be improved