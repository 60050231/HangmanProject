package os.hangman.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadRespond implements Runnable{

    private ServerSocket server;
    private int port;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;

    private static int MAX_TRY = 7;

    public MultiThreadRespond(int port){
        this.port = port;
        try{
            server = new ServerSocket(port);
        }catch(Exception e){

        }

    }

    @Override
    public void run(){
        String []word = {"Batman", "Deadpool"};
        int randRange = (int) (Math.random() * word.length);
        String randWord = word[randRange].toLowerCase();

        char []hiddenWord = new char[randWord.length()];
        char []missedWord = new char[7];

        int missedCount = 0;
        int hiddenLeft = 1;
        int isWin = 0;
        int isLose = 0;

        //make hidden word
        for (int i = 0; i < randWord.length(); i++) {
            if (randWord.charAt(i) == ' ') {
                hiddenWord[i] = ' ';
            } else {
                hiddenWord[i] = '_';
            }
        }

        while(true){
            try{
                Socket socket = server.accept();
                objectInput = new ObjectInputStream(socket.getInputStream());
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                while (true) {
                    //System.out.println("\nHidden Word: ");
                    String action = (String) objectInput.readObject();
                    if (hiddenLeft == 0) {
                        isWin = 1;
                    }
                    if (missedCount == MAX_TRY){
                        isLose = 1;
                    }
                    if (action.equals("START") || action.equals("STATUS")) {
                        System.out.println("[Thread] Command Received : " + action);
                        String output = new String(hiddenWord) + "#" + new String(missedWord) + "#" + missedCount + "#" + isWin + "#" + isLose;
                        System.out.println(output);
                        objectOutput.writeObject(output);
                    }
                    else if (action.equals("ANSWER")) {
                        System.out.println("[Thread] Command Received : " + action);
                        objectOutput.writeObject(randWord);
                    }
                    else if (action.substring(0,6).equals("guess:")) {
                        System.out.println("[Thread] Guess Received : " + action);
                        /// get Client input
                        char userGuess = action.substring(6, 7).charAt(0);

                        /// Game Logical
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
                    }
                    else {
                        System.out.println("[Thread] Command Received : " + action);
                        objectInput.close();
                        objectOutput.close();
                        socket.close();
                        break;
                    }
                }
            } catch(Exception err){
                err.printStackTrace();
            }
        }
    }
}
