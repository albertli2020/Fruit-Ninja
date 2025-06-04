import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class SplitCharacter {
    private CharacterFragment part1;
    private CharacterFragment part2;

    private static final String splitCharactersDir = "src/imgs/splitCharacters";
    private static final File directory = new File(splitCharactersDir);
    private static File[] splitCharactersFiles = directory.listFiles();
    private static ArrayList<BufferedImage> splitCharactersImages = new ArrayList<>();

    static{
        if(splitCharactersFiles != null){
            Arrays.sort(splitCharactersFiles);
            for(File file : splitCharactersFiles){
                if(file.isFile()){
                    String relativePath = "/imgs/splitCharacters/" + file.getName();
                    BufferedImage img = resizeHeight(loadImage(relativePath), 75);
                    if(img != null){
                        splitCharactersImages.add(img);
                    }else{
                        System.out.println("Failed to load image: " + file.getName());
                    }
                }
            }
        }
    }

    public SplitCharacter(int x, int y, int vx, int vy, double rotationAngle, double angularVelocity, int characterID, int width){
        part1 = new CharacterFragment(x, y, vx * -1, vy, splitCharactersImages.get(characterID * 2), rotationAngle, angularVelocity);
        part2 = new CharacterFragment(x + width / 2, y, vx, vy, splitCharactersImages.get(characterID * 2 + 1), rotationAngle, angularVelocity * -1);
    }

    public void paint(Graphics g){
        part1.paint(g);
        part2.paint(g);        
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

    public CharacterFragment getPart1(){
        return part1;
    }

    public CharacterFragment getPart2(){
        return part2;
    }

}
