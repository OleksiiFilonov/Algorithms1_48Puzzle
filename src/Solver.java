
public final class Solver {

	private final Board board;

	// find a solution to the initial board (using the A* algorithm)
	public Solver(final Board initial) {
		if (initial == null)
			throw new NullPointerException("Board can't be null");
		this.board = initial;
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return false;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		return -1;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		return null;
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
	}
}
