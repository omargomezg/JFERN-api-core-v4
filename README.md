# JFERN-api-core-v4

La version actual de mongo es la 4.4, para local ejecutar
```
docker run -d \
  --name mongodb44 \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  mongo:4.4
```