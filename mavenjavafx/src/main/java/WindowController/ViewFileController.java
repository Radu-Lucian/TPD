package WindowController;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ViewFileController extends BaseController {

    @FXML
    public TextArea textArea = new TextArea();

    private byte[] fileByteArray;

    @FXML
    void initialize() {
        textArea.setEditable(false);
    }

    public void initData(byte[] fileContent) throws IOException {
        fileByteArray = fileContent;

        File file = File.createTempFile("temp", null);
        Path path = Paths.get(file.toURI());
        Files.write(path, fileByteArray);

        List<String> content = Files.readAllLines(path);
        for (String line :
                content) {
            textArea.appendText(line);
            textArea.appendText("\n");
        }

        file.deleteOnExit();
    }
}
