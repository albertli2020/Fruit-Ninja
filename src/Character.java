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

public class Character extends Sprite{
    private static final Random random = new Random();
    private static final String characterDir = "src/imgs/characters";
    private static final File directory = new File(characterDir);
    private static final File[] characterFiles = directory.listFiles();
    private static final ArrayList<BufferedImage> characterImages = new ArrayList<>();

    private int characterID;

    private double rotationAngle;
    private double angularVelocity;

    static{
        if(characterFiles != null){
            Arrays.sort(characterFiles);
            for(File file : characterFiles){
                if(file.isFile()){
                    String relativePath = "/imgs/characters/" + file.getName();
                    BufferedImage img = resizeHeight(loadImage(relativePath), 75);
                    if(img != null){
                        characterImages.add(img);
                    }else{
                        System.out.println("Failed to load image: " + file.getName());
                    }
                }
            }
        }
    }

    public Character(int x, int y, int vx, int vy){
        super(x, y, vx, vy, 0, 0);

        characterID = random.nextInt(0, characterImages.size());
        sprite = characterImages.get(characterID);

        this.width = sprite.getWidth();
        this.height = sprite.getHeight();

        rotationAngle = Math.random()*10;
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

	}

    public SplitCharacter split(){
        return new SplitCharacter(x, y, vx, vy, rotationAngle, angularVelocity, characterID, width);
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
