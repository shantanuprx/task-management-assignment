mvn clean install -Dmaven.test.skip=true & docker build . -t gryffindor937/apigatewayservicedeployment:1.0 & docker push gryffindor937/apigatewayservicedeployment:1.0  & kubectl delete deployment apigatewayservicedeployment & kubectl apply -f deployment.yml