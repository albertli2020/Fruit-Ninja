import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Background {
    private int width = 1422;
    private int height = 800;
    private BufferedImage background;
    public Background(String fileName){
        try {
            URL imageURL = getClass().getResource("/imgs/backgrounds/" + fileName);
            background = ImageIO.read(imageURL);
            Image resBG = background.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage resziedBG = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            resziedBG.getGraphics().drawImage(resBG, 0, 0, null);
            background = resziedBG;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g){
        g.drawImage(background, 0, 0, null);
    }

    public void setBackground(String fileName){
        try {
            URL imageURL = getClass().getResource("/imgs/backgrounds/" + fileName);
            background = ImageIO.read(imageURL);
            Image resBG = background.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage resziedBG = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            resziedBG.getGraphics().drawImage(resBG, 0, 0, null);
            background = resziedBG;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
