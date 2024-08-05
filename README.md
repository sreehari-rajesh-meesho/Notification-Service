# Notification-Service Documentation

**Overview**

The notification service is used for passing messages to a particular user via mobile messaging (SMS). It is operated via an api end point (v1/sms/send) which gets a phone number and a text message and sends an SMS with the text message to the phone number which is provided as input

Structure of SMS request for the SMS endpoint

```json
{
  "phoneNumber": "",
  "message": ""
}
```

Components of Notification-Service

- Notification-Service-Server (Spring Boot Application)
- Redis Docker Container (Redis Server)
- ElasticSearch Docker Container (ElasticSearch Server)
- Kafka Server
- MySQL Server (message_db)

# Data Objects and SQL Schemas

**MySQL DB (Message DB) Schema for storing Messages**

```
status integer,
created_at datetime(6),
id bigint not null,
updated_at datetime(6),
failure_code varchar(255),
failure_comments varchar(255),
message varchar(255),
phone_number varchar(255),
primary key (id)
```

**Redis Cache**

```
String phoneNumber(Key)
```

**ElasticSearch Object Store Class**

```java
public class SendSMSDetails {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "phone_number")
    private String phoneNumber;

    @Field(type = FieldType.Text, name = "message")
    private String message;

    @Field(type = FieldType.Integer, name = "status")
    private Integer status;

    @Field(type = FieldType.Text, name = "failure_code")
    private String failureCode;

    @Field(type = FieldType.Text, name="failure_comments")
    private String failureComments;

    @Field(type=FieldType.Date, name="created_at")
    private Long created;

    @Field(type = FieldType.Date, name = "updated_at")
    private Long updated;
}
```

# System Design

<img src="SystemDesign.png>

# Program Design

<img src="Notification-Service.svg">

# Request Response Structures

**SMS Request Structure**

```java
public class SMSRequest {
    private String phoneNumber;
    private String message;
}
```

**Request Number List(BlackList and WhiteList PhoneNumbers)**

```java
public class RequestNumberList {
    private List<String> phoneNumbers;
}
```

**Third Party SMS Request Object**

```java
public class ThirdPartyRequest {
    private String deliverychannel;
    private Channel channels;
    private List<DestinationObject> destination;
}

// Destination Object
public class DestinationObject {
        private List<String> msisdn;
        private String correlationId;
}

// Channel
public class Channel {
    private SMSObject sms;
}

// SMS Object
public class SMSObject {
    private String text;
}
```

**Response Object**

```java
public class Response <T1,T2> {
    private T1 data;
    private T2 error;
}
```

**Response Data Object**

```java
public class ResponseDataObject {
    private Long requestId;
    private String comments;
}
```

**Response Error Object**

```java
public class ResponseErrorObject {
    private String code;
    private String message;
}
```

# API Endpoints

**Send SMS Endpoint**

**POST** v1/sms/send

This end point is used to send an SMS to a particular number

**BlackList Numbers**

**POST** v1/blacklist

This end point is used to blacklist a particular set of numbers

**WhiteList Numbers**

**DELETE** v1/blacklist

This end point is used to whitelist a particular set of numbers

**Get SMS as per ID**

**GET** sms/{request_id}

This end point is used to get the SMS associated with a particular ID from the db

**Get SMS Between start and end time with pagination**

**GET** between/{page}/{size}?start=x&end=y

This end point is used to get an SMS with sending time between start and end

**Get SMS with message containing a given text with pagination**

**GET** between/{page}/{size}?start=x&end=y

This end point is used to get an SMS with message containing a given text

# Important Tools to be Installed

**MySQL**
Link to installation:

https://dev.mysql.com/doc/refman/8.4/en/macos-installation.html

**Docker**

Link to installation:

https://docs.docker.com/desktop/install/mac-install/

# Setting Up Containers

**redis-docker-compose.yml**

```docker
version: "3.3"
services:
  redis:
    image: redis:6.0.7
    container_name: redis
    restart: always
    volumes:
      - redis_volume_data:/data
    ports:
      - 6379:6379

volumes:
  redis_volume_data:
```

**elastic-docker-compose.yml**

```docker
version: '3'
services:
  elasticsearch:
    image: elasticsearch:8.14.1
    ports:
      - 9200:9200
      - 9300:9300
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
```

**zipkin-docker-compose.yml**

```docker
version: '2.4'
services:
  zipkin:
    image: ghcr.io/openzipkin/zipkin-slim:${TAG:-latest}
    container_name: zipkin
    environment:
      - STORAGE_TYPE=mem
    ports:
      - 9411:9411
```

You can start each container by running

```bash
$ docker compose -f docker-compose-file-name up -d
```

# Setting Up MySQL and Kafka

# MySQL

```bash
$ brew services start mysql
```

# Kafka

Configure the topic “send-sms” in Spring Application. Messages are extracted from this Kafka Topic.

**Why Kafka ?**

Kafka transmits data from producers to consumers with very low latency and high throughput.

# Different Phases of Notification Service

**Message Ingestion Phase**

Message Is obtained from the SMS Request and a new Message Object is created

The Message Object is then injected into the database (message_db) table Message;

Message Ingested to DB (message_db) gives a message_id which is ingested into the kafka topic “send-sms”.

**Retrieval and Update Phase**

The Message Consumer picks up the ingested message id and retrieves the message record from the Message table. The redis cache db is checked to see if the phone number associated with the message is blacklisted. If it is not blacklisted an SMS Request is sent to a third party API which returns a response object

The response Object is of the form

```java
public class ThirdPartyResponse {
        private String code;
        private String description;
        private String transid;
}
```

The message is then updated in the DB and Indexed in elastic search

A controller object is used to access the sms-requests data which uses different functions for accessing the data
