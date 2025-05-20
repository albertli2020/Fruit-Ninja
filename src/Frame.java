import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	private static final Random random = new Random();
	public static boolean debugging = false;
	public static boolean simpleMovement = true;
	//Timer related variables
	Background background;
	long ellapseTime = 0;
	Font timeFont = new Font("Courier", Font.BOLD, 70);
	Font myFont = new Font("Courier", Font.BOLD, 40);

	int width = 1422;
	int height = 800;
	
	private ArrayList<Life> lives = new ArrayList<>();
	private ArrayList<Fruit> fruits = new ArrayList<>();
	private ArrayList<SplitFruit> fruitRemnants = new ArrayList<>();
	private ArrayList<Trail> trail = new ArrayList<>();

	public void paint(Graphics g) {
		Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		super.paintComponent(g);
		//Trail t = trail.get(trail.size() - 1);
		//trail.add(new Trail(mouseLoc.x, mouseLoc.y, t.getX(), t.getY()));
		background.draw(g);
		drawLives(g);
		drawFruits(g);
		//drawTrail(g);
		//System.out.println(trail.size());
		if(debugging) System.out.println(mouseLoc);
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		background = new Background("default.png");
		JFrame f = new JFrame("Fruit Ninja");
		f.setSize(new Dimension(width, height));
		f.setBackground(Color.white);
		f.add(this);
		f.setResizable(false);
		f.addMouseListener(this);
		f.addKeyListener(this);

		for(int i = 1; i <= 3; i++){
			lives.add(new Life(i));
		}

		for(int i = 1; i <= 20; i++){
			fruits.add(new Fruit());
		}
		
		//trail.add(new Trail(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y));

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("cursor.png").getImage().getScaledInstance(46, 40, Image.SCALE_DEFAULT),
				new Point(0,0),"custom cursor"));	
		
		Timer t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		
	
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if(keyCode == 82){
			resetLives();
		}
		// 38 = up, 40 = down, 37 = left, 39 = right, 82 = r
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void drawLives(Graphics g){
		for(Life life : lives){
			life.paint(g);
		}
	}

	public void drawFruits(Graphics g){
		for(Fruit fruit : fruits){
			fruit.paint(g);
		}
	}

	public void resetLives(){
		for(Life life : lives){
			life.update();
		}
	}

	public void drawTrail(Graphics g){
		for(Trail t : trail){
			if(t != null){
				t.paint(g);
			}
		}
	}
}