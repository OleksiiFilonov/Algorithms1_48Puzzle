import static java.lang.Math.abs;

import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public final class Board {

	private final int[][] blocks;
	private final int dimension;
	private int emptyRow;
	private int emptyColumn;

	// construct a board from an n-by-n array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] inputBlocks) {
		if (inputBlocks == null)
			throw new NullPointerException("blocks can't be null");
		this.dimension = inputBlocks.length;
		this.blocks = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				this.blocks[i][j] = inputBlocks[i][j];
				if (isEmptyBlock(i, j)) {
					emptyRow = i;
					emptyColumn = j;
				}
			}
		}
	}

	// board dimension n
	public int dimension() {
		return dimension;
	}

	// number of blocks out of place
	public int hamming() {
		int priority = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (!isEmptyBlock(i, j) && !isBlockInPlace(i, j)) {
					priority++;
				}
			}
		}
		return priority;
	}

	private boolean isBlockInPlace(int i, int j) {
		return blocks[i][j] == i * dimension + j + 1;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int priority = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (!isEmptyBlock(i, j) && !isBlockInPlace(i, j)) {
					int value = blocks[i][j];
					int expectedRow = (value - 1) / dimension;
					int expectedColumn = value % dimension - 1;
					if (expectedColumn == -1)
						expectedColumn = dimension - 1;
					priority += abs(i - expectedRow) + abs(j - expectedColumn);
				}
			}
		}
		return priority;
	}

	private boolean isEmptyBlock(int i, int j) {
		return blocks[i][j] == 0;
	}

	private boolean isNotEmptyBlock(int[][] blocks, int i, int j) {
		return blocks[i][j] != 0;
	}

	// is this board the goal board?
	public boolean isGoal() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension - 1; j++) {
				if (!isBlockInPlace(i, j))
					return false;
			}
		}
		for (int j = 0; j < dimension - 1; j++) {
			if (!isBlockInPlace(dimension - 1, j))
				return false;
		}
		return true;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		Board twin = new Board(this.blocks);
		boolean exchange = false;
		while (!exchange) {
			int i = StdRandom.uniform(dimension);
			int j = StdRandom.uniform(dimension);
			int ej = j;
			if (j == dimension - 1) {
				--ej;
			} else {
				++ej;
			}
			int[][] twinBlocks = twin.blocks;
			if (isNotEmptyBlock(twinBlocks, i, j) && isNotEmptyBlock(twinBlocks, i, ej)) {
				twin.swap(i, j, i, ej);
				exchange = true;
			}
		}
		return twin;
	}

	private void swap(int i, int j, int ei, int ej) {
		int temp = this.blocks[i][j];
		this.blocks[i][j] = this.blocks[ei][ej];
		this.blocks[ei][ej] = temp;
	}

	@Override
	// does this board equal y?
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		if (other.getClass() != this.getClass())
			return false;
		Board that = (Board) other;
		return Arrays.deepEquals(this.blocks, that.blocks);
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {

		Queue<Board> neighboards = new Queue<>();
		// shift empty block to left
		if (emptyColumn > 0) {
			Board leftBoard = new Board(blocks);
			leftBoard.swap(emptyRow, emptyColumn - 1, emptyRow, emptyColumn);
			neighboards.enqueue(leftBoard);
		}
		// shift empty block to top
		if (emptyRow > 0) {
			Board topBoard = new Board(blocks);
			topBoard.swap(emptyRow - 1, emptyColumn, emptyRow, emptyColumn);
			neighboards.enqueue(topBoard);
		}
		// shift empty block to right
		if (emptyColumn < dimension - 1) {
			Board rightBoard = new Board(blocks);
			rightBoard.swap(emptyRow, emptyColumn + 1, emptyRow, emptyColumn);
			neighboards.enqueue(rightBoard);
		}
		// shift empty block to bottom
		if (emptyRow < dimension - 1) {
			Board bottomBoard = new Board(blocks);
			bottomBoard.swap(emptyRow + 1, emptyColumn, emptyRow, emptyColumn);
			neighboards.enqueue(bottomBoard);
		}
		return neighboards;
	}

	@Override
	// string representation of this board (in the output format specified
	// below)
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(dimension + "\n");
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				s.append(String.format("%2d ", blocks[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	// unit tests (not graded)
	public static void main(String[] args) {
		int[][] test = new int[][] { { 1, 2 }, { 3, 4 }, { 15, 16 } };
		int[][] testCopy = Arrays.copyOf(test, test.length);
		for (int i = 0; i < testCopy.length; i++) {
			for (int j = 0; j < testCopy[i].length; j++) {
				System.out.printf("%3d", testCopy[i][j]);
			}
			System.out.println();
		}
		System.out.println(2 % 3);
	}
}