import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {

    private final SearchNode goalNode;
    private final boolean solvable;
    private int numberSolutionMoves = -1;

    private final class SearchNode implements Comparable<SearchNode> {

        private final SearchNode parentSearchNode;
        private final int numberOfMoves;
        private final Board board;
        private final int priority;

        public SearchNode(SearchNode parentSearchNode, Board board) {
            this.parentSearchNode = parentSearchNode;
            if (parentSearchNode == null) {
                this.numberOfMoves = 0;
            } else {
                this.numberOfMoves = parentSearchNode.numberOfMoves + 1;
            }
            this.board = board;
            this.priority = board.manhattan() + numberOfMoves;
        }

        @Override
        public int compareTo(SearchNode otherSearchNode) {
            if (this.priority > otherSearchNode.priority)
                return 1;
            if (this.priority < otherSearchNode.priority)
                return -1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(final Board initial) {
        if (initial == null)
            throw new NullPointerException("Board can't be null");
        final MinPQ<SearchNode> solutionPQ = new MinPQ<>();
        final MinPQ<SearchNode> twinPQ = new MinPQ<>();
        solutionPQ.insert(new SearchNode(null, initial));
        twinPQ.insert(new SearchNode(null, initial.twin()));
        SearchNode solutionNode = performSearchStep(solutionPQ);
        SearchNode twinSolutionNode = performSearchStep(twinPQ);
        while (!solutionNode.board.isGoal() && !twinSolutionNode.board.isGoal()) {
            solutionNode = performSearchStep(solutionPQ);
            twinSolutionNode = performSearchStep(twinPQ);
        }
        if (solutionNode.board.isGoal()) {
            goalNode = solutionNode;
            solvable = true;
            numberSolutionMoves = goalNode.numberOfMoves;
        } else {
            goalNode = null;
            solvable = false;
        }

    }

    private SearchNode performSearchStep(final MinPQ<SearchNode> searchQueue) {
        final SearchNode currentSearchNode = searchQueue.delMin();
        for (Board neighboard : currentSearchNode.board.neighbors()) {
            if ((currentSearchNode.parentSearchNode == null)
                    || !neighboard.equals(currentSearchNode.parentSearchNode.board)) {
                searchQueue.insert(new SearchNode(currentSearchNode, neighboard));
            }
        }
        return currentSearchNode;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return numberSolutionMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Stack<Board> solution = new Stack<>();
            SearchNode currentNode = goalNode;
            while (currentNode != null) {
                solution.push(currentNode.board);
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
