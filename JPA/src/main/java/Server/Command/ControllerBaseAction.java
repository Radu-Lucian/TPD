package Server.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class ControllerBaseAction implements ControllerAction {

    protected Socket socket;

    public ControllerBaseAction(Socket socket) {
        this.socket = socket;
    }
}
