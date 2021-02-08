package WindowController;

import Client.ClientSocket;
import Utils.ProjectConstants;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainController extends BaseController {

    @FXML
    public Label usernameLabel;

    @FXML
    public Button uploadButton;

    @FXML
    public Button downloadButton;

    @FXML
    public Button viewButton;

    @FXML
    public ListView<String> fileListView = new ListView<>();

    private String username;

    private HashMap<String, byte[]> files = new HashMap<String, byte[]>();

    @FXML
    void initialize() {
        downloadButton.disableProperty().bind(Bindings.isEmpty(fileListView.getSelectionModel().getSelectedItems()));
        viewButton.disableProperty().bind(Bindings.isEmpty(fileListView.getSelectionModel().getSelectedItems()));
    }

    void initData(String response) {
        interpretResponseFromServer(response);
    }

    public void onUploadButtonClick(ActionEvent event) {

        // 1. Request from server all the available users
        ExecutorService es = Executors.newCachedThreadPool();
        ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, buildCommand("getusers", username));

        Future<String> response = es.submit(commandWithSocket);

        try {
            String allUsers = response.get(); // format id:username

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/uploadFile.fxml"));
            Stage stage = new Stage();
            stage.setTitle(ProjectConstants.APPLICATION_NAME + " - Upload File");
            stage.setScene(new Scene(loader.load()));
            UploadFileController controller = loader.getController();
            controller.initData(username, allUsers);
            stage.show();

        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        // 2. Create a new window that takes as input those users
    }

    public void onViewButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFile.fxml"));
        Stage stage = new Stage();
        stage.setTitle(ProjectConstants.APPLICATION_NAME + " - View File");
        stage.setScene(new Scene(loader.load()));
        ViewFileController controller = loader.getController();
        controller.initData(files.get(fileListView.getSelectionModel().getSelectedItem()));
        stage.show();
    }

    public void onDownloadButtonClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file to disk");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file", ".txt"));

        String selectedFileIdToDownload = fileListView.getSelectionModel().getSelectedItem();

        File saveFileLocation = fileChooser.showSaveDialog((Stage) downloadButton.getScene().getWindow());
        if (saveFileLocation != null) {

            ExecutorService downloadRightExecutorService = Executors.newCachedThreadPool();
            ClientSocket downloadRightCommand = new ClientSocket("localhost", 9001, buildCommand(buildCommand("downloadRight", username), selectedFileIdToDownload));

            Future<String> downloadRightResponse = downloadRightExecutorService.submit(downloadRightCommand);

            try {
                if (downloadRightResponse.get().equals("Success")) {

                    Path path = Paths.get(saveFileLocation.toURI());
                    Files.write(path, files.get(selectedFileIdToDownload));

                    ExecutorService es = Executors.newCachedThreadPool();
                    ClientSocket commandWithSocket = new ClientSocket("localhost", 9001, buildCommand(buildCommand("download", username), selectedFileIdToDownload));

                    Future<String> response = es.submit(commandWithSocket);

                    try {
                        interpretResponseFromServer(response.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    showInformationMessage("No right to download!");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void interpretResponseFromServer(String response) {
        files.clear();
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();

        String[] responses = response.split("\\s+");

        this.username = responses[0];
        usernameLabel.setText(username);

        for (int i = 1; i < responses.length; i++) {
            String[] idAndFile = responses[i].split(":");
            files.put(idAndFile[0], base64Decoder.decode(idAndFile[1].getBytes(StandardCharsets.ISO_8859_1)));
        }
        ObservableList<String> itemsForDisplay = FXCollections.observableArrayList(new ArrayList<>(files.keySet()));
        fileListView.setItems(itemsForDisplay);
    }
}
