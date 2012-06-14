package numerik.expression_test;

import static org.junit.Assert.*;

import numerik.calc.MathLib;
import numerik.expression.*;

import org.junit.Test;

public class Expression_Testfixture
{
    
    @Test
    public void easyFormula() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "33*4-4*2+5/2";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        assertEquals("126.5", res.toDecimal().toPlainString());
    }
    
    
    @Test
    public void easyFormula2() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "27/5*3^6+5+5-9/10";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        assertEquals("3945.7", res.toDecimal().toPlainString());
    }
    
    
    @Test
    public void tan7_5() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "tan(7.5)";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        assertEquals("2.70601386677", res.toDecimal().toPlainString());
    }
    
    
    @Test
    public void cos180() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "cos(rad(180))";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        assertEquals("-1", res.toDecimal().toPlainString());
    }
    
    
    @Test
    public void sin3_5_mult_cos6_8() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "sin(PI/2)*cos(2*PI)";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        assertEquals("1", res.toDecimal().toPlainString());
    }
    
    
    @Test
    public void matrix_inverse() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "[[4,2],[8,8]]^-1";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        // '[' und ']' müssen an dieser Stelle wohl scheinbar ersetzt werden, da 
        // JUnit.assertEquals eckige Klammern als fehlerhaften Stringvergleich zu 
        // interpretieren scheint
        assertEquals("((0.5,-0.125),(-0.5,0.25))", res.toMatrix().toString().replace('[', '(').replace(']', ')'));
    }
    
    
    @Test
    public void matrix_multiplication() throws InvalidExpressionException
    {
        MathLib.setPrecision(12);
        String input = "[[4,2],[8,8]]*[[5,5],[1,3]]";
        ExpressionEngine engine = new ExpressionEngine();
        
        Value res = engine.solve(input);
        
        // '[' und ']' müssen an dieser Stelle wohl scheinbar ersetzt werden, da 
        // JUnit.assertEquals eckige Klammern als fehlerhaften Stringvergleich zu 
        // interpretieren scheint
        assertEquals("((22,26),(48,64))", res.toMatrix().toString().replace('[', '(').replace(']', ')'));
    }

}
