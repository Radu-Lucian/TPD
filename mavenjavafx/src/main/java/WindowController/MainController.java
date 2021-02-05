package WindowController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MainController extends BaseController {

    @FXML
    public Label usernameLabel;

    private String username;

    private ArrayList<byte[]> files;

    @FXML
    void initialize() {

    }

    void initData(String response) {
        String[] responses = response.split("\\s+");
        files = new ArrayList<byte[]>();
        this.username = responses[0];
        for (int i = 1; i < responses.length; i++) {
            Base64.Decoder base64Decoder = Base64.getUrlDecoder();
            byte[] file = responses[i].getBytes(StandardCharsets.ISO_8859_1);
            files.add(base64Decoder.decode(file));
        }
        usernameLabel.setText(username);
    }
}
