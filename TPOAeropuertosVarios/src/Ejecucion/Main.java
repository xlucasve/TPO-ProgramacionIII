package Ejecucion;

import Modelo.Tripulacion;
import Modelo.Vuelo;
import tda.ConjuntoTDA;
import tda.VectorTDA;
import tda.impl.Conjunto;
import tda.impl.Vector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String datosVuelos = "src/Recursos/Vuelos.csv";
        String datosTripulaciones = "src/Recursos/Tripulaciones.csv";

        //Lectura de los vuelos
        ConjuntoTDA<Vuelo> conjuntoVuelos = leerDatosVuelos(datosVuelos, "");
        VectorTDA<Vuelo> vectorVacio = new Vector<>();
        vectorVacio.inicializarVector(conjuntoVuelos.capacidad());
        VectorTDA<Vuelo> vectorVacioDos = new Vector<>();
        vectorVacioDos.inicializarVector(conjuntoVuelos.capacidad());
        //Lectura de las tripulaciones
        VectorTDA<Tripulacion> tripulaciones = calcularTripulaciones(datosTripulaciones, "", vectorVacio, vectorVacioDos);

        ConjuntoTDA<Vuelo> conjuntoVacio = new Conjunto<>();
        conjuntoVacio.inicializarConjunto();


        VectorTDA<Vuelo> camino = new Vector<>();
        camino.inicializarVector(12);

        //Conjunto que contenga los vuelos hechos por otras tripulaciones
        ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones = new Conjunto<>();
        vuelosOtrasTripulaciones.inicializarConjunto();

        VectorTDA<Vuelo> adyacentes = obtenerAdyacentes(tripulaciones.recuperarElemento(0).getCaminoTemp().recuperarElemento(0), conjuntoVuelos, conjuntoVuelos);
        for (int a = 0; a < adyacentes.cantidadElementos(); a++) {
            System.out.println(adyacentes.recuperarElemento(a).getAeropuertoOrigen());
            System.out.println(adyacentes.recuperarElemento(a).getAeropuertoDestino());
            System.out.println();
        }

        tripulaciones = realizarVuelos(1, conjuntoVuelos, 0, vuelosOtrasTripulaciones, tripulaciones, 0, Integer.MAX_VALUE, 1);
        int t = 0;

        while (true) {
            try {
                for (t = t; t < tripulaciones.cantidadElementos(); t++) {
                    System.out.println();
                    System.out.println("VUELOS TRIPULACION " + t);
                    for (int trip = 1; t < tripulaciones.recuperarElemento(t).getCamino().cantidadElementos(); trip++) {
                        System.out.println(tripulaciones.recuperarElemento(t).getCamino().recuperarElemento(trip).getNroVuelo());
                    }
                }
                break;
            } catch (IndexOutOfBoundsException e) {
                t++;
            }
        }

    }




    private static VectorTDA<Tripulacion> calcularTripulaciones(String caminoDatos, String linea, VectorTDA<Vuelo> vacio, VectorTDA<Vuelo> vacioDos) throws IOException, ParseException {
        VectorTDA<Tripulacion> tripulaciones = new Vector<>();

        BufferedReader contador = new BufferedReader(new FileReader(caminoDatos));
        int cont = 0;
        while ((linea = contador.readLine()) != null) {
            cont++;
        }
        BufferedReader lector = new BufferedReader(new FileReader(caminoDatos));
        tripulaciones.inicializarVector(cont);
        int i = 0;
        Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse("0000-00-00 00:00");

        boolean primeraLinea = true;
        while ((linea = lector.readLine()) != null) {
            if(primeraLinea){
                primeraLinea = false;
            }else {
                String[] datos = linea.split(",");
                tripulaciones.agregarElemento(i, new Tripulacion(vacio, vacioDos, 0));
                //Se le añade un viaje al camino temp de cada tripulacion, ya que
                //necesita un vuelo para sacar el punto de partida para el algoritmo de adyacentes
                Vuelo primero = new Vuelo("0", datos[1], datos[1], temp, temp);
                tripulaciones.recuperarElemento(i).getCaminoTemp().agregarElemento(0, primero);
                i++;
            }
        }
        return tripulaciones;
    }

    public static ConjuntoTDA<Vuelo> leerDatosVuelos(String caminoDatos, String linea) throws IOException, ParseException {

        //Creacion de objectos necesarias
        BufferedReader lector = new BufferedReader(new FileReader(caminoDatos));
        ConjuntoTDA<Vuelo> conjunto = new Conjunto<>();
        conjunto.inicializarConjunto();

        //Para no considerar la primera línea que tiene los titulos nomas
        boolean primeraLinea = true;
        while ((linea = lector.readLine()) != null) {
            if (primeraLinea) {
                primeraLinea = false;
            } else {
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
                System.out.println(vuelo.getFechaAterrizaje());*/ //DECOMENTAR PARA PROBAR SI FUNCIONA
            }
        }
        return conjunto;
    }

    public static VectorTDA<Vuelo> obtenerAdyacentes(Vuelo origen, ConjuntoTDA<Vuelo> todosVuelos, ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones) {
        VectorTDA<Vuelo> vectorVuelos = new Vector<>();
        vectorVuelos.inicializarVector(todosVuelos.capacidad());
        VectorTDA<Vuelo> vuelosAdyacentes = new Vector<>();
        vuelosAdyacentes.inicializarVector(4);
        vectorVuelos = todosVuelos.aVector();
        //j = Tengo que darle una posición al elemento en el vector
        int j = 0;
        for (int i = 0; i < vectorVuelos.capacidadVector(); i++) {
            //Si el origen del vuelo es el mismo que él a donde me llevo el vuelo en el que estoy
            if (Objects.equals(vectorVuelos.recuperarElemento(i).getAeropuertoOrigen(), origen.getAeropuertoDestino())) {
                if (vectorVuelos.recuperarElemento(i).getFechaDespegue().compareTo(origen.getFechaAterrizaje()) > 0) {
                    if(!vuelosOtrasTripulaciones.pertenece(vectorVuelos.recuperarElemento(i))) {
                        //Si la hora del vuelo que estoy probando es después de haber llegado
                        //Compare to: Devuelve 1 si es mayor el horario. Si es 1 entonces ese vuelo seria "adyacente"
                        vuelosAdyacentes.agregarElemento(j, vectorVuelos.recuperarElemento(i));
                        j++;
                    }
                }
            }
        }
        return vuelosAdyacentes;
    }

    public static long calcularCosto(Vuelo vueloLlegada, Vuelo vueloSaliente){
        long diferencia = vueloLlegada.getFechaAterrizaje().getTime() - vueloSaliente.getFechaDespegue().getTime();
        long diferenciaHoras = diferencia / (60 * 60 * 1000); //Pasamos de milisegundos a horas
        diferenciaHoras = diferenciaHoras*-1;
        if (diferenciaHoras > 2) { //Es el máximo que puede esperar una tripulación
            return (diferenciaHoras) - 2; //El costo es constante así que devolvemos el nùmero de horas extras
        } else{
            return 0;
        }
    }

    //Etapa tiene que empezar en 1, la posicion 0 de vuelo ya esta usada
    public static VectorTDA<Tripulacion> realizarVuelos(int etapa, ConjuntoTDA<Vuelo> todosVuelos, long costoActual, ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones,
                                                        VectorTDA<Tripulacion> tripulaciones, long costoAgregar, int mejorCosto, int intento) {
        System.out.println("MEJOR COSTO ACTUAL: " + mejorCosto);
        if (etapa > 1) {
            costoActual += costoAgregar;
        }
        System.out.println(costoActual);
       // if (vuelosOtrasTripulaciones.capacidad() == todosVuelos.capacidad()){
        System.out.println("COSTO ACTUAL: " + costoActual);
        System.out.println("MEJOR COSTO: " + mejorCosto);
        if(etapa > 1){
            if (costoActual <= mejorCosto) {
                boolean estanDevuelta = true;
                for (int t = 0; t < tripulaciones.capacidadVector(); t++) {
                    System.out.println("ESTAN DEVUELTA? ");
                    if (!Objects.equals(tripulaciones.recuperarElemento(t).obtenerUbicacion(), tripulaciones.recuperarElemento(t).getCaminoTemp().recuperarElemento(0).getAeropuertoOrigen())) {
                        estanDevuelta = false;
                        System.out.println("NO ESTAN DEVUELTA");
                        break;
                    }
                }
                if (estanDevuelta) {
                    System.out.println("VOLVIERON");
                    for (int t = 0; t < tripulaciones.capacidadVector(); t++) {
                        if (tripulaciones.recuperarElemento(t).getCaminoTemp().cantidadElementos() > tripulaciones.recuperarElemento(t).getCamino().cantidadElementos()) {
                            System.out.println("CAMINO ENCONTRADO NRO: " + intento);
                            System.out.println("MEJOR COSTO PREVIO: " + mejorCosto);
                            mejorCosto = (int) costoActual;
                            System.out.println("MEJOR COSTO ACTUAL: " + mejorCosto);
                            tripulaciones.recuperarElemento(t).setCamino(tripulaciones.recuperarElemento(t).getCaminoTemp().copiar());
                            System.out.println("HOLA : " + tripulaciones.recuperarElemento(t).getCamino().cantidadElementos());
                            intento++;
                        }
                    }
                }
            }
         //   }
        }
        for (int siguienteTripulacion = 0; siguienteTripulacion < tripulaciones.capacidadVector(); siguienteTripulacion++) {
            VectorTDA<Vuelo> adyacentes = obtenerAdyacentes(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().obtenerUltimoVuelo(), todosVuelos, vuelosOtrasTripulaciones);
            for (int a = 0; a < adyacentes.cantidadElementos(); a++) {
                if (etapa > 1) {
                    costoAgregar = calcularCosto(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().obtenerUltimoVuelo(), adyacentes.recuperarElemento(a));
                }
                tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().agregarElemento(etapa, adyacentes.recuperarElemento(a));
                vuelosOtrasTripulaciones.agregar(adyacentes.recuperarElemento(a));
                tripulaciones = realizarVuelos(etapa + 1, todosVuelos, costoActual, vuelosOtrasTripulaciones, tripulaciones, costoAgregar, mejorCosto, intento);
                tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().eliminarElemento(etapa);
                vuelosOtrasTripulaciones.sacar(adyacentes.recuperarElemento(a));
            }
        }
        return tripulaciones;
    }
}

    //TODO:
    //Hacer que viajen varias tripulaciones a la vez - IDEA: Hacer un for loop que pase por cada tripulacion
    //Y de allí ver si la tripulacion puede hacer algún viaje o no según lo que se esta analizando -- IDEA NO ME PARECE CORRECTA - Lucas
    //OTRA IDEA: Hacer un for loop adentro del algoritmo, antes de adyacentes, y que agarre los adyacentes de cada tripulacion