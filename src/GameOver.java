import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
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

public class GameOver extends JPanel implements ActionListener, MouseListener, KeyListener, MouseMotionListener{	
	Driver driver;
	private final double THINNING_RATE = 0.95;
    private final double MIN_WIDTH = 0.5;

	private static final Random random = new Random();
	int width = 1422;
	int height = 800;
	
	private ArrayList<TrailPoint> trail = new ArrayList<>();

	private Point loc;
	private Point lastMousePoint = null;

	private Fruit startFruit;
	private StartRing startRing;
	private SplitFruit fruitRemnant = null;
	
	Background background;
	private boolean start = false;

	public void paintComponent(Graphics g) {
		if (loc != null){
    		trail.add(new TrailPoint(loc.x + 2, loc.y - 18, 10));
		}
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.CYAN);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		background.draw(g);

		FontLoader.setSize(78);
		g2.setFont(FontLoader.feastFont);
		g2.setColor(new Color(157,72,5));
		g2.drawString("Fruit Ninja", 252, 353);
		g2.setColor(new Color(235,149,20));
		g2.drawString("Fruit Ninja", 250, 350);

		FontLoader.setSize(48);
		g2.setFont(FontLoader.feastFont);
		g2.setColor(new Color(157,72,5));
		g2.drawString("Albert Li & Emily Hoang", 252, 432);
		g2.setColor(new Color(235,149,20));
		g2.drawString("Albert Li & Emily Hoang", 250, 430);
		
		if(startFruit != null) startFruit.paint(g);
		if(fruitRemnant != null) {
			fruitRemnant.paint(g);
		}
		startRing.paint(g);

		for (int i = 0; i < trail.size() - 1; i++) {
			TrailPoint p1 = trail.get(i);
			TrailPoint p2 = trail.get(i + 1);

			drawTrail(p1, p2, g2);
        }
		g.setColor(Color.CYAN);

	}
	
	public GameOver(Driver driver) {
		this.driver = driver;
		setPreferredSize(new Dimension(1422, 800));
        setFocusable(true);
        requestFocusInWindow();

		JFrame f = driver.frame;
		
		background = new Background("default.png");
		f.setBackground(Color.white);
		f.add(this);
		f.setResizable(false);
		f.addMouseListener(this);
		f.addKeyListener(this);
		f.addMouseMotionListener(this);
		
		setFocusable(true);
		requestFocusInWindow(); 

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("cursor.png").getImage().getScaledInstance(46, 40, Image.SCALE_DEFAULT),
				new Point(0,0),"custom cursor"));	

		startFruit = new Fruit(975, 325, 0, 0);
		startFruit.setAngularVelocity(0.05);
		startFruit.fixed = true;
		startRing = new StartRing(900, 250, startFruit.getAngularVelocity() * -1);
	
		loc = MouseInfo.getPointerInfo().getLocation();


		Timer t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	public boolean getStartPrompt() {
		return start;
	}
	
	@Override
    public void keyTyped(KeyEvent e) {
    }

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
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

		if (fruitRemnant != null && fruitRemnant.getFrag1().y >= 850 && fruitRemnant.getFrag2().y >= 850) {
            driver.startGame();
        }

		repaint();
	}
			
	public void checkSegmentForCut(Point p1, Point p2) {
		if(startFruit == null) return;
		int steps = (int) p1.distance(p2);
		for(int i = 0; i <= steps; i++){
			float t = i / (float) steps;
			float x = (float)(p1.x + t * (p2.x - p1.x));
			float y = (float)(p1.y + t * (p2.y - p1.y));

			if(startFruit.slice(x, y)) {
				fruitRemnant = (startFruit.split());
				startFruit = null;
				break;
			}
		}	
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
}
