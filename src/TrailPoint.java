public class TrailPoint {
    public final int x, y;
    public double width;

    public TrailPoint(int x, int y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void thin(double rate) {
        width *= rate;
    }    
}
