import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Controller {
	private Display display;
	private Maze maze;
	
	private Position player1Pos;
	private Position player2Pos;
	
	private int numPlayers;
	
	private boolean displaySolution;
	
	private boolean canMove(int player, Direction direction) {
		return maze.getCell(player == 1 ? player1Pos : player2Pos).carved(direction);
	}
	
	public Position getPlayer1Pos() {
		return player1Pos;
	}
	
	public Position getPlayer2Pos() {
		return player2Pos;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	public void setDisplay(Display display) {
		this.display = display;
	}
	
	public void resetPlayers() {
		player1Pos = new Position(-1, -1);
		player2Pos = new Position(-1, -1);
	}
	
	public void setMaze(Maze maze) {
		this.maze = maze;
		player1Pos = maze.getEntrance().getPosition();
		player2Pos = maze.getExit().getPosition();
	}
	
	public void play() {
		display.updateWithoutDelay();
		
		Action up1Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(1, Direction.NORTH)) {
					player1Pos = new Position(player1Pos.getX(), player1Pos.getY() + 1);
					display.updateWithoutDelay();
				}
			}
		};
		
		Action right1Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(1, Direction.EAST)) {
					player1Pos = new Position(player1Pos.getX() + 1, player1Pos.getY());
					display.updateWithoutDelay();
				}
			}
		};
		
		Action down1Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(1, Direction.SOUTH)) {
					player1Pos = new Position(player1Pos.getX(), player1Pos.getY() - 1);
					display.updateWithoutDelay();
				}
			}
		};
		
		Action left1Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(1, Direction.WEST)) {
					player1Pos = new Position(player1Pos.getX() - 1, player1Pos.getY());
					display.updateWithoutDelay();
				}
			}
		};
		
		Action up2Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(2, Direction.NORTH)) {
					player2Pos = new Position(player2Pos.getX(), player2Pos.getY() + 1);
					display.updateWithoutDelay();
				}
			}
		};
		
		Action right2Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(2, Direction.EAST)) {
					player2Pos = new Position(player2Pos.getX() + 1, player2Pos.getY());
					display.updateWithoutDelay();
				}
			}
		};
		
		Action down2Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(2, Direction.SOUTH)) {
					player2Pos = new Position(player2Pos.getX(), player2Pos.getY() - 1);
					display.updateWithoutDelay();
				}
			}
		};
		
		Action left2Listen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (canMove(2, Direction.WEST)) {
					player2Pos = new Position(player2Pos.getX() - 1, player2Pos.getY());
					display.updateWithoutDelay();
				}
			}
		};
		
		Action solveListen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				displaySolution = !displaySolution;
				if (displaySolution) {
					MazeSolver solver = new MazeSolver(display); //get rid of display
					solver.solveMaze(maze);
				} else {
					maze.setToWhite();
				}
				display.updateWithoutDelay();
			}
		};
		
		Action exitListen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (display != null) {
					display.toggleDisplayExits();
				}
			}
		};
		
		Action quitListen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				display.quit();
			}
		};
		
		KeyStroke wStroke = KeyStroke.getKeyStroke("W");
		KeyStroke aStroke = KeyStroke.getKeyStroke("A");
		KeyStroke sStroke = KeyStroke.getKeyStroke("S");
		KeyStroke dStroke = KeyStroke.getKeyStroke("D");
		KeyStroke upStroke = KeyStroke.getKeyStroke("UP");
		KeyStroke leftStroke = KeyStroke.getKeyStroke("LEFT");
		KeyStroke downStroke = KeyStroke.getKeyStroke("DOWN");
		KeyStroke rightStroke = KeyStroke.getKeyStroke("RIGHT");
		KeyStroke quitStroke = KeyStroke.getKeyStroke("R");
		KeyStroke solveStroke = KeyStroke.getKeyStroke("T");
		KeyStroke exitStroke = KeyStroke.getKeyStroke("E");
		
		JPanel content = (JPanel)display.getFrame().getContentPane();
		InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(wStroke, "wStroke");
		inputMap.put(aStroke, "aStroke");
		inputMap.put(sStroke, "sStroke");
		inputMap.put(dStroke, "dStroke");
		inputMap.put(upStroke, "upStroke");
		inputMap.put(leftStroke, "leftStroke");
		inputMap.put(downStroke, "downStroke");
		inputMap.put(rightStroke, "rightStroke");
		inputMap.put(quitStroke, "quitStroke");
		inputMap.put(solveStroke, "solveStroke");
		inputMap.put(exitStroke, "exitStroke");
		content.getActionMap().put("wStroke", up1Listen);
		content.getActionMap().put("aStroke", left1Listen);
		content.getActionMap().put("sStroke", down1Listen);
		content.getActionMap().put("dStroke", right1Listen);
		content.getActionMap().put("quitStroke", quitListen);
		content.getActionMap().put("solveStroke", solveListen);
		content.getActionMap().put("exitStroke", exitListen);
		
		if (numPlayers == 1) {
			content.getActionMap().put("upStroke", up1Listen);
			content.getActionMap().put("leftStroke", left1Listen);
			content.getActionMap().put("downStroke", down1Listen);
			content.getActionMap().put("rightStroke", right1Listen);
		} else {
			content.getActionMap().put("upStroke", up2Listen);
			content.getActionMap().put("leftStroke", left2Listen);
			content.getActionMap().put("downStroke", down2Listen);
			content.getActionMap().put("rightStroke", right2Listen);
		}
	}
	
	public Controller(Maze maze, Display display, int numPlayers) {
		this.display = display;
		this.maze = maze;
		displaySolution = false;
		
		this.numPlayers = numPlayers;
		
		if (maze != null) {
			setMaze(maze);
		} else {
			player1Pos = new Position(-1, -1);
			player2Pos = new Position(-1, -1);
		}
	}
	
	public Controller(Maze maze, int numPlayers) {
		this(maze, null, numPlayers);
	}
	
	public Controller(int numPlayers) {
		this(null, null, numPlayers);
	}
}