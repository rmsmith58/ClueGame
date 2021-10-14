package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
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
	private String layoutConfigFile, setupConfigFile; //assuming all config files will be located in the src/data directory
	private Map<Character, Room> roomMap; //this will be populated from setup config file
	private static Board theInstance = new Board();

	/**
	 * Private constructor to ensure only one instance is created.
	 */
	private Board() {
		super();
	}

	/**
	 * Initializes board, including grid setup and initialization of all
	 * BoardCell objects contained in the grid. Handles errors throw in config file loading.
	 * 
	 */
	public void initialize() {
		//catch and handle any errors from config file loading
		try {
			this.loadSetupConfig();
			this.loadLayoutConfig();
			
			//add adjacencies for each cell on board, assuming board is not 1x1
			calcAdjacencies();
		} catch(Exception e) {
			System.out.println("Error: " + e.getClass().getName());
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Loads setup configuration file using filename supplied in .
	 * 
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 * 
	 */
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException{
		//initialize roomMap for this instance
		this.roomMap = new HashMap<Character, Room>();
		
		//open input buffer
		Scanner in = new Scanner(new File(this.setupConfigFile));
		
		while(in.hasNext()) {
			String line = in.nextLine();
			String[] lineValues = line.split(", ");
			
			//lines containing relevant data will start with "Room" or "Space"
			if(lineValues.length > 0 && (lineValues[0].equals("Room") || lineValues[0].equals("Space"))) {
				//grab room data and initialize a new Room object, then add it to this.roomMap
				String roomName = lineValues[1];
				String roomInitial = lineValues[2];
				Room newRoom = new Room(roomName);
				this.roomMap.put(roomInitial.charAt(0), newRoom);
			}
			//if we encounter anything other than a line starting with "Room", "Space", or "//" throw an error for bad formatting
			else if(!lineValues[0].substring(0, 2).equals("//"))
				throw new BadConfigFormatException("Unknown room type encountered in setup configuration: " + lineValues[0]);
		}
	}

	/**
	 * Loads layout configuration file.
	 * 
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 */
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException{
		Scanner in = new Scanner(new File(this.layoutConfigFile));
		
		//local multidimensional array to be used to hold layout data
		ArrayList<ArrayList<String>> layoutConfig = new ArrayList();
		
		//populate layoutConfig with data from layout config file
		while(in.hasNext()) {
			String[] line = in.nextLine().split(",");
			ArrayList<String> row = new ArrayList<String>();
			
			for(String str: line)
				row.add(str);
			
			if(row.size() > 0)
				layoutConfig.add(row);
		}
		
		//grab the dimensions and update this.numRows and this.numColumns
		//(using the first row to define the expected number of columns)
		this.numColumns = layoutConfig.get(0).size();
		this.numRows = layoutConfig.size();
		this.grid = new BoardCell[this.numRows][this.numColumns];
		
		//loop again to check that all rows have expected number of columns, throw exception otherwise
		for(ArrayList<String> row: layoutConfig) {
			if(row.size() != this.numColumns)
				throw new BadConfigFormatException("Inconsistent number of columns in layout configuration.");
		}

		//initialize all cells on board
		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; j < this.numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);
				
				//get config data for this cell
				String config = layoutConfig.get(i).get(j);
				
				//throw exception if room initial was not found in setup config
				if(this.roomMap.get(config.charAt(0)) == null )
					throw new BadConfigFormatException("Unkown room symbol encountered in layout configuration: " + config.charAt(0));
				
				//populate cell room initial
				grid[i][j].setInitial(config.charAt(0));
				
				//populate any other data for cell and set flags as needed
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
	}
	
	/**
	 * Updates filenames to be used for loading config files. Assumes these files will be located in the src/data directory
	 * 
	 * @param layoutConfigFilename
	 * @param setupConfigFilename
	 */
	public void setConfigFiles(String layoutConfigFilename, String setupConfigFilename) {
		this.layoutConfigFile = "data/" + layoutConfigFilename;
		this.setupConfigFile = "data/" + setupConfigFilename;
	}
	
	/**
	 * Returns the only Board instance.
	 * @return thisInstance
	 */
	public static Board getInstance() {
		return theInstance;
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
	 * Get adjacency list for a cell located at provided coordinates
	 * @param rowLocation
	 * @param colLocation
	 * 
	 */
	public Set<BoardCell> getAdjList(int rowLocation, int colLocation){
		return this.grid[rowLocation][colLocation].getAdjList();
	}
	
	/**
	 * Helper function for calcTargets, recursively explores board to
	 * determine possible movement locations.
	 * @param thisCell
	 * @param numSteps
	 * @param visited
	 */
	private void findAllTargets(BoardCell thisCell, int numSteps, Set<BoardCell> visited) {
		//implemented according to algorithm provided on Canvas page
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
