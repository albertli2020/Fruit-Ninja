import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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

    static{
        if(fruitFiles != null){
            Arrays.sort(fruitFiles);
            for(File file : fruitFiles){
                if(file.isFile()){
                    String relativePath = "/imgs/fruits/" + file.getName();
                    BufferedImage img = loadImage(relativePath);
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
        sprite = resizeFruitHeight(fruitImages.get(random.nextInt(0, fruitImages.size())), 75);

        //fruit shoudl have projectile motion, vx stays constant, vy changes with accleration from gravity, initial vy should be negative

    }


    private BufferedImage resizeFruitWidth(BufferedImage originalFruit, int newWidth){
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

    private BufferedImage resizeFruitHeight(BufferedImage originalFruit, int newHeight){
        int originalWidth = originalFruit.getWidth();
        int originalHeight = originalFruit.getHeight();
        if (newHeight == originalHeight) {
            return originalFruit;
        }

        double scaleFactor = (double) newHeight / originalHeight;
        int newWidth = (int) (originalWidth * scaleFactor);

        Image resBG = originalFruit.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedFruit = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        resizedFruit.getGraphics().drawImage(resBG, 0, 0, null);
        return resizedFruit;
    }


    @Override
    public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(x <= -20 || x >= 1430){
            vx *= -1;
        }
		super.paint(g);
	}

}
