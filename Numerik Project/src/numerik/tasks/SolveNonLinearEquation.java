
package numerik.tasks;

import java.math.BigDecimal;
import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;


public class SolveNonLinearEquation implements Task
{
    
    TaskPane taskPane;
    LatexFormula     formula = new LatexFormula();
    LatexFormula iterformula = new LatexFormula();
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
    }

    
    @Override
    public void run(Value... values)
    {
        iterformula.clear();
        
        MathLib.setNorm( MathLib.FROBENIUSEUKLIDNORM );
        MathLib.setPrecision( 16 );    // Achtung: Präzision > 16 führt zu Endlosschleife!!!
        
        // Lösen nichtlinearer Gleichungssysteme
        BigDecimal[] startvector = {new BigDecimal(-0.7) , new BigDecimal(-0.5)};
        
        Vector iterx = new Vector( startvector );
        Vector    x = new Vector( startvector.length );
        x = x.setUnitVector(x);
        Matrix   jm = new Matrix( startvector.length, startvector.length );
        int       i = 0;
        

        while (( x.norm()).compareTo( BigDecimal.ZERO )==1) 
        {
            iterformula.addLatexString("x_{"+ i +"} = ").addVector(iterx).addNewLine(1);
            i++;
            x = jm.jakobiMatrix( derivations(iterx) ).solveX( getFunctionsValue(iterx) );
            iterx = iterx.add( x );
        }
        
        
        // Ausgabe: Latex-Formula-String
        LatexFormula formula = new LatexFormula();
        
        formula.addText("Ableitungen der Gleichungen ").addLatexString("f_{i}(x_1,..., x_n):").addTextBold(" derivations()").addNewLine(1);
        formula.addText("Die Gleichungen selbst stehen in: ").addTextBold(" getFunctionsValue()").addNewLine(1);

        formula.addText("Der Vektor 'Startvektor' ist ").addLatexString("x_0.").addNewLine(3);
        formula.addText("Gegeben ist ein Gleichungssystem ").addLatexString("f_{i}(x_1,..., x_n)").addText(" mit 1...n").addText(" ").addNewLine(1);
        formula.addText("abhängigen Variablen und einem Startvektor der Länge n.").addLatexString("").addText(" ").addNewLine(4);
        formula.addText("Folgende Gleichungen werden benutzt:").addNewLine(1).addLatexString("\\Phi( x^{k} ) \\cdot \\Delta{x^{k+1}} = -f( x^{k} )")
        .addText("   und   ").addLatexString("x^{k+1} = x^{k} + \\Delta{x^{k+1}}").addText("  mit ").addNewLine(3).addLatexString("\\Phi( x ) = ");
        
        formula.jakobiMatrix();
        formula.addNewLine(3).addTextUL("Start\\;der\\;Iteration").addNewLine(1);
        formula.addFormula( iterformula ).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    

    public Matrix derivations(Vector vector)
    {
        Matrix derivation = new Matrix( vector.getLength(), vector.getLength() );
        Double[]        x = vector.toDoubleArray();
        
        // Hier die >> Ableitungen << der Funktionen eintragen:
        derivation.set(0,0, BigDecimal.valueOf(   2*x[0]      ));
        derivation.set(0,1, BigDecimal.valueOf(   2*x[1]+0.6  ));
        derivation.set(1,0, BigDecimal.valueOf(   2*x[0]+1    ));
        derivation.set(1,1, BigDecimal.valueOf(  -2*x[1]-1.6  ));
        
        return derivation;
    }
    
    
    public Vector getFunctionsValue(Vector vector) {
        
        Vector function = new Vector( vector.getLength() );
        Double[]      x = vector.toDoubleArray();           // x[0] = x_1 ; x[1] = x_2 ; usw.
        
        // Hier die >> Funktionen << eintragen:
        function.set(0, BigDecimal.valueOf(     x[0]*x[0] +x[1]*x[1]       +0.6*x[1] -0.16     ).negate());
        function.set(1, BigDecimal.valueOf(     x[0]*x[0] -x[1]*x[1] +x[0] -1.6*x[1] -0.14     ).negate());
        
        return function;
    }
}
