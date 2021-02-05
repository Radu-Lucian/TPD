package WindowController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UploadFileController extends BaseController {

    @FXML
    public Button selectFileButton;

    @FXML
    public Button uploadButton;

    @FXML
    public Label fileStatusLabel;

    @FXML
    public ListView<String> usersListView = new ListView<>();

    @FXML
    void initialize() {
    }

    public void initData(String username, String allUsers) {

    }

    public void onSelectFileButtonClick(ActionEvent event) {
    }

    public void onUploadButtonClick(ActionEvent event) {
    }
}
