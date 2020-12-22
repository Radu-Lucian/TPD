package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class ClientSocket implements Callable<String> {

    private final String host;
    private final int port;
    private final String command;

    public ClientSocket(String host, int port, String command) {
        this.host = host;
        this.port = port;
        this.command = command;
    }

    @Override
    public String call() throws Exception {
        try (Socket socket = new Socket(this.host, this.port)) {

            BufferedWriter bufferedOutputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            String response = "";

            String[] commands = command.split("\\s+");
            for (String command : commands) {
                bufferedOutputWriter.write(command);
                bufferedOutputWriter.newLine();
                bufferedOutputWriter.flush();
                response = bufferedInputReader.readLine();
            }
            bufferedOutputWriter.newLine();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

