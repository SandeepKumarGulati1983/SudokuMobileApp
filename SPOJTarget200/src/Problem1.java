import java.util.HashMap;

public class Problem1 {

	public static void main(String[] args) {

		Problem1 instance = new Problem1();
		// --------------------------
		// Create sudoku matrix from an array
		// fill the matrix with the object of filled cell and non filled cell

		// check one by one each cell value
		// if the cell is already filled leave it
		// if the cell is not filled
		// analyse the quardrent / row and column of this cell
		// fill the option array of cell

		// if failed to get options
		// start from the begining but with scaneNumber as +1
		// those cell , who are not having another option , will not be able to make
		// guess

		// make one guess as value , as per scanNumber value
		// guess boolean true if there are option more then one else keep it false
		// repeat
		// ---------------------------

		String r1 = new String("170800009");
		String r2 = new String("006010007");
		String r3 = new String("000007050");
		String r4 = new String("004905008");
		String r5 = new String("080060090");
		String r6 = new String("900403500");
		String r7 = new String("050700000");
		String r8 = new String("700090600");
		String r9 = new String("800006021");

		String[] sMatrix = new String[] { r1, r2, r3, r4, r5, r6, r7, r8, r9 };

		// Define the type of sudoku Globally 9*9 or 3*3 or 16*16 etc.
		instance.suType = sMatrix.length;

		// 1.
		// Cell[][] sudokuMatrix = instance.createSudoku(sMatrix);
		// Another way with HashMap
		HashMap<Integer, Cell> suMatrix = instance.createSudokuMap(sMatrix);
		instance.showCurrentMatrixStatus(suMatrix);
		// 2.
		// instance.scan(sudokuMatrix, sMatrix.length);
		instance.scan(suMatrix);
		instance.showPossibilityMatrixStatus(suMatrix);
	}

	int suType;

	class Cell {
		int value;
		boolean fixed;
		boolean guess;
		HashMap optionsArr;

		Cell(int v, int size, HashMap possibility, boolean filled) {
			this.value = v;
			this.fixed = filled;
			this.guess = false;
			this.optionsArr = possibility;
		}
	}

	HashMap<Integer, Cell> createSudokuMap(String[] value) {
		/*
		 * formula to access an element of array in hashMap is for 9*9 matrix -- element
		 * at 3 row * 3 colum can be find as (3-1)*9 + 3 = 21 another example 2 row *5
		 * colum = (2-1)*9 + 5 = 14 so the formula is - x row * y colum = (x-1)*size +
		 * column
		 */
		HashMap<Integer, Cell> sudokuMatrix = new HashMap<Integer, Cell>();

		for (int i = 0; i < value.length; i++) {
			for (int j = 0; j < value.length; j++) {
				int cellValue = value[i].charAt(j) - '0';
				int key = i * 9 + j;
				// now need to create a matrix of cell object
				System.out.print("(" + key + ")");
				if (cellValue == 0) {
					sudokuMatrix.put(key, new Cell(cellValue, value.length, getPossibilityArray(value.length), false));
					showPossibilities(sudokuMatrix.get(key).optionsArr);
				} else {
					sudokuMatrix.put(key, new Cell(cellValue, value.length, null, true));
					System.out.print(" " + sudokuMatrix.get(key).value + " ");
				}

			}
			System.out.println("");
		}
		return sudokuMatrix;
	}

	HashMap<Integer, Cell> scan(HashMap<Integer, Cell> suMatrix) {

		/*
		 * take one by one each cell. check the value is fixed or not. if not then do
		 * scan of quadrent , row and column build the option array.
		 */
		int length = this.suType;

		for (int i = 0; i < length * length; i++) {
			if (!suMatrix.get(i).fixed) {
				scanQuardrent(suMatrix, i);
				scanRow(suMatrix, i);
				scanColumn(suMatrix, i);
			}
		}
		return suMatrix;
	}

	void scanQuardrent(HashMap<Integer, Cell> suMatrix, int cellPosition) {
		/*
		 * need to check fixed value in quadrent. delete the available value. print the
		 * possibility matrix.
		 */
		int quardSqt = (int) Math.sqrt((double) this.suType);

		int row = getRow(cellPosition);
		int col = getCol(cellPosition);

		/*
		 * 3*0 +0 = 0 3*0+1 = 1 3*0 +2 = 2
		 * 
		 * 3*1 +0 = 3 
		 * 3*1 +1 = 4 
		 * 3*1 + 2 = 5
		 * 
		 * 3*2 +0 = 6 3*2+1 = 7 3*2 + 2 = 8
		 */
		int rowQuardrent = row / quardSqt ;
		int colQuardrent = col / quardSqt;
				
		for (int i = 0; i < quardSqt; i++) { // 1, 2 and 3 quard's cell i will lies in 0 to 2

			for (int j = 0 ; j < quardSqt; j++) {
				scanCell((quardSqt*rowQuardrent) + i, (quardSqt*colQuardrent) +j, suMatrix, cellPosition);
			}
		}

	}

	void scanRow(HashMap<Integer, Cell> suMatrix, int cellPosition) {
		int row = getRow(cellPosition);
		for (int col = 0; col < this.suType; col++) {
			scanCell(row, col, suMatrix, cellPosition);
		}
	}

	void scanColumn(HashMap<Integer, Cell> suMatrix, int cellPosition) {
		int col = getCol(cellPosition);
		for (int row = 0; row < this.suType; row++) {
			scanCell(row, col, suMatrix, cellPosition);
		}

	}

	void scanCell(int row, int col, HashMap<Integer, Cell> suMatrix, int cellPosition) {
		int currentcellPosition = row * 9 + col;
		Cell currentCell = suMatrix.get(currentcellPosition);
		Cell targetCell = suMatrix.get(cellPosition);
		if (currentCell.fixed) {
			if (targetCell.optionsArr.containsKey(currentCell.value - 1))
				targetCell.optionsArr.remove(currentCell.value - 1);
		}

	}

	int getRow(int cellPosition) {
		return cellPosition / this.suType;
	}

	int getCol(int cellposition) {
		return cellposition % this.suType;
	}

	HashMap getPossibilityArray(int sudokuSize) {

		HashMap allPossibilityTable = new HashMap();
		for (int i = 0; i < sudokuSize; i++) {
			allPossibilityTable.put(i, i + 1);
		}
		return allPossibilityTable;

	}

	void showPossibilities(HashMap possibilityMatrix) {
		for (int i = 0; i < this.suType; i++) {
			if (possibilityMatrix.containsKey(i))
				System.out.print(possibilityMatrix.get(i));
		}
		System.out.print(" ");

	}

	/*
	 * instead of preparing a Matrix of array , its better to prepare a hash map for
	 * fast searching
	 */
	// Cell[][] createSudoku(String[] values) {
	// Cell[][] matrix = new Cell[values.length][values.length];
	// for (int i = 0; i < values.length; i++) {
	// for (int j = 0; j < values.length; j++) {
	//
	// // System.out.print(values[i].charAt(j));
	// int cellValue = values[i].charAt(j) - '0';// *******Very important
	// conversion****
	// if (cellValue == 0) {
	// matrix[i][j] = new Cell(cellValue, values.length,
	// getPossibilityArray(values.length), false);
	// } else {
	// matrix[i][j] = new Cell(cellValue, values.length, null, true);
	// }
	// // System.out.print(matrix[i][j].value +" "+ matrix[i][j].fixed +" ");
	// if (matrix[i][j].value == 0) {
	// showPossibilityMatrix(matrix[i][j].optionsArr, values.length);
	// } else {
	// System.out.print(" " + matrix[i][j].value + " ");
	// }
	// }
	// System.out.println("");
	// }
	// return matrix;
	// }

	// Cell[][] scan(Cell[][] sudokuMatrix, int sudokuSize) {
	//
	// for (int i = 0; i < sudokuSize; i++) {
	// for (int j = 0; j < sudokuSize; j++) {
	// if (!sudokuMatrix[i][j].fixed) {
	//
	// // fillOptionsArray(sudokuMatrix[i][j].optionsArr, scanQuad, scanCol,
	// // scanInRow);
	//
	// }
	// }
	//
	// }
	//
	// return sudokuMatrix;
	// }

	void showCurrentMatrixStatus(HashMap<Integer, Cell> matrix) {
		System.out.println("");
		for (int i = 1; i <= (this.suType * this.suType); i++) {
			if (matrix.containsKey(i - 1)) {
				System.out.print(matrix.get(i - 1).value + " " /* + matrix.get(i).fixed */);
				if (i > 0 && i % 9 == 0)
					System.out.println("");
			}
		}

	}

	void showPossibilityMatrixStatus(HashMap<Integer, Cell> matrix) {
		System.out.println("");
		for (int i = 0; i < this.suType * this.suType; i++) {
			if (i > 0 && i % 9 == 0) {
				System.out.println("");
			}

			if (matrix.get(i).value == 0) {
				showPossibilities(matrix.get(i).optionsArr);
			} else {
				System.out.print(" " + matrix.get(i).value + " ");
			}

		}

	}
}
