public class RecursiveBacktracker implements Generator {
	private Maze maze;
	private Display display;
	
	private Direction[][] path;
	
	private Direction opposite(Direction dir) {
		switch(dir) {
			case NORTH:
				return Direction.SOUTH;
			case SOUTH:
				return Direction.NORTH;
			case EAST:
				return Direction.WEST;
			default:
				return Direction.EAST;
		}
	}
	
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
	
	private void carvePassage(Cell currentCell) {
		path = new Direction[maze.getSizeX()][maze.getSizeY()];
		boolean finished = false;
		do {
			currentCell.setStatus(Status.PINK);
			Direction next = pickDirection(currentCell);
			if (next != null) {
				currentCell.carveWall(next);
				display.update();
				currentCell = currentCell.getNeighbor(next);
				path[currentCell.getPosition().getX()][currentCell.getPosition().getY()] = opposite(next);
			} else {
				currentCell.setStatus(Status.WHITE);
				display.update();
				currentCell = currentCell.getNeighbor(path[currentCell.getPosition().getX()][currentCell.getPosition().getY()]);
				if (currentCell == null || currentCell.getStatus() == Status.WHITE) {
					finished = true;
				}
			}
		} while (!finished);
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
		
		carvePassage(maze.getCell(new Position(
			(int)(Math.random() * maze.getSizeX()), 
			(int)(Math.random() * maze.getSizeY())
		)));
		
		setEntrances();
		return maze;
	}
}