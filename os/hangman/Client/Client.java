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

    private static final int MAX_TRY = 7;

    private static String hiddenWord = "";
    private static String missedWord = "";
    private static int missedCount = 0;
    private static int isWin = 0;
    private static int isLose = 0;

    private static Scanner keyboardInput = new Scanner (System.in);

    public static void main(String[] args){
        Client gameClient = new Client();
        gameClient.run();
    }

    public void run() {
        while (true) {
            try {
                //get the localhost IP address
                InetAddress localhost = InetAddress.getLocalHost();

                //init socket with localhost and port
                socket = new Socket(localhost, port);

                //sending request new port
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject("Client request new port");

                //get new port
                objectInput = new ObjectInputStream(socket.getInputStream());
                String newPort = (String) objectInput.readObject();
                System.out.println("new Port: " + newPort);

                //switch to new port
                socket = new Socket(localhost, Integer.parseInt(newPort));
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectInput = new ObjectInputStream(socket.getInputStream());

                //just some announcement
                System.out.println("Client - Server Hangman game is starting...");
                System.out.println("Connecting to "+ localhost + " Port : " + newPort);

                getStatus();
                System.out.print("Do you want to play? (y/n) : ");
                if (toPlayOrNot()) {
                    objectOutput.writeObject("START");
                    int round = 1;
                    while (true) {
                        System.out.println("\n ========== ROUND " + round + " ========== \n");
                        while (isWin == 0 && isLose == 0) {
                            getStatus();
                            System.out.println("You have " + (MAX_TRY - missedCount) + " chance left");
                            System.out.println("Word : " + hiddenWord);
                            System.out.println("Missed : " + missedWord);
                            sendObject("guess:" + inputGuess());
                            System.out.println("\n =============================\n");
                            getStatus();
                        }
                        if (isWin == 1){
                            System.out.println( "You WIN!! :D" );
                            System.out.println( "The word is..." + hiddenWord );
                        }
                        else if (isLose == 1) {
                            String answer = getAnswer();
                            System.out.println("You LOSE!! :p");
                            System.out.println("The word is..." + answer);
                        }
                        /*
                        System.out.print("Do you want to play again? (y/n) : ");
                        if (toPlayOrNot()) {
                            round++;
                        }
                        else break;
                        */
                        break;
                    }
                }

                System.out.println("Client - Server Hangman game is stopping...");
                objectInput.close();
                objectOutput.close();
                break;
            }
            catch(IOException | ClassNotFoundException err){
                System.out.print(err);
            }
        }
    }

    private static void getStatus(){
        try {
            objectOutput.writeObject("STATUS");
            String input = (String) objectInput.readObject();
            String []detail = input.split("#");
            hiddenWord = detail[0];
            missedWord = detail[1];
            missedCount = Integer.parseInt(detail[2]);
            isWin = Integer.parseInt(detail[3]);
            isLose = Integer.parseInt(detail[4]);

        } catch (IOException | ClassNotFoundException err) {
            err.printStackTrace();
        }
    }

    private static String getAnswer(){
        try {
            objectOutput.writeObject("ANSWER");
            String input = (String) objectInput.readObject();
            return input;
        } catch (IOException | ClassNotFoundException err) {
            err.printStackTrace();
        }
        return "";
    }

    private boolean toPlayOrNot() {
        String userInput = null;
        while (true) {
            try {
                userInput = keyboardInput.nextLine();
            } catch (Exception ex) {
                break;
            }
            userInput = userInput.trim().toLowerCase();
            if (userInput == null) {
                break;
            } else if (userInput.equals("y")) {
                return true;
            } else if (userInput.equals("n")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
        return false;
    }

    private String inputGuess () {
        String guess;
        while (true) {
            System.out.print("Guess : ");
            guess = keyboardInput.nextLine().trim().toLowerCase();
            if (guess.length() > 1) {
                System.out.println("Invalid input.");
            }
            else if (guess.equals("")) {
                System.out.println("Invalid input.");
            }
            else break;
        }
        return guess;
    }

    private void sendObject(String request){
        try {
            objectOutput.writeObject(request);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}