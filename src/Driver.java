import java.awt.CardLayout;
import javax.swing.*;

public class Driver {
    JFrame frame;
    CardLayout cardLayout;
    JPanel cards;

    public Driver() {
        frame = new JFrame("Fruit Ninja");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1422, 800);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        StartScreen ss = new StartScreen(this);
        Frame gameFrame = new Frame(this);

        cards.add(ss, "start");
        cards.add(gameFrame, "game");

        frame.add(cards);
        frame.setVisible(true);

        showStartScreen();
    }

    public void showStartScreen() {
        cardLayout.show(cards, "start");
    }

    public void startGame() {
        cardLayout.show(cards, "game");
        SwingUtilities.invokeLater(() -> {
            Frame gameFrame = (Frame) cards.getComponent(1); // or store it in a variable
            gameFrame.requestFocusInWindow();
        });
    }

    public static void main(String[] args) {
        new Driver();
    }
}
