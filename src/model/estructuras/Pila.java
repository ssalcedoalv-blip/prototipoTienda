package tiendaLibros.model.estructuras;

import tiendaLibros.model.nodos.Nodo;

public class Pila<T> {
    private Nodo<T> tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }


    public T desapilar() {
        if (estaVacia()) return null;
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) return null;
        return tope.dato;
    }

   
    public Object[] toArray() {
        Object[] arr = new Object[tamanio];
        Nodo<T> actual = tope;
        int i = 0;
        while (actual != null) {
            arr[i++] = actual.dato;
            actual = actual.siguiente;
        }
        return arr;
    }

    public boolean estaVacia() { return tope == null; }
    public int getTamanio() { return tamanio; }

  
    public void vaciar() {
        tope = null;
        tamanio = 0;
    }
}
