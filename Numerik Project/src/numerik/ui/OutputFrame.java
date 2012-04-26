package numerik.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import numerik.calc.MathLib;
import numerik.calc.Matrix;

public class OutputFrame extends JFrame {
  
  public OutputFrame() {
    super("Numerik");
    
    MathLib.setPrecision(10);
    
    Matrix A    = new Matrix("Matrix1.txt");
    Matrix invA = A.getInverse();
    Matrix AinvA = A.mult(invA);  
    
    final LatexFormula formula = new LatexFormula();
       
    formula.addMatrix(A);
    formula.addNewLine(2);
    formula.addMatrix(invA);
    formula.addNewLine(2);
    formula.addMatrix(AinvA);
    
    this.add(new JScrollPane(new ImageComponent(formula.toImage())));
    
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
}
