#using openjdk 10 as base image
FROM openjdk:10
#copying files from current directory from host to docker images
COPY . .
#Compiling Server.java
RUN javac *.java
#executing Server
CMD ["java", "Server"]