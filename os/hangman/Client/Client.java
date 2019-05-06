package os.hangman.Client;

import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static int port = 1234;

    private static Socket socket = null;
    private static ObjectOutputStream objectOutput = null;
    private static ObjectInputStream objectInput = null;

    public static void main(String[] args){
        Client gameClient = new Client();
        gameClient.run();
    }

    public void run() {
        while (true) {
            try {
                //get the localhost IP address
                InetAddress host = InetAddress.getLocalHost();

                //init socket with localhost and port
                socket = new Socket("127.0.0.1", port);

                //sending request new port
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject("Client request new port");

                //get new port
                objectInput = new ObjectInputStream(socket.getInputStream());
                String newPort = (String) objectInput.readObject();
                System.out.println("new Port: " + newPort);

                //switch to new port
                socket = new Socket("127.0.0.1", Integer.parseInt(newPort));
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                //sendObject("Sent to thread");
                objectInput = new ObjectInputStream(socket.getInputStream());
                //System.out.println((String) objectInput.readObject());

                //just some announcement
                System.out.println("Client - Server Hangman game is starting...");
                System.out.println("Connecting to "+ host + " Port : " + newPort);

                while (true) {
                    if (toPlayOrNot()) {}
                    else break;
                }

                //toPlayOrNotToPlay();

                System.out.println("Client - Server Hangman game is stopping...");

                //close resources
                objectInput.close();
                objectOutput.close();
                break;
            }
            catch(IOException | ClassNotFoundException err){
                System.out.print(err);
            }
        }

    }

    protected boolean toPlayOrNot() {
        Scanner keyboardInput = new Scanner (System.in);
        String userInput = null;
        while (true) {
            System.out.print("Do you want to play? (y/n) ");
            try {
                userInput = keyboardInput.nextLine();
            } catch (Exception ex) {
                stopGame();
                break;
            }
            userInput = userInput.trim().toLowerCase();
            if (userInput == null) {
                stopGame();
                break;
            } else if (userInput.equals("y")) {
                startGame();
                return true;
            } else if (userInput.equals("n")) {
                stopGame();
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
        return false;
    }

    protected void startGame() {
        sendObject("Start");
    }

    protected void stopGame() {
        sendObject("Exit");
    }

    protected void sendObject(String request){
        try {
            objectOutput.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
