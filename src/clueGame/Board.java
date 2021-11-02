package clueGame;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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
	private Map<Character, Room> roomMap; //this will be populated from setup config file
	private static Board theInstance = new Board();
	private ArrayList<Card> deck;
	private ArrayList<Player> players;
	private Solution theAnswer;
	private Card solutionRoom, solutionPerson, soltionWeapon;

	/**
	 * Private constructor to ensure only one instance is created.
	 */
	private Board() {
		super();
		
		//intialize the targets array for this board
		this.targets = new HashSet<BoardCell>();
		
		//initialize roomMap for this instance
		this.roomMap = new HashMap<Character, Room>();
		
		//intialize other variables for this instance
		this.deck = new ArrayList<Card>();
		this.players = new ArrayList<Player>();
		this.theAnswer = new Solution();
	}

	/**
	 * Initializes board, including grid setup and initialization of all
	 * BoardCell objects contained in the grid. Handles errors throw in config file loading.
	 * Also initializes target set and roomMap for this board.
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
		
		deal();
	}

	/**
	 * Loads setup configuration file using this.setupConfigFile. Populates this.roomMap using data
	 * from config file and throws errors as needed for bad formatting in the config file.
	 * 
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 * 
	 */
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException{
		//open input buffer
		Scanner in = new Scanner(new File(this.setupConfigFile));

		while(in.hasNext()) {
			String line = in.nextLine();
			String[] lineValues = line.split(", ");

			//lines containing room data will start with "Room"
			//also create a card for every room
			if(lineValues.length > 0 && (lineValues[0].equals("Room"))) {
				//grab room data and initialize a new Room object, then add it to this.roomMap
				String roomName = lineValues[1];
				String roomInitial = lineValues[2];
				Room newRoom = new Room(roomName);
				this.roomMap.put(roomInitial.charAt(0), newRoom);
				Card roomCard = new Card(roomName, CardType.ROOM);
				this.deck.add(roomCard);
			}
			
			//for "Space" lines intialize a room object only
			else if(lineValues.length > 0 && lineValues[0].equals("Space")) {
				//grab room data and initialize a new Room object, then add it to this.roomMap
				String roomName = lineValues[1];
				String roomInitial = lineValues[2];
				Room newRoom = new Room(roomName);
				this.roomMap.put(roomInitial.charAt(0), newRoom);
			}
			
			//for "Player" lines initialize a new player object and create a PERSON card
			else if(lineValues.length > 0 && lineValues[0].equals("Player")) {
				Boolean isAI = new Boolean(lineValues[1]);
				Integer rowLoc = new Integer(lineValues[2]);
				Integer colLoc = new Integer(lineValues[3]);
				String name = lineValues[4];
				Color color = new Color(new Integer(lineValues[5]), new Integer(lineValues[6]), new Integer(lineValues[7]));
				if(isAI) {
					Player player = new HumanPlayer(name, color, rowLoc, colLoc);
					this.players.add(player);
				}
				else {
					Player player = new ComputerPlayer(name, color, rowLoc, colLoc);
					this.players.add(player);
				}
				Card playerCard = new Card(name, CardType.PERSON);
				this.deck.add(playerCard);
			}
			
			//for "card" lines initialize a new card of specified type
			else if(lineValues.length > 0 && lineValues[0].equals("Card")) {
				String name = lineValues[1];
				CardType type = CardType.valueOf(lineValues[2]);
				Card card = new Card(name, type);
				this.deck.add(card);
			}
			
			//if we encounter anything other than a line starting with "Room", "Space", or "//" throw an error for bad formatting
			else if(!lineValues[0].substring(0, 2).equals("//"))
				throw new BadConfigFormatException("Unknown data type encountered in setup configuration: " + lineValues[0]);
		}
	}

	/**
	 * Loads layout configuration file using this.layoutConfigFile. Initializes and populates this.grid with information
	 * in config file. Throws errors for bad formatting as needed.
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
		this.grid = new BoardCell[numRows][numColumns];

		//loop again to check that all rows have expected number of columns, throw exception otherwise
		for(ArrayList<String> row: layoutConfig) {
			if(row.size() != numColumns)
				throw new BadConfigFormatException("Inconsistent number of columns in layout configuration.");
		}

		//initialize all cells on board
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}
		//TODO (possibly for next refactor) check if consecutive for loops are needed for room label/center setup or if they can be combined into one loop
		//setup rooms
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				//get config data for this cell
				String config = layoutConfig.get(i).get(j);

				//throw exception if room initial was not found in setup config
				if(roomMap.get(config.charAt(0)) == null )
					throw new BadConfigFormatException("Unknown room symbol encountered in layout configuration: " + config.charAt(0));

				//populate cell room initial
				grid[i][j].setInitial(config.charAt(0));

				//populate room center and label data
				if(config.length() > 1) {
					switch (config.charAt(1)){
					//for room labels or centers set the correct flag and update the room object with the label/center cell
					case '#':
						grid[i][j].setRoomLabel(true);
						roomMap.get(grid[i][j].getInitial()).setLabelCell(grid[i][j]);
						break;
					case '*':
						grid[i][j].setRoomCenter(true);
						roomMap.get(grid[i][j].getInitial()).setCenterCell(grid[i][j]);
						break;
					}
				}
			}
		}

		//setup other cell data such as doorways and secret passages
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				//get config data for this cell
				String config = layoutConfig.get(i).get(j);

				//throw exception if room initial was not found in setup config
				if(roomMap.get(config.charAt(0)) == null )
					throw new BadConfigFormatException("Unknown room symbol encountered in layout configuration: " + config.charAt(0));

				//populate cell room initial
				grid[i][j].setInitial(config.charAt(0));

				//populate room data
				if(config.length() > 1) {
					switch (config.charAt(1)){
					//ignore room centers and labels as we already handled those
					case '*':
					case '#':
						break;
					//for any doorway set the correct flags in the BoardCell and update the room object to keep track of it's doorways
					case '^':
						grid[i][j].setDoorway(true);
						grid[i][j].setDoorDirection(DoorDirection.UP);
						roomMap.get(grid[i-1][j].getInitial()).addDoorway(grid[i][j]);
						break;
					case 'v':
						grid[i][j].setDoorway(true);
						grid[i][j].setDoorDirection(DoorDirection.DOWN);
						roomMap.get(grid[i+1][j].getInitial()).addDoorway(grid[i][j]);
						break;
					case '>':
						grid[i][j].setDoorway(true);
						grid[i][j].setDoorDirection(DoorDirection.RIGHT);
						roomMap.get(grid[i][j+1].getInitial()).addDoorway(grid[i][j]);
						break;
					case '<':
						grid[i][j].setDoorway(true);
						grid[i][j].setDoorDirection(DoorDirection.LEFT);
						roomMap.get(grid[i][j-1].getInitial()).addDoorway(grid[i][j]);
						break;
					//for any secret passages set the correct flags in BoardCell and add the secret passage to the room object
					default:
						grid[i][j].setSecretPassage(config.charAt(1));
						roomMap.get(grid[i][j].getInitial()).setHasSecretPassage(true);
						roomMap.get(grid[i][j].getInitial()).setSecretPassageDestinationInitial(config.charAt(1));
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
		//return null if the requested location is invalid
		if(row > numRows-1 || col > numColumns-1 || row < 0 || col < 0)
			return null;
		return grid[row][col];
	}

	/**
	 * Returns the associated Room object for any cell
	 *
	 */
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	/**
	 * Returns the associated Room object for a given initial
	 *
	 */
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}

	/**
	 * Returns the number of rows for this board
	 * 
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * Returns the number of columns for this board
	 * 
	 */
	public int getNumColumns() {
		return numColumns;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public Solution getSolution() {
		return theAnswer;
	}

	/**
	 * Returns the current target cell set in this board
	 * 
	 */
	public Set<BoardCell> getTargets(){
		return targets;
	}

	/**
	 * Calculates all possible move targets starting from startCell and
	 * with movement range of pathLength. Populates Board.targets with results.
	 * 
	 * @param startCell
	 * @param pathLength
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		//initialize visited set for helper function
		Set<BoardCell> visited = new HashSet<BoardCell>();
		visited.add(startCell);
		
		//reset targets set
		targets.clear();
		
		//call recursive helper function
		findAllTargets(startCell, pathLength, visited);
	}

	/**
	 * Get adjacency list for a cell located at provided coordinates
	 * @param rowLocation
	 * @param colLocation
	 * 
	 */
	public Set<BoardCell> getAdjList(int rowLocation, int colLocation){
		return grid[rowLocation][colLocation].getAdjList();
	}
	
	//Function to help deal the cards among the players.
	public void deal() {
		ArrayList<Card> dealDeck = deck;
		solutionDeal(dealDeck);
		
		Random rand = new Random();
		Card current;
		int count = 0;
		do {
			int randIndex = rand.nextInt(dealDeck.size());
			current = dealDeck.get(randIndex);
			switch(count) {
			case 0: 
				players.get(0).updateHand(current);
				dealDeck.remove(randIndex);
				count += 1;
				break;
			case 1:
				players.get(1).updateHand(current);
				dealDeck.remove(randIndex);
				count += 1;
				break;
			case 2:
				players.get(2).updateHand(current);
				dealDeck.remove(randIndex);
				count += 1;
				break;
			case 3:
				players.get(3).updateHand(current);
				dealDeck.remove(randIndex);
				count += 1;
				break;
			case 4:
				players.get(4).updateHand(current);
				dealDeck.remove(randIndex);
				count += 1;
				break;
			case 5:
				players.get(5).updateHand(current);
				dealDeck.remove(randIndex);
				count = 0;
				break;
			}
		}while(dealDeck.size() != 0);
	}
	
	private void solutionDeal(ArrayList<Card> deck) {
		Random rand = new Random();
		Card current;
		boolean roomHolder = false, personHolder = false, weaponHolder = false;
		do{
			int randIndex = rand.nextInt(deck.size());
			current = deck.get(randIndex);
			
			if(current.getCardType() == CardType.ROOM && roomHolder != true) {
				solutionRoom = current;
				deck.remove(randIndex);
				roomHolder = true;
			}
			else if(current.getCardType() == CardType.PERSON && personHolder != true) {
				solutionPerson = current;
				deck.remove(randIndex);
				personHolder = true;
			}
			else if(current.getCardType() == CardType.WEAPON && weaponHolder != true) {
				soltionWeapon = current;
				deck.remove(randIndex);
				weaponHolder = true;
			}
		
		}while(roomHolder != true || personHolder != true || weaponHolder != true);
		
		theAnswer.setSolution(solutionPerson, solutionRoom, soltionWeapon);
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

				//this ensures we are only adding the center of the other room when there is secret passage
				//way or else the target list builds of the other center of the room.
				if(cell.isRoomCenter() || numSteps == 1) {
					targets.add(cell);
				}
				else {
					findAllTargets(cell, numSteps-1, visited);
				}
				visited.remove(cell);
			}
			//when the room is occupied, we still need to add it to our target list because another player can
			//also enter the room.
			else if(cell.getOccupied() && cell.isRoomCenter()) {
				targets.add(cell);
			}
		}
	}

	/**
	 * For all cells in board, find adjacent cells and add them to
	 * that cell's adjacencies list.
	 */
	private void calcAdjacencies() {
		/*
		 * if cell is NOT on (top edge, left edge, right edge, bottom edge) add adjacent cell to (above, right, left below)
		 */
		//loop thru all cells on board and calculate adjacencies for each
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {

				//if cell is a room center only add doorways and secret passages to adj list
				if(grid[i][j].isRoomCenter()) {
					for(BoardCell door: this.roomMap.get(grid[i][j].getInitial()).getDoorways()) {
						grid[i][j].addAdj(door); 
						door.addAdj(grid[i][j]);
					}
					if(roomMap.get(grid[i][j].getInitial()).hasSecretPassage()) {
						grid[i][j].addAdj(roomMap.get(roomMap.get(grid[i][j].getInitial()).getSecretPassageDestinationInitial()).getCenterCell());
					}
					continue;
				}
				
				//if cell is room cell but not the center don't add any adjancencies
				else if(grid[i][j].getInitial() != 'W' && grid[i][j].getInitial() != 'X') {
					continue;
				}
				
				//if cell is not on the top edge add adjacencies above
				//only add cells that share the same initial
				if(i != 0 && grid[i-1][j].getInitial() == grid[i][j].getInitial()) {
					grid[i][j].addAdj(grid[i-1][j]);
				}
				
				//if cell is not on the bottom edge add adjacencies below
				//only add cells that share the same initial
				if(i != numRows - 1 && grid[i+1][j].getInitial() == grid[i][j].getInitial()) {
					grid[i][j].addAdj(grid[i+1][j]);
				}
				
				//if cell is not on the left edge add adjacencies to the left
				//only add cells that share the same initial
				if(j != 0 && grid[i][j-1].getInitial() == grid[i][j].getInitial()) {
					grid[i][j].addAdj(grid[i][j-1]);
				}
				
				//if cell is not on the right edge add adjacencies to the right
				//only add cells that share the same initial
				if(j != numColumns - 1 && grid[i][j+1].getInitial() == grid[i][j].getInitial()) {
					grid[i][j].addAdj(grid[i][j+1]);
				}
			}
		}
	}
}
