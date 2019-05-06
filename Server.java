import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    static ServerSocket serverSocket = null;
    static Socket socket = null;
    static DataOutputStream dataOutputStream = null;
    static DataInputStream dataInputStream = null;
    
    public static void main(String []args) throws IOException {
        serverSocket = new ServerSocket(9988);
        socket = serverSocket.accept();
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("Hello Client. I'm Find^^");

        dataInputStream =new DataInputStream(socket.getInputStream());
        String msg_in = dataInputStream.readUTF();
        System.out.println("Client :"+ msg_in);

        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
    }

}