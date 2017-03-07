import org.junit.Assert;
import org.junit.Test;

public class BoardTest {

	@Test
	public void equalBoards() {
		int[][] initialBlocks = new int[][] { { 3, 0 }, { 2, 1 } };
		Board firstBoard = new Board(initialBlocks);
		Board secondBoard = new Board(initialBlocks);
		Assert.assertEquals(firstBoard, secondBoard);
	}
}