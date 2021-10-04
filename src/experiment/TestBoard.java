package experiment;
import java.util.Set;

public class TestBoard {
	private Set<TestBoardCell> targets;
	
	public TestBoard() {
		TestBoardCell tbc = new TestBoardCell(1, 1);
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		
	}
	
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	 public TestBoardCell getCell(int row, int col) {
		 TestBoardCell tbc = new TestBoardCell(1, 1);
		 return tbc;
	 }
	
}