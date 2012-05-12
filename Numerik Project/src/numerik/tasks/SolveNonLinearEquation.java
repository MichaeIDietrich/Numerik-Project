
package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.ui.*;

public class SolveNonLinearEquation implements Task
{
    
    TaskPane taskPane;
    
    

    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
    }
    
    
    @Override
    public void run(Value... values)
    {
        Vector x;
        MathLib.setPrecision( 5 );
        
        // Lösen nichtlinearer Gleichungssysteme
        BigDecimal[] startvector = {new BigDecimal(-0.7) , new BigDecimal(-0.5)};
        
        Vector  iter = new Vector( startvector );
        Vector start = iter.clone();
        Matrix    jm = new Matrix( startvector.length, startvector.length );
        
        for(int i=1; i<=10; i++) 
        {
            x = jm.jakobiMatrix( iter ).solveX( iter.getEquationsValue() );
            iter = iter.add( x );
        }
        
        // Ausgabe: Latex-Formula-String
        LatexFormula formula = new LatexFormula();
        
        formula.addText("Ableitungen der Gleichungen ").addLatexString("f_{i}(x_1,..., x_n):").addTextBold(" Matrix/ jakobiMatrix() ").addNewLine(1);
        formula.addText("Die Gleichungen selbst stehen in: ").addTextBold(" Vektor/ getEquationsValue()").addNewLine(1);
        formula.addText("Der Vektor 'Startvektor' ist ").addLatexString("x_0.").addNewLine(3);
        formula.addText("Gegeben ist ein Gleichungssystem ").addLatexString("f_{i}(x_1,..., x_n)").addText(" mit 1...n abhängigen").addText(" ").addNewLine(1);
        formula.addText("Variablen und einem Startvektor der Länge n. ").addLatexString("").addText(" ").addNewLine(4);
        formula.addText("Folgende Gleichungen werden benutzt:").addNewLine(1).addLatexString("\\Phi( x^{k} ) \\cdot \\Delta{x^{k+1}} = -f( x^{k} )")
        .addText("   und   ").addLatexString("x^{k+1} = x^{k} + \\Delta{x^{k+1}}").addText("  mit ").addNewLine(2).addLatexString("\\Phi( x ) = ");
        formula.jakobiMatrix();
        formula.addNewLine(2).addText("Start der Iteration des nicht-linearen Gleichungssystems mit").addNewLine(1);
        formula.addLatexString("x_{0}").addText(" = ").addVector(start).addNewLine(2).addText("Lösung:").addNewLine(1).addText("x = ").addVector(iter).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}