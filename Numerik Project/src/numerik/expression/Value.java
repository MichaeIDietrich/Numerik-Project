package numerik.expression;

import java.math.BigDecimal;

import numerik.calc.*;

public class Value
{
    
    public enum ValueType
    {
        NULL, MATRIX, VECTOR, DECIMAL, VARIABLE, TEXT, BOOLEAN
    }
    
    public static Value EMPTY = new Value();
    
    private BigDecimal decimal = null;
    private Matrix matrix = null;
    private Vector vector = null;
    private Variable variable = null;
    private String text = null;
    private Boolean bool = null;
    
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
    
    public Value(String text)
    {
        this.text = text;
        type = ValueType.TEXT;
    }

    public Value(Boolean bool)
    {
        this.bool = bool;
        type = ValueType.BOOLEAN;
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
    
    public String toText()
    {
        return text;
    }
    
    public Boolean toBoolean()
    {
        return bool;
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
            case TEXT:
                return text;
            case BOOLEAN:
                return bool;
        }
        return null;
    }
    
}
