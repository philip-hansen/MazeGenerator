public class AldousBroder implements Generator {
	private Maze maze;
	private Display display;
	
	private int remaining;
	private Cell currentCell;
	
	private void carve() {
		Direction[] options = new Direction[4];
		int optionSize = 0;
		if (currentCell.getNeighbor(Direction.NORTH) != null) {
			options[optionSize++] = Direction.NORTH;
		}
		if (currentCell.getNeighbor(Direction.EAST) != null) {
			options[optionSize++] = Direction.EAST;
		}
		if (currentCell.getNeighbor(Direction.SOUTH) != null) {
			options[optionSize++] = Direction.SOUTH;
		}
		if (currentCell.getNeighbor(Direction.WEST) != null) {
			options[optionSize++] = Direction.WEST;
		}
		Direction dir = options[(int)(Math.random() * optionSize)];
		Cell newCell = currentCell.getNeighbor(dir);
		if (newCell.getStatus() != Status.WHITE) {
			remaining--;
			currentCell.carveWall(dir);
			newCell.setStatus(Status.WHITE);
			display.update();
		}
		currentCell = newCell;
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
		
		currentCell = maze.getCell(new Position((int)(Math.random() * width), (int)(Math.random() * height)));
		remaining = width * height - 1;
		
		currentCell.setStatus(Status.WHITE);
		display.update();
		
		do {
			carve();
		} while (remaining > 0);
		
		setEntrances();
		return maze;
	}
}