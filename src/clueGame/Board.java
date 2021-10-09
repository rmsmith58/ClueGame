package clueGame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Board: creates a single instance of itself to hold information
 * about the game board. Includes functions to load setup and layout
 * config files and contains all BoardCells in the board. Also handles
 * movement logic and target finding.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class Board {
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board();
	
	/**
	 * Private constructor to ensure only one instance is created.
	 * 
	 * @author Ryne Smith
	 * @author Mikayla Sherwood
	 */
	private Board() {
		super();
	}
	
	/**
	 * Initializes board, including grid setup and initialization of all
	 * BoardCell objects contained in the grid.
	 * 
	 */
	public void initialize() {
		//initialize all cells on board
		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; j < this.numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}

		//add adjacencies for each cell on board, assuming board is not 1x1
		calcAdjacencies();
	}
	
	/**
	 * Loads setup configuration file.
	 * 
	 */
	public void loadSetupConfig() {
		
	}
	
	/**
	 * Loads layout configuration file.
	 * 
	 */
	public void loadLayoutConfig() {
		
	}
	
	/**
	 * Returns the only Board instance.
	 * @return thisInstance
	 */
	public static Board getInstance() {
		return theInstance;
	}
	
	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numColumns;
	}
	
	public Set<BoardCell> getTargets(){
		return this.targets;
	}
	
	/**
	 * Returns the BoardCell contained at a specific location of the game grid.
	 * 
	 * @param row
	 * @param col
	 * @return grid[row][col]
	 */
	public BoardCell getCell(int row, int col) {
		if(row > this.numRows-1 || col > this.numColumns-1 || row < 0 || col < 0)
			return null;
		return grid[row][col];
	}
	
	/**
	 * Calculates all possible move targets starting from startCell and
	 * with movement range of pathLength. Populates Board.targets with results.
	 * 
	 * @param startCell
	 * @param pathLength
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		Set<BoardCell> visited = new HashSet<BoardCell>();
		visited.add(startCell);
		this.targets = new HashSet<BoardCell>();
		findAllTargets(startCell, pathLength, visited);
	}
	
	/**
	 * Helper function for calcTargets, recursively explores board to
	 * determine possible movement locations.
	 * @param thisCell
	 * @param numSteps
	 * @param visited
	 */
	private void findAllTargets(BoardCell thisCell, int numSteps, Set<BoardCell> visited) {
		for(BoardCell cell: thisCell.getAdjList()) {
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
	
	/**
	 * For all cells in board, find adjacent cells and add them to
	 * that cell's adjacencies list.
	 */
	private void calcAdjacencies() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				//if cell is along top edge
				if(i == 0) {
					//if cell is in top left corner
					if(j == 0) {
						this.grid[i][j].addAdjacency(grid[i+1][j]);
						this.grid[i][j].addAdjacency(grid[i][j+1]);
					}
					//if cell is in top right corner
					else if(j == this.numColumns-1) {
						this.grid[i][j].addAdjacency(grid[i+1][j]);
						this.grid[i][j].addAdjacency(grid[i][j-1]);
					}
					//if cell is not a corner
					else {
						this.grid[i][j].addAdjacency(grid[i+1][j]);
						this.grid[i][j].addAdjacency(grid[i][j+1]);
						this.grid[i][j].addAdjacency(grid[i][j-1]);
					}
				}
				//if cell is along bottom edge
				else if(i == this.numRows-1) {
					//if cell is in bottom left corner
					if(j == 0) {
						this.grid[i][j].addAdjacency(grid[i-1][j]);
						this.grid[i][j].addAdjacency(grid[i][j+1]);
					}
					//if cell is in bottom right corner
					else if(j == this.numColumns-1) {
						this.grid[i][j].addAdjacency(grid[i-1][j]);
						this.grid[i][j].addAdjacency(grid[i][j-1]);
					}
					//if cell is not a corner
					else {
						this.grid[i][j].addAdjacency(grid[i-1][j]);
						this.grid[i][j].addAdjacency(grid[i][j+1]);
						this.grid[i][j].addAdjacency(grid[i][j-1]);
					}
				}
				//if cell is along left edge
				else if(j == 0) {
					this.grid[i][j].addAdjacency(grid[i-1][j]);
					this.grid[i][j].addAdjacency(grid[i+1][j]);
					this.grid[i][j].addAdjacency(grid[i][j+1]);
				}
				//if cell is along right edge
				else if(j == this.numColumns-1) {
					this.grid[i][j].addAdjacency(grid[i-1][j]);
					this.grid[i][j].addAdjacency(grid[i+1][j]);
					this.grid[i][j].addAdjacency(grid[i][j-1]);
				}
				//if cell is not along any edge
				else {
					this.grid[i][j].addAdjacency(grid[i-1][j]);
					this.grid[i][j].addAdjacency(grid[i+1][j]);
					this.grid[i][j].addAdjacency(grid[i][j-1]);
					this.grid[i][j].addAdjacency(grid[i][j+1]);
				}
			}
		}
	}
}
