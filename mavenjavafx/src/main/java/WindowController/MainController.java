package WindowController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController extends BaseController {

    @FXML
    public Label usernameLabel;

    private String username;

    @FXML
    void initialize() {

    }

    void initData(String username) {
        this.username = username;
        usernameLabel.setText(username);
    }
}
