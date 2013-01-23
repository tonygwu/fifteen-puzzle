import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class FifteenPuzzleTest {

	final int[][] oldBoard = {
			{1, 2, 3, 4},
			{5, 6, 7, 8},
			{9, 10, 11, 12},
			{13, 14, 0, 15}
	};
	
	final int[][] newBoard = {
			{1, 2, 3, 4},
			{5, 6, 7, 8},
			{9, 10, 11, 12},
			{13, 14, 15, 0}	
	};
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testGetNextBoard() {


		assertArrayEquals(newBoard, FifteenPuzzle.getNextBoard(oldBoard, 3, 2, 3, 3));
	}
	
	@Test
	public final void testGetNextStates() {
		
	}

}
