import java.util.ArrayList;

public class Kruskal implements Generator {
	private Maze maze;
	private Display display;
	
	private ArrayList<ArrayList<Integer>> sets;
	private ArrayList<Edge> edges;
	private int[][] cells;
	
	private class Edge {
		int x;
		int y;
		Direction dir;
		
		Edge(int x, int y, Direction dir){
			this.x = x;
			this.y = y;
			this.dir = dir;
		}
	}
	
	private Edge pickEdge(){
		return edges.get((int)(Math.random() * edges.size()));
	}
	
	private void mergeSets(int setOne, int setTwo){
		int sizeY = maze.getSizeY();
		ArrayList<Integer> fromSet = sets.get(setTwo);
		ArrayList<Integer> toSet = sets.get(setOne);
		
		int s = fromSet.size();
		for(int i = 0; i < s; i++){
			int n = fromSet.get(0);
			setSet((n - (n % sizeY))/sizeY, n % sizeY, setOne);
		}
	}
	
	private void setSet(int x, int y, int set){
		int oldSet = cells[x][y];
		cells[x][y] = set;
		if(oldSet != -1){
			ArrayList<Integer> cset = sets.get(oldSet);
			cset.remove(cset.indexOf(x * maze.getSizeY() + y));
			sets.get(set).add(x * maze.getSizeY() + y);
		}else{
			sets.add(x * maze.getSizeY() + y, new ArrayList<Integer>());
			ArrayList<Integer> newSet = sets.get(x * maze.getSizeY() + y);
			newSet.add(x * maze.getSizeY() + y);
		}
	}

	private int getSet(int x, int y){
		return cells[x][y];
	}
	
	private int translateX(Direction d) {
		if (d == Direction.EAST) {
			return 1;
		} else if (d == Direction.WEST) {
			return -1;
		}
		return 0;
	}
	
	private int translateY(Direction d) {
		if (d == Direction.NORTH) {
			return 1;
		} else if (d == Direction.SOUTH) {
			return -1;
		}
		return 0;
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
		
		sets = new ArrayList<ArrayList<Integer>>();
		edges = new ArrayList<Edge>();
		cells = new int[width][height];
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				cells[x][y] = -1;
			}
		}
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				setSet(x, y, x * height + y);
				if(x < width - 1){
					edges.add(new Edge(x, y, Direction.EAST));
				}
				if(y < height - 1){
					edges.add(new Edge(x, y, Direction.NORTH));
				}
			}
		}
		
		while(edges.size() > 0){
			Edge goner = pickEdge();
			int setC = getSet(goner.x, goner.y);
			int setD = getSet(goner.x + translateX(goner.dir), goner.y + translateY(goner.dir));
			if(setC != setD){
				maze.getCell(new Position(goner.x, goner.y)).carveWall(goner.dir);
				maze.getCell(new Position(goner.x, goner.y)).setStatus(Status.WHITE);
				maze.getCell(new Position(goner.x, goner.y)).getNeighbor(goner.dir).setStatus(Status.WHITE);
				display.update();
				mergeSets(setC, setD);
			}
			edges.remove(edges.indexOf(goner));
		}
		
		setEntrances();
		return maze;
	}
}