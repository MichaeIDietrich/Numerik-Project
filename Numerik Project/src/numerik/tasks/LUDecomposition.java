package numerik.tasks;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JPanel;

import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.ui.LatexFormula;
import numerik.ui.Recorder;

public class LUDecomposition
{
    
    LatexFormula formula = new LatexFormula();
    Matrix A;
    Vector b;
    
    public LUDecomposition() 
    {
        init();
    }
    
    public void init()
    {
        Recorder recorder = Recorder.getInstance();
        
        
        // ####### Alle Berechnungen werden mit niedriger Präzision ausgeführt #########
        
        MathLib.setPrecision( 5 );                                     // Mantissenlänge
        MathLib.setPivotStrategy( true );
        MathLib.setRoundingMode( MathLib.EXACT    );                   // exact = Mantissen genau, normal = Nachkomma genau
        MathLib.setNorm( MathLib.ZEILENSUMMENNORM );                   // ZEILENSUMMENNORM oder FROBENIUSEUKILDNORM
        MathLib.setInversePrecision( 20 );
        
        Matrix A = new Matrix("Data.txt", "A");
        Vector b = new Vector("Data.txt", "a");
//        Matrix P = A.getScaleOf();
        
//        A = P.mult(A);
//        b = P.mult(b);
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
           
        formula.addNewLine(2).addText(A.name+" = ").addMatrix(A).addText(", "+b.name+" = ").addVector(b).addNewLine(2);
        formula.addFormula( recorder.get( true ) );
        formula.addText("x = ").addVector(x).addText(",     Exakt: "+A.name+"^{-1}").addSymbol("*").addText(b.name+" = ").addVector(invAb).addNewLine(2);
        formula.addText(A.name+"^{-1} = ").addMatrix(invA).addNewLine(2);

        MathLib.setRoundingMode( MathLib.NORMAL );
        formula.addText(A.name).addSymbol("*").addText(A.name+"^{-1} = ").addMatrix(AinvA).addNewLine(3);
        formula.addSymbol("kappa").addText("("+A.name+") = ").addMatrixNorm(A.name).addSymbol("*").addMatrixNorm(A.name+"^{-1}").addText(" = "+kappa).addNewLine(2);
        formula.addRelError("x").addText(" = ").addSymbol("kappa").addText("("+A.name+")").addSymbol("*").addVektornormXdivY("r", b.name, true).addLatexString(" \\le ").addText( ""+relFehler );
    }
    
    public LatexFormula getFormula() {
        return this.formula;
    }
}
