package numerik.calc;

import java.math.*;

// Wrapper-Klasse für BigDecimal, um den Umgang zu verbessern

public final class Decimal
{
    
    public final static Decimal ZERO = new Decimal(BigDecimal.ZERO);
    public final static Decimal ONE = new Decimal(BigDecimal.ONE);
    public final static Decimal TEN = new Decimal(BigDecimal.TEN);
    
    private BigDecimal number;

    
    // Contructors der BigDecimal-Klasse
    
    public Decimal(BigInteger unscaledVal, int scale, MathContext mc)
    {
        number = new BigDecimal(unscaledVal, scale, mc);
    }
    
    public Decimal(BigInteger unscaledVal, int scale)
    {
        number = new BigDecimal(unscaledVal, scale);
    }
    
    public Decimal(BigInteger val, MathContext mc)
    {
        number = new BigDecimal(val, mc);
    }
    
    public Decimal(BigInteger val)
    {
        number = new BigDecimal(val);
    }
    
    public Decimal(char[] in, int offset, int len, MathContext mc)
    {
        number = new BigDecimal(in, offset, len, mc);
    }
    
    public Decimal(char[] in, int offset, int len)
    {
        number = new BigDecimal(in, offset, len);
    }
    
    public Decimal(char[] in, MathContext mc)
    {
        number = new BigDecimal(in, mc);
    }
    
    public Decimal(char[] in)
    {
        number = new BigDecimal(in);
    }
    
    public Decimal(double val, MathContext mc)
    {
        number = new BigDecimal(val, mc);
    }
    
    public Decimal(int val, MathContext mc)
    {
        number = new BigDecimal(val, mc);
    }
    
    public Decimal(int val)
    {
        number = new BigDecimal(val);
    }
    
    public Decimal(long val, MathContext mc)
    {
        number = new BigDecimal(val, mc);
    }
    
    public Decimal(long val)
    {
        number = new BigDecimal(val);
    }
    
    public Decimal(String val, MathContext mc)
    {
        number = new BigDecimal(val, mc);
    }
    
    public Decimal(String val)
    {
        number = new BigDecimal(val);
    }
    
    public Decimal(double val)
    {
        number = new BigDecimal(val);
    }
    
    // Copy-Constructer (Wrapper)
    private Decimal(BigDecimal val)
    {
        number = val;
    }
    
    
    public Decimal negate()
    {
        return new Decimal(number.negate());
    }
    
    public Decimal abs()
    {
        return new Decimal(number.abs());
    }
    
    public Decimal pow(int n)
    {
        return new Decimal(number.pow(n));
    }
    
    public int signum()
    {
        return number.signum();
    }
    
    
    public Decimal round()
    {
        return MathLib.round(this);
    }
    
    public Decimal round(MathContext mc)
    {
        return new Decimal(number.round(mc));
    }
    
    public Decimal add(Decimal augend)
    {
        return new Decimal(number.add(augend.getBigDecimal())).round();
    }
    
    public Decimal sub(Decimal subtrahend)
    {
        return new Decimal(number.subtract(subtrahend.getBigDecimal())).round();
    }
    
    public Decimal mult(Decimal multiplicand)
    {
        return new Decimal(number.multiply(multiplicand.getBigDecimal())).round();
    }
    
    public Decimal div(Decimal divisor)
    {
        if (MathLib.getRoundingMode() == MathLib.EXACT)
        {
            return div(divisor, new MathContext(MathLib.getPrecision(), RoundingMode.HALF_UP));
        }
        return div(divisor, MathLib.getPrecision(), RoundingMode.HALF_UP);
    }
    
    public Decimal div(Decimal divisor, MathContext mc)
    {
        return new Decimal(number.divide(divisor.getBigDecimal(), mc)).round();
    }
    
    public Decimal div(Decimal divisor, int scale, RoundingMode roundingMode)
    {
        return new Decimal(number.divide(divisor.getBigDecimal(), scale, roundingMode)).round();
    }
    
    public Decimal setScale(int newScale, RoundingMode roundingMode)
    {
        return new Decimal(number.setScale(newScale, roundingMode));
    }
    
    public Decimal valueOf(double val)
    {
        return new Decimal(BigDecimal.valueOf(val));
    }
    
    public int intValue()
    {
        return number.intValue();
    }
    
    public double doubleValue()
    {
        return number.doubleValue();
    }
    
    
    public Decimal stripTrailingZeros()
    {
        String plainString = number.toPlainString();
        
        int dotPos = plainString.indexOf('.');
        if (dotPos > 0)
        {
            char[] plain = plainString.toCharArray();
            
            for (int i = plain.length - 1; i >= dotPos; i--)
            {
                if (plain[i] != '0')
                {
                    if (i == dotPos) { // Sonderfall, dass nach dem Komma nur Nullen stehen, dann fällt das Komma auch weg
                        return new Decimal(new String(plain, 0, i));
                    }
                    return new Decimal(new String(plain, 0, i + 1));
                }
            }
        }
        return this;
    }
    
    public int compareTo(Decimal val)
    {
        return number.compareTo(val.getBigDecimal());
    }
    
    @Override
    public String toString()
    {
        return number.toPlainString();
    }
    
    BigDecimal getBigDecimal()
    {
        return number;
    }
}