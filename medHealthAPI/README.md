# MedHealth

## Access DataBase

`PGPASSWORD=medpass psql -h localhost -p 5400 -U meduser -d medhealth`

`docker compose exec db psql -U meduser -d medhealth`

## Start Database with Docker

`docker compose up -d`

## Stop Database with Docker

`docker compose down`

## Run API

`./mvnw spring-boot:run`
