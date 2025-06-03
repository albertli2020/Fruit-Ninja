public class TrailPoint {
    private final double THINNING_RATE = 0.95;
    private final double MIN_WIDTH = 0.5;
    public final int x, y;
    public double width;

    public TrailPoint(int x, int y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void thin(double rate){
        width *= rate;
    }    
}
