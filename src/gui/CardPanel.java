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

import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
import clueGame.Player;

public class CardPanel extends JPanel{
	private Player currPlayer;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private ArrayList<Card> seen = new ArrayList<Card>();
	private ArrayList<Card> handPeople = new ArrayList<Card>();
	private ArrayList<Card> handRoom = new ArrayList<Card>();
	private ArrayList<Card> handWeapon = new ArrayList<Card>();
	private ArrayList<Card> seenPeople = new ArrayList<Card>();
	private ArrayList<Card> seenRoom = new ArrayList<Card>();
	private ArrayList<Card> seenWeapon = new ArrayList<Card>();
	
	

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
		cardPanel.cardSplit();
		cardPanel.setCurrentPlayer(me);
		cardPanel.drawPanel();
		frame.add(cardPanel, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
		
	}

	public CardPanel() {
		setLayout(new GridLayout(0,1));
	}
	
	public void drawPanel() {
		JPanel knowCardPanel = knowCardPanel();
		JPanel peoplePanel = peoplePanel();
		JPanel roomPanel = roomPanel();
		JPanel weaponPanel = weaponPanel();
		add(knowCardPanel);
		knowCardPanel.add(peoplePanel);
		knowCardPanel.add(roomPanel);
		knowCardPanel.add(weaponPanel);
	}
	
	private JPanel knowCardPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(3,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		return thisPanel;	
	}
	
	private JPanel peoplePanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(8,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		JLabel hand = new JLabel("In Hand:");
		thisPanel.add(hand);
		if(this.hand.size() != 0) {
			for(int i = 0; i < this.hand.size(); i++) {
				if(this.hand.get(i).getCardType() == CardType.PERSON) {
					JLabel person = new JLabel(this.hand.get(i).getCardName());
					person.setOpaque(true);
					//person.setBackground(currPlayer.getColor());
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
				}
			}
		}
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		
		JLabel seen = new JLabel("Seen:");
		thisPanel.add(seen);
		if(this.seen.size() != 0) {
			for(int i = 0; i < seenPeople.size(); i++) {
				if(this.hand.get(i).getCardType() == CardType.PERSON) {
					JLabel person = new JLabel(this.hand.get(i).getCardName());
					person.setOpaque(true);
					//person.setBackground(currPlayer.getColor());
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
				}
			}
		}
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		return thisPanel;
	}
	
	private JPanel roomPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(11,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		JLabel hand = new JLabel("In Hand:");
		thisPanel.add(hand);
		if(handRoom.size() != 0) {
			for(int i = 0; i < handRoom.size(); i++) {
					JLabel person = new JLabel(handRoom.get(i).getCardName());
					person.setOpaque(true);
					person.setBackground(currPlayer.getColor());
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
			}
		}
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		
		JLabel seen = new JLabel("Seen:");
		thisPanel.add(seen);
		if(seenRoom.size() != 0) {
			for(int i = 0; i < seenRoom.size(); i++) {
					JLabel person = new JLabel(seenRoom.get(i).getCardName());
					person.setOpaque(true);
					//person.setBackground(currPlayer.getColor());
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
			}
		}
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		return thisPanel;
	}
	
	private JPanel weaponPanel() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new GridLayout(8,0));
		thisPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		JLabel hand = new JLabel("In Hand:");
		thisPanel.add(hand);
		if(handWeapon.size() != 0) {
			for(int i = 0; i < handWeapon.size(); i++) {
					JLabel person = new JLabel(handWeapon.get(i).getCardName());
					person.setOpaque(true);
					person.setBackground(currPlayer.getColor());
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
			}
		}
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		
		JLabel seen = new JLabel("Seen:");
		thisPanel.add(seen);
		if(seenWeapon.size() != 0) {
			for(int i = 0; i < seenWeapon.size(); i++) {
					JLabel person = new JLabel(seenWeapon.get(i).getCardName());
					person.setOpaque(true);
					//person.setBackground(currPlayer.getColor());
					person.setBorder(new EtchedBorder());
					thisPanel.add(person);
			}
		}
		else {
			JLabel none = new JLabel("None");
			none.setOpaque(true);
			none.setBorder(new EtchedBorder());
			thisPanel.add(none);
			
		}
		return thisPanel;
	}
	
	private void cardSplit() {
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getCardType() == CardType.PERSON) {
				this.handPeople.add(hand.get(i));
			}
			else if(hand.get(i).getCardType() == CardType.ROOM) {
				this.handRoom.add(hand.get(i));
			}
			else if(hand.get(i).getCardType() == CardType.WEAPON){
				this.handWeapon.add(hand.get(i));
			}
		}
		
		if(seen != null) {
			for(int i = 0; i < seen.size(); i++) {
				if(seen.get(i).getCardType() == CardType.PERSON) {
					this.seenPeople.add(seen.get(i));
				}
				else if(seen.get(i).getCardType() == CardType.ROOM) {
					this.seenRoom.add(seen.get(i));
				}
				else if(seen.get(i).getCardType() == CardType.WEAPON){
					this.seenWeapon.add(seen.get(i));
				}
			}
		}
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		this.currPlayer = currentPlayer;
	}

	private void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	private void setSeen(ArrayList<Card> seen) {
		this.seen = seen;
	}
	
}
