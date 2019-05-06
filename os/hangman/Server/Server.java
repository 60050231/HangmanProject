package os.hangman.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;

    public static final String GAME_PLAY = "PLAY";
    public static final String GAME_STOP = "STOP";
    public static final String GAME_OVER = "OVER";
    public static final String GAME_WON = "WON";
    public static final String GAME_LOST = "LOST";

    public static void main(String args[]) throws ClassNotFoundException {
        //create the socket server object

        //keep listens indefinitely until receives 'exit' call or program terminates
        int i =0;
        Thread thread = null;
        try {
            server = new ServerSocket(port);

            while(true){
                System.out.println("Waiting for the client request");
                Socket socket = server.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                System.out.println("Message Received: " + message);
                Random rand = new Random();
                int newPort = rand.nextInt(9000)+1000;
                MultiThreadRespond mr = new MultiThreadRespond(newPort);
                thread = new Thread(mr);
                thread.start();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("This is answer : "+newPort);

                ois.close();
                oos.close();
                socket.close();
            }
        } catch (IOException ex) {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println("ERROR closing socket: " + e.getMessage());
            }
        }
    }
}
