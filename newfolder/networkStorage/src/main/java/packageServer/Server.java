package packageServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //private static final Logger LOGGER = LogManager.getLogger(Server.class);


    private ServerSocket serverSoket;
    private Socket clientSocket;
    private static final int PORT = 8189;

        public Server() throws IOException {
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            try {
                serverSoket = new ServerSocket(PORT);
                while (true) {
                    System.out.println("Сервер ожидает подключения");
                    clientSocket = serverSoket.accept();
                    System.out.println("Клиент подключился");
                    executorService.execute(new ClientHandler(clientSocket));
                }
            }catch(IOException ioException){
                ioException.printStackTrace();
                clientSocket.close();
            }

    }

    public static void main(String args[]) throws IOException {
            new Server();
    }

}
