import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class FruitFragment extends Sprite{
    private double rotationAngle;
    private double angularVelocity;

    public FruitFragment(int x, int y, int vx, int vy, BufferedImage sprite, double rotationAngle, double angularVelocity){
        super(x, y, vx, vy, sprite.getWidth(), sprite.getHeight());
        this.sprite = sprite;
        this.rotationAngle = rotationAngle;
        this.angularVelocity = angularVelocity;
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
	}

}
