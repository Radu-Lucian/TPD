package Server.Command;

import Model.User;
import Service.UserService;

import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginAction extends ControllerBaseAction {

    public LoginAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        AtomicBoolean result = new AtomicBoolean(false);
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
                    String message = getAllFilesAvailableForUser(resultedUser);
                    this.bufferedOutputWriter.write(message);

                    result.set(true);
                }
                else {
                    this.bufferedOutputWriter.write("Failed");
                }
                this.bufferedOutputWriter.flush();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return result.get();
    }


}
