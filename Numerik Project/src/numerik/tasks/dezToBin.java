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
    Double recalc = 0d;
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Dezimalzahl:", ArgType.DECIMAL, "130.05"), 
                                           new Argument("runde Dezimal:", ArgType.BOOLEAN),
                                           new Argument("runde Binär:", ArgType.BOOLEAN),
                                           new Argument("Mantissenlänge:", ArgType.DOUBLEPRECISION, "16"), 
                                               Argument.RUN_BUTTON);
    }

    @Override
    public void run(Value... values)
    {
        MathLib.setPrecision( values[3].toDecimal().intValue() );
        MathLib.setRoundingMode( MathLib.EXACT );
        
        formula.clear();
        recalc = 0d;
        
        Double  value = 0d;
        if (values[1].toBoolean()) value = MathLib.round( values[0].toDecimal()).doubleValue(); 
                              else value = values[0].toDecimal().doubleValue(); 
        Double tmpval = value;
        
        String binary = "";
        
        int mantiscount = 0;
        int    exponent = 0;
        
        boolean begincount = false;
        boolean      point = false;
        
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
            
            if (mantiscount==values[3].toDecimal().intValue() && values[2].toBoolean())
            {
                boolean check = (value-Math.pow(2, exponent-i)) >= 0;
                int  position = exponent-i;
                
                binary = roundBinary( binary, check, position );
                break;
            }
            
            if ((value-Math.pow(2, exponent-i))>=0) 
            {
                begincount = true;
                mantiscount++;
                    
                if (mantiscount<values[3].toDecimal().intValue()+1)
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
              else 
            {
                if (begincount) mantiscount++;  
                binary = binary + "0";
            }   
        }
        
        
        formula.addLatexString(tmpval+"_{10} \\cong "+ binary+"_{b} = "+recalc+"_{10}").addNewLine(4);
        formula.addText("eingegebener Wert: " +tmpval).addNewLine(1);
        formula.addText("konvertierter Wert: "+recalc).addNewLine(4);
        formula.addLatexString("abs.\\;Fehler\\;=\\;|\\;"+tmpval+"-"+recalc+"\\;|\\;=\\;"+Math.abs(tmpval-recalc)).addNewLine(1);
        MathLib.setPrecision( 5 );
        if (Math.abs(tmpval-recalc)/recalc>0) MathLib.setRoundingMode( MathLib.NORMAL );
        formula.addLatexString("rel.\\;Fehler\\;=\\;\\frac{"+(Math.abs(tmpval-recalc))+"}{"+tmpval+"}\\;=\\;"
                               +MathLib.round( new BigDecimal(Math.abs(tmpval-recalc)/recalc))).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    
    public String roundBinary(String binary, boolean check, int position) {
        
        System.out.println("## "+binary);
        
        int      length = binary.length()-1;
        String[] binarr = binary.split("\\.");
        int   firstmant = binarr[0].length();
        int  secondmant = binarr[1].length();
        int       dotat = binarr[0].length();
        binary = binarr[0] + binarr[1];
        
        System.out.println("length: "+length+" ## dotat: "+dotat+" ## "+binary+" ## 1 an Position"+position);
        
        if (check) formula.addText("1 an Position "+position).addNewLine(1);
        
        for(int i=position; i>=0; i--) 
        {
            binary = binary + "0";
        }

        String[] code = new String[length];
        
        for(int i=length; i>=0; i-- )
        {
           if (binary.codePointAt(i)=='0') code[i]="0";
           if (binary.codePointAt(i)=='1') code[i]="1"; 
           if (binary.codePointAt(i)=='.') code[i]=".";
        }
        
        out(code);
        System.out.print(" ;; "+position+ "\n");
        
        for(int i=position+1; i<=length; i++ )
        {
           if (code[length-i]=="0" && check)
           {
               check = false;
               code[length-i]="1";
           }
           
           if (code[length-i]=="1" && check)
           {
               code[length-i]="0";
           }
           out(code);
           System.out.print("  ::  "+(length-i)+"\n");
        }

        
        if (check) { 
            binary = "1"; 
            recalc = Math.pow(2, length+1);
        }
          else
        {
            binary = "";
            recalc = 0d;
        }
        
        for(int i=0; i<=length; i++ ) {
            
            binary = binary + code[i];
            if (code[i]=="1") recalc = recalc + Math.pow(2, length-i);
        }
        
        return binary;
    }
    
    public void out(String[] str) {
        for(int i=0; i<str.length; i++ ) System.out.print(str[i]);
    }
}

