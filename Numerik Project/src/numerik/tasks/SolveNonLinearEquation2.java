
package numerik.tasks;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import numerik.calc.FunctionsDiscussion;
import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;


public class SolveNonLinearEquation2 implements Task
{
    
    Vector  dqPlus;
    Vector dqMinus;
    Vector choosenvector;
    Vector z;
    
    TaskPane taskPane;
    LatexFormula         formula = new LatexFormula();
    LatexFormula     iterformula = new LatexFormula();
    FunctionsDiscussion function = new FunctionsDiscussion();
    Recorder recorder;
    
    
    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments( new Argument("Startvektor:", ArgType.VECTOR, 100 ), 
                                            new Argument("Formeltabelle (Zeile): ", ArgType.PRECISION, "1" ),
                                            Argument.RUN_BUTTON);
    }
    
    
    
    
    @Override
    public void run(Value... parameters)
    {
        iterformula.clear();
        
        MathLib.setNorm( MathLib.FROBENIUSEUKLIDNORM );
        MathLib.setRoundingMode( MathLib.EXACT );
        MathLib.setPrecision( 16 ); 
        
        Vector   iterx = parameters[0].toVector();
        Vector       x = new Vector( iterx.getLength() );
        Matrix      jm = new Matrix( iterx.getLength(), iterx.getLength() );
        int funcchoose =  parameters[1].toDecimal().intValue();
        int          i = 0;
                     x = x.setUnitVector(x);
                          
        choosenvector = iterx.clone(); 
        
        // Setze Abbruchbedingung 'obereschranke' bei x.norm() > 2^(-50) > eps
        BigDecimal obereschranke = BigDecimal.ONE.divide(new BigDecimal(2).pow(50), 16, RoundingMode.HALF_UP);

        while (( x.norm()).compareTo( obereschranke )==1) 
        {
            iterformula.addLatexString("x_{"+ i +"} = ").addVector(iterx).addNewLine(1);
            i++;
            
            try
            {
                x = jm.jakobiMatrix( function.derive( iterx, funcchoose )).solveX( function.systemOfFunctions( iterx, funcchoose ));
            } 
            catch(ArithmeticException e) 
            {
               if (e.getLocalizedMessage().equals("/ by zero") || e.getLocalizedMessage().equals("BigInteger divide by zero"))
               {
                   showManual( "Division durch Null.", funcchoose );
               }
               else
               {
                   formula.clear();
                   recorder.clear();
                   showManual( "", funcchoose );
               }
               return;
            }
            
            iterx = iterx.add( x );
            if (i==1000) break;
        }
        
        iterformula.addNewLine(2);
        iterformula.addText("Abbruch bei ");
        iterformula.addLatexString("\\; x_{"+i+"} = x_{"+(i-1)+"} \\;\\; \\leftrightarrow \\;\\; " +
        		                   "\\arrowvert{ x_{"+(i)+"}-x_{"+(i-1)+"} }\\arrowvert \\leq eps");
        
        // Ausgabe: Latex-Formula-String
        LatexFormula formula = new LatexFormula();
        
        formula.addTextBold("3.E) ");
        formula.addColorBoxBegin("green");
        formula.addText("Iter. Verfahren zur Lösung nichtlinearer Gleichungssysteme");
        formula.addColorBoxEnd().addNewLine(2);
        formula.addText("Gleichungen zum Eintragen in die Tabelle: ... ").addNewLine(1);
        formula.addTextBold(" numeric.calc/ FunctionsDiscussion/ systemOfFunctions()").addNewLine(3);
        formula.addText("Gegeben ist ein Gleichungssystem ").addLatexString("f_{i}(x_1,..., x_n)");
        formula.addText(" mit 1...n").addText(" ").addNewLine(1);
        formula.addText("abhängigen Variablen und einem Startvektor der Länge n.").addLatexString("").addText(" ").addNewLine(4);
        formula.addText("Folgende Gleichungen werden benutzt:").addNewLine(1);
        formula.addLatexString("\\Phi( x^{k} ) \\cdot \\Delta{x^{k+1}} = -f( x^{k} )");
        formula.addText("   und   ").addLatexString("x^{k+1} = x^{k} + \\Delta{x^{k+1}}").addText("  mit ").addNewLine(3);
        formula.addLatexString("\\Phi( x ) = ");
        
        formula.jakobiMatrix().addNewLine(3);
        if (choosenvector.getLength()<2) 
        {
            formula.addTextUL("Kontraktionsintervall").addNewLine(1);
            formula.addLatexString("|\\Phi(\\vec{x_{0}})| = ").addVector( function.getKontractionIntervall( choosenvector, funcchoose ));
            formula.addLatexString(" < 1").addNewLine(3);
        }
        formula.addTextUL("Start\\;der\\;Iteration").addNewLine(1);
        formula.addFormula( iterformula ).addNewLine(2);       
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    

    
    private void showManual(String error, int funcchoose )
    {
        formula.clear();
        
        formula.addNewLine(4);
        formula.addTextUL("Mögliche\\;Fehlerursachen").addNewLine(2);
        
        if (error.isEmpty()) 
        {
            formula.addText("I.    Länge des Vektors stimmt nicht mit der Anzahl der Gleichungen").addNewLine(1);
            formula.addText("\\;     überein.").addNewLine(1);
            formula.addText("II.  Gleichung enthält Infinity oder NaN.").addNewLine(1);
            formula.addText("III. Die größte Fehlerquelle sitzt vor dem Bildschirm.").addNewLine(3);
        }
        else 
        {
            formula.addText("Grund für Abbruch: "+error).addNewLine(3);
            formula.addTextUL("Kontraktionsintervall").addNewLine(1);
            formula.addLatexString("|\\Phi(\\vec{x_{0}})| = ").addVector( function.getKontractionIntervall( choosenvector, funcchoose ) ).addLatexString(" \\nless 1").addNewLine(3);
        }
        
        taskPane.setViewPortView(new TaskScrollPane(formula)); 
        return;
    }
}