public class Sidewinder implements Generator {
	private Maze maze;
	private Display display;
	
    private void runRow(int rowNum){
        int[] set = new int[maze.getSizeX()];
        int setSize = 0;
        
        for (int x = 0; x < maze.getSizeX(); x++){
            set[setSize++] = x;
			maze.getCell(new Position(x, rowNum)).setStatus(Status.WHITE);
            if(Math.random() > 0.5 && x != maze.getSizeX() - 1){
				maze.getCell(new Position(x, rowNum)).carveWall(Direction.EAST);
				maze.getCell(new Position(x + 1, rowNum)).setStatus(Status.WHITE);
            } else {
                int cell = set[(int)(Math.random() * setSize)];
				maze.getCell(new Position(cell, rowNum)).carveWall(Direction.NORTH);
                set = new int[maze.getSizeX()];
                setSize = 0;
            }
			display.update();
        }
    }
	
	private void setEntrances() {
		maze.setEntrance(maze.getCell(new Position(Math.random() > 0.5 ? 0 : maze.getSizeX() - 1, (int)(Math.random() * (maze.getSizeY() - 1)) + 1)));
		maze.setExit(maze.getCell(new Position((int)(Math.random() * maze.getSizeX()), 0)));
	}
	
	public Maze generate(int width, int height, Display display) {
		maze = new Maze(width, height);
		this.display = display;
		display.displayMaze(maze);
		
        for (int x = 0; x < width - 1; x++) {
            maze.getCell(new Position(x, height - 1)).carveWall(Direction.EAST);
			maze.getCell(new Position(x, height - 1)).setStatus(Status.WHITE);
			display.update();
        }
		
		maze.getCell(new Position(width - 1, height - 1)).setStatus(Status.WHITE);
		display.update();
		
		for (int y = height - 2; y >= 0; y--) {
            runRow(y);
        }
		
		setEntrances();
		return maze;
	}
}