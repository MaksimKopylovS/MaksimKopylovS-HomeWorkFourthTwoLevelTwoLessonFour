package packageClient;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {

    private static Socket socketClient;
    private static String serverAdress = "127.0.0.1";
    private static final int serverPort = 8189;

    private static DataInputStream din;
    private static DataOutputStream dout;
    private Thread potokRead;

    public Connection() {
        try {
            socketClient = new Socket(serverAdress, serverPort);

            dout = new DataOutputStream(socketClient.getOutputStream());
            din = new DataInputStream(socketClient.getInputStream());
            ExecutorService executorService = Executors.newFixedThreadPool(4);

            executorService.execute(new Thread(new Runnable() {
                public void run() {
                    System.out.println("Поток чтения запущен");
                    try {
                        while (true) {
                            readMessage();
                        }

                    } catch (SocketException socketException) {
                        socketException.fillInStackTrace();
                        System.out.println("Разрыв соединения с сервером");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }));

        } catch (IOException ioException) {
            ioException.printStackTrace();
            try {
                socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readMessage() throws IOException {
        String command = din.readUTF();
        if (command.equals("upload")) {
            String filename = din.readUTF();
            File file = new File("Client/" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            long size = din.readLong();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[256];
            for (int i = 0; i < (size + 255) / 256; i++) {
                System.out.println("Запись С");
                int read = din.read(buffer);
                fos.write(buffer, 0, read);
            }
            fos.close();
            dout.writeUTF("Клиент записал файл: " + filename);
        }
        System.out.println("Сервер пишет: " + command);
    }

    public void sendFile(String fileName) {
        try {

            File file = new File("client/" + fileName);
            if(file.exists()){
                dout.writeUTF("upload");
                dout.writeUTF(fileName);
                long length = file.length();
                dout.writeLong(length);
                FileInputStream fileBytes = new FileInputStream(file);
                int read = 0;
                byte[] buffer = new byte[256];
                while ((read = fileBytes.read(buffer)) != -1) {
                    System.out.println("Отправка С");
                    dout.write(buffer, 0, read);
                }
                dout.flush();
            }else {
                System.out.println("Файла не существует");
            }

        }catch (FileNotFoundException fileNotFoundException){
            fileNotFoundException.fillInStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void getFile(String fileName) {
        try {
            dout.writeUTF("download");
            dout.writeUTF(fileName);
            dout.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
