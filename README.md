# WeatherApp-Springboot

This project name is Live Weatherapp, as application name suggests it will keep monitoring live weather and produce result for every few minutes. As soon as the user login to the application user can see the city details, current city weather and 5 days forecast details. City weather details will be updating for every 8 minutes and city forecast details will be refreshing for every 50 minutes.

Now lets coming to the technologies and microservices used in this application.

**TechStack and Frameworks :**

**Technologies :**

Core Java, Spring boot Web, Rest API, Spring boot data JPA and MongoDB, Spring boot redis cache, Spring boot security, Spring boot cloud gateway, Spring boot netflix eureka server and eureka discovery client.

**Databases :** Postgresql and MongoDB

**Presentation layer :**  HTML5,CSS,Bootrap5,Jquery and thymeleaf for template engine

**Microservices :**

In total there are 4 microservices implemented in backend those are,

1. **CITY-AND-WEATHER-RESTAPI-SERVICE** - This microservice basically connected to the OpenWeather external API service to get current weather, city details and Forecast data.
2. **WEATHERAPP-SERVICE** - In this microservice all UI design related stuff have been implemented, basically it renders HTML pages and this service uses open feign client concept to communicate with CITY-AND-WEATHER-RESTAPI-SERVICE microservice to fetch data and display data in HTML page.
3. **OTP-SERVICE** - OTP service is used for resetting forgotten password, In order to reset password user must enter an OTP that they will get it via registered email ID. RabbitMQ concept has been implemented to make bridge between WEATHERAPP-SERVICE and OTP-SERVICE.
4. **API-GATEWAY** - API gateway basically routes the requests to eureka server, once the eureka service identifies the service is available in its bucket it send respond back to API gateway. Main purpose of using gateway is not to expose port number. It follows abstraction. Every service has its unique port number but all requests will be routed from API Gateway service port number.

**Netflix Eureka Server and Discovery client :**

Eureka server has used in this project, Server which helps to register all microservices as a discovery client in it so the API gateway will route request based on the path and registered service details.

**Let's have glimpse of this application**

**Registration and Main login page**

![Screenshot from 2022-09-27 16-54-08](https://user-images.githubusercontent.com/112934529/192523698-40d6a3de-a72f-4e17-a9ec-417a39ead6fa.png)

**Below screenshots are showing the procedure to reset forgotten password**

![Screenshot from 2022-09-27 16-54-31](https://user-images.githubusercontent.com/112934529/192523906-487f4c22-0398-4ddd-b95e-d449f99d4cfa.png)

![Screenshot from 2022-09-27 16-56-41](https://user-images.githubusercontent.com/112934529/192524144-cd230437-94a5-487e-a336-6dce4d9412d6.png)

![Screenshot from 2022-09-27 16-56-48](https://user-images.githubusercontent.com/112934529/192524193-4d5c19a6-92ad-438d-8410-b4b8d1358b73.png)

![Screenshot from 2022-09-27 16-57-00](https://user-images.githubusercontent.com/112934529/192524227-2eefe6a8-c31f-4d83-9b48-e8f0aa87f94d.png)

**If user enters invalid OTP details**

![Screenshot from 2022-09-27 16-57-44](https://user-images.githubusercontent.com/112934529/192524379-a4d4b8b9-5914-4835-b82c-091b6fa7100d.png)

**If unauthorized person tries to reset password**

![Screenshot from 2022-09-27 16-57-13](https://user-images.githubusercontent.com/112934529/192524446-17d1ad0d-d555-4f09-aa54-934787fd3eed.png)

**Weatherapp dashboard**

![Screenshot from 2022-09-27 17-01-20](https://user-images.githubusercontent.com/112934529/192524665-02075457-a7af-4fd2-8705-da2b1248d81b.png)

**Current weather model box**

![Screenshot from 2022-09-27 17-01-27](https://user-images.githubusercontent.com/112934529/192524894-4d5a791d-3615-4e52-a4b9-6b2d66fcbe8a.png)

**5 days forecast data**

![Screenshot from 2022-09-27 17-01-45](https://user-images.githubusercontent.com/112934529/192524924-d978459d-38e3-49fe-822f-185129727093.png)

![Screenshot from 2022-09-27 17-02-20](https://user-images.githubusercontent.com/112934529/192524942-c5c4e052-d5ea-4d3f-afbb-305369047992.png)

**Spring eureka server dashboard where we can monitor all microservice status**

![Screenshot from 2022-09-27 17-02-27](https://user-images.githubusercontent.com/112934529/192525159-3e9065f4-01e0-4ac8-b0e6-5813006e3dc5.png)

**RabbitMQ dashboard**

![Screenshot from 2022-09-27 17-20-44](https://user-images.githubusercontent.com/112934529/192525364-45b04c47-55dc-49a2-9bff-36cf92d54d61.png)

**PostgreSQL schemas**

![Screenshot from 2022-09-27 17-02-37](https://user-images.githubusercontent.com/112934529/192525216-ba833b96-ceee-4d68-8f72-25ff50b26909.png)

**MongoDB documents**

![Screenshot from 2022-09-27 17-03-02](https://user-images.githubusercontent.com/112934529/192525265-941b5daa-b562-4ae6-b8d2-bb890d32987c.png)

**Spring boot redis cache data**

![Screenshot from 2022-09-27 17-03-31](https://user-images.githubusercontent.com/112934529/192525312-0cfdb393-fcf3-420a-9162-428476f78bf4.png)

**Resources**

In this project, used Openweather external API's to fetch all weather details, It is free of cost with enough number of features.

60 calls/minute
1,000,000 calls/month

Link : https://openweathermap.org/

**Features may expect in future :**

1. Will try to deploy it in AWS cloud so that it will be readily availble to all users to utilize.
2. Oauth2 client login reduces signup process also more secure

**HappY Coding..!!**
