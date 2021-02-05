package Server.Command;

import Model.User;
import Model.UserRole;
import Service.UserRoleService;
import Service.UserService;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadAction extends ControllerBaseAction{

    public DownloadAction(Socket socket) {
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
                this.bufferedOutputWriter.newLine();
                this.bufferedOutputWriter.flush();
                int fileId = Integer.parseInt(this.bufferedInputReader.readLine());

                UserService userService = new UserService();
                User resultedUser = userService.findUserByUsername(username);

                if (resultedUser != null) {
                    markFileAsDownloaded(resultedUser, fileId);
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

    private void markFileAsDownloaded(User resultedUser, int fileId) {
        List<UserRole> userRoleList = resultedUser.getRoles();
        for (UserRole role:
             userRoleList) {
            if (role.getRole().getResource().getId() == fileId) {
                role.setDownloaded(true);

                UserRoleService userRoleService = new UserRoleService();
                userRoleService.update(role);
            }
        }
    }
}
