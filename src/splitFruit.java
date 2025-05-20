import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SplitFruit extends Sprite{
    private static final Random random = new Random();
    private static final String splitFruitDir = "src/imgs/fruits";
    private static final File directory = new File(splitFruitDir);
    private static File[] splitFruitFiles = directory.listFiles();
    private static ArrayList<BufferedImage> fruitImages = new ArrayList<>();


    public SplitFruit(){
        super(0, 0,0 ,0, 0, 0);
    }
}
