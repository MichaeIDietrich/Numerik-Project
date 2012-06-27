package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.MathLib;
import numerik.expression.*;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;

public final class GaussIntegrationOrder4Expr implements Task
{
    private TaskPane taskPane;
    private LatexFormula     formula = new LatexFormula();
    private LatexFormula iterformula = new LatexFormula();
    
    private BigDecimal[] stuetzstelle = { new BigDecimal("-0.8611363115940526"),
                                          new BigDecimal("-0.33998104358485626"), 
                                          new BigDecimal("0.3399810435848563"), 
                                          new BigDecimal("0.8611363115940526") 
    };
    private BigDecimal[] gewicht      = { new BigDecimal("0.3478548451374538"),
                                          new BigDecimal("0.6521451548625461"), 
                                          new BigDecimal("0.6521451548625461"), 
                                          new BigDecimal("0.3478548451374538") 
    };
    
    private String function;
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        
        taskPane.createJToolBarByArguments(
                new Argument("Funktion:", ArgType.EXPRESSION, "((1-6⋅pow(E,-x))-(1-6⋅pow(E,-2)))/pow((x-2)⋅(x-2),1.0/3.0)", 450),
                new Argument("obere Grenze:",   ArgType.DECIMAL, "10"), 
                new Argument("untere Grenze:",  ArgType.DECIMAL, "6"), 
                new Argument("Mantissenlänge:", ArgType.DOUBLEPRECISION, "5"), Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... values)
    {
        MathLib.setPrecision( values[3].toDecimal().intValue() );
        
        BigDecimal   sum = BigDecimal.ZERO;
        BigDecimal fwert = BigDecimal.ZERO;
        BigDecimal     b = values[1].toDecimal();
        BigDecimal     a = values[2].toDecimal();
        
        BigDecimal ba2 = MathLib.round( (b.subtract(a)).multiply( new BigDecimal(0.5)) );   // ba2 = (b-a)/2
        BigDecimal ab2 = MathLib.round(      (b.add(a)).multiply( new BigDecimal(0.5)) );   // ab2 = (a+b)/2
        
        function = values[0].toText();
        
        BigDecimal temp;
        
        formula.clear();
        iterformula.clear();
        
        // Ausgabe
        iterformula.addNewLine(2);
        formula.addTextBold("6.2. ").addColorBoxBegin("green").addText("4-Punkt Gauss Integration").addColorBoxEnd().addNewLine(2);
        iterformula.addTextUL("Funktionswerte\\;von\\;f(x)\\;an\\;den\\;Stellen\\;0\\;und\\;1:").addNewLine(1);
        iterformula.addLatexString("f(0) = "+MathLib.round( getFunctionsValue( new BigDecimal(0) ))).addNewLine(1);
        iterformula.addLatexString("f(1) = "+MathLib.round( getFunctionsValue( new BigDecimal(1) ))).addNewLine(8);
        iterformula.addTextUL("4-Punkt\\;Gaussintegration:").addNewLine(1);
        iterformula.addLatexString("\\int_{"+a+"}^{"+b+"}f(x)\\;dx \\hspace{3mm} \\approx " +
                           "\\hspace{3mm} \\frac{b-a}{2} \\cdot \\sum \\limits_{i=1}^4 \\omega_{i} \\cdot f(\\;" +
                           "\\frac{a+b}{2} + \\frac{b-a}{2} \\cdot t_i \\;) " +
                           "\\hspace{5mm};\\;a="+a+", \\; b="+b+"\\hspace{5mm}").addNewLine(1); 
        iterformula.addLatexString("= \\; "+ba2+"\\cdot \\sum \\limits_{i=1}^4 \\omega_{i} \\cdot " +
                "f(\\; "+ab2+" + "+ba2+" \\cdot t_i \\;) ").addNewLine(2);
        
        
        for(int i=0; i<=3; i++) 
        {
            fwert = stuetzstelle[i].multiply( ba2 ).add( ab2 );
            
            temp = gewicht[i].multiply( getFunctionsValue( fwert ));
            sum  = sum.add( temp );
            
            if (i==0) iterformula.addLatexString("= \\; "+ba2+"\\cdot (");
            if (temp.compareTo(BigDecimal.ZERO)==-1) iterformula.addText("\\;"); else iterformula.addText("\\;+");
            iterformula.addLatexString(""+MathLib.round(temp));
        }      

        
        formula.addFormula( iterformula ).addText(")").addNewLine(2);
        formula.addText("= "+ MathLib.round( sum.multiply( ba2 ))).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    
    
    public BigDecimal getFunctionsValue(BigDecimal value) {
        
        ExpressionEngine engine = new ExpressionEngine();
        engine.getVariableTable().set("x", new Value(value));
        
        try
        {
            return engine.solve(function).toDecimal();
        }
        catch (InvalidExpressionException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
