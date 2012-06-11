package numerik.tasks;

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
        
        formula.addText("Runge-Kutta 4ter Ordnung zur Lösung von Differenzialgleichungen").addNewLine(5);
        formula.addText("Dafür wird folgende Formel benutzt: ").addNewLine();
        formula.addLatexString("w_{i+1} = w_i + \\frac{1}{6} \\cdot (k_1 + 2\\cdot k_2 + 2\\cdot k_3 + k_4)").addNewLine();
        formula.addLatexString("Dabei\\ werden\\ k_1\\ ,\\ k_2\\ ,\\ k_3\\ , und\\ k_4").addText(" wie folgt berechnet:").addNewLine();
        formula.addLatexString("k_1 = h \\cdot f( x, w_i )").addNewLine();
        formula.addLatexString("k_2 = h \\cdot f( x + \\frac{h}{2}, w_i + \\frac{k_1}{2} )").addNewLine();
        formula.addLatexString("k_3 = h \\cdot f( x + \\frac{h}{2}, w_i + \\frac{k_2}{2} )").addNewLine();
        formula.addLatexString("k_4 = h \\cdot f( x + h, w_i + k_3 )").addNewLine();
        formula.addText("Die Lösung ihrer Differenzialgleichung folgt hier:").addNewLine(5);
        
        //Ausgabe der Lösung von RungeKuttaDGL
        formula.addFormula(calculateRungeKuttaDGL());
        
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
        
        internFormula.addText("Das Intervall ist: " + x + " <= t <= " + timeTillBreak).addNewLine();
        internFormula.addText("Die Schrittgröße beträgt: h = " + h).addNewLine();
        internFormula.addText("Deshalb werden " + iterations + " Iterationsschritte getätigt").addNewLine();
        internFormula.addText("Diese DGL startet mit folgendem Anfangsproblem:").addNewLine();
        internFormula.addText("y = " + y + " und x = " + x).addNewLine();
        internFormula.addLatexString("\\begin{tabular}{|l|l|l|} \\hline");
        internFormula.addLatexString("\\textbf{i} & \\textbf{x_i} & \\textbf{w_i} \\\\ \\hline");
        
        // Funktion (Differenzialgleichung) zum Ausrechnen der Funktionswerte
        // + Ausrechnung  des Anfangsfunktionswertes
        double w = y;
        
        internFormula.addLatexString("0 & " + x + " & " + w + "\\\\ \\hline").addNewLine();
        
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
            
            internFormula.addLatexString(i + " & " + x + " & " + w + "\\\\ \\hline").addNewLine();
        }
        
        internFormula.addLatexString("\\end{tabular}").addNewLine();
        internFormula.addLatexString("\\colorbox{green}{Die\\ Lösung\\ der\\ DGL\\ bis\\ zur\\ Zeit\\ t = " + timeTillBreak + "\\ lautet\\ " + w + "}");
        
        return internFormula;
    }
    
    // Die eigentliche Funktion zum Ausrechnen der Funktionswerte
    public double getFunctionValueFromDGL(double x, double y)
    {
        return y - Math.pow(x, 2) + 1;
    }
}