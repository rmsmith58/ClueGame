package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import clueGame.HumanPlayer;
import clueGame.Player;

/**
 * ControlPanel: uses Swing to build a GUI panel that contains information for this turn: current player, dice roll value,
 * accusation and continue buttons, and information about recent guesses and their results. Also has a main function to display
 * the panel with test data.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class ControlPanel extends JPanel{
	private Player currentPlayer; //used to display which player's turn it currently is, using player name and color
	private String rollValue; //used to display the value of last dice roll
	private String guessData; //used to display current guesses
	private String guessResult; //used to display guess result info
	private JButton cont;
	
	//function to display the gui with test data
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

	}
	
	//construtor, only needed to create initial layout
	public ControlPanel() {
		setLayout(new GridLayout(0, 2));
	}
	
	//drawPanel, can be called repeatedly to update values in the panel
	public void drawPanel() {
		this.removeAll();
		
		this.currentPlayer = Board.getInstance().getCurrentPlayer();
		this.rollValue = "" + Board.getInstance().getDieVal();
		
		JPanel turnAccusationContinuePanel = createTACPanel();
		JPanel guessPanel = createGuessPanel();
		
		add(turnAccusationContinuePanel);
		add(guessPanel);
		
	}
	
	//creates a panel that holds accusation/continue buttons, current player info, and roll value.
	private JPanel createTACPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(2, 2));
		
		//panel to hold information about player for current turn
		JPanel turnPanel = new JPanel();
		turnPanel.setLayout(new GridLayout(2, 1));
		turnPanel.setBorder(new EtchedBorder());
		turnPanel.add(new JLabel("Active player:"));
		
		JLabel turnPlayerDisplay = new JLabel("<no player>");
		turnPlayerDisplay.setOpaque(true);
		turnPlayerDisplay.setBackground(Color.white);
		if(currentPlayer != null) {
			turnPlayerDisplay = new JLabel(currentPlayer.getName());
			turnPlayerDisplay.setBackground(currentPlayer.getColor());
		}
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
		cont = new JButton("Next Turn");
		
		//added a action listener to check if the button has been pressed
		cont.addActionListener(new ButtonListener());
		thisPanel.add(cont);
		
		//return panel
		return thisPanel;
	}
	
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Continue button pressed");
			if(Board.getInstance().getPlayerInputNeeded())
				return;
			Board.getInstance().advanceTurn();	//if the button is pressed it should display the next player on the control panel.
			rollValue = "" + Board.getInstance().getDieVal();
			currentPlayer = Board.getInstance().getCurrentPlayer();
			drawPanel();
			//System.out.println("redrawing panel");
			revalidate();
		}
	}
	
	//creates a panel that holds information about guesses and their results
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
	
	//setters to update displayed data
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
