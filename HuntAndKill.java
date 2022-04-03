public class HuntAndKill implements Generator {
	private Maze maze;
	private Display display;
	
	private int lastHuntedX;
	private int lastHuntedY;
	
	private Direction pickDirection(Cell currentCell) {
		Direction[] options = new Direction[4];
		int optionSize = 0;
		if (currentCell.getNeighbor(Direction.NORTH) != null && currentCell.getNeighbor(Direction.NORTH).getStatus() == Status.GRAY) {
			options[optionSize++] = Direction.NORTH;
		}
		if (currentCell.getNeighbor(Direction.EAST) != null && currentCell.getNeighbor(Direction.EAST).getStatus() == Status.GRAY) {
			options[optionSize++] = Direction.EAST;
		}
		if (currentCell.getNeighbor(Direction.SOUTH) != null && currentCell.getNeighbor(Direction.SOUTH).getStatus() == Status.GRAY) {
			options[optionSize++] = Direction.SOUTH;
		}
		if (currentCell.getNeighbor(Direction.WEST) != null && currentCell.getNeighbor(Direction.WEST).getStatus() == Status.GRAY) {
			options[optionSize++] = Direction.WEST;
		}
		if (optionSize == 0) {
			return null;
		}
		return options[(int)(Math.random() * optionSize)];
	}
	
	private Direction visitedNeighbor(Cell currentCell) {
		if (currentCell.getNeighbor(Direction.NORTH) != null && currentCell.getNeighbor(Direction.NORTH).getStatus() == Status.WHITE) {
			return Direction.NORTH;
		}
		
		if (currentCell.getNeighbor(Direction.EAST) != null && currentCell.getNeighbor(Direction.EAST).getStatus() == Status.WHITE) {
			return Direction.EAST;
		}
		
		if (currentCell.getNeighbor(Direction.SOUTH) != null && currentCell.getNeighbor(Direction.SOUTH).getStatus() == Status.WHITE) {
			return Direction.SOUTH;
		}
		if (currentCell.getNeighbor(Direction.WEST) != null && currentCell.getNeighbor(Direction.WEST).getStatus() == Status.WHITE) {
			return Direction.WEST;
		}
		return null;
	}
	
	private Cell selectStartingCell() {
		return maze.getCell(new Position((int)(Math.random() * maze.getSizeX()), (int)(Math.random() * maze.getSizeY())));
	}
	
	private Cell hunt() {
		for (int x = lastHuntedX; x < maze.getSizeX(); x++) {
			for (int y = lastHuntedY; y < maze.getSizeY(); y++) {
				Cell currentCell = maze.getCell(new Position(x, y));
				//Status previousStatus = currentCell.getStatus(); //This is an abuse; will update system
				//currentCell.setStatus(Status.GREEN);
				//display.update();
				Direction neighborDir = visitedNeighbor(currentCell);
				if (neighborDir != null && currentCell.getStatus() == Status.GRAY) {
					//currentCell.setStatus(previousStatus);
					//display.displayMaze(maze);
					//lastHuntedX = x;
					//lastHuntedY = y;
					currentCell.carveWall(neighborDir);
					currentCell.setStatus(Status.WHITE);
					return currentCell;
				}
				//currentCell.setStatus(previousStatus);
			}
		}
		return null;
	}
	
	private void walk(Cell currentCell) {
		Direction next = null;
		do {
			currentCell.setStatus(Status.WHITE);
			next = pickDirection(currentCell);
			if (next != null) {
				currentCell.carveWall(next);
				currentCell = currentCell.getNeighbor(next);
			}
			display.update();
		} while (next != null);
	}
	
	private void setEntrances() {
		if (Math.random() > 0.5) {
			maze.setEntrance(maze.getCell(new Position(0, (int)(Math.random() * maze.getSizeY()))));
			maze.setExit(maze.getCell(new Position(maze.getSizeX() - 1, (int)(Math.random() * maze.getSizeY()))));
		} else {
			maze.setEntrance(maze.getCell(new Position((int)(Math.random() * maze.getSizeX()), maze.getSizeY() - 1)));
			maze.setExit(maze.getCell(new Position((int)(Math.random() * maze.getSizeX()), 0)));
		}
	}
	
	public Maze generate(int width, int height, Display display) {
		maze = new Maze(width, height);
		this.display = display;
		display.displayMaze(maze);
		
		lastHuntedX = 0;
		lastHuntedY = 0;
		
		Cell currentCell = selectStartingCell();
		do {
			walk(currentCell);
			currentCell = hunt();
		} while (currentCell != null);
		
		setEntrances();
		return maze;
	}
}