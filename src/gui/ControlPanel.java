package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.HumanPlayer;
import clueGame.Player;

public class ControlPanel extends JPanel {
	private Player currentPlayer; //used to display which player's turn it currently is, using player name and color
	private String rollValue; //used to display the value of last dice roll
	private String guessData; //used to display current guesses
	private String guessResult; //used to display guess result info
	
	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Control GUI - Test");
		frame.setSize(600, 150);	
		// Create the JPanel and add it to the JFrame
		ControlPanel controlPanel = new ControlPanel();
		controlPanel.setCurrentPlayer(new HumanPlayer("Me", Color.green, 0, 0));
		controlPanel.setRollValue(1);
		controlPanel.setGuessData("This is a guess");
		controlPanel.setGuessResult("This is the result of the guess");
		controlPanel.drawPanel();
		frame.add(controlPanel, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
		
		// TODO setup for test

	}
	
	public ControlPanel() {
		setLayout(new GridLayout(0, 2));
	}
	
	public void drawPanel() {
		JPanel turnAccusationContinuePanel = createTACPanel();
		JPanel guessPanel = createGuessPanel();
		
		add(turnAccusationContinuePanel);
		add(guessPanel);
		
	}

	private JPanel createTACPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(2, 2));
		
		//panel to hold information about player for current turn
		JPanel turnPanel = new JPanel();
		turnPanel.setLayout(new GridLayout(2, 1));
		turnPanel.setBorder(new EtchedBorder());
		turnPanel.add(new JLabel("Active player:"));
		
		JLabel turnPlayerDisplay = new JLabel(currentPlayer.getName());
		turnPlayerDisplay.setBackground(currentPlayer.getColor());
		turnPlayerDisplay.setBorder(new EtchedBorder());
		turnPanel.add(turnPlayerDisplay);
		
		thisPanel.add(turnPanel);
		
		//panel to hold information about this turn's roll value
		JPanel rollPanel = new JPanel();
		rollPanel.setLayout(new GridLayout(1, 2));
		rollPanel.add(new JLabel("Roll: "));
		
		JLabel rollVal = new JLabel(rollValue);
		rollPanel.add(rollVal);
		
		thisPanel.add(rollPanel);
		
		//next space to hold accusation button
		JButton acc = new JButton("Make Accusation");
		
		thisPanel.add(acc);
		
		//next space to hold continue button
		JButton cont = new JButton("Next Turn");
		
		thisPanel.add(cont);
		
		//return panel
		return thisPanel;
	}
	
	private JPanel createGuessPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(2, 1));
		
		//space for information about current guess
		JLabel guessInfo = new JLabel(guessData);
		guessInfo.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		
		thisPanel.add(guessInfo);
		
		//space for information about guess results
		JLabel guessResults = new JLabel(guessResult);
		guessResults.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));

		thisPanel.add(guessResults);
		
		//return panel
		return thisPanel;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setRollValue(Integer rollValue) {
		this.rollValue = rollValue.toString();
	}

	public void setGuessData(String guessData) {
		this.guessData = guessData;
	}

	public void setGuessResult(String guessResult) {
		this.guessResult = guessResult;
	}
	
	
}
