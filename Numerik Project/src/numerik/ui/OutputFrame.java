package numerik.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;

public class OutputFrame extends JFrame {
  
  public OutputFrame() throws IOException {
      
    super("Numerik");
    
    Recorder recorder = Recorder.getInstance();
    
    // ####### Alle Berechnungen werden mit niedriger Präzision ausgeführt #########
    
    MathLib.setPrecision( 5 );
    MathLib.setPivotStrategy( true );
    MathLib.setRoundingMode( MathLib.exact );
    MathLib.setInversePrecision( 20 );
    
    Matrix A = new Matrix("Matrix1.txt");
    Matrix P = A.getScaleOf();
    A.name   = "A";

    BigDecimal[] data = {new BigDecimal(6.5),new BigDecimal(-5.3),new BigDecimal(2.9),new BigDecimal(1.9),new BigDecimal(1.9)};
    Vector b = new Vector( data );
    b.name = "b";
    
    Matrix   scA = A;
    Vector   scb = b;
    Vector     x = scA.determineX(scb);
    
    
    // ####### Alle folgenden Berechnungen werden mit höherer Präzision ausgeführt #########
    
    MathLib.setPrecision( 20 );
    Matrix  invA = scA.getInverse();
    Matrix AinvA = scA.mult(invA); 
    Vector invAb = invA.mult(scb);
    Vector     r = scA.mult(x).sub(scb);
    BigDecimal kappa = invA.zsnorm().multiply( scA.zsnorm() );
    BigDecimal relFehler = kappa.multiply( r.zsnorm().divide( scb.zsnorm(), MathLib.getPrecision(), RoundingMode.HALF_UP) );
    
    
    // ####### Ausgabe wieder mit niedriger Präzision / Achtung! Ausgabe sollte Mantissengenauigkeit haben. #########
    
    final LatexFormula formula = new LatexFormula();
       
    MathLib.setPrecision( 5 );
    formula.addNewLine(2).addText("scA = ").addMatrix(scA).addText(", scb = ").addVector(scb).addNewLine(2);
    formula.addFormula( recorder.get( true ) );
    formula.addText("x = ").addVector(x).addText(",     Exakt: (A)^{-1}").addSymbol("*").addText("b = ").addVector(invAb).addNewLine(2);
    formula.addText("A^{-1} = ").addMatrix(invA).addNewLine(2);

    
    MathLib.setRoundingMode( MathLib.normal );
    formula.addText("A").addSymbol("*").addText("A^{-1} = ").addMatrix(AinvA).addNewLine(3);
    formula.addSymbol("kappa").addText("(A) = ").addNormVariable("A").addSymbol("*").addNormVariable("A^{-1}").addText(" = "+kappa).addNewLine(2);
    formula.addRelError("x").addText(" = ").addSymbol("kappa").addText("(A)").addSymbol("*").addNormXdivY("r","b").addText( " = "+relFehler );
    
    System.out.println( A.det() );
    

    JScrollPane scrollpane = new JScrollPane( new ImageComponent(formula.toImage(18)) );
    scrollpane.getVerticalScrollBar().setUnitIncrement(20);
    this.add(scrollpane);
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
}
