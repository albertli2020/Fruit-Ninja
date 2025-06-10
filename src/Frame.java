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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener, MouseMotionListener {
	Driver driver;

	private final double THINNING_RATE = 0.95;
    private final double MIN_WIDTH = 0.5;
	private static final Random random = new Random();
	public static boolean debugging = false;
	public static boolean simpleMovement = true;
	
	Background background;
	long ellapseTime = 0;
	Font timeFont = new Font("Courier", Font.BOLD, 70);
	Font myFont = new Font("Courier", Font.BOLD, 40);

	int width = 1422;
	int height = 800;

	private static String highScoreFile = "src/highScore.txt";

	private int livesLost;
	
	private ArrayList<Life> lives = new ArrayList<>();
	private ArrayList<Fruit> fruits = new ArrayList<>();
	private ArrayList<SplitFruit> fruitRemnants = new ArrayList<>();
	private ArrayList<TrailPoint> trail = new ArrayList<>();
	private ArrayList<Bomb> bombs = new ArrayList<>();
	private ArrayList<Character> characters = new ArrayList<>();
	private ArrayList<SplitCharacter> splitCharacters = new ArrayList<>();
	private ArrayList<Life> lostLives = new ArrayList<>();
	
	private Point lastMousePoint = null;
	private Point loc;

	private int score;
	private int highScore;

	public void paintComponent(Graphics g) {
		trail.add(new TrailPoint(loc.x + 2, loc.y - 18, 10));
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.CYAN);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		background.draw(g);
		drawFruits(g);

		drawCharaters(g);

		FontLoader.setSize(48);
		g2.setFont(FontLoader.feastFont);
		g2.setColor(new Color(157,72,5));
		g2.drawString("Score: " + score, 52, 52); // offset slightly for shadow

		g2.setColor(new Color(235,149,20));
		g2.drawString("Score: " + score, 50, 50);


		if(score > highScore) highScore = score;

		FontLoader.setSize(28);
		g2.setFont(FontLoader.feastFont);
		g2.setColor(new Color(157,72,5));
		g2.drawString("High Score: " + highScore, 52, 77); 

		g2.setColor(new Color(235,149,20));
		g2.drawString("High Score: " + highScore, 50, 75);

		drawBombs(g);

		if(livesLost >= 3){
			background.draw(g);

			FontLoader.setSize(55);
			g2.setFont(FontLoader.feastFont);
			g2.setColor(new Color(180,38,35));
			g2.drawString("Score: " + score, 502, 202); // offset slightly for shadow

			g2.setColor(new Color(227,35,35));
			g2.drawString("Score: " + score, 500, 200);


			FontLoader.setSize(80);
			g2.setFont(FontLoader.feastFont);
			g2.setColor(new Color(180,38,35));
			g2.drawString("Game Over", 502, 302); // offset slightly for shadow

			g2.setColor(new Color(227,35,35));
			g2.drawString("Game Over", 500, 300);


			FontLoader.setSize(55);
			g2.setFont(FontLoader.feastFont);

			g2.setColor(new Color(180,38,35));
			g2.drawString("Press 'r' to play again", 502, 402); // offset slightly for shadow

			g2.setColor(new Color(227,35,35));
			g2.drawString("Press 'r' to play again", 500, 400);


		}
		
		drawLostLives(g);
		drawLives(g);

		for (int i = 0; i < trail.size() - 1; i++) {
			TrailPoint p1 = trail.get(i);
			TrailPoint p2 = trail.get(i + 1);

			drawTrail(p1, p2, g2);
        }

		g.setColor(Color.CYAN);



		if(bombs.isEmpty() && fruits.isEmpty() && fruitRemnants.isEmpty()){
			resetFruits();
			resetBombs();
			resetCharacters();
		}
	}
	
	public Frame(Driver driver) {
		this.driver = driver;
		setPreferredSize(new Dimension(1422, 800));
        setFocusable(true);
        requestFocusInWindow();

		JFrame f = driver.frame;
		
		background = new Background("default.png");
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);

		setFocusable(true);
		requestFocusInWindow(); 

		try (Scanner scanner = new Scanner(new File(highScoreFile))) {
			while (scanner.hasNextLine()) {
				highScore = Integer.parseInt(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		score = 0;
		loc = MouseInfo.getPointerInfo().getLocation();

		for(int i = 1; i <= 3; i++){
			lives.add(new Life(i));
		}
		
		resetFruits();
		resetCharacters();
		resetBombs();
		
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
		loc = e.getPoint();

		if (lastMousePoint != null) {
			checkSegmentForCut(lastMousePoint, loc);
		}

    	lastMousePoint = loc;
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
			updateHighScore();
			resetLives();
			resetFruits();
			resetBombs();
			resetCharacters();
			score = 0;
			livesLost = 0;
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
		int numFruits = random.nextInt(0, 5);
		for(int i = 1; i <= numFruits; i++){
			int x = random.nextInt(100, 1200);
			int y = random.nextInt(800, 850);

			int vx = random.nextInt(-6, 6);
			int vy = random.nextInt(-20, -15);
			fruits.add(new Fruit(x, y, vx, vy));
		}
		fruitRemnants.clear();
	}

	public void resetCharacters(){
		characters.clear();
		if(random.nextInt(0, 10) > 7){
			int x = random.nextInt(100, 1200);
			int y = random.nextInt(800, 850);

			int vx = random.nextInt(-6, 6);
			int vy = random.nextInt(-20, -15);
			characters.add(new Character(x, y, vx, vy));
		}
		splitCharacters.clear();
	}

	public void resetBombs(){
		bombs.clear();
		int numBombs = random.nextInt(0, 2);
		for(int i = 1; i <= numBombs; i++){
			int x = random.nextInt(100, 1200);
			int y = random.nextInt(800, 850);

			int vx = random.nextInt(-6, 6);
			int vy = random.nextInt(-20, -15);
			bombs.add(new Bomb(x, y, vx, vy));
		}
	}

	public void drawFruits(Graphics g){
		for(int i = 0; i < fruits.size(); i++){
			Fruit fruit = fruits.get(i);
			fruit.paint(g);
			if(fruit.y >= 880){
				livesLost ++;
				lifeLost(fruit.x);
				fruits.remove(i);
			}
		}

		for(int i = 0; i < fruitRemnants.size(); i++){
			SplitFruit sf = fruitRemnants.get(i);
			if(sf.getFrag1().y >= 900 && sf.getFrag2().y >= 900){
				fruitRemnants.remove(i);
				i--;
				continue;
			}
			sf.paint(g);
		}
	}

	public void drawCharaters(Graphics g){
		for(Character character : characters){
			character.paint(g);
		}
		for(SplitCharacter sc : splitCharacters){
			if(sc.getPart1().y >= 1000 && sc.getPart2().y >= 1000){
				splitCharacters.remove(sc);
				continue;
			}
			sc.paint(g);
		}
		
	}

	public void drawBombs(Graphics g){
		for(int i = 0; i < bombs.size(); i++){
			Bomb bomb = bombs.get(i);
			bomb.paint(g);
			if(bomb.y >= 900){
				bombs.remove(i);
				i--;
			}
		}
	}

	public void resetLives(){
		for(Life life : lives){
			life.reset();
		}
	}

	public void checkSegmentForCut(Point p1, Point p2) {
		int steps = (int) p1.distance(p2);

		// Fruits
		ArrayList<Fruit> toRemoveFruits = new ArrayList<>();
		for (Fruit fruit : fruits) {
			for (int i = 0; i <= steps; i++) {
				float t = i / (float) steps;
				float x = (float)(p1.x + t * (p2.x - p1.x));
				float y = (float)(p1.y + t * (p2.y - p1.y));

				if (fruit.slice(x, y)) {
					fruitRemnants.add(fruit.split());
					toRemoveFruits.add(fruit);
					score += 100;
					break;
				}
			}
		}
		fruits.removeAll(toRemoveFruits);

		// Characters
		ArrayList<Character> toRemoveChars = new ArrayList<>();
		for (Character character : characters) {
			for (int i = 0; i <= steps; i++) {
				float t = i / (float) steps;
				float x = (float)(p1.x + t * (p2.x - p1.x));
				float y = (float)(p1.y + t * (p2.y - p1.y));

				if (character.slice(x, y)) {
					splitCharacters.add(character.split());
					toRemoveChars.add(character);
					score += 200;
					break;
				}
			}
		}

		for(int b = 0; b < bombs.size(); b++){
			for( int i = 0; i <= steps; i++){
				float t = i / (float) steps;
				float x = (float)(p1.x + t * (p2.x - p1.x));
				float y = (float)(p1.y + t * (p2.y - p1.y));

				Bomb bomb = bombs.get(b);
				if(bomb.slice(x, y)){
					livesLost = 10;
				}
			}
		}
		characters.removeAll(toRemoveChars);
	}

	private void killFruits(){
		for(Fruit f : fruits){
			fruitRemnants.add(f.split());
		}
		fruits.clear();
	}

	private void drawTrail(TrailPoint p1, TrailPoint p2, Graphics2D g2){
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;
		double len = Math.sqrt(dx * dx + dy * dy);
		if (len == 0) return;

		double nx = -dy / len;
		double ny = dx / len;

		int x1l = (int) (p1.x + nx * p1.width / 2);
		int y1l = (int) (p1.y + ny * p1.width / 2);
		int x1r = (int) (p1.x - nx * p1.width / 2);
		int y1r = (int) (p1.y - ny * p1.width / 2);

		int x2l = (int) (p2.x + nx * p2.width / 2);
		int y2l = (int) (p2.y + ny * p2.width / 2);
		int x2r = (int) (p2.x - nx * p2.width / 2);
		int y2r = (int) (p2.y - ny * p2.width / 2);

		float avgWidth = (float)((p1.width + p2.width) / 2.0);
		float alpha = Math.max(0f, Math.min(1f, avgWidth / 10f));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.setColor(Color.WHITE);

		Polygon triangle1 = new Polygon();
		triangle1.addPoint(x1l, y1l);
		triangle1.addPoint(x1r, y1r);
		triangle1.addPoint(x2l, y2l);
		g2.fillPolygon(triangle1);

		Polygon triangle2 = new Polygon();
		triangle2.addPoint(x2l, y2l);
		triangle2.addPoint(x1r, y1r);
		triangle2.addPoint(x2r, y2r);
		g2.fillPolygon(triangle2);
	}

	private void lifeLost(int x){
		if(livesLost > 3) return;
		Life life = lives.get(livesLost - 1);
		life.lifeLost();
		lostLives.add(new Life(x, true));
	}

	private void drawLostLives(Graphics g){
		if(lostLives.isEmpty()) return;
		for(int i = 0; i < lostLives.size(); i++){
			Life tempLife = lostLives.get(i);
			if(tempLife.y >= 900){
				lostLives.remove(i);
				i--;
				continue;
			}
			tempLife.paint(g);
		}
	}

	private void updateHighScore() {
	try {
		if (score > highScore) {
			highScore = score;
			java.io.PrintWriter writer = new java.io.PrintWriter(new File(highScoreFile));
			writer.println(highScore);
			writer.close();
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}

}