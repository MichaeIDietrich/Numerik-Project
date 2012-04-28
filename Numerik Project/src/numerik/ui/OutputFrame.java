package numerik.ui;

import java.io.IOException;
import java.math.BigDecimal;
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
    
    MathLib.setPrecision(5);
    
    BigDecimal[] data = {new BigDecimal(0.00129222083),new BigDecimal(-0.299435028),new BigDecimal(0.247863248)};
    Vector b = new Vector( data );
    
    Matrix A = new Matrix("Matrix1.txt");
    Matrix invA  = A.getInverse();
    Matrix AinvA = A.mult(invA); 
    Matrix L = A.getL();
    Matrix U = A.getU();
    Vector x = A.determineX(b);
    
    final LatexFormula formula = new LatexFormula();
       
    formula.addText("A = ").addMatrix(A).addText(", A = ").addMatrix(b).addNewLine(2);
    formula.addText("L = ").addMatrix(L).addNewLine(2);
    formula.addText("U = ").addMatrix(U).addNewLine(2);
    formula.addText("x = ").addMatrix(x).addNewLine(2);
    formula.addText("A^{-1} = ").addMatrix(invA).addNewLine(2);
    formula.addText("A").addSymbol("*").addText("A^{-1} = ").addMatrix(AinvA).addNewLine(2);
    
    this.add(new JScrollPane(new ImageComponent(formula.toImage(18))));

    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
}
