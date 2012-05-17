package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.MathLib;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;

public class dezToBin implements Task
{
    TaskPane taskPane;
    LatexFormula formula = new LatexFormula();

    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Dezimalzahl:", ArgType.DECIMAL, "1"), 
                                           new Argument("Genauigkeit:", ArgType.DOUBLEPRECISION, "5"), 
                                               Argument.RUN_BUTTON);
    }

    
    @Override
    public void run(Value... values)
    {
        MathLib.setPrecision( values[1].toDecimal().intValue() );
        MathLib.setRoundingMode( MathLib.EXACT );
        
        formula.clear();
        
        Double  value = MathLib.round( values[0].toDecimal()).doubleValue();
        Double tmpval = value;
        Double recalc = 0d;
        String binary = "";
        int  exponent = 0;
        boolean point = false;
        
        formula.addTextUL("").addNewLine(2);
        
        if (value>=1) {
            exponent = (int)Math.floor( Math.log(value)/Math.log(2) ); 
        } 
          else 
        {
            exponent = 0;
            point    = true;
            binary   = "0.";
        }
        
        for(int i=0; i<=exponent+16; i++ )
        {
            if ((exponent-i)==-1 && !point) 
            {
                binary = binary + ".";
                point  = true;
            }
            
            if ((value-Math.pow(2, exponent-i))>=0) 
            {
                binary = binary + "1";
                value  = value  - Math.pow(2, exponent-i);
                recalc = recalc + Math.pow(2, exponent-i);
            } 
              else 
            {
                binary = binary + "0";
            }   
        }
        
        
        formula.addLatexString(tmpval+"_{10} \\cong "+ binary+"_{b} = "+recalc+"_{10}").addNewLine(4);
        formula.addText("eingegebener Wert: " +tmpval).addNewLine(1);
        formula.addText("konvertierter Wert: "+recalc).addNewLine(4);
        formula.addLatexString("abs.\\;Fehler\\;=\\;|\\;"+tmpval+"-"+recalc+"\\;|\\;=\\;"+(tmpval-recalc)).addNewLine(1);
        formula.addLatexString("rel.\\;Fehler\\;=\\;\\frac{"+(Math.abs(tmpval-recalc))+"}{"+recalc+"}\\;=\\;"
                               +MathLib.round( new BigDecimal(Math.abs(tmpval-recalc)/recalc))).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
}

