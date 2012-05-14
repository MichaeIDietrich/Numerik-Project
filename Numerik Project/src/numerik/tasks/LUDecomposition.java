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
    Matrix trueA;
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
        String name;
        
        // ####### Alle Berechnungen werden mit niedriger Präzision ausgeführt #########
        
        MathLib.setPrecision( parameters[3].toDecimal().intValue() );                                        // Mantissenlänge
//        MathLib.setPrecision( 5 );                                      // Mantissenlänge
        MathLib.setPivotStrategy( true );
        MathLib.setRoundingMode( MathLib.EXACT );                         // exact = Mantissen genau, normal = Nachkomma genau
        MathLib.setNorm( MathLib.ZEILENSUMMENNORM );                      // ZEILENSUMMENNORM oder FROBENIUSEUKILDNORM
        MathLib.setInversePrecision( 20 );
        
        //Matrix A = new Matrix("Data.txt", "A");
        //Vector b = new Vector("Data.txt", "a");
        Matrix A = parameters[0].toMatrix();
        Vector b = parameters[1].toVector();
        Matrix trueA = A.clone();
        Vector trueb = b.clone();
        
        A.mult(b); // sind Matrix und Vektor verkettet?
        
        if (parameters[2].toBoolean())
        {
            MathLib.enableRound(false);
            Matrix P = A.getScaleOf();
            trueA = P.mult(trueA);
            MathLib.enableRound(true);
            
            name = A.name;
            A = P.mult(A);
            A.name = name;
            
            name = b.name;
            b = P.mult(b);
            b.name = name;
            
            MathLib.enableRound(false);
            trueb = P.mult(trueb);
            MathLib.enableRound(true);
        } 
        
        Vector x = A.solveX(b);
        
        // ####### Alle folgenden Berechnungen werden mit höherer Präzision ausgeführt #########
        
        MathLib.enableRound(false);
        Matrix  invA = trueA.getInverse();
        Matrix AinvA = trueA.mult(invA);
        Vector invAb = invA.mult(trueb);
        Vector     r = trueA.mult(x).sub(b);
        MathLib.enableRound(true);

        BigDecimal     kappa = invA.norm().multiply( trueA.norm() );
        BigDecimal relFehler = kappa.multiply( r.norm().divide( b.norm(), MathLib.getInversePrecision(), RoundingMode.HALF_UP) );
        
        
        // ####### Ausgabe wieder mit niedriger Präzision / Achtung! Ausgabe sollte Mantissengenauigkeit haben. #########
        LatexFormula formula = new LatexFormula();
        
        formula.addNewLine(2).addText(A.name+" = ").addMatrix(A).addText(", "+b.name+" = ").addVector(b).addNewLine(2);
        formula.addFormula( recorder.get( true ) );
        formula.addText("x = ").addVector(x).addText(",     Exakt: "+A.name+"^{-1}").addSymbol("*").addText(b.name+" = ").addVector(invAb).addNewLine(2);
        formula.addText(A.name+"^{-1} = ").addMatrix(invA).addNewLine(2);

        MathLib.setRoundingMode( MathLib.NORMAL );
        formula.addText(A.name).addSymbol("*").addText(A.name+"^{-1} = ").addMatrix(AinvA).addNewLine(3);
        formula.addSymbol("kappa").addText("("+A.name+") = ").addMatrixNorm(A.name).addSymbol("*").addMatrixNorm(A.name+"^{-1}").addText(" = "+kappa).addNewLine(2);
        formula.addRelError("x").addText(" = ").addSymbol("kappa").addText("("+A.name+")").addSymbol("*").addVektornormXdivY("r", b.name, true)
               .addLatexString(" \\le ").addText( ""+relFehler ).addNewLine(2);
        formula.addRelError("x_{exakt}").addText(" = ")
               .addVektornormXdivY("x-"+A.name+"^{-1}"+b.name, A.name+"^{-1}"+b.name, false)
               .addText(" = "+ x.sub(invAb).norm().divide(invAb.norm(), MathLib.getPrecision(), RoundingMode.HALF_UP) ).addNewLine(2);
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
