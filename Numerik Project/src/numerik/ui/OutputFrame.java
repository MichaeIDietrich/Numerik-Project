package numerik.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class OutputFrame extends JFrame {
  
  public OutputFrame() {
    super("Numerik");
    
    final LatexFormula formula = new LatexFormula("M={\\begin{pmatrix} a_1 & a_2 & a_3 \\\\ b_1 & b_2 & b_3 \\\\ c_1 & c_2 & c_3  \\end{pmatrix}}^{T}");
    
    
    this.add(new JScrollPane(new ImageComponent(formula.toImage())));
    
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
}
