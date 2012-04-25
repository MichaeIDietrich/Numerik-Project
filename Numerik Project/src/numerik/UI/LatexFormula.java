package numerik.UI;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class LatexFormula {
  
  private StringBuilder formula;
  
  public LatexFormula() {
    formula = new StringBuilder();
  }
  
  public LatexFormula(String formula) {
    this.formula = new StringBuilder(formula);
  }
  
  public void clear() {
    formula = new StringBuilder();
  }
  
  public void addString(String str) {
    
  }
  
  public Image toImage() {
    
    TeXFormula fomule = new TeXFormula(formula.toString());
    TeXIcon icon = fomule.createTeXIcon(
    TeXConstants.STYLE_DISPLAY, 20);
    BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    icon.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
    
    return b;
  }
  
}
