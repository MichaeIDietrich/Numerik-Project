
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
    LatexFormula formula = new LatexFormula();
    Recorder    recorder = Recorder.getInstance();
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
    }
    
    
    @Override
    public void run(Value... values)
    {
        recorder.clear();
        
        MathLib.setNorm( MathLib.FROBENIUSEUKILDNORM );
        MathLib.setPrecision( 16 );    // Achtung: Präzision>16 führt zu Endlosschleife!!!
        
        // Lösen nichtlinearer Gleichungssysteme
        BigDecimal[] startvector = {new BigDecimal(-0.7) , new BigDecimal(-0.5)};
        
        Vector  iter = new Vector( startvector );
        Vector     x = new Vector( startvector.length );
        x = x.setUnitVector(x);
        //Vector start = iter.clone();
        Matrix    jm = new Matrix( startvector.length, startvector.length );
        int        i = 0;
       
        while (( x.norm() ).compareTo( BigDecimal.ZERO )==1) 
        {
            formula.addLatexString("x_{"+ i +"} = ").addVector(iter).addNewLine(1);
            i++;
            x = jm.jakobiMatrix( iter ).solveX( iter.getEquationsValue() );
            iter = iter.add( x );
        }
        
        recorder.add( formula );
        formula.clear();
        
        // Ausgabe: Latex-Formula-String

        LatexFormula formula = new LatexFormula();
        
        formula.addText("Ableitungen der Gleichungen ").addLatexString("f_{i}(x_1,..., x_n):").addTextBold(" Matrix/ jakobiMatrix() ").addNewLine(1);
        formula.addText("Die Gleichungen selbst stehen in: ").addTextBold(" Vektor/ getEquationsValue()").addNewLine(1);

        formula.addText("Der Vektor 'Startvektor' ist ").addLatexString("x_0.").addNewLine(3);
        formula.addText("Gegeben ist ein Gleichungssystem ").addLatexString("f_{i}(x_1,..., x_n)").addText(" mit 1...n abhängigen").addText(" ").addNewLine(1);
        formula.addText("Variablen und einem Startvektor der Länge n. ").addLatexString("").addText(" ").addNewLine(4);
        formula.addText("Folgende Gleichungen werden benutzt:").addNewLine(1).addLatexString("\\Phi( x^{k} ) \\cdot \\Delta{x^{k+1}} = -f( x^{k} )")

        .addText("   und   ").addLatexString("x^{k+1} = x^{k} + \\Delta{x^{k+1}}").addText("  mit ").addNewLine(3).addLatexString("\\Phi( x ) = ");
        formula.jakobiMatrix();
        formula.addNewLine(3).addTextUL("Start der Iteration").addNewLine(1);
        formula.addFormula( recorder.get() ).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
