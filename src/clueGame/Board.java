package clueGame;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
		this.loadSetupConfig();
		this.loadLayoutConfig();

		//add adjacencies for each cell on board, assuming board is not 1x1
		calcAdjacencies();
	}

	/**
	 * Loads setup configuration file.
	 * 
	 */
	public void loadSetupConfig() {
		this.roomMap = new HashMap<Character, Room>();
		try{
			Scanner in = new Scanner(new File(this.setupConfigFile));
			while(in.hasNext()) {
				String line = in.nextLine();
				String[] lineValues = line.split(", ");
				if(lineValues.length > 0 && (lineValues[0].equals("Room") || lineValues[0].equals("Space"))) {
					String roomName = lineValues[1];
					String roomInitial = lineValues[2];
					Room newRoom = new Room(roomName);
					this.roomMap.put(roomInitial.charAt(0), newRoom);
				}
			}
		} catch(Exception e) {
			System.out.println("Error attempting to read Setup Config:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Loads layout configuration file.
	 * 
	 */
	public void loadLayoutConfig() {
		try{
			Scanner in = new Scanner(new File(this.layoutConfigFile));
			ArrayList<ArrayList<String>> layoutConfig = new ArrayList();
			while(in.hasNext()) {
				String[] line = in.nextLine().split(",");
				ArrayList<String> row = new ArrayList<String>();
				for(String str: line)
					row.add(str);
				if(row.size() > 0)
					layoutConfig.add(row);
			}

			this.numColumns = layoutConfig.get(0).size();
			this.numRows = layoutConfig.size();
			this.grid = new BoardCell[this.numRows][this.numColumns];

			//initialize all cells on board
			for(int i = 0; i < this.numRows; i++) {
				for(int j = 0; j < this.numColumns; j++) {
					grid[i][j] = new BoardCell(i, j);

					String config = layoutConfig.get(i).get(j);
					grid[i][j].setInitial(config.charAt(0));

					if(config.length() > 1) {
						switch (config.charAt(1)){
						case '#':
							grid[i][j].setRoomLabel(true);
							this.roomMap.get(grid[i][j].getInitial()).setLabelCell(grid[i][j]);
							break;
						case '*':
							grid[i][j].setRoomCenter(true);
							this.roomMap.get(grid[i][j].getInitial()).setCenterCell(grid[i][j]);
							break;
						case '^':
							grid[i][j].setDoorway(true);
							grid[i][j].setDoorDirection(DoorDirection.UP);
							break;
						case 'v':
							grid[i][j].setDoorway(true);
							grid[i][j].setDoorDirection(DoorDirection.DOWN);
							break;
						case '>':
							grid[i][j].setDoorway(true);
							grid[i][j].setDoorDirection(DoorDirection.RIGHT);
							break;
						case '<':
							grid[i][j].setDoorway(true);
							grid[i][j].setDoorDirection(DoorDirection.LEFT);
							break;
						default:
							grid[i][j].setSecretPassage(config.charAt(1));
							break;
						}
					}
				}

			}
		} catch(Exception e) {
			System.out.println("Error attempting to read Layout Config");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
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

	
	public int getNumColumns() {
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

	
	public void setConfigFiles(String layoutFile, String setupFile) {
		this.layoutConfigFile = "data/" + layoutFile;
		this.setupConfigFile = "data/" + setupFile;
	}
	

	/**
	 * Returns the associated Room object for any cell
	 *
	 */
	public Room getRoom(BoardCell cell) {
		return this.roomMap.get(cell.getInitial());
	}

	
	/**
	 * Returns the associated Room object for a given initial
	 *
	 */
	public Room getRoom(char initial) {
		return this.roomMap.get(initial);
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
						this.grid[i][j].addAdj(grid[i+1][j]);
						this.grid[i][j].addAdj(grid[i][j+1]);
					}
					//if cell is in top right corner
					else if(j == this.numColumns-1) {
						this.grid[i][j].addAdj(grid[i+1][j]);
						this.grid[i][j].addAdj(grid[i][j-1]);
					}
					//if cell is not a corner
					else {
						this.grid[i][j].addAdj(grid[i+1][j]);
						this.grid[i][j].addAdj(grid[i][j+1]);
						this.grid[i][j].addAdj(grid[i][j-1]);
					}
				}
				//if cell is along bottom edge
				else if(i == this.numRows-1) {
					//if cell is in bottom left corner
					if(j == 0) {
						this.grid[i][j].addAdj(grid[i-1][j]);
						this.grid[i][j].addAdj(grid[i][j+1]);
					}
					//if cell is in bottom right corner
					else if(j == this.numColumns-1) {
						this.grid[i][j].addAdj(grid[i-1][j]);
						this.grid[i][j].addAdj(grid[i][j-1]);
					}
					//if cell is not a corner
					else {
						this.grid[i][j].addAdj(grid[i-1][j]);
						this.grid[i][j].addAdj(grid[i][j+1]);
						this.grid[i][j].addAdj(grid[i][j-1]);
					}
				}
				//if cell is along left edge
				else if(j == 0) {
					this.grid[i][j].addAdj(grid[i-1][j]);
					this.grid[i][j].addAdj(grid[i+1][j]);
					this.grid[i][j].addAdj(grid[i][j+1]);
				}
				//if cell is along right edge
				else if(j == this.numColumns-1) {
					this.grid[i][j].addAdj(grid[i-1][j]);
					this.grid[i][j].addAdj(grid[i+1][j]);
					this.grid[i][j].addAdj(grid[i][j-1]);
				}
				//if cell is not along any edge
				else {
					this.grid[i][j].addAdj(grid[i-1][j]);
					this.grid[i][j].addAdj(grid[i+1][j]);
					this.grid[i][j].addAdj(grid[i][j-1]);
					this.grid[i][j].addAdj(grid[i][j+1]);
				}
			}
		}
	}
}
