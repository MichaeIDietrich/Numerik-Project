package numerik.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.tasks.LUDecomposition;
import numerik.tasks.NewtonIteration;
import numerik.tasks.SolveNonLinearEquation;

public class OutputFrame extends JFrame 
{
    
  LatexFormula formula = new LatexFormula();
 
  
  public OutputFrame() throws IOException 
  {
    super("Numerik");
    
    LUDecomposition         luDecomp = new LUDecomposition();
    NewtonIteration       newtonIter = new NewtonIteration();
    SolveNonLinearEquation nonLinEqu = new SolveNonLinearEquation();
    
//    formula = luDecomp.getFormula();
    formula = newtonIter.getFormula();
//    formula = nonLinEqu.getFormula();
    
    ImageComponent imgcomp = new ImageComponent( formula.toImage( 18 ));
    JScrollPane scrollpane = new JScrollPane( imgcomp );
    scrollpane.getVerticalScrollBar().setUnitIncrement( 25 );
    this.add(scrollpane);
    
    this.setSize( 660, 800 );
    this.setLocationRelativeTo( null );
    
    this.setDefaultCloseOperation( EXIT_ON_CLOSE );
    this.setVisible( true ); 
  }
}
