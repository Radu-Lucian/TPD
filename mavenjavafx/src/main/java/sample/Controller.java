package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Controller {

    @FXML
    private TextField tokenField;
    @FXML
    private Button loginButton;

    @FXML
    void initialize() {

    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) throws IOException {
        String token = (String) tokenField.getText();

        ExecutorService es = Executors.newCachedThreadPool();
        ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, "token" + " " + token);

        Future<String> response = es.submit(commandWithSocket);
        try {
            String message = response.get();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText("Message");
            alert.setContentText(message);
            alert.showAndWait().ifPresent(rs -> {
            });
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
