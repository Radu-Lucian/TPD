package Server.Command;

import Model.*;
import Service.*;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
                respondClientOk();

                String uploadingUserString = this.bufferedInputReader.readLine();
                respondClientOk();

                UserService userService = new UserService();
                User uploadingUser = userService.findUserByUsername(uploadingUserString);

                String usersWithFileAccess = this.bufferedInputReader.readLine();

                if ((usersWithFileAccess != null) && (uploadingUser != null)) {
                    String allUsersThatNeedAccessToFile = usersWithFileAccess + uploadingUser.getId() + ",A";
                    giveAccessToUsers(allUsersThatNeedAccessToFile, resource);

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

    private void giveAccessToUsers(String allUsersThatNeedAccessToFile, Resource resource) {
        UserRoleService userRoleService = new UserRoleService();
        UserService userService = new UserService();
        RightService rightService = new RightService();
        RoleService roleService = new RoleService();

        String[] usersToGiveAccess = allUsersThatNeedAccessToFile.split(":");
        for (String userIdAndRightString :
                usersToGiveAccess) {
            String[] userIdAndRight = userIdAndRightString.split(",");
            User user = userService.findUserById(Integer.parseInt(userIdAndRight[0]));

            Role role = new Role(resource);
            roleService.addNewRole(new Role(resource));

            UserRole userRole = new UserRole(user, role, false);

            ArrayList<Right> rights = new ArrayList<>();
            if (userIdAndRight[1].equals("A")) {
                rights.add(rightService.findById(2));
            }
            else {
                rights.add(rightService.findById(1));
            }
            user.getRoles().add(userRole);
            resource.getRoles().add(role);
            role.getUsers().add(userRole);

            for (Right userRight:
                 rights) {
                role.getRights().add(userRight);
            }

            for (Right userRight:
                    rights) {
                userRight.getRoles().add(role);
            }

            roleService.updateRole(role);
            userRoleService.addNewUserRole(userRole);
        }

    }
}
