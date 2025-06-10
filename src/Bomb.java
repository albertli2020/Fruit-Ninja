import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bomb extends Sprite {
    private static final Random random = new Random();
    private static final BufferedImage bomb = resizeHeight(loadImage("/imgs/bomb.png"), 75);
    private static final BufferedImage boom = resizeHeight(loadImage("/imgs/boom.png"), 150);

    private double rotationAngle;
    private double angularVelocity;

    public Bomb(int x, int y, int vx, int vy){
        super(x, y, vx, vy, 0, 0);
        sprite = bomb;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        rotationAngle = Math.random() * 10;
        angularVelocity = random.nextDouble(-0.2, 0.2);
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
    public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();

		if(x <= -20 || x >= 1380){
            vx *= -1;
        }
        x += vx;
        y += vy;
        cntr ++;
		if(cntr % 3 == 0){
			if(!fixed) vy += GRAVITY;
		}

        if(y >= 900) vy = 0;

        rotationAngle += angularVelocity;
        
        g2.translate(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
        g2.rotate(rotationAngle);
        g2.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
        g2.drawImage(sprite, 0, 0, null);

        g2.setTransform(originalTransform);

        if(Frame.debugging){
			g.setColor(Color.green);
			g.drawRect(x, y, width, height);
		}
	}

    public void rotate(float angle){
        this.rotationAngle = angle;
    }

    public void setAngularVelocity(float w){
        angularVelocity = w;
    }

    public void explode(){
        sprite = boom;
        rotationAngle = 0;
        angularVelocity = 0;
        vx = 0;
        vy = 0;
        fixed = true;
    }

    public boolean slice(float fx, float fy) {
        AffineTransform inverse = new AffineTransform();
        inverse.translate(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
        inverse.rotate(rotationAngle);
        inverse.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);

        Point2D.Float mouse = new Point2D.Float(fx, fy);
        Point2D.Float local = new Point2D.Float();
        try {
            inverse.inverseTransform(mouse, local);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
            return false;
        }

        int lx = (int) local.getX();
        int ly = (int) local.getY();

        if (lx >= 0 && ly >= 0 && lx < sprite.getWidth() && ly < sprite.getHeight()) {
            int alpha = (sprite.getRGB(lx, ly) >> 24) & 0xff;
            if (alpha > 10) {
                return true;
            }
        }
        return false;
    }

}
