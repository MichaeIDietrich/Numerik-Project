package numerik.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;

public class FunctionsDiscussion
{
    Vector  dqPlus;
    Vector dqMinus;
    Vector choosenvector;
    Vector z;
    
    LatexFormula formula = new LatexFormula();
    Recorder recorder;
    
    /**
     * Zweite Partielle Ableitung der mit choose gewähltenFunktion in systemOfFunction(). 
     * Der Parameter Vector dient als Referenz für die Größe der Jakobimatrix und 
     * gleichzeitig als Container der Werte für die Stelle der Ableitung. 
     * 
     * @param vector
     * @return Matrix (Jakobimatrix)
     */
    public Matrix derive( Vector vector, int choose )
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
                    dqMinus.set(row, new BigDecimal(1.23456789123456789));  // Asymptote der abgeleiteten Funktionen ist!
                } 
            }

            // Berechne df(x,y) = ( f(x + 1/h ) - f(x - 1/h) ) * h/2
            storevalue = (systemOfFunctions(dqPlus, choose).sub( systemOfFunctions(dqMinus, choose) ));

            for(int t=0; t < arguments; t++) 
            {
                storevalue.set(t, MathLib.round( storevalue.get(t).multiply( h.divide( new BigDecimal(2) ).negate() )));
                 dfunction.set(t, i, storevalue.get(t));
            }
        }
        return dfunction;
    }

    
    /**
     * Zweite Partielle Ableitung der mit choose gewähltenFunktion in systemOfFunction(). 
     * Der Parameter Vector dient als Referenz für die Größe der Jakobimatrix und 
     * gleichzeitig als Container der Werte für die Stelle der Ableitung. 
     * 
     * @param vector
     * @return Matrix (Jakobimatrix)
     */
    public Matrix derive2( Vector vector, int choose )
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
            storevalue = ( systemOfFunctions(dqPlus, choose).sub( systemOfFunctions(z, choose).add(systemOfFunctions(z, choose)) ).add( systemOfFunctions(dqMinus, choose) ));

            for(int t=0; t < arguments; t++) 
            {
                storevalue.set(t, MathLib.round( storevalue.get(t).multiply( h.multiply(h) ).negate() ));
                 dfunction.set(t, i, storevalue.get(t));
            }
        }
        return dfunction;
    }
    
    
    
    public Vector getKontractionIntervall( Vector testvector, int choose )
    {
        Vector   func = systemOfFunctions( testvector, choose );
        Matrix  dfunc = derive(  testvector, choose );
        Matrix ddfunc = derive2( testvector, choose );
        
        recorder = Recorder.getInstance();
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
    
    
    
    public Matrix getJacobiMatrix(Vector vector, int choose) 
    {
        return derive(vector, choose);
    }
    
    
    /**
     * Tabelle mit Funktionen 
     * @param vector
     * @param choose
     * @return
     */
    public Vector systemOfFunctions(Vector vector, int choose) 
    {
        Vector function = new Vector( vector.getLength() );
        Double[]      x = vector.toDoubleArray();           // x[0] = x ; x[1] = y ; x[2] = z usw.
        
        for(int t=0; t < vector.getLength(); t++) function.set(t, BigDecimal.ZERO);
        
        // Hier die >>Funktionen/ Gleichungssysteme<< eintragen:
        switch (choose)
        {
            case 1: 
            {
                function.set(0, BigDecimal.valueOf(   x[0]*x[0]+x[1]*x[1]+0.6*x[1]-0.16       ).negate());
                function.set(1, BigDecimal.valueOf(   x[0]*x[0]-x[1]*x[1]+x[0]-1.6*x[1]-0.14  ).negate());
                break;
            }
            case 2: 
            {
                function.set(0, BigDecimal.valueOf(      x[0]*x[0]*x[0]+10*x[1]-x[0]*x[1]  ).negate());
                function.set(1, BigDecimal.valueOf( -1.4-x[0]+Math.cos(x[1])               ).negate());
                break;
            }
            case 3: 
            {
                function.set(0, BigDecimal.valueOf(    1-x[0]+Math.sin(x[1])  ).negate());
                function.set(1, BigDecimal.valueOf( -1.4-x[1]+Math.cos(x[0])  ).negate());
                break;
            }
            case 4: 
            {
                function.set(0, BigDecimal.valueOf(    1-x[1]+Math.sin(x[0])+x[3]       ).negate());
                function.set(1, BigDecimal.valueOf( -1.4-x[0]+Math.cos(x[1])-x[2]*x[3]  ).negate());
                function.set(2, BigDecimal.valueOf(    1-x[1]+x[2]                      ).negate());
                function.set(3, BigDecimal.valueOf(     -x[0]-2.67*x[3]                 ).negate());
                break;
            }
            case 5: 
            {
                function.set(0, BigDecimal.valueOf(   x[0]*x[0]-0.16    ).negate());
                break;
            }
            
            default: break;
        }

        
        return function;
    } 
}
