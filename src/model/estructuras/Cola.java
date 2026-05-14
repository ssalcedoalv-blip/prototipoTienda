package tiendaLibros.model.estructuras;

import tiendaLibros.model.nodos.Nodo;

/**
 * COLA (QUEUE) - FIFO
 * Implementación propia sin usar java.util.Queue.
 * Usada para: Historial de compras (primera compra = primera en mostrarse).
 */
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> final_;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.final_ = null;
        this.tamanio = 0;
    }

    /** Encola un elemento (enqueue) */
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            final_ = nuevo;
        } else {
            final_.siguiente = nuevo;
            final_ = nuevo;
        }
        tamanio++;
    }

    /** Desencola el elemento del frente (dequeue) */
    public T desencolar() {
        if (estaVacia()) return null;
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) final_ = null;
        tamanio--;
        return dato;
    }

    /** Ver el frente sin eliminarlo (peek) */
    public T verFrente() {
        if (estaVacia()) return null;
        return frente.dato;
    }

    /** Retorna todos los elementos como arreglo */
    public Object[] toArray() {
        Object[] arr = new Object[tamanio];
        Nodo<T> actual = frente;
        int i = 0;
        while (actual != null) {
            arr[i++] = actual.dato;
            actual = actual.siguiente;
        }
        return arr;
    }

    public boolean estaVacia() { return frente == null; }
    public int getTamanio() { return tamanio; }
}
