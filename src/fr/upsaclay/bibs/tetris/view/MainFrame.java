package fr.upsaclay.bibs.tetris.view;

import fr.upsaclay.bibs.tetris.control.manager.GameManager;
import fr.upsaclay.bibs.tetris.view.component.MenuBar;
import fr.upsaclay.bibs.tetris.view.component.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Set main frame
 * <p>
 * @author Celine and Fiona
 */
public class MainFrame extends JFrame implements Frame{
    private fr.upsaclay.bibs.tetris.view.component.MenuBar menuBar;
    private GameManager gameManager;
    private Settings settings;
    private Home home;

    /**
     * Constructor that sets the initialization values of the main frame
     */
    public MainFrame(GameManager gm) {
        this.gameManager = gm;

        this.setTitle("Sliminos");
        this.setSize(800,600);
        this.setLocation(50,50);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        this.settings = new Settings();

        menuBar = new MenuBar(this.gameManager, this, this.settings);
        this.setJMenuBar(menuBar);

        this.drawManagementView();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Draw home on frame
     */
    public void drawManagementView() {
        this.home = new Home(this, this.gameManager, this.settings);
        this.add(this.home);
    }

    /**
     * Draw game on frame
     */
    public void drawGamePlayView() {
        this.dispose();
        new Game(this.gameManager, this.getLocation(), this.settings);
    }

    /**
     * Quit frame
     */
    public void quitFrame() {
        this.dispose();
    }
}