package Server.Command;

import Model.User;
import Service.UserService;

import java.io.*;
import java.net.ServerSocket;
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
            BufferedWriter bufferedOutputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            bufferedOutputWriter.write("\n");
            bufferedOutputWriter.flush();

            String token = bufferedInputReader.readLine();
            if (token != null) {
                Base64.Encoder base64Encoder = Base64.getUrlEncoder();
                String databaseToken = base64Encoder.encodeToString(token.getBytes());

                UserService userService = new UserService();
                User resultedUser = userService.findUserByToken(databaseToken);

                if (resultedUser != null) {
                    bufferedOutputWriter.write("Success");
                    bufferedOutputWriter.flush();
                    result = true;
                }
                else {
                    bufferedOutputWriter.write("Failed");
                    bufferedOutputWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
