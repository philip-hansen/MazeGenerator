import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

//import javax.swing.SwingUtilities;
//import java.lang.Runnable;
//import javax.swing.Timer;
//import java.awt.event.WindowEvent;

public class Display2 {
	private final int CELL_SIZE_X = 15;
	private final int CELL_SIZE_Y = 15;
	
	private final boolean DEFAULT_ANIMATE = true;
	
	private final Color PLAYER_ONE_COLOR = new Color(119, 255, 119);
	private final Color PLAYER_TWO_COLOR = new Color(119, 119, 255);
	private final Color OVERLAP_COLOR = new Color(119, 187, 187);
	private final Color PLAYER_ONE_EXIT_COLOR = new Color(140, 200, 140);
	private final Color PLAYER_TWO_EXIT_COLOR = new Color(140, 140, 200);
	
	private JFrame mazeFrame;
	private JPanel mazePanel;
		
	private Engine engine;
	
	private boolean displayExits;
	private boolean animate;
	
	public void update() {
		if (animate) {
			mazePanel.repaint();
			try {
				Thread.sleep(10); //ORIG 20
			} catch(Exception e){}
		}
	}
	
	public void updateWithoutDelay() {
		mazePanel.repaint();
	}
	
	public JFrame getFrame() {
		return mazeFrame;
	}
	
	public void toggleDisplayExits() {
		displayExits = !displayExits;
		update();
	}
	
	public void quit() {
		mazeFrame.dispose();
		displayHome();
	}
	
	public void displayHome() {
		JFrame homeFrame = new JFrame("Maze");
		homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel homePanel = new JPanel();
		JButton genButton = new JButton("Generate");
		JComboBox algorithmBox = new JComboBox(Engine.ALGORITHMS);
		JComboBox numPlayersBox = new JComboBox(new String[]{"0 Players", "1 Player", "2 Players"});
		JSpinner widthSpinner = new JSpinner();
		JSpinner heightSpinner = new JSpinner();
		JCheckBox animateCheck = new JCheckBox("Animate");
		
		widthSpinner.setValue(engine.getDefaultSizeX());
		heightSpinner.setValue(engine.getDefaultSizeY());
		algorithmBox.setSelectedIndex(engine.getDefaultAlgorithm());
		numPlayersBox.setSelectedIndex(engine.getDefaultNumPlayers());
		animateCheck.setSelected(animate);
		
		homePanel.setLayout(null);
		homePanel.setBackground(Color.WHITE);
		
		genButton.setBounds(190, 148, 100, 20);
        algorithmBox.setBounds(50, 20, 200, 24);
        numPlayersBox.setBounds(50, 50, 150, 24);
        widthSpinner.setBounds(50, 80, 60, 24);
        heightSpinner.setBounds(50, 110, 60, 24);
		animateCheck.setBounds(50, 140, 100, 24);
		
		homeFrame.getRootPane().setDefaultButton(genButton);
		
		genButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				homeFrame.dispose();
				
				
				String algorithmName = (String)algorithmBox.getSelectedItem();
				animate = animateCheck.isSelected();
				int sizeX = (Integer)widthSpinner.getValue();
				int sizeY = (Integer)heightSpinner.getValue();
				int numPlayers;
				switch ((String)numPlayersBox.getSelectedItem()) {
					case "0 Players":
						numPlayers = 0;
						break;
					case "1 Player":
						numPlayers = 1;
						break;
					default:
						numPlayers = 2;
				}
				Thread t = new Thread() {
					public void run() {
						engine.generateMaze(algorithmName, sizeX, sizeY, numPlayers);
					}
				};
				t.start();
			}
		});
		
		homePanel.add(genButton);
		homePanel.add(algorithmBox);
		homePanel.add(numPlayersBox);
		homePanel.add(widthSpinner);
		homePanel.add(heightSpinner);
		homePanel.add(animateCheck);
		
		homeFrame.add(homePanel);
		homeFrame.setBounds(300, 100, 300+20, 200+20);
		homeFrame.setVisible(true);
	}
	
	public void displayMaze(Maze maze) {
		mazeFrame = new JFrame("MAZE");
		mazeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Controller controller = engine.getController();
		mazePanel = new JPanel(new GridLayout(10, 10)) {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				for (int x = 0; x < maze.getSizeX(); x++) {
					for (int y = 0; y < maze.getSizeY(); y++) {
						Cell currentCell = maze.getCell(new Position(x, y));
						if (controller.getNumPlayers() > 0 && currentCell.getPosition().equals(controller.getPlayer1Pos())) {
							if (controller.getNumPlayers() == 2 && controller.getPlayer1Pos().equals(controller.getPlayer2Pos())) {
								g.setColor(OVERLAP_COLOR);
							} else {
								g.setColor(PLAYER_ONE_COLOR);
							}
						} else if (controller.getNumPlayers() == 2 && currentCell.getPosition().equals(controller.getPlayer2Pos())) {
							g.setColor(PLAYER_TWO_COLOR);
						} else if (displayExits && currentCell == maze.getExit()) {
							g.setColor(PLAYER_ONE_EXIT_COLOR);
						} else if (displayExits && currentCell == maze.getEntrance()) {
							g.setColor(PLAYER_TWO_EXIT_COLOR);
						} else {
							Status status = currentCell.getStatus();
							if (status == Status.GRAY) {
								g.setColor(new Color(204, 204, 204));
							} else if (status == Status.PINK) {
								g.setColor(new Color(255, 170, 170));
							} else {
								g.setColor(Color.WHITE);
							}
						}
						g.fillRect(x * CELL_SIZE_X + 1, ((maze.getSizeY() - 1) * CELL_SIZE_Y) - y * CELL_SIZE_Y + 1, 
							currentCell.carved(Direction.EAST) ? CELL_SIZE_X : CELL_SIZE_X - 1, currentCell.carved(Direction.SOUTH) ? CELL_SIZE_Y : CELL_SIZE_Y - 1);
						if (currentCell.getStatus() != Status.PUREWHITE && currentCell.carved(Direction.EAST) && currentCell.carved(Direction.SOUTH)) {
							g.setColor(Color.BLACK);
							g.fillRect(x * CELL_SIZE_X + CELL_SIZE_X, ((maze.getSizeY() - 1) * CELL_SIZE_Y) - y * CELL_SIZE_Y + CELL_SIZE_Y, 1, 1);
						}
					}
				}
			}
		};
		controller.resetPlayers();
		
		displayExits = controller.getNumPlayers() > 0;
		mazePanel.setBackground(Color.BLACK);
		mazeFrame.setBounds(300, 100, maze.getSizeX() * CELL_SIZE_X+1+18, maze.getSizeY() * CELL_SIZE_Y+23+25);
		mazeFrame.add(mazePanel);
		mazeFrame.setVisible(true);
	}
	
	public Display2(Engine engine) {
		this.engine = engine;
		animate = DEFAULT_ANIMATE;
	}
}