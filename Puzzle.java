
public class Puzzle {
	String[][] puzzle;
	String[][] solution;
	
	
	Puzzle (String[][] puzzle, String[][] solution) {
		this.puzzle = puzzle;
		this.solution = solution;
	}
	
	public String[][] getSolution() {
		return solution;
	}
	
	public String[][] getPuzzle() {
		return puzzle;
	}
}
