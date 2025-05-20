import java.awt.Graphics;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Trail {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final WeakReference<Trail> selfReference;
    
    private static final int delayMS = 200;
    private int x, y;
    private int pX, pY;

    private int[] xPoints;
    private int[] yPoints;
    private int frame;

    public Trail(int x, int y){
        this.x = x;
        this.y = y;
        pX = x - 1;
        pY = y - 1;
        frame = 0;

        this.selfReference = new WeakReference<>(this);
        xPoints = new int[3];
        yPoints = new int[3];
        xPoints[0] = x - 3;
        xPoints[1] = x + 3;
        xPoints[2] = x + 3;
        yPoints[0] = y;
        yPoints[1] = y - 3;
        yPoints[2] = y + 3;

        getRotation(Math.toRadians(getAngle()));
        scheduleDestruction(delayMS); //trail expires after 200 ms        
    }


    public Trail(int x, int y, int pX, int pY){
        this.x = x;
        this.y = y;
        this.selfReference = new WeakReference<>(this);
        frame = 0;

        xPoints = new int[3];
        yPoints = new int[3];
        xPoints[0] = x - 3;
        xPoints[1] = x + 3;
        xPoints[2] = x + 3;
        yPoints[0] = y;
        yPoints[1] = y - 3;
        yPoints[2] = y + 3;

        getRotation(Math.toRadians(getAngle()));
        scheduleDestruction(delayMS); //trail expires after 200 ms        
    }

    private void scheduleDestruction(int delayMS) {
        scheduler.schedule(() -> {
            Trail obj = selfReference.get();
            if (obj != null) {
            }
        }, delayMS, TimeUnit.MILLISECONDS);
    }

    private double getAngle(){
        double deltaX = pX - x;
        double deltaY = pY - y;
        double angleInRadians = Math.atan2(deltaY, deltaX);
        return Math.toDegrees(angleInRadians);
    }

    private void thin(){

    }

    private void getRotation(double angle){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for(int i = 0; i < 3; i++){
            int[] rotated = rotatePoint(new int[] {xPoints[i], yPoints[i]}, this.x, this.y, cos, sin);
            xPoints[i] = rotated[0];
            yPoints[i] = rotated[1];
        }
    }

    private int[] rotatePoint(int[] point, int cx, int cy, double cos, double sin){
        int dx = point[0] - cx;
        int dy = point[1] - cy;

        int[] rotated = new int[2];
        rotated[0] = (int) (dx * cos - dy * sin);
        rotated[1] = (int) (dx * sin + dy * cos);
        return rotated;
    }

    public void paint(Graphics g){
        if(frame == 1){
            getRotation(Math.toRadians(360.0 - getAngle()));
        }
        frame++;
        thin();
        g.drawPolygon(xPoints, yPoints, 3);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    
}
