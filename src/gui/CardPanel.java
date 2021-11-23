package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
import clueGame.Player;

public class CardPanel extends JPanel{
	private Player currPlayer;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private ArrayList<Card> seen = new ArrayList<Card>();
	
	

	public static void main(String[] args) {
		Card room1 = new Card("Bathroom", CardType.ROOM);
		Card room2 = new Card("Hall", CardType.ROOM);
		Card person1 = new Card("Phil Hopkins", CardType.PERSON);
		Card person2 = new Card("Judy Edwards", CardType.PERSON);
		Card person3 = new Card("You", CardType.PERSON);
		Card weapon1 = new Card("Cyanide", CardType.WEAPON);
		Card weapon2 = new Card("Axe", CardType.WEAPON);
		Card weapon3 = new Card("Steak Knife", CardType.WEAPON);

		// TODO Auto-generated method stub
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Control GUI - Test");
		frame.setSize(150, 600);	
		// Create the JPanel and add it to the JFrame
		CardPanel cardPanel = new CardPanel();
		HumanPlayer me = new HumanPlayer("Me", Color.green, 0, 0);
		me.updateHand(room2);
		me.updateHand(person1);
		me.updateHand(person2);
		me.updateSeen(person3);
		me.updateSeen(weapon1);
		me.updateSeen(weapon2);
		me.updateSeen(weapon3);
		cardPanel.setHand(me.getHand());
		cardPanel.setSeen(me.getSeen());
		cardPanel.setCurrentPlayer(me);
		cardPanel.drawPanel();
		frame.add(cardPanel, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
		
	}
	
	//constructor, only creates the initial layout.
	public CardPanel() {
		setLayout(new GridLayout(0,1));
	}
	
	//begins to draw the panel
	public void drawPanel() {
		//this.removeAll();
		this.currPlayer = Board.getInstance().getCurrentPlayer();
		this.hand = currPlayer.getHand();
		this.seen = currPlayer.getSeen();
		
		// create the main panel (Known Cards) and sub-panels (People, Rooms, Weapons)
		JPanel knowCardPanel = knowCardPanel();
		JPanel peoplePanel = peoplePanel();
		JPanel roomPanel = roomPanel();
		JPanel weaponPanel = weaponPanel();
		//creates the main panel
		add(knowCardPanel);
		//place the sub-panels inside the main one.
		knowCardPanel.add(peoplePanel);
		knowCardPanel.add(roomPanel);
		knowCardPanel.add(weaponPanel);
	}
	
	//creates the known card panel.
	private JPanel knowCardPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(3,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		return thisPanel;	
	}
	
	//creates the people panel.
	private JPanel peoplePanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(8,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		
		//starts with the hand deck.
		JLabel handLabel = new JLabel("In Hand:");
		thisPanel.add(handLabel);
		//checks to see if any card are a person type.
		if(cardTypeIn(hand, "PERSON")) { 
			for(int i = 0; i < hand.size(); i++) {
				if(hand.get(i).getCardType() == CardType.PERSON) {
					JLabel person = new JLabel(hand.get(i).getCardName());
					person.setOpaque(true);
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
				}
			}
		}
		//if no cards are person type, then is displays none.
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		
		//then moves onto the seen deck.
		JLabel seenLabel = new JLabel("Seen:");
		thisPanel.add(seenLabel);
		//checks to see if any card are a person type.
		if(cardTypeIn(seen, "PERSON")) {
			for(int i = 0; i < seen.size(); i++) {
				if(seen.get(i).getCardType() == CardType.PERSON) {
					JLabel person = new JLabel(seen.get(i).getCardName());
					person.setOpaque(true);
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
				}
			}
		}
		//if no cards are person type, then is displays none.
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		return thisPanel;
	}
	
	//creates the room panel.
	private JPanel roomPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(11,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		
		//starts with the hand section.
		JLabel handLabel = new JLabel("In Hand:");
		thisPanel.add(handLabel);
		//checks to see if hand deck contains a card type of room.
		if(cardTypeIn(hand, "ROOM")) {
			for(int i = 0; i < hand.size(); i++) {
				if(hand.get(i).getCardType() == CardType.ROOM) {
					JLabel room = new JLabel(hand.get(i).getCardName());
					room.setOpaque(true);
					room.setBorder(new EtchedBorder());
					thisPanel.add(room);
			}
			}
		}
		//if not, it will display a none panel.
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		
		//then moves to the seen deck
		JLabel seenLabel = new JLabel("Seen:");
		thisPanel.add(seenLabel);
		//checks if any cards are card type room in the seen deck.
		if(cardTypeIn(seen, "ROOM")) {
			for(int i = 0; i < seen.size(); i++) {
				if(seen.get(i).getCardType() == CardType.ROOM) {
					JLabel person = new JLabel(seen.get(i).getCardName());
					person.setOpaque(true);
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
				}
			}
		}
		//if not, it will display a none panel.
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		return thisPanel;
	}
	
	//creates the weapon panel.
	private JPanel weaponPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(8,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		
		//starts with the hand deck.
		JLabel handLabel = new JLabel("In Hand:");
		thisPanel.add(handLabel);
		//checks to see if any cards are a weapon type.
		if(cardTypeIn(hand, "WEAPON")) {
			for(int i = 0; i < hand.size(); i++) {
				if(hand.get(i).getCardType() == CardType.WEAPON) {
					JLabel weapon = new JLabel(hand.get(i).getCardName());
					weapon.setOpaque(true);
					weapon.setBorder(new EtchedBorder());
					thisPanel.add(weapon);
				}
			}
		}
		//if not, it displays a none panel.
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		
		//then moves to the seen panel.
		JLabel seenLabel = new JLabel("Seen:");
		thisPanel.add(seenLabel);
		//checks to see if any card types are weapon type.
		if(cardTypeIn(seen, "WEAPON")) {
			for(int i = 0; i < seen.size(); i++) {
				if(seen.get(i).getCardType() == CardType.WEAPON) {
					JLabel weapon = new JLabel(seen.get(i).getCardName());
					weapon.setOpaque(true);
					weapon.setBorder(new EtchedBorder());
					thisPanel.add(weapon);
				}
			}
		}
		//if not, displays a none panel.
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		return thisPanel;
	}
	
	//setters to help the main function.
	public void setCurrentPlayer(Player currentPlayer) {
		this.currPlayer = currentPlayer;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public void setSeen(ArrayList<Card> seen) {
		this.seen = seen;
	}
	
	//fixes the branch logic by actually looking through the deck.
	private boolean cardTypeIn(ArrayList<Card> deck, String name) {
		for(int i = 0; i < deck.size(); i++) {
			if(deck.get(i).getCardType() == CardType.valueOf(name)) {
				return true;
			}
		}
		return false;
	}
}
