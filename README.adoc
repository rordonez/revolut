= Revolut REST API Design

The purpouse of this project is to define a basic REST API for money transfers between internal users/accounts.

== Technology Stack

Spring Boot makes it easy to create Spring-powered, production-grade applications and services with absolute minimum fuss. It takes an opinionated view of the Spring platform so that new and existing users can quickly get to the bits they need.

This project uses the following modules / libraries:

* Latest version of Spring Boot
* Spring 4
* Java 8
* JPA for data persistence
* Embedded H2 database
* Spring HATEOAS for Hypermedia

This project also includes Unit tests and Integration tests. The approach is to have a separation of concerns between the different layers of the application. This way, there are three layers in the application:

* Controller layer: The TransactionsController contains the necessary logic to manage the transfer.
* Service layer: This project uses the Spring container to inject depedencies, using interfaces to have the posibility to choose between different implementations. For this layer, the interface TransferService provides all the necessary services to work with transactions.
* Repository layer: It is the DAO. It uses JPA repositories. The TransferService implementation has two colaborators: AccountRepository (to get the internal accounts) and a TransferRepository (to store the transaction).

== Design Decisions

There is one resource `transactions` and one operation using the POST method. I had in mind to implementations to do the transfer:

* First, use an accounts resource and use path variables like: /accounts/0/transfers/1?amount=5.0. This approach forces you to have knowledge about the account ids.
* Second (implemented), make a POST request to transations resource, like this:

----
curl -H "Content-Type: application/json" -X POST -d '{"sourceAccount":"0","targetAccount":"1", "amount":5.0}' http://localhost:8080/transactions
----

Where:

* sourceAccount is the source account number.
* targetAccount is the target account number.
* amount is the desired amoung to be transfered.

The second one is simpler for this exercise.

NOTE:There are validations to all these fields. For this it is used JSR-303 validations in Spring.

TransferService is a collaborator  within TransactionsController to make the transfers. Constructor injection has been used since it is a mandatory dependency.

It is a good practice to include links inside the resource to implement HATEOAS. For future features, it is a good idea to implement OPTIONS request to discover the API.

The result is a JSON object including the link to itself and a status. The value of the status property is PENDING since we could need to ask user to with its credentials. When the user gives this information (maybe using a PUT request) then the transfer will have COMPLETED status.

For content negotiation, by default Spring uses the value of the Accept Header. The could change this configuration as needed.

There is one central piece to manage the error within the controllers. This class is the RevolutControllerAdvice and provides all the information for the error. The implementation contains only a message, but it would be necessary to add more information like a developerMessage with more technical information, status code or manage errors as another resource providing a link with an identifier.

== Run the application

To run the application, execute:

----
mvn package && java -jar target/revolut-0.0.1-SNAPSHOT.jar

----

