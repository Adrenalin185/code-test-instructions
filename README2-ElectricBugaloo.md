# URL Service
The url service is an end-to-end solution for conversion and storage of URLs

## Requirements

- Docker
- Java
- Node

## Setup 
The service is designed to be spun up inside of docker containers.

- url-service (Java backend)
- url-ui-service (React frontend)
- MYSQL_TPXTEST (MySQL database)

from the code-test-instruction directory run the following command

- Docker Compose up --build

this should build the containers and start them inside of Docker.

## Operation

use the following link once the docker containers have successfully spun up to access the UI

- http://localhost:3000/

follow the steps of 'post' to create a new short url.
open the url from its alias and delete it based on the same alias.
the table at the bottom is to keep track of the urls.