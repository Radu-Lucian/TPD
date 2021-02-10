package WindowController;

import Client.ClientSocket;
import Utils.Encryption;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EditFileController extends BaseController {

    @FXML
    public TextArea textArea = new TextArea();

    private byte[] fileByteArray;

    public String fileId;

    public String cypher;

    @FXML
    void initialize() {
        textArea.setEditable(true);
    }

    void initData(String fileId, byte[] fileByteArray, String cypher) throws IOException {
        ((Stage) textArea.getScene().getWindow()).getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        this.cypher = cypher;
        this.fileId = fileId;
        this.fileByteArray = fileByteArray;

        File file = File.createTempFile("temp", null);
        Path path = Paths.get(file.toURI());
        Files.write(path, this.fileByteArray);

        List<String> content = Files.readAllLines(path);
        for (String line :
                content) {
            textArea.appendText(line);
            textArea.appendText("\n");
        }

        file.deleteOnExit();
    }

    private void closeWindowEvent(WindowEvent event) {
        byte[] textByte = textArea.getText().getBytes();
        String cypherType;

        if (cypher.equals("False")) {
            cypherType = "Base64";
        } else {
            cypherType = "Cypher";
        }
        String fileToUpload = Encryption.encode(textByte, cypherType);

        ExecutorService es = Executors.newCachedThreadPool();
        ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, buildCommand(buildCommand("edit", fileId), fileToUpload));

        Future<String> response = es.submit(commandWithSocket);

        try {
            if (response.get().equals("Failed")) {
                operationFailedShowMessageBox("Upload failed");
            } else {
                showInformationMessage("Upload succeeded");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ((Stage) textArea.getScene().getWindow()).close();
    }

}
