package maze;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;



/**
 * 
 * Maze
 *
 */
public class Maze extends JFrame {
	private int rowN = 10;
	private int colN = 10;
	private Cell[][] board = new Cell[rowN][colN];
	private JButton jbtFindPath = new JButton("Find Path");
	private JButton jbtClearPath = new JButton("Clear Path");
	private JButton jbtClearMaze = new JButton("Clear Maze");		// added 5/8 JB
	private JButton jbtClose = new JButton("Close");				// added 5/8 JB
	private JPanel jpBoard, jpButton;
	private JLabel jlblStatus = new JLabel();
	private boolean pressed = false;				// added 5/8 JB




	// Main method
	public static void main(String[] args) {
		// Create a frame
		//JFrame frame = new JFrame("Maze");

		// Create an
		Maze frame = new Maze();

		// Invoke init()
		frame.init();
		// Display the frame
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center the frame
		frame.setVisible(true);
	}




	// initialize the GUI
	public void init() {
		setSize(500,500);
		jpBoard = new JPanel();
		jpBoard.setLayout(new GridLayout(rowN, colN));

		jpButton = new JPanel();
		jpButton.setLayout(new FlowLayout());
		jpButton.add(jbtFindPath);
		jpButton.add(jbtClearPath);
		jpButton.add(jbtClearMaze);			// added 5/8 JB
		jpButton.add(jbtClose);				// added 5/8 JB

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
		jbtFindPath.addActionListener(new Listener());
		jbtClearPath.addActionListener(new Listener());
		jbtClearMaze.addActionListener(new Listener());		// added 5/8
		jbtClose.addActionListener(new Listener());			// added 5/8
		
		setMarkers();
	}

	

	class Listener implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent e) {
			String arg = e.getActionCommand();
			if (e.getSource() instanceof JButton) {
				if ("Find Path".equals(arg)) {
					clearPath();
					findPath();
				}
				else if ("Clear Path".equals(arg)) {
					clearPath();
					setMarkers();
				}
				else if ("Clear Maze".equals(arg)) {   	// added on 5/8
					clearAll();
					setMarkers();
				}
				else if ("Close".equals(arg)) {			// added on 5/8
					closeWindow();
				}
			}
		}
	}




	/** 
	 * recursive method to find the path
	 * 
	 * Adam, I believe you have the updated code to fix this
	 * so it no longer snakes across the frame
	 * 
	 * Jason
	 */
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


	/**
	 * Temporary block method
	 * 
	 * blocks the neighbor to prevent neighboring path.
	 * 
	 * possibly remove this method
	 * 
	 * @author Jason
	 */
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




	/**
	 * Method to remove the temporary block
	 * 
	 * 
	 */
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




	/**
	 * method to clear the path
	 * 
	 * Fixed, now it is working properly
	 * 
	 * Jason
	 * 
	 */
	public void clearPath() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].deselectCell();
			}
		}
	}


	/** 
	 * method for closing the window
	 * 
	 * Jason
	 */
	public void closeWindow(){

		WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
	}



	/**
	 * clearAll method to clear the entire maze
	 * 
	 * Jason
	 */

	public void clearAll() {								// added 5/8 JB
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].resetCell();
			}
		}
	}




	public boolean isPressed(){			// added 5/8 JB
		return pressed;
	}


	public void setPressed(boolean e){		// added 5/8 JB
		pressed = e;
	}

	public void setMarkers(){
		board[0][0].setBackground(Color.green);
		board[rowN-1][colN-1].setBackground(Color.orange);
	}


	/**
	 * Cell class
	 * 
	 * we could update this class to display graphics such as brick walls,
	 * a start and stop graphic for the starting and ending cells, maybe a
	 * character or some image that moves after each recursion...
	 * 
	 * Jason
	 *
	 */
	public // Inner class
	class Cell extends JPanel implements MouseListener {
		private BufferedImage image;
		private boolean marked = false;
		private boolean visited = false;
		private boolean blocked = false;

		public Cell() {
			setBackground(Color.DARK_GRAY);
			addMouseListener(this);
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

		public void resetCell(){				// added 5/8 JB
			setBackground(Color.DARK_GRAY);
			marked = false;
			visited = false;
			blocked = false;
			repaint();

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (marked) {
				try{
					image = ImageIO.read(new File("Brick.jpg"));
					Image scaledImage = image.getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT);
					//ImageIcon icon = new ImageIcon(scaledImage);
					g.drawImage(scaledImage, 0 ,0,null);
				}
				catch(IOException ex){};
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {		// added 5/8 JB
			if(isPressed()){
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
			setPressed(true);
			marked = !marked;
			repaint();
		}
	}
}

