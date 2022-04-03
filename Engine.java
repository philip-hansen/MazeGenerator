public class Engine {
	public final static int SIZE_X = 30;
	public final static int SIZE_Y = 30;
	
	private Controller controller;
	private Display display;
	
	private int defaultAlgorithm;
	private int defaultSizeX;
	private int defaultSizeY;
	private int defaultNumPlayers;
	
	public final static String[] ALGORITHMS = {
		"Recursive Backtracker", 
		"Hunt and Kill", 
		"Aldous-Broder", 
		"Recursive Division",
		"Prim", 
		"Wilson", 
		"Kruskal",
		"Eller",
		"Sidewinder",   
		"Binary Tree"};
		
	public Controller getController() {
		return controller;
	}
	
	public int getDefaultAlgorithm() {
		return defaultAlgorithm;
	}
	
	public int getDefaultSizeX() {
		return defaultSizeX;
	}
	
	public int getDefaultSizeY() {
		return defaultSizeY;
	}
	
	public int getDefaultNumPlayers() {
		return defaultNumPlayers;
	}
	
	public void generateMaze(String algorithm, int sizeX, int sizeY, int numPlayers) {
		defaultSizeX = sizeX;
		defaultSizeY = sizeY;
		defaultNumPlayers = numPlayers;
		Generator generator;
		switch (algorithm) {
			case "Recursive Backtracker":
				generator = new RecursiveBacktracker();
				defaultAlgorithm = 0;
				break;
			case "Hunt and Kill":
				generator = new HuntAndKill();
				defaultAlgorithm = 1;
				break;
			case "Binary Tree":
				generator = new BinaryTree();
				defaultAlgorithm = 9;
				break;
			case "Aldous-Broder":
				generator = new AldousBroder();
				defaultAlgorithm = 2;
				break;
			case "Sidewinder":
				generator = new Sidewinder();
				defaultAlgorithm = 8;
				break;
			case "Wilson":
				generator = new Wilson();
				defaultAlgorithm = 5;
				break;
			case "Prim":
				generator = new Prim();
				defaultAlgorithm = 4;
				break;
			case "Kruskal":
				generator = new Kruskal();
				defaultAlgorithm = 6;
				break;
			case "Eller":
				generator = new Eller();
				defaultAlgorithm = 7;
				break;
			default:
				generator = new RecursiveDivision();
				defaultAlgorithm = 3;
		}
		controller.setNumPlayers(numPlayers);
		Maze maze = generator.generate(sizeX, sizeY, display);
		controller.setDisplay(display);
		controller.setMaze(maze);
		controller.play();
	}
	
	public void play() {
		display.displayHome();
	}
	
	public Engine() {		
		defaultAlgorithm = 0;
		defaultSizeX = SIZE_X;
		defaultSizeY = SIZE_Y;
		defaultNumPlayers = 0;
		
		this.controller = new Controller(defaultNumPlayers);
		this.display = new Display(this);
	}
	
	public static void main(String[] args) {
		Engine e = new Engine();
		e.play();
	}
}