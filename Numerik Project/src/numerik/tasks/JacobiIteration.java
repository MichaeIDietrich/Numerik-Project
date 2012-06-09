package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.*;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.ImageComponent;
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
        MathLib.enableRound( true );
        MathLib.setRoundingMode( MathLib.NORMAL ); 
        MathLib.setNorm( MathLib.ZEILENSUMMENNORM );
        
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Matrix:", ArgType.MATRIX, 100), 
                                           new Argument("Vektor:", ArgType.VECTOR, 100),
                                               Argument.PRECISION, Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        LatexFormula formula  = new LatexFormula();
        Recorder recorder = Recorder.getInstance();
        recorder.clear();
        
        Matrix A = parameters[0].toMatrix();
        Vector b = parameters[1].toVector();
        MathLib.setPrecision( parameters[2].toDecimal().intValue() ); 
        
        Matrix matrixInput;
        Vector vectorInput;
        
        matrixInput = A.getDiagonalMatrix().getInverse().mult(BigDecimal.ONE.negate()).mult( (A.subtract( A.getDiagonalMatrix() )) );
        vectorInput = A.getDiagonalMatrix().getInverse().mult(b);
        
//        matrixInput = new Matrix(4, 4);
//        
//        matrixInput.set(0, 0, new BigDecimal("0"));
//        matrixInput.set(0, 1, new BigDecimal("0.1"));
//        matrixInput.set(0, 2, new BigDecimal("-0.2"));
//        matrixInput.set(0, 3, new BigDecimal("0"));
//        
//        matrixInput.set(1, 0, new BigDecimal("0.0909091"));
//        matrixInput.set(1, 1, new BigDecimal("0"));
//        matrixInput.set(1, 2, new BigDecimal("0.0909091"));
//        matrixInput.set(1, 3, new BigDecimal("-0.272727"));
//        
//        matrixInput.set(2, 0, new BigDecimal("-0.2"));
//        matrixInput.set(2, 1, new BigDecimal("0.1"));
//        matrixInput.set(2, 2, new BigDecimal("0"));
//        matrixInput.set(2, 3, new BigDecimal("0.1"));
//        
//        matrixInput.set(3, 0, new BigDecimal("0"));
//        matrixInput.set(3, 1, new BigDecimal("-0.375"));
//        matrixInput.set(3, 2, new BigDecimal("0.125"));
//        matrixInput.set(3, 3, new BigDecimal("0"));
//        
//        vectorInput = new Vector(4);
//        
//        vectorInput.set(0, new BigDecimal("0.6"));
//        vectorInput.set(1, new BigDecimal("2.27273"));
//        vectorInput.set(2, new BigDecimal("-1.1"));
//        vectorInput.set(3, new BigDecimal("1.875"));
//        
        Vector startVectorX0 = new Vector(4);
        
        startVectorX0.set(0, new BigDecimal("0"));
        startVectorX0.set(1, new BigDecimal("0"));
        startVectorX0.set(2, new BigDecimal("0"));
        startVectorX0.set(3, new BigDecimal("0"));
        
        
        BigDecimal normM = matrixInput.norm();
        
        formula.addText("M = ").addMatrix(matrixInput).addText(", ").addNewLine(1);
        formula.addText("x = ").addVector(vectorInput).addNewLine(2);
        formula.addText(" \\rho(M) ").addLEQ().addMatrixNorm("M").addText(" = "+normM).addNewLine(1);
        if (normM.compareTo( BigDecimal.ONE )==-1 || normM.compareTo( BigDecimal.ONE )==0) {
            formula.addLatexString("\\rightarrow ").addText("Lösung existiert, da \\rho(M)").addLEQ().addText("1").addNewLine(1);
        }
        
        
        MatrixIterationMethods.jacobiIteration(matrixInput, vectorInput, startVectorX0, 20);
         
        formula.addNewLine(4).addTextUL("Beginne\\;mit\\;Iteration").addNewLine(1).addFormula( recorder.get(true) );
        
        
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
