package numerik.expression;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

import numerik.calc.*;
import numerik.expression.Value.ValueType;
import numerik.ui.*;

public class ExpressionEngine
{
    
    enum Token
    {
        NONE, EQUAL, UNEQUAL, GREATER, LESS, GREATEREQ, LESSEQ, ASSIGN, PLUS, MINUS, TIMES, DIVISION, POW, LGROUP, RGROUP, KOMMA, FUNCTION, NUMERIC, VARIABLE, LMATRIX, RMATRIX
    }
    
    private final char END_OF_INPUT = 0;
    
    private ArrayList<TokenListener> tokenListeners;
    
    private static HashMap<Token, Token[]> tokenRelationMap;
    private HashMap<String, Value> variables;
    
    private char[] input;
    private int index;
    
    private Token lastToken;
    private BigDecimal lastNumeric;
    private String lastVariable;
    private String lastFunction;
    
    private Value parsedValue;
    
    private String assignedVariable = null;
    
    private LatexFormula calcSteps;
    
    static
    {
        tokenRelationMap = new HashMap<Token, Token[]>();
        
        tokenRelationMap.put(Token.NONE, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        
        tokenRelationMap.put(Token.EQUAL, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.UNEQUAL, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.GREATER, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.LESS, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.GREATEREQ, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.LESSEQ, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        
        tokenRelationMap.put(Token.ASSIGN, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.PLUS, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.MINUS, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.TIMES, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.DIVISION, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.POW, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE });
        tokenRelationMap.put(Token.LGROUP, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.RGROUP, new Token[] { Token.NONE, Token.EQUAL, Token.UNEQUAL, Token.GREATER, Token.LESS, Token.GREATEREQ, Token.LESSEQ, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.RMATRIX });
        tokenRelationMap.put(Token.KOMMA, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.FUNCTION, new Token[] { Token.LGROUP });
        tokenRelationMap.put(Token.NUMERIC, new Token[] { Token.NONE, Token.EQUAL, Token.UNEQUAL, Token.GREATER, Token.LESS, Token.GREATEREQ, Token.LESSEQ, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.RMATRIX });
        tokenRelationMap.put(Token.VARIABLE, new Token[] { Token.NONE, Token.ASSIGN, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.RMATRIX });
        tokenRelationMap.put(Token.LMATRIX, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.RMATRIX, new Token[] { Token.NONE, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.LMATRIX, Token.RMATRIX });
    }
    
    public ExpressionEngine()
    {
        tokenListeners = new ArrayList<TokenListener>();
        variables = new HashMap<String, Value>();
        calcSteps = new LatexFormula("");
    }
    
    public Value solve(String input) throws InvalidExpressionException
    {
        this.input = new char[input.length() + 1];
        System.arraycopy(input.toCharArray(), 0, this.input, 0, input.length());
        this.input[input.length()] = END_OF_INPUT;
        
        calcSteps.clear();
        calcSteps.startAlignedEquation();
        
        index = 0;
        assignedVariable = null;
        lastToken = Token.NONE;
        lastToken = getNextToken();
        
        Value res = expression(0);
        calcSteps.endAlignedEquation();
        Recorder.getInstance().add(calcSteps);
        
        return res;
    }
    
    public HashMap<String, Value> getVariableTable()
    {
        return variables;
    }
    
    public String getAssignedVariable()
    {
        return assignedVariable;
    }
    
    public void addTokenListener(TokenListener tokenListener)
    {
        tokenListeners.add(tokenListener);
    }
    
    public void removeTokenListener(TokenListener tokenListener)
    {
        tokenListeners.remove(tokenListener);
    }
    
    private Token parseNextToken() throws InvalidExpressionException
    {
        parsedValue = null;
        
        if (input[index] == END_OF_INPUT)
        {
            return Token.NONE;
        }
        
        while (input[index] == ' ' || input[index] == 10 || input[index] == 13)
        {
            index++;
            
            if (input[index] == END_OF_INPUT)
            {
                return Token.NONE;
            }
        }
        
        char c = input[index];
        
        if (c == '=')
        {
            if (input[++index] == '=')
            {
                index++;
                return Token.EQUAL;
            }
            return Token.ASSIGN;
        }
        if (c == '!')
        {
            if (input[++index] == '=')
            {
                index++;
                return Token.UNEQUAL;
            }
        }
        if (c == '>')
        {
            if (input[++index] == '=')
            {
                index++;
                return Token.GREATEREQ;
            }
            return Token.GREATER;
        }
        if (c == '<')
        {
            if (input[++index] == '=')
            {
                index++;
                return Token.LESSEQ;
            }
            return Token.LESS;
        }
        if (c == '+')
        {
            index++;
            return Token.PLUS;
        }
        else if (c == '-')
        {
            index++;
            return Token.MINUS;
        }
        else if (c == '*')
        {
            index++;
            return Token.TIMES;
        }
        else if (c == '/')
        {
            index++;
            return Token.DIVISION;
        }
        else if (c == '(')
        {
            index++;
            return Token.LGROUP;
        }
        else if (c == ')')
        {
            index++;
            return Token.RGROUP;
        }
        else if (c == ',')
        {
            index++;
            return Token.KOMMA;
        }
        else if (c == '^')
        {
            index++;
            return Token.POW;
        }
        else if (c == '[')
        {
            index++;
            return Token.LMATRIX;
        }
        else if (c == ']')
        {
            index++;
            return Token.RMATRIX;
        }
        else if (isDigit(c))
        {
            int numBegin = index;
            while (isDigit(input[++index]));
            
            if (input[index] == '.')
            {
                while (isDigit(input[++index]));
            }
            
            if (input[index] == 'e' || input[index] == 'E')
            {
                if (input[++index] == '-')
                {
                    index++;
                }
                
                if (!isDigit(input[index]))
                {
                    throw new InvalidExpressionException("Fehlerhafter Dezimalwert: Exponent fehlt.");
                }
                while (isDigit(input[++index]));
            }
            
            lastNumeric = new BigDecimal(new String(input, numBegin, index - numBegin));
            parsedValue = new Value(new BigDecimal(new String(input, numBegin, index - numBegin)));
            return Token.NUMERIC;
            
        }
        else if (isAlpha(c))
        {
            
            int strBegin = index;
            while (isAlpha(input[++index]));
            
            if (input[index] == '(')
            {
                lastFunction = new String(input, strBegin, index - strBegin);
                parsedValue = new Value(new String(input, strBegin, index - strBegin));
                return Token.FUNCTION;
            }
            
            lastVariable = new String(input, strBegin, index - strBegin);
            parsedValue = new Value(new String(input, strBegin, index - strBegin));
            return Token.VARIABLE;
            
        }
        else
        {
            throw new InvalidExpressionException("Unbekanntes Zeichen: " + input[index] + "(" + (int) input[index] + ").");
        }
    }
    
    private Token getNextToken() throws InvalidExpressionException
    {
        Token nextToken = parseNextToken();
        
        System.out.println("check: " + lastToken + " -> " + nextToken);
        for (Token token : tokenRelationMap.get(lastToken))
        {
            if (token == nextToken)
            {
                for (TokenListener tokenListener : tokenListeners)
                {
                    tokenListener.tokenParsed(nextToken, parsedValue);
                }
                return nextToken;
            }
        }
        
        throw new InvalidExpressionException("Ungültige Zeichenfolge: " + nextToken + " darf nicht auf " + lastToken + " folgen.");
    }
    
    private Value expression(int priority) throws InvalidExpressionException
    {
        Queue<Value> vars = new LinkedList<Value>();
        Token token;
        
        switch (priority)
        {
            case 0:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.EQUAL || lastToken == Token.GREATER || lastToken == Token.LESS ||
                       lastToken == Token.UNEQUAL || lastToken == Token.GREATEREQ || lastToken == Token.LESSEQ)
                {
                    token = lastToken;
                    
                    lastToken = getNextToken();
                    vars.add(calc(vars.poll(), expression(priority + 1), token));
                }
                break;
                
            case 1:
                
                vars.add(expression(priority + 1));
                
                if (lastToken == Token.ASSIGN)
                {
                    
                    lastToken = getNextToken();
                    vars.add(resolveVariable(expression(priority + 1)));
                    
                    assignedVariable = vars.peek().toVariable().toString();
                    variables.put(vars.poll().toVariable().toString(), vars.peek());
                    
                }
                else
                {
                    
                    vars.add(resolveVariable(vars.poll()));
                }
                break;
            
            case 2:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.PLUS || lastToken == Token.MINUS)
                {
                    
                    token = lastToken;
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority + 1));
                    vars.add(calc(vars.poll(), vars.poll(), token));
                    
                }
                break;
                
            case 3:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.TIMES || lastToken == Token.DIVISION)
                {
                    
                    token = lastToken;
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority + 1));
                    vars.add(calc(vars.poll(), vars.poll(), token));
                    
                }
                break;
                
            case 4:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.POW)
                {
                    
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority + 1));
                    vars.add(calc(vars.poll(), vars.poll(), Token.POW));
                    
                }
                break;
                
            case 5:
                
                if (lastToken == Token.MINUS)
                {
                    
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority));
                    vars.add(calc(vars.poll(), Value.EMPTY, Token.MINUS));
                    
                }
                else if (lastToken == Token.NUMERIC)
                {
                    
                    lastToken = getNextToken();
                    
                    vars.add(new Value(lastNumeric));
                    
                }
                else if (lastToken == Token.VARIABLE)
                {
                    
                    lastToken = getNextToken();
                    vars.add(new Value(new Variable(lastVariable)));
                    
                }
                else if (lastToken == Token.LGROUP)
                {
                    
                    lastToken = getNextToken();
                    
                    vars.add(expression(1));
                    
                    while (lastToken == Token.KOMMA)
                    {
                        
                        lastToken = getNextToken();
                        
                        vars.add(expression(1));
                        
                    }
                    
                    if (lastToken != Token.RGROUP)
                    {
                        
                        throw new InvalidExpressionException("Klammer zu fehlt.");
                    }
                    lastToken = getNextToken(); // müsste, glaube ich, im else
                                                // stehen
                    
                }
                else if (lastToken == Token.LMATRIX)
                {
                    
                    ArrayList<BigDecimal> values = new ArrayList<BigDecimal>();
                    
                    lastToken = getNextToken();
                    
                    if (lastToken == Token.LMATRIX)
                    {
                        
                        int rows = 0;
                        
                        while (lastToken == Token.LMATRIX)
                        {
                            
                            rows++;
                            
                            lastToken = getNextToken();
                            
                            values.addAll(fetchArgs());
                            
                            if (lastToken != Token.RMATRIX)
                            {
                                
                                throw new InvalidExpressionException("Klammer zu fehlt.");
                            }
                            lastToken = getNextToken(); // müsste, glaube ich,
                                                        // im else stehen
                            
                            if (lastToken == Token.KOMMA) { // optionales Komma
                                lastToken = getNextToken();
                            }
                        }
                        
                        parsedValue = new Value(new Matrix(values.toArray(new BigDecimal[values.size()]), values.size() / rows));
                        for (TokenListener tokenListener : tokenListeners)
                        {
                            tokenListener.matrixParsed(parsedValue.toMatrix());
                        }
                        vars.add(parsedValue);
                        
                    }
                    else
                    {
                        
                        values.addAll(fetchArgs());
                        
                        parsedValue = new Value(new Vector(values.toArray(new BigDecimal[values.size()])));
                        for (TokenListener tokenListener : tokenListeners)
                        {
                            tokenListener.vectorParsed(parsedValue.toVector());
                        }
                        vars.add(parsedValue);
                    }
                    
                    if (lastToken != Token.RMATRIX)
                    {
                        
                        throw new InvalidExpressionException("Eckige Klammer zu fehlt.");
                    }
                    lastToken = getNextToken(); // müsste, glaube ich, im else
                                                // stehen
                    
                }
                else if (lastToken == Token.FUNCTION)
                {
                    
                    String funcName = lastFunction;
                    
                    lastToken = getNextToken();
                    
                    if (lastToken == Token.LGROUP)
                    {
                        lastToken = getNextToken();
                        
                        vars.add(expression(1));
                        
                        while (lastToken == Token.KOMMA)
                        {
                            
                            lastToken = getNextToken();
                            
                            vars.add(expression(1));
                        }
                        
                        if (lastToken == Token.RGROUP)
                        {
                            
                        }
                        lastToken = getNextToken();
                    }
                    Value res = callFunc(funcName, vars.toArray(new Value[vars.size()]));
                    vars.clear();
                    vars.add(res);
                    
                }
                break;
        
        }
        return vars.poll();
    }
    
    private ArrayList<BigDecimal> fetchArgs() throws InvalidExpressionException
    {
        
        ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
        
        Value res = expression(1);
        if (res.getType() == ValueType.DECIMAL)
        {
            
            list.add(res.toDecimal());
        }
        else
        {
            throw new InvalidExpressionException("Nur Dezimalzahlen sind als Inhalt von Matrizen zulässig.");
        }
        
        while (lastToken == Token.KOMMA)
        {
            
            lastToken = getNextToken();
            
            res = expression(1);
            if (res.getType() == ValueType.DECIMAL)
            {
                
                list.add(res.toDecimal());
            }
            else
            {
                throw new InvalidExpressionException("Nur Dezimalzahlen sind als Inhalt von Matrizen zulässig.");
            }
        }
        
        return list;
    }
    
    private Value calc(Value var1, Value var2, Token operation) throws InvalidExpressionException
    {
        
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
                    return new Value(var1.toDecimal().compareTo(var2.toDecimal()) == 1 ||
                                     var1.toDecimal().equals(var2.toObject()));
                    
                case LESSEQ:
                    return new Value(var1.toDecimal().compareTo(var2.toDecimal()) == -1||
                                     var1.toDecimal().equals(var2.toObject()));
                    
                case PLUS:
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + " + " + var2.toDecimal().toPlainString() + " &=& ");
                    value = new Value(MathLib.round(var1.toDecimal().add(var2.toDecimal())));
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    return value;
                    
                case MINUS:
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + " - " + var2.toDecimal().toPlainString() + " &=& ");
                    value = new Value(MathLib.round(var1.toDecimal().subtract(var2.toDecimal())));
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    return value;
                    
                case TIMES:
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + " ⋅ " + var2.toDecimal().toPlainString() + " &=& ");
                    value = new Value(MathLib.round(var1.toDecimal().multiply(var2.toDecimal())));
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    return value;
                    
                case DIVISION:
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + " : " + var2.toDecimal().toPlainString() + " &=& ");
                    value = new Value(MathLib.round(var1.toDecimal().divide(var2.toDecimal())));
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    return value;
                    
                case POW:
                    calcSteps.addLatexString(var1.toDecimal().toPlainString() + " ^ " + var2.toDecimal().toPlainString() + " &=& ");
                    value = new Value(MathLib.round(var1.toDecimal().pow(var2.toDecimal().intValue())));
                    calcSteps.addText(value.toDecimal().toPlainString()).addNewLine();
                    return value;
                    
            }
            
        }
        else if (var1.getType() == ValueType.MATRIX && var2.getType() == ValueType.MATRIX)
        {
            
            switch (operation)
            {
                case PLUS:
                    calcSteps.addMatrix(var1.toMatrix()).addText(" + ").addMatrix(var2.toMatrix()).addLatexString(" &=& ");
                    value = new Value(var1.toMatrix().add(var2.toMatrix()));
                    calcSteps.addMatrix(value.toMatrix()).addNewLine();
                    return value;
                    
                case MINUS:
                    calcSteps.addMatrix(var1.toMatrix()).addText(" - ").addMatrix(var2.toMatrix()).addLatexString(" &=& ");
                    value = new Value(var1.toMatrix().add(var2.toMatrix().mult(new BigDecimal(-1))));
                    calcSteps.addMatrix(value.toMatrix()).addNewLine();
                    return value;
                    
                case TIMES:
                    calcSteps.addMatrix(var1.toMatrix()).addText(" ⋅ ").addMatrix(var2.toMatrix()).addLatexString(" &=& ");
                    value = new Value(var1.toMatrix().mult(var2.toMatrix()));
                    calcSteps.addMatrix(value.toMatrix()).addNewLine();
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
    
    private Value callFunc(String funcName, Value... args) throws InvalidExpressionException
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
    
    private Value resolveVariable(Value var)
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
    
    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }
    
    private boolean isAlpha(char c)
    {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }
    
}