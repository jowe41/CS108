// Board.java
package edu.stanford.cs108.tetris;

//import javax.management.RuntimeErrorException;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int maxHeight;
	private int xMaxHeight;
	private boolean[][] grid;
	private boolean[][] backup;
	private boolean DEBUG = true;
	private int[] rowWidth;
	private int[] xWidth;
	private int[] columnHeight;
	private int[] xHeight;
	boolean committed;


	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		backup = new boolean[width][height];
		rowWidth = new int[height];
		xWidth = new int[height];
		columnHeight = new int[width];
		xHeight = new int[width];
		committed = true;
		maxHeight = 0;
		xMaxHeight = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j ++) {
				grid[i][j] = false;
			}
		}

		for (int i = 0; i < height; i++) {
			rowWidth[i] = 0;
		}
		for (int i = 0; i < width; i++) {
			columnHeight[i] = 0;
		}
		// YOUR CODE HERE
	}


	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return maxHeight; // YOUR CODE HERE
	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			for (int x = 0; x < width; x++) {
				if (columnHeight[x] > height || columnHeight[x] > maxHeight) {
					throw new RuntimeException("The height of column "+ x +" is not correct.");
				}
			}

			for (int y = 0; y < height; y++) {
				if (rowWidth[y] > width) {
					throw new RuntimeException("The width of row "+ y +" is not correct");
				}
			}
			System.out.println(this);
			// YOUR CODE HERE
		}
	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int stayRow = 0;
		int[] skirt = piece.getSkirt();
		for (int i = 0; i < skirt.length; i++) {
			stayRow = getColumnHeight(i + x) - skirt[i] > stayRow ? getColumnHeight(i + x) - skirt[i] : stayRow;
		}
		return stayRow; // YOUR CODE HERE
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		if (x < 0 || x > width - 1) {
			return -1;
		}
		return columnHeight[x]; // YOUR CODE HERE
	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		if (y < 0 || y > height - 1) {
			return -1;
		}
		return rowWidth[y]; // YOUR CODE HERE
	}


	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			return true;
		}
		return grid[x][y]; // YOUR CODE HERE
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		int result = PLACE_OK;
		backup();
		TPoint[] points = piece.getBody();
		if (0 > x || x + piece.getWidth() > width  || 0 > y || y + piece.getHeight() > height ) {
			committed = false;
			return PLACE_OUT_BOUNDS;
		}

		for (TPoint point:points) {
			if (grid[x + point.x][y + point.y] == true) {
				committed = false;
				return  PLACE_BAD;
			}

			grid[x + point.x][y + point.y] = true;
		}
		updateRowWidth(y, y + piece.getHeight());
		updateColumnHeight(x, x + piece.getWidth());
		maxHeight = y + piece.getHeight() > maxHeight? y + piece.getHeight():maxHeight;

		if (result == PLACE_OK) {
			for (int i = y; i < y + piece.getHeight(); i++) {
				if (rowWidth[i] == width) {
					result = PLACE_ROW_FILLED;
				}
			}
		}
		// YOUR CODE HERE
		committed = false;
		sanityCheck();
		return result;
	}

	private void backup() {
		for (int i = 0; i < width; i ++){
			System.arraycopy(grid[i], 0, backup[i], 0, grid[i].length);
		}
		xMaxHeight = maxHeight;
		System.arraycopy(rowWidth, 0, xWidth, 0, rowWidth.length);
		System.arraycopy(columnHeight, 0, xHeight, 0, columnHeight.length);
	}


	private void updateColumnHeight(int left, int right) {
		for (int x = left; x < right; x++) {
			int newColumnHeight = 0;
			for (int y = height - 1; y > -1 ; y--) {
				if (grid[x][y] == true) {
					newColumnHeight = y + 1;
					break;
				}
			}
			columnHeight[x] = newColumnHeight;
		}

		// TODO Auto-generated method stub

	}


	private void updateRowWidth(int bot, int top) {
		for (int y = bot; y < top; y++) {
			int result = 0;
			for (int i = 0; i < width; i++) {
				if (grid[i][y] == true) {
					result++;
				}
			}
			rowWidth[y] = result;
		}
		// TODO Auto-generated method stub
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed){
			committed = false;
			backup();
		}
		int rowsCleared = 0;
		for (int i = 0; i < maxHeight; i++) {
			if (rowWidth[i] == width) {
				rowsCleared++;
				for (int y = i; y < maxHeight - 1; y++) {
					for (int x = 0; x < columnHeight.length; x++) {
						grid[x][y] = grid[x][y + 1];
					}
					rowWidth[y] = rowWidth[y + 1];
				}
				for (int x = 0; x < columnHeight.length; x++) {
					grid[x][maxHeight - 1] = false;
				}
				rowWidth[maxHeight - 1] = 0;
				i--;
				maxHeight--;
			}
		}
		updateColumnHeight(0, width);
		// YOUR CODE HERE
		sanityCheck();
		return rowsCleared;
	}




	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		for (int i = 0; i < width; i ++){
			System.arraycopy(backup[i], 0, grid[i], 0, grid[i].length);
		}
		maxHeight = xMaxHeight;
		System.arraycopy(xWidth, 0, rowWidth, 0, rowWidth.length);
		System.arraycopy(xHeight, 0, columnHeight, 0, columnHeight.length);

		commit();
		sanityCheck();
		// YOUR CODE HERE
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}



	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


