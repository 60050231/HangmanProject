#using openjdk 10 as base image
FROM openjdk:10
#copying files from current directory from host to docker images
COPY . .
#Compiling Client.java
RUN javac *.java
#executing Client
CMD ["java", "Client"]