package WindowController;

import Client.ClientSocket;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        BooleanBinding textFieldValid = Bindings.createBooleanBinding(() -> {
            return !fileStatusLabel.getText().equals("not loaded");
        }, fileStatusLabel.textProperty());
        usersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        uploadButton.disableProperty().bind(Bindings.isEmpty(usersListView.getSelectionModel().getSelectedItems()).or(textFieldValid.not())); // find a way to restrict it only after file was loaded
    }

    public void initData(String username, String allUsers) {
        String[] responses = allUsers.split("\\s+");

        this.username = username;
        for (String user : responses) {
            String[] idAndUsername = user.split(":");
            users.put(idAndUsername[1], idAndUsername[0]);
        }
        ObservableList<String> itemsForDisplay = FXCollections.observableArrayList(users.keySet());
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
        ObservableList<String> selectedUsers = usersListView.getSelectionModel().getSelectedItems();

        StringBuilder commandString = new StringBuilder();
        commandString.append(fileToUpload).append(" ").append(username).append(" ");
        if (!selectedUsers.isEmpty()) {
            for (String selectedUser :
                    selectedUsers) {
                String userId = users.get(selectedUser);
                commandString.append(userId).append(":");
            }
        }
        else {
            commandString.append("-1");
        }

        ExecutorService es = Executors.newCachedThreadPool();
        ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, buildCommand("upload", commandString.toString()));

        Future<String> response = es.submit(commandWithSocket);

        try {
            interpretResponseFromServer(response.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 1. Select users
        // 2. To server and a litle implementation there
    }

    private void interpretResponseFromServer(String response) {
        if (response.equals("Failed"))
            operationFailedShowMessageBox("Upload failed!");
        else
            showInformationMessage("Upload succeeded!");
    }
}
