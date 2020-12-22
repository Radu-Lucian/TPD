package WindowController;

import Client.ClientSocket;
import Utils.ProjectConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EntryController extends BaseController {

    @FXML
    public Button registerButton;
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
        ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, buildCommand("token", token));

        Future<String> response = es.submit(commandWithSocket);
        try {
            if (response.get().equals("Failed")) {
                operationFailedShowMessageBox("Login Failed!");
            }
            else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainWindow.fxml"));
                Stage stage = new Stage();
                stage.setTitle(ProjectConstants.APPLICATION_NAME + "- Main Control");
                stage.setScene(new Scene(loader.load()));
                MainController controller = loader.getController();
                controller.initData(response.get());
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void onRegisterButtonClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/register.fxml"));
            Stage stage = new Stage();
            stage.setTitle(ProjectConstants.APPLICATION_NAME + "- Register");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
