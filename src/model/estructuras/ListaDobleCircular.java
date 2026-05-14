package tiendaLibros.model.estructuras;

import tiendaLibros.model.nodos.Nodo;


public class ListaDobleCircular<T> {
    private Nodo<T> cabeza;
    private int tamanio;

    public ListaDobleCircular() {
        this.cabeza = null;
        this.tamanio = 0;
    }


    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza; // apunta a sí mismo
            cabeza.anterior = cabeza;
        } else {
            Nodo<T> ultimo = cabeza.anterior; // el anterior de cabeza es el último
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
        }
        tamanio++;
    }


    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (actual.dato.equals(dato)) {
                if (tamanio == 1) {
                    cabeza = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) {
                        cabeza = actual.siguiente;
                    }
                }
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

   
    public T buscar(java.util.function.Predicate<T> condicion) {
        if (cabeza == null) return null;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (condicion.test(actual.dato)) return actual.dato;
            actual = actual.siguiente;
        }
        return null;
    }


    public boolean actualizar(java.util.function.Predicate<T> condicion, T nuevoDato) {
        if (cabeza == null) return false;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (condicion.test(actual.dato)) {
                actual.dato = nuevoDato;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

   
    public Object[] toArray() {
        if (cabeza == null) return new Object[0];
        Object[] arr = new Object[tamanio];
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            arr[i] = actual.dato;
            actual = actual.siguiente;
        }
        return arr;
    }

    public T siguiente(T actual) {
        Nodo<T> nodo = buscarNodo(actual);
        if (nodo != null) return nodo.siguiente.dato;
        return null;
    }

   
    public T anterior(T actual) {
        Nodo<T> nodo = buscarNodo(actual);
        if (nodo != null) return nodo.anterior.dato;
        return null;
    }

    private Nodo<T> buscarNodo(T dato) {
        if (cabeza == null) return null;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (actual.dato.equals(dato)) return actual;
            actual = actual.siguiente;
        }
        return null;
    }

    public int getTamanio() { return tamanio; }
    public boolean estaVacia() { return cabeza == null; }
    public Nodo<T> getCabeza() { return cabeza; }
}
