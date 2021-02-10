package Server.Command;

import Model.User;
import Model.UserRole;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public String getAllFilesAvailableForUser(User user) {
        StringBuilder userFiles = new StringBuilder();
        userFiles.append(user.getUsername()).append(" ");
        List<UserRole> roles = user.getRoles(); // get all the files that the user has access and has not downloaded yet
        for (UserRole role : roles) {
            if (!role.getDownloaded()) {
                byte[] file = role.getRole().getResource().getFile();
                int fileId = role.getRole().getResource().getId();
                boolean cypherType = role.getRole().getResource().isCypher();
                String fileString = new String(file, StandardCharsets.ISO_8859_1);
                userFiles.append(fileId).append(",").append(cypherType).append(":").append(fileString).append(" ");
            }
        }
        return String.valueOf(userFiles);
    }

    public void respondClientOk() throws IOException {
        this.bufferedOutputWriter.newLine();
        this.bufferedOutputWriter.flush();
    }
}
