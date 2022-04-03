public class Eller implements Generator {
	private Maze maze;
	private Display display;
	
	private int[] nextRow;
	private int[] cells;
	private int[][] sets;
	
	private void mergeSet(int setOne, int setTwo){
		for(int y = 0; y < cells.length; y++){
			if(cells[y] == setTwo){
				cells[y] = setOne;
			}
		}
		
		int[] cSet = sets[setOne];
		for(int n = 1; n <= sets[setTwo][0]; n++){
			cSet[++cSet[0]] = sets[setTwo][n];
		}
		sets[setTwo] = new int[maze.getSizeY() + 1];
	}
	
	private boolean setInNextRow(int set){
		for(int i = 0; i < nextRow.length; i++){
			if(nextRow[i] == set){
				return true;
			}
		}
		return false;
	}
	
	private int findEmptySet(){
		for(int set = 0; set < sets.length; set++){
			if(sets[set][0] == 0 && !setInNextRow(set)){
				return set;
			}
		}
		return 0;
	}
	
	private void fleshOut(){
		sets = new int[maze.getSizeY()][maze.getSizeY() + 1];
		for(int y = 0; y < nextRow.length; y++){
			if(nextRow[y] != -1){
				cells[y] = nextRow[y];
				int[] cset = sets[nextRow[y]];
				cset[++cset[0]] = y;
			}else{
				int nextSet = findEmptySet();
				cells[y] = nextSet;
				int[] cset = sets[nextSet];
				cset[++cset[0]] = y;
			}
		}
		nextRow = new int[maze.getSizeY()];
		for (int i = 0; i < nextRow.length; i++) {
			nextRow[i] = -1;
		}
	}
	
	private void createColumn(int colNum) {
		fleshOut();
		for (int y = 0; y < maze.getSizeY() - 1; y++) {
			maze.getCell(new Position(colNum, y)).setStatus(Status.WHITE);
			display.update();
			if (cells[y] != cells[y + 1] && Math.random() > 0.5) {
				//Carve path and merge
				maze.getCell(new Position(colNum, y)).carveWall(Direction.NORTH);
				mergeSet(cells[y], cells[y + 1]);
			}
		}
		maze.getCell(new Position(colNum, maze.getSizeY() - 1)).setStatus(Status.WHITE);
		display.update();
		for (int set = 0; set < sets.length; set++) {
			int inSet = sets[set][0];
			if (inSet != 0) {
				int times = (int)(Math.random() * inSet)+1;
				for (int i = 1; i <= times; i++) {
					int victim = (int)(Math.random() * inSet) + 1;
					int yValue = sets[set][victim];
					maze.getCell(new Position(colNum, yValue)).carveWall(Direction.EAST);
					maze.getCell(new Position(colNum + 1, yValue)).setStatus(Status.WHITE);
					display.update();
					nextRow[yValue] = set;
				}
			}
		}
	}
	
	private void setEntrances() {
		maze.setEntrance(maze.getCell(new Position(0, (int)(Math.random() * maze.getSizeY()))));
		maze.setExit(maze.getCell(new Position(maze.getSizeX() - 1, (int)(Math.random() * maze.getSizeY()))));
	}
	
	public Maze generate(int width, int height, Display display) {
		maze = new Maze(width, height);
		this.display = display;
		display.displayMaze(maze);
		
		cells = new int[height];
		sets = new int[height][height + 1];
		nextRow = new int[height];
		
		for (int i = 0; i < height; i++){
			nextRow[i] = i;
		}
		
		for (int w = 0; w < width - 1; w++){
			createColumn(w);
		}
		
		fleshOut();
		for (int i = 0; i < cells.length - 1; i++){
			if (cells[i] != cells[i + 1]){
				maze.getCell(new Position(maze.getSizeX() - 1, i)).carveWall(Direction.NORTH);
				mergeSet(cells[i], cells[i+1]);
			}
			maze.getCell(new Position(maze.getSizeX() - 1, i)).setStatus(Status.WHITE);
			display.update();
		}
		maze.getCell(new Position(maze.getSizeX() - 1, cells.length - 1)).setStatus(Status.WHITE);
		display.update();
		
		setEntrances();
		return maze;
	}
}