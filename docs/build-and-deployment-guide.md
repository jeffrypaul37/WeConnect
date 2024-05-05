## Build and Deployment 

  

###  Building the application 

Once you have installed all the dependencies, you can proceed with building the application. 

  

**Backend Application** 

  

For the backend Spring Boot application, navigate to the project directory and run the following command to build the application: 

```java 

mvn clean package 

``` 

  

*Alternatively*, 

  

To build the Springboot Application in your CI/CD pipeline, please follow the below steps: 

  

- Create a new job in your Gitlab/GitHub CI/CD pipeline and define it as a ```build``` stage. 

- Choose an appropriate Docker image to run the job. In this case, we can use the ```maven:latest``` image to build the Spring Boot application. 

- In a section called ```script```, change the directory to your project directory using ```cd``` command. 

- Now, use ```mvn clean package``` command to build the application. 

- Define the remaining stages of the pipeline (```test``` stage in this project) and ```quality```, ```publish``` stages as the other stages. 

- Define the ```artifacts:``` that needs to be included in the CI/CD pipeline artifacts. For this project, the .jar file is stored as an artifact for our Project. 

  

These steps can be used to build the backend Spring Boot application in your CI/CD pipeline and store the build artifacts as pipeline artifacts. 

  

```java 

$CI_PROJECT_DIR/target/*.jar

``` 

### Deploying The Application 

  

The following guide provides steps can be used to deploy the Project Application using GitLab CI/CD Pipeline and Docker. Docker needs to be installed in the virtual machine. The deployment process uses Docker containers and SSH to deploy your project to remote server. 

  

#### Prerequisites 

  

Before you begin, ensure you have the following to successful complete the process: 

  

* GitLab repository where CI/CD is configured. 

* A remote server where you need to deploy. 

* Docker installed and configured on local and remote server. 

* SSH access to remote server with appropriate permission. 

  

#### Configure SSH Keys 

Perform following steps to obtain private rsa key : 

```java 

cat ~/.ssh/id_rsa 

``` 

Copy the output of this command and keep it safely. 

  

#### Set following variables in GitLab CI/CD Pipeline settings: 

  

```ID_RSA``` : The Private SSH key. 

```SERVER_USER``` : Your username for SSH authentication on the remote server. 

```SERVER_IP``` : IP address or hostname of the remote server. 

```DOCKER_HUB_USER``` : Your Docker Hub username. 

```DOCKER_HUB_PWD``` : Your Docker Hub password. 

  

  

### Create Pipeline 

The next step is to create a CI/CD pipeline that will automate the deployment process. 

  

* Set the Docker image to use for running the job to the latest version of Docker 

  

```java 

image:alpine:latest 

``` 

* Change the permission to 600 for the `ID_RSA` 

* Provide commands that needs to be executed by job . 

* Connect to the server using SSH, and stops the running Docker container if it exists. The `StrictHostKeyChecking` option is set to no to disable host key checking, which is useful when connecting to a new host. 

* Execute the below command to connect to Docker : 

```java 

docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PWD docker.io 

``` 

*  This connects to the docker using credentials stored in the variables that are defined in CI/CD settings. 

* Pull the docker image from the docker registry  

``` 

docker pull docker.io/{repository_name}/{Image_Name}:$CI_COMMIT_SHORT_SHA 

``` 

* Remove the Docker container if it exists. The `StrictHostKeyChecking` option is set to no to disable host key checking. 

``` 

docker container rm -f Group04 || true 

``` 

* In the script, login to the docker using the `$DOCKER_HUB_USER` and `$DOCKER_HUB_PWD` and the name of the docker service. 

* Build a Docker image for the backend application on the server. It changes to the directory containing the Dockerfile, builds the image. 

```java 

docker  build  -t  docker.io/weconnect056/weconnect:$CI_COMMIT_SHORT_SHA 

``` 

* Run the Docker container for the backend application on the server. It starts the container in detached mode (-d option), names it as `Group04`, maps `port 8080` of the container to `port 8080` of the host (`-p` option), and uses the image that was built earlier. 

```java 

docker run -d -p 8080:8080 --name Group04 docker.io/{repository_name}/{image_name}:$CI_COMMIT_SHORT_SHA 

``` 

  

Now if you go to your VM host, then you can see your application up and running. 

  

By following the above describe steps, you should be able to build and deploy the web application using GitLab CI/CD.  

  

For more information related to CI/CD, please refer to: <u>https://docs.gitlab.com/ee/ci/</u>


Back to <u>[Home Page](../README.md)</u>.