package tiendaLibros.vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tiendaLibros.controlador.CatalogoControlador;
import tiendaLibros.controlador.LoginControlador;
import tiendaLibros.model.Libro;
import tiendaLibros.model.Usuario;
import tiendaLibros.model.nodos.Nodo;


public class DashboardAdminVista {

    private Stage stage;
    private Usuario usuario;
    private CatalogoControlador catalogoCtrl;
    private LoginControlador loginCtrl;

   
    private TableView<Libro> tablaCatalogo;
    private ObservableList<Libro> datosTabla;

    public DashboardAdminVista(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
        this.catalogoCtrl = new CatalogoControlador();
        this.loginCtrl = new LoginControlador();
    }

    public void mostrar() {
        stage.setTitle("📚 Tienda de Libros — Administrador: " + usuario.getNombre());

    
        Label lblBienvenida = new Label("Panel de Administración");
        lblBienvenida.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label lblUsuario = new Label("Usuario: " + usuario.getNombre() + " (Admin)");
        lblUsuario.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

        Button btnCerrar = new Button("Cerrar Sesión");
        btnCerrar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnCerrar.setOnAction(e -> {
            new LoginVista(stage).mostrar();
        });

        HBox header = new HBox(10, new VBox(2, lblBienvenida, lblUsuario));
        header.setAlignment(Pos.CENTER_LEFT);
        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        header.getChildren().addAll(espaciador, btnCerrar);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");

 
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().add(crearTabCatalogo());
        tabPane.getTabs().add(crearTabUsuarios());

        VBox root = new VBox(header, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 900, 620);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

  
    private Tab crearTabCatalogo() {
        Tab tab = new Tab("📋 Catálogo de Libros");

        // Tabla
        tablaCatalogo = new TableView<>();
        datosTabla = FXCollections.observableArrayList();
        refrescarTabla();

        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colIsbn.setPrefWidth(100);

        TableColumn<Libro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setPrefWidth(200);

        TableColumn<Libro, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAutor.setPrefWidth(160);

        TableColumn<Libro, String> colGenero = new TableColumn<>("Género");
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colGenero.setPrefWidth(110);

        TableColumn<Libro, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(80);

        TableColumn<Libro, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(60);

        tablaCatalogo.getColumns().addAll(colIsbn, colTitulo, colAutor, colGenero, colPrecio, colStock);
        tablaCatalogo.setItems(datosTabla);

      
        Label lblForm = new Label("Agregar / Editar Libro");
        lblForm.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextField txtIsbn   = new TextField(); txtIsbn.setPromptText("ISBN");
        TextField txtTitulo = new TextField(); txtTitulo.setPromptText("Título");
        TextField txtAutor  = new TextField(); txtAutor.setPromptText("Autor");
        TextField txtGenero = new TextField(); txtGenero.setPromptText("Género");
        TextField txtPrecio = new TextField(); txtPrecio.setPromptText("Precio");
        TextField txtStock  = new TextField(); txtStock.setPromptText("Stock");

        Label lblMsg = new Label("");
        lblMsg.setStyle("-fx-font-size: 12px;");

    
        tablaCatalogo.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtIsbn.setText(sel.getIsbn());
                txtTitulo.setText(sel.getTitulo());
                txtAutor.setText(sel.getAutor());
                txtGenero.setText(sel.getGenero());
                txtPrecio.setText(String.valueOf(sel.getPrecio()));
                txtStock.setText(String.valueOf(sel.getStock()));
            }
        });

        Button btnAgregar = new Button("➕ Agregar");
        btnAgregar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnAgregar.setOnAction(e -> {
            try {
                Libro nuevo = new Libro(
                    txtIsbn.getText().trim(), txtTitulo.getText().trim(),
                    txtAutor.getText().trim(), txtGenero.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim())
                );
                if (catalogoCtrl.agregarLibro(nuevo)) {
                    refrescarTabla(); // refrescar tabla tras agregar
                    limpiarForm(txtIsbn, txtTitulo, txtAutor, txtGenero, txtPrecio, txtStock);
                    lblMsg.setStyle("-fx-text-fill: #27ae60;");
                    lblMsg.setText("✅ Libro agregado correctamente.");
                } else {
                    lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                    lblMsg.setText("❌ Ya existe un libro con ese ISBN.");
                }
            } catch (NumberFormatException ex) {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ Precio y stock deben ser números.");
            }
        });

        Button btnEditar = new Button("✏️ Actualizar");
        btnEditar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnEditar.setOnAction(e -> {
            try {
                boolean ok = catalogoCtrl.actualizarLibro(
                    txtIsbn.getText().trim(), txtTitulo.getText().trim(),
                    txtAutor.getText().trim(), txtGenero.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim())
                );
                if (ok) {
                    refrescarTabla(); // refrescar tabla tras editar
                    lblMsg.setStyle("-fx-text-fill: #27ae60;");
                    lblMsg.setText("✅ Libro actualizado.");
                } else {
                    lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                    lblMsg.setText("❌ ISBN no encontrado.");
                }
            } catch (NumberFormatException ex) {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ Precio y stock deben ser números.");
            }
        });

        Button btnEliminar = new Button("🗑️ Eliminar");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnEliminar.setOnAction(e -> {
            String isbn = txtIsbn.getText().trim();
            if (catalogoCtrl.eliminarLibro(isbn)) {
                refrescarTabla(); // refrescar tabla tras eliminar
                limpiarForm(txtIsbn, txtTitulo, txtAutor, txtGenero, txtPrecio, txtStock);
                lblMsg.setStyle("-fx-text-fill: #27ae60;");
                lblMsg.setText("✅ Libro eliminado.");
            } else {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ No se encontró el libro.");
            }
        });

        HBox botones = new HBox(10, btnAgregar, btnEditar, btnEliminar);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(8);
        form.add(new Label("ISBN:"), 0, 0);    form.add(txtIsbn, 1, 0);
        form.add(new Label("Título:"), 0, 1);  form.add(txtTitulo, 1, 1);
        form.add(new Label("Autor:"), 0, 2);   form.add(txtAutor, 1, 2);
        form.add(new Label("Género:"), 0, 3);  form.add(txtGenero, 1, 3);
        form.add(new Label("Precio:"), 0, 4);  form.add(txtPrecio, 1, 4);
        form.add(new Label("Stock:"), 0, 5);   form.add(txtStock, 1, 5);
        form.add(botones, 1, 6);
        form.add(lblMsg, 1, 7);

        VBox panelForm = new VBox(10, lblForm, form);
        panelForm.setPadding(new Insets(15));
        panelForm.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        panelForm.setPrefWidth(300);

        HBox contenido = new HBox(15, tablaCatalogo, panelForm);
        HBox.setHgrow(tablaCatalogo, Priority.ALWAYS);
        contenido.setPadding(new Insets(15));

        tab.setContent(contenido);
        return tab;
    }

    private Tab crearTabUsuarios() {
        Tab tab = new Tab("👥 Gestión de Usuarios");

        TableView<Usuario> tablaUsers = new TableView<>();
        ObservableList<Usuario> datos = FXCollections.observableArrayList();

       
        Nodo<Usuario> actual = loginCtrl.getListaUsuarios().getCabeza();
        while (actual != null) {
            datos.add(actual.dato);
            actual = actual.siguiente;
        }

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Usuario");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(150);

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colRol.setPrefWidth(100);

        tablaUsers.getColumns().addAll(colNombre, colRol);
        tablaUsers.setItems(datos);

        // Formulario nuevo usuario
        Label lblForm = new Label("Registrar Nuevo Usuario");
        lblForm.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextField txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        PasswordField txtPass = new PasswordField(); txtPass.setPromptText("Contraseña");
        ComboBox<String> cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll("usuario", "admin");
        cmbRol.setValue("usuario");

        Label lblMsg = new Label("");
        lblMsg.setStyle("-fx-font-size: 12px;");

        Button btnRegistrar = new Button("➕ Registrar");
        btnRegistrar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String pass = txtPass.getText().trim();
            String rol = cmbRol.getValue();
            if (nombre.isEmpty() || pass.isEmpty()) {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ Completa todos los campos.");
                return;
            }
            if (loginCtrl.registrarUsuario(nombre, pass, rol)) {
                datos.clear();
                Nodo<Usuario> n = loginCtrl.getListaUsuarios().getCabeza();
                while (n != null) { datos.add(n.dato); n = n.siguiente; }
                txtNombre.clear(); txtPass.clear();
                lblMsg.setStyle("-fx-text-fill: #27ae60;");
                lblMsg.setText("✅ Usuario registrado.");
            } else {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ Ese usuario ya existe.");
            }
        });

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(8);
        form.add(new Label("Nombre:"), 0, 0);    form.add(txtNombre, 1, 0);
        form.add(new Label("Contraseña:"), 0, 1); form.add(txtPass, 1, 1);
        form.add(new Label("Rol:"), 0, 2);        form.add(cmbRol, 1, 2);
        form.add(btnRegistrar, 1, 3);
        form.add(lblMsg, 1, 4);

        VBox panelForm = new VBox(10, lblForm, form);
        panelForm.setPadding(new Insets(15));
        panelForm.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        panelForm.setPrefWidth(280);

        HBox contenido = new HBox(15, tablaUsers, panelForm);
        HBox.setHgrow(tablaUsers, Priority.ALWAYS);
        contenido.setPadding(new Insets(15));

        tab.setContent(contenido);
        return tab;
    }

    /** Refresca la tabla cargando de nuevo la ListaDobleCircular */
    private void refrescarTabla() {
        datosTabla.clear();
        Libro[] libros = catalogoCtrl.getLibrosArray();
        for (Libro l : libros) datosTabla.add(l);
    }

    private void limpiarForm(TextField... fields) {
        for (TextField f : fields) f.clear();
    }
}
