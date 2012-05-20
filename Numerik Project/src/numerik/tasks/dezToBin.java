package numerik.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        taskPane.createJToolBarByArguments(new Argument("Dezimalzahl:", ArgType.DECIMAL, "130.05", 100), 
                                           new Argument("runde Dezimal", ArgType.BOOLEAN),
                                           new Argument("runde Binär", ArgType.BOOLEAN),
                                           new Argument("Mantissenlänge:", ArgType.DOUBLEPRECISION, "16"), 
                                               Argument.RUN_BUTTON);
    }

    @Override
    public void run(Value... values)
    {
        MathLib.setPrecision( values[3].toDecimal().intValue() );
        MathLib.setRoundingMode( MathLib.EXACT );
        
        formula.clear();
        
        Double value = 0d;
        if (values[1].toBoolean()) value = MathLib.round( values[0].toDecimal()).doubleValue(); 
                              else value = values[0].toDecimal().doubleValue(); 
        Double tmpval = value;

        String[] temp = value.toString().split("\\.");
        
        BigDecimal     zero = BigDecimal.ZERO;
        BigDecimal firstdec = new BigDecimal(temp[0]);
        BigDecimal secondec = new BigDecimal("0."+temp[1]);
        BigDecimal  tempdec = BigDecimal.ZERO;
        String     firstbin = "";
        String     seconbin = "";
        String       binary = "";
        int      highestbit = (int) Math.floor( Math.log(firstdec.doubleValue())/Math.log(2) );
        int       lowestbit = 16;
        BigDecimal two_to_power = BigDecimal.ZERO;

        
        // Konvertiere Vorkomma-Dezimalzahl nach Binär
        tempdec = firstdec;
        
        for (int i=highestbit; i>=0; i--) 
        {
            two_to_power = new BigDecimal( Math.pow( 2, i));
            
            if ((tempdec.subtract( two_to_power ).compareTo( zero )==0) || (tempdec.subtract( two_to_power ).compareTo( zero )==1))
            {
                firstbin = firstbin + "1";
                tempdec = tempdec.subtract( two_to_power );
            }
              else
            {
                firstbin = firstbin + "0";
            }
        }
        
        if (firstdec.compareTo(zero)==0) { firstbin = "0"; }
        
        
        // Konvertiere Nachkomma-Dezimalzahl nach Binär
        tempdec = secondec;
        
        for (int i=1; i<=lowestbit; i++) 
        {
            two_to_power = BigDecimal.ONE.divide( new BigDecimal(Math.pow( 2, i)), 32, RoundingMode.HALF_UP);
            
            if ((tempdec.subtract( two_to_power ).compareTo( zero )==0) || (tempdec.subtract( two_to_power ).compareTo( zero )==1))
            {
                seconbin = seconbin + "1"; 
                tempdec = tempdec.subtract( two_to_power );
            }
              else
            {
                seconbin = seconbin + "0";
            }
        }
        
        // gerundet oder ungerundet?
        if (values[2].toBoolean()) 
        {
            binary = roundBinary(firstbin+seconbin, values[3].toDecimal().intValue(), firstbin.length());
        }
          else
        {
            binary = firstbin+"."+seconbin;
        }
        
        Double recalc = getDecimal(binary).doubleValue();

        formula.addLatexString(tmpval+"_{10} \\cong "+ binary+"_{b} = "+recalc+"_{10}").addNewLine(4);
        formula.addText("eingegebener Wert: " +tmpval).addNewLine(1);
        formula.addText("konvertierter Wert: "+recalc).addNewLine(4);
        formula.addLatexString("abs.\\;Fehler\\;=\\;|\\;"+tmpval+"-"+recalc+"\\;|\\;=\\;"+Math.abs(tmpval-recalc)).addNewLine(1);
        MathLib.setPrecision( 5 );

        formula.addLatexString("rel.\\;Fehler\\;=\\;\\frac{"+(Math.abs(tmpval-recalc))+"}{"+tmpval+"}\\;=\\;"
                               +MathLib.round( new BigDecimal(Math.abs(tmpval-recalc)/tmpval))).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    
    
    
    
    public String roundBinary(String binary, int mantisse, int dotpos) {

        System.out.println(mantisse);
        
        char[]  binchar = binary.toCharArray();
        int      length = binary.length()-1;
        int    position = 0;
        boolean gocount = false;
        binary = "";
        
        // Zähle Mantisse ab erster gefundener Eins
        for (int i=0; i<=length; i++) 
        {
            if (binchar[i]=='1') gocount = true;
           
            System.out.println(binchar[i]);
            
            if (gocount) position++;
            
            if (position==mantisse)
            {
                position = i;
                break;
            }
        }

        // Erstelle String auf Mantissengenauigkeit (noch nicht gerundet)
        for(int i=0; i<=position; i++)
        {
            binary = binary + binchar[i];
        }
        
        // lösche alle Folgestellen > position
        for(int i=position+1; i<=length+1; i++)
        {
            binary = binary + "0";
        }
        
        // Wenn Folgestelle nach Mantissenlänge gleich 0 ist, dann runde nicht und gib String zurück
        if (binchar[position+1]=='0')
        {
            binary = binary.substring(0, dotpos) +"."+ binary.substring(dotpos, length+1);
            return binary;
        }
        
        boolean oflag = true;
        
        // Runde binary
        for(int i=position; i>=0; i--)
        {
            if (binchar[i]=='0') { binchar[i]='1'; oflag = false; break; }
            if (binchar[i]=='1')   binchar[i]='0'; 
        }
        
        // Reorganisiere String "binary"
        binary = "";
        for(int i=0; i<=length; i++)
        {
            if (i>position) binchar[i]='0';
            binary = binary + binchar[i];
        }
                
        // War ein Überlauf? Erstelle neues MSB und setzte es auf 1.
        if (oflag==true) { binary = "1"+binary; dotpos++; }
        
        // Füge Trennzeichen ein
        binary = binary.substring(0, dotpos) +"."+ binary.substring(dotpos, length+1);
        
        return binary;
    }
    
    
    public BigDecimal getDecimal(String binary)
    {
        String[] tempbin = binary.split("\\.");
        char[]  bincharL = tempbin[0].toCharArray();
        char[]  bincharR = tempbin[1].toCharArray();
        int       length = 16;
        int      lengthL = tempbin[0].length();
        int      lengthR = tempbin[1].length();
        BigDecimal   sum = BigDecimal.ZERO;

        System.out.println(lengthL+" | "+lengthR);
        
        if (length < tempbin[0].length()-1) length = tempbin[0].length()-1; length = 16; 
        
        for(int i=1; i<=length; i++)
        {
            if (i<lengthL+1) 
            {
                System.out.println(bincharL[lengthL-i]);
                if (bincharL[lengthL-i]=='1') 
                {
                    sum = sum.add( new BigDecimal( Math.pow(2, i-1) ));
                }
            }
            
            if (i<=lengthR) 
            {
                
                if (bincharR[i-1]=='1')
                {
                    sum = sum.add( BigDecimal.ONE.divide( new BigDecimal( Math.pow(2, i) )));
                }
            }
        }
        
        return sum;
    }
}
