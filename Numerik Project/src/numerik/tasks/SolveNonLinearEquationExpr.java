
package numerik.tasks;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import numerik.calc.FunctionsDiscussion;
import numerik.calc.MathLib;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.*;
import numerik.tasks.Argument.ArgType;
import numerik.ui.controls.TaskPane;
import numerik.ui.controls.TaskScrollPane;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;


public final class SolveNonLinearEquationExpr implements Task
{
    private static final String[] VAR_NAMES = { "x", "y", "z" };
    
    private Vector  dqPlus;
    private Vector dqMinus;
    private Vector chosenvector;
    private Vector z;
    
    private TaskPane taskPane;
    private LatexFormula     formula = new LatexFormula();
    private LatexFormula iterformula = new LatexFormula();
    private Recorder recorder;
    
    private ArrayList<String> functions;
    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
        taskPane.createJToolBarByArguments( 
                new Argument("Funktion 1:", ArgType.EXPRESSION, "x⋅x+y⋅y+0.6⋅y-0.16", 450),
                new Argument("Funktion 2:", ArgType.EXPRESSION, "x⋅x-y⋅y+x-1.6⋅y-0.14", 450),
                new Argument("Funktion 3:", ArgType.EXPRESSION, 450),
                new Argument("max. Iterationen:", ArgType.PRECISION, "100"),
                new Argument("Startvektor:", ArgType.VECTOR, "d", 100 ), 
                new Argument("Mantissenlänge:", ArgType.PRECISION, "16"), 
                Argument.RUN_BUTTON);
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        recorder = Recorder.getInstance();
        iterformula.clear();
        
        MathLib.setNorm( MathLib.FROBENIUSEUKLIDNORM );
        MathLib.setRoundingMode( MathLib.EXACT );
        // wenn Precision zu gering -> Fehler
        MathLib.setPrecision( parameters[5].toDecimal().intValue() ); 
        
        Vector iterx = parameters[4].toVector();
        int maxiters = parameters[3].toDecimal().intValue();
        System.out.println(maxiters);
        // setze Vector mit Funktionen
        functions = new ArrayList<>();
        
        
        Vector     x = new Vector( iterx.getLength() );
        Matrix    jm = new Matrix( iterx.getLength(), iterx.getLength() );
        int        i = 0;
                   x = x.setUnitVector(x);
               
        chosenvector = iterx.clone();
        

        for (int t = 0; t < 3; t++)
        {
            if (parameters[t].toText().isEmpty()) break;
            
            functions.add(parameters[t].toText());
        }
        
        
        if (iterx.getLength() != functions.size())
        {
            showManual("Anzahl der Funktionen ungleich der Länge des Vektors!");
            return;
        }

        
        // Abbruchbedingung 'obereschranke' bei x.norm() > 2^(-50) > eps
        BigDecimal obereschranke = BigDecimal.ONE.divide(new BigDecimal(2).pow(50), 16, RoundingMode.HALF_UP);

        
        while (( x.norm()).compareTo( obereschranke )==1) 
        {
            iterformula.addLatexString("x_{"+ i +"} = ").addVector(iterx).addNewLine(1);
            i++;
            
            try
            {
                x = jm.jakobiMatrix( derive(iterx) ).solveX( getFunctionsValue(iterx) );
            } 
            catch(ArithmeticException e) 
            {
               if (e.getLocalizedMessage().equals("/ by zero") || e.getLocalizedMessage().equals("BigInteger divide by zero"))
               {
                   showManual( "Division durch Null." );
               }
               else
               {
                   formula.clear();
                   recorder.clear();
                   showManual( "" );
                   
                   throw e;
               }
               return;
            }
            
            iterx = iterx.add( x );
            if (i==maxiters) break;
        }
      
        iterformula.addNewLine(2);
        iterformula.addText("Abbruch bei ").addLatexString("\\; x_{"+i+"} = x_{"+(i-1)+"} \\;\\; \\leftrightarrow \\;\\; \\arrowvert{ x_{"+(i)+"}-x_{"+(i-1)+"} }\\arrowvert \\leq eps");
        
        
        // Ausgabe: Latex-Formula-String
        LatexFormula formula = new LatexFormula();
        
        
        formula.addTextBold("3.E) ").addColorBoxBegin("green").addText("Iter. Verfahren zur Lösung nichtlinearer Gleichungssysteme");
        formula.addColorBoxEnd().addNewLine(2);
        formula.addText("Gegeben ist ein Gleichungssystem ").addLatexString("f_{i}(x_1,..., x_n)").addText(" mit 1...n").addText(" ").addNewLine(1);
        formula.addText("abhängigen Variablen und einem Startvektor der Länge n.").addLatexString("").addText(" ").addNewLine(4);
        formula.addText("Folgende Gleichungen werden benutzt:").addNewLine(1).addLatexString("\\Phi( x^{k} ) \\cdot \\Delta{x^{k+1}} = -f( x^{k} )");
        formula.addText("   und   ").addLatexString("x^{k+1} = x^{k} + \\Delta{x^{k+1}}").addText("  mit ").addNewLine(3).addLatexString("\\Phi( x ) = ");
        
        formula.jakobiMatrix().addNewLine(3);
        
        if (chosenvector.getLength()<2) 
        {
            formula.addTextUL("Kontraktionsintervall").addNewLine(1);
            formula.addLatexString("|\\Phi(\\vec{x_{0}})| = ").addVector( getKontractionIntervall( chosenvector ));
            formula.addLatexString(" < 1").addNewLine(3);
        }
        formula.addNewLine(3).addTextUL("Start\\;der\\;Iteration").addNewLine(1);
        formula.addFormula( iterformula ).addNewLine(2);       
               
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    

    private void showManual(String error)
    {
        formula.clear();
        
        formula.addNewLine(4);
        formula.addTextUL("Fehlerursache").addNewLine(2);
        
        if (error.isEmpty()) 
        {
            formula.addText("I.    Länge des Vektors stimmt nicht mit der Anzahl der Gleichungen").addNewLine(1);
            formula.addText("\\;     überein.").addNewLine(1);
            formula.addText("II.  Gleichung enthält Infinity oder NaN.").addNewLine(1);
        }
        else 
        {
            formula.addText(""+error).addNewLine(6);
            
            if (chosenvector.getLength()<2) 
            {
                BigDecimal value = chosenvector.get(0);
                
                formula.addTextUL("Kontraktionsintervall").addNewLine(1);
                formula.addLatexString("|\\Phi(\\vec{x_{0}})| = "+value);
                
                if (value.compareTo(BigDecimal.ONE) == -1 ) 
                {
                    formula.addLatexString("\\leq 1").addNewLine(2); 
                }
                else  
                {
                    formula.addLatexString("\\nleq 1").addNewLine(2);
                    formula.addColorBoxBegin("red").addText("Achtung! Kontraktionsintervall \\nleq 1 \\rightarrow keine Aussage")
                           .addColorBoxEnd();
                    recorder.clear();
                    recorder.add( new LatexFormula().addText("Keine Lösung! Der Lösungsvektor divergiert.") );
                }
            }
        }
        
        taskPane.setViewPortView(new TaskScrollPane(formula)); 
        return;
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
            storevalue = ( getFunctionsValue(dqPlus).sub( getFunctionsValue(dqMinus) ));

            for(int t=0; t < arguments; t++) 
            {
                storevalue.set(t, MathLib.round( storevalue.get(t).multiply( h.divide( new BigDecimal(2) ).negate() )));
                 dfunction.set(t, i, storevalue.get(t));
            }
        }
        return dfunction;
    }
    
    
    
    public Vector getKontractionIntervall( Vector testvector )
    {
        Vector   func = getFunctionsValue( testvector );
        Matrix  dfunc = derive(  testvector );
        Matrix ddfunc = derive2( testvector );
        
        recorder.clear();
        recorder.add( 
                formula.addLatexString("\\frac{").addVector(func).addText(" \\cdot ").addMatrix(ddfunc).addLatexString("}" +
                        "{").addMatrix(dfunc).addText(" \\cdot ").addMatrix(dfunc).addLatexString("}").addNewLine(1)
        );
        
        int    length = func.getLength();
        Vector kointv = new Vector( length );
        
        BigDecimal   f = BigDecimal.ZERO;
        BigDecimal  df = BigDecimal.ZERO;
        BigDecimal ddf = BigDecimal.ZERO;
        
        for(int i=0; i < length; i++)
        {
            f = func.get(i);
            
            for(int col=0; col < length; col++)
            {
                 df =  df.add(  dfunc.get(i, col));
                ddf = ddf.add( ddfunc.get(i, col));
            }
            
            kointv.set(i, (f.multiply(ddf)).abs().divide( df.multiply(df), MathLib.getPrecision(), RoundingMode.HALF_DOWN ));
        }
        
        return kointv;
    }
    

    public Matrix derive2( Vector vector )
    {
        int  arguments = vector.getLength();
        BigDecimal[] x = new BigDecimal[ arguments ];
        BigDecimal   h = BigDecimal.TEN.pow(5).multiply( new BigDecimal(0.78), new MathContext(16, RoundingMode.HALF_UP) );
        Vector storevalue;
        
        for(int i=0; i < arguments; i++) x[i]=vector.get(i); 

        Matrix dfunction = new Matrix(arguments, arguments);
        
         dqPlus = new Vector(arguments);
        dqMinus = new Vector(arguments);
              z = new Vector(arguments);
         
        for(int i=0; i < arguments; i++)
        {
            for(int row=0; row < arguments; row++) 
            {
                if (row==i) 
                {
                     dqPlus.set(i, x[i].add(      BigDecimal.ONE.divide( h, MathLib.getPrecision(), RoundingMode.HALF_DOWN )));
                    dqMinus.set(i, x[i].subtract( BigDecimal.ONE.divide( h, MathLib.getPrecision(), RoundingMode.HALF_DOWN )));
                          z.set(i, x[i]);
                } else {
                     dqPlus.set(row, new BigDecimal(1.23456789123456789));  // Sollte Zufallszahl sein bzw. eine Zahl die keine
                    dqMinus.set(row, new BigDecimal(1.23456789123456789));  // Asymptote der unten eingegebenen Funktionen ist!
                          z.set(row, x[i]);
                } 
            }

            // Berechne df(x,y) = ( f(x + 1/h ) - 2*f(x) + f(x - 1/h ) ) * h²/2
            storevalue = ( getFunctionsValue(dqPlus).sub( getFunctionsValue(z).add(getFunctionsValue(z)) ).add( getFunctionsValue(dqMinus) ));

            for(int t=0; t < arguments; t++) 
            {
                storevalue.set(t, MathLib.round( storevalue.get(t).multiply( h.multiply(h) ).negate() ));
                 dfunction.set(t, i, storevalue.get(t));
            }
        }
        return dfunction;
    }
    
    
    public Vector getFunctionsValue(Vector vector) 
    {
        
        Vector function = new Vector( vector.getLength() );
        
        ExpressionEngine engine = new ExpressionEngine();
        
        // setze x, y, z ...
        for (int i = 0; i < function.getLength(); i++)
        {
            engine.getVariableTable().set(VAR_NAMES[i], new Value(vector.get(i)));
        }
        
        // löse die Gleichungen
        for (int i = 0; i < functions.size(); i++)
        {
            try
            {
                function.set(i, engine.solve(functions.get(i)).toDecimal().negate());
            }
            catch (InvalidExpressionException ex)
            {
                // sollte wenn möglich nicht passieren, da sonst eine Endlosschleife resultieren könnte :/
                ex.printStackTrace();
            }
        }
        
        return function;
    }
}
