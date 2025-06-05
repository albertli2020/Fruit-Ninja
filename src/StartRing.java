import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class StartRing extends Sprite{
    private static final Random random = new Random();
    private static final BufferedImage startRing = resizeHeight(loadImage("/imgs/startRing.png"), 225);
 
    private double rotationAngle;
    private double angularVelocity;

    
    public StartRing(int x, int y, double rotation){
        super(x, y, 0, 0, 0, 0);
        this.sprite = startRing;
        this.fixed = true;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        rotationAngle = Math.random() * 10;
        this.angularVelocity = rotation;

    }

    private static BufferedImage resizeHeight(BufferedImage originalImage, int newHeight){
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        if (newHeight == originalHeight) {
            return originalImage;
        }

        double scaleFactor = (double) newHeight / originalHeight;
        int newWidth = (int) (originalWidth * scaleFactor);

        Image resFruit = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(resFruit, 0, 0, null);
        return resizedImage;
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();

        rotationAngle += angularVelocity;
        g2.translate(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
        g2.rotate(rotationAngle);
        g2.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
        g2.drawImage(sprite, 0, 0, null);

        g2.setTransform(originalTransform);
    }
    

}
