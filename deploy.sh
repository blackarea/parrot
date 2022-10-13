#!/bin/bash

REPOSITORY=/home/ec2-user/parrot/project
PROJECT_NAME=parrot

echo "> Build 파일 복사"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "> 현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 2
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
echo "$REPOSITORY : $JAR_NAME"

chmod +x $JAR_NAME

nohup java -jar \
        -Dspring.config.location=classpath:/application.properties,/home/ec2-user/parrot/application-db.properties,/home/ec2-user/parrot/application-mail.properties,classpath:/application-real.properties \
        -Dspring.profiles.active=real \
        $REPOSITORY/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
