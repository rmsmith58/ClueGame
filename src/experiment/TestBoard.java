package experiment;

import java.util.*;


/**
 * TestBoard: experimental class for a game board. Includes a 2 dimensional array to hold
 * individual cell objects and a set of possible targets for a move. calcTargets will update this set
 * depending on the starting cell and pathlength.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class TestBoard {
	private Set<TestBoardCell> targets;
	private TestBoardCell[][] board;
	private int rows, cols;
	
	/**
	 * Public constructor with number of rows and columns for game board.
	 * 
	 * @param rows
	 * @param cols
	 */
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
		calcAdjacencies();
	}
	
	/**
	 * Calculate and update adjacent cells for all cells in game board.
	 * 
	 */
	private void calcAdjacencies() {
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
	
	/**
	 * Calculate all possible movement targets given a starting location
	 * and path length.
	 * 
	 * @param startCell
	 * @param pathLength
	 */
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		Set<TestBoardCell> visited = new HashSet<TestBoardCell>();
		visited.add(startCell);
		this.targets = new HashSet<TestBoardCell>();
		findAllTargets(startCell, pathLength, visited);
	}

	public Set<TestBoardCell> getTargets(){
		return this.targets;
	}
	
	/**
	 * Returns the cell at a specified location in the game board,
	 * or null if the cell could not be found.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public TestBoardCell getCell(int row, int col) {
		if(row > this.rows-1 || col > this.cols-1 || row < 0 || col < 0)
			return null;
		return board[row][col];
	}
	
	/**
	 * Helper function for calcTargets, recursively paths thru game board
	 * to evaluate movement targets.
	 * 
	 * @param thisCell
	 * @param numSteps
	 * @param visited
	 */
	private void findAllTargets(TestBoardCell thisCell, int numSteps, Set<TestBoardCell> visited) {
		for(TestBoardCell cell: thisCell.getAdjList()) {
			if(!(visited.contains(cell) || cell.getOccupied())) {
				visited.add(cell);
				if(numSteps == 1 || cell.getRoom()) {
					targets.add(cell);
				}
				else {
					findAllTargets(cell, numSteps-1, visited);
				}
				visited.remove(cell);
			}
		}

	}
}