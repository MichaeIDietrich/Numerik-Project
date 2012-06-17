package numerik.expression;

import java.math.*;
import java.util.HashMap;

import numerik.calc.MathLib;
import numerik.expression.ExpressionEngine.Token;
import numerik.expression.Value.ValueType;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;

public final class MathPool
{
    
    public static final String[] FUNCTIONS = { "getPrecision", "setPrecision", "del", "delete", "det", "determinant", "L", "U", "solve", "get", "ln", 
        "sqrt", "sin", "cos", "tan", "asin", "acos", "atan", "deg", "rad" };
    
    
    private static final BigDecimal PI = new BigDecimal("3.14159265358979");
    
    private HashMap<String, Value> variables;
    
    
    public MathPool()
    {
        variables = new HashMap<String, Value>();
        variables.put("PI", new Value(PI));
    }
    
    
    public HashMap<String, Value> getVariableTable()
    {
        return variables;
    }
    
    
    public Value calculate(Value var1, Value var2, Token operation) throws InvalidExpressionException
    {
        LatexFormula calcSteps = new LatexFormula();
        
        var1 = resolveVariable(var1);
        var2 = resolveVariable(var2);
        
        Value value;
        
        if (var1.getType() == ValueType.DECIMAL && var2.getType() == ValueType.DECIMAL)
        {
            var1 = new Value(MathLib.stripTrailingZeros(var1.toDecimal()));
            var2 = new Value(MathLib.stripTrailingZeros(var2.toDecimal()));
            
            switch (operation)
            {
                case EQUAL:
                    return new Value(var1.toDecimal().equals(var2.toObject()));
                    
                case UNEQUAL:
                    return new Value(!var1.toDecimal().equals(var2.toObject()));
                    
                case GREATER:
                    return new Value(var1.toDecimal().compareTo(var2.toDecimal()) == 1);
                    
                case LESS:
                    return new Value(var1.toDecimal().compareTo(var2.toDecimal()) == -1);
                    
                case GREATEREQ:
                    return new Value(var1.toDecimal().compareTo(var2.toDecimal()) == 1 || var1.toDecimal().equals(var2.toObject()));
                    
                case LESSEQ:
                    return new Value(var1.toDecimal().compareTo(var2.toDecimal()) == -1 || var1.toDecimal().equals(var2.toObject()));
                    
                case PLUS:
                    value = new Value(MathLib.round(var1.toDecimal().add(var2.toDecimal())));
                    
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + "+" + var2.toDecimal().toPlainString() + "=");
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
                case MINUS:
                    value = new Value(MathLib.round(var1.toDecimal().subtract(var2.toDecimal())));
                    
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + "-" + var2.toDecimal().toPlainString() + "=");
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
                case TIMES:
                    value = new Value(MathLib.round(var1.toDecimal().multiply(var2.toDecimal())));
                    
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + "⋅" + var2.toDecimal().toPlainString() + "=");
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
                case DIVISION:
                    value = new Value(MathLib.round(var1.toDecimal().divide(var2.toDecimal(), MathLib.getPrecision(), RoundingMode.HALF_UP)));
                    
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + ":" + var2.toDecimal().toPlainString() + "=");
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
                case POW:
                    value = new Value(MathLib.round(var1.toDecimal().pow(var2.toDecimal().intValue())));
                    
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + "\\^" + var2.toDecimal().toPlainString() + "=");
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
            }
            
        }
        else if (var1.getType() == ValueType.MATRIX && var2.getType() == ValueType.MATRIX)
        {
            
            switch (operation)
            {
                case PLUS:
                    value = new Value(var1.toMatrix().add(var2.toMatrix()));
                    
                    calcSteps.addMatrix(var1.toMatrix()).addText("+").addMatrix(var2.toMatrix()).addLatexString("=");
                    calcSteps.addMatrix(value.toMatrix()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
                case MINUS:
                    value = new Value(var1.toMatrix().add(var2.toMatrix().mult(new BigDecimal(-1))));
                    
                    calcSteps.addMatrix(var1.toMatrix()).addText("-").addMatrix(var2.toMatrix()).addLatexString("=");
                    calcSteps.addMatrix(value.toMatrix()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
                case TIMES:
                    value = new Value(var1.toMatrix().mult(var2.toMatrix()));
                    
                    calcSteps.addMatrix(var1.toMatrix()).addText("⋅").addMatrix(var2.toMatrix()).addLatexString("=");
                    calcSteps.addMatrix(value.toMatrix()).addNewLine();
                    Recorder.getInstance().add(calcSteps);
                    
                    return value;
                    
            }
            
        }
        else if (var1.getType() == ValueType.DECIMAL && var2.getType() == ValueType.NULL)
        {
            
            switch (operation)
            {
                case MINUS:
                    return new Value(var1.toDecimal().negate());
            }
            
        }
        else if (var1.getType() == ValueType.MATRIX && var2.getType() == ValueType.NULL)
        {
            
            switch (operation)
            {
                case TIMES:
                    return new Value(var1.toMatrix().mult(new BigDecimal(-1)));
            }
            
        }
        else if (var1.getType() == ValueType.DECIMAL && var2.getType() == ValueType.MATRIX)
        {
            
            switch (operation)
            {
                case TIMES:
                    return new Value(var2.toMatrix().mult(var1.toDecimal()));
                    
                case DIVISION:
                    return new Value(var2.toMatrix().getInverse().mult(var2.toDecimal()));
                    
            }
            
        }
        else if (var1.getType() == ValueType.MATRIX && var2.getType() == ValueType.DECIMAL)
        {
            
            switch (operation)
            {
                case TIMES:
                    return new Value(var1.toMatrix().mult(var2.toDecimal()));
                    
                case DIVISION:
                    return new Value(var1.toMatrix().mult(BigDecimal.ONE.divide(var2.toDecimal())));
                    
                case POW:
                    if (!var2.toDecimal().equals(BigDecimal.ONE.negate()))
                    {
                        throw new InvalidExpressionException("Momentan ist nur -1 als Exponent für Matrizen implementiert.");
                    }
                    return new Value(var1.toMatrix().getInverse());
                    
            }
            
        }
        
        throw new InvalidExpressionException("Ungültige Operation.");
    }
    
    
    public Value callFunction(String funcName, Value... args) throws InvalidExpressionException
    {
//        System.out.println("Funktion: " + funcName);
        switch (funcName)
        {
            case "getPrecision":
                if (args.length == 0)
                {
                    return new Value(new BigDecimal(MathLib.getPrecision()));
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, getPrecision() nimmt keine Parameter.");
                
            case "setPrecision":
                if (args.length == 1 && args[0].getType() == ValueType.DECIMAL)
                {
                    MathLib.setPrecision(args[0].toDecimal().intValue());
                    return new Value("Genauigkeit auf " + args[0].toDecimal().intValue() + " gesetzt.");
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, setPrecision() nimmt als Parameter einen Integer.");
                
            case "delete":
            case "del":
                if (args.length == 1 && args[0].getType() == ValueType.VARIABLE)
                {
                    variables.remove(args[0].toVariable().toString());
                    return new Value("Variable '" + args[0].toVariable() + "' wurde gelöscht.");
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, delete() nimmt als Parameter eine Variable.");
                
            case "determinant":
            case "det":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.MATRIX)
                    {
                        return new Value(args[0].toMatrix().getDeterminant());
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, determinant() nimmt als Parameter eine Matrix.");
                
            case "L":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.MATRIX)
                    {
                        return new Value(args[0].toMatrix().getL());
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, L() nimmt als Parameter eine Matrix.");
                
            case "U":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.MATRIX)
                    {
                        return new Value(args[0].toMatrix().getU());
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, U() nimmt als Parameter eine Matrix.");
                
            case "solve":
                if (args.length == 2)
                {
                    args[0] = resolveVariable(args[0]);
                    args[1] = resolveVariable(args[1]);
                    
                    if (args[0].getType() == ValueType.MATRIX && args[1].getType() == ValueType.VECTOR)
                    {
                        return new Value(args[0].toMatrix().solveX(args[1].toVector()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, solve() nimmt als Parameter eine Matrix und einen Vektor.");
                
            case "get":
                if (args.length == 3)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.MATRIX && args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(args[0].toMatrix().get(args[1].toDecimal().intValue(), args[2].toDecimal().intValue()));
                    }
                }
                else if (args.length == 2)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.VECTOR && args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(args[0].toVector().get(args[1].toDecimal().intValue()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, get() nimmt als Parameter eine Matrix und zwei Indizes oder einen Vektor und ein Index.");
                
                
              //*********************************************************//
             // mathematische Standardfunktionen mit double-Genauigkeit //
            //*********************************************************//
            case "ln":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.ln(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, ln() nimmt als Parameter eine Dezimalzahl.");
                
            case "sqrt":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.sqrt(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, sqrt() nimmt als Parameter eine Dezimalzahl.");
                
            case "sin":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.sin(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, sin() nimmt als Parameter eine Dezimalzahl.");
                
            case "cos":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.cos(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, cos() nimmt als Parameter eine Dezimalzahl.");
                
            case "tan":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.tan(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, tan() nimmt als Parameter eine Dezimalzahl.");
                
            case "asin":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.asin(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, asin() nimmt als Parameter eine Dezimalzahl.");
                
            case "acos":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.acos(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, acos() nimmt als Parameter eine Dezimalzahl.");
                
            case "atan":
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(MathLib.atan(args[0].toDecimal()));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, atan() nimmt als Parameter eine Dezimalzahl.");
                
            case "deg":
                
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(args[0].toDecimal().multiply(new BigDecimal(180)).divide(PI, MathLib.getPrecision(), RoundingMode.HALF_UP));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, deg() nimmt als Parameter eine Radianten (Dezimalzahl).");
                
            case "rad":
                
                if (args.length == 1)
                {
                    args[0] = resolveVariable(args[0]);
                    
                    if (args[0].getType() == ValueType.DECIMAL)
                    {
                        return new Value(args[0].toDecimal().multiply(PI).divide(new BigDecimal(180), MathLib.getPrecision(), RoundingMode.HALF_UP));
                    }
                }
                
                throw new InvalidExpressionException("Bitte Eingabe überprüfen, deg() nimmt als Parameter eine Gradzahl (Dezimalzahl).");
        }
        
        
        //schauen, ob es sich um eine Matrix oder einen Vektor handelt und die Parameter als Indizes interpretieren 
        if (variables.containsKey(funcName))
        {
            if (variables.get(funcName).getType() == ValueType.MATRIX && args.length == 2)
            {
                args[0] = resolveVariable(args[0]);
                args[1] = resolveVariable(args[1]);
                
                if (args[0].getType() == ValueType.DECIMAL && args[1].getType() == ValueType.DECIMAL)
                {
                    return new Value(variables.get(funcName).toMatrix().get(
                            args[0].toDecimal().intValue(), args[1].toDecimal().intValue()));
                }
            }
            
            if (variables.get(funcName).getType() == ValueType.MATRIX && args.length == 1)
            {
                args[0] = resolveVariable(args[0]);
                
                if (args[0].getType() == ValueType.DECIMAL)
                {
                    return new Value(variables.get(funcName).toVector().get(args[0].toDecimal().intValue()));
                }
            }
        }
        
        throw new InvalidExpressionException("Funktion '" + funcName + "' existiert nicht.");
    }
    
    
    public Value resolveVariable(Value var)
    {
        if (var.getType() == ValueType.VARIABLE)
        {
            if (!variables.containsKey(var.toVariable().toString())) 
            {
                return new Value("undefiniert");
            }
            return variables.get(var.toVariable().toString());
        }
        return var;
    }
}