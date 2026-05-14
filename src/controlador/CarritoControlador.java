package tiendaLibros.controlador;

import tiendaLibros.model.ItemCarrito;
import tiendaLibros.model.Libro;
import tiendaLibros.model.estructuras.Pila;


public class CarritoControlador {


    private Pila<ItemCarrito> carrito;
    private double total;

    public CarritoControlador() {
        carrito = new Pila<>();
        total = 0;
    }

  
    public boolean agregar(Libro libro, int cantidad) {
        if (libro.getStock() < cantidad) return false;
        ItemCarrito item = new ItemCarrito(libro, cantidad);
        carrito.apilar(item);
        total += item.getSubtotal();
        return true;
    }

 
    public ItemCarrito quitarUltimo() {
        ItemCarrito item = carrito.desapilar();
        if (item != null) total -= item.getSubtotal();
        return item;
    }

   
    public ItemCarrito verUltimo() {
        return carrito.verTope();
    }

    
    public ItemCarrito[] getItemsArray() {
        Object[] arr = carrito.toArray();
        ItemCarrito[] items = new ItemCarrito[arr.length];
        for (int i = 0; i < arr.length; i++) items[i] = (ItemCarrito) arr[i];
        return items;
    }

    public void vaciar() {
        carrito.vaciar();
        total = 0;
    }

    public double getTotal() { return total; }
    public boolean estaVacio() { return carrito.estaVacia(); }
    public int getCantidadItems() { return carrito.getTamanio(); }
    public Pila<ItemCarrito> getCarrito() { return carrito; }
}
