package Server.Command;

import Model.User;
import Service.UserService;

import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class RefreshAction extends ControllerBaseAction {
    public RefreshAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            this.bufferedOutputWriter.newLine();
            this.bufferedOutputWriter.flush();

            String username = this.bufferedInputReader.readLine();
            if (username != null) {
                UserService userService = new UserService();
                User resultedUser = userService.findUserByUsername(username);

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
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return result.get();
    }
}
