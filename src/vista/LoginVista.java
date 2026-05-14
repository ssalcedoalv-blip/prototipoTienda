package tiendaLibros.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import tiendaLibros.controlador.LoginControlador;
import tiendaLibros.model.Usuario;


public class LoginVista {

    private Stage stage;
    private LoginControlador controlador;

    public LoginVista(Stage stage) {
        this.stage = stage;
        this.controlador = new LoginControlador();
    }

    public void mostrar() {
        stage.setTitle("📚 Tienda de Libros — Login");

        
        Label lblTitulo = new Label("📚 Tienda de Libros");
        lblTitulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblSub = new Label("Inicia sesión para continuar");
        lblSub.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

       
        Label lblUser = new Label("Usuario:");
        lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField txtUser = new TextField();
        txtUser.setPromptText("Nombre de usuario");
        txtUser.setPrefHeight(38);

        Label lblPass = new Label("Contraseña:");
        lblPass.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setPrefHeight(38);

        Label lblError = new Label("");
        lblError.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

      
        Button btnLogin = new Button("Ingresar");
        btnLogin.setPrefWidth(260);
        btnLogin.setPrefHeight(42);
        btnLogin.setStyle(
            "-fx-background-color: #2c3e50; -fx-text-fill: white;" +
            "-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;"
        );

        Label lblHint = new Label("Admin: admin/admin123  |  Usuario: juan/juan123");
        lblHint.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");

      
        Runnable accion = () -> {
            String user = txtUser.getText().trim();
            String pass = txtPass.getText().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                lblError.setText("Completa todos los campos.");
                return;
            }
            Usuario u = controlador.validarLogin(user, pass);
            if (u != null) {
                lblError.setText("");
                abrirDashboard(u);
            } else {
                lblError.setText("❌ Usuario o contraseña incorrectos.");
                txtPass.clear();
            }
        };

        btnLogin.setOnAction(e -> accion.run());
        txtPass.setOnAction(e -> accion.run());
        txtUser.setOnAction(e -> txtPass.requestFocus());

     
        VBox form = new VBox(10, lblUser, txtUser, lblPass, txtPass, lblError, btnLogin, lblHint);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(30));
        form.setStyle(
            "-fx-background-color: white; -fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        form.setMaxWidth(380);

        VBox encabezado = new VBox(4, lblTitulo, lblSub);
        encabezado.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, encabezado, form);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #ecf0f1;");

        Scene scene = new Scene(root, 480, 460);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        txtUser.requestFocus();
    }

    private void abrirDashboard(Usuario usuario) {
        if ("admin".equals(usuario.getRol())) {
            new DashboardAdminVista(stage, usuario).mostrar();
        } else {
            new DashboardUsuarioVista(stage, usuario).mostrar();
        }
    }
}
