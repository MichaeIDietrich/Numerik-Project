package numerik.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;

import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;

public final class LUDecomposition implements Task
{
    private TaskPane taskPane;
    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(
                new Argument("Matrix:",         ArgType.MATRIX, 100),
                new Argument("Vektor:",         ArgType.VECTOR, 100),
                new Argument("Optimieren",      ArgType.BOOLEAN),
                new Argument("Pivot-Strategie", ArgType.BOOLEAN),
                new Argument("Norm:", "Zeilensummen-Norm", "Frobenius-Euklid-Norm"),
                new Argument("Genauigkeit",     ArgType.PRECISION, "16"),
                Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        Recorder recorder = Recorder.getInstance();
        recorder.setActive(true);
        recorder.clear();
        String name;
        
        // ####### Alle Berechnungen werden mit niedriger Präzision ausgeführt #########
        
        MathLib.setPrecision( parameters[5].toDecimal().intValue() );     // Mantissenlänge
        
        MathLib.setPivotStrategy( parameters[3].toBoolean() );
        MathLib.setRoundingMode( MathLib.EXACT );                         // EXACT = Mantissen genau, NORMAL = Nachkomma genau
        MathLib.setNorm( parameters[4].toText().equals("Zeilensummen-Norm") ? MathLib.ZEILENSUMMENNORM : MathLib.FROBENIUSEUKLIDNORM );  // ZEILENSUMMENNORM oder FROBENIUSEUKILDNORM
        MathLib.setInversePrecision( 20 );
        
        Matrix A = parameters[0].toMatrix();
        Vector b = parameters[1].toVector();
        Matrix trueA = A.clone();
        Vector trueb = b.clone();
        
        A.mult(b);     // Prüfe: Matrix und Vektor verkettet? -> sonst Fehler.
        
        if (parameters[2].toBoolean())
        {
            MathLib.enableRound( false );
            Matrix P = A.getScaleOf();
            trueA = P.mult(trueA);
            trueb = P.mult(trueb);
            MathLib.enableRound( true );
            
            name = A.name;
            A = P.mult(A);
            A.name = name;
            
            name = b.name;
            b = P.mult(b);
            b.name = name;
        } 

        Vector x = A.solveX(b);

        
        // ####### Alle folgenden Berechnungen werden mit "maximaler" Präzision ausgeführt #########
        MathLib.enableRound( false );
        Matrix  invA = trueA.getInverse();
        Matrix AinvA = trueA.mult(invA);
        Vector invAb = invA.mult(trueb);
        Vector     r = trueA.mult(x).sub(b);
        MathLib.enableRound( true );

        BigDecimal     kappa = invA.norm().multiply( trueA.norm() );
        BigDecimal relFehler = kappa.multiply( r.norm().divide( b.norm(), MathLib.getPrecision(), RoundingMode.HALF_UP) );
       
        
        // ####### Ausgabe mit niedriger Präzision / Achtung! Ausgabe sollte Mantissengenauigkeit haben. #########
        LatexFormula formula = new LatexFormula();
        
        formula.addTextBold("2. ");
        formula.addColorBoxBegin("green").addText("LU-Zerlegung mit Pivotstrategie und Fehlerabschätzung").addColorBoxEnd().addNewLine(1);
        
        formula.addNewLine(2);
        formula.addText("Löse Gleichung der Form ").addLatexString(A.name+" \\cdot x = "+b.name).addNewLine(2);
        formula.addText(A.name+" = ").addMatrix(A).addText(", "+b.name+" = ").addVector(b).addNewLine(2);
        formula.addFormula( recorder.get( true ) );
        formula.addText("x = ").addVector(x).addText(",     Exakt: "+A.name+"^{-1}").addSymbol("*").addText(b.name+" = ");
        formula.addVector(invAb).addNewLine(2);
        formula.addText(A.name+"^{-1} = ").addMatrix(invA).addNewLine(4);

        MathLib.setRoundingMode( MathLib.NORMAL );
        formula.addText(A.name).addSymbol("*").addText(A.name+"^{-1} = ").addMatrix(AinvA).addNewLine(3);
        formula.addTextUL("relativer\\;Fehler\\;als\\;obere\\;Schranke:").addNewLine(1);
        formula.addSymbol("kappa").addText("("+A.name+") = ").addMatrixNorm(A.name).addSymbol("*").addMatrixNorm(A.name+"^{-1}");
        formula.addText(" = "+kappa).addNewLine(1);
        formula.addLatexString("r = "+A.name+" \\cdot ").addLatexString("x - "+b.name+" = ").addVector(r).addNewLine(1);
        formula.addRelError("x").addText(" = ").addSymbol("kappa").addText("("+A.name+")").addSymbol("*").addVektornormXdivY("r", b.name, true);
        formula.addLatexString("\\;\\;\\le\\;\\;").addText( ""+relFehler ).addNewLine(4);
        
        formula.addTextUL("relativer\\;Fehler\\;als\\;exakter\\;Wert:").addNewLine(1);
        formula.addRelError("x").addText(" = ");
        formula.addVektornormXdivY("x-"+A.name+"^{-1}"+b.name, A.name+"^{-1}"+b.name, false);
        formula.addText(" = "+ x.sub(invAb).norm().divide(invAb.norm(), MathLib.getPrecision(), RoundingMode.HALF_UP) ).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
