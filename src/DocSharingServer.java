import java.io.*;
import java.net.*;
import java.util.*;

public class DocSharingServer {
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Doc Sharing Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new thread to handle each client session
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                // Add client's PrintWriter to the set
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Broadcast message to all clients
                    broadcastMessage(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Remove client's PrintWriter from the set when client disconnects
                if (out != null) {
                    clientWriters.remove(out);
                }
            }
        }

        private void broadcastMessage(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }
}