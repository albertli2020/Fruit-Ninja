import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener, MouseMotionListener {
	private final double THINNING_RATE = 0.95;
    private final double MIN_WIDTH = 0.5;
	private static final Random random = new Random();
	public static boolean debugging = true;
	public static boolean simpleMovement = true;

	//Timer related variables
	Background background;
	long ellapseTime = 0;
	Font timeFont = new Font("Courier", Font.BOLD, 70);
	Font myFont = new Font("Courier", Font.BOLD, 40);

	int width = 1422;
	int height = 800;
	int frame;
	
	private ArrayList<Life> lives = new ArrayList<>();
	private ArrayList<Fruit> fruits = new ArrayList<>();
	private ArrayList<SplitFruit> fruitRemnants = new ArrayList<>();
	private ArrayList<TrailPoint> trail = new ArrayList<>();
	private ArrayList<Bomb> bombs = new ArrayList<>();

	private final ArrayList<TrailPoint> trailPoints = new ArrayList<>();

	private Point loc;

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.CYAN);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		background.draw(g);
		drawLives(g);
		drawFruits(g);
		g.fillOval(loc.x, loc.y, 10, 10);

		//drawBombs(g);
		for (int i = 0; i < trail.size() - 1; i++) {
			TrailPoint p1 = trail.get(i);
			TrailPoint p2 = trail.get(i + 1);

			drawTrail(p1, p2, g2);
        }
		g.fillOval(300, 300, 10, 10);

		g.setColor(Color.CYAN);
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		background = new Background("default.png");
		JFrame f = new JFrame("Fruit Ninja");
		f.setSize(new Dimension(width, height));
		f.add(this);
		f.setBackground(Color.white);
		f.setResizable(false);
		f.addMouseListener(this);
		f.addKeyListener(this);
		f.addMouseMotionListener(this);


		loc = MouseInfo.getPointerInfo().getLocation();


		for(int i = 1; i <= 3; i++){
			lives.add(new Life(i));
		}
		
		resetFruits();
		//resetBombs();
		
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
	public void mouseMoved(MouseEvent e) {
        trail.add(new TrailPoint(e.getX() + 2, e.getY() - 18, 10));
		
		loc = new Point(e.getX(), e.getY() - 26);

		detectCuts();
    }

	@Override
	public void mouseDragged(MouseEvent e){
		mouseMoved(e);
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Iterator<TrailPoint> it = trail.iterator();
        while (it.hasNext()) {
            TrailPoint p = it.next();
            p.thin(THINNING_RATE);
            if (p.width < MIN_WIDTH) {
                it.remove();
            }
        }
		repaint();
	}

	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if(keyCode == 82){
			resetLives();
			resetFruits();
			resetBombs();
		}else if(keyCode == 81){
			explodeAll();
			killFruits();
			System.out.println("boom");
		}
		// 38 = up, 40 = down, 37 = left, 39 = right, 82 = r, 22 = q
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

	public void resetFruits(){
		fruits.clear();
		for(int i = 1; i <= 1; i++){
			int x = random.nextInt(100, 1200);
			int y = random.nextInt(800, 850);

			int vx = random.nextInt(-6, 6);
			int vy = random.nextInt(-20, -15);
			fruits.add(new Fruit(x, y, vx, vy));
		}

		fruitRemnants.clear();
	}

	public void resetBombs(){
		bombs.clear();
		for(int i = 1; i <= 3; i++){
			int x = random.nextInt(100, 1200);
			int y = random.nextInt(800, 850);

			int vx = random.nextInt(-6, 6);
			int vy = random.nextInt(-20, -15);
			bombs.add(new Bomb(x, y, vx, vy));
		}
	}

	public void drawFruits(Graphics g){
		for(Fruit fruit : fruits){
			fruit.paint(g);
		}

		for(SplitFruit sf : fruitRemnants){
			sf.paint(g);
		}
	}

	public void drawBombs(Graphics g){
		for(Bomb bomb : bombs){
			bomb.paint(g);
		}
	}

	public void resetLives(){
		for(Life life : lives){
			life.update();
		}
	}

	public void detectCuts(){
		for(int i = 0; i < fruits.size(); i++){
			Fruit f = fruits.get(i);
			if(f.slice(loc.x, loc.y)){
				fruitRemnants.add(f.split());
				fruits.remove(i);
				i--;
			}
		}
	}

	private void killFruits(){
		for(Fruit f : fruits){
			fruitRemnants.add(f.split());
		}
		fruits.clear();
	}

	private void explodeAll(){
		for(Bomb bomb : bombs){
			bomb.explode();
		}
	}

	private void drawTrail(TrailPoint p1, TrailPoint p2, Graphics2D g2){

			double dx = p2.x - p1.x;
			double dy = p2.y - p1.y;
			double len = Math.sqrt(dx * dx + dy * dy);
			if (len == 0) return;

			// Normal vector (perpendicular)
			double nx = -dy / len;
			double ny = dx / len;

			// Left and right offset points for both ends
			int x1l = (int) (p1.x + nx * p1.width / 2);
			int y1l = (int) (p1.y + ny * p1.width / 2);
			int x1r = (int) (p1.x - nx * p1.width / 2);
			int y1r = (int) (p1.y - ny * p1.width / 2);

			int x2l = (int) (p2.x + nx * p2.width / 2);
			int y2l = (int) (p2.y + ny * p2.width / 2);
			int x2r = (int) (p2.x - nx * p2.width / 2);
			int y2r = (int) (p2.y - ny * p2.width / 2);

			// Set fading alpha based on average width
			float avgWidth = (float)((p1.width + p2.width) / 2.0);
			float alpha = Math.max(0f, Math.min(1f, avgWidth / 10f));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.setColor(Color.WHITE);

			// Triangle 1
			Polygon triangle1 = new Polygon();
			triangle1.addPoint(x1l, y1l);
			triangle1.addPoint(x1r, y1r);
			triangle1.addPoint(x2l, y2l);
			g2.fillPolygon(triangle1);

			// Triangle 2
			Polygon triangle2 = new Polygon();
			triangle2.addPoint(x2l, y2l);
			triangle2.addPoint(x1r, y1r);
			triangle2.addPoint(x2r, y2r);
			g2.fillPolygon(triangle2);
	}
}