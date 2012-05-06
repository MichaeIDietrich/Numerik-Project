package numerik.expression;

import numerik.calc.Matrix;
import numerik.calc.Vector;

public interface TokenListener
{
    public void tokenParsed(ExpressionEngine.Token token, Value value);
    public void matrixParsed(Matrix matrix);
    public void vectorParsed(Vector vector);
}
