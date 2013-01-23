import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


public class FifteenPuzzle {
	
	public static final int NUM_ROWS = 4;
	public static final int NUM_COLS = 4;
	
	public static final int[][] SOLUTION = {
		{1, 2, 3, 4},
		{5, 6, 7, 8},
		{9, 10, 11, 12},
		{13, 14, 15, 0}
	};
	
	private static final int[][] INCREMENTS = {
		{0, -1},
		{0, 1},
		{-1, 0},
		{1, 0}
	};
	
	private static final Map<Integer, Pair<Integer, Integer>> SOLUTION_COORD_LOOKUP = 
			new HashMap<Integer, Pair<Integer, Integer>>();
	
	static {
		for (int i = 0; i < SOLUTION.length; i++) {
			for (int j = 0; j < SOLUTION[i].length; j++) {
				SOLUTION_COORD_LOOKUP.put(SOLUTION[i][j], Pair.of(i, j));
			}
		}
		
	}
	
	/**
	 * Returns a pseudo hashcode for a baord, using |
	 * @param board
	 * @return
	 */
	private static int arrayHashCode(int[][] board) {
		int ret = 0;
		for (int i = 0; i < board.length; i++) {
			ret += i * Arrays.hashCode(board[i]);
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				ret += 1337 * i + j;
			}
		}
		return ret;
	}
	
	static class GameState implements Comparable<GameState> {
		public int[][] board;
		public int emptyI;
		public int emptyJ;
		public int depth;
		
		public GameState(int[][] board, int emptyI, int emptyJ, int depth) {
			this.board = board;
			this.emptyI = emptyI;
			this.emptyJ = emptyJ;
			this.depth = depth;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof GameState)) {
				return false;
			}
			GameState other = (GameState) o;
			return arrayEquals(this.board, other.board) &&
				this.emptyI == other.emptyI &&
				this.emptyJ == other.emptyJ &&
				this.depth == other.depth;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					sb.append(board[i][j]);
					sb.append(" ");
				}
				sb.append("\n");
			}
			return sb.toString();
		}

		protected int h() {
			// take the sum of the manhattan distances
			int sum = 0;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					Pair<Integer, Integer> p = SOLUTION_COORD_LOOKUP.get(board[i][j]);
					sum += Math.abs(p.first - i) + Math.abs(p.second - j);
				}
			}
			return sum;
		}
		
		protected int f() {
			return depth + h();
		}
		
		@Override
		public int compareTo(GameState o) {
			return f() - o.f();
		}
	}
	
	private static int[][] copy(int[][] oldArray) {
		int[][] newArray = new int[oldArray.length][oldArray[0].length];
		for (int i = 0; i < oldArray.length; i++) {
			newArray[i] = Arrays.copyOf(oldArray[i], oldArray[i].length);
		}
		return newArray;
	}
	
	private static boolean inBounds(int i, int j) {
		return i >= 0 && i < NUM_ROWS && j >= 0 && j < NUM_COLS;
	}
	
	public static int[][] getNextBoard(int[][] oldBoard, int oldI, int oldJ, int newI, int newJ) {
		int[][] newBoard = copy(oldBoard);
		newBoard[oldI][oldJ] = oldBoard[newI][newJ];
		newBoard[newI][newJ] = oldBoard[oldI][oldJ];
		return newBoard;
	}
	
	private static List<GameState> getNextStates(GameState current) {
		List<GameState> nextStates = new ArrayList<GameState>(4);
		int i = current.emptyI;
		int j = current.emptyJ;
		int[][] board = current.board;
		int depth = current.depth;
		
		for (int[] increment : INCREMENTS) {
			int newI = i + increment[0];
			int newJ = j + increment[1];
			if (inBounds(newI, newJ)) {
				GameState newState = new GameState(getNextBoard(board, i, j, newI, newJ), newI, newJ, depth + 1);
				nextStates.add(newState);
			}
		}
		return nextStates;
	}
	
	private static boolean arrayEquals(int[][] a, int[][] b) {
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i].length != b[i].length) {
				return false;
			}
			if (!Arrays.equals(a[i], b[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * returns depth of solution, -1 if none found.
	 * uses A* search.
	 * @return
	 */
	private static int computeSolution(int[][] originalBoard, int startI, int startJ) {
		PriorityQueue<GameState> states = new PriorityQueue<GameState>();
		Set<Integer> seenBoard = new HashSet<Integer>();
		states.offer(new GameState(originalBoard, startI, startJ, 0));
		
		while (!states.isEmpty()) {
			GameState state = states.poll();
			if (arrayEquals(state.board, SOLUTION)) {
				return state.depth;
			} else if (!seenBoard.contains(arrayHashCode(state.board))) {
				List<GameState> nextStates = getNextStates(state);
				for (GameState nextState : nextStates) {
					states.offer(nextState);
				}
				seenBoard.add(arrayHashCode(state.board));
			}
		}
		return -1;
	}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int[][] original = {
			{14, 1, 5, 4},
			{2, 7, 6, 8},
			{9, 10, 15, 12},
			{13, 3, 11, 0}
		};
		
		final int[][] original2 = {
			{1, 2, 3, 4},
			{5, 6, 7, 8},
			{9, 10, 15, 11},
			{13, 14, 0, 12}
		};
		
		final int i = 3, j = 3;
		final int i2 = 3, j2 = 2;
		System.out.println(computeSolution(original, i, j));
		System.out.println(computeSolution(original2, i2, j2));
		
	}

}
