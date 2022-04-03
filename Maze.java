public class Maze {
	private int sizeX;
	private int sizeY;
	
	private Cell[][] maze;
	
	private Cell entrance;
	private Cell exit;
	
	public Cell getCell(Position pos) {
		return maze[pos.getX()][pos.getY()];
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public Cell getEntrance() {
		return entrance;
	}
	
	public Cell getExit() {
		return exit;
	}
	
	public void setEntrance(Cell entrance) {
		this.entrance = entrance;
	}
	
	public void setExit(Cell exit) {
		this.exit = exit;
	}
	
	public void setToWhite() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				maze[x][y].setStatus(Status.WHITE);
			}
		}
	}
	
	public Maze(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		maze = new Cell[sizeX][sizeY];
		entrance = null;
		exit = null;
		
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				maze[x][y] = new Cell(new Position(x, y));
			}
		}
		
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				try {
					maze[x][y].setNeighbor(Direction.NORTH, maze[x][y + 1]);
				} catch(Exception e){}
				try {
					maze[x][y].setNeighbor(Direction.EAST, maze[x + 1][y]);
				} catch(Exception e){}
				try {
					maze[x][y].setNeighbor(Direction.SOUTH, maze[x][y - 1]);
				} catch(Exception e){}
				try {
					maze[x][y].setNeighbor(Direction.WEST, maze[x - 1][y]);
				} catch(Exception e){}
			}
		}
	}
}