import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.*;

public class Sudoku {

	public static int boardSize = 9;
	public static String[][] solvedBoard = new String[boardSize][boardSize];
	public static ArrayList<Puzzle> puzzles = new ArrayList<>();
	public static String[][] currentPuzzle = new String[boardSize][boardSize];
	public static String[][] currentSolution = new String[boardSize][boardSize];
	public static int boardNumber = 0;
	
	public static void main(String[] args) {
	
		drawUi();
		String[][] puzzle = {
				{"","7","","","2","","","4","6"},
				{"","6","","","","","8","9",""},
				{"2","","","8","","","7","1","5"},
				{"","8","4","","9","7","","",""},
				{"7","1","","","","","","5","9"},
				{"","","","1","3","","4","8",""},
				{"6","9","7","","","2","","","8"},
				{"","5","8","","","","","6",""},
				{"4","3","","","8","","","7",""}};
		
		String[][] solution = {
				{"8","7","5","9","2","1","3","4","6"},
				{"3","6","1","7","5","4","8","9","2"},
				{"2","4","9","8","6","3","7","1","5"},
				{"5","8","4","6","9","7","1","2","3"},
				{"7","1","3","2","4","8","6","5","9"},
				{"9","2","6","1","3","5","4","8","7"},
				{"6","9","7","4","1","2","5","3","8"},
				{"1","5","8","3","7","9","2","6","4"},
				{"4","3","2","5","8","6","9","7","1"}};
		
		String[][] puzzle2 = {
				{"4","","6","5","","2","8","","9"},
				{"","","","","4","","","3",""},
				{"","","","","","","","","5"},
				{"6","","","8","","","1","",""},
				{"5","","","","7","","","8",""},
				{"3","","2","9","","4","","6",""},
				{"","2","","6","","","","","1"},
				{"","","","","5","3","9","4",""},
				{"8","3","","","9","","","","2"}};
		
		String[][] solution2 = {
				{"4","7","6","5","3","2","8","1","9"},
				{"2","5","8","1","4","9","7","3","6"},
				{"1","9","3","7","6","8","4","2","5"},
				{"6","4","7","8","2","5","1","9","3"},
				{"5","1","9","3","7","6","2","8","4"},
				{"3","8","2","9","1","4","5","6","7"},
				{"9","2","4","6","8","7","3","5","1"},
				{"7","6","1","2","5","3","9","4","8"},
				{"8","3","5","4","9","1","6","7","2"}};
		
		
		addPuzzle(puzzle, solution);
		addPuzzle(puzzle2, solution2);
	}
	

	
	public static void drawUi() {
		
		JFrame mainFrame = new JFrame("Sudoku");
		
		//set up menu bar
		JMenuBar mainMenuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem rules = new JMenuItem("Rules");
		JMenuItem addPuzzle = new JMenuItem("Add new puzzle");
		JMenuItem howItWorks = new JMenuItem("How the app works");
		mainMenuBar.add(menu);
		menu.add(rules);
		menu.add(addPuzzle);
		menu.add(howItWorks);
		mainFrame.setJMenuBar(mainMenuBar);
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainFrame,"- Numbers 1-9 must by placed within each 3x3 grid.\n"
						+ "- Each row and column must contain numbers 1-9.\n"
						+ "- There is only one unique solution to each puzzle.\n"
						+ "- Etc");
			}
		});
		addPuzzle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				JOptionPane.showMessageDialog(mainFrame, "Can not add new puzzles at the moment");

			}
		});
		howItWorks.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainFrame, "Check: will check current input to a known solution.\n"
						+ "Reset: will rest entire board.\n"
						+ "New: will select a brand new puzzle.\n" 
						+ "Solve: will solve puzzle for you if a solution exists\n");
			}
		});
		
		// dimensions for spacing of grid and textfields
		int horizontalDistance = 25;
		int verticalDistance = 50;
		int textFieldSize = 30;
		
		// size of main window
		int frameWidth = 800;
		int frameHeight = 600;
		
		// get resolution to center window when app opens
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		
		// create the grid
		JTextField[][] squares = new JTextField[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				squares[i][j] = new JTextField();
				squares[i][j].setBounds(horizontalDistance, verticalDistance, textFieldSize, textFieldSize);
				squares[i][j].setFont(new Font("Arial", Font.BOLD, 18));
				mainFrame.add(squares[i][j]);
				horizontalDistance += 50;
			}
			verticalDistance += 50;
			horizontalDistance = 25;
		}
		

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(frameWidth,frameHeight);
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		mainFrame.setLocation((screenWidth / 2) - (frameWidth/2),(screenHeight / 2) - (frameHeight / 2));
		mainFrame.setVisible(true);
		
		
		JButton buttonCheck = new JButton("Check");
		buttonCheck.setBounds(530, 50, 80, 30);
		buttonCheck.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				String[][] numbersCheck = new String[boardSize][boardSize];
				for (int i = 0; i < numbersCheck.length; i++) {
					for (int j = 0; j < numbersCheck.length; j++) {
						numbersCheck[i][j] = squares[i][j].getText();
						
					}
				}
				
				boolean check = check(numbersCheck, currentSolution);
				if (check == false) {
					JOptionPane.showMessageDialog(mainFrame, "Incorrect");
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "Correct");
				}
			}
		});
		
		JButton buttonReset = new JButton("Reset");
		buttonReset.setBounds(530,100,80,30);
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(mainFrame, "This will reset the entire board. Continue?", "Confirm",JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					for (int i = 0; i < boardSize; i++) {
						for (int j = 0; j < boardSize; j++) {
							squares[i][j].setText(currentPuzzle[i][j]);
						}
					}
				}
				else if (confirm == JOptionPane.NO_OPTION) {
					return;
				}
			}
		});
		
		JButton buttonClear = new JButton("Clear");
		buttonClear.setBounds(530, 150, 80, 30);
		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int confirm = JOptionPane.showConfirmDialog(mainFrame, "This will clear the entire board. Continue?", "Confirm",JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					for (int i = 0; i < boardSize; i++) {
						for (int j = 0; j < boardSize; j++) {
							squares[i][j].setText("");
							
						}
					}	
				} else if (confirm == JOptionPane.NO_OPTION) {
					return;
				}
			}
		});
		
		JButton buttonNew = new JButton("New");
		buttonNew.setBounds(530, 200, 80, 30);
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(mainFrame, "Move on to new puzzle?", "Confirm",JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					Random random = new Random();
					int boardSelected = random.nextInt(puzzles.size());
					while (boardSelected == boardNumber) {
						boardSelected = random.nextInt(puzzles.size());
					}
					
					currentPuzzle = puzzles.get(boardSelected).puzzle;
					currentSolution = puzzles.get(boardSelected).solution;
					
					for (int i = 0; i < boardSize; i++) {
						for (int j = 0; j < boardSize; j++) {
							squares[i][j].setText(currentPuzzle[i][j]);
						}
					}
					boardNumber = boardSelected;
				} else if (confirm == JOptionPane.NO_OPTION) {
					return;
				}
			}
		});
		
		JButton buttonSolve = new JButton("Solve");
		buttonSolve.setBounds(530, 250, 80, 30);
		buttonSolve.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				String[][] getInput = new String[boardSize][boardSize];
				for (int i = 0; i < boardSize; i++) {
					for (int j = 0; j < boardSize; j++) {
						getInput[i][j] = squares[i][j].getText();
					}
				}
				if (solve(getInput)) {
					for (int i = 0; i < boardSize; i++) {
						for (int j = 0; j < boardSize; j++) {
							squares[i][j].setText(solvedBoard[i][j]);
						}
					}	
				} else {
					JOptionPane.showMessageDialog(mainFrame, "Could not solve board");
				}
				
				
			}
		});	
		
		mainFrame.add(buttonNew);
		mainFrame.add(buttonCheck);
		mainFrame.add(buttonReset);
		mainFrame.add(buttonClear);
		mainFrame.add(buttonSolve);
	}
	

	
	// check  current input to known solution
	public static boolean check(String[][] userInput, String[][] solution) {
		for (int i = 0; i  < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (userInput[i][j].length() != 0 ) {
					if (!userInput[i][j].equals(solution[i][j])) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// check if input number exists in row
	public static boolean isInRow(String[][] board, String number, int row) {
		for (int i = 0; i < boardSize; i++) {
			if (board[row][i].equals(number)) {
				return true;
			}
		}
		return false;
	}
	
	//check if input number exists in column
	public static boolean isInColumn(String[][] board, String number, int column) {
		for (int i = 0; i < boardSize; i++) {
			if (board[i][column].equals(number)) {
				return true;
			}
		}
		return false;
		
	}
	
	// check if number exists in square
	public static boolean isInSquare(String[][] board, String number, int row, int column) {
		int startRow = row - row % 3;
		int startColumn = column - column % 3;
		for (int i = startRow; i < startRow + 3; i++) {
				for (int j = startColumn; j < startColumn + 3; j++) {
					if (board[i][j].equals(number))
						return true;
				}
			}
		return false;
	}
	
	// checks all conditions (row, column and square)
	public static boolean canPlace(String[][] board, int numIn, int row, int column) {

		String number = Integer.toString(numIn);
		if (isInRow(board, number, row) || isInColumn(board,number,column)  || isInSquare(board,number,row,column)) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	// add puzzle
	public static void addPuzzle(String[][] puzzle, String[][] solution) {
		puzzles.add(new Puzzle(puzzle, solution));
	}
	
	// solve puzzle
	public static boolean solve(String[][] input) {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (input[i][j].length() == 0) {
					for (int k = 1; k <= boardSize; k++) {
						if (canPlace(input,k,i,j)) {
							input[i][j] = Integer.toString(k);
							
							if (solve(input)) {
								return true;
							}
							else {
								input[i][j] = "";
							}
						}
					}
					return false;
				}
			}
		}
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				solvedBoard[i][j] = input[i][j];
			}
		}
		return true;
	}
}