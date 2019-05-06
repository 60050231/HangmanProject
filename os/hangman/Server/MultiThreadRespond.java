package os.hangman.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadRespond implements Runnable{

    private ServerSocket server;
    private int port;
    private ObjectOutputStream ObjectOutput;
    private ObjectInputStream ObjectInput;

    public MultiThreadRespond(int port){
        this.port = port;
        try{
            server = new ServerSocket(port);
        }catch(Exception e){

        }

    }

    @Override
    public void run(){
        String []word = {"Batman", "Deadpool", "Avatar"};
        int randRange = (int) (Math.random() * word.length);
        String randWord = word[randRange].toLowerCase();
        char []hiddenWord = new char[randWord.length()];
        String user_guess = "";
        int miss_chance = 0;
        char[] missed = new char[7];
        boolean letter_found = false, solved = false;

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
                ObjectInput = new ObjectInputStream(socket.getInputStream());
                ObjectOutput = new ObjectOutputStream(socket.getOutputStream());
                while (true) {
                    String message = (String) ObjectInput.readObject();
                    if (message.equalsIgnoreCase("START")) {
                        System.out.println("[Thread] Message Received: " + message);
                    }
                    else {
                        System.out.println("[Thread] Message Received: " + message);
                        ObjectInput.close();
                        ObjectOutput.close();
                        socket.close();
                        break;
                    }
                }
            }catch(Exception err){
                err.printStackTrace();
            }

        }
    }

}
