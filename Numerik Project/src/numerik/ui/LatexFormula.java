package numerik.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JLabel;

import numerik.calc.MathLib;
import numerik.calc.Matrix;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class LatexFormula
{
    // package scope
    StringBuilder formula;
    
    private static  HashMap<String, String> characterTable;
    
    static
    {
        // weitere Sonderzeichen hinzufügen
        
        characterTable = new HashMap<String, String>();
        //characterTable.put("kappa", "\\{\\kappa}");
        characterTable.put("mal", "\\cdot");
        characterTable.put("*", "\\cdot");
        
    }
    
    
    public LatexFormula()
    {
        formula = new StringBuilder();
    }
    
    
    public LatexFormula(String latexFormula)
    {
        formula = new StringBuilder(latexFormula);
    }
    
    
    public void clear()
    {
        formula = new StringBuilder();
    }
    
    
    public LatexFormula addLatexFormula(String latexFormula)
    {
        formula.append(latexFormula);
        
        return this;
    }
    
    
    public LatexFormula addText(String text)
    {
        formula.append("\\text{");
        formula.append(text);
        formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addText(String str, boolean bold, boolean italic, boolean underline)
    {
        if (bold) formula.append("\\textbf{");
        if (italic) formula.append("\\textit{");
        if (underline) formula.append("\\underline{");
        formula.append(str);
        if (bold) formula.append("}");
        if (italic) formula.append("}");
        if (underline) formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addExponent(String exponent)
    {
        formula.append("^{");
        formula.append(exponent);
        formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addIndex(String index)
    {
        formula.append("_{");
        formula.append(index);
        formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addFraction(String upperPart, String lowerPart)
    {
        formula.append("\\frac {");
        formula.append(upperPart);
        formula.append("}{");
        formula.append(lowerPart);
        formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addFraction()
    {
        formula.append("\\frac");
        
        return this;
    }
    
    
    public LatexFormula startGroup()
    {
        formula.append("{");
        
        return this;
    }
    
    
    public LatexFormula endGroup()
    {
        formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addSpecialCharacter(String name)
    {
        if (characterTable.containsKey(name))
        {
            formula.append(characterTable.get(name));
        }
        else
        {
            //formula.append("\\{\\");
            formula.append("\\");
            formula.append(name);
            //formula.append("}");
        }
        
        return this;
    }
    
    
    public LatexFormula addNewLine()
    {
        formula.append("\\\\");
        
        return this;
    }
    
    public LatexFormula addNewLine(int lineCount)
    {
        for (int i = 0; i < lineCount; i++)
            formula.append("\\\\");
        
        return this;
    }
    
    public LatexFormula addMatrix(Matrix matrix) 
    {
        formula.append("\\begin{pmatrix}");
        for (int n = 0; n < matrix.getRows(); n++)
        {
          for (int m = 0; m < matrix.getCols(); m++)
          {
                formula.append(MathLib.round(matrix.get(n, m)));
                
                if (m < matrix.getCols() - 1)
                    formula.append("&");
            }
            
            if (n < matrix.getRows() - 1)
                formula.append("\\\\");
        }
        formula.append("\\end{pmatrix}");
        
        return this;
    }
    
    
    public LatexFormula addNormVariable(String variable)
    {
        formula.append("\\lVert");
        formula.append(variable);
        formula.append("\\rVert");
        
        return this;
    }
    
    
    public LatexFormula addFormula(LatexFormula formula)
    {
        formula.addLatexFormula(formula.formula.toString());
        
        return this;
    }
    
    
    public Image toImage()
    {
        return toImage(20);
    }
    
    
    public Image toImage(int size)
    {
        System.out.println(formula.toString());

        TeXFormula texFormula = new TeXFormula(formula.toString() + "\\end{array}");
        TeXIcon    		 icon = texFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
        BufferedImage 		b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        icon.paintIcon(new JLabel(), b.getGraphics(), 0, 0);

        return b;
    }
}
