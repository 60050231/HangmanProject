package os.hangman.Client;

import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static ObjectOutputStream objectOutput = null;
    private static ObjectInputStream objectInput = null;

    private static final int MAX_TRY = 7;

    private static String hiddenWord = "";
    private static String missedWord = "";
    private static int missedCount = 0;
    private static int isWin = 0;
    private static int isLose = 0;

    private static boolean isContinue = true;

    private static Scanner keyboardInput = new Scanner (System.in);

    public static void main(String[] args){
        Client gameClient = new Client();
        gameClient.run();
    }

    private void run() {
        while (isContinue) {
            try {
                //get the localhost IP address
                InetAddress localhost = InetAddress.getLocalHost();

                //init socket with localhost and port
                int port = 1234;
                Socket socket = new Socket(localhost, port);

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

                System.out.print("\nDo you want to play? (y/n) : ");
                isContinue = toPlayOrNot();
                if (isContinue) {
                    System.out.println("\n ============================= \n");
                    while (true) {
                        getStatus();
                        if (isWin == 1 || isLose == 1) break;
                        System.out.println("You have " + (MAX_TRY - missedCount) + " chance left");
                        System.out.println("Word : " + hiddenWord);
                        System.out.println("Missed : " + missedWord);
                        sendObject(inputGuess());
                        System.out.println("\n =============================\n");
                    }

                    if (isWin == 1){
                        System.out.println( "You WIN!! :D" );
                        System.out.println( "The word is " + hiddenWord );
                    }
                    else {
                        String answer = getAnswer();
                        System.out.println("You LOSE!! :p");
                        System.out.println("The word is " + answer);
                    }
                }
                System.out.println("\nClient - Server Hangman game is stopping...\n");
                objectOutput.writeObject("EXIT");
                objectInput.close();
                objectOutput.close();
            }
            catch(IOException | ClassNotFoundException err){
                err.printStackTrace();
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
            return (String) objectInput.readObject();
        } catch (IOException | ClassNotFoundException err) {
            err.printStackTrace();
        }
        return "";
    }

    private boolean toPlayOrNot() {
        String userInput;
        while (true) {
            try {
                userInput = keyboardInput.nextLine();
            } catch (Exception ex) {
                break;
            }
            userInput = userInput.trim().toLowerCase();
            if (userInput.equals("y")) {
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
