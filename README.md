# Six Letter Words API

Spring Boot 3 / Java 21 application that finds every way to split a dictionary
word into two or more dictionary words. The default target length is six, but
the JSON API accepts another target length.

## Run locally

```bash
mvn spring-boot:run
```

The application listens on `http://localhost:8080`.

## Run with Docker

Docker is the only prerequisite:

```bash
docker compose up --build
```

The application listens on `http://localhost:8080`. Stop it with:

```bash
docker compose down
```

## API

Find combinations from JSON:

```bash
curl -X POST http://localhost:8080/api/combinations \
  -H 'Content-Type: application/json' \
  -d '{"words":["foobar","fo","obar"],"targetLength":6}'
```

Response:

```json
{"combinations":["fo+obar=foobar"]}
```

The file endpoint accepts one word per line and uses target length six:

```bash
curl -X POST http://localhost:8080/api/file \
  -H 'Content-Type: text/plain' \
  --data-binary @input.txt > output.txt
```

## Design

The domain algorithm is isolated behind `CombinationFinder`, so the input
source and HTTP layer can change independently. It uses prefix matching and
depth-first search, supports any number of parts, removes duplicate dictionary
entries, and returns deterministic output.

Run the unit tests with:

```bash
mvn test
```
