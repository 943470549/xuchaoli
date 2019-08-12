package krq;

public class Zuobiao{
    int x;
    int y;
    public Zuobiao(int x, int y){
        this.x = x;
        this.y = y;
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

    public boolean equal(Zuobiao zuobiao){
        return x == zuobiao.x && y == zuobiao.y;
    }
}
