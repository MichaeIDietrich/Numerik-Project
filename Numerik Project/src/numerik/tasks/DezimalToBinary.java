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

public class DezimalToBinary implements Task
{
    
    TaskPane taskPane;
    LatexFormula formula = new LatexFormula();
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments(new Argument("Dezimalzahl:", ArgType.DECIMAL, "0.00567", 100), 
                                           new Argument("Runde:", "Binär", "Dezimal"),
                                           new Argument("Mantissenlänge:", ArgType.DOUBLEPRECISION, "16"), 
                                               Argument.RUN_BUTTON);
    }
    

    @Override
    public void run(Value... values)
    {
        
        Boolean round = values[1].toText().equals("Binär") ? true : false;
        
        MathLib.setPrecision( values[2].toDecimal().intValue() );
        MathLib.setRoundingMode( MathLib.EXACT );
        
        formula.clear();
        
        Double value = 0d;
        if (!round) value = MathLib.round( values[0].toDecimal()).doubleValue(); 
                             else value = values[0].toDecimal().doubleValue(); 
        Double tmpval = value;
        
        
        String[] temp = new BigDecimal(value.toString()).toPlainString().split("\\.");
        
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
        
        for (int i = highestbit; i >= 0; i--) 
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
        
        
        if (firstdec.compareTo(zero) == 0) { firstbin = "0"; }
        
        
        // Konvertiere Nachkomma-Dezimalzahl nach Binär
        tempdec = secondec;
        
        for (int i = 1; i <= lowestbit; i++) 
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
        if (round) 
        {
            binary = roundBinary(firstbin+seconbin, values[2].toDecimal().intValue(), firstbin.length());
        }
          else
        {
            binary = firstbin+"."+seconbin;
        }
        
        Double recalc = getDecimal(binary).doubleValue();
        
        MathLib.setPrecision( 8 );
           
        String showtmpval = toPlain( tmpval.toString() );
        String showrecalc = toPlain( recalc.toString() );
        
        formula.setColorBoxBegin("cyan").addLatexString(showtmpval+"_{10} \\cong "+binary+"_{b} = "+showrecalc+"_{10}").setColorBoxEnd().addNewLine(4);
        formula.addText("Eingabe: "+showtmpval).addNewLine(1);
        formula.addText("Ausgabe: "+showrecalc).addNewLine(4);
        formula.addLatexString("abs.\\;Fehler\\;=\\;|\\;"+showtmpval+"-"+showrecalc+"\\;|\\;=\\;"
                               +MathLib.round( new BigDecimal( Math.abs(tmpval-recalc)) )).addNewLine(1);
        formula.addLatexString("rel.\\;Fehler\\;=\\;\\frac{"
                               +MathLib.round( new BigDecimal(Math.abs(tmpval-recalc)))+"}{"+showtmpval+"}\\;=\\;"); 
        if (tmpval!=0)
        {
            formula.addLatexString(""+MathLib.round( new BigDecimal(Math.abs(tmpval-recalc)/tmpval))).addNewLine(4);
        }
          else
        {
            formula.addLatexString("0").addNewLine(4);  
        }

        formula.setColorBoxBegin("red");
        formula.addText("Problem: wenn bspw. bei der Zahl 0.00567 die Mantissenlänge 9 ").setColorBoxEnd().addNewLine(1);
        formula.setColorBoxBegin("red");
        formula.addText("               überschreitet, wird falsch gerundet.");
        formula.setColorBoxEnd().addNewLine(2);
        formula.setColorBoxBegin("green");
        formula.addText("Fehler behoben. Bitte noch testen (also ein bisschen herumspielen).");
        formula.setColorBoxEnd();
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }   
    
    
    
    public String roundBinary(String binary, int mantisse, int dotpos) {

        char[]  binchar = binary.toCharArray();
        int      length = binary.length()-1;
        int relposition = 0;
        int absposition = 0;
        boolean gocount = false;
        
        binary = "";
        
        // Zähle Mantisse ab erster gefundener Eins
        for (int i=0; i<=length; i++) 
        {
            if (binchar[i] == '1' && !gocount ) { gocount = true; absposition=i; }
            
            if (gocount) relposition++;
            
            if (relposition+absposition > 16 || relposition == mantisse)
            {
                relposition = i;
                break;
            }
        }
        
        // Erstelle String nach Mantissengenauigkeit (ungerundet)
        for(int i = 0; i <= relposition; i++)
        {
            binary = binary + binchar[i];
        }
        
        // lösche alle Folgestellen > position
        for(int i = relposition+1; i <= length+1; i++)
        {
            binary = binary + "0";
        }
        
        // Wenn Folgestelle nach Mantissenlänge gleich 0 ist, dann runde nicht und gib String zurück
        if (relposition+1 > 16 || binchar[relposition+1] == '0')
        {
            binary = binary.substring(0, dotpos) +"."+ binary.substring(dotpos, length+1);
            return binary;
        }
        
        boolean oflag = true;
        
        System.out.println("OK."+relposition);
        
        // Runde binary
        for(int i = relposition; i >= 0; i--)
        {
            if (binchar[i]=='0') { binchar[i]='1';   oflag = false;  break; }
            if (binchar[i]=='1')   binchar[i]='0';
        }
        
        // Reorganisiere String "binary"
        binary = "";
        for(int i=0; i<=length; i++)
        {
            if (i>relposition) binchar[i]='0';
            binary = binary + binchar[i];
        }
                
        // Überlauf? Addiere zu neuem MSB=1 "binary".
        if (oflag == true) { binary = "1"+binary; dotpos++; }
        
        // Füge das Trennzeichen ein
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

        if (length < lengthL) length = lengthL; else length = lengthR; 
        
        for(int i = 1; i <= length; i++)
        {
            if (i<lengthL+1) 
            {
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
    
    public String toPlain(String string)
    {
        if (string.contains("E"))
        {
            String[] temp1    = string.split("E");
            String[] vunkomma = temp1[0].split("\\.");
            
            if (vunkomma[1].equals("0")) vunkomma[1]="";
            
            if (temp1[1].contains("-"))
            {
                int nullen = (int) Math.abs( (double) Integer.parseInt(temp1[1]) );
                string = "0.";
                
                for(int i=1; i<nullen; i++) string = string + "0";
                string = string + vunkomma[0] + vunkomma[1];
            }
        }
        return string;
    }
}
