
package numerik.calc;

import java.math.*;

public class MathLib
{
    private static int precision;
    private static boolean pivotstrategy;
    private static boolean active;
    
    public static BigDecimal round(BigDecimal value)
    {
        if (active)
        {
            if (BigDecimalExtension.equals(value, BigDecimal.ZERO))
            {
                return value;
            }
            
            int sign = value.signum();
            
            /* destinguish sign */
            if (sign == -1)
            {
                return value.round(new MathContext(precision, RoundingMode.HALF_DOWN));
            }

            return value.round(new MathContext(precision, RoundingMode.HALF_UP));
        }
        
        return value;
    }

    //So würde ich das Runden unmsetzen
    public static BigDecimal roundMantissa(BigDecimal value, int scale) {
		boolean neg = value.doubleValue() < 0 ? true : false;
		if (neg)
			value.multiply(BigDecimal.valueOf(-1d));
		int exponent = log10(value).intValue();
		BigDecimal mantissa = value.multiply(BigDecimal.valueOf(Math.pow(10.0, -exponent)));
		BigDecimal bd = mantissa.setScale(scale,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal round_value = bd.multiply(BigDecimal.valueOf(Math.pow(10, exponent)));
		if (neg)
			round_value.multiply(BigDecimal.valueOf(-1d));
		return round_value;
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
    
    public static void setActive(boolean active)
    {
        MathLib.active = active;
    }

	public static boolean isPivotstrategy() {
		return pivotstrategy;
	}

	public static void setPivotstrategy(boolean pivotstrategy) {
		MathLib.pivotstrategy = pivotstrategy;
	}
}
