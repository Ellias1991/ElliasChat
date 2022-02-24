package sam2BorderPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegController {

    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;
    @FXML public TextField nicknameField;
    @FXML public TextArea textArea;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void TryToReg(ActionEvent actionEvent) {
        String login= loginField.getText().trim();
        String password=passwordField.getText().trim();
        String nickname=nicknameField.getText().trim();
        controller.registration(login,password,nickname);

    }
    public void result(String command){
        if(command.equals("/reg_success")) {
            textArea.appendText("Регистрация прошла успешно\n");
        }else {
            textArea.appendText("Регистрация не прошла успешно\n");
        }
    }
}
