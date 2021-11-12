package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import clueGame.Board;

public class ClueGame extends javax.swing.JFrame {
	private static Board board = Board.getInstance();
	
	//this will be our entry point for the full game
	public static void main(String[] args) {
		ClueGame cg = new ClueGame();
		board.setConfigFiles("BoardLayout.csv", "ClueSetup.txt");
		board.initialize();
		cg.drawGame();
		cg.setVisible(true);
	}
	
	public ClueGame() {
		this.setSize(500, 500); //TODO figure out dimensions
		//TODO other stuff maybe
	}
	
	//this will call other functions to draw and update the game board as needed
	private void drawGame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(board, BorderLayout.CENTER);
	}
}
