# About the project

In this project...

# Local Execution Guide

In order to run and test the project, several steps are needed to be takne. Considering that our project, offers two different ways to run, being docker and deployed on azure, here is the guide for both.

For either solution, the first step to take is to clone the github repository using the following command:

```bash
git clone https://github.com/pedroLopes5555/tukano-project.git
```

## Docker

Starting off, in order to run the project we need to compile it and to do this, we need to access the file pom.xml directory.

```bash
cd scc2425-tukano/
```

When in the desired directory, we just have to compile using the following command.

```bash
mvn clean compile package
```

- if the machine does not have maven installed, https://maven.apache.org/index.html

After compiling the solution, we need to run the following command in the command line in order to create the docker container.

```bash
sudo docker run -d --net=host --name <container-name> smduarte/tomcat10
```
- if the machine does not have docker installed, https://www.docker.com/products/docker-desktop/

When the docker container is running, you can visit this page, http://127.0.0.1:8080/manager/html, in order to check if the services are available.

Considering that our solution was implemented using hibernate for cache and postgreSQL, we need to use the following commands, to copy the configuration files into the tomcat docker directory.

```bash
docker cp /path/to/hibernate.cfg.xml <container-name>:/usr/local/tomcat/hibernate.cfg.xml
```

```bash
docker cp /path/to/redisson-config.yaml <container-name>:/usr/local/tomcat/redisson-config.yaml
```

When all these steps are complete, the final part requires deploying a .war file in this page, http://127.0.0.1:8080/manager/html, following the steps that the next images show.
In this page, you will find a tab like the following image.

![War file deploy](/images/war_deploy.png)
Firstly click on Browse and travel to the project directory. When inside the project directory find the target folder and inside will be the following file, tukano-1.war.
After choosing this file click on Deploy and the server will start running.

![Server available](/images/server.png)
In this image as it is possible to see, the tukano-1 server is now available and running.

### Setting up variables

During the development, we used environment tables and hidded some others in order to keep the privacy of the keys.

In files hibernate and redisson, you will find some variables with the value "###", replace this by the desired values in order
to allow the connection.

In order to make the webapp work, on the docker and on the webapp environment there are 2 variables that need to be set being.:
- storageConnectionString: which is the connection string to blobs
- URI: which is the URI of the machine running the docker server
