package Server.Command;

import Model.User;
import Service.UserService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginAction extends ControllerBaseAction {

    public LoginAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        boolean result = false;
        try {
            this.bufferedOutputWriter.newLine();
            this.bufferedOutputWriter.flush();

            String token = this.bufferedInputReader.readLine();
            if (token != null) {
                Base64.Encoder base64Encoder = Base64.getUrlEncoder();
                String databaseToken = base64Encoder.encodeToString(token.getBytes());

                UserService userService = new UserService();
                User resultedUser = userService.findUserByToken(databaseToken);

                if (resultedUser != null) {
                    this.bufferedOutputWriter.write(resultedUser.getUsername());
                    result = true;
                }
                else {
                    this.bufferedOutputWriter.write("Failed");
                }
                this.bufferedOutputWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
