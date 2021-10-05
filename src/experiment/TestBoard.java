package experiment;

import java.util.*;


/**
 * TestBoard: experimental class for a game board. Includes a 2 dimensional array to hold
 * individual cell objects and a set of possible targets for a move. calcTargets will update this set
 * depending on the starting cell and pathlength.
 * 
 * @author Ryne Smith and Mikayla Sherwood
 *
 */
public class TestBoard {
	private Set<TestBoardCell> targets;
	private TestBoardCell[][] board;
	private int rows, cols;

	public TestBoard(int rows, int cols) {
		board = new TestBoardCell[rows][cols];
		this.rows = rows;
		this.cols = cols;

		//initialize all cells on board
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				board[i][j] = new TestBoardCell(i, j);
			}
		}

		//add adjacencies for each cell on board, assuming board is not 1x1
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				//if cell is along top edge
				if(i == 0) {
					//if cell is in top left corner
					if(j == 0) {
						this.board[i][j].addAdjacency(board[i+1][j]);
						this.board[i][j].addAdjacency(board[i][j+1]);
					}
					//if cell is in top right corner
					else if(j == this.cols-1) {
						this.board[i][j].addAdjacency(board[i+1][j]);
						this.board[i][j].addAdjacency(board[i][j-1]);
					}
					//if cell is not a corner
					else {
						this.board[i][j].addAdjacency(board[i+1][j]);
						this.board[i][j].addAdjacency(board[i][j+1]);
						this.board[i][j].addAdjacency(board[i][j-1]);
					}
				}
				//if cell is along bottom edge
				else if(i == this.rows-1) {
					//if cell is in bottom left corner
					if(j == 0) {
						this.board[i][j].addAdjacency(board[i-1][j]);
						this.board[i][j].addAdjacency(board[i][j+1]);
					}
					//if cell is in bottom right corner
					else if(j == this.cols-1) {
						this.board[i][j].addAdjacency(board[i-1][j]);
						this.board[i][j].addAdjacency(board[i][j-1]);
					}
					//if cell is not a corner
					else {
						this.board[i][j].addAdjacency(board[i-1][j]);
						this.board[i][j].addAdjacency(board[i][j+1]);
						this.board[i][j].addAdjacency(board[i][j-1]);
					}
				}
				//if cell is along left edge
				else if(j == 0) {
					this.board[i][j].addAdjacency(board[i-1][j]);
					this.board[i][j].addAdjacency(board[i+1][j]);
					this.board[i][j].addAdjacency(board[i][j+1]);
				}
				//if cell is along right edge
				else if(j == this.cols-1) {
					this.board[i][j].addAdjacency(board[i-1][j]);
					this.board[i][j].addAdjacency(board[i+1][j]);
					this.board[i][j].addAdjacency(board[i][j-1]);
				}
				//if cell is not along any edge
				else {
					this.board[i][j].addAdjacency(board[i-1][j]);
					this.board[i][j].addAdjacency(board[i+1][j]);
					this.board[i][j].addAdjacency(board[i][j-1]);
					this.board[i][j].addAdjacency(board[i][j+1]);
				}
			}
		}
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void calcTargets(TestBoardCell startCell, int pathlength) {
		Set<TestBoardCell> visited = new HashSet<TestBoardCell>();
		//TODO implement this
		this.targets = Collections.<TestBoardCell>emptySet();
	}

	public Set<TestBoardCell> getTargets(){
		return this.targets;
	}
	public TestBoardCell getCell(int row, int col) {
		if(row > this.rows-1 || col > this.cols-1 || row < 0 || col < 0)
			return null;
		return board[row][col];
	}
	
	private void findAllTargets(TestBoardCell thisCell, int numSteps, Set<TestBoardCell> visited) {
		for(TestBoardCell cell: thisCell.getAdjList()) {
			//was already visited, you should skip over.
			//if()
			visited.add(cell);
			if(numSteps == 1) {
				targets.add(cell);
			}
			else {
				findAllTargets(cell, numSteps, visited);
			}
			visited.remove(cell);
		}
	}

}