package maze;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;



// Maze class
public class Maze extends JFrame {
	private int rowN = MainMenu.getBoardSize();
	private int colN = MainMenu.getBoardSize();
	private Cell[][] board = new Cell[rowN][colN];
	private JButton jbtFindPath = new JButton("Find Path");
	private JButton jbtClearPath = new JButton("Clear Path");
	private JButton jbtClearMaze = new JButton("Clear Maze");
	private JButton jbtClose = new JButton("Close");
	private JPanel jpBoard, jpButton;
	private JLabel jlblStatus = new JLabel("Started..");
	private boolean pressed = false;
	private static MainMenu mainFrame;
	public Maze(){
		setTitle("Build Your Own Maze");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // Center the frame
		setVisible(true);
		jpBoard = new JPanel();
		jpBoard.setLayout(new GridLayout(rowN, colN));

		jpButton = new JPanel();
		jpButton.setLayout(new FlowLayout());
		jpButton.add(jbtFindPath);
		jpButton.add(jbtClearPath);
		jpButton.add(jbtClearMaze);
		jpButton.add(jbtClose);


		// Add cells to jpBoard
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col] = new Cell();
				jpBoard.add(board[row][col]);
			}
		}

		// Add jpBoard, jpButtons and jlblStatus to the applet
		setLayout(new BorderLayout());
		add(jlblStatus, BorderLayout.NORTH);
		add(jpBoard, BorderLayout.CENTER);
		add(jpButton, BorderLayout.SOUTH);

		// Register listeners
		jbtFindPath.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearPath();
				findPath();
			}
		});
		jbtClearPath.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearPath();
				setMarkers();
			}
		});
		jbtClearMaze.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearAll();
				setMarkers();
			}
		});
		jbtClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				closeWindow();

			}
		});
		setMarkers();
	}




	// Main method
	public static void main(String[] args) {
		// Create a frame
		Maze frame = new Maze();
	}

	//Method used to set the parent frame for easy dismissal
	public void setParent(MainMenu parentFrame){
		mainFrame = parentFrame;
	}







	// recursive method to find a path from start to finish
	public void findPath() {
		if (findPath(0, 0)) {
			jlblStatus.setText("path found");
		}
		else {
			jlblStatus.setText("No path exists");
		}
	}

	public boolean findPath(int row, int col) {
		board[row][col].visit();

		if ((col == colN-1) && (row == rowN-1)) {
			board[row][col].selectCell();
			return true;
		}
		if ((row < rowN-1) && !board[row + 1][col].marked() &&
				!board[row + 1][col].blocked() && !board[row + 1][col].visited()) {
			block(row, col);

			if (findPath(row + 1, col)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row, col);
		}
		if ((col < colN-1) && !board[row][col + 1].marked() &&
				!board[row][col + 1].blocked() && !board[row][col + 1].visited()) {
			block(row,col);
			if (findPath(row, col + 1)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row,col);
		}
		if ((row > 0) && !board[row - 1][col].marked() &&
				!board[row - 1][col].blocked() && !board[row - 1][col].visited()) {
			block(row, col);

			if (findPath(row - 1, col)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row, col);
		}
		if ((col > 0) && !board[row][col - 1].marked() &&
				!board[row][col - 1].blocked() && !board[row][col - 1].visited()) {
			block(row,col);
			if (findPath(row, col - 1)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row,col);
		}
		return false;

	}


	// Temporary block the neighbor to prevent neighboring path
	public void block(int row, int col) {
		if (row > 0) {
			board[row - 1][col].block();
		}

		if (row < rowN-1) {
			board[row + 1][col].block();
		}

		if (col > 0) {
			board[row][col - 1].block();
		}

		if (col < colN-1) {
			board[row][col + 1].block();
		}
	}




	// Remove the temporary block
	public void unblock(int row, int col) {
		if (row > 0) {
			board[row - 1][col].unblock();
		}

		if (row < rowN-1) {
			board[row + 1][col].unblock();
		}

		if (col > 0) {
			board[row][col - 1].unblock();
		}

		if (col < colN-1) {
			board[row][col + 1].unblock();
		}
	}




	// method for closing the window
	public void closeWindow(){

		WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
		mainFrame.setEnabled(true);	//Added 5/10 AA
	}
	// method to only clear the path
	public void clearPath() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].deselectCell();
			}
		}
	}
	
	// clearAll method to clear the entire maze board
	public void clearAll() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].resetCell();
			}
		}
	}

	// getter method to determine if the mouse is pressed
	public boolean isPressed(){
		return pressed;
	}

	// setter method to set the mouse pressed variable
	public void setPressed(boolean e){
		pressed = e;
	}

	// method to add start and finish graphics to the board
	public void setMarkers(){
		board[0][0].setStart();
		board[rowN-1][colN-1].setFinish();
	}







	// Inner class
	class Cell extends JPanel implements MouseListener {
		private boolean marked = false;
		private boolean visited = false;
		private boolean blocked = false;
		private boolean start = false;
		private boolean finish = false;
		public Cell() {
			setBackground(Color.DARK_GRAY);
			addMouseListener(this);
		}
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

		public void selectCell() {
			setBackground(Color.RED);
			repaint();
		}

		public void deselectCell() {
			if(visited){
				setBackground(Color.DARK_GRAY);
			}
			visited = false;
			blocked = false;
			repaint();
		}

		public void resetCell(){
			setBackground(Color.DARK_GRAY);
			marked = false;
			visited = false;
			blocked = false;
			repaint();

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			try{
				if(isStart()){
					BufferedImage image = ImageIO.read(new File("StartIcon.jpg"));
					Image scaledImage = image.getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT);
					g.drawImage(scaledImage, 0 ,0,null);
				}
				if(isFinish()){
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

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {		// added 5/8 JB
			//			if(isPressed() && !isStart() && !isFinish()){
			//				marked = !marked;		//added 5/8 AA
			//				repaint();

			/**
			 * try it out with this code, if you click on an empty cell
			 * and drag it will fill the cells with the brick image and
			 * it will not remove bricks if you go over them, but if you 
			 * click on a cell with a brick it will remove it.
			 * 
			 * if you don't like it we can go back to the code above
			 * 
			 */
			if(isPressed() && !isStart() && !isFinish()){
				marked = true;
				repaint();
			}
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			setPressed(false);
		}

		public void mousePressed(MouseEvent e) {		// added 5/8 JB
			if(!isStart() && !isFinish()){
				setPressed(true);
				marked = !marked;		
				repaint();
			}
		}
	}
}
