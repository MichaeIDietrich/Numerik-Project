package numerik.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.swing.JLabel;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import numerik.calc.*;
import numerik.expression.Value;


public class LatexFormula
{
    private StringBuilder formula;
    
    private static HashMap<String, String> characterTable;
    
    
    static
    {
        // weitere Sonderzeichen hinzufügen
        
        characterTable = new HashMap<String, String>();
        characterTable.put( "mal", "\\cdot" );
        characterTable.put( "*",   "\\cdot" );
        
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
    
    
    public LatexFormula addLatexString(String latexFormula)
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
        if (bold)
            formula.append("\\textbf{");
        if (italic)
            formula.append("\\textit{");
        if (underline)
            formula.append("\\underline{");
        formula.append(str);
        if (bold)
            formula.append("}");
        if (italic)
            formula.append("}");
        if (underline)
            formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addTextBold(String str)
    {
        return addText(str, true, false, false);
    }
    
    
    public LatexFormula addTextUL(String str)
    {
        return addText(str, false, false, true);
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
    
    
    public LatexFormula addSolidLine()
    {
        formula.append("\\hline");
        
        return this;
    }
    
    
    public LatexFormula addFraction()
    {
        formula.append("\\frac");
        
        return this;
    }
    
    
    public LatexFormula addTildeText(String text)
    {
        
        formula.append("\\widetilde{");
        formula.append(text);
        formula.append("}");
        
        return this;
    }
    
    
    public LatexFormula addRelError(String text)
    {
        
        addVektornormXdivY("\\Delta{" + text + "}", text, false);
        
        return this;
    }
    
    
    public LatexFormula addVektornormXdivY(String x, String y, boolean showindex)
    {
        String index = "";
        if (showindex) {
            if(MathLib.getNorm()==0) index = "g";
            if(MathLib.getNorm()==1) index = "2";
        }
        
        formula.append("\\frac{");
        
        formula.append("\\lVert{");
        formula.append(x);
        formula.append("}\\rVert_{"+index);
        formula.append("}}{\\lVert{");
        formula.append(y);
        formula.append("}\\rVert_{"+index);
        
        formula.append("}}");
        
        return this;
    }
    
    
    public LatexFormula addMatrixNorm(String variable)
    {
        String index = "";
        if(MathLib.getNorm()==0) index = "z";
        if(MathLib.getNorm()==1) index = "f";
        
        formula.append("\\lVert{");
        formula.append(variable);
        formula.append("}\\rVert_"+index);
        
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
    
    
    public LatexFormula addSymbol(String name)
    {
        if (characterTable.containsKey(name))
        {
            formula.append(characterTable.get(name));
        }
        else
        {
            formula.append("\\");
            formula.append(name);
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
                formula.append(MathLib.round(matrix.get(n, m)).toPlainString());
                
                if (m < matrix.getCols() - 1)
                    formula.append("&");
            }
            
            if (n < matrix.getRows() - 1)
                formula.append("\\\\");
        }
        formula.append("\\end{pmatrix}");
        
        return this;
    }
    
    
    public LatexFormula addVector(Vector vector)
    {
        addMatrix(vector.toMatrix());
        return this;
    }
    
    
    public LatexFormula addDecimal(BigDecimal value)
    {
        formula.append(MathLib.round(value).toPlainString());
        
        return this;
    }
    
    
    public LatexFormula addDecimal(double value)
    {
        return addDecimal(BigDecimal.valueOf(value));
    }
    
    
    public LatexFormula addValue(Value value)
    {
        switch (value.getType())
        {
            case DECIMAL:
                addDecimal(value.toDecimal());
                break;
            case MATRIX:
                addMatrix(value.toMatrix());
                break;
            case VECTOR:
                addVector(value.toVector());
                break;
            default:
                addText(value.toObject().toString());
        }
        
        return this;
    }
    
    
    public LatexFormula addFormula(LatexFormula formula)
    {
        this.formula.append(formula.formula.toString());
        
        return this;
    }
    
    
    public LatexFormula startAlignedEquation()
    {
        formula.append("\\begin{eqnarray}");
        
        return this;
    }
    
    
    public LatexFormula addAlignedEquation(String equation, String alignedChar)
    {
        formula.append(equation.replaceAll(new String(alignedChar), "&" + alignedChar + "&"));
        
        return this;
    }
    
    
    public LatexFormula endAlignedEquation()
    {
        formula.append("\\end{eqnarray}");
        
        return this;
    }
    
    
    public LatexFormula jakobiMatrix() {
        formula.append("\\begin{pmatrix}" +
                "\\frac{\\partial{f_1}}{\\partial{x_1}}&\\hdots&\\frac{\\partial{f_1}}{" +
                "\\partial{x_n}}\\\\\\vdots&\\ddots&\\vdots\\\\" +
                "\\frac{\\partial{f_n}}{\\partial{x_1}}&\\hdots&\\frac{\\partial{f_n}}{" +
                "\\partial{x_n}}\\end{pmatrix}").append("\\text{  }\\equiv\\text{Jakobi-Matrix}");
        return this;
    }
    
    
    public boolean isEmpty()
    {
        return formula.length() == 0;
    }
    
    
    public Image toImage()
    {
        return toImage(20, null);
    }
    
    
    public Image toImage(int fontSize)
    {
        return toImage(fontSize, null);
    }
    
    
    public Image toImage(Color foregroundColor)
    {
        return toImage(20, foregroundColor);
    }
    
    
    public Image toImage(int fontSize, Color foregroundColor)
    {
        System.out.println("\\begin{array}{l}" + formula.toString() + "\\end{array}"); // nur zum Debug
        
        TeXFormula texFormula = new TeXFormula("\\begin{array}{l}" + formula.toString() + "\\end{array}");
        TeXIcon icon = texFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize);
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        
        if (foregroundColor == null)
        {
            icon.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
        }
        else
        {
            icon.setForeground(null);
            icon.paintIcon(null, b.getGraphics(), 0, 0);
        }
        
        return b;
    }
    
    
    @Override
    public String toString()
    {
        return formula.toString();
    }
}