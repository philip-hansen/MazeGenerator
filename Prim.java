import java.util.ArrayList;

public class Prim implements Generator {
	private Maze maze;
	private Display display;
	
	private ArrayList<Cell> frontier;
	
	private void insertFrontier(Cell cell){
		if (cell != null && cell.getStatus() == Status.GRAY) {
			frontier.add(cell);
			cell.setStatus(Status.PINK);
		}
	}
	
	private void mark(Cell cell){
		cell.setStatus(Status.WHITE);
		insertFrontier(cell.getNeighbor(Direction.NORTH));
		insertFrontier(cell.getNeighbor(Direction.EAST));
		insertFrontier(cell.getNeighbor(Direction.SOUTH));
		insertFrontier(cell.getNeighbor(Direction.WEST));
		display.update();
	}
	
	private Direction pickInNeighbor(Cell cell){
		Direction options[] = new Direction[4];
		int optionSize = 0;
		if (cell.getNeighbor(Direction.NORTH) != null && cell.getNeighbor(Direction.NORTH).getStatus() == Status.WHITE) {
			options[optionSize++] = Direction.NORTH;
		}
		if (cell.getNeighbor(Direction.EAST) != null && cell.getNeighbor(Direction.EAST).getStatus() == Status.WHITE) {
			options[optionSize++] = Direction.EAST;
		}
		if (cell.getNeighbor(Direction.SOUTH) != null && cell.getNeighbor(Direction.SOUTH).getStatus() == Status.WHITE) {
			options[optionSize++] = Direction.SOUTH;
		}
		if (cell.getNeighbor(Direction.WEST) != null && cell.getNeighbor(Direction.WEST).getStatus() == Status.WHITE) {
			options[optionSize++] = Direction.WEST;
		}
		return options[(int)(Math.random() * optionSize)];
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
		
		frontier = new ArrayList<Cell>();
		
		mark(maze.getCell(new Position((int)(Math.random()*width), (int)(Math.random()*height))));
		while (frontier.size() > 0) {
			Cell victim = frontier.get((int)(Math.random() * frontier.size()));
			Direction dir = pickInNeighbor(victim);
			victim.carveWall(dir);
			mark(victim);
			frontier.remove(victim);
		}
		
		setEntrances();
		return maze;
	}
}