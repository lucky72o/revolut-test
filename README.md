- To run this application, you need Java 1.8 to be installed
- sbt-dist is included
- To start the app run:
sbt run or sbt.bat run


API's:

<table>
<tr><td>Request</td><td>URL</td><td>Description</td><td>Example</td></tr>
<tr><td>GET</td><td>/users</td><td>get List of users</td><td></td></tr>
<tr><td>GET</td><td>/users/$id</td><td>get User by Id</td><td></td></tr>
<tr><td>POST</td><td>/users/create </td><td>create a new user</td><td><p>Content-Type: application/json</p>{"name":"user-name", "wallet":{"currecny":"GBP", "amount": 20}}</td></tr>
<tr><td>POST</td><td>/users/$id/transfer</td><td>transfer money from one user to another</td><td><p>Content-Type: application/json</p>{"fromUser":1,"toUser":3,"currency":"GBP","amount":5}</td></tr>

Integration test is included, which creates users, do money transfer from one user to another and then checks balances: EndToEndIntegrationTest.java
