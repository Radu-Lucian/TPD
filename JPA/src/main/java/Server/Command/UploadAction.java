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
                String[] fileAndEncryptionAlg = fileToUpload.split(":");

                Resource resource = addFileToDataBase(fileAndEncryptionAlg[0], fileAndEncryptionAlg[1]);
                respondClientOk();

                String uploadingUserString = this.bufferedInputReader.readLine();
                respondClientOk();

                UserService userService = new UserService();
                User uploadingUser = userService.findUserByUsername(uploadingUserString);

                String usersWithFileAccess = this.bufferedInputReader.readLine();

                if ((usersWithFileAccess != null) && (uploadingUser != null)) {
                    String allUsersThatNeedAccessToFile = usersWithFileAccess + uploadingUser.getId() + ",V'D'U";
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

    private Resource addFileToDataBase(String fileToUpload, String encryptAlg) {
        byte[] result = fileToUpload.getBytes(StandardCharsets.ISO_8859_1);
        boolean encryptingAlg = false;

        ResourceService service = new ResourceService();
        if (!encryptAlg.equals("Base64")) {
            encryptingAlg = true;
        }
        Resource resourceToAdd = new Resource(result, encryptingAlg);
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

            String[] userRights = userIdAndRight[1].split("'");

            Role role = new Role(resource);
            roleService.addNewRole(new Role(resource));

            UserRole userRole = new UserRole(user, role, false);

            ArrayList<Right> rights = new ArrayList<>();

            for (String rightUserFromString :
                    userRights) {
                if (rightUserFromString.equals("D")) {
                    rights.add(rightService.findById(2));
                }
                if (rightUserFromString.equals("U")) {
                    rights.add(rightService.findById(3));
                }
                if (rightUserFromString.equals("V")) {
                    rights.add(rightService.findById(1));
                }
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
