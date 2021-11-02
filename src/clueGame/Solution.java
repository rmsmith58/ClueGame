package clueGame;

public class Solution {
	private Card person, room, weapon;
	
	public Solution() {
		super();
	}
	
	public void setSolution(Card person, Card room, Card weapon) {
		this.person=person;
		this.room=room;
		this.weapon=weapon;
	}
	
	public Card[] getSolution() {
		return new Card[] {this.person, this.room, this.weapon};
	}
}
