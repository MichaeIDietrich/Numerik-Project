
package numerik.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        BigDecimal[] startvector = {new BigDecimal(1.5) , new BigDecimal(-1)};
        
        Vector iterx = new Vector( startvector );
        Vector    x = new Vector( startvector.length );
        x = x.setUnitVector(x);
        Matrix   jm = new Matrix( startvector.length, startvector.length );
        int       i = 0;
        
        
        // Abbruchbedingung 'obereschranke' bei x.norm() < 2^(-50) < eps
        BigDecimal obereschranke = BigDecimal.ONE.divide(new BigDecimal(2).pow(50), 16, RoundingMode.HALF_UP);

        while (( x.norm()).compareTo( obereschranke )==1) 
        {
            iterformula.addLatexString("x_{"+ i +"} = ").addVector(iterx).addNewLine(1);
            i++;
            x = jm.jakobiMatrix( derivations(iterx) ).solveX( getFunctionsValue(iterx) );
            iterx = iterx.add( x );
        }
        iterformula.addNewLine(2);
        iterformula.addText("Abbruch bei ").addLatexString("\\; x_"+i+" = x_"+(i-1)+" \\;\\; \\leftrightarrow \\;\\; \\arrowvert{ x_{"+(i)+"}-x_{"+(i-1)+"} }\\arrowvert \\leq eps");
        
        // Ausgabe: Latex-Formula-String
        LatexFormula formula = new LatexFormula();
        
        formula.addTextBold("3.E) ").addColorBoxBegin("green").addText("Iteratives Verfahren z. Lösung nichtlinearer Gleichungssysteme").addColorBoxEnd().addNewLine(2);
        formula.addText("Ableitungen der Gleichungen ").addLatexString("f_{i}(x_1,..., x_n)").addText(" in:").addTextBold(" derivations()").addNewLine(1);
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
        derivation.set(0,0, BigDecimal.valueOf(   -1.0             ));
        derivation.set(0,1, BigDecimal.valueOf(   -Math.sin(x[1])  ));
        derivation.set(1,0, BigDecimal.valueOf(    Math.cos(x[0])  ));
        derivation.set(1,1, BigDecimal.valueOf(   -1.0             ));
        
        return derivation;
    }
    
    
    public Vector getFunctionsValue(Vector vector) {
        
        Vector function = new Vector( vector.getLength() );
        Double[]      x = vector.toDoubleArray();           // x[0] = x ; x[1] = y ; x[2] = z usw.
        
        // Hier die >> Funktionen << eintragen:
        function.set(0, BigDecimal.valueOf(     1.0 - x[0] + Math.cos(x[1])     ).negate());
        function.set(1, BigDecimal.valueOf(    -1.4 - x[1] + Math.sin(x[0])     ).negate());
        
        return function;
    }
}
