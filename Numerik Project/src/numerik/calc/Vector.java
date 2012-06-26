package numerik.calc;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;

import numerik.calc.Matrix;
import numerik.ui.misc.MathDataSynchronizer;

public final class Vector
{
    
    private boolean transposed;
    public   String name;
    private     int length;

    BigDecimal[] values;

    public Vector(int rows)
    {
        this(new BigDecimal[rows]);
        setToNullvector();
    }
    
    public Vector(BigDecimal[] values)
    {
        this(values, false);
    }
    
    public Vector(BigDecimal[] values, boolean transposed)
    {
        this.values     = values;
        this.transposed = transposed;
        this.length     = values.length;
    }
    
    public Vector(String file, String name)
    {
        this(MathDataSynchronizer.getInstance().getVector(name));
    }
    
    
    public Vector(Vector vector)
    {
        Vector copy = vector.clone();
        this.name = copy.name;
        this.values = copy.values;
        this.length = copy.length;
        this.transposed = copy.transposed;
    }
    
    public boolean isTransposed()
    {
        return transposed;
    }
    
    public BigDecimal get(int index)
    {
        if (index >= length)
        {
            throw new IndexOutOfBoundsException("Vector.get-Funktion: Index out of Bounds: index=" + index);
        }
        
        return values[index];
    }
    
    public void set(int index, BigDecimal value)
    {
        if (index >= length)
        {
            throw new IndexOutOfBoundsException("Vector.set-Funktion: Index out of Bounds: index=" + index);
        }
        
        values[index] = value;
    }
    
    public int getLength()
    {
        return length;
    }
    
    
    public Vector mult(BigDecimal value)
    {
        BigDecimal[] v = new BigDecimal[length];
        for (int i = 0; i < values.length; i++)
        {
            v[i] = MathLib.round(MathLib.round(values[i]).multiply(MathLib.round(value)));
        }
        
        return new Vector(v);
    }
    
    
    public BigDecimal mult(Vector vector)
    {
        BigDecimal result = BigDecimal.ZERO;
        
        if (!transposed) 
        {
            throw new ArithmeticException("Multiplikation nur von transponiertem Vektor mit Vektor möglich.");
        }
        
        for(int i=0; i<length; i++) result = result.add( this.get(i).multiply( vector.get(i) ));
        
        return result;
    }
    
    
    public Vector divide(BigDecimal value)
    {
        if (BigDecimalExtension.equals(BigDecimal.ZERO, value))
        {
            throw new ArithmeticException("Bei der Skalardivision kann nicht durch 0 geteilt werden.");
        }
        
        BigDecimal dividend = new BigDecimal("1");
        BigDecimal quotient = dividend.divide(value, MathLib.getPrecision(), MathLib.getRoundingMode());
        
        return mult(quotient);
    }
    
    public Vector add(Vector vector) throws ArithmeticException
    {
        if (vector.getLength() != length)
        {
            throw new ArithmeticException("Die Länge der beiden Vektoren muss übereinstimmen.");
        }
        BigDecimal[] v = new BigDecimal[length];
        for (int i = 0; i < length; i++)
        {
            v[i] = MathLib.round(MathLib.round(values[i]).add(MathLib.round(vector.get(i))));
        }
        return new Vector(v);
    }
    
    
    public Vector sub(Vector vector) throws ArithmeticException
    {
        return add(vector.mult(new BigDecimal("-1")));
    }

//    Muss noch �berarbeitet werden --> z.B. wegen dem transponierend
//    public Matrix mult(Matrix matrix)
//    {
//        if (length != matrix.getCols())
//        {
//            throw new ArithmeticException("Bei der Multiplikations von einem Vektor mit einer Matrix, muss der Vektor die selbe L�nge haben, wie die Matrix an Spalten besitzt.");
//        }
//        
//        BigDecimal[][] v = new BigDecimal[1][matrix.getCols()];
//        
//        for (int j = 0; j < matrix.getCols(); j++)
//        {   
//            BigDecimal sum = BigDecimal.ZERO;
//            
//            for (int j2 = 0; j2 < matrix.getCols(); j2++)
//            {
//                sum = MathLib.round( sum.add( MathLib.round( values[j].multiply( matrix.get(j2, j) ))));
//            }
//            
//            v[0][j] = sum;
//        }
//        
//        return new Matrix(v);
//    }
    
    public Vector getTransposed()
    {
        return new Vector(Arrays.copyOf(values, values.length), !transposed);
    }
    
    public void setToNullvector()
    {
        for (int i = 0; i < length; i++)
        {
            values[i] = BigDecimal.ZERO;
        }
    }
    
    public Matrix toMatrix() {
        return new Matrix(Arrays.copyOf(values, values.length), transposed ? values.length : 1);
    }
    
    @Override
    public Vector clone()
    {
        Vector copy = new Vector(length);
        copy.name = name;
        
        for (int i = 0; i < length; i++)
        {
            copy.set(i, new BigDecimal("0").add(get(i)));
        }
        
        return copy;
    }
    
    public BigDecimal norm() throws RuntimeException 
    {
        if( MathLib.getNorm()==0 ) return zsnorm();
        if( MathLib.getNorm()==1 ) return eunorm();
        
        throw new RuntimeException(MessageFormat.format("'MathLib.getNorm()' liefert den Wert {0}, für welche es keine Normimplementierung für Vektoren gibt.", MathLib.getNorm()));
    }
    
    private BigDecimal zsnorm() {
        
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;

        for(int i=0; i<getLength(); i++) {
            sum = get(i).abs();
            if ( max.compareTo( sum ) == -1 ) max = sum;
        }
        return MathLib.round( max );
    }
    
    private BigDecimal eunorm() {
        
        BigDecimal euklidNorm = BigDecimal.ZERO;
        BigDecimal        sum = BigDecimal.ZERO;
        
        for(int i=0; i<length; i++)    sum = sum.add( get(i).multiply( get(i) ));
        
        euklidNorm = MathLib.sqrt( sum );
        
        return euklidNorm;
    }
    
    /**
     * Tauscht die Reihen eines Vektors durch zwei Indizes
     * @param rowIndex1 erster Index
     * @param rowIndex2 zweiter Index
     */
    public Vector swapRows(int rowIndex1, int rowIndex2)
    {
        Vector tempVector = clone();
        
        BigDecimal tempBigDecimal = get(rowIndex1);
        tempVector.set(rowIndex1, get(rowIndex2));
        tempVector.set(rowIndex2, tempBigDecimal);
        
        return tempVector;
    }
    
    public Double[] toDoubleArray() {
        
        Double x[] = new Double[ length ];
        for(int i=0; i < length; i++) x[i] = get(i).doubleValue();
        
        return x;
    }
    
    
    public Vector getEquationsValue() {
        
        Vector   f = new Vector( getLength() );
        Double[] x = toDoubleArray();                  // x[0] = x_1 ; x[1] = x_2 ; usw.
        
        f.set(0, BigDecimal.valueOf(     x[0]*x[0] +x[1]*x[1]       +0.6*x[1] -0.16     ).negate());
        f.set(1, BigDecimal.valueOf(     x[0]*x[0] -x[1]*x[1] +x[0] -1.6*x[1] -0.14     ).negate());
        
        return f;
    }
    
    public Vector setUnitVector(Vector vector) {
        for(int i=0; i<vector.length; i++) {
            vector.set(i, BigDecimal.ONE);
        }
        return vector;
    }
    
    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[[");
    
        for (int row = 0; row < length; row++)
        {
            buffer.append(values[row].toPlainString());
            if (row < length - 1)
            {
                buffer.append(",");
            }
        }
        buffer.append("]]");
        return buffer.toString();
    }
}
