#!/d/IdeaWorkSpace sh
mvn clean package -Dmaven.test.skip=true -U

docker build -t hub.c.163.com/sian/translate .

docker push hub.c.163.com/sian/translate


