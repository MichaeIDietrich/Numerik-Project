package numerik.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import numerik.io.DocumentLoader;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;


public class Matrix {
    
    public enum SubstitutionDirection { FORWARD, BACKWARD }
    
    
    private int     rows;
    private int     cols;
    public  String  name;                                        // Name der Matrix für Ausgabe
    
    LatexFormula formula  = new LatexFormula();
    Recorder     recorder = Recorder.getInstance();
    
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


    public Matrix(String file, String name) {
        
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            
//            String line;
//            boolean transmit = false;
//            ArrayList<ArrayList<BigDecimal>> entries = new ArrayList<ArrayList<BigDecimal>>();
//            while ((line = br.readLine()) != null) {
//                
//                if(line.equals("Matrix#"+name) || line.equals("") ) 
//                    transmit = false;
//                
//                if (transmit) 
//                {
//                    ArrayList<BigDecimal> entry = new ArrayList<BigDecimal>();
//                    entries.add(entry);
//                
//                    for (String number : line.split(",")) {
//                        entry.add(new BigDecimal(number));
//                    }
//                }
//                
//                if(line.equals("Matrix#"+name)) 
//                    transmit = true;
//            }
//            
//            rows = entries.size();
//            cols = entries.get(0).size();
//            this.name = name;
//            
//            values = new BigDecimal[rows][cols];
//            
//            for (int n = 0; n < rows; n++) {
//                for (int m = 0; m < cols; m++) {
//                    values[n][m] = entries.get(n).get(m);
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        this(new DocumentLoader().readMatrix(file, name));
    }
    
    public Matrix(ArrayList<BigDecimal> values, int cols)
    {
        this.cols = cols;
        this.rows = values.size() / cols;
        
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
    
    
    public Matrix(Matrix matrix) {
        Matrix copy = matrix.clone();
        this.name = copy.name;
        this.values = copy.values;
        this.cols = copy.getCols();
        this.rows = copy.getRows();
    }
    
    
    // getters
    public int getRows()
    {
        return rows;
    }
    
    
    public int getCols()
    {
        return cols;
    }
    
    
    protected void setRows(int rows)
    {
        this.rows = rows;
    }
    
    
    protected void setCols(int cols)
    {
        this.cols = cols;
    }
    
    
    public BigDecimal get(int row, int col) throws IndexOutOfBoundsException
    {
        if (!isValidIndex(row, col))
        {
            throw new IndexOutOfBoundsException("Matrix.get-Funktion: Index out of Bounds: row=" + row + ", col=" + col);
        }
        
        return values[row][col];
    }
    
    
    public void set(int row, int col, BigDecimal value) throws IndexOutOfBoundsException
    {
        if (!isValidIndex(row, col))
        {
            throw new IndexOutOfBoundsException("Matrix.set-Funktion: Index out of Bounds: row=" + row + ", col=" + col);
        }
        
        values[row][col] = value;
    }
    
    
    // functions
    public boolean isQuadratic() {
        return rows == cols;
    }
    
    
    public Matrix identity()
    {
        // Einheitsmatrix muss quadratisch sein
        if (!isQuadratic())
        {
            throw new ArithmeticException("Die Matrix muss quadratisch sein");
        }
        
        Matrix identity = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
        {
            identity.set(i, i, BigDecimal.ONE);
        }
        
        return identity;
    }
    
    
    public Matrix getTransposed()
    {
        Matrix transposedMatrix = new Matrix(cols, rows);
        
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < cols; n++) {
                transposedMatrix.set(n, m, values[m][n]);
            }
        }
        
        return transposedMatrix;
    }
    
    
    public Matrix add(Matrix x) throws ArithmeticException
    {
        if (rows != x.getRows() || cols != x.getCols())
        {
            throw new ArithmeticException("Beide Matrizen müssen der Form mxn entsprechen!");
        }
        BigDecimal[][] v = new BigDecimal[rows][cols];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                v[i][j] = MathLib.round(MathLib.round(values[i][j]).add(x.get(i, j)));
            }
        }
        return new Matrix(v);
    }
    
    public Matrix subtract(Matrix x)
    {
        return add(x.mult(new BigDecimal("-1")));
    }
    
    
    public Matrix mult(BigDecimal x) {
        BigDecimal[][] v = new BigDecimal[rows][cols];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                v[i][j] = MathLib.round(MathLib.round(values[i][j]).multiply(x));
            }
        }
        return new Matrix(v);
    }
    
    public Matrix divide(BigDecimal x) throws ArithmeticException
    {  
        if (x == BigDecimal.ZERO)
        {
            throw new ArithmeticException("Bei der Skalardivision kann nicht durch 0 geteilt werden.");
        }
        
        BigDecimal dividend = new BigDecimal("1");
        BigDecimal quotient = dividend.divide(x, MathLib.getPrecision(), MathLib.getRoundingMode());
        
        return mult(quotient);
    }
    
    public Vector mult(Vector vector) throws ArithmeticException
    {                       // Multipliziere Matrix und Vektor
        if(cols != vector.getLength())
        {
            throw new ArithmeticException("Matrizen sind nicht verkettet, Spaltenanzahl der Matrix muss gleich der Länge des Vektors sein.");
        }

        BigDecimal   sum = BigDecimal.ZERO;
        Vector newVector = new Vector( rows );
        
        for(int zeile=0; zeile<rows; zeile++)
        {
                for(int j=0; j<vector.getLength(); j++)
                {
                    sum = sum.add( MathLib.round( values[zeile][j].multiply( vector.get(j) )));
                }
                newVector.set(zeile, sum);
                sum = BigDecimal.ZERO;
        }
        
        newVector.name = vector.name;
        
        return newVector;
    }
    
    
    public Matrix mult(Matrix matrix) throws ArithmeticException
    {
        if(cols != matrix.getRows())
        {
            throw new ArithmeticException("Matrizen sind nicht verkettet, Spaltenanzahl der 1. Matrix muss gleich der Zeilenanzahl der 2 Matrix sein.");
        }
        
        BigDecimal[][] v = new BigDecimal[rows][matrix.getCols()];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < matrix.getCols(); j++)
            {               
                BigDecimal sum = BigDecimal.ZERO;
                
                for (int j2 = 0; j2 < cols; j2++)
                {
//                    sum = sum.add( MathLib.round( values[i][j2].multiply( matrix.get(j2, j) )));
                    sum = MathLib.round(sum.add( MathLib.round( values[i][j2].multiply( matrix.get(j2, j) ))));
                }
                
                v[i][j] = sum;
            }
        }
        
        return new Matrix(v);
    }
    
    
    @Override
    public Matrix clone()
    {
        
        Matrix copy = new Matrix(rows, cols);
        copy.name = name;
        
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
    
    
    public Matrix getInverse() throws ArithmeticException
    {
        if(!isQuadratic()) 
        {
            throw new ArithmeticException("Die Matrix muss quadratisch sein");
        }
        
        Matrix inverse = new Matrix( rows, rows ).identity();
        
        BigDecimal temp = BigDecimal.ZERO;
        Matrix    clone = clone();
        
        for(int col=0; col<rows-1; col++) 
        { // überführt A aus (A|E) in obere Dreiecksmatrix, Umformungen in A parallel in E
            for(int row=col; row<rows-1; row++) 
            {
                
                temp = clone.values[row+1][col].divide( 
                       clone.values[col][col].negate(), MathLib.getInversePrecision(), RoundingMode.FLOOR );    
                
                
                for(int i=0; i<this.values.length; i++) 
                {
                      clone.values[row+1][i] =   clone.values[row+1][i].add( temp.multiply(   clone.values[col][i] ));
                    inverse.values[row+1][i] = inverse.values[row+1][i].add( temp.multiply( inverse.values[col][i] ));
                }
                clone.values[row+1][col] = BigDecimal.ZERO;
            }
        }
        
        for(int row=0; row<rows; row++) 
        { // normiert Spur von A auf 1, Rechenschritte parallel auch in E durchführen
            temp = clone.values[row][row];
            for(int col=0; col<rows; col++) 
            {
                inverse.values[row][col] = inverse.values[row][col].divide( temp, MathLib.getInversePrecision(), RoundingMode.HALF_UP );
                  clone.values[row][col] =   clone.values[row][col].divide( temp, MathLib.getInversePrecision(), RoundingMode.HALF_UP );
            }
        }
        
        for(int t=rows-1; t>=1; t--) 
        { // überführe A in Einheitsmatrix E, dann (A|E) -> (E|A^(-1))
            for(int row=rows-1; row>=0; row--) 
            {
                if(row<t) 
                {
                    temp = clone.values[row][t].negate();
                    for(int i=0; i<rows; i++) 
                    {
                        inverse.values[row][i] = inverse.values[row][i].add( temp.multiply( inverse.values[t][i] ));
                    }
                }
            }
        }
        return inverse;
    }
    

    public Vector solveX(Vector b) 
    {
        boolean recorderState = recorder.isActive();
        
        Vector clone_b = b.clone();
        
        recorder.setActive( false );
        Matrix L = getL( b.clone());
        
        recorder.setActive( recorderState );
        Matrix U = getU( clone_b );
        
        Vector y = substitution( L, clone_b, SubstitutionDirection.FORWARD  );
        Vector x = substitution( U, y,       SubstitutionDirection.BACKWARD );
        
        return x;
    }
    
    
    public Matrix getL()
    {
        return doLUDecomposition(0, null).item2; // 0 liefert L zurück
    } 
    
    
    
    public Matrix getU()
    {
        return doLUDecomposition(1, null).item2; // 1 liefert U zurück
    }
    
    public Vector getLPerm()
    {
        return doLUDecomposition(2, null).item1; // 2 liefert Permutationen zurück (Reihenfolge der Zeilenvertauschung einer Matrix)
    }
    
    public Matrix getL(Vector b)
    {
        return doLUDecomposition(0, b).item2;
    }
    
    public Matrix getU(Vector b)
    {
        return doLUDecomposition(1, b).item2;
    }
    
    public Vector getlperm(Vector b)
    {
        return doLUDecomposition(2, b).item1;
    }
    
    /**
     * Gibt eine Tuple zurück, die entweder eine Matrix (für L oder U) hält oder ein Vector (für die Permutationen)
     * @param which_matrix Gibt an, ob L (=0), U (=1), oder lperm(=2) zurückgegeben wird
     * @param b Ist ein Vektor, der die Ergebnisse von den Gleichungssystemen darstellt
     */
    private Tuple<Vector, Matrix> doLUDecomposition(int which_matrix, Vector b) {
        
        BigDecimal temp = BigDecimal.ZERO;
        Matrix        U = clone();
        Matrix        L = identity();
        
        //Reihenfolge der Vertauschung bei der Pivotstrategie(Permutationen)
        Vector    lperm = new Vector(rows);
        
        // Ergebnis ist [1, 2, 3, 4, ...] als Vektor
        for (int cellOfVector = 0; cellOfVector < rows; cellOfVector++)
        {
            lperm.set(cellOfVector, new BigDecimal(cellOfVector + 1));
        }
             
        if (name == null)
        {
            name = "A";
        }
        
        if (b != null && recorder.isActive())
        {
            if(b.name == null) b.name = "b";

            formula.addNewLine(2).addSolidLine().addNewLine(1);
            formula.addText("LU-Zerlegung").addNewLine(2);
        }
        
        for(int row=0; row < L.rows; row++) 
        { 
            // Pivotisierung + Gaussschritte, reduzierte Zeilenstufenform
            if (MathLib.isPivotStrategy())
            {
                lperm = lperm.swapRows(row, pivotColumnStrategy(U, b, row )); 
            }
            
            for(int t=row; t<U.rows-1; t++) 
            {
                temp = MathLib.round( U.values[t+1][row].divide( U.values[row][row], MathLib.getInversePrecision(), RoundingMode.HALF_UP ));
                
                for(int i=row; i<U.rows; i++)
                { 
                    U.values[t+1][i] = MathLib.round( U.values[t+1][i].subtract( MathLib.round( temp.multiply( U.values[row][i] ) )));
                }  
                U.values[t+1][row] = temp;
                
                if (b!=null && recorder.isActive()) 
                    formula.addTildeText( name ).addText(" = ").addMatrix(U).addText(", ")
                           .addTildeText(b.name).addText(" = ").addVector(b).addNewLine(1);
            }
        }
        
        for(int row=0; row<L.rows; row++) 
        { // Trenne Matizen U und L voneinander
            for(int col=0; col<L.cols; col++) 
            {
                if (row>col) {
                    L.values[row][col] = U.values[row][col];
                    U.values[row][col] = BigDecimal.ZERO;
                }
            }   
        }
        
        if (b!=null && recorder.isActive()) 
        {  
            formula.addTildeText("L").addText(" = ").addMatrix(L).addNewLine(1);
            formula.addTildeText("U").addText(" = ").addMatrix(U).addNewLine(2);
            formula.addText("Probe: ").addTildeText("L").addSymbol("*").addTildeText("U")
                   .addText(" = ").addMatrix(L.mult(U)).addNewLine(2);
            formula.addSolidLine().addNewLine(1);
            recorder.add(formula);
        }
        
        if(which_matrix == 0) 
        {
            return new Tuple<Vector, Matrix>(null, L);
        }
        if(which_matrix == 1)
        {
            return new Tuple<Vector, Matrix>(null, U);
        }
        else
        {
            return new Tuple<Vector, Matrix>(lperm, null);
        }
    }
    
    
    /**
     * Ermöglicht die Vorwärts und Rückwärtssubstitution von einer Matrix mit einem Vector b
     * 
     * @param matrix Matrix, die man Substituieren will
     * @param b Vektor, den man Substituieren will
     * @param str gibt an, wie man Substituieren will "forward" oder "backward"
     */
    public Vector substitution( Matrix matrix, Vector b, SubstitutionDirection direction )
    {
        BigDecimal term0 = BigDecimal.ZERO;
        BigDecimal term1 = BigDecimal.ZERO;
        BigDecimal term2 = BigDecimal.ZERO;
        Vector         y = new Vector( b.getLength());
        
        if ( direction == SubstitutionDirection.FORWARD)
        {
            y.set( 0, b.get(0) );
            
            for(int row=1; row<matrix.rows; row++)
            {
                term0 = BigDecimal.ZERO;
                
                for(int i=0; i<y.getLength()-1; i++)
                {
                    term0 = MathLib.round( matrix.values[row][i].multiply( y.get(i) )).add( term0 );
                }
                term1 = MathLib.round( b.get(row).subtract( term0 ));
                term2 = MathLib.round( term1.divide( matrix.values[row][row], MathLib.getInversePrecision(), RoundingMode.HALF_UP ));
                
                y.set( row, term2);
            }
            
            if (b!=null && recorder.isActive())
            {  
                formula.clear();
                formula.addText("Vorwärtssubstitution").addNewLine(2);
                formula.addText("y = ").addVector(y).addNewLine(2);
                formula.addSolidLine().addNewLine(2);
                recorder.add(formula);
            }
        }
        
        
        if ( direction == SubstitutionDirection.BACKWARD )
        {
            int dim = matrix.getRows()-1;
            
            y.set(dim, MathLib.round( b.get(dim).divide( matrix.values[dim][dim], MathLib.getPrecision(), RoundingMode.HALF_UP )));
            
            for(int row=dim; row>=0; row--)
            {
                term0 = BigDecimal.ZERO;
                for(int i=0; i<dim-row; i++)
                {
                    term0 = MathLib.round( term0.add( MathLib.round( matrix.values[row][dim-i].multiply( y.get(dim-i) ))));
                }
                term1 = MathLib.round( b.get(row).subtract( term0 ));
                term2 = MathLib.round( term1.divide( matrix.values[row][row], MathLib.getPrecision(), RoundingMode.HALF_UP ));
                y.set( row, term2);
            }
        }
        
        return y;
    }
    
    /**
     * Anwenden der Zeilenpivotstrategie, bei welcher Zeilen vertauscht werden nach dem Kriterium,
     * dass ein absoluter Wert aus einer Zeile der aktuellen Spalte ermittelt wird und die Zeile 
     * mit dem höchsten Wert wird mit der aktuellen Reihe int row getauscht
     * Zum Schluss wird die Zeile als int ausgegeben, die den maximalen Wert einer Zeile von der aktuellen
     * Spalte besitzt.
     * @param matrix Matrix, für die die Pivotstrategie angewendet werden soll
     * @param vector Vektor, für die die Pivotstrategie angewendet werden soll
     * @param row Anfangsreihe, bei welcher angefangen wird, die Zeilen zu vertauschen
     */
    public int pivotColumnStrategy( Matrix matrix, Vector b, int row )
    {
        BigDecimal maximum = BigDecimal.ZERO;
        BigDecimal    temp = BigDecimal.ZERO;
        int    rowposition = 0;
        boolean    rowswap = false;
        
        for(int t=0; t<matrix.getRows()-row; t++)
        {                              
            for(int i=row+t; i<matrix.getCols(); i++)
            {                            
                if ( matrix.values[i][row].abs().compareTo( maximum ) == 1 )
                {  
                    rowposition = i;                                             // Markiere Zeile mit Maximum
                    maximum     = matrix.values[i][row].abs();
                    rowswap     = true;
                }
            }
            
            if (rowswap && rowposition!=row )
            {
                for(int col=0; col<matrix.getCols(); col++)
                { // Zeilenvertauschung der Matrix
                    temp                            = matrix.values[row][col];
                    matrix.values[row][col]         = matrix.values[rowposition][col];
                    matrix.values[rowposition][col] = temp;
                }
                
                if(b!=null) 
                {
                    temp = b.get(row);                                               // Zeilenvertauschung des Vektors
                    b.set(row, b.get(rowposition));
                    b.set(rowposition, temp);
                }
                
                rowswap = false;
            }
        }
        
        return rowposition;
    }
      
    
    public Matrix getScaleOf()
    {
        Matrix scaledMatrix = identity();
        Vector        koeff = new Vector( rows );
        
        for(int row=0; row < rows; row++)
        {
            for(int col=0; col<getCols(); col++)
            {
                koeff.set(row, koeff.get(row).add( values[row][col].abs() ));
            }
            koeff.set(row, BigDecimal.ONE.divide( koeff.get(row), MathLib.getInversePrecision(), RoundingMode.HALF_UP ));
            
            scaledMatrix.values[row][row] = koeff.get(row).multiply( scaledMatrix.values[row][row] );
        }
        return scaledMatrix;
    }
    
    
    public BigDecimal det() throws ArithmeticException
    {
        if (!isQuadratic() || (rows>3 && cols>3))
        {
            throw new ArithmeticException("Die Matrix muss quadratisch sein");
        }
        BigDecimal sum;
        
        // Determinante mittels Sarrus Regel für 2x2 und 3x3 Matrizen
        int        dim = cols-1;
        BigDecimal det = BigDecimal.ONE;
                   sum = BigDecimal.ZERO;
                   
        for(int i=0; i<=dim; i++) 
        { // Regel von Sarrus: Teil 1 - Addition
            for(int t=0; t<=dim; t++)
            {
                if( i+t <= dim ) {
                    det = det.multiply( values[t][t+i] );
                } else {
                    det = det.multiply( values[t][t+i-dim-1] );
                }
            }
            sum = sum.add( det );
            det = BigDecimal.ONE;
        }
        
        
        for(int i=dim; i>=0; i--)
        { // Regel von Sarrus: Teil 2 - Subtraktion
            for(int t=dim; t>=0; t--)
            {
                if( t+i-dim >= 0 ) {
                    det = det.multiply( values[dim-t][t+i-dim] );
                } else {
                    det = det.multiply( values[dim-t][t+i+1] );
                }
            sum = sum.subtract( det );
            det = BigDecimal.ONE;
            }
        }
        return sum;
    }
    
    
    public BigDecimal getDeterminant() throws ArithmeticException
    {
//            double result = 0;
//
//            if(mat.length == 1) {
//            result = mat[0][0];
//            return result;
//            }
//
//            if(mat.length == 2) {
//            result = mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
//            return result;
//            }
//
//            for(int i = 0; i < mat[0].length; i++) {
//            double temp[][] = new double[mat.length - 1][mat[0].length - 1];
//
//            for(int j = 1; j < mat.length; j++) {
//            System.arraycopy(mat[j], 0, temp[j-1], 0, i);
//            System.arraycopy(mat[j], i+1, temp[j-1], i, mat[0].length-i-1);
//            }
//
//            result += mat[0][i] * Math.pow(-1, i) * determinant(temp);
//            }
//
//            return result;
        
        if (!isQuadratic())
        {
            throw new ArithmeticException("Die Matrix muss quadratisch sein");
        }
        
        return calculateDeterminant(values);
    }
    
    
    //Quelle: http://wiki.answers.com/Q/Determinant_of_matrix_in_java
    private BigDecimal calculateDeterminant(BigDecimal[][] matrix)
    {
        if (matrix.length == 1)
        {
            return matrix[0][0];
        }
        
        if (matrix.length == 2)
        {
            return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        }
        
        BigDecimal result = BigDecimal.ZERO;
        
        for (int col = 0; col < matrix[0].length; col++)
        {
            BigDecimal tmpMatrix[][] = new BigDecimal[matrix.length - 1][matrix[0].length - 1];
            
            for (int row = 1; row < matrix.length; row++)
            {
                System.arraycopy(matrix[row], 0, tmpMatrix[row - 1], 0, col);
                System.arraycopy(matrix[row], col + 1, tmpMatrix[row - 1], col, matrix[0].length - col - 1);
            }
            result = result.add(matrix[0][col].multiply(calculateDeterminant(tmpMatrix)));
            if (col % 2 == 1)
            {
                result = result.negate();
            }
        }
        
        return result;
    }
    
    
    public Matrix jakobiMatrix(Matrix functions)
    {
        for(int row=0; row<functions.getRows(); row++) 
        {
            for(int col=0; col<functions.getCols(); col++) 
            {
                values[row][col] = functions.get(row, col);
            }
        }

        return this;
    }
    
    
    /**
     * Gibt die Matrix zur�ck, die nur an der Diagonale die Zahlen der originalen Matrix enth�lt
     * Die anderen Zellen der Matrix beinhalten die Zahl 0
     * In Formeln auch als "B-Matrix" bezeichnet
     */
    public Matrix getDiagonalMatrix()
    {
        Matrix tempMatrix = new Matrix(rows, cols);

        for (int columnRow = 0; columnRow < cols; columnRow++)
        {
            tempMatrix.set( columnRow, columnRow, MathLib.round( this.get(columnRow, columnRow)));
        }

        return tempMatrix;
    }
    
    
    /**
     * Rundet die aktuelle Matrix mit der Mantissenlänge, die in der MathLib statisch enthalten ist.
     * Gibt eine Matrix, die zu einer bestimmten Mantissenlänge gerundet wurde.
     */
    public Matrix roundToMantissaLength()
    {
        Matrix tempMatrix = new Matrix(rows, cols);
        
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < cols; column++)
            {
                tempMatrix.set(row, column, MathLib.round(get(row, column)));
            }
        }
        
        return tempMatrix;
    }
    
    
    public BigDecimal norm() throws RuntimeException
    {
        if( MathLib.getNorm()==0 ) return zeilensummenNorm();
        if( MathLib.getNorm()==1 ) return frobeniusNorm();
        
        throw new RuntimeException("'MathLib.getNorm()' liefert keinen gültigen Wert");
    }
    
    
    private BigDecimal zeilensummenNorm()
    {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        
        for(int t=0; t<rows; t++) 
        {
            for(int i=0; i<rows; i++) 
            {
                sum = sum.add( values[t][i].abs() );
            }
            if ( max.compareTo( sum ) == -1 ) max = sum;
            
            sum = BigDecimal.ZERO;
        } 
        return MathLib.round( max );
    }
    
    
    private BigDecimal frobeniusNorm()
    {
        BigDecimal sum = BigDecimal.ZERO;
        
        for(int row=0; row<rows; row++)
        {
            for(int col=0; col<cols; col++)
            {
                sum = sum.add( values[row][col].multiply( values[row][col] ));
            }
        }
        return MathLib.sqrt( sum );
    }
    
    
    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int row = 0; row < rows; row++)
        {
            buffer.append("[");
            for (int col = 0; col < cols; col++)
            {
                buffer.append(MathLib.round(values[row][col]).toPlainString());
                if (col < cols - 1)
                {
                    buffer.append(",");
                }
            }
            buffer.append("]");
            if (row < rows - 1)
            {
                buffer.append(",");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
}