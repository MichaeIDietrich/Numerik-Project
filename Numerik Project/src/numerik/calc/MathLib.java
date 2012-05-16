
package numerik.calc;

import java.math.*;

public final class MathLib
{
    
    public  static final int               EXACT = 0;
    public  static final int              NORMAL = 1;
    
    public  static final int    ZEILENSUMMENNORM = 0;
    public  static final int FROBENIUSEUKILDNORM = 1;
    
    private static boolean         pivotstrategy = false;
    private static boolean                active = true;
    private static int                 precision = 5;
    private static int         inverse_precision = 20;
    private static int              roundingmode = 0;
    private static int                      norm = 0;
    
    
    public static BigDecimal round(BigDecimal value)
    {
        if (active)
        {
            if (MathLib.getRoundingMode() == MathLib.EXACT ) 
            {
                value = value.round(new MathContext( precision, RoundingMode.HALF_UP ));
            }
            
            if (MathLib.getRoundingMode() == MathLib.NORMAL ) 
            {
                value = value.setScale( precision, RoundingMode.HALF_UP );
            }
        }
        
        if(value.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO;
        
        return value.stripTrailingZeros();
    }
    
    
    
    // auskommentiert, was nicht gebraucht wird
    
    /*// Die gleiche Funktion ist BigDecimalMath.log(BigDecimal x), enthalten in BigDecimalMath.jar
    // Funktion stammt von:
    // http://www.humbug.in/stackoverflow/de/logarithm-of-a-bigdecimal-739532.html
    // siehe unten auf der Seite
    public static BigDecimal ln(BigDecimal x)
    {
        // diese beiden Variablen vllt. von als Parameter oder so übergeben,
        // bzw. von precision abhängig machen
        int ITER = 1000;
        MathContext context = new MathContext(100);
        
        if (x.equals(BigDecimal.ONE))
        {
            return BigDecimal.ZERO;
        }
        
        x = x.subtract(BigDecimal.ONE);
        BigDecimal ret = new BigDecimal(ITER + 1);
        for (long i = ITER; i >= 0; i--)
        {
            BigDecimal N = new BigDecimal(i / 2 + 1).pow(2);
            N = N.multiply(x, context);
            ret = N.divide(ret, context);
            
            N = new BigDecimal(i + 1);
            ret = ret.add(N, context);
            
        }
        
        ret = x.divide(ret, context);
        return ret;
    }
    
    
    // Logarithmus zur Basis 10
    public static BigDecimal log10(BigDecimal value) 
    {
        return ln(value).divide(ln(BigDecimal.TEN), precision, BigDecimal.ROUND_HALF_UP);
    }
    
    
    // Berechnen eines Exponenten einer Zahl
    public static int getExponent(BigDecimal value)
    {
        if (BigDecimalExtension.equals(new BigDecimal(0), value))
        {
            return 0;
        }
            
        BigDecimal absoluteDecimal = value.abs();
        BigDecimal exponent = MathLib.log10(absoluteDecimal);
        
        String roundedValue = BigDecimalExtension.roundingAwayFromZero(exponent).toEngineeringString();
        
        return Integer.valueOf(roundedValue);
    }*/
    
    
    public static BigDecimal stripTrailingZeros(BigDecimal value)
    {
        
        char[] plain = value.toPlainString().toCharArray();
        for (int i = plain.length - 1; i > 0; i--)
        {
            if (plain[i] != '0')
            {
                return new BigDecimal(new String(plain, 0, i + 1));
            }
        }
        return value;
    }
    
    
    public static BigDecimal ln(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.log(value.doubleValue())));
    }
    
    
    public static BigDecimal log(BigDecimal value, BigDecimal base)
    {
        return round(ln(value).divide(ln(base), precision, RoundingMode.HALF_UP));
    }
    
    
    public static BigDecimal sin(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.sin(value.doubleValue())));
    }
    
    
    public static BigDecimal cos(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.cos(value.doubleValue())));
    }
    
    
    public static BigDecimal tan(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.tan(value.doubleValue())));
    }
    
    
    public static BigDecimal asin(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.asin(value.doubleValue())));
    }
    
    
    public static BigDecimal acos(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.acos(value.doubleValue())));
    }
    
    
    public static BigDecimal atan(BigDecimal value)
    {
        return round(BigDecimal.valueOf(Math.atan(value.doubleValue())));
    }
    
    
    public static BigDecimal sqrt(BigDecimal root) {
        
        return root_n_Of(root, 2);
    }
    
    
    public static BigDecimal root_n_Of(BigDecimal root, int k) {
        
        BigDecimal a = root.add(BigDecimal.valueOf(1));
        BigDecimal x = root.divide( BigDecimal.valueOf(2) );
        BigDecimal comparand  = BigDecimal.ONE.divide( BigDecimal.TEN.pow( getPrecision() ));
        
        // a, x siehe Wikipedia Wurzelberechnung nach Heron
        if(root.compareTo( BigDecimal.ZERO ) != 0) {
            while( (a.subtract(x)).abs().compareTo( comparand )==1 ) {
                a = x;
                x = BigDecimal.valueOf( k-1 ).multiply( x.pow( k )).add( root )
                              .divide( BigDecimal.valueOf( k ).multiply( x.pow( k-1 ) ), 2*getPrecision() , RoundingMode.HALF_UP );
            }
        } else {
            return root;
        }
        return round(x);
    }
    
    
    public static Vector doubleArrayToVector( Double[] f ) {
        
        Vector x = new Vector(f.length);
        
        for(int i=0; i<=f.length; i++) {
            x.set(i, BigDecimal.valueOf( f[i] ) );
        }
        
        return null;
    }
    
    
    public static int getNorm()
    {
        return norm;
    }
    
    
    public static void setNorm(int norm)
    {
        MathLib.norm = norm;
    }
    
    
    // getters and setters
    public static int getPrecision()
    {
        return precision;
    }
    
    public static void setPrecision(int precision)
    {
        MathLib.precision = precision;
    }
    
    public static boolean isActive()
    {
        return active;
    }
    
    public static void enableRound(boolean active)
    {
        MathLib.active = active;
    }
    
    public static boolean isPivotStrategy() {
        return pivotstrategy;
    }
    
    public static void setPivotStrategy(boolean pivotstrategy) {
        MathLib.pivotstrategy = pivotstrategy;
    }
    
    public static void setRoundingMode(int mode) {
        MathLib.roundingmode = mode;
    }
    
    public static int getRoundingMode() {
        return MathLib.roundingmode;
    }
    
    
    public static int getInversePrecision() {
        return inverse_precision;
    }
    
    
    public static void setInversePrecision(int inverse_precision) {
        MathLib.inverse_precision = inverse_precision;
    }
}
