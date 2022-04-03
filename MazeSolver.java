import java.util.ArrayList;

public class MazeSolver {
	private Maze maze;
	
	private Cell currentCell;
	
	//BEGIN TEST
	private Display display;
	//END TEST
	
	private int[][] marks;
	
	private ArrayList<Cell> getNeighborsWithMark(int mark) {
		ArrayList<Cell> options = new ArrayList<Cell>();
		if (currentCell.carved(Direction.NORTH) && marks[currentCell.getPosition().getX()][currentCell.getPosition().getY() + 1] == mark) {
			options.add(currentCell.getNeighbor(Direction.NORTH));
		}
		if (currentCell.carved(Direction.EAST) && marks[currentCell.getPosition().getX() + 1][currentCell.getPosition().getY()] == mark) {
			options.add(currentCell.getNeighbor(Direction.EAST));
		}
		if (currentCell.carved(Direction.SOUTH) && marks[currentCell.getPosition().getX()][currentCell.getPosition().getY() - 1] == mark) {
			options.add(currentCell.getNeighbor(Direction.SOUTH));
		}
		if (currentCell.carved(Direction.WEST) && marks[currentCell.getPosition().getX() - 1][currentCell.getPosition().getY()] == mark) {
			options.add(currentCell.getNeighbor(Direction.WEST));
		}
		return options;
	}
	
	private void handleCell() {
		//	currentCell.setStatus(Status.PINK);
		//	display.update();		
		

		//Step 1: Mark current cell
		if (marks[currentCell.getPosition().getX()][currentCell.getPosition().getY()] == 0) {
			marks[currentCell.getPosition().getX()][currentCell.getPosition().getY()] = 1;
			//BEGIN TEST
			//currentCell.setStatus(Status.PINK);
			//display.update();
			//END TEST
		}
		
		//Step 2: Pick from a list of neighboring cells w/ zero marks.
		ArrayList<Cell> zeroCells = getNeighborsWithMark(0);
		
		//Step 3: If found, move to that cell and handle that cell.
		if (zeroCells.size() > 0) {
			currentCell = zeroCells.get((int)(Math.random() * zeroCells.size()));
		}
		
		//Step 4: If not found, mark current cell again and move to n. cell with one mark.
		else {
			ArrayList<Cell> oneCells = getNeighborsWithMark(1);
			marks[currentCell.getPosition().getX()][currentCell.getPosition().getY()]++;
			//BEGIN TEST
		//	currentCell.setStatus(Status.GRAY);
		//	display.update();
			//END TEST
			currentCell = oneCells.get((int)(Math.random() * oneCells.size()));
		}
	}
	
	public void solveMaze(Maze maze) {
		this.maze = maze;
		currentCell = maze.getEntrance();
//currentCell.setStatus(Status.PINK);
//display.update();		
		marks = new int[maze.getSizeX()][maze.getSizeY()];
		
		while (currentCell != maze.getExit()) {
			handleCell();
		}
		
		for (int x = 0; x < maze.getSizeX(); x++) {
			for (int y = 0; y < maze.getSizeY(); y++) {
				if (marks[x][y] == 1) {
					maze.getCell(new Position(x, y)).setStatus(Status.WHITE);
				} else {
					maze.getCell(new Position(x, y)).setStatus(Status.GRAY);
				}
			}
		}
		
		maze.getExit().setStatus(Status.WHITE);
	}
	
	public MazeSolver(Display display) {
		this.display = display;
	}
}