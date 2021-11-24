package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.ClueGame;

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
public class Board extends JPanel{
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap; //this will be populated from setup config file
	private static Board theInstance = new Board();
	private ArrayList<Card> deck;
	private ArrayList<Player> players;
	private Solution theAnswer;
	//TODO it might be better to have these as local variables in deal()? double check this
	//TODO also fix typo in line below
	private Card solutionRoom, solutionPerson, soltionWeapon;
	private int dieVal; 
	private int curPlayerIndex; //index location of current player in player list
	private Boolean playerInputNeeded; //boolean to block advancement while we're waiting on a player's input
	public final int WIDTH = 20; //width of board cells drawn in gui
	public final int OFFSET = 2; //offset for door indicators
	protected int targetRow, targetCol;

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
		
		//add click listener for gui
		this.addMouseListener(new BoardClickListener());
	}

	//TODO possibly reorder the functions in board, maybe alphabetically
	//TODO check that all instance variable for all classes in project are private
	
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
			deal();
		} catch(Exception e) {
			System.out.println("Error: " + e.getClass().getName());
			System.out.println(e.getMessage());
		}
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
				newRoom.setRoomColor(Color.blue);
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
				if(lineValues[1].equals("Walkway"))
					newRoom.setRoomColor(Color.yellow);
				else
					newRoom.setRoomColor(Color.black);
				this.roomMap.put(roomInitial.charAt(0), newRoom);
			}
			
			//for "Player" lines initialize a new player object and create a PERSON card
			else if(lineValues.length > 0 && lineValues[0].equals("Player")) {
				Boolean isAI = new Boolean(lineValues[1]);
				Integer rowLoc = new Integer(lineValues[2]);
				Integer colLoc = new Integer(lineValues[3]);
				String name = lineValues[4];
				Color color = new Color(new Integer(lineValues[5]), new Integer(lineValues[6]), new Integer(lineValues[7]));
				if(!isAI) {
					Player player = new HumanPlayer(name, color, rowLoc, colLoc);
					this.players.add(player);
				}
				else {
					Player player = new ComputerPlayer(name, color, rowLoc, colLoc, this);
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
	 * miscellanous data setup 
	 */
	public void miscDataInit() {
		for(Player player: this.players) {
			if(player.isAI())
				((ComputerPlayer) player).setUnseen(this.deck);
		}
		for(int i = 0; i < this.players.size(); i++) {
			if(!players.get(i).isAI()) {
				this.curPlayerIndex = i-1;
				break;
			}
		}
		this.advanceTurn();
	}
	
	/**
	 * Given a Solution (suggestion) and Player (accuser) object, checks all non-accusing players in order to check
	 * if they can disprove the suggestion. Returns the Card object that was used to disprove, or null if no player
	 * could disprove the accusation.
	 */
	public Card handleSuggestion(Player accuser, Solution suggestion) {
		//teleport the accused player to the accuser's location
		String accusedPlayer = suggestion.getPerson().getCardName();
		for(Player player: this.players) {
			if(player.getName().equals(accusedPlayer)) {
				player.setLocation(accuser.getRow(), accuser.getColumn());
				break;
			}
		}
		repaint(); //repaint to update target player location on board
		
		Card disprove = null;
		for (Player player: this.players) {
			if (player != accuser) {
				disprove = player.disproveSuggestion(suggestion);
				if(disprove != null)
					break;
			}
		}
		
		//create strings for guess and guess result and update the gui
		String guess = suggestion.toString();
		String guessResult;
		if(disprove == null)
			guessResult = "Not Disproved";
		else
			guessResult = disprove.getCardName();
		ClueGame.getTheInstance().updateGuessInfo(guess, guessResult);
		
		return disprove;
	}
	
	//TODO fix issue where dots will overlap if multiple players in one room
	public void paintComponent(Graphics g) {
		//draw super first to avoid issues
		super.paintComponent(g);
		//g.drawRect(0,  0, 5, 5);
		
		//draw every board cell
		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; j < this.numColumns; j++) {
				this.grid[i][j].drawCell(g, i, j, WIDTH); //TODO implement this
			}
		}
		
		
		//draw room labels over the board cells
		//also draw lines to indicate doorways
		for(Room room: this.roomMap.values()) {
			if(!room.getName().equals("Walkway") && !room.getName().equals("Unused")) {
				room.drawRoomLabel(g, room.getCenterCell().getRow(), room.getCenterCell().getCol(), WIDTH);
				room.drawDoorways(g, WIDTH, OFFSET);
			}
		}
		
		
		//draw markers for player locations
		for(Player player: this.players) {
			player.drawPlayer(g, WIDTH); //TODO implement this
		}
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
	
	public void handleAccusation(Player accuser, Solution accusation) {
		if(checkAccusation(accusation.getPerson(), accusation.getRoom(), accusation.getWeapon())) {
			//popup window to indicate player has won
			JOptionPane window = new JOptionPane();
			String introString = this.getCurrentPlayer().getName() + " has won the game!";
			window.showMessageDialog(null, introString, "Game Over", window.INFORMATION_MESSAGE);
			window.setVisible(true);
			ClueGame.getTheInstance().endGame();
		}
		else {
			//popup window to indicate player has lost
			JOptionPane window = new JOptionPane();
			String introString = this.getCurrentPlayer().getName() + " has lost the game!";
			window.showMessageDialog(null, introString, "Game Over", window.INFORMATION_MESSAGE);
			window.setVisible(true);
			ClueGame.getTheInstance().endGame();
		}
	}
	
	public Boolean checkAccusation(Card person, Card room, Card weapon) {
		Card[] solution = this.theAnswer.getSolution();
		if(person.equals(solution[0])
				&& room.equals(solution[1])
				&& weapon.equals(solution[2]))
			return true;
		return false;
	}
	
	public Card getCard(String name) {
		for(Card card: this.deck) {
			if (card.getCardName() == name)
				return card;
		}
		return null;
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
	
	//only for testing purposes
	public void setSolution(Solution newSolution) {
		this.theAnswer = newSolution;
	}
	
	public Boolean getPlayerInputNeeded() {
		return playerInputNeeded;
	}

	public void setPlayerInputNeeded(Boolean playerInputNeeded) {
		this.playerInputNeeded = playerInputNeeded;
	}

	/**
	 * Resets all data structures associated with this board, to prepare for another data load/initialization.
	 * Only intended for use in testing.
	 * 
	 */
	public void resetBoard() {
		this.grid = null;
		numRows = 0; 
		numColumns = 0;
		layoutConfigFile = null;
		setupConfigFile = null;
		solutionRoom = null;
		solutionPerson = null;
		soltionWeapon = null;
		
		//intialize the targets array for this board
		this.targets = new HashSet<BoardCell>();
				
		//initialize roomMap for this instance
		this.roomMap = new HashMap<Character, Room>();
				
		//intialize other variables for this instance
		this.deck = new ArrayList<Card>();
		this.players = new ArrayList<Player>();
		this.theAnswer = new Solution();
	}
	
	//only for testing
	public void addPlayer(Player player) {
		this.players.add(player);
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
		
		this.resetTargets();
		
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
		ArrayList<Card> deckCopy = new ArrayList<Card>();
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
				deckCopy.add(current);
				count += 1;
				break;
			case 1:
				players.get(1).updateHand(current);
				dealDeck.remove(randIndex);
				deckCopy.add(current);
				count += 1;
				break;
			case 2:
				players.get(2).updateHand(current);
				dealDeck.remove(randIndex);
				deckCopy.add(current);
				count += 1;
				break;
			case 3:
				players.get(3).updateHand(current);
				dealDeck.remove(randIndex);
				deckCopy.add(current);
				count += 1;
				break;
			case 4:
				players.get(4).updateHand(current);
				dealDeck.remove(randIndex);
				deckCopy.add(current);
				count += 1;
				break;
			case 5:
				players.get(5).updateHand(current);
				dealDeck.remove(randIndex);
				deckCopy.add(current);
				count = 0;
				break;
			}
		}while(dealDeck.size() != 0);
		this.deck = deckCopy;
		this.deck.add(this.soltionWeapon);
		this.deck.add(this.solutionPerson);
		this.deck.add(this.solutionRoom);
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
					cell.setIsTarget(true);
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
				cell.setIsTarget(true);
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
	
	public int getDieVal() {
		return dieVal;
	}

	/**
	 * handles turn advancement and updating current player/die value, also calls functions to process turns
	 */
	public void advanceTurn() {
		//advance to next player
		curPlayerIndex++;
		if(curPlayerIndex == this.players.size())
			curPlayerIndex = 0;
		
		//roll dice
		Random rand = new Random();
		this.dieVal = rand.nextInt(6)+1;
		
		if(this.players.get(curPlayerIndex).isAI())
			processAITurn();
		else
			setupHumanTurn();
	}
	
	/**
	 * process a turn for computer players
	 */
	private void processAITurn() {
		//find the possible targets for this move
		BoardCell curCell = this.getCell(this.players.get(curPlayerIndex).getRow(), this.players.get(curPlayerIndex).getColumn());
		calcTargets(curCell, dieVal);
		
		//have the ai select a board cell to move to
		//convert targets set to an arraylist
		ArrayList<BoardCell> targetList = new ArrayList<BoardCell>();
		for(BoardCell cell: this.targets)
			targetList.add(cell);
		
		BoardCell targetCell = ((ComputerPlayer)this.players.get(curPlayerIndex)).selectTargets(targetList);
		this.players.get(curPlayerIndex).setLocation(targetCell.getRow(), targetCell.getCol());
		
		resetTargets(); //call this to remove target highlighting
		repaint();
		
		//if we are not on a walkway create a suggestion
		Solution suggestion = null;
		Card suggestionReturn = null;
		if(!this.getRoom(this.getCell(targetCell.getRow(), targetCell.getCol())).getName().equals("Walkway")) {
			suggestion = ((ComputerPlayer)this.getCurrentPlayer()).createSuggestion();
			suggestionReturn = this.handleSuggestion(getCurrentPlayer(), suggestion);
		}	
		//make accusation here if nobody could disprove the suggestion and none of the suggestion cards are in the current player's hand
		if(suggestionReturn == null && suggestion != null) {
			for(Card card: this.getCurrentPlayer().getHand()) {
				if(card.equals(suggestion.getPerson())
						|| card.equals(suggestion.getRoom())
						|| card.equals(suggestion.getWeapon())) {
					return;
				}
			}
			//TODO make accusation here
		}
	}
	
	/**
	 * setup for human player to pick a valid movement location
	 */
	private void setupHumanTurn() {
		this.playerInputNeeded = true; //set flag to block advancement until we have all input
		
		Player humanPlayer = this.players.get(curPlayerIndex);
		this.calcTargets(this.getCell(humanPlayer.getRow(), humanPlayer.getColumn()), dieVal);
		repaint();

		//exit and wait for valid input to call finishHumanTurn
	}
	
	/**
	 * Finish human turn after valid location has been selected
	 * 
	 */
	private void finishHumanTurn() {
		this.players.get(curPlayerIndex).setLocation(this.targetRow, this.targetCol);
		this.playerInputNeeded = false;
		
		//if we are not on a walkway display the create suggestion gui
		if(!this.getRoom(this.getCell(targetRow, targetCol)).getName().equals("Walkway"))
			this.createSuggestionGUI();
	}
	
	public Player getCurrentPlayer() {
		return this.players.get(curPlayerIndex);
	}

	public BoardCell[][] getGrid() {
		// TODO Auto-generated method stub
		return this.grid;
	}
	
	private void resetTargets() {
		//reset targets set
		targets.clear();
		
		//reset cell flags
		for(BoardCell[] row: this.grid) {
			for(BoardCell cell: row) {
				cell.setIsTarget(false);
			}
		}
	}
	
	/**
	 * Displays GUI for player to choose suggestion
	 *
	 */
	public void createSuggestionGUI() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(4, 2));
		frame.setTitle("Make a Suggestion");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		
		//get string name for current room
		BoardCell curCell = this.getCell(this.getCurrentPlayer().getRow(), this.getCurrentPlayer().getColumn());
		String roomName = this.getRoom(curCell).getName();
		
		//get list of weapon and person name strings
		ArrayList<String> weaponNames = new ArrayList<String>();
		ArrayList<String> playerNames = new ArrayList<String>();
		for(Card card: deck) {
			if(card.getCardType().equals(CardType.WEAPON))
				weaponNames.add(card.getCardName());
			else if(card.getCardType().equals(CardType.PERSON))
				playerNames.add(card.getCardName());
		}
		
		//create drop-down boxes for selecting weapon and person
		JComboBox weapons = new JComboBox(weaponNames.toArray());
		JComboBox players = new JComboBox(playerNames.toArray());
		
		//create buttons for submitting and cancelling the suggestion
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");
		
		//if submit button is pressed, create a Solution for the suggestion and call handleSuggestion
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Card player = null;
				Card weapon = null;
				Card room = null;
				for(Card card: deck) {
					if(card.getCardType().equals(CardType.PERSON) && card.getCardName().equals(players.getSelectedItem()))
						player = card;
					else if(card.getCardType().equals(CardType.WEAPON) && card.getCardName().equals(weapons.getSelectedItem()))
						weapon = card;
					else if(card.getCardType().equals(CardType.ROOM) && card.getCardName().equals(roomName))
						room = card;
				}
				Solution suggestion = new Solution(player, room, weapon);
				handleSuggestion(getCurrentPlayer(), suggestion);
				frame.dispose();
			}
		});
		
		//if cancel button is pressed close the window and do nothing
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		frame.add(new JLabel("Current room: "));
		frame.add(new JLabel(roomName));
		frame.add(new JLabel("Weapon: "));
		frame.add(weapons);
		frame.add(new JLabel("Person: "));
		frame.add(players);
		frame.add(submit);
		frame.add(cancel);
		
		frame.setVisible(true);		
	}
	
	/*
	 * ==================================================
	 * MouseListener functions
	 * ==================================================
	 */
	private class BoardClickListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(!playerInputNeeded)
				return;
			
			double x = e.getX();
			double y = e.getY();
			
			//convert x and y to row, col locations for board cells
			int correctedX = (int) Math.ceil(x / (double) WIDTH);
			
			int correctedY = (int) Math.ceil(y / (double) WIDTH);
			
			//System.out.println("Raw X, Y: " + x + " " + y);
			//System.out.println("Corrected Row, Col: " + correctedY + " " + correctedX);
			
			if(getGrid()[correctedY-1][correctedX-1].isTarget()) {
				targetRow = correctedY-1;
				targetCol = correctedX-1;
				finishHumanTurn();
				resetTargets();
				repaint();
			}
			else {
				//TODO possibly issue error message or do nothing
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {} 
	}
}