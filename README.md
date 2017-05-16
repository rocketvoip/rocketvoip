README
===

Running Locally
---

When running RocketVoip locally, you can choose between two databases. A local PostgreSQL instance or an In-Memory-Database.

To start RocketVoip together with an In-Memory-Database, use

    mvn -P inmemorydb -Dspring.profiles.active=inmemorydb spring-boot:run
    

Using a local PostgreSQL server, you need to start RocketVoip as such
    
    mvn -Dspring.profiles.active=localdb spring-boot:run
    
The `localdb` profile expects the database user to be named `rocketvoip` with the password `rocketvoip`. The database has to be called `rocketvoip`

Setting up a local PostgreSQL server is outside the scope of this README. Please refer to the appropriate documentation on how to setup a local PostgreSQL database.

Operations Manual
---
[Operations Manual](OPERATIONS.md)


Build Pipeline
---

* CI/CD: [travis-ci](https://travis-ci.org/rocketvoip/rocketvoip)
* Coverage: [Coveralls.io](https://coveralls.io/github/rocketvoip/rocketvoip)
* Code Quality: [Sonar Qube](https://sonarqube.com/dashboard/index?id=ch.zhaw.psit4%3Arocketvoip)

Travis deploys to rocketvoip-staging.


Heroku
---

* Production: https://dashboard.heroku.com/apps/rocketvoip
* Staging: https://dashboard.heroku.com/apps/rocketvoip-staging


Deployment
---

* Production: https://rocketvoip.herokuapp.com/
* Staging: https://rocketvoip-staging.herokuapp.com/

Build Status
---

Master: [![Build Status](https://travis-ci.org/rocketvoip/rocketvoip.svg?branch=master)](https://travis-ci.org/rocketvoip/rocketvoip) [![Coverage Status](https://coveralls.io/repos/github/rocketvoip/rocketvoip/badge.svg?branch=master)](https://coveralls.io/github/rocketvoip/rocketvoip?branch=master) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=ch.zhaw.psit4:rocketvoip)](https://sonarqube.com/dashboard/index/ch.zhaw.psit4:rocketvoip)
