package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

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
		cont.addActionListener(new ContinueButtonListener());
		thisPanel.add(cont);

		//return panel
		return thisPanel;
	}

	//functionality for continue button press
	private class ContinueButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Continue button pressed");
			if(Board.getInstance().getPlayerInputNeeded())
				return;
			Board.getInstance().advanceTurn();	//if the button is pressed it should display the next player on the control panel.
			rollValue = "" + Board.getInstance().getDieVal();
			currentPlayer = Board.getInstance().getCurrentPlayer();
			drawPanel();
			repaint();
			//System.out.println("redrawing panel");
			revalidate();
		}
	}

	//functionality for accusation button press
	private class AccButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			frame.setLayout(new GridLayout(4, 2));
			frame.setTitle("Make a Suggestion");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(200, 200);
			
			//get list of weapon and person name strings
			ArrayList<String> roomNames = new ArrayList<String>();
			ArrayList<String> weaponNames = new ArrayList<String>();
			ArrayList<String> playerNames = new ArrayList<String>();
			
			for(Card card: Board.getInstance().getDeck()) {
				if(card.getCardType().equals(CardType.WEAPON))
					weaponNames.add(card.getCardName());
				else if(card.getCardType().equals(CardType.PERSON))
					playerNames.add(card.getCardName());
				else if(card.getCardType().equals(CardType.ROOM))
					roomNames.add(card.getCardName());
			}

			//create drop-down boxes for selecting weapon and person
			JComboBox rooms = new JComboBox(roomNames.toArray());
			JComboBox weapons = new JComboBox(weaponNames.toArray());
			JComboBox players = new JComboBox(playerNames.toArray());

			//create buttons for submitting and cancelling the suggestion
			JButton submit = new JButton("Submit");
			JButton cancel = new JButton("Cancel");

			//if submit button is pressed, create a Solution for the suggestion and call handleSuggestion
			submit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Card player = null;
					Card weapon = null;
					Card room = null;
					for(Card card: Board.getInstance().getDeck()) {
						if(card.getCardType().equals(CardType.PERSON) && card.getCardName().equals(players.getSelectedItem()))
							player = card;
						else if(card.getCardType().equals(CardType.WEAPON) && card.getCardName().equals(weapons.getSelectedItem()))
							weapon = card;
						else if(card.getCardType().equals(CardType.ROOM) && card.getCardName().equals(rooms.getSelectedItem()))
							room = card;
					}
					Solution accusation = new Solution(player, room, weapon);
					Board.getInstance().handleAccusation(Board.getInstance().getCurrentPlayer(), accusation);
					frame.dispose();
				}
			});

			//if cancel button is pressed close the window and do nothing
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}
			});

			frame.add(new JLabel("Current room: "));
			frame.add(rooms);
			frame.add(new JLabel("Weapon: "));
			frame.add(weapons);
			frame.add(new JLabel("Person: "));
			frame.add(players);
			frame.add(submit);
			frame.add(cancel);

			frame.setVisible(true);		
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
