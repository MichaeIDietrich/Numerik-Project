package numerik.calc;

import java.math.*;
import org.nevec.rjm.BigDecimalMath;

public class MathLib
{
    private static int precision;
    private static boolean active;
    
    public static BigDecimal round(BigDecimal value)
    {
        // ziemlich wahrscheinlich noch fehlerhaft -> ungenutzte Variablen
        if (active)
        {
            
            long tempmant = 0;
            double tempexp = 0;
            String tempstr[];
            boolean sign = value.signum() == -1;
            value = value.abs();
            
            String strValue = value.toEngineeringString();
            
            String[] parts = strValue.split("\\.");
            String intDigits = parts[0];
            String fracDigits = parts[1];
            int exponent = 0;
            int mantissa = 0;
            
            if (fracDigits.contains("E"))
            {
                parts = fracDigits.split("E");
                fracDigits = parts[0];
                exponent = Integer.valueOf(parts[1]);
            }
            
            if (Integer.valueOf(intDigits) != 0 && value.doubleValue() != 0)
            {
                
                mantissa = mantissa - intDigits.length();
                
            }
            else
            {
                
                tempexp = fracDigits.length();
                tempmant = Long.valueOf(fracDigits);
                
                fracDigits = String.valueOf(tempmant);
                mantissa = precision + 3;
                
                if (fracDigits.length() < mantissa)
                {
                    // System.out.println( "nachher: "+zahl+"\n" );
                    
                }
                else
                {
                    
                    // zahl = toBD( String.copyValueOf( nkomma.toCharArray(), 0,
                    // mantisse+1 ) );
                    // d_mantisse =
                    // (int)(-(tempexp-String.valueOf(tempmant).length()+mantisse)-2);
                    // zahl = zahl.divide( toBD( toBD( Math.pow(10,
                    // 1)).doubleValue()), 0, BigDecimal.ROUND_HALF_UP);
                    // zahl = zahl.divide( toBD( Math.pow(10, mantisse)),
                    // mantisse, BigDecimal.ROUND_HALF_UP);
                    // dec = zahl.multiply( toBD(sign) );
                    
                    // System.out.println("nachher: "+dec+"\n");
                }
            }
            
            value = value.multiply(new BigDecimal(10).pow(mantissa + 1)).setScale(0, BigDecimal.ROUND_FLOOR);
            value = value.divide(BigDecimal.TEN, 0, BigDecimal.ROUND_HALF_UP);
            value = value.divide(BigDecimal.TEN.pow(mantissa), precision, BigDecimal.ROUND_HALF_UP);
            if (sign)
            {
                value = value.negate();
            }
            
            // System.out.println("nachher: "+ dec +", d_mantisse: "+ d_mantisse
            // );
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
    	return BigDecimalMath.log(value).divide(BigDecimalMath.log(BigDecimal.TEN));
        //return ln(value).divide(ln(BigDecimal.TEN));
    }
    
    // Berechnen eines Exponenten einer Zahl
    public static int getExponent(BigDecimal value)
    {
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
}