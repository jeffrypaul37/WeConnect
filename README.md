# WeConnect

Developed by Group 4

| Name                    | Email                | Banner ID |
|-------------------------|----------------------|-----------|
| Haoyu Wang              | hy873711@dal.ca   | B00976563 |
| Jeet Jani               | jeetjani@dal.ca    | B00972192 |
| Jeffry Paul Suresh Durai| jeffrypaul.sureshdurai@dal.ca  | B00952114 |
| Kushal Jankinkumar Panchal                  | ks735728@dal.ca   | B00985061 |
| Luv Prakashkumar Patel               | lv455130@dal.ca   | B00950942 |

Mentor: Tushar Sharma (tushar@dal.ca)

TA: Saurabh Rajput (saurabh@dal.ca)

## Table of Contents
1. [Overview](#overview)
1. [Dependencies](#dependencies)
2. [Build/Deployment](#builddeployment)
3. [User Scenario](#user-scenario)


## Overview
WeConnect serves as a hub where urban residents can unite and express their enthusiasm for their common interests.
Our distinctive gatherings take place throughout Canada, whether it's at a brewery in Halifax, by a lake in Alberta, or within a store in Downtown Toronto.
Wherever our gatherings occur, you'll discover amiable outdoor enthusiasts coming together over maps, guidebooks, beverages, and tales of adventure.

## Dependencies
The `WeConnect` Java web app utilizes various dependencies to improve its functionality. These dependencies cover important aspects such as data handling, email communication, security, web development, and testing. 

It uses the Spring Boot starter dependencies, to support rapid development of web applications, and Hibernate Validator, which allows data integrity through bean validation. 

Libraries like Mockito and JUnit are implememented for testing purposes to ensure robustness and reliability. MySQL Connector and Jakarta Validation API supports database connectivity and data validation respectively.

For more information, check out the <u>[full dependency list](docs/dependencies.md)</u>.

## Build/Deployment
The build and deployment process for the `WeConnect` web app involves several steps to ensure smooth execution. Firstly, after configuring the project with necessary dependencies, it's important to build the application using tools like Maven. This process compiles the source code and packages it into an executable JAR file. 

Following that, deploying the application involves deploying this JAR file to a suitable server environment, such as Tomcat or a cloud platform like Heroku. Finally, ensuring proper configuration and monitoring post-deployment allows the application to run seamlessly.

For more information, check out the detailed instructions on <u>[build and deployment](docs/build-and-deployment-guide.md)</u>.

## User Scenario
The WeConnect web app offers various features to users, allowing them to create and explore communities, interact with members, and contribute to events. Users can log in, create communities, explore existing ones, register for events, donate resources or money, and view community donations. Admins can log in, view approved communities, and approve new community requests.

For more information, check out the detailed <u>[user scenarios](docs/user-scenario.md)</u>.
