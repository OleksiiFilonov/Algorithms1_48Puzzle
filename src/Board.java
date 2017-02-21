import java.util.Arrays;

public final class Board {

	private final int[][] blocks;

	private final int dimension;

	// construct a board from an n-by-n array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		if (blocks == null)
			throw new NullPointerException("blocks can't be null");
		this.blocks = Arrays.copyOf(blocks, blocks.length);
		this.dimension = blocks.length;
	}

	// board dimension n
	public int dimension() {
		return dimension;
	}

	// number of blocks out of place
	public int hamming() {
		int counter = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (blocks[i][j] != 0 && isBlockInPlace(i, j)) {
					counter++;
				}
			}
		}
		return counter;
	}

	private boolean isBlockInPlace(int i, int j) {
		return blocks[i][j] == i * dimension + j + 1;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		return -1;
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
			if (isBlockInPlace(dimension - 1, j))
				return false;
		}
		return true;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		return null;
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
		return (this.dimension == that.dimension) && (Arrays.equals(this.blocks, that.blocks));
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		return null;
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
		int[][] test = new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } };
		int[][] testCopy = Arrays.copyOf(test, test.length);
		for (int i = 0; i < testCopy.length; i++) {
			for (int j = 0; j < testCopy[i].length; j++) {
				System.out.printf("%3d", testCopy[i][j]);
			}
			System.out.println();
		}
	}
}