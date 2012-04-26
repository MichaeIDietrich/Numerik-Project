package numerik.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import numerik.calc.Matrix;

public class OutputFrame extends JFrame {
  
  public OutputFrame() {
    super("Numerik");
    
    final LatexFormula formula = new LatexFormula();
    //formula.addLatexFormula("\\{ \\kappa} ");
    formula.addSpecialCharacter("kappa");
    formula.addText("(x)=");
    formula.addMatrix(Matrix.getIdentity(4));
    formula.addExponent("T");
    formula.addNewLine();
    
    formula.addFraction();
    
    formula.startGroup();
    formula.addSpecialCharacter("kappa");
    formula.addSpecialCharacter("*");
    formula.addText("x");
    formula.endGroup();
    
    formula.startGroup();
    formula.addSpecialCharacter("kappa");
    formula.addSpecialCharacter("*");
    formula.addText("x");
    formula.endGroup();
    
    formula.addText("=z");
    
    formula.addNewLine();
    formula.addText("hallo");
    formula.addNewLine();
    formula.addText("hallo", false, true, false);
    
    
    this.add(new JScrollPane(new ImageComponent(formula.toImage())));
    
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
}
