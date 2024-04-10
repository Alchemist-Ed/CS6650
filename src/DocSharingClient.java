import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DocSharingClient {
    private static JTextArea textArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Document Sharing Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JTextField inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer(inputField.getText());
                inputField.setText(""); // Clear the input field after sending
            }
        });
        frame.add(inputField, BorderLayout.SOUTH);

        frame.setVisible(true);

        connectToServer();
    }

    private static void connectToServer() {
        try (Socket socket = new Socket("192.168.1.97", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String message;
            while ((message = in.readLine()) != null) {
                appendMessageToCanvas(message); // Update canvas with received message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToServer(String message) {
        try (Socket socket = new Socket("192.168.1.97", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendMessageToCanvas(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(message + "\n"); // Append message to text area
            }
        });
    }
}
