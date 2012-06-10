package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.*;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;


public class JacobiIteration implements Task
{
    private TaskPane taskPane;

    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {       
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Matrix:", ArgType.MATRIX, 100), 
                                           new Argument("Vektor:", ArgType.VECTOR, 100),
                                           new Argument("n = ", ArgType.PRECISION, "20"),
                                           new Argument("Mant = ", ArgType.PRECISION,  "7"),
                                               Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        // predefinition
        MathLib.enableRound( true );
        MathLib.setRoundingMode( MathLib.EXACT ); 
        MathLib.setNorm( MathLib.ZEILENSUMMENNORM );
        
        LatexFormula formula  = new LatexFormula();
        Recorder recorder = Recorder.getInstance();
        recorder.clear();
        
        // Übergabe Parameter aus Toolbar
        int iterationen = parameters[2].toDecimal().intValue();
        Matrix A = parameters[0].toMatrix();
        Vector b = parameters[1].toVector();
        MathLib.setPrecision( parameters[3].toDecimal().intValue() ); 

        // Berechne M und c
        Matrix matrixInput = A.getDiagonalMatrix().getInverse().mult(BigDecimal.ONE.negate())
                              .mult( (A.subtract( A.getDiagonalMatrix() )) );
        Vector vectorInput = A.getDiagonalMatrix().getInverse().mult(b);

        // Hole Startvektor x_0
        Vector startVectorX0 = setStartVector(4);
        
        BigDecimal normM = matrixInput.norm();
        
        
        // Ausgabe
        formula.addText("2.3. ").addColorBoxBegin("green");
        formula.addText("Iteratives Verfahren zur Lösung linearer Gleichungssysteme").addColorBoxEnd().addNewLine(3);
        formula.addLatexString("\\bullet").addHorizSpace(10).addText(A.name+" sei lineares Gleichungssystem").addNewLine(1);
        formula.addLatexString("\\bullet").addHorizSpace(10).addText("B sei Trace("+A.name+")").addNewLine(1);
        formula.addLatexString("\\bullet").addHorizSpace(10).addText("M sei Substitution für -B^{-1}{\\cdot}("+A.name+"-B)").addNewLine(1);
        formula.addLatexString("\\bullet").addHorizSpace(10).addText("c sei Substitution für B^{-1}{\\cdot}"+b.name).addNewLine(2);
        formula.addText("löse allgemeine Gleichung:").addHorizSpace(10).addLatexString("x^{k+1} = M \\cdot x^{*} + c").addNewLine(3);
        
        formula.addTextUL("Berechne\\;dafür\\;M\\;und\\;c").addNewLine(1);
        formula.addText("M = ").addMatrix(matrixInput).addText(", ").addNewLine(1);
        formula.addText("c = ").addVector(vectorInput).addNewLine(3);
        
        formula.addTextUL("Wähle\\;Startvektor\\;mit").addNewLine(1);
        formula.addText("Zum ändern des Startvektors ").addLatexString("x_0").addText(", den Vektor in der Klasse").addNewLine(1);
        formula.addText("Jakobi-Iteration/setStartVector ändern.").addNewLine(1);
        formula.addLatexString("x_0").addText(" = ").addVector(startVectorX0).addNewLine(4);
        
        formula.addTextUL("Prüfe\\;Konvergenzverhalten - \\;existiert\\;eine\\;Lösung?").addNewLine(1);
        formula.addText(" \\rho(M) ").addLEQ().addMatrixNorm("M").addText(" = "+normM);
        
        MatrixIterationMethods.jacobiIteration(matrixInput, vectorInput, startVectorX0, iterationen);
        
        if (normM.doubleValue() <= 1) formula.addLatexString("\\leq 1"); 
                                else  
                                {
                                    formula.addLatexString("\\nleq 1").addNewLine(1);
                                    formula.addColorBoxBegin("red").addText("Achtung \\rho(M) \\nleq 1 \\rightarrow divergent, keine Lösung")
                                           .addColorBoxEnd();
                                    recorder.clear();
                                    recorder.add( new LatexFormula().addText("Keine Lösung! Der Lösungsvektor divergiert.") );
                                }

        formula.addNewLine(4).addTextUL("Beginne\\;mit\\;Iteration").addNewLine(1);
        formula.addLatexString("x_0 = ").addVector(startVectorX0).addNewLine(1).addFormula( recorder.get(true) );
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }

    
    // hier den Startvektor x_0 eintragen
    private Vector setStartVector(int length)
    {
        Vector startVectorX0 = new Vector(length);
        
        startVectorX0.set(0, new BigDecimal("0"));
        startVectorX0.set(1, new BigDecimal("0"));
        startVectorX0.set(2, new BigDecimal("0"));
        startVectorX0.set(3, new BigDecimal("0"));
        
        return startVectorX0;
    }
}
