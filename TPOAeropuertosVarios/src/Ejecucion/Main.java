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
        ConjuntoTDA<Vuelo> todosVuelosConjunto = leerDatosVuelos(datosVuelos, "");
        VectorTDA<Vuelo> todosVuelosVector = new Vector<>();
        todosVuelosVector.inicializarVector(todosVuelosConjunto.capacidad());
        todosVuelosVector = todosVuelosConjunto.aVector();



        //Lectura de las tripulaciones
        VectorTDA<Tripulacion> tripulaciones = calcularTripulaciones(datosTripulaciones, "", todosVuelosConjunto.capacidad());

        //Creacion de estructuras necesarias para el algoritmo realizarVuelos
        ConjuntoTDA<Vuelo> conjuntoVacio = new Conjunto<>();
        conjuntoVacio.inicializarConjunto();


        VectorTDA<Vuelo> camino = new Vector<>();
        camino.inicializarVector(12);

        //Conjunto que contenga los vuelos hechos por otras tripulaciones
        ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones = new Conjunto<>();
        vuelosOtrasTripulaciones.inicializarConjunto();

        System.out.println("Buscando mejor camino...");
        tripulaciones = realizarVuelos(todosVuelosConjunto, todosVuelosVector, 0, vuelosOtrasTripulaciones, tripulaciones, 0, Integer.MAX_VALUE);
        int t = 0;
        System.out.println();
        while (t < tripulaciones.capacidadVector()) {
            try {
                System.out.println("VUELOS TRIPULACION " + t);
                for (int vuelo = 1; vuelo < tripulaciones.recuperarElemento(t).getCamino().cantidadElementos(); vuelo++) {
                    System.out.println(tripulaciones.recuperarElemento(t).getCamino().recuperarElemento(vuelo).getNroVuelo());
                }
                t++;
            } catch (IndexOutOfBoundsException e) {
                t++;
            }
        }

        System.out.println("COSTO DEL CAMINO: " + tripulaciones.recuperarElemento(0).getCostoCamino());

    }


    private static VectorTDA<Tripulacion> calcularTripulaciones(String caminoDatos, String linea, int capacidadVector) throws IOException, ParseException {
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
                VectorTDA<Vuelo> vectorVacio = new Vector<>();
                vectorVacio.inicializarVector(capacidadVector);
                VectorTDA<Vuelo> vectorVacioDos = new Vector<>();
                vectorVacioDos.inicializarVector(capacidadVector);
                String[] datos = linea.split(",");
                tripulaciones.agregarElemento(i, new Tripulacion(vectorVacio, vectorVacioDos, 0));
                //Se le añade un viaje al camino temp de cada tripulacion, ya que
                //necesita un vuelo para sacar el punto de partida para el algoritmo de adyacentes
                Vuelo primero = new Vuelo("0", datos[1], datos[1], temp, temp);
                tripulaciones.recuperarElemento(i).getCaminoTemp().agregarElemento(tripulaciones.recuperarElemento(i).getCaminoTemp().getSiguiente(), primero);
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

                Vuelo vuelo = new Vuelo(nrovuelo, origen, destino, fechaSalida, fechaLlegada);
                conjunto.agregar(vuelo);

            }
        }
        lector.close();
        return conjunto;
    }

    public static boolean esAdyacente(Vuelo vueloAHacer, ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones, Vuelo ultimoVueloTripulacion) {
        if(vuelosOtrasTripulaciones.pertenece(vueloAHacer)){
            return false;
        }
        //Check si el vuelo despega después de que hayamos aterrizado
        //Fecha menor = -1; Fechas iguales = 0; Fecha mayor = 1
        if(vueloAHacer.getFechaDespegue().compareTo(ultimoVueloTripulacion.getFechaAterrizaje()) > 0){
            //Chek si el vuelo sale del mismo aeropuerto en el que me encuentro
            return Objects.equals(vueloAHacer.getAeropuertoOrigen(), ultimoVueloTripulacion.getAeropuertoDestino());
        }else{
            return false;
        }
    }

    public static long calcularCosto(Vuelo vueloLlegada, Vuelo vueloSaliente) {
        long diferencia = vueloLlegada.getFechaAterrizaje().getTime() - vueloSaliente.getFechaDespegue().getTime();
        long diferenciaHoras = diferencia / (60 * 60 * 1000); //Pasamos de milisegundos a horas
        diferenciaHoras = diferenciaHoras * -1;
        if (diferenciaHoras > 2) { //Check si supera el tiempo de espera permitido sin multa
            return (diferenciaHoras) - 2; //El costo es constante así que devolvemos el nùmero que represente las horas extras
        } else {
            return 0;
        }
    }

    public static VectorTDA<Tripulacion> realizarVuelos(ConjuntoTDA<Vuelo> todosVuelosConjunto, VectorTDA<Vuelo> todosVuelosVector, long costoActual, ConjuntoTDA<Vuelo> vuelosOtrasTripulaciones,
                                                        VectorTDA<Tripulacion> tripulaciones, long costoAgregar, long mejorCosto) {

        costoActual = costoActual + costoAgregar;
        //Check si se realizaron todos los vuelos
        if (vuelosOtrasTripulaciones.capacidad() == todosVuelosConjunto.capacidad()) {
            //Check si el costo actual es mejor al mejorCosto previamente encontrado
            if (costoActual < mejorCosto) {
                boolean estanDevuelta = true;
                for (int t = 0; t < tripulaciones.capacidadVector(); t++) {
                    //Check si todas las tripulaciones estan devuelta en su punto de origen
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
            //RECORRER TODOS LOS VUELOS
            for(int v = 0; v < todosVuelosVector.capacidadVector(); v++) {
                //ASIGNAR EL VUELO A UNA TRIPULACION
                for (int siguienteTripulacion = 0; siguienteTripulacion < tripulaciones.capacidadVector(); siguienteTripulacion++) {
                    //SE AÑADE EL VUELO A LA TRIPULACION SI ES QUE ES ADYACENTE, SINO NO SE AÑADE Y ELIGE OTRA TRIPULACION
                    if (esAdyacente(todosVuelosVector.recuperarElemento(v), vuelosOtrasTripulaciones, tripulaciones.recuperarElemento(siguienteTripulacion).obtenerUltimoVuelo())){
                        //Se calcula el costo al asignar el segundo vuelo, ya que el primer vuelo asignado sería el inicial y no tendría sentido
                        if(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().getSiguiente() > 1) {
                            costoAgregar = calcularCosto(tripulaciones.recuperarElemento(siguienteTripulacion).obtenerUltimoVuelo(), todosVuelosVector.recuperarElemento(v));
                        }
                        vuelosOtrasTripulaciones.agregar(todosVuelosVector.recuperarElemento(v));
                        tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().agregarElemento(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().getSiguiente(), todosVuelosVector.recuperarElemento(v));
                        tripulaciones = realizarVuelos( todosVuelosConjunto,todosVuelosVector, costoActual,vuelosOtrasTripulaciones, tripulaciones, costoAgregar, mejorCosto);
                        vuelosOtrasTripulaciones.sacar(todosVuelosVector.recuperarElemento(v));
                        tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().eliminarElemento(tripulaciones.recuperarElemento(siguienteTripulacion).getCaminoTemp().getSiguiente()-1);
                    }
                }
            }
        }
        return tripulaciones;
    }
}
