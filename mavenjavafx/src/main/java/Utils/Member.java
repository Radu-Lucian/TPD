package Utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {

    private StringProperty userName;

    private BooleanProperty downloadCheck;

    private BooleanProperty updateCheck;

    public Member(String name, boolean checked, boolean updateCheck) {
        this.userName = new SimpleStringProperty(name);
        this.downloadCheck = new SimpleBooleanProperty(checked);
        this.updateCheck = new SimpleBooleanProperty(updateCheck);
    }

    public StringProperty nameProperty() { return userName; }

    public BooleanProperty checkPropertyDownload() { return downloadCheck; }

    public BooleanProperty checkPropertyUpdate() { return updateCheck; }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public boolean isDownloadCheck() {
        return downloadCheck.get();
    }

    public BooleanProperty downloadCheckProperty() {
        return downloadCheck;
    }

    public void setDownloadCheck(boolean downloadCheck) {
        this.downloadCheck.set(downloadCheck);
    }


    public boolean isUpdateCheck() {
        return updateCheck.get();
    }

    public BooleanProperty updateCheckProperty() {
        return updateCheck;
    }

    public void setUpdateCheck(boolean updateCheck) {
        this.updateCheck.set(updateCheck);
    }

    public void setCheck(Boolean newValue) {
        downloadCheck.setValue(newValue);
    }
}
