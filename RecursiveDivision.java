public class RecursiveDivision implements Generator {
	private Maze maze;
	private Display display;
	
	private void divide(int startX, int startY, int width, int height){
        boolean horizontal = true;
        if (width > height) {
            horizontal = false;
        } else if (width == height) {
            horizontal = Math.random() > 0.5;
        }
        if (width < 2 || height < 2) {
            return;
        }
        if (horizontal) {
            int y = (int)(Math.random() * height - 1);
            int hole = (int)(Math.random() * width);
            for (int x = 0; x < width; x++) {
                if (x != hole) {
					maze.getCell(new Position(x + startX, y + startY)).createWall(Direction.NORTH);
					if (x + startX - 1 >= 0 && y + startY + 1 < maze.getSizeY()) {
						maze.getCell(new Position(x + startX - 1, y + startY + 1)).setStatus(Status.WHITE);
					}
                }
            }
			display.update();
            divide(startX, startY + y + 1, width, height - y - 1);
            divide(startX, startY, width, y + 1);
        } else {
            int x = (int)(Math.random() * width - 1);
            int hole = (int)(Math.random() * height);
            for (int y = 0; y < height; y++) {
                if (y != hole) {
					maze.getCell(new Position(x + startX, y + startY)).createWall(Direction.EAST);
					maze.getCell(new Position(x + startX, y + startY)).setStatus(Status.WHITE);
                }
            }
			display.update();
            divide(startX + x + 1, startY, width - x - 1, height);
            divide(startX, startY, x + 1, height);
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
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze.getCell(new Position(x, y)).carveWall(Direction.NORTH);
				maze.getCell(new Position(x, y)).carveWall(Direction.EAST);
				maze.getCell(new Position(x, y)).setStatus(Status.PUREWHITE);
			}
        }
        
        divide(0, 0, width, height);
		
		//maze.setWhite();
		setEntrances();
		return maze;
	}
}