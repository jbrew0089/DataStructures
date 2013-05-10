package maze;
import maze.Maze;
import maze.RandomMaze.Cell;

public class MazeTester {
	private int colN;
	private int rowN;
	private Cell[][] board;
	public MazeTester(Cell[][] nBoard, int row, int col){
		board = nBoard;
		colN = col;
		rowN = row;
	}
	public boolean isTraversable(){
		return testPath(0,0);
	}
	public boolean testPath(int row, int col) {
		board[row][col].visit();

		if ((col == colN-1) && (row == rowN-1)) {
			board[row][col].selectCell();
			return true;
		}
		if ((row < rowN-1) && !board[row + 1][col].marked() &&
				!board[row + 1][col].blocked() && !board[row + 1][col].visited()) {
			block(row, col);

			if (testPath(row + 1, col)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row, col);
		}
		if ((col < colN-1) && !board[row][col + 1].marked() &&
				!board[row][col + 1].blocked() && !board[row][col + 1].visited()) {
			block(row,col);
			if (testPath(row, col + 1)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row,col);
		}
		if ((row > 0) && !board[row - 1][col].marked() &&
				!board[row - 1][col].blocked() && !board[row - 1][col].visited()) {
			block(row, col);

			if (testPath(row - 1, col)) {
				board[row][col].selectCell();
				return true;
			}

			unblock(row, col);
		}
		if ((col > 0) && !board[row][col - 1].marked() &&
				!board[row][col - 1].blocked() && !board[row][col - 1].visited()) {
			block(row,col);
			if (testPath(row, col - 1)) {
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
}
