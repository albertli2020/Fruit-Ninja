import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Life extends Sprite{
    private boolean counter;
    private static BufferedImage blankLife = loadImage("/imgs/blankLife.png");
    private static BufferedImage fullLife = loadImage("/imgs/fullLife.png");
    private int lifeNum;
    private boolean filled;

    public Life(){
        super(0, 0, 0, 0, 0, 0);
        counter = false;
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

    @Override  
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
    }

    public void update(){
        filled = !filled;
        if(filled){
            sprite = fullLife;
        }else{
            sprite = blankLife;
        }
    }

}
