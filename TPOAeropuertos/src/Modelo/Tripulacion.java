package Modelo;

import tda.VectorTDA;
import tda.impl.Vector;

public class Tripulacion {

    private VectorTDA<Vuelo> camino;
    private long costoCamino;

    public Tripulacion(VectorTDA<Vuelo> camino, long costoCamino) {
        this.camino = camino;
        this.costoCamino = costoCamino;
    }

    public VectorTDA<Vuelo> getCamino() {
        return camino;
    }

    public void setCamino(VectorTDA<Vuelo> camino) {
        this.camino = camino;
    }

    public long getCostoCamino() {
        return costoCamino;
    }

    public void setCostoCamino(long costoCamino) {
        this.costoCamino = costoCamino;
    }

    public int cantidadElementos(){
        int cont = 0;
        try{
            for (int i = 0; i < this.camino.capacidadVector(); i++) {
                this.camino.recuperarElemento(i);
                cont++;
            }
        } catch (NullPointerException ignored){
            //Dejo al algoritmo recorrer toda la lista por si hay algún elemento suelto
        }
        return cont;
    }
}
