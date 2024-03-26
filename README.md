# DevPath Backend

Check the collections to see the avaliable features

## How to start the project
- `docker-compose up`
- ./gradlew 

## How to develop a new feature

After developing the feature and the tests, do the following:
- `./gradlew ktlintFormat`
- `./gradlew clean build`
- Run tests using Intellij coverage to make sure your new code is covered by your tests
- Update the collections
