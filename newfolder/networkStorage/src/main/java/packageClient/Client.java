package packageClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    //Объекты Окна сцены клиента
    private FXMLLoader loaderClient;
    private Parent rootClient;
    private Scene sceneClient;
    private ClientControler clientControler;
    private Stage stageClient;
    private static Connection connection;

    public Client() {

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
//      Инициализация соединения
        connection = new Connection();
//        Инициализация окна клиента
        stageClient = new Stage();
        loaderClient = new FXMLLoader(getClass().getResource("/Client.fxml"));
        rootClient = (Parent) loaderClient.load();
        sceneClient = new Scene(rootClient, 300, 275);
        stageClient.setScene(sceneClient);
        stageClient.setTitle("Сетевое Хранилище");
        clientControler = loaderClient.getController();
        stageClient.show();

    }

    public static Connection getConnection(){
        return connection;
    }

    public static void main(String args[]){
        launch(args);
    }

}
