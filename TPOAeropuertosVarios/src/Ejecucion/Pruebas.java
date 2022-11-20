package Ejecucion;

import Modelo.Vuelo;
import tda.ConjuntoTDA;
import tda.VectorTDA;
import tda.impl.Vector;

import java.io.IOException;
import java.text.ParseException;

import static Ejecucion.Main.leerDatosVuelos;
import static Ejecucion.Main.obtenerAdyacentes;

public class Pruebas {

    public void adyacentesTest() throws IOException, ParseException {
        String datosAeropuertos = "src/Recursos/Aeropuertos.csv";
        String datosVuelos = "src/Recursos/Vuelos.csv";
        String datosTripulaciones = "src/Recursos/Tripulaciones.csv";
        ConjuntoTDA<Vuelo> conjuntoVuelos = leerDatosVuelos(datosVuelos, "");
        VectorTDA<Vuelo> vuelosAdyacentes = new Vector<>();
        vuelosAdyacentes.inicializarVector(conjuntoVuelos.capacidad());
        Vuelo pruebaAdyacente = conjuntoVuelos.elegir();
        System.out.println("Hay adyacentes? = " + !vuelosAdyacentes.estaVacio());
        System.out.println();
        if (!vuelosAdyacentes.estaVacio()){
            for (int i = 0; i < vuelosAdyacentes.capacidadVector(); i++) {
                System.out.println(vuelosAdyacentes.recuperarElemento(i).getNroVuelo() + " | ORIGEN: " + vuelosAdyacentes.recuperarElemento(i).getAeropuertoOrigen() + " | DESTINO: " + vuelosAdyacentes.recuperarElemento(i).getAeropuertoDestino() + " | DESPUEGE: " + vuelosAdyacentes.recuperarElemento(i).getFechaDespegue());
            }
        }
    }
}
