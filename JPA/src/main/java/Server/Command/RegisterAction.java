package Server.Command;

import Model.User;
import Service.UserService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class RegisterAction extends ControllerBaseAction {

    public RegisterAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        boolean result = false;
        try {
            this.bufferedOutputWriter.newLine();
            this.bufferedOutputWriter.flush();

            String username = this.bufferedInputReader.readLine();
            if (username != null) {
                UserService userService = new UserService();

                Base64.Encoder base64Encoder = Base64.getUrlEncoder();
                SecureRandom secureRandom = new SecureRandom();
                byte[] randomBytes = new byte[16];
                secureRandom.nextBytes(randomBytes);

                String userToken = base64Encoder.encodeToString(randomBytes);
                String dbToken = base64Encoder.encodeToString(userToken.getBytes());

                userService.addUser(new User(username, dbToken));

                if (userService.findUserByToken(dbToken) != null) {
                    this.bufferedOutputWriter.write(userToken);
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
