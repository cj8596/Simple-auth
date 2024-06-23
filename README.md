# Simple-auth
created auth apis usign springboot to signin and signup users


/signup

curl --location 'http://localhost:8085/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"tony",
    "username":"USER",
    "email":"tony@gmail.com",
    "password":"abcd"
}'

/signin

curl --location 'http://localhost:8085/auth/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
    "usernameOrEmail":"tony@gmai.com",
    "password":"abcd"
}'
