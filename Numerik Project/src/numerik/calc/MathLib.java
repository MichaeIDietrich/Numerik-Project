
package numerik.calc;

import java.math.*;

public class MathLib
{
    private static boolean pivotstrategy = false;
    private static boolean        active = true;
    private static int         precision = 5;
    private static int inverse_precision = 20;
    private static int      roundingmode = 0;
    public  static final int       exact = 0;
    public  static final int      normal = 1;
        
    
    public static BigDecimal round(BigDecimal value)
    {
        if (active)
        {
            if (MathLib.getRoundingMode() == MathLib.exact ) 
            {
                value = value.round(new MathContext( precision, RoundingMode.HALF_UP ));
            }
            
            if (MathLib.getRoundingMode() == MathLib.normal ) 
            {
                value = value.setScale( precision, RoundingMode.HALF_UP );
            }
        }
        if (BigDecimalExtension.equals(value, BigDecimal.ZERO))
        {
            return BigDecimal.ZERO;
        }
        return value.stripTrailingZeros();
    }
   
    
    // Die gleiche Funktion ist BigDecimalMath.log(BigDecimal x), enthalten in BigDecimalMath.jar
    // Funktion stammt von:
    // http://www.humbug.in/stackoverflow/de/logarithm-of-a-bigdecimal-739532.html
    // siehe unten auf der Seite
    /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     */
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
}
