package tiendaLibros.controlador;

import tiendaLibros.model.Compra;
import tiendaLibros.model.ItemCarrito;
import tiendaLibros.model.estructuras.Cola;
import tiendaLibros.util.ArchivoUtil;


public class HistorialControlador {

   
    private Cola<Compra> historial;

    public HistorialControlador() {
        historial = new Cola<>();
        cargarDesdeArchivo();
    }


    public void registrarCompra(String usuario, ItemCarrito[] items, double total) {
        StringBuilder detalle = new StringBuilder();
        for (ItemCarrito item : items) {
            detalle.append(item.getLibro().getTitulo())
                   .append(" x").append(item.getCantidad()).append("; ");
        }
        Compra compra = new Compra(usuario, detalle.toString(), total);
        historial.encolar(compra);
        ArchivoUtil.agregarHistorial(compra.toString());
    }

   
    public Compra[] getComprasArray() {
        Object[] arr = historial.toArray();
        Compra[] compras = new Compra[arr.length];
        for (int i = 0; i < arr.length; i++) compras[i] = (Compra) arr[i];
        return compras;
    }

 
    private void cargarDesdeArchivo() {
        String[] lineas = ArchivoUtil.leerHistorial();
        for (String linea : lineas) {
            
            String[] p = linea.split("\\|");
            if (p.length == 4) {
                try {
                    double total = Double.parseDouble(p[3].trim().replace("$", "").replace(",", ""));
                    Compra c = new Compra(p[1].trim(), p[2].trim(), total);
                    historial.encolar(c);
                } catch (NumberFormatException e) {
                  
                }
            }
        }
    }

    public Cola<Compra> getHistorial() { return historial; }
}
