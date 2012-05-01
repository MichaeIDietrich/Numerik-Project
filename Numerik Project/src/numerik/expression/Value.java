package numerik.expression;

import java.math.BigDecimal;

import numerik.calc.*;

public class Value
{
    
    public enum ValueType
    {
        NULL, MATRIX, VECTOR, DECIMAL, VARIABLE, STRING
    }
    
    public static Value EMPTY = new Value();
    
    private BigDecimal decimal = null;
    private Matrix matrix = null;
    private Vector vector = null;
    private Variable variable = null;
    private String string = null;
    
    private ValueType type = ValueType.NULL;
    
    public Value()
    {
        type = ValueType.NULL;
    }
    
    public Value(Matrix matrix)
    {
        this.matrix = matrix;
        type = ValueType.MATRIX;
    }
    
    public Value(Vector vector)
    {
        this.vector = vector;
        type = ValueType.VECTOR;
    }
    
    public Value(BigDecimal decimal)
    {
        this.decimal = decimal;
        type = ValueType.DECIMAL;
    }
    
    public Value(Variable variable)
    {
        this.variable = variable;
        type = ValueType.VARIABLE;
    }
    
    public Value(String string)
    {
        this.string = string;
        type = ValueType.STRING;
    }
    
    public ValueType getType()
    {
        return type;
    }
    
    public Matrix toMatrix()
    {
        return matrix;
    }
    
    public Vector toVector()
    {
        return vector;
    }
    
    public BigDecimal toDecimal()
    {
        return decimal;
    }
    
    public Variable toVariable()
    {
        return variable;
    }
    
    public String toString()
    {
        return string;
    }
    
    public Object toObject()
    {
        switch (type)
        {
            case MATRIX:
                return matrix;
            case VECTOR:
                return vector;
            case DECIMAL:
                return decimal;
            case VARIABLE:
                return variable;
            case STRING:
                return string;
        }
        return null;
    }
}
