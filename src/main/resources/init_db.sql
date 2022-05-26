CREATE DATABASE IF NOT EXISTS ${MYSQL_DB_NAME};

CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';
GRANT ALL PRIVILEGES ON ${MYSQL_DB_NAME}.* TO '${MYSQL_USER}'@'%';

insert ignore into weather_station (host, port, path, protocol, location) values ('192.168.0.137', 80, "/", "http", "balcony");