#!bin/sh

docker-compose up -d

echo "Приложение разворачивается, ожидайте (может быть, много ожидайте...)"
while ! curl --fail --silent --head http://localhost:9000/api; do
  sleep 1
done

start http://localhost:9000/api/swagger-ui