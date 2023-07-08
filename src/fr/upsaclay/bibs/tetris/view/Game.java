package fr.upsaclay.bibs.tetris.view;

import fr.upsaclay.bibs.tetris.TetrisAction;
import fr.upsaclay.bibs.tetris.control.manager.GameManager;
import fr.upsaclay.bibs.tetris.view.component.MenuBar;
import fr.upsaclay.bibs.tetris.view.component.Settings;
import fr.upsaclay.bibs.tetris.view.component.SoundMenu;
import fr.upsaclay.bibs.tetris.view.drawer.Drawer;
import fr.upsaclay.bibs.tetris.view.drawer.GameDrawer;
import fr.upsaclay.bibs.tetris.view.drawer.Hold;
import fr.upsaclay.bibs.tetris.view.drawer.Provider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class provides all the information that will be displayed on the frame for the game.
 * <p>
 * It shows all the basic information like the score, the level, the tetromino held and the next tetromino.
 * <p>
 * We also implemented a bare menu, sound options and also the best score with the name of the player.
 *<p>
 *In order to play the game, we also implemented a key pressed method that link
 * each action possible to a different key
 * @author Celine and Fiona
 */

public class Game extends JFrame implements ActionListener, KeyListener, Frame {
    private GameManager gameManager;
    private fr.upsaclay.bibs.tetris.view.component.MenuBar menuBar;
    public Timer timerDown;
    private JButton buttonStopContinue;
    private Drawer drawer;
    private Drawer nextTetrominoDrawer;
    private Drawer holdDrawer;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private SoundMenu sm;
    private Settings settings;
    private JLabel labelImage ;
    private Timer timerImages;

    /**
     * Loading slime animations
     * The first one is the default one
     */
    private ImageIcon defaultImage = new ImageIcon("resources/images/game1.gif");
    /**
     * Loading slime animations
     * The second one appears when the player makes a hard drop
     */
    private ImageIcon endImage = new ImageIcon("resources/images/game2.gif");
    /**
     * Loading slime animations
     * The third one is for the game over
     */
    private ImageIcon gaveOverImage = new ImageIcon("resources/images/game3.gif");

    /**
     * Constructor used for the initial window
     */
    public Game(GameManager gm, Settings s) {
        this.gameManager = gm;
        this.settings = s;

        this.setLocation(50, 50);
        this.init();
    }

    /**
     * Constructor used when doing a back to previous page: p is used to put the same location of the window
     */
    public Game(GameManager gm, Point p, Settings s) {
        this.gameManager = gm;
        this.settings = s;
        this.setLocation(p);
        this.init();
    }

    /**
     * Initialise all the buttons and the basic information
     * <p>
     * Three panels are created in order to organise the information
     *<p>
     *In order to play the game, we also implemented a key pressed method that link each action possible
     * to a different key
     */
    private void init() {
        this.gameManager.createPlayer();
        this.gameManager.loadNewGame();
        this.gameManager.startPlayer();

        this.setTitle("Sliminos");
        this.setSize(800, 600);
        this.setLayout(new BorderLayout(0, 0));
        this.setResizable(false);

        /**
         * Places the menu bar at the top of the page
         */
        menuBar = new MenuBar(this.gameManager, this, this.settings);
        this.setJMenuBar(menuBar);

        //////////////////////////////////////////////////////////////////////////////////////////
        //The page is divided in 3: on the left the animations, in the middle the game and on the right the information about the game
        //The information are displayed using overlapping panels
        //////////////////////////////////////////////////////////////////////////////////////////

        JPanel menuOptions = new JPanel();
        menuOptions.setPreferredSize(new Dimension(300, 600));
        menuOptions.setBackground(new Color(255, 250, 250));

        JLayeredPane layeredPaneGame = new JLayeredPane();
        layeredPaneGame.setPreferredSize(new Dimension(290, 590));
        menuOptions.add(layeredPaneGame);

        ImageIcon background2 = new ImageIcon("resources/images/background2.png");
        Image scalebackground2 = background2.getImage().getScaledInstance(300, 600,Image.SCALE_DEFAULT);
        JLabel imagebackground2 = new JLabel(new ImageIcon(scalebackground2));
        imagebackground2.setBounds(6, 6, 300, 529);
        layeredPaneGame.add(imagebackground2, Integer.valueOf(0));

        Image scaleImage2 = this.defaultImage.getImage().getScaledInstance(150, 150,Image.SCALE_DEFAULT);
        this.labelImage = new JLabel(new ImageIcon(scaleImage2));
        this.labelImage.setBounds(30, 310, 300, 212);
        layeredPaneGame.add(this.labelImage, Integer.valueOf(1));

        this.add(menuOptions, BorderLayout.WEST);

        JPanel gameInfos = new JPanel();
        gameInfos.setLayout(new BorderLayout());
        gameInfos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel scoresInfos = new JPanel();
        scoresInfos.setLayout(new BorderLayout());

        this.scoreLabel = new JLabel("Score: 0");
        this.scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoresInfos.add(this.scoreLabel, BorderLayout.WEST);

        this.levelLabel = new JLabel("Level: " + String.valueOf(this.gameManager.getPlayer().getLevel()));
        this.levelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoresInfos.add(this.levelLabel, BorderLayout.EAST);

        gameInfos.add(scoresInfos, BorderLayout.NORTH);

        gameInfos.setBackground(new Color(255, 250, 250));
        scoresInfos.setBackground(new Color(255, 250, 250));


        this.drawer = new GameDrawer(this.gameManager);
        gameInfos.add((Component) this.drawer, BorderLayout.CENTER);
        this.drawer.setBackground(new Color(255, 250, 250));

        this.add(gameInfos, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();

        rightPanel.setPreferredSize(new Dimension(130, 600));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        rightPanel.setBackground(new Color(255, 250, 250));

        JLabel infoNext = new JLabel("Next: ");
        this.nextTetrominoDrawer = new Provider(this.gameManager);
        infoNext.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(infoNext);
        rightPanel.add((Component) this.nextTetrominoDrawer);

        JLabel infoHold = new JLabel("Held: ");
        this.holdDrawer = new Hold(this.gameManager);
        infoHold.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(infoHold);
        rightPanel.add((Component) this.holdDrawer);

        //////////////////////////////////////////////////////////////////////////////////////////
        //Get the best score to display on the right side of the window
        //////////////////////////////////////////////////////////////////////////////////////////
        File fileBestScore = new File("resources/bestScore.txt");
        Scanner myReaderFile = null;
        try {
            myReaderFile = new Scanner(fileBestScore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int bestScore = 0;
        String bestPlayer = "Unidentified_slime";
        if (myReaderFile.hasNextLine()) {
            String[] split = myReaderFile.nextLine().split(" ");
            bestScore = Integer.parseInt(split[1]);
            bestPlayer = split[0];
        }
        myReaderFile.close();

        JLabel infoBestScore1 = new JLabel("Best score: ");
        JLabel infoBestScore2 = new JLabel(String.valueOf(bestScore));
        infoBestScore1.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(infoBestScore1);
        rightPanel.add(infoBestScore2);

        JLabel infoBestPlayer1 = new JLabel("Best player: ");
        JLabel infoBestPlayer2 = new JLabel(String.valueOf(bestPlayer));
        infoBestPlayer1.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(infoBestPlayer1);
        rightPanel.add(infoBestPlayer2);


        JPanel buttonMenu = new JPanel();
        buttonMenu.setPreferredSize(new Dimension(130, 600));
        buttonMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.buttonStopContinue = new JButton("Pause");
        this.buttonStopContinue.addActionListener(this);
        buttonMenu.add(buttonStopContinue);

        this.sm = new SoundMenu(this, this.settings);
        this.sm.setLayout(new BoxLayout(this.sm, BoxLayout.Y_AXIS));

        buttonMenu.add(this.sm);
        this.buttonStopContinue.setOpaque(false);
        this.sm.setOpaque(false);
        buttonMenu.setOpaque(false);

        rightPanel.add(buttonMenu, BorderLayout.SOUTH);
        this.add(rightPanel, BorderLayout.EAST);


        this.timerDown = new Timer(500, this);
        this.timerDown.start();

        this.timerImages = new Timer(1600, this);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Link the buttons declared above to the actions when they are pressed and information updates
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.gameManager.getPlayer().isOver()) {
            return;
        }

        if (e.getSource() == this.timerDown && this.gameManager.getPlayer().isActive()) { //update score and level
            this.gameManager.getPlayer().performAction(TetrisAction.DOWN);
            this.drawer.updatePaint();
            this.nextTetrominoDrawer.updatePaint();

            int score = this.gameManager.getPlayer().getScore();
            this.scoreLabel.setText("Score: " + score);

            int level = this.gameManager.getPlayer().getLevel();
            this.levelLabel.setText("Level: " + level);
        } else if (e.getSource() == this.buttonStopContinue) { //pause the game
            if (this.gameManager.getPlayer().isActive()) {
                this.gameManager.getPlayer().pause();
                this.buttonStopContinue.setText("Continue");
            } else {
                this.gameManager.getPlayer().start();
                this.buttonStopContinue.setText("Pause");
            }
            this.requestFocusInWindow();
        } else if (e.getSource() == this.timerImages) { //manages animations
            this.timerImages.stop();
            this.labelImage.setIcon(this.defaultImage);
        }

        this.showEndOfGame();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }


    /**
     * Link each action possible is link to a different key
     * <p>
     * Here the player must press the key to do the action
     */
    public void keyPressed(KeyEvent e) {
        if (!this.gameManager.getPlayer().isActive()) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.gameManager.getPlayer().performAction(TetrisAction.MOVE_LEFT);
            this.drawer.updatePaint();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.gameManager.getPlayer().performAction(TetrisAction.MOVE_RIGHT);
            this.drawer.updatePaint();
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            this.gameManager.getPlayer().performAction(TetrisAction.ROTATE_RIGHT);
            this.drawer.updatePaint();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            this.gameManager.getPlayer().performAction(TetrisAction.ROTATE_LEFT);
            this.drawer.updatePaint();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.gameManager.getPlayer().performAction(TetrisAction.HARD_DROP);
            this.drawer.updatePaint();
            this.nextTetrominoDrawer.updatePaint();

            this.sm.playEffect();
            this.requestFocusInWindow();

            this.timerImages.start();
            this.labelImage.setIcon(this.endImage);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.gameManager.getPlayer().performAction(TetrisAction.HOLD);
            this.drawer.updatePaint();
            this.holdDrawer.updatePaint();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.gameManager.getPlayer().performAction(TetrisAction.START_SOFT_DROP);
            this.gameManager.getPlayer().performAction(TetrisAction.DOWN);
            this.nextTetrominoDrawer.updatePaint();
            this.drawer.updatePaint();

            this.sm.playEffect();
        }

        this.requestFocusInWindow();

        // if after do action, the game is finished
        this.showEndOfGame();
    }

    /**
     * Link each action possible is link to a different key
     * <p>
     * Here the player must release the key to do the action
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (!this.gameManager.getPlayer().isActive()) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.gameManager.getPlayer().performAction(TetrisAction.END_SOFT_DROP);
            this.drawer.updatePaint();
        }
    }

    /**
     * Actions to do when the game is over
     * <p>
     * Records the best score if obtained, asks the player his nickname to record him in the top 10, displays the animations
     */
    private void showEndOfGame() {
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);
        if (this.gameManager.getPlayer().isOver()) {
            this.timerImages.start();
            this.labelImage.setIcon(this.gaveOverImage);

            String[] getScore = this.scoreLabel.getText().split(": ");
            int testScore = Integer.parseInt(getScore[1]);

            try {
                File fileBestScore = new File("resources/bestScore.txt");
                Scanner myReaderFile = new Scanner(fileBestScore);

                ArrayList<Integer> listScore = new ArrayList<>();
                ArrayList<String> listName = new ArrayList<>();

                while (myReaderFile.hasNextLine()) {
                    String[] rawData = myReaderFile.nextLine().split(" ");
                    listName.add(rawData[0]);
                    listScore.add(Integer.parseInt(rawData[1]));
                }
                myReaderFile.close();


                this.sortScoresList(listName, listScore);

                if (listScore.get(0) < testScore) {
                    String jobName;
                    String nameRecord;
                    ImageIcon icon = new ImageIcon("resources/images/winner.png");
                    Image image = icon.getImage(); // transform it
                    Image newimg = image.getScaledInstance(113, 116,  java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(newimg);
                    jobName = (String) JOptionPane.showInputDialog(null,"Congratulations! You have set a new score record! Enter your username:","Register your username",JOptionPane.QUESTION_MESSAGE,icon,null,"");
                    if (jobName != null) {
                        nameRecord = jobName;
                    } else {
                        nameRecord = "Unidentified_slime";
                    }

                    listName.add(nameRecord);
                    listScore.add(testScore);

                    this.sortScoresList(listName, listScore);

                    if (listScore.size() > 10) {
                        listScore.remove(0);
                        listName.remove(0);
                    }
                }

                BufferedWriter output = new BufferedWriter(new FileWriter(fileBestScore));
                for (int i = listName.size() -1; i >= 0; i--) {
                    output.write(listName.get(i) + " ");
                    output.write(listScore.get(i) + "\n");
                    if (testScore == listScore.get(i)) {
                        int rang = listName.size()-i;
                        ImageIcon icon = new ImageIcon("resources/images/trophee.png");
                        Image image = icon.getImage(); // transform it
                        Image newimg = image.getScaledInstance(120, 110,  java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(newimg);
                        JOptionPane.showMessageDialog(null, listName.get(i)+", you have taken the position "+rang+" in the overall ranking!", "Rang",
                                JOptionPane.WARNING_MESSAGE,icon);
                    }
                }

                output.close();

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ImageIcon icon = new ImageIcon("resources/images/gameover.png");
            Image image = icon.getImage(); // transform it
            Image newimg = image.getScaledInstance(140, 130,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newimg);
            String s = "" + JOptionPane.showConfirmDialog(
                    null,
                    "Game over.\nGo back home ?",
                    "Restart", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,icon);

            if (s.equals("0")) {
                this.quitFrame();
                this.gameManager.initialize();
            }
        }
    }

    /**
     * Sort the list of scores to keep the top 10 in the correct order
     */
    private void sortScoresList(ArrayList<String> listName, ArrayList<Integer> listScore){
        int temp;
        String tempName;
        for (int i = 0; i < listScore.size(); i++)
        {
            for (int j = i + 1; j < listScore.size(); j++)
            {
                if (listScore.get(i) > listScore.get(j))
                {
                    temp = listScore.get(i);
                    listScore.set(i, listScore.get(j));
                    listScore.set(j, temp);

                    tempName = listName.get(i);
                    listName.set(i, listName.get(j));
                    listName.set(j, tempName);
                }
            }
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
        this.timerDown.stop();
        this.dispose();
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
