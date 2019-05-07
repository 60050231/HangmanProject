package os.hangman.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadRespond implements Runnable {

    private ServerSocket server;
    private int port;

    public MultiThreadRespond(int port) {
        this.port = port;
        try {
            server = new ServerSocket(port);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public void run() {
        int MAX_TRY = 7;
        ObjectOutputStream objectOutput;
        ObjectInputStream objectInput;

        String[] word = {
                "Batman",
                "Deadpool"
        };
        
        int randRange = (int)(Math.random() * word.length);
        String randWord = word[randRange].toLowerCase();
        char[] hiddenWord = new char[randWord.length()];
        char[] missedWord = new char[7];

        int missedCount = 0;
        int hiddenLeft = 1;
        int isWin = 0;
        int isLose = 0;

        for (int i = 0; i < randWord.length(); i++) {
            if (randWord.charAt(i) == ' ') {
                hiddenWord[i] = ' ';
            } else {
                hiddenWord[i] = '_';
            }
        }

        try {
            Socket socket = server.accept();
            objectInput = new ObjectInputStream(socket.getInputStream());
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                String action = (String) objectInput.readObject();
                if (hiddenLeft == 0) {
                    isWin = 1;
                }
                if (missedCount == MAX_TRY) {
                    isLose = 1;
                }
                if (action.equals("STATUS")) {
                    System.out.println("[Thread] Command Received : " + action);
                    String output = new String(hiddenWord) + "#" + new String(missedWord) + "#" + missedCount + "#" + isWin + "#" + isLose;
                    System.out.println(output);
                    objectOutput.writeObject(output);
                } else if (action.length() == 1) {
                    System.out.println("[Thread] Guess Received : " + action);
                    char userGuess = action.charAt(0);

                    boolean letterFound = false;
                    for (int i = 0; i < randWord.length(); i++) {
                        if (userGuess == randWord.charAt(i)) {
                            hiddenWord[i] = randWord.charAt(i);
                            letterFound = true;
                        }
                    }

                    if (!letterFound) {
                        missedWord[missedCount] = userGuess;
                        missedCount++;
                    }

                    hiddenLeft = randWord.length();
                    for (int i = 0; i < randWord.length(); i++) {
                        if ('_' != hiddenWord[i])
                            hiddenLeft--;
                    }
                } else if (action.equals("ANSWER")) {
                    System.out.println("[Thread] Command Received : " + action);
                    objectOutput.writeObject(randWord);
                    System.out.println(randWord);
                } else if (action.equals("EXIT")) {
                    System.out.println("[Thread] Command Received : " + action);
                    objectInput.close();
                    objectOutput.close();
                    socket.close();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException err) {
            err.printStackTrace();
        }
    }
}