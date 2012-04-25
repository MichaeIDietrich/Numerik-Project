package numerik.calc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Matrix {
  
  private int rows;
  private int cols;
  
  double[][] values;
  
  
  // constructors
  public Matrix(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    
    values = new double[rows][cols];
  }
  
  
  public Matrix(double[][] values) {
    this.rows = values.length;
    this.cols = values[0].length;
    
    this.values = values;
  }
  
  
  public Matrix(double[] values, int cols) {
    this.cols = cols;
    this.rows = values.length / cols;
    
    this.values = new double[rows][cols];
    
    int m = 0, n = 0;
    for (double v : values) {
      this.values[m][n] = v;
      
      n++;
      if (n == cols) {
        n = 0;
        m++;
      }
    }
  }
  
  
  public Matrix(int cols, int rows, double value) {
    this(cols, rows);
    
    for (int m = 0; m < values.length; m++) {
      for (int n = 0; n < values[m].length; n++) {
        values[m][n] = value;
      }
    }
  }
  
  
  public Matrix(String file) {
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      
      String line;
      ArrayList<ArrayList<Double>> entries = new ArrayList<ArrayList<Double>>();
      while ((line = br.readLine()) != null && !line.equals("")) {
        ArrayList<Double> entry = new ArrayList<Double>();
        entries.add(entry);
        for (String number : line.split(",")) {
          entry.add(Double.parseDouble(number));
        }
      }
      
      rows = entries.size();
      cols = entries.get(0).size();
      
      values = new double[rows][cols];
      
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
  
  public double get(int row, int col) {
    if (!isValidIndex(row, col)) {
      System.err.println("Matrix-Get-Function: Index out of Bounds: row=" + row + ", col=" + col);
      return 0;
    }
    
    return values[row][col];
  }
  
  public void set(int row, int col, double value) {
    if (!isValidIndex(row, col)) {
      System.err.println("Matrix-Set-Function: Index out of Bounds: row=" + row + ", col=" + col);
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
      identity.set(i, i, 1d);
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
