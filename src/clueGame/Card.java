package clueGame;

import clueGame.Board;
import clueGame.BoardCell;

public class Card {
	private String cardName;
	private CardType cardType;
	
	//Constructor that is taking in the cardName and CardType.
	public Card(String cardName, CardType card) {
		super();
		this.cardName = cardName;
		this.cardType = card;
	}
	
	public boolean equals(Card card) {
		if(this.cardName.equals(card.getCardName())
				&& this.cardType.equals(card.getCardType())) {
			return true;
		}
		return false;
	}

	public String getCardName() {
		return cardName;
	}

	public CardType getCardType() {
		return cardType;
	}
}
