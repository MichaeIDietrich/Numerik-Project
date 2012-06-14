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

public class InputErrorEstimation implements Task
{
    
    TaskPane taskPane;
    LatexFormula formula = new LatexFormula();
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;  
        taskPane.createJToolBarByArguments(
                new Argument("Matrix A =", ArgType.MATRIX, 100),
                new Argument("Matrix \u0394A =", ArgType.MATRIX, 100),
                Argument.PRECISION, Argument.RUN_BUTTON,
                new Argument("Vektor b =", ArgType.VECTOR, 100),
                new Argument("Vektor \u0394b =", ArgType.VECTOR, 100));
                
    }

    @Override
    public void run(Value... parameters)
    {
        
        MathLib.setNorm( MathLib.ZEILENSUMMENNORM );
                
        Matrix    A = parameters[0].toMatrix();
        Matrix   dA = parameters[1].toMatrix();
        Matrix invA = A.getInverse();
        Vector    b = parameters[3].toVector();
        Vector   db = parameters[4].toVector();

        BigDecimal kappa = A.norm().multiply( invA.norm() );
        BigDecimal norm_dAdivA = dA.norm().divide(A.norm(), MathLib.getPrecision(), RoundingMode.HALF_DOWN);
        BigDecimal norm_dbdivb = db.norm().divide(b.norm(), MathLib.getPrecision(), RoundingMode.HALF_DOWN);
        BigDecimal result      = kappa.divide(BigDecimal.ONE.subtract(kappa.multiply(norm_dAdivA)), MathLib.getPrecision(), RoundingMode.HALF_DOWN ).multiply(norm_dbdivb.add(norm_dAdivA));     
        
        formula.clear();
        
        formula.addText("2.2 B) ii) ").addColorBoxBegin("green");
        formula.addText("Eingabefehler einer Matrix und eines Vektors abschätzen").addColorBoxEnd().addNewLine(3);
        
        formula.addText(" A = ").addMatrix(A).addText(",").addHorizSpace(10).addText(" b = ").addVector(b);
        formula.addNewLine(1);
        formula.addText("\\Delta{A} = ").addMatrix(dA).addText(",").addHorizSpace(10).addText("\\Delta{b} = ").addVector(db);
        formula.addNewLine(4);
        
        formula.addTextUL("Benutze\\;Ungleichung").addNewLine();
        formula.addRelErrorDeltaInput().addNewLine(3);

        formula.addTextUL("Berechne\\;Zwischenwerte").addNewLine();
        formula.addSymbol("kappa").addText("(A) = ").addMatrixNorm("A").addSymbol("*").addMatrixNorm("A^{-1}").addText(" = "+kappa).addNewLine(2);
        formula.addRelError("A").addText(" = "+norm_dAdivA).addNewLine(1);
        formula.addRelError("b").addText(" = "+norm_dbdivb).addNewLine(4);
        
        formula.addTextUL("Berechne\\;Fehler;\\;setze\\;Zwischenwerte\\;in\\;Ungleichung\\;ein").addNewLine();
        formula.addRelError("x").addText(" \\leq "+ result).addNewLine(3);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}
