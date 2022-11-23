package tda;

import Modelo.Vuelo;

public interface VectorTDA<E> {
    void agregarElemento(int posicion, E elemento);

    int capacidadVector();

    void eliminarElemento(int posicion);

    void inicializarVector(int n);

    E recuperarElemento(int posicion);

    boolean contieneElemento(E elemento);

    VectorTDA<E> copiar();

    boolean estaVacio();

    int cantidadElementos();

    Vuelo obtenerUltimoVuelo();

    int getSiguiente();
}
