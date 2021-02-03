import Model.User;
import Model.UserRole;
import Server.Server;
import Service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {

        UserService userService = new UserService();
        User user = userService.findUser(3);
        List<UserRole> roles = user.getRoles();
        for (UserRole item :
                roles) {
            System.out.println(item);
        }

        ExecutorService exSrv = Executors.newCachedThreadPool();
        try {
            Server server = new Server(9001);
            exSrv.submit(server);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] randomBytes = new byte[16];
//        secureRandom.nextBytes(randomBytes);
//
//        String result = base64Encoder.encodeToString(randomBytes); // Give to the user
//
//        String db = base64Encoder.encodeToString(result.getBytes()); // Only in data base
//
//        System.out.println(result);
//        System.out.println(db);
//        System.out.println("");

//       Base64.Encoder base64Encoder = Base64.getUrlEncoder();
//       Base64.Decoder base64Decoder = Base64.getUrlDecoder();
//       File file = new File("C:\\Users\\Luc\\Desktop\\files\\9.txt");
//       byte[] fileContent = Files.readAllBytes(file.toPath());
//       byte[] result = base64Encoder.encode(fileContent); // Give to the user
//       System.out.println("");

//       ResourceService service = new ResourceService();
//       service.addFile(new Resource(result));

//       byte[] decrypt = base64Decoder.decode(result);
    }

}
