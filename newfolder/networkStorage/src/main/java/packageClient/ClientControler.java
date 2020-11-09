package packageClient;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;

public class ClientControler {
    private String message;

    @FXML
    private TextArea textAreaStorage;

    @FXML
    private TextField textFieldSend;

    @FXML
    private TextField textFieldGetFile;
    @FXML
    private Button buttonSend;

    @FXML
    private Button buttonGetFile;


    public void actionButtonSend() throws IOException {
        message = textFieldSend.getText();
        if(message.equals("")){
            textAreaStorage.appendText("Введите имя файла для отправки" + "\n");
        }else{
            Client.getConnection().sendFile(message);
        }
    }

    public void actionButtonGetFile(){
        message = textFieldGetFile.getText();
        if(message.equals("")){
            textAreaStorage.appendText("Введите имя файла для получения" + "\n");
        }else{
            Client.getConnection().getFile(message);
        }
    }

    @FXML
    public void initialize(){


    }



}
