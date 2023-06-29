# 🏃🏼Benevity Challenge - Server

## API
API documentation is available as OpenAPI sepc in `api.yaml` file.
Examples of HTTP request can be found on `api.http` file.

## Prepare environment

1. Set java version
   ```shell
   jenv local
   ```

2. Run tests
    ```shell
    ./gradlew clean test
   ```

> In order to run, MongoDB database must be running. Check main README.md file.
3. Run server
   ```shell
    ./gradlew clean run
    ```
   
4. Execute request defined on `api.http` file

## Improvements

- HTTP parameters and request: parse and validate correctly.
- Improve error handling, having a default error handler and mapping domain exceptions.