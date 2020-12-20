package Server;

import Model.User;
import Server.Command.ControllerAction;
import Server.Command.LoginAction;
import Service.UserService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UserServer implements Runnable {

    private final ServerSocket serverSocket;

    private HashMap<String, Function<Socket, ControllerAction>> controllerActions;

    public UserServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(250);
        controllerActions = new HashMap<String, Function<Socket, ControllerAction>>() {{
            put("token", LoginAction::new);
        }};
    }

    @Override
    public void run() {
        try {
            accept();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void accept() throws IOException {
        System.out.println("Accepting connections on port " + serverSocket.getLocalPort());
        while (!Thread.interrupted()) {
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Connection accepted");
                BufferedWriter bufferedOutputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                String input = bufferedInputReader.readLine();
                System.out.println("Input received from client: " + input);

                Function<Socket, ControllerAction> socketControllerActionFunction = this.controllerActions.get(input);
                ControllerAction controllerAction = socketControllerActionFunction.apply(socket);
                controllerAction.execute();

                bufferedOutputWriter.newLine();
                bufferedOutputWriter.flush();
            } catch (SocketTimeoutException ste) {
                // Not here
            }

        }
        System.out.println("Done accepting");
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
