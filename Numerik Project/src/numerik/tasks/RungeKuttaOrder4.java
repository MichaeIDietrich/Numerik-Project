package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.MathLib;
import numerik.expression.Value;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;

// Berechnung von Funktionswerten einer Differezialgleichung
// von Anfangswert y0, mit h-Schrittgröße bis zur Zeit t
public class RungeKuttaOrder4 implements Task
{
    TaskPane taskPane;
    LatexFormula formula = new LatexFormula();
    
    public RungeKuttaOrder4() 
    {
        init();
    }
    
    
    
    public void init()
    {
        formula.addText("leer");
    }
    
    
    
    public LatexFormula getFormula() {
        return this.formula;
    }

    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;       
    }

    
    
    @Override
    public void run(Value... parameters)
    {
        formula = new LatexFormula();
        
        formula.addTextBold("7.B) ");
        formula.addColorBoxBegin("green");
        formula.addText("Runge-Kutta 4.O. zur Lösung v. Differenzialgleichungen 1.O.");
        formula.addColorBoxEnd().addNewLine(5);
        formula.addTextUL("Benutze\\;Formel").addNewLine();
        formula.addLatexString("w_{i+1} = w_i + \\frac{1}{6} \\cdot (k_1 + 2\\cdot k_2 + 2\\cdot k_3 + k_4)").addNewLine(2);
        formula.addText("berechne ").addLatexString("k_{1}, \\; k_{2}, \\; k_{3}").addText(" und ").addLatexString("k_{4}").addText(" wie folgt").addNewLine(1);
        formula.addLatexString("k_1 = h \\cdot f( x, w_i )").addNewLine();
        formula.addLatexString("k_2 = h \\cdot f( x + \\frac{h}{2}, w_i + \\frac{k_1}{2} )").addNewLine();
        formula.addLatexString("k_3 = h \\cdot f( x + \\frac{h}{2}, w_i + \\frac{k_2}{2} )").addNewLine();
        formula.addLatexString("k_4 = h \\cdot f( x + h, w_i + k_3 )").addNewLine(3);
        
        formula.addFormula( calculateRungeKuttaDGL() );
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    
    
    
    public LatexFormula calculateRungeKuttaDGL()
    {
        LatexFormula internFormula = new LatexFormula();
        
        // Die Zeit oder ein x-Wert, bis die Iteration abgebrochen werden soll,
        // weil sich die Rekursion am Ende des Intervalls befindet (Bsp. 0 <= t <= 2)
        double timeTillBreak = 2d;
        
        // Schritte h, die durchgeführt werden, um Punkte der 
        // Differenzialfunktionen zu berechnen
        double h = 0.2d;
        
        // Berechnen der benötigten Iterationen, um die Funktionswerte
        // bis Zeit t zu berechnen
        int iterations = (int) Math.round(timeTillBreak / h);
        
        // Anfangswertproblem, Bsp.: y(0) = 5, die Startzeit ist hierbei 0
        double x = 0d;
        double y = 0.5d;

        
        internFormula.addText("Definiere Intervall: " + showRoundedVal(x) + " \\leq t \\leq " + showRoundedVal(timeTillBreak)).addNewLine();
        internFormula.addText("Setze Schrittweite: h = " + h).addNewLine();
        internFormula.addText("Aus Intervalllänge und Schrittweite folgt " + iterations + " Iterationsschritte nötig.").addNewLine(3);
        internFormula.addText("Löse DGL mit Anfangswertproblem:").addNewLine();
        internFormula.addLatexString("y("+showRoundedVal(x)+")="+showRoundedVal(y)+" \\; \\leftrightarrow \\; y = " + showRoundedVal(y))
                     .addText(" und ").addLatexString(" x = " + showRoundedVal(x)).addNewLine(3);
        
        internFormula.addTextUL("Beginne\\;mit\\;Iteration").addNewLine(2);
        internFormula.addLatexString("\\begin{tabular}{l|l|l}");
        internFormula.addLatexString("\\textbf{$i$} & \\textbf{$x_{i}$} & \\textbf{$w_i$} \\\\ \\hline");
        
        // Funktion (Differenzialgleichung) zum Ausrechnen der Funktionswerte
        // + Ausrechnung des Anfangsfunktionswertes
        double w = y;
        
        internFormula.addLatexString("0 & " + x + " & " + w + "\\\\");
        
        for (int i = 1; i < iterations + 1; i++)
        {
            // k1 = h * f(x, w)
            double k1 = h * getFunctionValueFromDGL(x, w);

            // k2 = h * f(x + (1/2)h, w + (k1/ 2))
            double k2 = h * getFunctionValueFromDGL(x + (h/2d), w + (k1 / 2d));
            
            // k3 = h * f(x + (1/2)h, w + (k2/ 2)) 
            double k3 = h * getFunctionValueFromDGL(x + (h/2d), w + (k2 / 2d));
            
            // k4 = h * f(x + h, w0 + k3)
            double k4 = h * getFunctionValueFromDGL(x + h, w + k3);
            
            // wi+1 = wi + (1/6)(k1 + 2*k2 + 2*k3 + k4)
            w = w + ((k1 + 2*k2 + 2*k3 + k4)/(6d));
            
            // Weitergehen der Zeit t oder auch x --> x = x + h
            x = x + h;
            
            internFormula.addLatexString(i +" & "+ showRoundedVal(x) +" & "+ w +"\\\\");
        }
        
        internFormula.addLatexString("\\hline");
        internFormula.addLatexString("\\end{tabular}").addNewLine(3);
        internFormula.addText("Lösung der DGL an der Stelle ").addLatexString("x_{"+iterations+"}").addNewLine(1);
        internFormula.addLatexString("y(x_{"+iterations+"}) = y("+ showRoundedVal(timeTillBreak)+") = "+ w);
        
        return internFormula;
    }
    
    
    // Die eigentliche Funktion zum Ausrechnen der Funktionswerte
    public double getFunctionValueFromDGL(double x, double y)
    {
        return y - Math.pow(x, 2) + 1;
    }
    
    
    public String showRoundedVal(double x) 
    {
        MathLib.setPrecision(15);
        MathLib.setRoundingMode(MathLib.EXACT);
        
        BigDecimal temp = MathLib.round(new BigDecimal(x));
        
        return temp.toString();
    }
}