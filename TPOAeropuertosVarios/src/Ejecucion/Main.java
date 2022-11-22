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

        tripulaciones = realizarVuelos(1, conjuntoVuelos, 0, vuelosOtrasTripulaciones, tripulaciones, 0, Integer.MAX_VALUE);
        int t = 0;

        while (t < tripulaciones.cantidadElementos()) {
            try {
                System.out.println();
                System.out.println("VUELOS TRIPULACION " + t);
                for (int vuelo = 1; t < tripulaciones.recuperarElemento(t).getCamino().cantidadElementos(); vuelo++) {
                    System.out.println(tripulaciones.recuperarElemento(t).getCamino().recuperarElemento(vuelo).getNroVuelo());
                }
                t++;
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
        contador.close();

        boolean primeraLinea = true;
        while ((linea = lector.readLine()) != null) {
            if (primeraLinea) {
                primeraLinea = false;
            } else {
                String[] datos = linea.split(",");
                tripulaciones.agregarElemento(i, new Tripulacion(vacio, vacioDos, 0));
                //Se le añade un viaje al camino temp de cada tripulacion, ya que
                //necesita un vuelo para sacar el punto de partida para el algoritmo de adyacentes
                Vuelo primero = new Vuelo("0", datos[1], datos[1], temp, temp);
                tripulaciones.recuperarElemento(i).getCaminoTemp().agregarElemento(0, primero);
                i++;
            }
        }
        lector.close();
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

            }
        }
        lector.close();
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
                    if (!vuelosOtrasTripulaciones.pertenece(vectorVuelos.recuperarElemento(i))) {
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

    public static long calcularCosto(Vuelo vueloLlegada, Vuelo vueloSaliente) {
        long diferencia = vueloLlegada.getFechaAterrizaje().getTime() - vueloSaliente.getFechaDespegue().getTime();
        long diferenciaHoras = diferencia / (60 * 60 * 1000); //Pasamos de milisegundos a horas
        diferenciaHoras = diferenciaHoras * -1;
        if (diferenciaHoras > 2) { //Es el máximo que puede esperar una tripulación
            return (diferenciaHoras) - 2; //El costo es constante así que devolvemos el nùmero de horas extras
        } else {
            return 0;
        }
    }

    //Etapa tiene que empezar en 1, la posicion 0 de vuelo ya esta usada
    public static VectorTDA<Tripulacion> realizarVuelos(int etapa, ConjuntoTDA<Vuelo> todosVuelos, long costoActual, ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones,
                                                        VectorTDA<Tripulacion> tripulaciones, long costoAgregar, long mejorCosto) {
        if (etapa > 1) {
            costoActual += costoAgregar;
        }

        //Se realizaron todos los vuelos
        if (vuelosOtrasTripulaciones.capacidad() == todosVuelos.capacidad()) {
            //El costo que se encontro es mejor o el mismo
            if (costoActual <= mejorCosto) {
                boolean estanDevuelta = true;
                for (int t = 0; t < tripulaciones.capacidadVector(); t++) {
                    //Regresaron todas las tripulaciones a su origen??
                    if (!Objects.equals(tripulaciones.recuperarElemento(t).obtenerUbicacion(), tripulaciones.recuperarElemento(t).getCaminoTemp().recuperarElemento(0).getAeropuertoOrigen())) {
                        estanDevuelta = false;
                    }
                }
                //Regresaron - Guardar los datos de cada tripulacion
                if (estanDevuelta) {
                    mejorCosto = costoActual;
                    tripulaciones.recuperarElemento(0).setCostoCamino(mejorCosto);
                    for (int t = 0; t < tripulaciones.capacidadVector(); t++) {
                        tripulaciones.recuperarElemento(t).setCamino(tripulaciones.recuperarElemento(t).getCaminoTemp().copiar());
                    }
                }
            }
        } else {
            //Cambiar de tripulacion
            for (int siguienteTripulacion = 0; siguienteTripulacion < tripulaciones.capacidadVector(); siguienteTripulacion++) {
                //Obtener adyacentes de esta tripulacion
                VectorTDA<Vuelo> adyacentes = obtenerAdyacentes(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().obtenerUltimoVuelo(), todosVuelos, vuelosOtrasTripulaciones);
                for (int a = 0; a < adyacentes.cantidadElementos(); a++) {
                    if (etapa > 1) {
                        costoAgregar = calcularCosto(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().obtenerUltimoVuelo(), adyacentes.recuperarElemento(a));
                    }
                    //Guardar vuelo en camino temporal y en vuelos ya hechos para siguiente llamado
                    tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().agregarElemento(etapa, adyacentes.recuperarElemento(a));
                    vuelosOtrasTripulaciones.agregar(adyacentes.recuperarElemento(a));
                    tripulaciones = realizarVuelos(etapa + 1, todosVuelos, costoActual, vuelosOtrasTripulaciones, tripulaciones, costoAgregar, mejorCosto);
                    tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().eliminarElemento(etapa);
                    vuelosOtrasTripulaciones.sacar(adyacentes.recuperarElemento(a));
                }
            }
        }
        return tripulaciones;
    }
}
