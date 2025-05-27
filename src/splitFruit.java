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
    private static final String splitFruitDir = "src/imgs/fruits";
    private static final File directory = new File(splitFruitDir);
    private static File[] splitFruitFiles = directory.listFiles();
    private static ArrayList<BufferedImage> fruitImages = new ArrayList<>();

    private BufferedImage part1;
    private BufferedImage part2;

    private double rotationAngle;
    private double angularVelocity;
    private int fruitID;

    static{
        if(splitFruitFiles != null){
            Arrays.sort(splitFruitFiles);
            for(File file : splitFruitFiles){
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

    public SplitFruit(int x, int y, int vx, int vy, double rotationAngle, double angularVelocity, int fruitID){
        this.rotationAngle = rotationAngle;
        this.angularVelocity = angularVelocity;
        this.fruitID = fruitID;

        part1 = fruitImages.get(fruitID * 2);
        part2 = fruitImages.get(fruitID * 2 + 1);
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

}
