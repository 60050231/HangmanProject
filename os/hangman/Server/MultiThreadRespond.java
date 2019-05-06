package os.hangman.Server;

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
        int missedCount = 0;
        char[] missedWord = new char[7];
        boolean letterFound, solved = false;

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
                    System.out.println("\nHidden Word: ");
                    String action = (String) objectInput.readObject();
                    if (action.equals("START") || action.equals("STATUS")) {
                        System.out.println("[Thread] Message Received : " + action);
                        String output = new String(hiddenWord) + "#" + new String(missedWord) + "#" + missedCount + "#" + isWin + "#" + isLose;
                        System.out.println(output);
                        objectOutput.writeObject(output);
                    } else if (action.substring(0,6).equals("guess:")) {
                        System.out.println("[Thread] Guess Received : " + action);
                        /// get Client input
                        String userGuess = action.substring(6, 7);
                        System.out.print("\nGuess: " + userGuess);

                        /// Game Logical
                        letterFound = false;
                        for (int i = 0; i < randWord.length(); i++) {
                            if (userGuess.toLowerCase().charAt(0) == randWord.toLowerCase().charAt(i)) {
                                hiddenWord[i] = randWord.charAt(i);
                                letterFound = true;
                            }
                        }
                        if (!letterFound) {
                            missedWord[missedCount] = userGuess.charAt(0);
                            missedCount++;
                        }

                        int hiddenLeft = 0;
                        for (int i = 0; i < randWord.length(); i++) {
                            if ('_' == hiddenWord[i])
                                hiddenLeft++;
                        }
                        if (hiddenLeft > 0) {
                            solved = false;
                        } else {
                            solved = true;
                        }
                    } else if (action.equals("getAnswer") && isLose == 1) {
                        objectOutput.writeObject(randWord);
                    } else {
                        System.out.println("[Thread] msg : " + action);
                        System.out.println("Exiting ...");
                        objectInput.close();
                        objectOutput.close();
                        socket.close();
                        break;
                    }
                    /// check win or lose
                    if (missedCount >= MAX_TRY){
                        isLose = 1;

                    }
                    if (solved){
                        isWin = 1;
                    }
                }
            } catch(Exception err){
                err.printStackTrace();
            }
        }
    }
}
