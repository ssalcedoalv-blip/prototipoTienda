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
import tiendaLibros.controlador.CarritoControlador;
import tiendaLibros.controlador.CatalogoControlador;
import tiendaLibros.controlador.HistorialControlador;
import tiendaLibros.model.Compra;
import tiendaLibros.model.ItemCarrito;
import tiendaLibros.model.Libro;
import tiendaLibros.model.Usuario;


public class DashboardUsuarioVista {

    private Stage stage;
    private Usuario usuario;
    private CatalogoControlador catalogoCtrl;
    private CarritoControlador carritoCtrl;
    private HistorialControlador historialCtrl;

  
    private TableView<Libro> tablaCatalogo;
    private ObservableList<Libro> datosCatalogo;
    private TableView<ItemCarrito> tablaCarrito;
    private ObservableList<ItemCarrito> datosCarrito;
    private Label lblTotal;

    public DashboardUsuarioVista(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
        this.catalogoCtrl = new CatalogoControlador();
        this.carritoCtrl = new CarritoControlador();
        this.historialCtrl = new HistorialControlador();
    }

    public void mostrar() {
        stage.setTitle("📚 Tienda de Libros — " + usuario.getNombre());

       
        Label lblBienvenida = new Label("Bienvenido, " + usuario.getNombre() + " 👋");
        lblBienvenida.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button btnCerrar = new Button("Cerrar Sesión");
        btnCerrar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnCerrar.setOnAction(e -> new LoginVista(stage).mostrar());

        Region esp = new Region();
        HBox.setHgrow(esp, Priority.ALWAYS);
        HBox header = new HBox(10, lblBienvenida, esp, btnCerrar);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");

      
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().add(crearTabCatalogo());
        tabPane.getTabs().add(crearTabCarrito());
        tabPane.getTabs().add(crearTabHistorial());

        VBox root = new VBox(header, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 950, 650);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    
    private Tab crearTabCatalogo() {
        Tab tab = new Tab("📋 Catálogo");

        tablaCatalogo = new TableView<>();
        datosCatalogo = FXCollections.observableArrayList();
        refrescarCatalogo();

        TableColumn<Libro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setPrefWidth(210);

        TableColumn<Libro, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAutor.setPrefWidth(160);

        TableColumn<Libro, String> colGenero = new TableColumn<>("Género");
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colGenero.setPrefWidth(110);

        TableColumn<Libro, Double> colPrecio = new TableColumn<>("Precio ($)");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(90);

        TableColumn<Libro, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(60);

        tablaCatalogo.getColumns().addAll(colTitulo, colAutor, colGenero, colPrecio, colStock);
        tablaCatalogo.setItems(datosCatalogo);

       
        Label lblAgregar = new Label("Agregar al Carrito");
        lblAgregar.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblLibroSel = new Label("Selecciona un libro de la tabla");
        lblLibroSel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        Spinner<Integer> spnCantidad = new Spinner<>(1, 99, 1);
        spnCantidad.setPrefWidth(80);

        Label lblMsg = new Label("");
        lblMsg.setStyle("-fx-font-size: 12px;");

        tablaCatalogo.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) lblLibroSel.setText(sel.getTitulo() + " — $" + sel.getPrecio());
        });

        Button btnAgregar = new Button("🛒 Agregar al Carrito");
        btnAgregar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnAgregar.setOnAction(e -> {
            Libro sel = tablaCatalogo.getSelectionModel().getSelectedItem();
            if (sel == null) {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ Selecciona un libro primero.");
                return;
            }
            int cant = spnCantidad.getValue();
            if (carritoCtrl.agregar(sel, cant)) {
                refrescarCarrito();
                lblMsg.setStyle("-fx-text-fill: #27ae60;");
                lblMsg.setText("✅ Agregado: " + sel.getTitulo());
            } else {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ Stock insuficiente.");
            }
        });

        VBox panelDer = new VBox(12, lblAgregar, lblLibroSel,
            new HBox(8, new Label("Cantidad:"), spnCantidad),
            btnAgregar, lblMsg);
        panelDer.setPadding(new Insets(15));
        panelDer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        panelDer.setPrefWidth(250);

        HBox contenido = new HBox(15, tablaCatalogo, panelDer);
        HBox.setHgrow(tablaCatalogo, Priority.ALWAYS);
        contenido.setPadding(new Insets(15));

        tab.setContent(contenido);
        return tab;
    }


    private Tab crearTabCarrito() {
        Tab tab = new Tab("🛒 Carrito (Pila)");

        tablaCarrito = new TableView<>();
        datosCarrito = FXCollections.observableArrayList();

        TableColumn<ItemCarrito, String> colTitulo = new TableColumn<>("Libro");
        colTitulo.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getLibro().getTitulo()));
        colTitulo.setPrefWidth(220);

        TableColumn<ItemCarrito, Integer> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCantidad()));
        colCant.setPrefWidth(80);

        TableColumn<ItemCarrito, Double> colSub = new TableColumn<>("Subtotal ($)");
        colSub.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSubtotal()));
        colSub.setPrefWidth(100);

        tablaCarrito.getColumns().addAll(colTitulo, colCant, colSub);
        tablaCarrito.setItems(datosCarrito);

        lblTotal = new Label("Total: $0");
        lblTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblNota = new Label("Estructura: PILA (LIFO) — El último agregado es el primero en quitarse");
        lblNota.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6; -fx-font-style: italic;");

        Label lblMsg = new Label("");
        lblMsg.setStyle("-fx-font-size: 12px;");

      
        Button btnDeshacer = new Button("↩️ Quitar Último (Deshacer)");
        btnDeshacer.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnDeshacer.setOnAction(e -> {
            ItemCarrito quitado = carritoCtrl.quitarUltimo();
            if (quitado != null) {
                refrescarCarrito();
                lblMsg.setStyle("-fx-text-fill: #e67e22;");
                lblMsg.setText("↩️ Quitado: " + quitado.getLibro().getTitulo());
            } else {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ El carrito está vacío.");
            }
        });

        // Botón comprar (confirmar)
        Button btnComprar = new Button("✅ Confirmar Compra");
        btnComprar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnComprar.setOnAction(e -> {
            if (carritoCtrl.estaVacio()) {
                lblMsg.setStyle("-fx-text-fill: #e74c3c;");
                lblMsg.setText("❌ El carrito está vacío.");
                return;
            }
            ItemCarrito[] items = carritoCtrl.getItemsArray();
            double total = carritoCtrl.getTotal();
            historialCtrl.registrarCompra(usuario.getNombre(), items, total);
            carritoCtrl.vaciar();
            refrescarCarrito();
            lblMsg.setStyle("-fx-text-fill: #27ae60;");
            lblMsg.setText("✅ Compra realizada por $" + String.format("%.0f", total));

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Compra exitosa");
            alerta.setHeaderText("¡Gracias por tu compra!");
            alerta.setContentText("Total pagado: $" + String.format("%.0f", total));
            alerta.showAndWait();
        });

        VBox panelBot = new VBox(10, lblTotal, lblNota, btnDeshacer, btnComprar, lblMsg);
        panelBot.setPadding(new Insets(15));

        VBox contenido = new VBox(10, tablaCarrito, panelBot);
        VBox.setVgrow(tablaCarrito, Priority.ALWAYS);
        contenido.setPadding(new Insets(15));

        tab.setContent(contenido);
        return tab;
    }

    private Tab crearTabHistorial() {
        Tab tab = new Tab("📜 Historial (Cola)");

        TableView<Compra> tablaHistorial = new TableView<>();
        ObservableList<Compra> datos = FXCollections.observableArrayList();

        TableColumn<Compra, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setPrefWidth(130);

        TableColumn<Compra, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colUsuario.setPrefWidth(100);

        TableColumn<Compra, String> colDetalle = new TableColumn<>("Detalle");
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        colDetalle.setPrefWidth(350);

        TableColumn<Compra, Double> colTotal = new TableColumn<>("Total ($)");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setPrefWidth(90);

        tablaHistorial.getColumns().addAll(colFecha, colUsuario, colDetalle, colTotal);
        tablaHistorial.setItems(datos);

    
        Compra[] compras = historialCtrl.getComprasArray();
        for (Compra c : compras) datos.add(c);

        Label lblNota = new Label("Estructura: COLA (FIFO) — La primera compra aparece primero");
        lblNota.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6; -fx-font-style: italic;");

        Button btnRefrescar = new Button("🔄 Refrescar");
        btnRefrescar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnRefrescar.setOnAction(e -> {
            datos.clear();
            for (Compra c : historialCtrl.getComprasArray()) datos.add(c);
        });

        VBox contenido = new VBox(10, tablaHistorial, lblNota, btnRefrescar);
        VBox.setVgrow(tablaHistorial, Priority.ALWAYS);
        contenido.setPadding(new Insets(15));

        tab.setContent(contenido);
        return tab;
    }

    private void refrescarCatalogo() {
        datosCatalogo.clear();
        for (Libro l : catalogoCtrl.getLibrosArray()) datosCatalogo.add(l);
    }

    private void refrescarCarrito() {
        datosCarrito.clear();
        for (ItemCarrito i : carritoCtrl.getItemsArray()) datosCarrito.add(i);
        lblTotal.setText("Total: $" + String.format("%.0f", carritoCtrl.getTotal()));
    }
}
