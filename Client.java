import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client{

    static Socket socket = null;

    static DataOutputStream dataOutputStream = null;
    static DataInputStream dataInputStream = null;
    
    public static void main(String []args) throws IOException {
      
        socket = new Socket("localhost",9988);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("Hello Server How are U");

        dataInputStream =new DataInputStream(socket.getInputStream());
        String msg_in = dataInputStream.readUTF();
        System.out.println("Server :"+ msg_in);

        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
    }

}