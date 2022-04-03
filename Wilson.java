public class Wilson implements Generator {
	private Maze maze;
	private Display display;
	
	private Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	
	boolean remaining;
	
	int[][] frontier;
	
	private void retrace(Cell origin) {
		origin.setStatus(Status.WHITE);
		int dir;
		do {
			dir = frontier[origin.getPosition().getX()][origin.getPosition().getY()];
			if (dir != 0) {
				origin.setStatus(Status.WHITE);
				origin.carveWall(directions[dir - 1]);
				origin = origin.getNeighbor(directions[dir - 1]);
				display.update();
			}
		} while (dir != 0);
	}
	
	private void walk(Cell currentCell) {
		while (currentCell.getStatus() != Status.WHITE) {
			if (currentCell.getStatus() != Status.PINK) {
				currentCell.setStatus(Status.PINK);
				display.update();
			}
			int dir;
			do {
				dir = (int)(Math.random() * 4) + 1;
			} while (currentCell.getNeighbor(directions[dir - 1]) == null);
			frontier[currentCell.getPosition().getX()][currentCell.getPosition().getY()] = dir;
			currentCell = currentCell.getNeighbor(directions[dir - 1]);
		}
	}
	
	private void clearFrontier() {
		remaining = false;
		frontier = new int[maze.getSizeX()][maze.getSizeY()];
		for (int x = 0; x < maze.getSizeX(); x++) {
			for (int y = 0; y < maze.getSizeY(); y++) {
				if (maze.getCell(new Position(x, y)).getStatus() == Status.PINK) {
					maze.getCell(new Position(x, y)).setStatus(Status.GRAY);
				} else if (maze.getCell(new Position(x, y)).getStatus() == Status.GRAY) {
					remaining = true;
				}
			}
		}
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
		
		remaining = true;
		frontier = new int[width][height];
		
		maze.getCell(new Position((int)(Math.random() * width), (int)(Math.random() * height))).setStatus(Status.WHITE);
		
		do {
			Cell pathOrigin;
			do {
				pathOrigin = maze.getCell(new Position((int)(Math.random() * width), (int)(Math.random() * height)));
			} while (pathOrigin.getStatus() == Status.WHITE);
			walk(pathOrigin);
			retrace(pathOrigin);
			clearFrontier();
		} while (remaining);
		
		setEntrances();
		return maze;
	}
}