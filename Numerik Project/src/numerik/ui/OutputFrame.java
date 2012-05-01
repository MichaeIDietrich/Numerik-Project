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
    
    Recorder recorder = Recorder.getInstance();
    
    MathLib.setPrecision( 5 );
    MathLib.setPivotStrategy( true );
    MathLib.setRoundingMode( MathLib.exact );
 
    Matrix A = new Matrix("Matrix1.txt");
    A.name = "A";
    A.setNormalized( false );
    
    BigDecimal[] data = {new BigDecimal(6.5),new BigDecimal(-5.3),new BigDecimal(2.9)};//,new BigDecimal(1.9),new BigDecimal(1.9)};
    Vector b = new Vector( data );
    b.name = "b";
    
    Matrix  invA = A.getInverse();
    Matrix AinvA = A.mult(invA); 
    Matrix     L = A.getL(null);
    Matrix     U = A.getU(null);
    Vector     x = A.determineX(b);
    Vector invAb = invA.mult(b);
    Vector     r = A.mult(x).sub(b);
    BigDecimal kappa = invA.zsnorm().multiply( A.zsnorm() );
    BigDecimal relFehler = kappa.multiply( r.zsnorm().divide( b.zsnorm(), MathLib.getPrecision(), RoundingMode.HALF_UP) );
    
    final LatexFormula formula = new LatexFormula();
       
    formula.addNewLine(2).addText("A = ").addMatrix(A).addText(", b = ").addVector(b).addNewLine(2);
    formula.addFormula( recorder.get( true ) );
    formula.addText("x = ").addVector(x).addText(",     Exakt: (A)^{-1}").addSymbol("*").addText("b = ").addVector(invAb).addNewLine(2);
    formula.addText("A^{-1} = ").addMatrix(invA).addNewLine(2);

    MathLib.setRoundingMode( MathLib.normal );
    formula.addText("A").addSymbol("*").addText("A^{-1} = ").addMatrix(AinvA).addNewLine(3);
    formula.addSymbol("kappa").addText("(A) = ").addNormVariable("A").addSymbol("*").addNormVariable("A^{-1}").addText(" = "+kappa).addNewLine(2);
    formula.addRelError("x").addText(" = ").addSymbol("kappa").addText("(A)").addSymbol("*").addNormXdivY("r","b").addText( " = "+relFehler );
    
    
    
    JScrollPane scrollpane = new JScrollPane( new ImageComponent(formula.toImage(18)) );
    scrollpane.getVerticalScrollBar().setUnitIncrement(20);
    this.add(scrollpane);
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
}
