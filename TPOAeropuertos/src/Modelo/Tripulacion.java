package Modelo;

import tda.VectorTDA;
import tda.impl.Vector;

public class Tripulacion {

    private VectorTDA<Vuelo> camino;

    public Tripulacion(VectorTDA<Vuelo> camino) {
        this.camino = camino;
    }

    public VectorTDA<Vuelo> getCamino() {
        return camino;
    }

    public void setCamino(VectorTDA<Vuelo> camino) {
        this.camino = camino;
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
