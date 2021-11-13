package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import clueGame.Board;

/**
 * ClueGame: Creates and refreshes GUI for the game. Also contains the starting point for the full game (not yet implemented).
 * @author Ryne Smith
 * @author Mikayla Sherwood
 */
public class ClueGame extends javax.swing.JFrame {
	private static Board board = Board.getInstance();
	
	//this will be our entry point for the full game
	public static void main(String[] args) {
		ClueGame cg = new ClueGame();
		cg.setTitle("Clue");
		board.setConfigFiles("BoardLayout.csv", "ClueSetup.txt");
		board.initialize();
		cg.drawGameBoard();
		cg.setVisible(true);
	}
	
	public ClueGame() {
		this.setSize(1100, 1000);
	}
	
	//this will call other functions to draw and update the game board as needed
	private void drawGameBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(2, 2));
		
		//generate and add board display
		add(board, BorderLayout.CENTER);
		
		//generate and add panel for card info
		CardPanel cardPanel = new CardPanel();
		cardPanel.drawPanel();
		add(cardPanel);
				
		//generate and add control panel
		ControlPanel control = new ControlPanel();
		control.drawPanel();
		add(control);

	}
}
