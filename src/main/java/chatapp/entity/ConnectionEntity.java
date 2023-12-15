package chatapp.entity;


import lombok.Getter;

import java.io.*;
import java.net.Socket;

public class ConnectionEntity {
    private final Socket client;
    private final BufferedReader reader;

    private final PrintWriter writer;

    @Getter
    private final String username;

    public ConnectionEntity(Socket client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.writer = new PrintWriter(client.getOutputStream(), true);

        this.username = this.reader.readLine();
    }

    public void sendMessage(String message) {
        this.writer.println(message);
    }

    public String readMessage() throws IOException {
        return this.reader.readLine();
    }

    public void sendObject(Object object) throws IOException {
        OutputStream outputStream = client.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(object);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        InputStream inputStream = client.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return objectInputStream.readObject();
    }

    public void close() {
        try {
            this.client.close();
        } catch (IOException e) {
            System.out.println("Error closing client socket: " + e.getMessage());
        }
    }

    public boolean isClosed() {
        return this.client.isClosed();
    }
}