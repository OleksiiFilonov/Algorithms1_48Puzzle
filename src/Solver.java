import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {

	private final SearchNode goalNode;

	private final class SearchNode implements Comparable<SearchNode> {

		private final SearchNode parentSearchNode;
		private final int numberOfMoves;
		private final Board board;

		public SearchNode(SearchNode parentSearchNode, int numberOfMoves, Board board) {
			this.parentSearchNode = parentSearchNode;
			this.numberOfMoves = numberOfMoves;
			this.board = board;
		}

		private int priority() {
			return board.manhattan() + numberOfMoves;
		}

		@Override
		public int compareTo(SearchNode otherSearchNode) {
			if (this.priority() > otherSearchNode.priority())
				return 1;
			if (this.priority() < otherSearchNode.priority())
				return -1;
			return 0;
		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(final Board initial) {
		if (initial == null)
			throw new NullPointerException("Board can't be null");
		final MinPQ<SearchNode> solution = new MinPQ<>();
		final MinPQ<SearchNode> twinSolution = new MinPQ<>();
		solution.insert(new SearchNode(null, 0, initial));
		twinSolution.insert(new SearchNode(null, 0, initial.twin()));
		SearchNode solutionNode = performSearchStep(solution);
		SearchNode twinSolutionNode = performSearchStep(twinSolution);
		while (!solutionNode.board.isGoal() || !twinSolutionNode.board.isGoal()) {
			solutionNode = performSearchStep(solution);
			twinSolutionNode = performSearchStep(twinSolution);
		}
		if (solutionNode.board.isGoal()) {
			goalNode = solutionNode;
		} else {
			goalNode = null;
		}

	}

	private SearchNode performSearchStep(final MinPQ<SearchNode> searchQueue) {
		final SearchNode parentNode = searchQueue.delMin();
		for (Board neighboard : parentNode.board.neighbors()) {
			if (!neighboard.equals(parentNode.board)) {
				searchQueue.insert(new SearchNode(parentNode, parentNode.numberOfMoves + 1, neighboard));
			}
		}
		return parentNode;
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return goalNode != null;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (isSolvable()) {
			return goalNode.numberOfMoves;
		} else {
			return -1;
		}
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if (isSolvable()) {
			Queue<Board> solution = new Queue<>();
			SearchNode currentNode = goalNode;
			while (currentNode != null) {
				solution.enqueue(currentNode.board);
				currentNode = currentNode.parentSearchNode;
			}
			return solution;
		} else {
			return null;
		}
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
