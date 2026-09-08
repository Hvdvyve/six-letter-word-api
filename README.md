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

Find combinations from the bundled `input.txt`:

```bash
curl -X POST http://localhost:8080/api/combinations \
  -H 'Content-Type: application/json' \
  -d '{"words":["abroad","a","broad"],"targetLength":6}'
```

Response:

```json
{"combinations":["a+broad=abroad"]}
```


Every word supplied to the JSON API must also be present in the bundled
`input.txt`; otherwise the request is rejected.

For example, a request containing an unknown word returns HTTP 400:

```json
{"error":"Words not present in input.txt: unknown"}
```

The file endpoint also uses the bundled `input.txt` and target length six:

```bash
curl -X POST http://localhost:8080/api/file \
  > output.txt
```

Store the same results in the in-memory H2 database:

```bash
curl -X POST http://localhost:8080/api/database
```

Retrieve the stored results:

```bash
curl http://localhost:8080/api/database
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
