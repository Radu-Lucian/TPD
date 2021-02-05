package WindowController;

import Client.ClientSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegisterController extends BaseController {
    @FXML
    public TextField usernameField;
    @FXML
    public Button registerButton;
    @FXML
    public Button cancelButton;

    @FXML
    void initialize() {
    }

    public void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void onRegisterButtonClick(ActionEvent event) {
        String username = (String) usernameField.getText();

        ExecutorService es = Executors.newCachedThreadPool();
        ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, buildCommand("register", username));

        Future<String> response = es.submit(commandWithSocket);
        try {
            if (response.get().equals("Failed")) {
                operationFailedShowMessageBox("Register Failed! Try a new username");
            }
            else {
                showInformationMessage("Your new password is: " + response.get());
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
