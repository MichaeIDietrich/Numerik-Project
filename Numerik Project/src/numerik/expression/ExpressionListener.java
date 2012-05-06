package numerik.expression;

public interface ExpressionListener
{
    public enum ActionType
    {
        DO, WHILE, STARTPARSING, BADEXPRESSION, PARSEDEXPRESSION
    }
    
    public void expressionParsed(ExpressionEngine expression, Value result);
    public void actionParsed(ActionType action, String data);
}
