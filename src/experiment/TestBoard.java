package experiment;

import java.util.Collections;
import java.util.Set;

public class TestBoard {
	private Set<TestBoardCell> targets;
	private TestBoardCell[][] board;
	
	public TestBoard(int rows, int cols) {
		board = new TestBoardCell[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				board[i][j] = new TestBoardCell(i, j);
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		//TODO implement this
		this.targets = Collections.<TestBoardCell>emptySet();
	}
	
	public Set<TestBoardCell> getTargets(){
		return this.targets;
	}
	 public TestBoardCell getCell(int row, int col) {
		 TestBoardCell tbc = new TestBoardCell(1, 1);
		 return tbc;
	 }
	
}