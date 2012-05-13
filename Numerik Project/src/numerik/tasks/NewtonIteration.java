package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.MathLib;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.*;

public class NewtonIteration implements Task
{
    
    TaskPane taskPane;
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("k:", ArgType.INTEGER, "5"), new Argument("a:", ArgType.DECIMAL, "18"), 
                new Argument("x_0:", ArgType.DECIMAL, "12800"), Argument.RUN_BUTTON);
    }

    @Override
    public void run(Value... values)
    {
        MathLib.setPrecision( 16 );
        MathLib.setRoundingMode( MathLib.EXACT );
        
        int counter = 0;
        
        // Berechne k-te Wurzel aus a;  --> x = a^(1/k);  df ist Ableitung von f
        double  f , df;

        //double  x = 12800;         // Achtung bei df(x)=0 -> Division durch Null
        double x = values[2].toDecimal().doubleValue();
        
        double ox = x+1;
        //int     k = 5;
        int     k = values[0].toDecimal().intValue();
        //double  a = 18;
        double  a = values[1].toDecimal().doubleValue();
        
        LatexFormula formula = new LatexFormula();
        // Ausgabe 
        formula.addText("Newton-Verfahren zur Bestimmung  ").addLatexString("x = \\sqrt[k\\hspace{0.8mm}]{a}").addNewLine(2);
        formula.addText("1. Forme dazu  ").addLatexString("x = \\sqrt[k\\hspace{1mm}]{a}").addText(" derart um, dass ").addLatexString("f(x)=0").addText(" entsteht.").addNewLine(1);
        formula.addText("    In diesem Fall:  ").addLatexString("f(x) = x^{k}-a = 0").addNewLine(1);
        formula.addText("2. Bilde ").addLatexString("f'(x)").addText(" und setze beides in die Newton-Iteration ein.").addNewLine(4);
        formula.addText("geg: ").addNewLine(1);
        formula.addLatexString("k = "+k).addNewLine(1);
        formula.addLatexString("a = "+a).addNewLine(1);
        formula.addLatexString("x_{0} = "+x).addNewLine(3);
        formula.addText("ges: ").addNewLine(1);
        formula.addLatexString("x = \\sqrt["+k+"\\hspace{1mm}]{"+a+"}").addNewLine(4);
        formula.addTextUL("Iterationsschritte").addNewLine(1);
        // ---
        
        // Iteration
        if(a<0 && (k % 2)==0) {
            formula.addTextBold("Fehler: ").addText("Keine geradzahlige Wurzel aus einer negativen Zahl möglich!");
        } 
        else 
        {
            while( ox-x != 0 ) 
            {
                formula.addLatexString("x_{"+counter+"} = "+x).addNewLine(1);
                counter++;
                
                ox = x;
                f = Math.pow(x, k) - a;
                df = k * Math.pow(x, k-1);
                x = MathLib.round( BigDecimal.valueOf(     x - f/df       )).doubleValue();
            }
            formula.addNewLine(1);
        }
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
