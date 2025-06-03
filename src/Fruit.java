import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Fruit extends Sprite{
    private static final Random random = new Random();
    private static final String fruitDir = "src/imgs/fruits";
    private static final File directory = new File(fruitDir);
    private static final File[] fruitFiles = directory.listFiles();
    private static final ArrayList<BufferedImage> fruitImages = new ArrayList<>();

    private double rotationAngle;
    private double angularVelocity;

    private int fruitID;

    static{
        if(fruitFiles != null){
            Arrays.sort(fruitFiles);
            for(File file : fruitFiles){
                if(file.isFile()){
                    String relativePath = "/imgs/fruits/" + file.getName();
                    BufferedImage img = resizeFruitHeight(loadImage(relativePath), 75);
                    if(img != null){
                        fruitImages.add(img);
                    }else{
                        System.out.println("Failed to load image: " + file.getName());
                    }
                }
            }
        }
    }

    public Fruit(int x, int y, int vx, int vy){
        super(x, y, vx, vy, 0, 0);
        //sprite = fruitImages.get(random.nextInt(0, fruitImages.size()));
        fruitID = random.nextInt(0, fruitImages.size());
        sprite = fruitImages.get(fruitID);
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        rotationAngle = Math.random()*10;
        angularVelocity = random.nextDouble(-0.2, 0.2);
        //fruit shoudl have projectile motion, vx stays constant, vy changes with accleration from gravity, initial vy should be negative

    }

    private static BufferedImage resizeFruitHeight(BufferedImage originalFruit, int newHeight){
        int originalWidth = originalFruit.getWidth();
        int originalHeight = originalFruit.getHeight();
        if (newHeight == originalHeight) {
            return originalFruit;
        }

        double scaleFactor = (double) newHeight / originalHeight;
        int newWidth = (int) (originalWidth * scaleFactor);

        Image resFruit = originalFruit.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedFruit = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        resizedFruit.getGraphics().drawImage(resFruit, 0, 0, null);
        return resizedFruit;
    }

    @Override
    public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();

		if(x <= -20 || x >= 1430){
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

        g2.fillOval(x, y, 10, 10);

        g2.translate(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
        g2.rotate(rotationAngle);
        g2.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
        g2.drawImage(sprite, 0, 0, null);

        

        /* 
        if (Frame.debugging) {
			g.setColor(Color.red);
			//get rotated hitbox
			Rectangle boundingBox = hitbox();
			
			int[] xPoints = {
				boundingBox.x, 
				boundingBox.x + boundingBox.width, 
				boundingBox.x + boundingBox.width, 
				boundingBox.x
			};
			int[] yPoints = {
				boundingBox.y, 
				boundingBox.y, 
				boundingBox.y + boundingBox.height, 
				boundingBox.y + boundingBox.height
			};

            System.out.println(xPoints[0] + ", " + yPoints[0]);
            System.out.println(xPoints[1] + ", " + yPoints[1]);
            System.out.println(xPoints[2] + ", " + yPoints[2]);
			
			g.drawPolygon(xPoints, yPoints, 4);
		}
        */


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
        
    }

    public SplitFruit split(){
        SplitFruit sf =  new SplitFruit(x, y, vx, vy, rotationAngle, angularVelocity, fruitID, width);
        System.out.println(sf.toString());
        return sf;
    }

    public boolean slice(float fx, float fy) {
        AffineTransform inverse = new AffineTransform();
        inverse.translate(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
        inverse.rotate(rotationAngle);
        inverse.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);

        // Transform the global point (fx, fy) to image-local coordinates
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
                System.out.println("Pixel-perfect segment hit!");
                return true;
            }
        }
        return false;
    }
}
