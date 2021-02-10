package WindowController;

import Client.ClientSocket;
import Utils.Encryption;
import Utils.Member;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView<Member> usersTableView = new TableView<>();

    @FXML
    public ComboBox<String> algorithmDropBox = new ComboBox<>();

    private String username;

    private String fileToUpload;

    private String encryptionAlgorithm;

    private HashMap<String, String> users = new HashMap<String, String>();

    @FXML
    void initialize() {
        BooleanBinding textFieldValid = Bindings.createBooleanBinding(() -> {
            return !fileStatusLabel.getText().equals("not loaded");
        }, fileStatusLabel.textProperty());
        usersTableView.setEditable(true);
        usersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        uploadButton.disableProperty().bind(Bindings.isEmpty(usersTableView.getSelectionModel().getSelectedItems()).or(textFieldValid.not())); // find a way to restrict it only after file was loaded
    }

    public void initData(String username, String allUsers) {
        String[] responses = allUsers.split("\\s+");
        ArrayList<Member> membersToPopulate = new ArrayList<>();

        this.username = username;
        for (String user : responses) {
            String[] idAndUsername = user.split(":");
            users.put(idAndUsername[1], idAndUsername[0]);
            membersToPopulate.add(new Member(idAndUsername[1], true, true));
        }
        ObservableList<Member> itemsForDisplay = FXCollections.observableArrayList(membersToPopulate);

        usersTableView.setItems(itemsForDisplay);

        usersTableView.getColumns().clear();

        TableColumn<Member,String> c1 = new TableColumn<>("Username");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        usersTableView.getColumns().add(c1);

        TableColumn<Member,Boolean> c2 = new TableColumn<>("Download Right");
        c2.setCellFactory(column -> new CheckBoxTableCell());
        c2.setCellValueFactory(cellData -> {
            Member cellValue = cellData.getValue();
            BooleanProperty property = cellValue.checkPropertyDownload();
            property.addListener((observable, oldValue, newValue) -> cellValue.setDownloadCheck(newValue));
            return property;
        });
        usersTableView.getColumns().add(c2);

        TableColumn<Member,Boolean> c3 = new TableColumn<>("Update Right");
        c3.setCellFactory(column -> new CheckBoxTableCell());
        c3.setCellValueFactory(cellData -> {
            Member cellValue = cellData.getValue();
            BooleanProperty property = cellValue.checkPropertyUpdate();
            property.addListener((observable, oldValue, newValue) -> {
                cellValue.setUpdateCheck(newValue);
            });
            return property;
        });
        usersTableView.getColumns().add(c3);

        algorithmDropBox.setItems(FXCollections.observableArrayList(
                new String("Base64"),
                new String("Cypher")
        ));

        algorithmDropBox.getSelectionModel().selectFirst();
    }

    public void onSelectFileButtonClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chose file to upload");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file", "*.txt"));

        File fileToUploadFile = fileChooser.showOpenDialog((Stage) selectFileButton.getScene().getWindow());
        if (fileToUploadFile != null) {
            byte[] byteFile = Files.readAllBytes(fileToUploadFile.toPath());
            Base64.Encoder base64Encoder = Base64.getUrlEncoder();

            fileToUpload = new String(base64Encoder.encode(byteFile), StandardCharsets.ISO_8859_1);
            fileToUpload = Encryption.encode(byteFile, algorithmDropBox.getSelectionModel().getSelectedItem());
            encryptionAlgorithm = algorithmDropBox.getSelectionModel().getSelectedItem();

            fileStatusLabel.setText(fileToUploadFile.getName() + " loaded!");
        }
    }

    public void onUploadButtonClick(ActionEvent event) {
        ObservableList<Member> selectedUsers = usersTableView.getSelectionModel().getSelectedItems();

        StringBuilder commandString = new StringBuilder();
        commandString.append(fileToUpload).append(":").append(encryptionAlgorithm).append(" ").append(username).append(" ");
        if (!selectedUsers.isEmpty()) {
            for (Member selectedUser :
                    selectedUsers) {
                String userId = users.get(selectedUser.nameProperty().get());
                commandString.append(userId).append(",");
                if (selectedUser.isDownloadCheck())
                    commandString.append("D").append("'"); // download right
                if (selectedUser.isUpdateCheck())
                    commandString.append("U").append("'"); // update right
                commandString.append("V");                 // view right
                commandString.append(":");
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
