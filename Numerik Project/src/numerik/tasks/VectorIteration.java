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

public class VectorIteration implements Task
{
    private TaskPane taskPane;

    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {       
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Matrix:", ArgType.MATRIX, 100), 
                                           new Argument("Vektor:", ArgType.VECTOR, 100),
                                           new Argument("Startvektor:", ArgType.VECTOR, 100),
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
        
        LatexFormula formula  = new LatexFormula();
        Recorder recorder = Recorder.getInstance();
        recorder.clear();
        
        // Übergabe Parameter aus Toolbar
        MathLib.setPrecision( parameters[4].toDecimal().intValue() ); 
        int       iterations = parameters[3].toDecimal().intValue();
        Vector startVectorY0 = parameters[2].toVector();
        Matrix A = parameters[0].toMatrix();


        // Berechne M
        Matrix matrixInput = MatrixIterationMethods.getM(A);
        
        BigDecimal normM = matrixInput.norm();
        
        formula.addTextBold("2.4. ").addColorBoxBegin("green");
        formula.addText("Iteratives Verfahren zur Bestimmung").addText(" d. größten Eigenwertes ");
        formula.addLatexString("\\lambda").addColorBoxEnd().addNewLine(3);
        formula.addLatexString("\\bullet").addHorizSpace(10).addText(A.name+" sei lineares Gleichungssystem").addNewLine(1);
        formula.addLatexString("\\bullet").addHorizSpace(10).addText("M sei Substitution für -B^{-1}{\\cdot}("+A.name+"-B)").addNewLine(3);
        
        formula.addTextUL("Berechne\\;dafür\\;M").addNewLine(1);
        formula.addText("M = ").addMatrix(matrixInput).addText(", ").addNewLine(3);
        
        formula.addTextUL("Wähle\\;Startvektor").addNewLine(1);
        formula.addLatexString("y_0").addText(" = ").addVector(startVectorY0).addNewLine(4);
        
        formula.addTextUL("Prüfe\\;Konvergenzverhalten - \\;existiert\\;eine\\;Lösung?").addNewLine(1);
        formula.addText(" \\rho(M) ").addLEQ().addMatrixNorm("M").addText(" = "+normM);
        
        MatrixIterationMethods.vectorIteration(matrixInput, startVectorY0, iterations);
        
        if (normM.doubleValue() <= 1) 
        {
            formula.addLatexString("\\leq 1"); 
        }
        else  
        {
            formula.addLatexString("\\nleq 1").addNewLine(1);
            formula.addColorBoxBegin("red").addText("Achtung \\rho(M) \\nleq 1 \\rightarrow divergent, keine Lösung")
                   .addColorBoxEnd();
            recorder.clear();
            recorder.add( new LatexFormula().addText("Keine Lösung! Der Lösungsvektor divergiert.") );
        }
        
        formula.addNewLine(4).addTextUL("Beginne\\;mit\\;Iteration").addNewLine(2);
        formula.addLatexString("y_0 = ").addVector( startVectorY0 ).addNewLine(2).addFormula( recorder.get(true) );
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
