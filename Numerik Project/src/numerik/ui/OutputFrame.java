package numerik.ui;

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

public class OutputFrame extends JFrame {
  
  public OutputFrame() {
    super("Numerik");
    
    MathLib.setPrecision(12);
    
    Matrix A    = new Matrix("Matrix1.txt");
//    Matrix invA = A.getInverse();
//    Matrix AinvA = A.mult(invA); 
    Matrix L    = A.getL();
    Matrix U    = A.getU();
    
    final LatexFormula formula = new LatexFormula();
       
    formula.addText("A = "); 	formula.addMatrix(A); 	formula.addNewLine(2);
    formula.addText("L = ");	formula.addMatrix(L); 	formula.addNewLine(2);
    formula.addText("U = ");	formula.addMatrix(U); 	formula.addNewLine(2);
//    formula.addMatrix(AinvA);
    
    this.add(new JScrollPane(new ImageComponent(formula.toImage(18))));
    
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);

  }
}
