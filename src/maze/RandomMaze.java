package maze;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class RandomMaze extends JFrame {
	//Declare instance variables
	private int rowN = MainMenu.getBoardSize();
	private int colN = MainMenu.getBoardSize();
	private Cell[][] board = new Cell[rowN][colN];
	private Cell[][] testBoard = new Cell[rowN][colN];
	private JButton jbtFindPath = new JButton("Find Path");
	private JButton jbtClearPath = new JButton("Clear Path");
	private JButton jbtNewMaze = new JButton("New Maze");			// added 5/8 JB
	private JButton jbtClose = new JButton("Close");				// added 5/8 JB
	private JPanel jpBoard, jpButton;
	private JLabel jlblStatus = new JLabel("Started..");
	private boolean pressed = false;				// added 5/8 JB
	private boolean pathFound = false;
	private static MainMenu mainFrame;
	public RandomMaze() {
		//Setup the GUI
		setTitle("Random Maze");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // Center the frame
		setVisible(true);
		jpBoard = new JPanel();
		jpBoard.setLayout(new GridLayout(rowN, colN));
		jpButton = new JPanel();
		jpButton.setLayout(new FlowLayout());
		jpButton.add(jbtFindPath);
		jpButton.add(jbtClearPath);
		jpButton.add(jbtNewMaze);			// added 5/8 JB
		jpButton.add(jbtClose);				// added 5/8 JB
		board = createRandomMaze(testBoard);
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				jpBoard.add(board[row][col]);
			}
		}
		// Add jpBoard, jpButtons and jlblStatus to the applet
		setLayout(new BorderLayout());
		add(jlblStatus, BorderLayout.NORTH);
		add(jpBoard, BorderLayout.CENTER);
		add(jpButton, BorderLayout.SOUTH);
		clearPath();
		setMarkers();
		// Register listeners
		jbtFindPath.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(pathFound){
					clearPath();
					findPath();
				}else{
					findPath();
				}
			}
		});
		jbtClearPath.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearPath();
				setMarkers();
			}
		});
		jbtNewMaze.addActionListener(new ActionListener(){		// added 5/8
			@Override
			public void actionPerformed(ActionEvent e) {
				clearAll();
				jpBoard.removeAll();
				board = createRandomMaze(testBoard); 				//added 5/8 AA
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board[row].length; col++) {
						jpBoard.add(board[row][col]);
					}
				}
				validate();
				clearPath();
				setMarkers();
			}
		});
		jbtClose.addActionListener(new ActionListener(){		// added 5/8
			@Override
			public void actionPerformed(ActionEvent e) {
				closeWindow();
			}
		});
	}
	// Main method
	public static void main(String[] args) {
		// Create a frame
		RandomMaze frame = new RandomMaze();
	}
	//Method used to set the parent frame (the MainMenu frame)
	public void setParent(MainMenu parentFrame){
		mainFrame = parentFrame;
	}
	//Method determines if path exists or not
	public void findPath() {
		if (findPath(0, 0)) {
			jlblStatus.setText("path found");
			pathFound = true;
		}
		else {
			jlblStatus.setText("No path exists");
		}
	}
	//Method used to recursively find a traversable path
	public boolean findPath(int row, int col) {
		board[row][col].visit();
		//Base case
		//If the path is at the end, return true
		if ((col == colN-1) && (row == rowN-1)) {
			board[row][col].selectCell();
			return true;
		}
		//If the path can move down one cell, do so
		if ((row < rowN-1) && !board[row + 1][col].marked() &&!board[row + 1][col].blocked() && !board[row + 1][col].visited()) {
			block(row, col);
			if (findPath(row + 1, col)) {
				board[row][col].selectCell();
				return true;
			}
			unblock(row, col);
		}
		//If the path can move to the right one cell, do so
		if ((col < colN-1) && !board[row][col + 1].marked() && !board[row][col + 1].blocked() && !board[row][col + 1].visited()) {
			block(row,col);
			if (findPath(row, col + 1)) {
				board[row][col].selectCell();
				return true;
			}
			unblock(row,col);
		}
		//If the path can move up one cell, do so
		if ((row > 0) && !board[row - 1][col].marked() && !board[row - 1][col].blocked() && !board[row - 1][col].visited()) {
			block(row, col);
			if (findPath(row - 1, col)) {
				board[row][col].selectCell();
				return true;
			}
			unblock(row, col);
		}
		//If the path can move to the left one cell, do so.
		if ((col > 0) && !board[row][col - 1].marked() && !board[row][col - 1].blocked() && !board[row][col - 1].visited()) {
			block(row,col);
			if (findPath(row, col - 1)) {
				board[row][col].selectCell();
				return true;
			}
			unblock(row,col);
		}
		return false;
	}
	//Block adjacent cell to prevent neighboring path
	public void block(int row, int col) {
		if (row > 0)
			board[row - 1][col].block();
		if (row < rowN-1)
			board[row + 1][col].block();
		if (col > 0) 
			board[row][col - 1].block();
		if (col < colN-1)
			board[row][col + 1].block();
	}
	//Remove the block if within range of the array
	public void unblock(int row, int col) {
		if (row > 0)
			board[row - 1][col].unblock();
		if (row < rowN-1)
			board[row + 1][col].unblock();
		if (col > 0)
			board[row][col - 1].unblock();
		if (col < colN-1)
			board[row][col + 1].unblock();
	}
	//Method for closing the window
	public void closeWindow(){
		WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
		mainFrame.setEnabled(true);		//Added 5/10 AA
	}
	//Method to only clear the path
	public void clearPath() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].deselectCell();
			}
		}
	}
	//Method to clear the board
	public void clearAll() {								// added 5/8 JB
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].resetCell();
			}
		}
	}
	//Getter method to determine if the mouse is pressed
	public boolean isPressed(){			// added 5/8 JB
		return pressed;
	}
	//Setter method to set the mouse pressed variable
	public void setPressed(boolean e){		// added 5/8 JB
		pressed = e;
	}
	// method to add start and finish graphics to the board
	public void setMarkers(){
		board[0][0].setStart();
		board[rowN-1][colN-1].setFinish();
	}
	//Method to reset the path
	protected void resetPath() {
		pathFound = false;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].unblock();
				board[row][col].deselectCell();
				unblock(row, col);
			}
		}
		validate();
		repaint();
	}
	//Method used to create a random maze in the passed Cell array
	//and return a populate Cell array
	public Cell[][] createRandomMaze(Cell[][] nBoard){
		//Populate array
		for (int row = 0; row < nBoard.length; row++) {
			for (int col = 0; col < nBoard[row].length; col++) {
				nBoard[row][col] = new Cell();
				if((row > 0 && col > 0)){
					//Create a random instance to set the cell as marked
					int randSeed = (int)(Math.random() * 100 + 1);
					if(randSeed > 50)
						nBoard[row][col].setMarked();
				}
			}
		}
		MazeTester newMaze = new MazeTester(nBoard, rowN, colN );
		//If the new maze is not traversable, then call the method again
		//This way the method will eventually create a traversable maze
		if(!newMaze.isTraversable())
			createRandomMaze(nBoard);
		return nBoard;
	}
	public void printBoard(Cell[][] newBoard){
		for (int row = 0; row < newBoard.length; row++) {
			for (int col = 0; col < newBoard[row].length; col++) {
				System.out.print(newBoard[row][col].marked() + " | ");
			}
			System.out.println();
		}
	}
	//Inner Cell class
	class Cell extends JPanel implements MouseListener {
		//Declare instance variables
		private boolean marked = false;
		private boolean visited = false;
		private boolean blocked = false;
		private boolean start = false;
		private boolean finish = false;
		//Constructor
		public Cell() {
			setBackground(Color.DARK_GRAY);
			addMouseListener(this);
		}
		//Accessor methods
		public void setStart(){
			start = true;
		}
		public boolean isStart(){
			return start;
		}
		public void setFinish(){
			finish = true;
		}
		public boolean isFinish(){
			return finish;
		}
		public void removeMarker(){
			start = false;
			finish = false;
		}
		public boolean marked() {
			return marked;
		}
		public void setMarked(){
			marked = true;
		}
		public void visit() {
			visited = true;
		}
		public boolean visited() {
			return visited;
		}
		public boolean blocked() {
			return blocked;
		}
		public void block() {
			blocked = true;
		}
		public void unblock() {
			blocked = false;
		}
		//Method used to indicate the cell is selected as a path cell
		public void selectCell() {
			setBackground(Color.red);
			repaint();
		}
		//Method used to return the cell to an unselected state
		public void deselectCell() {
			if(visited){
				setBackground(Color.DARK_GRAY);
			}
			visited = false;
			blocked = false;
			repaint();
		}
		//Method used to reset the cell completely
		public void resetCell(){				// added 5/8 JB
			setBackground(Color.DARK_GRAY);
			marked = false;
			visited = false;
			blocked = false;
			repaint();

		}
		//Method used to draw the cells corresponding graphics for either the 
		//start, finish, or marked cells
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			try{
				if(isStart()){		//added 5/8 AA
					BufferedImage image = ImageIO.read(new File("StartIcon.jpg"));
					Image scaledImage = image.getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT);
					g.drawImage(scaledImage, 0 ,0,null);
				}
				if(isFinish()){		//added 5/8 AA
					BufferedImage image = ImageIO.read(new File("FinishIcon.jpg"));
					Image scaledImage = image.getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT);
					g.drawImage(scaledImage, 0 ,0,null);
				}
				if (marked) {
					BufferedImage image = ImageIO.read(new File("Brick.jpg"));
					Image scaledImage = image.getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT);
					g.drawImage(scaledImage, 0 ,0,null);
				}
			}catch(IOException ex){};
		}
		//Unused implemented method
		public void mouseClicked(MouseEvent e) {}
		//Implemented method used to drag and set the marked value to true
		//as long as it is not a start or finish cell when the cell is entered
		//by the mouse
		public void mouseEntered(MouseEvent e) {
			if(isPressed() && !isStart() && !isFinish()){
				marked = true;
				repaint();
			}
		}
		//Unused implemented method
		public void mouseExited(MouseEvent e) {}
		//Implemented method used to change its pressed status to false
		//when the mouse is released
		public void mouseReleased(MouseEvent e) {
			setPressed(false);
		}
		//Implemented method used to set the cell as either marked or unmarked
		//as long as it is not a start or finish cell when clicked upon
		public void mousePressed(MouseEvent e) {
			if(!isStart() && !isFinish()){
				setPressed(true);
				marked = !marked;		
				repaint();
			}
		}
	}
}