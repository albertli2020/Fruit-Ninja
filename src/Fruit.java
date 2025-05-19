import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Fruit extends Sprite{
    private static final Random random = new Random();
    private static final String fruitDir = "src/imgs/fruits";
    private static final File directory = new File(fruitDir);
    private static File[] fruitFiles = directory.listFiles();
    private static ArrayList<BufferedImage> fruitImages = new ArrayList<>();

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

    public Fruit(){
        super(0, 0, 0, 0, 0, 0);
    }



    @Override
    public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(x > 1250){
			x = -30;
		}
		else if(x + width < 0){
			x = 1250;
		}
		super.paint(g);
	}

}
