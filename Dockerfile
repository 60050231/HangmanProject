FROM openjdk:latest

WORKDIR /Hangman

COPY . /Hangman

RUN    javac MultiThread.java && \
    javac Server.java && \
    javac Client.java
EXPOSE    6789
CMD    ["java", "Server"]