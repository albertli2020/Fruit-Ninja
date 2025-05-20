import java.awt.image.BufferedImage;

public class Bomb extends Sprite {
    private static BufferedImage bomb = loadImage("/imgs/bomb.png");
    private static BufferedImage boom = loadImage("/imgs/boom.png");

    public Bomb(){
        super(0, 0, 0, 0, 0, 0);
    }
}
