package chatapp.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionEntity {
    public Socket client;
    public BufferedReader reader;
    public PrintWriter writer;
    public String username;

    public ConnectionEntity(Socket client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.writer = new PrintWriter(client.getOutputStream(), true);
    }
}
