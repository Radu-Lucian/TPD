package Server.Command;

import Model.Right;
import Model.RightType;
import Model.User;
import Model.UserRole;
import Service.UserService;

import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadRightAction extends ControllerBaseAction{

    public DownloadRightAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            respondClientOk();
            String usernameToCheckRight = this.bufferedInputReader.readLine();

            if (usernameToCheckRight != null) {
                respondClientOk();
                UserService userService = new UserService();
                User resultedUser = userService.findUserByUsername(usernameToCheckRight);
                String downloadedFileId = this.bufferedInputReader.readLine();

                if (resultedUser != null) {
                    String message = checkDownloadRight(resultedUser, downloadedFileId);
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

    private String checkDownloadRight(User resultedUser, String downloadedFileId) {
        for (UserRole userRole:
             resultedUser.getRoles()) {
            if (userRole.getRole().getResource().getId() == Integer.parseInt(downloadedFileId)) {
                for (Right right:
                     userRole.getRole().getRights()) {
                    if (right.getType() == RightType.DOWNLOAD) {
                        return "Success";
                    }
                }
            }
        }
        return "Failed";
    }
}
