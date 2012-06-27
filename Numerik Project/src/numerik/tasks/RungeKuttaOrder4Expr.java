package numerik.tasks;

import java.math.*;

import numerik.calc.MathLib;
import numerik.expression.*;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;

// Berechnung von Funktionswerten einer Differezialgleichung
// von Anfangswert y0, mit h-Schrittgröße bis zur Zeit t
public class RungeKuttaOrder4Expr implements Task
{
    private static final BigDecimal TWO = new BigDecimal("2");
    
    
    private TaskPane taskPane;
    private LatexFormula formula = new LatexFormula();
    
    private String function;
    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        
        taskPane.createJToolBarByArguments(
                new Argument("Funktion:", ArgType.EXPRESSION, "y-x^2+1", 400),
                new Argument("Startwert:", ArgType.DECIMAL_EX, "0"),
                new Argument("Funktionswert:", ArgType.DECIMAL_EX, "0.5"),
                Argument.PRECISION, Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        function = parameters[0].toText();
        
        
        MathLib.setPrecision(parameters[3].toDecimal().intValue());
        MathLib.setRoundingMode(MathLib.EXACT);
        
        formula = new LatexFormula();
        
        formula.addTextBold("7.B) ");
        formula.addColorBoxBegin("green");
        formula.addText("Runge-Kutta 4.O. zur Lösung v. Differenzialgleichungen 1.O.");
        formula.addColorBoxEnd().addNewLine(5);
        formula.addTextUL("Benutze\\;Formel").addNewLine();
        formula.addLatexString("w_{i+1} = w_i + \\frac{1}{6} \\cdot (k_1 + 2 \\cdot k_2 + 2 \\cdot k_3 + k_4)").addNewLine(2);
        formula.addText("berechne ").addLatexString("k_{1}, \\; k_{2}, \\; k_{3}").addText(" und ").addLatexString("k_{4}").addText(" wie folgt").addNewLine(1);
        formula.addLatexString("k_1 = h \\cdot f( x, w_i )").addNewLine();
        formula.addLatexString("k_2 = h \\cdot f( x + \\frac{h}{2}, w_i + \\frac{k_1}{2} )").addNewLine();
        formula.addLatexString("k_3 = h \\cdot f( x + \\frac{h}{2}, w_i + \\frac{k_2}{2} )").addNewLine();
        formula.addLatexString("k_4 = h \\cdot f( x + h, w_i + k_3 )").addNewLine(3);
        
        formula.addFormula( calculateRungeKuttaDGL(parameters[1].toDecimal(), parameters[2].toDecimal()) );
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    
    
    public LatexFormula calculateRungeKuttaDGL(BigDecimal x_0, BigDecimal y_0)
    {
        LatexFormula internFormula = new LatexFormula();
        
        // Die Zeit oder ein x-Wert, bis die Iteration abgebrochen werden soll,
        // weil sich die Rekursion am Ende des Intervalls befindet (Bsp. 0 <= t <= 2)
        BigDecimal timeTillBreak = TWO;
        
        // Schritte h, die durchgeführt werden, um Punkte der 
        // Differenzialfunktionen zu berechnen
        BigDecimal h = TWO.divide(BigDecimal.TEN);
        
        // Berechnen der benötigten Iterationen, um die Funktionswerte
        // bis Zeit t zu berechnen
        int iterations = timeTillBreak.divide(h, MathLib.getPrecision(), RoundingMode.HALF_UP).intValue();
        
        // Anfangswertproblem, Bsp.: y(0) = 5, die Startzeit ist hierbei 0
        BigDecimal x = x_0;
        BigDecimal y = y_0;

        
        internFormula.addText("definiere Intervall: " + showRoundedVal(x) + " \\leq t \\leq " + showRoundedVal(timeTillBreak)).addNewLine();
        internFormula.addText("setze Schrittweite: h = " + h).addNewLine();
        internFormula.addText("Aus Intervalllänge und Schrittweite folgt " + iterations + " Iterationsschritte nötig.").addNewLine(3);
        internFormula.addText("Löse DGL mit Anfangswertproblem:").addNewLine();
        internFormula.addLatexString("y("+showRoundedVal(x)+")="+showRoundedVal(y)+" \\; \\leftrightarrow \\; y = " + showRoundedVal(y))
                     .addText(" und ").addLatexString(" x = " + showRoundedVal(x)).addNewLine(5);
        
        internFormula.addTextUL("Beginne\\;mit\\;Iteration").addNewLine(2);
        internFormula.addLatexString("\\begin{tabular}{l|l|l}");
        internFormula.addLatexString("$\\bf{i}$ & $\\bf{x_{i}}$ & $\\bf{w_i}$ \\\\ \\hline");
        
        // Funktion (Differenzialgleichung) zum Ausrechnen der Funktionswerte
        // + Ausrechnung des Anfangsfunktionswertes
        BigDecimal w = y;
        
        internFormula.addLatexString("0 & " + showRoundedVal(x) + " & " + w + "\\\\");
        
        for (int i = 1; i < iterations + 1; i++)
        {
            // k1 = h * f(x, w)
            BigDecimal k1 = h.multiply(getFunctionValueFromDGL(x, w));
            
            // k2 = h * f(x + (1/2)h, w + (k1/ 2))
            BigDecimal k2 = h.multiply(getFunctionValueFromDGL(x.add(h.divide(TWO)), w.add(k1.divide(TWO))));
            
            // k3 = h * f(x + (1/2)h, w + (k2/ 2)) 
            BigDecimal k3 = h.multiply(getFunctionValueFromDGL(x.add(h.divide(TWO)), w.add(k2.divide(TWO))));
            
            // k4 = h * f(x + h, w0 + k3)
            BigDecimal k4 = h.multiply(getFunctionValueFromDGL(x.add(h), w.add(k3)));
            
            // wi+1 = wi + (1/6)(k1 + 2*k2 + 2*k3 + k4)
            
            w = w.add(k1.add(k2.multiply(TWO)).add(k3.multiply(TWO)).add(k4).divide(new BigDecimal("6"), MathLib.getPrecision(), RoundingMode.HALF_UP));
            
            // Weitergehen der Zeit t oder auch x --> x = x + h
            x = x.add(h);
            
            internFormula.addLatexString(i +" & "+ showRoundedVal(x) +" & "+ w +"\\\\");
        }
        
        internFormula.addLatexString("\\hline");
        internFormula.addLatexString("\\end{tabular}").addNewLine(3);
        internFormula.addText("Lösung der DGL an der Stelle ").addLatexString("x_{"+iterations+"}").addNewLine(1);
        internFormula.addLatexString("y(x_{"+iterations+"}) = y("+ showRoundedVal(timeTillBreak)+") = "+ w);
        
        return internFormula;
    }
    
    
    public String showRoundedVal(BigDecimal x) 
    {
        BigDecimal temp = MathLib.round(x);
        
        return temp.toString();
    }
    
    // Die eigentliche Funktion zum Ausrechnen der Funktionswerte
    public BigDecimal getFunctionValueFromDGL(BigDecimal x, BigDecimal y)
    {
        ExpressionEngine engine = new ExpressionEngine();
        engine.getVariableTable().set("x", new Value(x));
        engine.getVariableTable().set("y", new Value(y));
        
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