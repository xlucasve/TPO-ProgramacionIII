import java.util.Random;
import java.util.Scanner;


public class Ejecucion {
    public static void main(String[] args) {
        //Indicacion de valores de tablero
        Scanner scan = new Scanner(System.in);
        System.out.print("Valor de x: ");//Columnas
        int x = scan.nextInt();
        System.out.print("Valor de y: ");//Filas
        int y = scan.nextInt();
        scan.close();

        //Creacion de tablero
        int[][] tablero = new int[y][x];
        tablero = añadirValores(tablero, x, y);
        //Test para chequear que se impren los valores
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                System.out.print("Y" + i + " - X" + j + ":");
                System.out.print(tablero[i][j]);
                System.out.print("     ");
            }
            System.out.println("");
        }
        recorrerAdyacentes(tablero, 1, 1);
    }

    public static int[][] añadirValores(int[][] tablero, int x, int y){
        Random r = new Random();
        int numero;
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                numero = r.nextInt(101); //(0, 1, 2, ..., 101) No inclusive el 101
                tablero[i][j] = numero;
            }
        }
        return tablero;
    }
    int[]
    public static void recorrerAdyacentes(int[][] tablero, int x, int y) {
        int valory = y;
        int valorx = x;
        try {
            for (int i = valory - 1; i < valory+2; i++) {
                for (int j = valorx-1; j < valorx+2; j++) {
                    System.out.println("Y" + i + " X" + j);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Fuera de limite");
        }
    }

    //public static boolean posicionValida
}
