package numerik.expression;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

import numerik.calc.*;
import numerik.expression.Value.ValueType;

public final class ExpressionEngine
{
    
    enum Token
    {
        NONE, EQUAL, UNEQUAL, GREATER, LESS, GREATEREQ, LESSEQ, ASSIGN, PLUS, MINUS, TIMES, DIVISION, POW, 
        LGROUP, RGROUP, KOMMA, FUNCTION, NUMERIC, VARIABLE, LMATRIX, RMATRIX//, LINDEX, RINDEX
    }
    
    private final char END_OF_INPUT = 0;
    
    private static HashMap<Token, Token[]> tokenRelationMap;
    
    private ArrayList<TokenListener> tokenListeners;
    
    private char[] input;
    private int index;
    
    private Token lastToken;
    private BigDecimal lastNumeric;
    private String lastVariable;
    private String lastFunction;
    
    private Value parsedValue;
    
    private String assignedVariable = null;
    
    private MathPool mathPool;
    
    
    static
    {
        tokenRelationMap = new HashMap<Token, Token[]>();
        
        Token[] operands = { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX };
        
        tokenRelationMap.put(Token.NONE, operands);
        
        tokenRelationMap.put(Token.EQUAL, operands);
        tokenRelationMap.put(Token.UNEQUAL, operands);
        tokenRelationMap.put(Token.GREATER, operands);
        tokenRelationMap.put(Token.LESS, operands);
        tokenRelationMap.put(Token.GREATEREQ, operands);
        tokenRelationMap.put(Token.LESSEQ, operands);
        
        tokenRelationMap.put(Token.ASSIGN, operands);
        
        tokenRelationMap.put(Token.PLUS, operands);
        tokenRelationMap.put(Token.MINUS, operands);
        tokenRelationMap.put(Token.TIMES, operands);
        tokenRelationMap.put(Token.DIVISION, operands);
        tokenRelationMap.put(Token.POW, new Token[] { Token.MINUS, Token.LGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE });
        
        tokenRelationMap.put(Token.LGROUP, new Token[] { Token.MINUS, Token.LGROUP, Token.RGROUP, Token.FUNCTION, Token.NUMERIC, Token.VARIABLE, Token.LMATRIX });
        tokenRelationMap.put(Token.RGROUP, new Token[]   { Token.NONE, Token.EQUAL, Token.UNEQUAL, Token.GREATER, Token.LESS, Token.GREATEREQ, Token.LESSEQ, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.RMATRIX });
        tokenRelationMap.put(Token.KOMMA, operands);
        tokenRelationMap.put(Token.FUNCTION, new Token[] { Token.LGROUP });
        tokenRelationMap.put(Token.NUMERIC, new Token[]  { Token.NONE, Token.EQUAL, Token.UNEQUAL, Token.GREATER, Token.LESS, Token.GREATEREQ, Token.LESSEQ, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.RMATRIX });
        tokenRelationMap.put(Token.VARIABLE, new Token[] { Token.NONE, Token.ASSIGN, Token.EQUAL, Token.UNEQUAL, Token.GREATER, Token.LESS, Token.GREATEREQ, Token.LESSEQ, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.RMATRIX });
        tokenRelationMap.put(Token.LMATRIX, operands);
        tokenRelationMap.put(Token.RMATRIX, new Token[]  { Token.NONE, Token.PLUS, Token.MINUS, Token.TIMES, Token.DIVISION, Token.POW, Token.KOMMA, Token.RGROUP, Token.LMATRIX, Token.RMATRIX });
    }
    
    
    public ExpressionEngine()
    {
        tokenListeners = new ArrayList<TokenListener>();
        mathPool = new MathPool();
    }
    
    
    public Value solve(String input) throws InvalidExpressionException
    {
        this.input = new char[input.length() + 1];
        System.arraycopy(input.toCharArray(), 0, this.input, 0, input.length());
        this.input[input.length()] = END_OF_INPUT;
        
        
        index = 0;
        assignedVariable = null;
        lastToken = Token.NONE;
        lastToken = getNextToken();
        
        Value res = expression(0);
        
        return res;
    }
    
    
    public HashMap<String, Value> getVariableTable()
    {
        return mathPool.getVariableTable();
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
    
    
    public boolean isExpressionTrue(String expression) throws InvalidExpressionException
    {
        Value result;
        
        try
        {
            result = solve(expression);
            System.out.println("res: " + result);
        }
        catch (InvalidExpressionException ex)
        {
            return false;
        }
        switch (result.getType())
        {
            case BOOLEAN:
                return result.toBoolean();
                
            case DECIMAL:
                return !result.toDecimal().equals(BigDecimal.ZERO);
                
            case NULL:
                return false;
                
            default:
                throw new InvalidExpressionException("Der Ausdruck \"" + expression + "\" kann nicht als boolscher Wert ausgeweret werden!");
        }
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
        else if (c == '*' || c == '⋅')
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
            while (isAlphaNum(input[++index]));
            
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
        
//        System.out.println("check: " + lastToken + " -> " + nextToken);
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
                    vars.add(mathPool.calculate(vars.poll(), expression(priority + 1), token));
                }
                break;
                
            case 1:
                
                vars.add(expression(priority + 1));
                
                if (lastToken == Token.ASSIGN)
                {
                    
                    lastToken = getNextToken();
                    vars.add(mathPool.resolveVariable(expression(priority + 1)));
                    
                    assignedVariable = vars.peek().toVariable().toString();
                    mathPool.getVariableTable().put(vars.poll().toVariable().toString(), vars.peek());
                    
                }
                else
                {
                    
                    vars.add(mathPool.resolveVariable(vars.poll()));
                }
                break;
            
            case 2:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.PLUS || lastToken == Token.MINUS)
                {
                    
                    token = lastToken;
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority + 1));
                    vars.add(mathPool.calculate(vars.poll(), vars.poll(), token));
                    
                }
                break;
                
            case 3:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.TIMES || lastToken == Token.DIVISION)
                {
                    
                    token = lastToken;
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority + 1));
                    vars.add(mathPool.calculate(vars.poll(), vars.poll(), token));
                    
                }
                break;
                
            case 4:
                
                vars.add(expression(priority + 1));
                
                while (lastToken == Token.POW)
                {
                    
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority + 1));
                    vars.add(mathPool.calculate(vars.poll(), vars.poll(), Token.POW));
                    
                }
                break;
                
            case 5:
                
                if (lastToken == Token.MINUS)
                {
                    
                    lastToken = getNextToken();
                    
                    vars.add(expression(priority));
                    vars.add(mathPool.calculate(vars.poll(), Value.EMPTY, Token.MINUS));
                    
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
                        
                        if (lastToken != Token.RGROUP)
                        {
                            vars.add(expression(1));
                            
                            while (lastToken == Token.KOMMA)
                            {
                                
                                lastToken = getNextToken();
                                
                                vars.add(expression(1));
                            }
                            
                            if (lastToken == Token.RGROUP)
                            {
                                
                            }
                        }
                        
                        lastToken = getNextToken();
                    }
                    Value res = mathPool.callFunction(funcName, vars.toArray(new Value[vars.size()]));
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
    
    
    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }
    
    
    private boolean isAlpha(char c)
    {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }
    
    
    private boolean isAlphaNum(char c)
    {
        return isAlpha(c) || isDigit(c);
    }
}