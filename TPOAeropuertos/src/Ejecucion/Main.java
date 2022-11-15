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
            System.out.println();*/
        //DECOMENTAR Para probar que se devuelven bien los elementos





        //Por ahora el algoritmo funciona tomando el destino del vuelo que elegí
        //En el algoritmo voy a mandar en el parametro el vuelo a realizar en los llamados recursivos, para asi agregarlo a la lista de vuelos realizados y también mandarlo a este algoritmo de adyacentes

        //Prueba de si obtiene los adyacentes correctamente - FUNCIONA 14/11/22
        /*VectorTDA<Vuelo> vuelosAdyacentes = new Vector<>();
        vuelosAdyacentes.inicializarVector(conjuntoVuelos.capacidad());
        Vuelo pruebaAdyacente = conjuntoVuelos.elegir();
        System.out.println("ELEMENTO A PROBAR EN ADYACENTES = " + pruebaAdyacente.getNroVuelo() + " " + pruebaAdyacente.getAeropuertoDestino() + " " + pruebaAdyacente.getFechaAterrizaje());
        vuelosAdyacentes = obtenerAdyacentes(pruebaAdyacente, conjuntoVuelos);
        System.out.println("Hay adyacentes? = " + !vuelosAdyacentes.estaVacio());
        System.out.println();
        if (!vuelosAdyacentes.estaVacio()){
                for (int i = 0; i < vuelosAdyacentes.capacidadVector(); i++) {
                System.out.println(vuelosAdyacentes.recuperarElemento(i).getNroVuelo() + " | ORIGEN: " + vuelosAdyacentes.recuperarElemento(i).getAeropuertoOrigen() + " | DESTINO: " + vuelosAdyacentes.recuperarElemento(i).getAeropuertoDestino() + " | DESPUEGE: " + vuelosAdyacentes.recuperarElemento(i).getFechaDespegue());
            }
        }*/

        //Prueba de vector.cantidadElementos - FUNCIONA 14/11/22
        //System.out.println(vuelosAdyacentes.cantidadElementos());

        VectorTDA<Vuelo> vectorVacio = new Vector<>();
        vectorVacio.inicializarVector(conjuntoVuelos.capacidad());

        Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse("0000-00-00 00:00");
        System.out.println(temp);

        ConjuntoTDA<Vuelo> conjuntoVacio = new Conjunto<>();
        conjuntoVacio.inicializarConjunto();

        String puntoOrigen = "AEROPARQUE";

        VectorTDA<Vuelo> camino = new Vector<>();
        camino.inicializarVector(12);

        //Almacena que vuelos ya estan en otra solucion
        ConjuntoTDA<Vuelo> vuelosPrevios = new Conjunto<>();
        vuelosPrevios.inicializarConjunto();

        Tripulacion tripulacion = new Tripulacion(vectorVacio);

        //El primer "viaje" de la tripulacion es este con valor 1, para no considerarlo siempre que se trate
        //Sobre los vuelos de la tripulacion empezar del 1 en vez de 0
        //Era necesario por como hice el algoritmo de adyacentes - Lucas
        Vuelo primero = new Vuelo("1", puntoOrigen, puntoOrigen, temp, temp);
        tripulacion = realizarVuelos(conjuntoVacio, tripulacion, camino, 0, primero, conjuntoVuelos, vuelosPrevios, puntoOrigen);

        System.out.println();
        for (int i = 1; i < tripulacion.cantidadElementos(); i++) {
            System.out.println(tripulacion.getCamino().recuperarElemento(i).getNroVuelo());
            System.out.println(tripulacion.getCamino().recuperarElemento(i).getAeropuertoOrigen() + " " + tripulacion.getCamino().recuperarElemento(i).getAeropuertoDestino());
            System.out.println();
        }


    }

    public static ConjuntoTDA<Vuelo> leerDatosVuelos(String caminoDatos, String linea) throws IOException, ParseException {

        //Creacion de objectos necesarias
        BufferedReader lector = new BufferedReader(new FileReader(caminoDatos));
        ConjuntoTDA<Vuelo> conjunto = new Conjunto<>();
        conjunto.inicializarConjunto();

        //Para no considerar la primera línea que tiene los titulos nomas
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
                System.out.println(vuelo.getFechaAterrizaje());*/ //DECOMENTAR PARA PROBAR SI FUNCIONA
            }
        }
        return conjunto;
    }

    public static VectorTDA<Vuelo> obtenerAdyacentes(Vuelo origen, ConjuntoTDA<Vuelo> todosVuelos){
        VectorTDA<Vuelo> vectorVuelos = new Vector<>();
        vectorVuelos.inicializarVector(todosVuelos.capacidad());
        VectorTDA<Vuelo> vuelosAdyacentes = new Vector<>();
        vuelosAdyacentes.inicializarVector(4);
        vectorVuelos = todosVuelos.aVector();
        //j = Tengo que darle una posición al elemento en el vector
        int j = 0;
        for (int i = 0; i < vectorVuelos.capacidadVector(); i++){
            Vuelo v = vectorVuelos.recuperarElemento(i);
            //Si el origen del vuelo es el mismo que él a donde me llevo el vuelo en el que estoy
            if (Objects.equals(vectorVuelos.recuperarElemento(i).getAeropuertoOrigen(), origen.getAeropuertoDestino())){
                if(vectorVuelos.recuperarElemento(i).getFechaDespegue().compareTo(origen.getFechaAterrizaje()) > 0) {
                    //Si la hora del vuelo que estoy probando es después de haber llegado
                    //Compare to: Devuelve 1 si es mayor el horario. Si es 1 entonces ese vuelo seria "adyacente"
                    vuelosAdyacentes.agregarElemento(j, vectorVuelos.recuperarElemento(i));
                    j++;
                }
            }
        }
        return vuelosAdyacentes;
    }

    public static Tripulacion realizarVuelos(ConjuntoTDA<Vuelo> vuelosHechos, Tripulacion tripulacion, VectorTDA<Vuelo> caminoActual, int etapa, Vuelo vueloActual,
                                             ConjuntoTDA<Vuelo> todosVuelos, ConjuntoTDA<Vuelo> vuelosSolucionPrevia, String puntoOrigen){
        //Agregar el vuelo que acabo de agregar al camino actual
        caminoActual.agregarElemento(etapa, vueloActual);
        //Centrarme en conseguir camino más largo
        if (caminoActual.cantidadElementos() > tripulacion.cantidadElementos()){
            //Check de que haya vuelto al punto de origen
            if(Objects.equals(caminoActual.obtenerUltimoVuelo().getAeropuertoDestino(), puntoOrigen)){
                //Reemplaza mejor camino anterior por nuevo
                tripulacion = new Tripulacion(caminoActual.copiar());
                for (int k = 1;  k < tripulacion.cantidadElementos(); k++) {
                    System.out.println("Actualizado de Resultado: " + tripulacion.getCamino().recuperarElemento(k).getNroVuelo());
                }
            }
        }

        //Obtengo los viajes que posibles que puedo hacer de este punto
        VectorTDA<Vuelo> adyacentes = new Vector<>();
        adyacentes.inicializarVector(4);
        adyacentes = obtenerAdyacentes(vueloActual, todosVuelos);

        //Recorro todos los viajes posibles para hacer todos los caminos
        for (int i = 0; i < adyacentes.cantidadElementos(); i++) {
            Vuelo vueloSiguiente = adyacentes.recuperarElemento(i);
            if (!vuelosHechos.pertenece(vueloSiguiente)) {
                vuelosHechos.agregar(vueloSiguiente);
                tripulacion = realizarVuelos(vuelosHechos, tripulacion, caminoActual, etapa + 1, vueloSiguiente, todosVuelos, vuelosSolucionPrevia,puntoOrigen);
                vuelosHechos.sacar(vueloSiguiente);
            }
        }
        return tripulacion;
    }
}
