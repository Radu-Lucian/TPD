package Server.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class ControllerBaseAction implements ControllerAction {

    protected Socket socket;
    protected BufferedWriter bufferedOutputWriter;
    protected BufferedReader bufferedInputReader;

    public ControllerBaseAction(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedOutputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
