package numerik.calc_test;

import numerik.calc.*;
import java.math.BigDecimal;
import org.junit.*;
import static org.junit.Assert.*;

public class BigDecimalExtension_TestFixture
{   
    private BigDecimal m_BigDecimalInput;
    private BigDecimal m_BigDecimalOutput;
    
    @Test
    public void greater__BigDecimal_Is_Not_Greater()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.111");
        BigDecimal expectedToBeGreater = new BigDecimal("-0.000111");
        
        boolean resultOfComparision = BigDecimalExtension.greater(bigDecimalToCompare, expectedToBeGreater);
        
        assertFalse(resultOfComparision);
    }
    
    @Test
    public void greater__BigDecimal_Is_Greater()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.111");
        BigDecimal expectedToBeGreater = new BigDecimal("1000342314324");
        
        boolean resultOfComparision = BigDecimalExtension.greater(bigDecimalToCompare, expectedToBeGreater);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void lesser__BigDecimal_Is_Not_Lesser()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("-0.111");
        BigDecimal expectedToBeLesser = new BigDecimal("0.000111");
        
        boolean resultOfComparision = BigDecimalExtension.lesser(bigDecimalToCompare, expectedToBeLesser);
        
        assertFalse(resultOfComparision);
    }
    
    @Test
    public void lesser__BigDecimal_Is_Lesser()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.111");
        BigDecimal expectedToBeLesser = new BigDecimal("-1000342314324");
        
        boolean resultOfComparision = BigDecimalExtension.lesser(bigDecimalToCompare, expectedToBeLesser);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void equals__BigDecimal_Is_Not_Equal()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("-0.111");
        BigDecimal expectedToBeEqual = new BigDecimal("0.000111");
        
        boolean resultOfComparision = BigDecimalExtension.equals(bigDecimalToCompare, expectedToBeEqual);
        
        assertFalse(resultOfComparision);
    }
    
    @Test
    public void equals__BigDecimal_Is_Equal()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.00000000000000000000");
        BigDecimal expectedToBeEqual = new BigDecimal("0");
        
        boolean resultOfComparision = BigDecimalExtension.equals(bigDecimalToCompare, expectedToBeEqual);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void unequals__BigDecimal_Is_Not_Unequal()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.00000000000000000000");
        BigDecimal expectedToBeUnequal = new BigDecimal("0");
        
        boolean resultOfComparision = BigDecimalExtension.unequals(bigDecimalToCompare, expectedToBeUnequal);
        
        assertFalse(resultOfComparision);
    }
    
    @Test
    public void unequals__BigDecimal_Is_Unequal()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.00000000000000000000");
        BigDecimal expectedToBeUnequal = new BigDecimal("0.00000000000000000001");
        
        boolean resultOfComparision = BigDecimalExtension.unequals(bigDecimalToCompare, expectedToBeUnequal);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void lesserOrEquals__BigDecimal_Is_Not_LesserOrEquals()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.00000000000000000000");
        BigDecimal expectedToBeLesserOrEqual = new BigDecimal("0.00000000000000000001");
        
        boolean resultOfComparision = BigDecimalExtension.lesserOrEquals(bigDecimalToCompare, expectedToBeLesserOrEqual);
        
        assertFalse(resultOfComparision);
    }
    
    @Test
    public void lesserOrEquals__BigDecimal_Is_Lesser()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.000000000000000000001");
        BigDecimal expectedToBeLesserOrEqual = new BigDecimal("-0.00000000000000000000");
        
        boolean resultOfComparision = BigDecimalExtension.lesserOrEquals(bigDecimalToCompare, expectedToBeLesserOrEqual);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void lesserOrEquals__BigDecimal_Is_Equal()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("-4.3");
        BigDecimal expectedToBeLesserOrEqual = new BigDecimal("-4.3");
        
        boolean resultOfComparision = BigDecimalExtension.lesserOrEquals(bigDecimalToCompare, expectedToBeLesserOrEqual);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void greaterOrEquals__BigDecimal_Is_Not_GreaterOrEquals()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.00000000000000000000");
        BigDecimal expectedToBeGreaterOrEqual = new BigDecimal("-0.00000000000000000001");
        
        boolean resultOfComparision = BigDecimalExtension.greaterOrEquals(bigDecimalToCompare, expectedToBeGreaterOrEqual);
        
        assertFalse(resultOfComparision);
    }
    
    @Test
    public void greaterOrEquals__BigDecimal_Is_Greater()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("0.000000000000000000001");
        BigDecimal expectedToBeGreaterOrEqual = new BigDecimal("1.00000000000000000000");
        
        boolean resultOfComparision = BigDecimalExtension.greaterOrEquals(bigDecimalToCompare, expectedToBeGreaterOrEqual);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void greaterOrEquals__BigDecimal_Is_Equal()
    {
        BigDecimal bigDecimalToCompare = new BigDecimal("4.3");
        BigDecimal expectedToBeGreaterOrEqual = new BigDecimal("4.3");
        
        boolean resultOfComparision = BigDecimalExtension.greaterOrEquals(bigDecimalToCompare, expectedToBeGreaterOrEqual);
        
        assertTrue(resultOfComparision);
    }
    
    @Test
    public void floor__BigDecimal_Should_Be_Rounded_Correctly()
    {
        m_BigDecimalInput = new BigDecimal("-5.22342");
        m_BigDecimalOutput = BigDecimalExtension.floor(m_BigDecimalInput);
        
        assertEquals("-6", m_BigDecimalOutput.toEngineeringString());
    }
    
    @Test
    public void ceiling__BigDecimal_Should_Be_Rounded_Correctly()
    {
        m_BigDecimalInput = new BigDecimal("-5.232");
        m_BigDecimalOutput = BigDecimalExtension.ceiling(m_BigDecimalInput);
        
        assertEquals("-5", m_BigDecimalOutput.toEngineeringString());
    }
    
    @Test
    public void roundingAwayFromZero_BigDecimal_Is_Zero()
    {
        m_BigDecimalInput = new BigDecimal("0");
        m_BigDecimalOutput = BigDecimalExtension.roundingAwayFromZero(m_BigDecimalInput);
        
        assertEquals("0", m_BigDecimalOutput.toEngineeringString());
    }
    
    @Test
    public void roundingAwayFromZero_BigDecimal_Is_Greater_Than_Zero()
    {
        m_BigDecimalInput = new BigDecimal("5.2232");
        m_BigDecimalOutput = BigDecimalExtension.roundingAwayFromZero(m_BigDecimalInput);
        
        assertEquals("6", m_BigDecimalOutput.toEngineeringString());
    }
    
    @Test
    public void roundingAwayFromZero_BigDecimal_Is_Less_Than_Zero()
    {
        m_BigDecimalInput = new BigDecimal("-5.2");
        m_BigDecimalOutput = BigDecimalExtension.roundingAwayFromZero(m_BigDecimalInput);
        
        assertEquals("-6", m_BigDecimalOutput.toEngineeringString());
    }
    
    /* Setzen von allgemeinen Werten, die bei jedem Test verwendet werden */
    @Before
    public void setUp()
    {
    }
    
    /* Zurücksetzen von Werten, um Ausgangszustand eines Tests zu erreichen --> wichtig für weitere Tests */
    @After
    public void tearDown()
    {
        m_BigDecimalInput = null;
        m_BigDecimalOutput = null;
    }
}
