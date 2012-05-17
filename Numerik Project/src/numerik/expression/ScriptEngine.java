package numerik.expression;

import java.math.BigDecimal;
import java.util.*;

import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.ExpressionEngine.Token;
import numerik.expression.ExpressionListener.ActionType;
import numerik.ui.misc.LatexFormula;

public final class ScriptEngine implements TokenListener
{
    
    ArrayList<ExpressionListener> expressionListeners;
    
    ExpressionEngine expression;
    
    LatexFormula formula;
    
    public ScriptEngine()
    {
        expressionListeners = new ArrayList<ExpressionListener>();
        expression = new ExpressionEngine();
        formula = new LatexFormula();
    }
    
    public void addExpressionListener(ExpressionListener expressionListener)
    {
        expressionListeners.add(expressionListener);
    }
    
    public void removeExpressionListener(ExpressionListener expressionListener)
    {
        expressionListeners.remove(expressionListener);
    }
    
    public void run(String script)
    {
        Queue<Integer> loopIndices = new LinkedList<Integer>();
        String[] lines = script.split("\n");
        
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++)
        {
            String line = lines[lineIndex];
            
            System.out.println("Line: " + line);
            if (line.equals("\n") || line.equals("") || line.startsWith("#") || line.startsWith("\n#"))
                continue;
            
            if (line.equals("do"))
            {
                if (!loopIndices.contains(lineIndex))
                {
                    loopIndices.add(lineIndex);
                    for (ExpressionListener listener : expressionListeners)
                    {
                        listener.actionParsed(ActionType.DO, "Do");
                    }
                }
                continue;
            }
            if (line.startsWith("while "))
            {
                String var = line.substring(6);
                
                if (expression.getVariableTable().get(var) != null && !expression.getVariableTable().get(var).toDecimal().equals(BigDecimal.ZERO))
                {
                    lineIndex = loopIndices.peek() - 1;
                }
                else
                {
                    loopIndices.poll();
                    for (ExpressionListener listener : expressionListeners)
                    {
                        listener.actionParsed(ActionType.WHILE, "While");
                    }
                }
                continue;
            }
            
            for (ExpressionListener listener : expressionListeners)
            {
                listener.actionParsed(ActionType.STARTPARSING, line);
            }
            
            Value res;
            
            try
            {
                formula.clear();
                res = expression.solve(line);
                
                for (ExpressionListener listener : expressionListeners)
                {
                    listener.actionParsed(ActionType.PARSEDEXPRESSION, formula.toString());
                }
                for (ExpressionListener listener : expressionListeners)
                {
                    listener.expressionParsed(expression, res);
                }
            }
            catch (InvalidExpressionException ex)
            {
                for (ExpressionListener listener : expressionListeners)
                {
                    listener.actionParsed(ActionType.BADEXPRESSION, ex.getMessage());
                }
            }
        }
    }
    
    @Override
    public void tokenParsed(Token token, Value value)
    {
        switch (token)
        {
            case EQUAL:
                formula.addLatexString("=");
                break;
            case UNEQUAL:
                formula.addLatexString("\\neq");
                break;
            case GREATER:
                formula.addLatexString(">");
                break;
            case LESS:
                formula.addLatexString("<");
                break;
            case GREATEREQ:
                formula.addLatexString("\\ge");
                break;
            case LESSEQ:
                formula.addLatexString("\\le");
                break;
            case ASSIGN:
                formula.addLatexString(":=");
                break;
            case MINUS:
                formula.addLatexString("-");
                break;
            case PLUS:
                formula.addLatexString("+");
                break;
            case TIMES:
                formula.addLatexString("\\cdot");
                break;
            case DIVISION:
                formula.addLatexString(":");
                break;
            case POW:
                formula.addLatexString("^");
                break;
            case LGROUP:
                formula.addLatexString("(");
                break;
            case RGROUP:
                formula.addLatexString(")");
                break;
            case KOMMA:
                formula.addLatexString(",");
                break;
            case NUMERIC:
                formula.addLatexString(value.toDecimal().toPlainString());
                break;
            case FUNCTION:
                formula.addLatexString(value.toText());
                break;
            case VARIABLE:
                formula.addLatexString(value.toText());
                break;
        }
    }
    
    @Override
    public void matrixParsed(Matrix matrix)
    {
        formula.addMatrix(matrix);
    }
    
    @Override
    public void vectorParsed(Vector vector)
    {
        formula.addVector(vector);
    }
}
