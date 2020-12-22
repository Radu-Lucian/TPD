package WindowController;

import javafx.scene.control.Alert;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BaseController {

    public String buildCommand(String type, String value) {
        return type + " " + value;
    }

    public void ifOperationFailedShowMessageBox(String headerMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText(headerMessage);
        alert.showAndWait().ifPresent(rs -> { });
    }

}
