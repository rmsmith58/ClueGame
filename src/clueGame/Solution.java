package clueGame;

public class Solution {
	private Card person, room, weapon;
	
	//TODO possibly add protection to ensure cards are correct type, how to handle this?
	
	public Solution() {
		super();
	}
	
	public Solution(Card person, Card room, Card weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}
	
	public void setSolution(Card person, Card room, Card weapon) {
		this.person=person;
		this.room=room;
		this.weapon=weapon;
	}
	
	public Card[] getSolution() {
		return new Card[] {this.person, this.room, this.weapon};
	}

	public Card getPerson() {
		return person;
	}

	public Card getRoom() {
		return room;
	}

	public Card getWeapon() {
		return weapon;
	}
	
	
}
