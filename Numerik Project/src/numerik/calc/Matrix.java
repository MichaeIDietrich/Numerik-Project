package numerik.calc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	
	protected void setRows(int rows) {
		this.rows = rows;
	}

	protected void setCols(int cols) {
		this.cols = cols;
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

	public Matrix identity() {
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

	public Matrix getTransposed() {
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
					
					BigDecimal sum = BigDecimal.ZERO;
					
					for (int j2 = 0; j2 < cols; j2++) {
						sum = sum.add( values[i][j2].multiply( x.get(j2, j) ));
					}
					v[i][j] = sum;
				}
			}
			return new Matrix(v);
		}
	}

	
	
	@Override
	public Matrix clone() {

		Matrix copy = new Matrix(rows, cols);
		
		for(int row=0; row<rows; row++) {
			for(int col=0; col<cols; col++) {
				copy.values[row][col] = values[row][col];
			}
		}
		return copy;
	}

	
	
	// helper function
	private boolean isValidIndex(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

	
	
	public Matrix getInverse() {
		
		Matrix inverse = new Matrix( rows, rows ).identity();
		
		if(!isQuadratic()) {
			return null;
		} else {
			BigDecimal temp = BigDecimal.ZERO;
			Matrix    clone = clone();
			
			for(int col=0; col<rows-1; col++) {					// überführt A aus (A|E) in obere Dreiecksmatrix, Umformungen in A parallel in E -
				for(int row=col; row<rows-1; row++) {	
					temp = clone.values[row+1][col].divide( clone.values[col][col].negate(), 20, RoundingMode.FLOOR );	
					for(int i=0; i<this.values.length; i++) {
						  clone.values[row+1][i] =   clone.values[row+1][i].add( temp.multiply(   clone.values[col][i] ));
						inverse.values[row+1][i] = inverse.values[row+1][i].add( temp.multiply( inverse.values[col][i] ));
					}
					clone.values[row+1][col] = BigDecimal.ZERO;
				}
			}
			
			for(int row=0; row<rows; row++) {					// normiert Spur von A auf 1, Rechenschritte parallel auch in E durchführen -
				temp = clone.values[row][row];
				for(int col=0; col<rows; col++) {
					inverse.values[row][col] = inverse.values[row][col].divide( temp, 20, RoundingMode.HALF_UP );
					  clone.values[row][col] =   clone.values[row][col].divide( temp, 20, RoundingMode.HALF_UP );
				}
			}
			
			for(int t=rows-1; t>=1; t--) {						// überführe A in Einheitsmatrix E, dann (A|E) -> (E|A^(-1)) -
				for(int row=rows-1; row>=0; row--) {
					if(row<t) {
						temp = clone.values[row][t].negate();
						for(int i=0; i<=rows-1; i++) {
							inverse.values[row][i] = inverse.values[row][i].add( temp.multiply( inverse.values[t][i] ));
						}
					}
				}
			}
		}
		return inverse;
	}
	
	
	
	public Vector determineX(Vector b) {
		Matrix L = getL();
		Matrix U = getU();
		
		Vector y = substitution( L, b, "forward"  );
		Vector x = substitution( U, y, "backward" );
		
		return x;
	}
	
	
	
	public Matrix getL() {
		return doLUDecomposition(0, null);	// 0 liefert L zurück
	}
	
	
	
	public Matrix getU() {
		return doLUDecomposition(1, null);	// 1 liefert U zurück
	}
	
	
	
	private Matrix doLUDecomposition(int what_matrix, Vector b) {
		
		BigDecimal temp = BigDecimal.ZERO;
		Matrix    	  L = clone();
		Matrix 		  U = new Matrix( rows, rows ).identity();
		
		for(int row=0; row<U.rows; row++) {
			
			if (MathLib.isPivotstrategy()) L = pivotColumnStrategy( L, b, row );	
		
			for(int t=row; t<L.cols-1; t++) {
				temp = L.values[t+1][row].divide( L.values[row][row].negate(), MathLib.getPrecision(), RoundingMode.HALF_UP );
				U.values[t+1][row] = temp.multiply( BigDecimal.ONE.negate() );				

				for(int i=0; i<L.rows; i++) {
					L.values[t+1][i] = L.values[t+1][i].add( temp.multiply( L.values[row][i]) );
				}
				L.values[t+1][row] = BigDecimal.ZERO;
			}
		}
		
		if(what_matrix==0) {
			return U;			// Matrizen vertauscht U=L und L=U
		} else {
			return L;
		}
	}
	
	
	
	public Vector substitution( Matrix matrix, Vector b, String str ) {
		
		BigDecimal term0 = BigDecimal.ZERO;
		BigDecimal term1 = BigDecimal.ZERO;						
		BigDecimal term2 = BigDecimal.ZERO;
		Vector    	   y = new Vector( b.getLength());
		
		if ( str.equals("forward") ) {
			y.set( 0, b.get(0) );		  // y_0 = b_0
			
			for(int row=1; row<matrix.rows; row++) {
				term0 = BigDecimal.ZERO;
				for(int i=0; i<y.getLength()-1; i++) term0 = matrix.values[row][i].multiply( y.get(i) ).add( term0 );
				term1 = b.get(row).subtract( term0 );
				term2 = term1.divide( matrix.values[row][row], MathLib.getPrecision(), RoundingMode.HALF_UP );
				y.set(row, term2);
			}
		}
		
		if ( str.equals("backward") ) {
			int dim = matrix.getRows()-1;
			y.set(dim, b.get(dim).divide( matrix.values[dim][dim], MathLib.getPrecision(), RoundingMode.HALF_UP ));
			
			for(int row=matrix.getRows()-1; row>=0; row--) {
				term0 = BigDecimal.ZERO;
				for(int i=0; i<matrix.getRows()-1-row; i++) term0 = term0.add( matrix.values[row][dim-i].multiply( y.get(dim-i) ) );
				term1 = b.get(row).subtract( term0 );
				term2 = term1.divide( matrix.values[row][row], MathLib.getPrecision(), RoundingMode.HALF_UP );
				y.set(row, term2);
			}
		}
		return y;
	}
	
	
	
	public Matrix pivotColumnStrategy( Matrix matrix, Vector b, int row ) {
		
		BigDecimal  max = BigDecimal.ZERO;
		BigDecimal temp = BigDecimal.ZERO;
		int 	 laenge = matrix.rows-(row-1);
		int 		pos = 0;
		
		for(int t=0; t<laenge-row+1; t++) {						    // Bsp 4x4: vergleicht Zeilen ...
			for(int i=t; i<laenge-1; i++) {						    // Row=1: 1,2,3,4; Row=2: 2,3,4; Row=3: 3,4 ; Ende
				if (matrix.values[row+i][row].abs().compareTo( max.abs() ) == 1) {
					pos = row+i;									// Markiert Zeile mit groesstem Wert
					max = matrix.values[row+i][row];
				}
			}
		
			for(int i=0; i<matrix.getRows(); i++) {					// Zeilenvertauschung der Matrix	
				temp = matrix.values[row][i];
				matrix.values[row][i] = matrix.values[pos][i];		
				matrix.values[pos][i] = temp;
			}
		
			if(pos!=row) {											// Zeilenvertauschung des Vektors
				temp = b.get(row);
				b.set(row, b.get(pos));								
				b.set(pos, temp);
			}
		}
		return matrix;
	}
}



