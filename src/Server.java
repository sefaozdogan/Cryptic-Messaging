import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {

    private static HashSet<PrintWriter> prWriters = new HashSet<PrintWriter>();

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                output.println("_UserName_");
                input.readLine();
                prWriters.add(output);

                while (true) {
                    String input = this.input.readLine();
                    if (input == null)
                        return;
                    for (PrintWriter writer : prWriters) {
                        writer.println(input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        try (ServerSocket listener = new ServerSocket(1414)) {
            while (true)
                new ClientHandler(listener.accept()).start();
        }
    }
}

