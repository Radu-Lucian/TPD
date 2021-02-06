package Server.Command;

import Model.*;
import Service.ResourceService;
import Service.RoleService;
import Service.UserRoleService;
import Service.UserService;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class UploadAction extends ControllerBaseAction {

    public UploadAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            respondClientOk();

            String fileToUpload = this.bufferedInputReader.readLine();
            if (fileToUpload != null) {
                Resource resource = addFileToDataBase(fileToUpload);
                Role role = addRoleToDataBase(resource);
                respondClientOk();

                String uploadingUserString = this.bufferedInputReader.readLine();
                respondClientOk();

                UserService userService = new UserService();
                User uploadingUser = userService.findUserByUsername(uploadingUserString);

                String usersWithFileAccess = this.bufferedInputReader.readLine();

                if ((usersWithFileAccess != null) && (uploadingUser != null)) {
                    String allUsersThatNeedAccessToFile = usersWithFileAccess + uploadingUser.getId();
                    giveAccessToUsers(allUsersThatNeedAccessToFile, resource, role);

                    result.set(true);
                    this.bufferedOutputWriter.write("Success");
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

    private Role addRoleToDataBase(Resource resource) {
        RoleService roleService = new RoleService();

        Role roleToAdd = new Role(resource);
        roleService.addNewRole(new Role(resource));

        return roleToAdd;
    }

    private Resource addFileToDataBase(String fileToUpload) {
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();

        byte[] result = base64Encoder.encode(fileToUpload.getBytes(StandardCharsets.ISO_8859_1));

        ResourceService service = new ResourceService();
        Resource resourceToAdd = new Resource(result);
        service.addFile(resourceToAdd);

        return resourceToAdd;
    }

    private void giveAccessToUsers(String allUsersThatNeedAccessToFile, Resource resource, Role role) {
        UserRoleService userRoleService = new UserRoleService();
        UserService userService = new UserService();

        String[] usersToGiveAccess = allUsersThatNeedAccessToFile.split(":");
        for (String userId :
                usersToGiveAccess) {
            User user = userService.findUserById(Integer.parseInt(userId));
            UserRole userRole = new UserRole(user, role, false);
            userRoleService.addNewUserRole(userRole);
        }

    }
}
