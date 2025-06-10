import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Life extends Sprite{
    private boolean counter;
    private static BufferedImage blankLife = loadImage("/imgs/blankLife.png");
    private static BufferedImage fullLife = loadImage("/imgs/fullLife.png");
    private int lifeNum;
    private boolean filled;

    public Life(int x, boolean filled){
        super(x, 800, 0, -10, 0, 0);
        fixed = false;
        this.filled = filled;
        counter = false;
        if(filled){
            sprite = resizeHeight(fullLife, 60);
        }else{
            sprite = blankLife;
        }
    }

    public Life(int lifeNum){
        super(970 + (int) (Math.pow(1.3, (lifeNum - 1)) * 150), 30, 0, 0, 40, 40);
        super.fixed();
        counter = true;
        filled = false;
        sprite = blankLife;
        this.lifeNum = lifeNum;
        scaleHeight = Math.pow(1.3, lifeNum - 1) + 0.1;
        scaleWidth = Math.pow(1.3, lifeNum - 1) + 0.1;
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
        super.paint(g);
    }

    public void lifeLost(){
        sprite = fullLife;
    }

    public void reset(){
        sprite = blankLife;
    }
}
