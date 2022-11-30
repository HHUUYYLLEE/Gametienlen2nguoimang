package main;

import java.awt.Toolkit;

import javax.swing.*;
import game.Game;
import inputpanel.InputPanel;


public class Main {

	public static void main(String[] args) {
		final int inputPanelWidth = 400;
		final int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50;
		final int gameWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - inputPanelWidth;
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("2 Player Tiến lên");
		window.setLocationRelativeTo(null);
		
		
		JPanel mainPanel = new MainPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.repaint();
		
		Game game = new Game(gameWidth, screenHeight, inputPanelWidth);
		InputPanel inputPanel = new InputPanel(game, inputPanelWidth, screenHeight);
		
		mainPanel.add(inputPanel);
		mainPanel.add(game);
		
		
	
		window.add(mainPanel);
		window.pack();
		window.setVisible(true);
		
		game.setupGame();
		game.startGameThread();
		
		

	}
	
}