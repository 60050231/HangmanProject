package os.hangman.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    protected BufferedReader consoleReader;

    public static void main(String[] args){
        Client gameClient = new Client();
        gameClient.run();
    }

    protected void run() {
        System.out.println("Client-Server Hangman game is starting...");
        //toPlayOrNotToPlay();
        //sendObject("TEST");
        System.out.println("Client-Server Hangman game is stopping...");
    }

    protected void toPlayOrNotToPlay() {
        String userInput = null;
        while (true) {
            System.out.print("Do you want to play? (y/n) ");
            try {
                userInput = this.consoleReader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
                stopGame();
                break;
            }
            userInput = userInput.trim().toLowerCase();
            if (userInput == null) {
                stopGame();
                break;
            } else if (userInput.equals("y")) {
                startGame();
            } else if (userInput.equals("n")) {
                stopGame();
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    protected void startGame() {
        sendObject("START");
    }

    protected void stopGame() {
        sendObject("STOP");
    }

    protected void sendObject(String request){
        try {
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            int port = 9876;
            socket = new Socket("127.0.0.1", port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            oos.writeObject(request);
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            //port = Integer.parseInt(message);
            System.out.println("Message : " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(1000);
            //get the localhost IP address, if server is running on some other IP, you need to use that
        }
        catch(IOException | ClassNotFoundException | InterruptedException err){
            System.out.print(err);
        }
    }
}
