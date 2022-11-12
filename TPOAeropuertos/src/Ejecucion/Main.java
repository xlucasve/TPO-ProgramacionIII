package Ejecucion;

import Modelo.Vuelo;
import tda.ConjuntoTDA;
import tda.VectorTDA;
import tda.impl.Conjunto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String datosAeropuertos = "src/Recursos/Aeropuertos.csv";
        String datosVuelos = "src/Recursos/Vuelos.csv";
        String datosTripulaciones = "src/Recursos/Tripulaciones.csv";
        ConjuntoTDA<Vuelo> conjuntoVuelos = leerDatosVuelos(datosVuelos, "");
        /*VectorTDA<Vuelo> listadoVuelos = conjuntoVuelos.aVector();
        for (int i = 0; i < conjuntoVuelos.capacidad(); i++){
            System.out.print(listadoVuelos.recuperarElemento(i).getNroVuelo()+" ");
            System.out.print(listadoVuelos.recuperarElemento(i).getAeropuertoOrigen()+" ");
            System.out.print(listadoVuelos.recuperarElemento(i).getAeropuertoDestino()+" ");
            System.out.print(listadoVuelos.recuperarElemento(i).getFechaDespegue()+" ");
            System.out.print(listadoVuelos.recuperarElemento(i).getFechaAterrizaje()+" ");
            System.out.println();DECOMENTAR SI*/
    }

    public static ConjuntoTDA<Vuelo> leerDatosVuelos(String caminoDatos, String linea) throws IOException, ParseException {
        BufferedReader lector = new BufferedReader(new FileReader(caminoDatos));
        ConjuntoTDA<Vuelo> conjunto = new Conjunto<>();
        conjunto.inicializarConjunto();
        boolean primeraLinea = true;
        while ((linea = lector.readLine()) != null){
            if (primeraLinea){
                primeraLinea = false;
            }else {
                String[] datos = linea.split(",");
                String nrovuelo = datos[0];
                String origen = datos[1];
                String destino = datos[2];
                //sdf = SimpleDateFormat
                DateFormat salidasdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                //Parse: Transforma el sdf a Date
                Date fechaSalida = salidasdf.parse(datos[3]);
                DateFormat llegadasdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date fechaLlegada = llegadasdf.parse(datos[4]);
                //Comparar Fechas: fechaSalida.compareTo(fechaLlegada)
                //Menor = -1; Iguales = 0; Mayor = 1
                Vuelo vuelo = new Vuelo(nrovuelo, origen, destino, fechaSalida, fechaLlegada);
                conjunto.agregar(vuelo);
                /*System.out.println(vuelo.getNroVuelo());
                System.out.println(vuelo.getAeropuertoOrigen());
                System.out.println(vuelo.getAeropuertoDestino());
                System.out.println(vuelo.getFechaDespegue());
                System.out.println(vuelo.getFechaAterrizaje());*DECOMENTAR PARA PROBAR SI FUNCIONA*/
            }
        }
        return conjunto;
    }

    private static void realizarVuelos(ConjuntoTDA<Vuelo> todosVuelos){
    }
}
