import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class SplitFruit{
    private static final Random random = new Random();
    private static final String splitFruitDir = "src/imgs/splitFruits";
    private static final File directory = new File(splitFruitDir);
    private static File[] splitFruitFiles = directory.listFiles();
    private static ArrayList<BufferedImage> fruitImages = new ArrayList<>();

    private FruitFragment part1;
    private FruitFragment part2;

    static{
        if(splitFruitFiles != null){
            Arrays.sort(splitFruitFiles);
            for(File file : splitFruitFiles){
                if(file.isFile()){
                    String relativePath = "/imgs/splitFruits/" + file.getName();
                    BufferedImage img;
                    if(file.getName().contains("persimmon")){
                        img = resizeFruitWidth(loadImage(relativePath), 85);
                    }else{
                        img = resizeFruitHeight(loadImage(relativePath), 75);
                    }

                    if(img != null){
                        fruitImages.add(img);
                    }else{
                        System.out.println("Failed to load image: " + file.getName());
                    }
                }
            }
        }
    }

    public SplitFruit(int x, int y, int vx, int vy, double rotationAngle, double angularVelocity, int fruitID, int width){
        part1 = new FruitFragment(x, y, vx * -1, vy, fruitImages.get(fruitID * 2), rotationAngle, angularVelocity);
        part2 = new FruitFragment(x + width / 2, y, vx, vy, fruitImages.get(fruitID * 2 + 1), rotationAngle, angularVelocity * -1);
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

    private static BufferedImage resizeFruitWidth(BufferedImage originalFruit, int newWidth){
        int originalWidth = originalFruit.getWidth();
        int originalHeight = originalFruit.getHeight();
        if (newWidth == originalWidth) {
            return originalFruit;
        }

        double scaleFactor = (double) newWidth / originalWidth;
        int newHeight = (int) (originalHeight * scaleFactor);

        Image resBG = originalFruit.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedFruit = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        resizedFruit.getGraphics().drawImage(resBG, 0, 0, null);
        return resizedFruit;
    }


    private static BufferedImage loadImage(String path) {
        BufferedImage img = null;
        try {
            URL imageURL = Sprite.class.getResource(path);
            img = ImageIO.read(imageURL); // Load the image as a BufferedImage
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public void paint(Graphics g){
        part1.paint(g);
        part2.paint(g);        
    }

}
