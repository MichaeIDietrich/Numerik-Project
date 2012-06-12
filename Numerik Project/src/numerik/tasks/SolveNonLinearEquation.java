
package numerik.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;

import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;


public class SolveNonLinearEquation implements Task
{
    Vector  dqPlus;
    Vector dqMinus;

    TaskPane taskPane;
    LatexFormula     formula = new LatexFormula();
    LatexFormula iterformula = new LatexFormula();
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments( new Argument("Startvektor:", ArgType.VECTOR, 100 ), Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        iterformula.clear();
        
        MathLib.setNorm( MathLib.FROBENIUSEUKLIDNORM );
        MathLib.setRoundingMode( MathLib.EXACT );
        MathLib.setPrecision( 16 ); 
        
        Vector iterx = parameters[0].toVector();
        Vector     x = new Vector( iterx.getLength() );
        Matrix    jm = new Matrix( iterx.getLength(), iterx.getLength() );
        int        i = 0;
                   x = x.setUnitVector(x);
        
        
        // Abbruchbedingung 'obereschranke' bei x.norm() < 2^(-50) < eps
        BigDecimal obereschranke = BigDecimal.ONE.divide(new BigDecimal(2).pow(50), 16, RoundingMode.HALF_UP);

        while (( x.norm()).compareTo( obereschranke )==1) 
        {
            iterformula.addLatexString("x_{"+ i +"} = ").addVector(iterx).addNewLine(1);
            i++;
            
            try
            {
                x = jm.jakobiMatrix( derive(iterx) ).solveX( getFunctionsValue(iterx) );
            } 
            catch(Exception e) 
            {
               showManual(iterx.getLength()); 
               return;
            }
            
            iterx = iterx.add( x );
            if (i==1000) break;
        }
        
        iterformula.addNewLine(2);
        iterformula.addText("Abbruch bei ").addLatexString("\\; x_{"+i+"} = x_{"+(i-1)+"} \\;\\; \\leftrightarrow \\;\\; \\arrowvert{ x_{"+(i)+"}-x_{"+(i-1)+"} }\\arrowvert \\leq eps");
        
        
        // Ausgabe: Latex-Formula-String
        LatexFormula formula = new LatexFormula();
        
        formula.addTextBold("3.E) ").addColorBoxBegin("green").addText("Iteratives Verfahren z. Lösung nichtlinearer Gleichungssysteme").addColorBoxEnd().addNewLine(2);
        formula.addText("Ableitungen der Gleichungen ").addLatexString("f_{i}(x_1,..., x_n)").addText(" in:").addTextBold(" derivations()").addNewLine(1);
        formula.addText("Die Gleichungen selbst stehen in: ").addTextBold(" getFunctionsValue()").addNewLine(1);

        formula.addText("Der Vektor 'Startvektor' ist ").addLatexString("x_0.").addNewLine(3);
        formula.addText("Gegeben ist ein Gleichungssystem ").addLatexString("f_{i}(x_1,..., x_n)").addText(" mit 1...n").addText(" ").addNewLine(1);
        formula.addText("abhängigen Variablen und einem Startvektor der Länge n.").addLatexString("").addText(" ").addNewLine(4);
        formula.addText("Folgende Gleichungen werden benutzt:").addNewLine(1).addLatexString("\\Phi( x^{k} ) \\cdot \\Delta{x^{k+1}} = -f( x^{k} )")
        .addText("   und   ").addLatexString("x^{k+1} = x^{k} + \\Delta{x^{k+1}}").addText("  mit ").addNewLine(3).addLatexString("\\Phi( x ) = ");
        
        formula.jakobiMatrix();
        formula.addNewLine(3).addTextUL("Start\\;der\\;Iteration").addNewLine(1);
        formula.addFormula( iterformula ).addNewLine(2);
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    


    /**
     * Partielles Ableiten aller Funktionen in getFunctionsValue() und speichern
     * aller Werte in einer Matrix, der Jakobimatrix. Der Parameter Vector dient 
     * als Referenz für die Größe der Jakobimatrix und gleichzeitig als Container
     * für die Stelle der Ableitung. 
     * @param vector
     * @return Matrix
     */
    public Matrix derive( Vector vector )
    {
        int  arguments = vector.getLength();
        BigDecimal[] x = new BigDecimal[ arguments ];
        BigDecimal   h = BigDecimal.TEN.pow(10);
        Vector storevalue;
        
        for(int i=0; i < arguments; i++) x[i]=vector.get(i); 

        Matrix dfunction = new Matrix(arguments, arguments);
        
         dqPlus = new Vector(arguments);
        dqMinus = new Vector(arguments);
        
        for(int i=0; i < arguments; i++)
        {
            for(int row=0; row < arguments; row++) 
            {
                if (row==i) 
                {
                     dqPlus.set(i, x[i].add(      BigDecimal.ONE.divide(h) ));
                    dqMinus.set(i, x[i].subtract( BigDecimal.ONE.divide(h) ));
                } else {
                     dqPlus.set(row, new BigDecimal(1.23456789123456789));  // Sollte Zufallszahl sein bzw. eine Zahl die keine
                    dqMinus.set(row, new BigDecimal(1.23456789123456789));  // Asymptote der unten eingegebenen Funktionen ist!
                } 
            }

            // Berechne df(x,y) = ( f(x + 1/h ) - f(x - 1/h) ) * h/2
            storevalue = (getFunctionsValue(dqPlus).sub( getFunctionsValue(dqMinus) ));

            for(int t=0; t < arguments; t++) 
            {
                storevalue.set(t, MathLib.round( storevalue.get(t).multiply( h.divide( new BigDecimal(2) ).negate() )));
                 dfunction.set(t, i, storevalue.get(t));
            }
        }
        return dfunction;
    }
    
    private void showManual(int length)
    {
        formula.clear();
        
        formula.addNewLine(4);
        formula.addTextUL("Mögliche\\;Fehlerursachen").addNewLine(2);
        formula.addText("I.    Länge des Vektors stimmt nicht mit der Anzahl der Gleichungen").addNewLine(1);
        formula.addText("\\;     überein.").addNewLine(1);
        formula.addText("II.  Gleichung enthält Infinity oder NaN.").addNewLine(1);
        formula.addText("III. Die größte Fehlerquelle sitzt vor dem Bildschirm.").addNewLine(1);
        taskPane.setViewPortView(new TaskScrollPane(formula)); 
    }
    
    /**
     * Trage hier alle nicht-linearen Gleichungssysteme ein.
     * @param vector
     * @return Vector
     */
    public Vector getFunctionsValue(Vector vector) 
    {
        
        Vector function = new Vector( vector.getLength() );
        Double[]      x = vector.toDoubleArray();           // x[0] = x ; x[1] = y ; x[2] = z usw.
        
        for(int t=0; t < vector.getLength(); t++) function.set(t, BigDecimal.ZERO);
        
        // Hier die >> Funktionen << eintragen:
        function.set(0, BigDecimal.valueOf(   x[0]*x[0]+x[1]*x[1]+0.6*x[1]-0.16       ).negate());
        function.set(1, BigDecimal.valueOf(   x[0]*x[0]-x[1]*x[1]+x[0]-1.6*x[1]-0.14  ).negate());
        
//        function.set(0, BigDecimal.valueOf(    1-x[1]+Math.sin(x[0])  ).negate());
//        function.set(1, BigDecimal.valueOf( -1.4-x[0]+Math.cos(x[1])  ).negate());
        
//        function.set(0, BigDecimal.valueOf(      x[0]*x[0]*x[0]+10*x[1]-x[0]*x[1]  ).negate());
//        function.set(1, BigDecimal.valueOf( -1.4-x[0]+Math.cos(x[1])               ).negate());
        
        return function;
    }
}
