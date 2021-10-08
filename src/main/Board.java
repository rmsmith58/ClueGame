package main;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class Board {
	private Set<TestBoardCell> targets;
	private BoardCell[][] grid;
	private int numRows, numColumnss;

	public Board(int rows, int cols) {
		grid = new TestBoardCell[rows][cols];
		this.rows = rows;
		this.cols = cols;

		//initialize all cells on board
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}

		//add adjacencies for each cell on board, assuming board is not 1x1
		calcAdjacencies();
	}

	private void calcAdjacencies() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				//if cell is along top edge
				if(i == 0) {
					//if cell is in top left corner
					if(j == 0) {
						this.grid[i][j].addAdjacency(grid[i+1][j]);
						this.grid[i][j].addAdjacency(grid[i][j+1]);
					}
					//if cell is in top right corner
					else if(j == this.cols-1) {
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
				else if(i == this.rows-1) {
					//if cell is in bottom left corner
					if(j == 0) {
						this.grid[i][j].addAdjacency(grid[i-1][j]);
						this.grid[i][j].addAdjacency(grid[i][j+1]);
					}
					//if cell is in bottom right corner
					else if(j == this.cols-1) {
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
				else if(j == this.cols-1) {
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

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void calcTargets(TestBoardCell startCell, int pathLength) {
		Set<TestBoardCell> visited = new HashSet<TestBoardCell>();
		visited.add(startCell);
		this.targets = new HashSet<TestBoardCell>();
		findAllTargets(startCell, pathLength, visited);
	}

	public Set<TestBoardCell> getTargets(){
		return this.targets;
	}

	public TestBoardCell getCell(int row, int col) {
		if(row > this.rows-1 || col > this.cols-1 || row < 0 || col < 0)
			return null;
		return grid[row][col];
	}

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
