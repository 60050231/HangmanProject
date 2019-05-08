FROM openjdk:latest

COPY . .

RUN    javac MultiThread.java && \
    javac Server.java

EXPOSE    1234
CMD    ["java", "Server"]