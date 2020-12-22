import Server.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
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
    }

}
