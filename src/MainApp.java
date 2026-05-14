package tiendaLibros;

import javafx.application.Application;
import javafx.stage.Stage;
import tiendaLibros.util.ArchivoUtil;
import tiendaLibros.vista.LoginVista;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ArchivoUtil.inicializarArchivos();
        LoginVista login = new LoginVista(primaryStage);
        login.mostrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
