package numerik.expression;

import java.math.BigDecimal;
import java.util.HashMap;

import numerik.calc.MathLib;
import numerik.expression.ExpressionEngine.Token;
import numerik.expression.Value.ValueType;
import numerik.ui.LatexFormula;
import numerik.ui.Recorder;

public final class MathPool
{
    
    private HashMap<String, Value> variables;
    
    
    public MathPool()
    {
        variables = new HashMap<String, Value>();
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
                    value = new Value(MathLib.round(var1.toDecimal().divide(var2.toDecimal())));
                    
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
        if (funcName.equals("setPrecision"))
        {
            if (args.length == 1 && args[0].getType() == ValueType.DECIMAL)
            {
                MathLib.setPrecision(args[0].toDecimal().intValue());
                return new Value("Genauigkeit auf " + args[0].toDecimal().intValue() + " gesetzt.");
            }
            
            throw new InvalidExpressionException("Bitte Eingabe überprüfen, setPrecision() nimmt als Parameter einen Integer.");
            
        }
        else if (funcName.equals("delete") || funcName.equals("del"))
        {
            
            if (args.length == 1 && args[0].getType() == ValueType.VARIABLE)
            {
                variables.remove(args[0].toVariable().toString());
                return new Value("Variable '" + args[0].toVariable() + "' wurde gelöscht.");
            }
            
            throw new InvalidExpressionException("Bitte Eingabe überprüfen, delete() nimmt als Parameter eine Variable.");
            
        }
        else if (funcName.equals("L"))
        {
            
            if (args.length == 1)
            {
                args[0] = resolveVariable(args[0]);
                
                if (args[0].getType() == ValueType.MATRIX)
                {
                    return new Value(args[0].toMatrix().getL());
                }
                
            }
            
            throw new InvalidExpressionException("Bitte Eingabe überprüfen, L() nimmt als Parameter eine Matrix.");
            
        }
        else if (funcName.equals("U"))
        {
            
            if (args.length == 1)
            {
                args[0] = resolveVariable(args[0]);
                
                if (args[0].getType() == ValueType.MATRIX)
                {
                    return new Value(args[0].toMatrix().getU());
                }
                
            }
            
            throw new InvalidExpressionException("Bitte Eingabe überprüfen, U() nimmt als Parameter eine Matrix.");
        }
        else if (funcName.equals("solve"))
        {
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
        }
        else if (funcName.equals("get"))
        {
            
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
            throw new InvalidExpressionException("Bitte Eingabe überprüfen, get() nimmt als Parameter eine Matrix und zwei Indizes ooder einen Vektor und ein Index.");
        }
        
        // mathematische Standardfunktionen mit double-Genauigkeit
        else if (funcName.equals("ln"))
        {
            args[0] = resolveVariable(args[0]);
            
            double d = Math.log(args[0].toDecimal().doubleValue());
            return new Value(MathLib.round(new BigDecimal(d)));
        }
        else if (funcName.equals("sqrt"))
        {
            args[0] = resolveVariable(args[0]);
            
            double d = Math.sqrt(args[0].toDecimal().doubleValue());
            return new Value(MathLib.round(new BigDecimal(d)));
        }
        else if (funcName.equals("sin"))
        {
            args[0] = resolveVariable(args[0]);
            
            double d = Math.sin(args[0].toDecimal().doubleValue());
            return new Value(MathLib.round(new BigDecimal(d)));
        }
        else if (funcName.equals("cos"))
        {
            args[0] = resolveVariable(args[0]);
            
            double d = Math.cos(args[0].toDecimal().doubleValue());
            return new Value(MathLib.round(new BigDecimal(d)));
        }
        else if (funcName.equals("tan"))
        {
            args[0] = resolveVariable(args[0]);
            
            double d = Math.tan(args[0].toDecimal().doubleValue());
            return new Value(MathLib.round(new BigDecimal(d)));
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
