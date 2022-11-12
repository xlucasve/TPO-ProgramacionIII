import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Tablero {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.print("Valor de Y: ");
        int y = scan.nextInt();
        System.out.print("Valor de X: ");
        int x = scan.nextInt();

        Random r = new Random();
        ArrayList<Celda> todasCeldas = new ArrayList<>();

        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                Celda c = new Celda(i, j, r.nextInt(30));
                todasCeldas.add(c);
            }
        }

        Celda celdaInicial = new Celda(3, 3, 0);

        for (Celda c : todasCeldas){
            if (c.getY() == 3 && c.getX() == 3){
                celdaInicial.setPeso(c.getPeso());
            }
        }

        ArrayList<Celda> visitados = new ArrayList<>();
        ArrayList<Celda> mejorCaminoTemp = new ArrayList<>();
        int mejorPeso = 99999999;


        ArrayList<Celda> solucion = reyTablero(todasCeldas, visitados, mejorPeso, mejorCaminoTemp, 0, celdaInicial, 1);

        System.out.println("");
        System.out.println("Impresión de Datos");
        int cont = 0;
        for (Celda c: solucion){
            System.out.println(cont + " Y: " + c.getY() + " X: " + c.getX());
            cont++;
        }

    }

    public static ArrayList<Celda> reyTablero(ArrayList<Celda> todasCeldas, ArrayList<Celda> visitados, int mejorPeso,
                                              ArrayList<Celda> mejorCamino, int pesoActual, Celda celdaActual, int etapa) {
        //Sumar el peso de este movimiento
        pesoActual += etapa * celdaActual.getPeso();

        visitados.add(celdaActual);

        //Obtener lista de adyacentes
        ArrayList<Celda> adyacentes = obtenerAdyacentes(celdaActual, todasCeldas, visitados);

        if (visitados.size() == todasCeldas.size()) {
            if (pesoActual < mejorPeso) {
                mejorPeso = pesoActual;
                mejorCamino = new ArrayList<Celda>(visitados);
            }
        }
        //Recorrer cada adyacente
        for (Celda c : adyacentes) {
            mejorCamino = reyTablero(todasCeldas, visitados, mejorPeso, mejorCamino, pesoActual, c, etapa + 1);
            adyacentes.remove(c);
        }
        return mejorCamino;
    }

    public static ArrayList<Celda> obtenerAdyacentes(Celda celdaActual, ArrayList<Celda> todasCeldas, ArrayList<Celda> visitados){
        int valorY = celdaActual.getY();
        int valorX = celdaActual.getX();
        ArrayList<Celda> celdasDevolver = new ArrayList<>();
        for(int i = (valorY-1); i < (valorY+2); i++){
            for (int j = (valorX-1); j < (valorX+2); j++){
                for(Celda c : todasCeldas){
                    if (c.getY() == i && c.getX() == j && !visitados.contains(c)){
                        celdasDevolver.add(c);
                    }
                }
            }
        }
        return celdasDevolver;
    }
}

