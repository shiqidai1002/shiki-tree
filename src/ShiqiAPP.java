import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Shiqi
 */
public class ShiqiAPP {
    private static final int ANGLE_MIN = 0;
    private static final int ANGLE_MAX = 90;
    private static final int ANGLE_INIT = 45; // initial stem angle
    private static final int LENGTH_MIN = 100;
    private static final int LENGTH_MAX = 500;
    private static final int LENGTH_INIT = 350;// initial stem length
    private Logger log = Logger.getLogger(ShiqiAPP.class.getName());
    private JFrame frame;
    private JPanel mainPanel = null;
    ArrayList<JPanel> panels = new ArrayList<JPanel>();
    private JSlider angleSlider = null;
    private JSlider lengthSlider = null;
    JComboBox<String> forkNumList;
    JComboBox<String> generationNumList;
    private JButton generateBtn = null;
    private JButton clearBtn = null;
    private MyCanvas canvas = new MyCanvas();
    private int forkNum = 3;
    private int angle = ANGLE_INIT;
    private int initialLength = LENGTH_INIT;
    private int generationNum = 5;
    private JComboBox<String> rulesList;

    // constructor
    public ShiqiAPP() {
        log.info("App started");
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame();
        frame.setSize(1500, 950);
        frame.setTitle("ShiqiAPP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        JLabel title = new JLabel("<html><font size=+5><b>SHIQI'S BIOLOGICAL GROWTH</b></font></html>", JLabel.CENTER);
        title.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 8));
        frame.add(title, BorderLayout.NORTH);
        frame.add(getMainPanel(), BorderLayout.EAST);
        frame.add(canvas, BorderLayout.CENTER);
    }

    /**
     * @return JPanel Build the top main panel
     */
    private JPanel getMainPanel() {
        // initialize mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1, 50, 50));
        mainPanel.setPreferredSize(new Dimension(frame.getWidth() / 4, frame.getHeight()));
        JPanel p_angle = new JPanel(new BorderLayout());
        panels.add(p_angle);
        JPanel p_length = new JPanel(new BorderLayout());
        panels.add(p_length);
        JPanel p_forks = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panels.add(p_forks);
        JPanel p_generations = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panels.add(p_generations);
        JPanel p_rules = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panels.add(p_rules);
        JPanel p_buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        panels.add(p_buttons);
        // initialize two buttons
        generateBtn = new JButton("Generate");
        generateBtn.addActionListener((actionEvent1) -> {
            log.info("action:Generate");
            BGRule rule = new BGRule(forkNum, angle, initialLength);
            BGGeneration test = new BGGeneration(rule, generationNum);
            //System.out.println("Generate: " + "\nRule: " + "BGRule(fork: " + forkNum + ", angle: " + angle
            //        + ", length: " + initialLength + ")" + "\nGenerations: " + generationNum); // TODO GEORGE can i just comment it?
            Thread canvasThread = new Thread(() -> {
                canvas.setTree(new ArrayList<>());
                for (BGStem stem : test.tree) {
                    // System.out.println(stem.toString());
                    canvas.draw(stem);
                    try {
                        Thread.sleep(5);
                        //System.out.println("canvas: I got sleep!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            canvasThread.start();
        });
        clearBtn = new JButton("Clear");
        clearBtn.addActionListener((actionEvent2) -> {
            log.info("action:clear");
            canvas.setTree(null);
            canvas.repaint();
        });


        // initialize the angleSilder section
        JLabel lb1 = new JLabel("<html><font size=+2><b>Angle</b></font></html>");
        angleSlider = new JSlider(JSlider.HORIZONTAL, ANGLE_MIN, ANGLE_MAX, ANGLE_INIT);
        angleSlider.setMajorTickSpacing(15);
        angleSlider.setMinorTickSpacing(5);
        angleSlider.setPaintTicks(true);
        angleSlider.setPaintLabels(true);
        angleSlider.addChangeListener((changeEvent1) -> {
            JSlider source = (JSlider) changeEvent1.getSource();
            if (!source.getValueIsAdjusting()) {
                angle = (int) source.getValue();
            }
        });
        // initialize the lengthSilder section
        JLabel lb2 = new JLabel("<html><font size=+2><b>Length</b></font></html>");
        lengthSlider = new JSlider(JSlider.HORIZONTAL, LENGTH_MIN, LENGTH_MAX, LENGTH_INIT);
        lengthSlider.setMajorTickSpacing(50);
        lengthSlider.setMinorTickSpacing(25);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);
        lengthSlider.addChangeListener((changeEvent2) -> {
            JSlider source = (JSlider) changeEvent2.getSource();
            if (!source.getValueIsAdjusting()) {
                initialLength = (int) source.getValue();
            }
        });

        // initialize the fork number list section
        JLabel lb3 = new JLabel("<html><font size=+2><b>Forks</b></font></html>");
        String[] forkNumStrings = {"2", "3", "4", "5", "6", "7", "8", "9"};
        // Create the combo box, select item at index 0.
        // Indices start at 0, so 0 specifies "2".
        forkNumList = new JComboBox<>(forkNumStrings);
        forkNumList.setSize(20, forkNumList.getPreferredSize().height);
        forkNumList.setSelectedIndex(1);// default selected fork number is 3
        forkNumList.addActionListener((actionEvent3) -> {
            JComboBox<String> source = (JComboBox<String>) actionEvent3.getSource();
            log.info("action:fork");
            forkNum = Integer.parseInt((String) source.getSelectedItem());
            System.out.println("fork: " + forkNum);
        });

        // initialize the generation number list section
        JLabel lb4 = new JLabel("<html><font size=+2><b>Generations</b></font></html>");
        String[] generationNumStrings = {"1", "2", "3", "4", "5", "6", "7"};
        generationNumList = new JComboBox<>(generationNumStrings);
        generationNumList.setSelectedIndex(4);// default selected fork number is
        // 5
        generationNumList.addActionListener((actionEvent4) -> {
            JComboBox<String> source = (JComboBox<String>) actionEvent4.getSource();
            log.info("action:generations");
            generationNum = Integer.parseInt((String) source.getSelectedItem());
            System.out.println("generationNum: " + generationNum);
        });

        // rules hot keys
        JLabel lb5 = new JLabel("<html><font size=+2><b>Given Rules</b></font></html>");
        String[] rulesStrings = {"-", "1", "2", "3", "4", "5"};
        rulesList = new JComboBox<>(rulesStrings);
        rulesList.setSelectedIndex(0);
        rulesList.addActionListener((actionEvent5) -> {
            JComboBox<String> source = (JComboBox<String>) actionEvent5.getSource();
            if (((String) source.getSelectedItem()).equalsIgnoreCase("1")) {
                log.info("action:rule1");
                BGRule rule1 = new BGRule(7, 53, 305);
                // generationNum = 5;
                forkNumList.setSelectedIndex(rule1.getForkNum() - 2);
                generationNumList.setSelectedIndex(4);
                angleSlider.setValue((int) rule1.getAngle());
                lengthSlider.setValue((int) rule1.getInitialStemLength());

            }
            if (((String) source.getSelectedItem()).equalsIgnoreCase("2")) {
                log.info("action:rule2");
                BGRule rule2 = new BGRule(7, 60, 350);
                // generationNum = 5;
                forkNumList.setSelectedIndex(rule2.getForkNum() - 2);
                generationNumList.setSelectedIndex(4);
                angleSlider.setValue((int) rule2.getAngle());
                lengthSlider.setValue((int) rule2.getInitialStemLength());

            }
            if (((String) source.getSelectedItem()).equalsIgnoreCase("3")) {
                log.info("action:rule3");
                BGRule rule3 = new BGRule(5, 60, 350);
                // generationNum = 7;
                forkNumList.setSelectedIndex(rule3.getForkNum() - 2);
                generationNumList.setSelectedIndex(6);
                angleSlider.setValue((int) rule3.getAngle());
                lengthSlider.setValue((int) rule3.getInitialStemLength());
            }
            if (((String) source.getSelectedItem()).equalsIgnoreCase("4")) {
                log.info("action:rule4");
                BGRule rule4 = new BGRule(7, 30, 350);
                // generationNum = 5;
                forkNumList.setSelectedIndex(rule4.getForkNum() - 2);
                generationNumList.setSelectedIndex(4);
                angleSlider.setValue((int) rule4.getAngle());
                lengthSlider.setValue((int) rule4.getInitialStemLength());
            }
            if (((String) source.getSelectedItem()).equalsIgnoreCase("5")) {
                log.info("action:rule5");
                BGRule rule5 = new BGRule(9, 36, 330);
                // generationNum = 4;
                forkNumList.setSelectedIndex(rule5.getForkNum() - 2);
                generationNumList.setSelectedIndex(3);
                angleSlider.setValue((int) rule5.getAngle());
                lengthSlider.setValue((int) rule5.getInitialStemLength());
            }
        });

        // set the layout
        p_angle.add(lb1, BorderLayout.NORTH);
        p_angle.add(angleSlider, BorderLayout.CENTER);
        p_length.add(lb2, BorderLayout.NORTH);
        p_length.add(lengthSlider, BorderLayout.CENTER);
        p_forks.add(lb3);
        p_forks.add(forkNumList);
        p_generations.add(lb4);
        p_generations.add(generationNumList);
        p_rules.add(lb5);
        p_rules.add(rulesList);
        p_buttons.add(generateBtn);
        p_buttons.add(clearBtn);
        for (JPanel p : panels) {
            mainPanel.add(p);
        }
        return mainPanel;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShiqiAPP());
    }

}