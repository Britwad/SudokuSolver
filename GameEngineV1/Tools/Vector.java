package GameEngineV1.Tools;

public class Vector {

    public float x, y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector vector) {
        this.x+=vector.x;
        this.y+=vector.y;
    }
    public void add(float x, float y) {
        this.x+=x;
        this.y+=y;
    }
    public void subtract(Vector vector) {
        this.x-=vector.x;
        this.y-=vector.y;
    }
    public void subtract(float x, float y) {
        this.x-=x;
        this.y-=y;
    }
    public void scale(float m) {
        this.x*=m;
        this.y*=m;
    }
    public float distance() {
        return (float) Math.sqrt(x*x+y*y);
    }
}
