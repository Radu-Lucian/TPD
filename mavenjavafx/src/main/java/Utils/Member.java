package Utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {

    private StringProperty userName;

    private BooleanProperty downloadCheck;

    public Member(String name, boolean checked) {
        userName = new SimpleStringProperty(name);
        downloadCheck = new SimpleBooleanProperty(checked);
    }

    public StringProperty nameProperty() { return userName; }

    public BooleanProperty checkProperty() { return downloadCheck; }

    public void setCheck(Boolean newValue) {
        downloadCheck.setValue(newValue);
    }
}
