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

import clueGame.Card;
import clueGame.Player;

public class CardPanel extends JPanel{
	private Card currCard;
	private Player currPlayer;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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

	public CardPanel() {
		setLayout(new GridLayout(0,0));
	}
	
	public void drawPanel() {
		JPanel knowCardPanel = knowCardPanel();
	}
	
	private JPanel knowCardPanel() {
		
	}
	
	private JPanel peoplePanel() {
		
	}
	
	private JPanel roomPanel() {
		
	}
	
	private JPanel weaponPanel() {
		
	}
	
}
