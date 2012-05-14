package numerik.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;

import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.*;

public class LUDecomposition implements Task
{
    
    Matrix A;
    Vector b;
    Vector trueb;
    
    TaskPane taskPane;
    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Matrix A:", ArgType.MATRIX), new Argument("Vektor b:", ArgType.VECTOR), 
                new Argument("Normalisieren", ArgType.BOOLEAN), Argument.PRECISION, Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        Recorder recorder = Recorder.getInstance();
        recorder.clear();
        
        // ####### Alle Berechnungen werden mit niedriger Präzision ausgeführt #########
        
        MathLib.setPrecision( parameters[3].toDecimal().intValue() );                                        // Mantissenlänge
//        MathLib.setPrecision( 5 );                                        // Mantissenlänge
        MathLib.setPivotStrategy( true );
        MathLib.setRoundingMode( MathLib.EXACT );                         // exact = Mantissen genau, normal = Nachkomma genau
        MathLib.setNorm( MathLib.FROBENIUSEUKILDNORM );                   // ZEILENSUMMENNORM oder FROBENIUSEUKILDNORM
        MathLib.setInversePrecision( 20 );
        
        //Matrix A = new Matrix("Data.txt", "A");
        //Vector b = new Vector("Data.txt", "a");
        Matrix A = parameters[0].toMatrix();
        Vector b = parameters[1].toVector();
        
        A.mult(b); // sind Matrix und Vektor verkettet?
        
        if (parameters[2].toBoolean())
        {
            Matrix P = A.getScaleOf();
            A = P.mult(A);
            b = P.mult(b);
            MathLib.enableRound(false);
            trueb = P.mult(trueb);
            MathLib.enableRound(true);
        } else {
            trueb = b.clone();
        }
        
        Vector x = A.solveX(b);
        
        // ####### Alle folgenden Berechnungen werden mit höherer Präzision ausgeführt #########
        
        MathLib.setPrecision( 20 );
        Matrix  invA = A.getInverse();
        Matrix AinvA = A.mult(invA); 
        Vector invAb = invA.mult(b);
        Vector     r = A.mult(x).sub(b);
        
        MathLib.setPrecision( 5 );

        BigDecimal     kappa = invA.norm().multiply( A.norm() );
        BigDecimal relFehler = kappa.multiply( r.norm().divide( b.norm(), 2*MathLib.getPrecision(), RoundingMode.HALF_UP) );
        
        
        // ####### Ausgabe wieder mit niedriger Präzision / Achtung! Ausgabe sollte Mantissengenauigkeit haben. #########
        LatexFormula formula = new LatexFormula();
        
        formula.addNewLine(2).addText(A.name+" = ").addMatrix(A).addText(", "+b.name+" = ").addVector(b).addNewLine(2);
        formula.addFormula( recorder.get( true ) );
        formula.addText("x = ").addVector(x).addText(",     Exakt: "+A.name+"^{-1}").addSymbol("*").addText(b.name+" = ").addVector(invAb).addNewLine(2);
        formula.addText(A.name+"^{-1} = ").addMatrix(invA).addNewLine(2);

        MathLib.setRoundingMode( MathLib.NORMAL );
        formula.addText(A.name).addSymbol("*").addText(A.name+"^{-1} = ").addMatrix(AinvA).addNewLine(3);
        formula.addSymbol("kappa").addText("("+A.name+") = ").addMatrixNorm(A.name).addSymbol("*").addMatrixNorm(A.name+"^{-1}").addText(" = "+kappa).addNewLine(2);
        formula.addRelError("x").addText(" = ").addSymbol("kappa").addText("("+A.name+")").addSymbol("*").addVektornormXdivY("r", b.name, true).addLatexString(" \\le ").addText( ""+relFehler );
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
