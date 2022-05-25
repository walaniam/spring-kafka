CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'mysecret';
GRANT ALL PRIVILEGES ON weatherdb.* TO 'app_user'@'%';