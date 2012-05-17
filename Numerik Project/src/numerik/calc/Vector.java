package numerik.calc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import numerik.calc.Matrix;

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
    
    public Vector(String file, String name) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            boolean transmit = false;
            ArrayList<BigDecimal> entry = new ArrayList<BigDecimal>();
            while ((line = br.readLine()) != null) {
 
                if(line.contains("Vector#"+name) || line.equals("") ) 
                    transmit = false;

                if (transmit) 
                {
                    for (String number : line.split(",")) {
                        entry.add(new BigDecimal(number));
                    }
                }
                
                if(line.contains("Vector#"+name)) 
                    transmit = true;
            }
            
            length    = entry.size();
            this.name = name;
            
            values = new BigDecimal[length];

            for (int n = 0; n < length; n++) {
                    values[n] = entry.get(n);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            v[i] = MathLib.round(MathLib.round(values[i]).multiply(value));
        }
        return new Vector(v);
    }
    
    public Vector divide(BigDecimal value)
    {
        if (value == BigDecimal.ZERO)
        {
            throw new ArithmeticException("Bei der Skalardivision kann nicht durch 0 geteilt werden.");
        }
        
        BigDecimal dividend = new BigDecimal("1");
        BigDecimal quotient = dividend.divide(value, MathLib.getPrecision(), MathLib.getRoundingMode());
        
        return mult(quotient);
    }
    
    public Vector add(Vector vector)
    {
        if (vector.getLength() != length)
        {
            throw new ArithmeticException("Die Länge der beiden Vektoren muss übereinstimmen.");
        }
        BigDecimal[] v = new BigDecimal[length];
        for (int i = 0; i < length; i++)
        {
            v[i] = MathLib.round(MathLib.round(values[i]).add(vector.get(i)));
        }
        return new Vector(v);
    }
    
    
    public Vector sub(Vector vector)
    {
        if (vector.getLength() != length)
        {
            throw new ArithmeticException("Die Länge der beiden Vektoren muss übereinstimmen.");
        }
        BigDecimal[] v = new BigDecimal[length];
        for (int i = 0; i < length; i++)
        {
            v[i] = MathLib.round(MathLib.round(values[i]).subtract(vector.get(i)));
        }
        return new Vector(v);
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
            copy.set(i, get(i));
        }
        return copy;
    }
    
    public BigDecimal norm() {
        if( MathLib.getNorm()==0 ) return zsnorm();
        if( MathLib.getNorm()==1 ) return eunorm();
        
        return null;
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
    
    
    public Double[] toDoubleArray() {
        
        Double x[] = new Double[ length ];
        for(int i=0; i<=length-1; i++) x[i] = get(i).doubleValue();
        
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
