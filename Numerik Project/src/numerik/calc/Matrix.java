package numerik.calc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Matrix {

	private int rows;
	private int cols;

	BigDecimal[][] values;

	// constructors
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		values = new BigDecimal[rows][cols];

		for (int m = 0; m < rows; m++) {
			for (int n = 0; n < cols; n++) {
				values[m][n] = BigDecimal.ZERO;
			}
		}

	}

	public Matrix(BigDecimal[][] values) {
		this.rows = values.length;
		this.cols = values[0].length;

		this.values = values;
	}

	public Matrix(BigDecimal[] values, int cols) {
		this.cols = cols;
		this.rows = values.length / cols;

		this.values = new BigDecimal[rows][cols];

		int m = 0, n = 0;
		for (BigDecimal v : values) {
			this.values[m][n] = v;

			n++;
			if (n == cols) {
				n = 0;
				m++;
			}
		}
	}

	public Matrix(int cols, int rows, BigDecimal initValue) {
		this(cols, rows);

		for (int m = 0; m < values.length; m++) {
			for (int n = 0; n < values[m].length; n++) {
				values[m][n] = initValue;
			}
		}
	}

	public Matrix(String file) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;
			ArrayList<ArrayList<BigDecimal>> entries = new ArrayList<ArrayList<BigDecimal>>();
			while ((line = br.readLine()) != null && !line.equals("")) {

				ArrayList<BigDecimal> entry = new ArrayList<BigDecimal>();
				entries.add(entry);

				for (String number : line.split(",")) {
					entry.add(new BigDecimal(number));
				}
			}

			rows = entries.size();
			cols = entries.get(0).size();

			values = new BigDecimal[rows][cols];

			for (int n = 0; n < rows; n++) {
				for (int m = 0; m < cols; m++) {
					values[n][m] = entries.get(n).get(m);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// getters
	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public BigDecimal get(int row, int col) {
		if (!isValidIndex(row, col)) {
			System.err.println("Matrix.get-Funktion: Index out of Bounds: row="
					+ row + ", col=" + col);
			return BigDecimal.ZERO;
		}

		return values[row][col];
	}

	public void set(int row, int col, BigDecimal value) {
		if (!isValidIndex(row, col)) {
			System.err.println("Matrix.set-Funktion: Index out of Bounds: row="
					+ row + ", col=" + col);
		}

		values[row][col] = value;
	}

	// functions
	public boolean isQuadratic() {
		return rows == cols;
	}

	public Matrix getIdentity() {
		// Einheitsmatrix muss quadratisch sein
		if (!isQuadratic()) {
			return null;
		}

		Matrix identity = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			identity.set(i, i, BigDecimal.ONE);
		}

		return identity;
	}

	public Matrix getTransposedMatrix() {
		Matrix transposedMatrix = new Matrix(cols, rows);

		for (int m = 0; m < rows; m++) {
			for (int n = 0; n < cols; n++) {
				transposedMatrix.set(n, m, values[m][n]);
			}
		}

		return transposedMatrix;
	}

	public Matrix add(Matrix x) {
		if (rows != x.getRows() || cols != x.getCols())
			return null;
		else {
			BigDecimal[][] v = new BigDecimal[rows][cols];
			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[i].length; j++) {
					v[i][j] = values[i][j].add(x.get(i, j));
				}
			}
			return new Matrix(v);
		}
	}
	
	public Matrix mult(BigDecimal x) {
		BigDecimal[][] v = new BigDecimal[rows][cols];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				v[i][j] = values[i][j].multiply(x);
			}
		}
		return new Matrix(v);
	}
	
	public Matrix mult(Matrix x) {
		if(cols != x.getRows())
			return null;
		else {
			BigDecimal[][] v = new BigDecimal[rows][x.getCols()];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < x.getCols(); j++) {
					BigDecimal bd = BigDecimal.ZERO;
					for (int j2 = 0; j2 < cols; j2++) {
						bd.add(values[i][j2].multiply(x.get(j2, j)));
					}
					v[i][j] = bd;
				}
			}
			return new Matrix(v);
		}
	}

	@Override
	public Matrix clone() {

		Matrix copy = new Matrix(rows, cols);
		copy.values = values.clone();
		return copy;
	}

	// helper function
	private boolean isValidIndex(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

}
