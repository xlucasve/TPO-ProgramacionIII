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
        tablero = añadirValores(tablero, y, x);
        //Test para chequear que se impren los valores
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                System.out.print("Y" + i + " - X" + j + ":");
                System.out.println(tablero[i][j]);
            }
        }
    }

    public static int[][] añadirValores(int[][] tablero, int y, int x){
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
}
