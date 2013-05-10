package maze;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RandomMaze extends JFrame {

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


	public RandomMaze() {
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
				//				board[row][col] = new Cell();
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
					//resetPath();
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
				revalidate();
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







	// recursive method to find a path from start to finish
	public void findPath() {
		if (findPath(0, 0)) {
			jlblStatus.setText("path found");
			pathFound = true;
		}
		else {
			jlblStatus.setText("No path exists");
		}
	}

	public boolean findPath(int row, int col) {
		board[row][col].visit();

		if ((col == colN-1) && (row == rowN-1)) {
			board[row][col].selectCell();
			//repaint();				//added 5/8 AA
			return true;
		}
		if ((row < rowN-1) && !board[row + 1][col].marked() &&
				!board[row + 1][col].blocked() && !board[row + 1][col].visited()) {
			block(row, col);

			if (findPath(row + 1, col)) {
				board[row][col].selectCell();
				//repaint();				//added 5/8 AA
				return true;
			}

			unblock(row, col);
		}
		if ((col < colN-1) && !board[row][col + 1].marked() &&
				!board[row][col + 1].blocked() && !board[row][col + 1].visited()) {
			block(row,col);
			if (findPath(row, col + 1)) {
				board[row][col].selectCell();
				//repaint();				//added 5/8 AA
				return true;
			}

			unblock(row,col);
		}
		if ((row > 0) && !board[row - 1][col].marked() &&
				!board[row - 1][col].blocked() && !board[row - 1][col].visited()) {
			block(row, col);

			if (findPath(row - 1, col)) {
				board[row][col].selectCell();
				//repaint();				//added 5/8 AA
				return true;
			}

			unblock(row, col);
		}
		if ((col > 0) && !board[row][col - 1].marked() &&
				!board[row][col - 1].blocked() && !board[row][col - 1].visited()) {
			block(row,col);
			if (findPath(row, col - 1)) {
				board[row][col].selectCell();
				//repaint();				//added 5/8 AA
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
	}



	// method to only clear the path
	public void clearPath() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].deselectCell();
			}
		}
		//repaint();	//added 5/8 AA
	}



	// clearAll method to clear the entire maze board
	public void clearAll() {								// added 5/8 JB
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].resetCell();
			}
		}
	}



	// getter method to determine if the mouse is pressed
	public boolean isPressed(){			// added 5/8 JB
		return pressed;
	}

	// setter method to set the mouse pressed variable
	public void setPressed(boolean e){		// added 5/8 JB
		pressed = e;
	}




	// method to add start and finish graphics to the board
	public void setMarkers(){
		board[0][0].setStart();
		board[rowN-1][colN-1].setFinish();
	}



	// method to reset the path
	protected void resetPath() {
		pathFound = false;
		//		repaint();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col].unblock();
				board[row][col].deselectCell();
				unblock(row, col);
			}
		}
		revalidate();
		repaint();
	}
	public Cell[][] createRandomMaze(Cell[][] nBoard){
		for (int row = 0; row < nBoard.length; row++) {
			for (int col = 0; col < nBoard[row].length; col++) {
				nBoard[row][col] = new Cell();
				if((row > 0 && col > 0)){
					int randSeed = (int)(Math.random() * 100 + 1);
					if(randSeed > 50)
						nBoard[row][col].setMarked();
				}
			}
		}
		MazeTester newMaze = new MazeTester(nBoard, rowN, colN );
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







	// Inner class
	class Cell extends JPanel implements MouseListener {
		//		private BufferedImage image;
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
		public void selectCell() {
			setBackground(Color.red);
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
		public void mouseClicked(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
			if(isPressed() && !isStart() && !isFinish()){
				marked = !marked;		//added 5/8 AA
				repaint();
			}

		}
		public void mouseExited(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
			setPressed(false);
		}
		public void mousePressed(MouseEvent e) {
			if(!isStart() && !isFinish()){
				setPressed(true);
				marked = !marked;		
				repaint();
			}
		}
	}
}