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
    private

    public MultiThreadRespond(int port){
        this.port = port;
        try{
            server = new ServerSocket(port);
        }catch(Exception e){

        }

    }

    @Override
    public void run(){
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
            }catch(Exception e){

            }

        }
    }

}
