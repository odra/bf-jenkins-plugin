# Sample Jenkins Plugin

Sample jenkins plugin project 

## Instructions


### Running tests

```
mvn verify -X
```

### Development server

```
set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n && mvn hpi:run
```


### Generating a relase file

```
mvn package
```