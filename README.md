# Booking

- [Purpose](#purpose)
- [Technologies](#techs)
- [Requirements](#reqs)
- [Architecture](#architecture)
- [Local Configuration](#localconfig)
- [Testing](#testing)
- [Postman Doc](#postman)
- [Possible improvements](#improvements)

<a id="purpose"></a>
## Purpose

This system was created to make hotel rooms booking easier.

<a id="techs"></a>
## Technologies Used

- [Spring boot](https://spring.io/projects/spring-boot/)
- [Java11](https://www.java.com)
- [MongoDB](https://www.mongodb.com/)
- Mockito and Junit for testing

<a id="reqs"></a>
## Requirements
- To run the application itself, **mvn clean install** and then **mvn spring-boot:run**. Alternatively you can setup the build and run on your favorite IDE.

<a id="architecture"></a>
## Architecture and technical Decisions

### MongoDB
- Given the possibility of having aggressive flow inside the **booking** api, I decided to use **MongoDB**, mainly due to its ease to scale horizontaly really quickly, if needed. We also don't have many complex relationships between different entities, making the use of **NoSQL** even better.



<a id="localconfig"></a>

## Local Configuration
```
- Please notice that you have to have **mongo** installed upfront. If you use mac I suggest using homebrew for the installation.

- You can find more information on this link to install it depending on your operational system (https://www.mongodb.com/docs/manual/administration/install-community/) 

- On the project's **application.yml** you will notice that I'm using a sample user username: myUserAdmin password: abc123. You can create this same one and give it access to the database, or you can change it with your own user.

- if you choose to create a new user you can do it like that on the mongo console: ```db.createUser(
  {
    user: "root",
    pwd: "root",
    roles: [ { role: "readWrite", db: "booking" } ]
  }
);```

- By default application will be started at port 8080, but you can change it in application.yml file.
```
<a id="testing"></a>
### Testing
- To run the tests you can access the folder src > test.

<a id="postman"></a>
## Postman Documentation

I have prepared a postman documentation, in which you will be able to check in details each endpoint and possible Requests and responses.

Please access it by link below:

https://documenter.getpostman.com/view/4694407/VUxNT8Wf

<a id="improvements"></a>
## Possible Improvements

A system is never perfect and there is always room for improvement. Here are some points that I would like to enhance if I had more time:

- Enhance the way I deal with Dates and MongoDB. This is a common problem in the community so I would spend more time on that.
- Dockerize the DB to make the setup easier
- Create more tests not only unit tests but also integration ones.

## Support

* If you have any query or doubt, please, feel free to contact me by e-mail.

