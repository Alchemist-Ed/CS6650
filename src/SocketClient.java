import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) {
        String serverAddress = "192.168.1.97"; // Replace with your server's IP address
        int serverPort = 12345; // Replace with your server's port number

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Send a message to the server
            out.println("Hello, server!");

            // Read and display the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
