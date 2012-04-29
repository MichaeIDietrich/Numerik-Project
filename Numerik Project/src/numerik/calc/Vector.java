package numerik.calc;

import java.math.BigDecimal;
import java.util.Arrays;

import numerik.calc.Matrix;

public final class Vector
{
    
    private boolean transposed;
    private int length;
    
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
        this.values = values;
        this.transposed = transposed;
        this.length = values.length;
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
        
        for (int i = 0; i < length; i++)
        {
            copy.set(i, get(i));
        }
        return copy;
    }
    
    
}
