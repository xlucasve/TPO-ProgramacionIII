package tda.impl;

import Modelo.Vuelo;
import tda.VectorTDA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vector<E> implements VectorTDA<E> {
    private List<E> vector;

    @Override
    public void agregarElemento(int posicion, E elemento) {
        if (this.vector.size() > posicion && this.vector.get(posicion) != null) {
            this.vector.set(posicion, elemento);
        } else {
            this.vector.add(posicion, elemento);
        }
    }

    @Override
    public int capacidadVector() {
        return this.vector.size();
    }

    @Override
    public void eliminarElemento(int posicion) {
        this.vector.remove(posicion);
    }

    @Override
    public void inicializarVector(int n) {
        this.vector = new ArrayList<>(n);
    }

    @Override
    public E recuperarElemento(int posicion) {
        return this.vector.get(posicion);
    }

    @Override
    public boolean contieneElemento(E elemento) {
        return this.vector.contains(elemento);
    }

    @Override
    public VectorTDA<E> copiar() {
        VectorTDA<E> copia = new Vector<>();
        copia.inicializarVector(vector.size());
        for (int i = 0; i < vector.size(); i++) {
            copia.agregarElemento(i, vector.get(i));
        }
        return copia;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "vector=" + vector +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector<?> vector1 = (Vector<?>) o;
        return Objects.equals(vector, vector1.vector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vector);
    }

    public boolean estaVacio() {
        try {
            this.recuperarElemento(0);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }
    public int cantidadElementos(){
        int cont = 0;
        try{
            for (int i = 0; i < this.capacidadVector(); i++) {
                this.recuperarElemento(i);
                cont++;
            }
        } catch (NullPointerException ignored){
            //Dejo recorrer toda la lista por si hay algún elemento suelto
        }
        return cont;
    }


    public Vuelo obtenerUltimoVuelo() {
        Vuelo ultimo = null;
        try {
            for (int i = 0; i < this.capacidadVector(); i++) {
                ultimo = (Vuelo) this.recuperarElemento(i);
            }
        } catch (NullPointerException ignored) {
        }
        return ultimo;
    }
}