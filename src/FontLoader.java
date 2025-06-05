import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    public static Font feastFont;

    static {
        try {
            feastFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/FEASFBRG.TTF")).deriveFont(Font.PLAIN, 48f); 
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(feastFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            feastFont = new Font("SansSerif", Font.BOLD, 48);
        }
    }

    public static void setSize(float size){
        try {
            feastFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/FEASFBRG.TTF")).deriveFont(Font.PLAIN, size); 
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(feastFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            feastFont = new Font("SansSerif", Font.BOLD, (int) size);
        }
    }
}
