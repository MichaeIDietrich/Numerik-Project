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
        return values[index];
    }
    
    public void set(int index, BigDecimal value)
    {
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
            v[i] = values[i].multiply(value);
        }
        return new Vector(v);
    }
    
    
    public Vector add(Vector vector)
    {
        if (vector.getLength() != length)
        {
            return null;
        }
        BigDecimal[] v = new BigDecimal[length];
        for (int i = 0; i < length; i++)
        {
            v[i] = values[i].add(vector.get(i));
        }
        return new Vector(v);
    }
    
    
    public Vector sub(Vector vector)
    {
        if (vector.getLength() != length)
        {
            return null;
        }
        BigDecimal[] v = new BigDecimal[length];
        for (int i = 0; i < length; i++)
        {
            v[i] = values[i].subtract(vector.get(i));
        }
        return new Vector(v);
    }
    
    
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
    
    
    public Double[] toDouble() {
        
        Double x[] = new Double[ length ];
        for(int i=0; i<=length-1; i++) x[i] = get(i).doubleValue();
        
        return x;
    }
    
    
    public Vector getEquationsValue() {
        
        Vector   f = new Vector( getLength() );
        Double[] x = toDouble();                  // x[0] = x_1 ; x[1] = x_2 ; usw.
        
        f.set(0, BigDecimal.valueOf(     x[0]*x[0] +x[1]*x[1]       +0.6*x[1] -0.16     ).negate());
        f.set(1, BigDecimal.valueOf(     x[0]*x[0] -x[1]*x[1] +x[0] -1.6*x[1] -0.14     ).negate());
        
        return f;
    }
}
