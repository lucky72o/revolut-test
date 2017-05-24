- To run this application, you need Jaca 1.8 to be installed
- sbt-dist is included
- To start the app run:
sbt run or sbt.bat run


API's:

GET         /users                          : get List of users

GET         /users/$id<[0-9]+>              : get User by Id

POST        /users/create                   : create a new user

POST        /users/$id<[0-9]+>/transfer     : transfer money from one user to another

Integration test is included, which creates users, do money transfer from one user to another and then checks balances: EndToEndIntegrationTest.java
