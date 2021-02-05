package Server.Command;

import Model.User;
import Service.UserService;

import java.net.Socket;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GetUsersAction extends ControllerBaseAction {

    public GetUsersAction(Socket socket) {
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
                List<User> availableUsers = userService.findAll();

                if ((availableUsers != null) && (availableUsers.size() > 0)) {
                    String message = getAllUsers(availableUsers, username);
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
        }
        return result.get();
    }

    private String getAllUsers(List<User> availableUsers, String username) {
        StringBuilder stringBuilder = new StringBuilder();
        for (User user :
                availableUsers) {
            if (!user.getUsername().equals(username)) {
                stringBuilder.append(user.getId()).append(":").append(user.getUsername()).append(" ");
            }
        }
        return String.valueOf(stringBuilder);
    }
}
