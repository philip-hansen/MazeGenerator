public class BinaryTree implements Generator {
	private Maze maze;
	private Display display;
	
	private void setEntrances() {
		if (Math.random() > 0.5) {
			maze.setEntrance(maze.getCell(new Position((int)(Math.random() * (maze.getSizeX() - 1)), 0)));
			maze.setExit(maze.getCell(new Position(maze.getSizeX() - 1, (int)(Math.random() * maze.getSizeY()))));
		} else {
			maze.setExit(maze.getCell(new Position((int)(Math.random() * (maze.getSizeX() - 1)), 0)));
			maze.setEntrance(maze.getCell(new Position(maze.getSizeX() - 1, (int)(Math.random() * maze.getSizeY()))));
		}
	}
	
	public Maze generate(int width, int height, Display display) {
		maze = new Maze(width, height);
		this.display = display;
		display.displayMaze(maze);

		maze.getCell(new Position(0, height - 1)).setStatus(Status.WHITE);
		display.update();
		
		for (int x = 1; x < width; x++) {
			maze.getCell(new Position(x, height - 1)).carveWall(Direction.WEST);
			maze.getCell(new Position(x, height - 1)).setStatus(Status.WHITE);
			display.update();
		}
		
		for (int y = height - 2; y >= 0; y--) {
			maze.getCell(new Position(0, y)).carveWall(Direction.NORTH);
			maze.getCell(new Position(0, y)).setStatus(Status.WHITE);
			display.update();
			for (int x = 1; x < width; x++) {
				Direction dir = Math.random() > 0.5 ? Direction.WEST : Direction.NORTH;
				maze.getCell(new Position(x, y)).carveWall(dir);
				maze.getCell(new Position(x, y)).setStatus(Status.WHITE);
				display.update();
			}
		}
		
		setEntrances();
		return maze;
	}
}