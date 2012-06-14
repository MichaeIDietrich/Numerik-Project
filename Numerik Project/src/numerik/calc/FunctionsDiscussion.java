package numerik.calc;

import java.math.BigDecimal;

public class FunctionsDiscussion
{
    Vector  dqPlus;
    Vector dqMinus;
    
    /**
     * Partielles Ableiten aller Funktionen in getFunctionsValue() und speichern
     * aller Werte in einer Matrix, der Jakobimatrix. Der Parameter Vector dient 
     * als Referenz für die Größe der Jakobimatrix und gleichzeitig als Container
     * für die Stelle der Ableitung. 
     * @param vector
     * @return Matrix
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
                    dqMinus.set(row, new BigDecimal(1.23456789123456789));  // Asymptote der unten eingegebenen Funktionen ist!
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

    
    public Matrix getJacobiMatrix(Vector vector, int choose) 
    {
        return derive(vector, choose);
    }
    
    
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
                function.set(0, BigDecimal.valueOf(    1-x[1]+Math.sin(x[0])  ).negate());
                function.set(1, BigDecimal.valueOf( -1.4-x[0]+Math.cos(x[1])  ).negate());
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
            default: break;
        }

        
        return function;
    } 
}
