# contract-service
## Code structure
```
project  
│
└───api  //Web Controller, Dto and Springboot configuration
│   └─── src
│   │   build.gradle
└─── common // Common Utilities
│   └─── src  
│   │   build.gradle
└─── core  // Enterprise and Application Business Rules and Domain
│   └─── src
│   │   build.gradle
└─── infra // External, DB, UI and Device layer
│   └─── src 
│   │   build.gradle
│build.gradle
│settings.gradle
```
## Formatting

Format by running following command

```
./gradlew spotlessApply
```

## How to start contract-service

Start the application by running following command

```
./gradlew bootRun
```

## Reference Document
https://galaxyfinx.atlassian.net/wiki/spaces/EN/pages/49152238/Contract+and+Cloud+Signature+Service