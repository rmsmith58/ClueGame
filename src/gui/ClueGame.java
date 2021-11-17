package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clueGame.Board;

/**
 * ClueGame: Creates and refreshes GUI for the game. Also contains the starting point for the full game (not yet implemented).
 * @author Ryne Smith
 * @author Mikayla Sherwood
 */
public class ClueGame extends javax.swing.JFrame {
	private static Board board = Board.getInstance();
	private static Boolean firstDraw = true; //flag to display popup on game start
	
	//this will be our entry point for the full game
	public static void main(String[] args) {
		ClueGame cg = new ClueGame();
		cg.setTitle("Clue");
		board.setConfigFiles("BoardLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.miscDataInit();
		cg.drawGameBoard();
		cg.setVisible(true);
	}
	
	public ClueGame() {
		this.setSize(1100, 1000);
	}
	
	//this will call other functions to draw and update the game board as needed
	private void drawGameBoard() {
		//this.removeAll();
		
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
		
		//add popup message for game start
		if(firstDraw) {
			gameStartPopup();
			firstDraw = false;
		}
		
	}
	
	//shows popup message for game start
	private void gameStartPopup() {
		JOptionPane window = new JOptionPane();
		String introString = "Welcome to Clue!\nUse clues to solve the murder.\nCan you solve it before your opponents?";
		window.showMessageDialog(null, introString, "Welcome!", window.INFORMATION_MESSAGE);
		window.setVisible(true);
	}
	
	public void redrawControlPanel() {
		
	}
}
