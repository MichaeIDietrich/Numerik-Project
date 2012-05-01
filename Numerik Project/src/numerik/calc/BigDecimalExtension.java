package numerik.calc;

import java.math.BigDecimal;

public class BigDecimalExtension
{
    /* expectedToBeGreater > bigDecimalToCompare */
    public static boolean greater(BigDecimal bigDecimalToCompare, BigDecimal expectedToBeGreater)
    {
        return expectedToBeGreater.compareTo(bigDecimalToCompare) == 1;
    }
    
    /* expectedToBeLesser < bigDecimalToCompare */
    public static boolean lesser(BigDecimal bigDecimalToCompare, BigDecimal expectedToBeLesser)
    {
        return expectedToBeLesser.compareTo(bigDecimalToCompare) == -1;
    }
    
    /* expectedToBeEqual == bigDecimalToCompare */
    public static boolean equals(BigDecimal bigDecimalToCompare, BigDecimal expectedToBeEqual)
    {
        return expectedToBeEqual.compareTo(bigDecimalToCompare) == 0;
    }
    
    /* expectedToBeUnequal != bigDecimalToCompare */
    public static boolean unequals(BigDecimal bigDecimalToCompare, BigDecimal expectedToBeUnequal)
    {
        return expectedToBeUnequal.compareTo(bigDecimalToCompare) != 0;
    }
    
    public static boolean lesserOrEquals(BigDecimal bigDecimalToCompare, BigDecimal expectedToBeLesserOrEqual)
    {
        return lesser(bigDecimalToCompare, expectedToBeLesserOrEqual) || equals(bigDecimalToCompare, expectedToBeLesserOrEqual);
    }
    
    public static boolean greaterOrEquals(BigDecimal bigDecimalToCompare, BigDecimal expectedToBeGreaterOrEqual)
    {
        return greater(bigDecimalToCompare, expectedToBeGreaterOrEqual) || equals(bigDecimalToCompare, expectedToBeGreaterOrEqual);
    }
    
    /* Bis zur nächsten Ganzzahl abrunden */
    public static BigDecimal floor(BigDecimal value)
    {
        return value.setScale(0, BigDecimal.ROUND_FLOOR);
    }
    
    /* Bis zur nächsten Ganzzahl aufrunden */
    public static BigDecimal ceiling(BigDecimal value)
    {
        return value.setScale(0, BigDecimal.ROUND_CEILING);
    }
    
    /* Von der Null wegrunden */
    public static BigDecimal roundingAwayFromZero(BigDecimal value)
    {
        return BigDecimalExtension.greater(new BigDecimal("0"), value) ? BigDecimalExtension.ceiling(value) : BigDecimalExtension.floor(value);
    }
}
