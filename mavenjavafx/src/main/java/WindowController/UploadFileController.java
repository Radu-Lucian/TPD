package WindowController;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class UploadFileController extends BaseController {

    @FXML
    public Button selectFileButton;

    @FXML
    public Button uploadButton;

    @FXML
    public Label fileStatusLabel;

    @FXML
    public ListView<String> usersListView = new ListView<>();

    private String username;

    private String fileToUpload;

    private HashMap<String, String> users = new HashMap<String, String>();

    @FXML
    void initialize() {
        usersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        uploadButton.disableProperty().bind(Bindings.isEmpty(usersListView.getSelectionModel().getSelectedItems())); // find a way to restrict it only after file was loaded
    }

    public void initData(String username, String allUsers) {
        String[] responses = allUsers.split("\\s+");

        this.username = username;
        for (String user : responses) {
            String[] idAndUsername = user.split(":");
            users.put(idAndUsername[0], idAndUsername[1]);
        }
        ObservableList<String> itemsForDisplay = FXCollections.observableArrayList(users.values());
        usersListView.setItems(itemsForDisplay);
    }

    public void onSelectFileButtonClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chose file to upload");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file", "*.txt"));

        File fileToUploadFile = fileChooser.showOpenDialog((Stage) selectFileButton.getScene().getWindow());
        if (fileToUploadFile != null) {
            byte[] byteFile = Files.readAllBytes(fileToUploadFile.toPath());
            fileToUpload = new String(byteFile, StandardCharsets.ISO_8859_1);

            fileStatusLabel.setText(fileToUploadFile.getName() + " loaded!");
        }
    }

    public void onUploadButtonClick(ActionEvent event) {

        // 1. Select users
        // 2. To server and a litle implementation there
    }
}
