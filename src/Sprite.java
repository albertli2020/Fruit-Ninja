import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;


public class Sprite{
	protected BufferedImage sprite; 	
	protected AffineTransform tx;
	int dir = 0; 					//0-forward, 1-backward, 2-left, 3-right
	int width, height;
	int x, y;						//position of the object
	int vx, vy;						//movement variables
	double scaleWidth = 1.0;		//change to scale image
	double scaleHeight = 1.0; 		//change to scale image

	public Sprite(int x, int y, int vx, int vy, int width, int height) {
		//Initialize variables
		this.width = (int) (width * scaleWidth);
		this.height = (int) (height * scaleHeight);
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y); //Initialize the transform
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

        //update pos
		x+=vx;
		y+=vy;	
		init(x,y);

        //draw image
		g2.drawRenderedImage(sprite, tx);

        //draw debugging box around sprite
		if(Frame.debugging){
			g.setColor(Color.green);
			g.drawRect(x, y, width, height);
		}
	}
	
	public void updateX(int vx){
		this.vx = vx;
	}
	
	public void updateY(int vy){
		this.vy = vy;
	}
        
    public void move(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void stop(){
        this.vx = 0;
        this.vy = 0;
    }

    protected void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(scaleWidth, scaleHeight);
	}

    protected static BufferedImage loadImage(String path) {
        BufferedImage img = null;
        try {
            URL imageURL = Sprite.class.getResource(path);
            img = ImageIO.read(imageURL); // Load the image as a BufferedImage
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public boolean collision(Rectangle r){
        Rectangle hitbox = new Rectangle(x, y, width, height);
        return hitbox.intersects(r);
    }

    public Rectangle hitbox(){
        return new Rectangle(x, y, width, height);
    }



    /*
    private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Monkey.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
    */
}
