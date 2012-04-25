package numerik.UI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class OutputFrame extends JFrame {
  
  public OutputFrame() {
    super("Numerik");
    
    LatexFormula formula = new LatexFormula("M={\\begin{pmatrix} a_1 & a_2 & a_3 \\\\ b_1 & b_2 & b_3 \\\\ c_1 & c_2 & c_3  \\end{pmatrix}}^{T}");
    
    JLabel lblImage = new JLabel();
    lblImage.setIcon(new ImageIcon(formula.toImage()));
    
    
    this.add(new JScrollPane(lblImage));
    
    
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
}
