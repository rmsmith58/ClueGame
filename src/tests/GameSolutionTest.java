package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

class GameSolutionTest {

	@Test
	void checkAccusations() {
		Solution testAnswer = new Solution();
		Card room = new Card("Room", CardType.ROOM);
		Card weapon = new Card("Weapon", CardType.WEAPON);
		Card person = new Card("Person", CardType.PERSON);
		testAnswer.setSolution(person, room, weapon);
		
		Card newRoom = new Card("NewRoom", CardType.ROOM);
		Card newWeapon = new Card("NewWeapon", CardType.WEAPON);
		Card newPerson = new Card("NewPerson", CardType.PERSON);
		
		assertTrue(board.checkAccusation)
				
	}

}
