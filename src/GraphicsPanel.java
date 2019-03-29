import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;

public class GraphicsPanel extends JPanel implements ActionListener {
    private JButton initialButton, trainButton, fileButton;
    private JTextField fileField, neighbourField, learningField, epochsField, messageField;
    private JLabel neighbourLabel, learningLabel, epochsLabel;
    private ImageReader imgReader = ImageReader.getInstance();
    private ColorManager colorManager = ColorManager.getInstance();
    private BufferedImage image;
    private SelfOrganizingMap som;
    private JFrame masterFrame;
    private JPanel thisPanel;

    private int epochsPerGraphics;
    private int baseX, baseY;

    JLabel picLabel;

    public GraphicsPanel(JFrame aFrame) {
        initialButton = new JButton("Initialize");
        initialButton.addActionListener(this);
        trainButton = new JButton("Train");
        trainButton.addActionListener(this);
        fileButton = new JButton("Choose Image:");
        fileButton.addActionListener(this);
        fileField = new JTextField(25);
        neighbourLabel = new JLabel("Neighbourhood Size:");
        neighbourField = new JTextField(6);
        learningLabel = new JLabel("Learning Rate:");
        learningField = new JTextField(6);
        epochsLabel = new JLabel("Epochs:");
        epochsField = new JTextField(8);
        messageField = new JTextField(30);
        messageField.setEditable(false);
        messageField.setHorizontalAlignment(JTextField.CENTER);
        masterFrame = aFrame;
        thisPanel = this;

        add(initialButton);
        add(trainButton);
        add(fileButton);
        add(fileField);
        add(neighbourLabel);
        add(neighbourField);
        add(learningLabel);
        add(learningField);
        add(epochsLabel);
        add(epochsField);
        add(messageField);

        epochsPerGraphics = 5;
        baseX = 20;
        baseY = 100;

        //image = imgReader.loadImage("Images/Hero.png");
        //picLabel = new JLabel(new ImageIcon(image));
        //add(picLabel);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int size = 4;
        if (som != null) {
            ArrayList<ArrayList<Integer>> weights = som.getWeights();
            for (int i=0; i<som.getLength(); i++) {
                for (int j=0; j<som.getWidth(); j++) {
                    Color aColour = colorManager.generateColor(weights.get(0).get(i*j),
                            weights.get(1).get(i*j), weights.get(2).get(i*j));
                    g2.setColor(aColour);
                    g2.fillRect(baseX + j*size, baseY + i*size, size, size);
                }
            }
        }
    }

    private void actionHelper(ActionEvent e) {
        Object source = e.getSource();
        if (source == initialButton) {
            messageField.setText("Initializing... Please wait.");
            double neighbourhoodSize = Double.parseDouble(neighbourField.getText());
            double learningRate = Double.parseDouble(learningField.getText());
            int epochs = Integer.parseInt(epochsField.getText());
            int xSize = 20;
            int ySize = 10;
            EventQueue.invokeLater(new Runnable() { // Create thread, as messageField waits to update when in same thread
                @Override
                public void run() {
                    som = new SelfOrganizingMap(xSize, ySize, neighbourhoodSize, learningRate, epochs);
                    thisPanel.repaint();
                    messageField.setText("");
                }
            });
        } else if (source == trainButton) {
            messageField.setText("Training... Please wait.");
            EventQueue.invokeLater(new Runnable() { // Create thread, as messageField waits to update when in same thread
                @Override
                public void run() {
                    som.train();
                    som.getWeights();
                    messageField.setText("Training finished.");
                    thisPanel.repaint();
                }
            });
        } else if (source == fileButton) {
            JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));     // Current working directory
            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {   // Once user selects and approves the file
                File selectedFile = jfc.getSelectedFile();
                fileField.setText(selectedFile.getAbsolutePath());
                image = imgReader.loadImage(fileField.getText()); // will be null if invalid image format
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!messageField.getText().isEmpty())
            messageField.setText("");
        try {
            actionHelper(e);
        } catch (NumberFormatException ex) {
            messageField.setText("Please fill out the parameter fields.");
        }
    }
}
