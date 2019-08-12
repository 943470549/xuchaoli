package krq;

public class Move{
    Zuobiao begin;
    Zuobiao end;
    boolean isGun;
    float score = 0;
    public Move(int x1, int y1, int x2, int y2, boolean isGun){
        this.begin = new Zuobiao(x1, y1);
        this.end = new Zuobiao(x2, y2);
        this.isGun = isGun;
    }


    public Zuobiao getBegin() {
        return begin;
    }

    public void setBegin(Zuobiao begin) {
        this.begin = begin;
    }

    public Zuobiao getEnd() {
        return end;
    }

    public void setEnd(Zuobiao end) {
        this.end = end;
    }

    public boolean isGun() {
        return isGun;
    }

    public void setGun(boolean gun) {
        isGun = gun;
    }
}
