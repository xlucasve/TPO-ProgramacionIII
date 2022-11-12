public class Celda {
    private int y;
    private int x;
    private int peso;

    public Celda(int y, int x, int peso) {
        this.y = y;
        this.x = x;
        this.peso = peso;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
