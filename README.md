# 🏃🏼Benevity Challenge

> 🚀  Take Home Test from Benevity

## Development

### Dependencies
- Node (install using [nvm](https://github.com/nvm-sh/nvm#installing-and-updating))
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [jenv](https://www.jenv.be/)
- [nvm](https://github.com/nvm-sh/nvm)

### Prepare environment
1. Set node version
   ```shell
   nvm use
   npm install
   npm run prepare # setup pre-commit hooks
   ```
   
### Run in local
1. Run docker-compose to start MongoDB database
    ```shell
    docker-compose up -d
    ```

2. Start up backend server:
   ```shell
    cd server
    jenv local
    export MONGO_DB_DATABASE=test
    export MONGO_DB_CONNECTION_STRING=mongodb://localhost:27017
    ./gradlew clean run
   ```
   
3. Start up client:
   ```shell
   cd client
   nvm use
   npm start
   ```
   
4. Open browser and go to http://127.0.0.1:8000/signup

## Run e2e (cypress) tests
1. Run cypress:
   ```shell
    cd client
    nvm use
    npm run cypress:open
    ```
2. Select E2E Testing
3. Select Chrome
4. Select Specs and run

## Further improvements
- Error handling on client side for server errors.
- Handling authentication errors (e.g. token expired) on client side and redirect to user.

