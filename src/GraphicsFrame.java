import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphicsFrame extends JFrame {
    public GraphicsFrame() {
        setTitle("Self-Organizing Feature Map Visualization");
        setSize(1100, 800);
        setMinimumSize(new Dimension(800, 500));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();

        //this.setJMenuBar(new TopMenu(this));

        GraphicsPanel panel = new GraphicsPanel(this);
        contentPane.add(panel);
        this.setVisible(true);
    }
}
