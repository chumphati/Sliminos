package fr.upsaclay.bibs.tetris.view;

import fr.upsaclay.bibs.tetris.control.manager.GameManager;
import fr.upsaclay.bibs.tetris.view.component.ImagePanel;
import fr.upsaclay.bibs.tetris.view.component.Settings;
import fr.upsaclay.bibs.tetris.view.component.SoundMenu;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class displays all the functionalities of the Home page
 * <p>
 * A main frame is created with buttons that have different purposes like to start a new game,
 * load a game from a file or from a save and to see the top 10 ranking.
 * <p>
 * Two buttons are also created to activate or deactivate the sound while playing
 * @author Celine and Fiona
 */
public class Home extends JPanel implements ActionListener, Frame {
    private GameManager gameManager;
    private JButton startButton;
    private JButton loadButton;
    private JButton RankButton;
    private JPopupMenu loadPop;
    private MainFrame mainFrame;
    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private SoundMenu sm;
    private Settings settings;

    /**
     *Create a main frame
     * <p>
     *Create all the buttons necessary for the functionality of the game
     * @author Celine and Fiona
     */
    public Home(MainFrame mainFrame, GameManager game, Settings settings) {
        this.settings = settings;
        this.mainFrame = mainFrame;
        this.gameManager = game;

        this.setLocation(50, 50);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        ImagePanel backgroundPanel = new ImagePanel("resources/images/background.jpg");;
        backgroundPanel.setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        layeredPane.add(backgroundPanel, Integer.valueOf(0));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(layeredPane, BorderLayout.CENTER);
        mainPanel.add(gamePanel, BorderLayout.EAST);

        mainFrame.setContentPane(mainPanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        this.startButton = new JButton("Start new game");
        this.startButton.setBounds(100, 70, 180, 80);
        this.startButton.addActionListener(this);

        layeredPane.add(startButton, Integer.valueOf(2));

        this.loadButton = new JButton("Load game");
        this.loadButton.setBounds(100, 170, 180, 80);
        this.loadButton.addActionListener(this);
        layeredPane.add(loadButton, Integer.valueOf(3));

        loadPop = new JPopupMenu();

        this.cutMenuItem = new JMenuItem("From file");
        this.cutMenuItem.addActionListener(this);
        loadPop.add(cutMenuItem);

        this.copyMenuItem = new JMenuItem("From save");
        this.copyMenuItem.addActionListener(this);
        loadPop.add(copyMenuItem);

        mainFrame.pack();
        mainFrame.setVisible(true);


        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                loadPop.show(loadButton, 190, 17);
            }
        });


        this.RankButton = new JButton("Top 10 players ranking");
        this.RankButton.setBounds(100, 270, 180, 80);
        this.RankButton.addActionListener(this);
        layeredPane.add(RankButton, Integer.valueOf(4));

        //display ranking
        RankButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame rangFrame = new JFrame("Top 10 players ranking");
                rangFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                try {
                    BufferedReader reader = new BufferedReader(new FileReader("resources/bestScore.txt"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    reader.close();

                    JPanel panel = new JPanel();
                    JTextArea textArea = new JTextArea(10, 30);
                    textArea.setText(sb.toString());
                    textArea.setEditable(false);
                    panel.add(textArea);
                    JScrollPane scrollPane = new JScrollPane(panel);
                    rangFrame.add(scrollPane);

                    rangFrame.pack();
                    rangFrame.setLocation(700, 300);
                    rangFrame.setVisible(true);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.sm = new SoundMenu(this, this.settings);
        this.sm.setOpaque(false);

        JPanel mergeOnOff = new JPanel();
        mergeOnOff.add(this.sm, BorderLayout.CENTER);
        mergeOnOff.setBounds(53, 400, 300, 50);
        mergeOnOff.setOpaque(false);
        layeredPane.add(mergeOnOff, Integer.valueOf(5));

        //add animations:

        ImageIcon image2 = new ImageIcon("resources/images/home2.gif");
        Image scaleImage2 = image2.getImage().getScaledInstance(130, 102,Image.SCALE_DEFAULT);
        JLabel image2Label = new JLabel(new ImageIcon(scaleImage2));
        image2Label.setBounds(560, 290, 300, 212);
        layeredPane.add(image2Label, Integer.valueOf(7));

        ImageIcon image3 = new ImageIcon("resources/images/home3.gif");
        Image scaleImage3 = image3.getImage().getScaledInstance(100, 60,Image.SCALE_DEFAULT);
        JLabel image3Label = new JLabel(new ImageIcon(scaleImage3));
        image3Label.setBounds(450, 50, 100, 60);
        layeredPane.add(image3Label, Integer.valueOf(8));

        ImageIcon image4 = new ImageIcon("resources/images/home4.gif");
        Image scaleImage4 = image4.getImage().getScaledInstance(80, 62,Image.SCALE_DEFAULT);
        JLabel image4Label = new JLabel(new ImageIcon(scaleImage4));
        image4Label.setBounds(560, 340, 100, 82);
        layeredPane.add(image4Label, Integer.valueOf(9));

    }

    /**
     *Link the different buttons to the action they need to perform
     * <p>
     * @author Celine and Fiona
     */
    public void actionPerformed( ActionEvent e ) {
        if (e.getSource() == startButton) { //start game
            try {
                this.mainFrame.drawGamePlayView();
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
            this.quitFrame();

        } else if (e.getSource() == this.cutMenuItem) { //load from file
            JFileChooser choose = new JFileChooser(
                    FileSystemView
                            .getFileSystemView()
                            .getHomeDirectory()
            );

            int res = choose.showOpenDialog(null);
            File file = choose.getSelectedFile();

            if (file != null) {
                try {
                    this.gameManager.loadFromFile(file);
                } catch (IOException ex) {
                    throw new RuntimeException("no file selected");
                }

                this.launchGameFrame();
            }

        } else if (e.getSource() == this.copyMenuItem) { //load from save


            String[] options = new String[] {"Saved game 3", "Saved game 2", "Saved game 1"};
            String response = String.valueOf(JOptionPane.showOptionDialog(null, "Which saved game do you want to launch?", "Load saved game",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options));

            if (response.equals("0") || response.equals("1") || response.equals("2") ) {
                File file = new File("resources/mySaveFile"  + (Integer.parseInt(response)+1) + ".txt");
                if (file.length() == 0) {
                    JOptionPane.showConfirmDialog(null, "Sorry, nothing is recorded at this location!", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                } else {
                    try {
                        this.gameManager.loadFromFile(file);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    this.launchGameFrame();
                }
            }

        }

    }

    /**
     * Launch new game
     * <p>
     * @author Celine and Fiona
     */
    private void launchGameFrame() {
        this.quitFrame();
        try {
            new Game(this.gameManager, this.settings);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Comes from the interface frame
     * <p>
     * Quit the frame (& stops the timer and music)
     */
    @Override
    public void quitFrame() {
        this.sm.stop();
        this.mainFrame.dispose();
    }

    /**
     * Comes from the interface frame
     * <p>
     * Reset focus
     */
    public void requestFocus() {
        this.requestFocusInWindow();
    }
}
