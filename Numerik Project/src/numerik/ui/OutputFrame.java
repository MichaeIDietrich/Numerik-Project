package numerik.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;

public class OutputFrame extends JFrame {
  
  public OutputFrame() throws IOException {
      
    super("Numerik");
    
    MathLib.setPrecision( 5 );
    MathLib.setPivotStrategy( true );
    MathLib.setRoundingMode( MathLib.exact );
 
    Matrix A = new Matrix("Matrix1.txt");
    A.setNormalized( true );
    
    BigDecimal[] data = {new BigDecimal(6.5),new BigDecimal(-5.3),new BigDecimal(2.9)};//,new BigDecimal(1.9),new BigDecimal(1.9)};
    Vector b = new Vector( data );
    
    Matrix  invA = A.getInverse();
    Matrix AinvA = A.mult(invA); 
    Matrix     L = A.getL(null);
    Matrix     U = A.getU(null);
    Matrix    LU = L.mult(U);
    Vector     x = A.determineX(b);
    Matrix invAb = invA.mult(b);
    
    final LatexFormula formula = new LatexFormula();
       
    formula.addText("A = ").addMatrix(A).addText(", b = ").addMatrix(b).addNewLine(2);
    formula.addText("L = ").addMatrix(L).addNewLine(2);
    formula.addText("U = ").addMatrix(U).addNewLine(2);
    formula.addText("Probe: L").addSymbol("*").addText("U = ").addMatrix(LU).addNewLine(2);
    formula.addText("x = ").addMatrix(x).addText(",     Exakt: (A)^{-1}").addSymbol("*").addText("b = ").addMatrix(invAb).addNewLine(2);
    formula.addText("A^{-1} = ").addMatrix(invA).addNewLine(2);
    
    MathLib.setRoundingMode( MathLib.normal );
    formula.addText("A").addSymbol("*").addText("A^{-1} = ").addMatrix(AinvA).addNewLine(2);
    
    JScrollPane scrollpane = new JScrollPane( new ImageComponent(formula.toImage(18)) );
    scrollpane.getVerticalScrollBar().setUnitIncrement(20);
    this.add(scrollpane);
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    
    System.out.println( "\n Kappa(A) = " + invA.zsnorm().multiply(A.zsnorm()) );
  }
}
