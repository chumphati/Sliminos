package fr.upsaclay.bibs.tetris.view.component;

import fr.upsaclay.bibs.tetris.control.manager.GameManager;
import fr.upsaclay.bibs.tetris.view.Frame;
import fr.upsaclay.bibs.tetris.view.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * Display the menu bar
 * @author Celine and Fiona
 */
public class MenuBar extends JMenuBar implements ActionListener {
    private fr.upsaclay.bibs.tetris.view.Frame originalFrame;
    private JMenuItem mnuOpenFile;
    private JMenuItem mnuOpenFileSave;
    private JMenuItem mnuSaveFileAs;
    private JMenuItem mnuExit;
    private JMenuItem mnuRules;
    private JMenuItem mnuBack;
    private GameManager gameManager;
    private Settings settings;

    /**
     * Constructeur
     * We set the frame as a parameter because we need to leave the home frame when we launch the game without removing the menu
     */
    public MenuBar(GameManager gm, Frame originalFrame, Settings settings) {
        this.gameManager = gm;
        this.settings = settings;
        this.originalFrame = originalFrame;

        JMenu mnuFile = new JMenu( "File" );
        mnuFile.setMnemonic( 'F' );

        mnuOpenFile = new JMenuItem( "Load from file" );
        mnuOpenFile.setMnemonic( 'O' );
        mnuOpenFile.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK) );
        mnuOpenFile.addActionListener(this);
        mnuFile.add(mnuOpenFile);

        mnuOpenFileSave = new JMenuItem( "Load from save" );
        mnuOpenFileSave.setMnemonic( 'I' );
        mnuOpenFileSave.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK) );
        mnuOpenFileSave.addActionListener(this);
        mnuFile.add(mnuOpenFileSave);

        mnuSaveFileAs = new JMenuItem( "Save" );
        mnuSaveFileAs.setMnemonic( 'A' );
        mnuSaveFileAs.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK ));
        mnuSaveFileAs.addActionListener(this);
        mnuFile.add(mnuSaveFileAs);

        mnuFile.addSeparator();

        mnuBack = new JMenuItem( "Back to the menu" );
        mnuBack.setMnemonic( 'B' );
        mnuBack.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK ));
        mnuBack.addActionListener(this);
        mnuFile.add(mnuBack);

        mnuExit = new JMenuItem( "Exit" );
        mnuExit.addActionListener(this);
        mnuExit.setMnemonic( 'x' );
        mnuExit.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK) );
        mnuFile.add(mnuExit);

        this.add(mnuFile);

        JMenu mnuHelp = new JMenu( "Help" );
        mnuHelp.setMnemonic( 'H' );

        mnuRules = new JMenuItem( "Rules" );
        mnuRules.addActionListener(this);
        mnuRules.setMnemonic('r');
        mnuRules.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK) );
        mnuHelp.add(mnuRules);

        this.add( mnuHelp );

    }


    /**
     * Link the buttons with their actions
     * There are a back to home button, exit the game, save the game, load from file and load from save,
     * a help menu
     */
    public void actionPerformed(ActionEvent e ) {
        UIManager UI=new UIManager();
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);

        if (e.getSource() == mnuExit) {

            if (this.gameManager.getPlayer() != null) {
                if (this.gameManager.getPlayer().isActive()) {
                    this.gameManager.getPlayer().pause();
                }
            }

            ImageIcon icon = new ImageIcon("resources/images/exit.jpg");
            Image image = icon.getImage(); // transform it
            Image newimg = image.getScaledInstance(140, 130,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newimg);

            String s = "" + JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to quit this great game?",
                    "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icon);
            if (s.equals("0")) {
                System.exit(0);
            }

        } else if (e.getSource() == mnuBack) {
            if (this.gameManager.getPlayer() != null) {
                if (this.gameManager.getPlayer().isActive()) {
                    this.gameManager.getPlayer().pause();
                }
            }
            this.originalFrame.quitFrame();
            this.gameManager.initialize();

        } else if (e.getSource() == mnuRules) {
            if (this.gameManager.getPlayer() != null) {
                if (this.gameManager.getPlayer().isActive()) {
                    this.gameManager.getPlayer().pause();
                }
            }

            JOptionPane.showConfirmDialog(null, new ImageIcon("resources/help.jpg"), "Rules", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        } else if (e.getSource() == mnuOpenFile) {

            if (this.gameManager.getPlayer() != null) {
                if (this.gameManager.getPlayer().isActive()) {
                    this.gameManager.getPlayer().pause();
                }
            }

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


        } else if (e.getSource() == mnuOpenFileSave) {

            if (this.gameManager.getPlayer() != null) {
                if (this.gameManager.getPlayer().isActive()) {
                    this.gameManager.getPlayer().pause();
                }
            }

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

        } else if (e.getSource() == mnuSaveFileAs) {

            if (this.gameManager.getPlayer() != null) {
                if (this.gameManager.getPlayer().isActive()) {
                    this.gameManager.getPlayer().pause();
                }

                String[] options = new String[] {"Saved game 3", "Saved game 2", "Saved game 1"};
                String response = String.valueOf(JOptionPane.showOptionDialog(null, "Where do you want to save your game?", "Save game",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options));

                if (response.equals("0") || response.equals("1") || response.equals("2") ) {
                    File file = new File("resources/mySaveFile"  + (Integer.parseInt(response)+1) + ".txt");
                    try {
                        this.gameManager.save(file);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    this.launchGameFrame();
                }
            }

        }
    }

    /**
     * Launch a new game
     */
    private void launchGameFrame() {
        this.originalFrame.quitFrame();
        try {
            new Game(this.gameManager, this.settings);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}