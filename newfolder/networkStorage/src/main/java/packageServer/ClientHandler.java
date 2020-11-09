package packageServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String word;
    private DataOutputStream dout;
    private DataInputStream din;

    private ExecutorService executorService;

    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            dout = new DataOutputStream(socket.getOutputStream());
            din = new DataInputStream(socket.getInputStream());

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                readMessage();
            }
        } catch (SocketException socketException) {
            socketException.fillInStackTrace();
            System.out.println("Клиент отключился");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void readMessage() throws IOException {
        while (true) {
            String command = din.readUTF();
            if (command.equals("upload")) {
                String fileName = din.readUTF();
                System.out.println("От Клиента " + command + " " + fileName);
                try {
                    File file = new File("Server/" + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    long size = din.readLong();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[256];
                    for (int i = 0; i < (size + 255) / 256; i++) {
                        System.out.println("upS");
                        int read = din.read(buffer);
                        fos.write(buffer, 0, read);
                    }
                    fos.close();
                    dout.writeUTF("Сервер сохранил файл: " + fileName);
                } catch (Exception e) {
                    dout.writeUTF("Ошибка");
                }
            }

            if (command.equals("download")) {
//
//
//                dout.writeUTF("upload");
//                dout.writeUTF(fileName);

                try {
                    String fileName = din.readUTF();
                    File file = new File("Server/" + fileName);
                    System.out.println("От Клиента " + command + " " + fileName);
                    if (file.exists()) {
                        dout.writeUTF("upload");
                        dout.writeUTF(fileName);
                        long length = file.length();
                        dout.writeLong(length);
                        FileInputStream fileBytes = new FileInputStream(file);
                        int read = 0;
                        byte[] buffer = new byte[256];
                        while ((read = fileBytes.read(buffer)) != -1) {
                            System.out.println("downS");
                            dout.write(buffer, 0, read);
                        }
                        fileBytes.close();
                        dout.writeUTF("Сервер отправил файл: " + fileName);
                        dout.flush();
                    } else {
                        System.out.println("Файл не найден");
                        dout.writeUTF("Файл не найден " + fileName);
                    }

                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.fillInStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            System.out.println("Клиент пишет: " + command);
        }
    }
}