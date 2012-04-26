package numerik.calc_test;

import numerik.calc.*;
import java.math.BigDecimal;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathLib_TestFixture
{
    private BigDecimal m_BigDecimalInput;
    private BigDecimal m_BigDecimalOutput;
   
	@Test
    public void round__Exponent_Of_Decimal_Is_30_And_Precision_Is_12()
    {
        MathLib.setPrecision(12);
        
        m_BigDecimalInput = new BigDecimal("1.1111111111191234123412341234132E+30");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);
        
        assertEquals("1.111111111112E+30", m_BigDecimalOutput.toEngineeringString());
    }
	
	@Test
    public void round__Exponent_Of_Decimal_Is_1_And_Precision_Is_6()
    {
        MathLib.setPrecision(6);
        
        m_BigDecimalInput = new BigDecimal("1.111119");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);
        
        assertEquals("1.11112", m_BigDecimalOutput.toEngineeringString());
    }
	
	@Test
    public void round__Exponent_Of_Decimal_Is_Minus_20_And_Precision_Is_10()
    {
        MathLib.setPrecision(6);
        
        m_BigDecimalInput = new BigDecimal("1.11111111119E-20");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);
        
        assertEquals("1.111111112E-20", m_BigDecimalOutput.toEngineeringString());
    }
	
	@Test
    public void round__Exponent_Of_Decimal_Is_Zero_And_Precision_Is_20()
    {
        MathLib.setPrecision(20);
        
        m_BigDecimalInput = new BigDecimal("0.111111111111111111119");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);
        
        assertEquals("0.11111111111111111112", m_BigDecimalOutput.toEngineeringString());
    }
	
	@Test
	public void getExponent__Exponent_Of_Decimal_Is_40()
	{	    
	    m_BigDecimalInput = new BigDecimal("1.93423423423324234E+40");
	    int resultExponent = MathLib.getExponent(m_BigDecimalInput);
	    
	    assertEquals(40, resultExponent);
	}
	
	@Test
	public void getExponent__Exponent_Of_Decimal_Is_Zero()
	{
        m_BigDecimalInput = new BigDecimal("0.000000000");
        int resultExponent = MathLib.getExponent(m_BigDecimalInput);
        
        assertEquals(0, resultExponent);
	}
	
    @Test
    public void getExponent__Exponent_Of_Decimal_Is_One()
    {
        m_BigDecimalInput = new BigDecimal("9.9999999999");
        int resultExponent = MathLib.getExponent(m_BigDecimalInput);
        
        assertEquals(1, resultExponent);
    }
	
    @Test
    public void getExponent__Exponent_Of_Decimal_Is_Minus_One()
    {
        m_BigDecimalInput = new BigDecimal("0.9999999999");
        int resultExponent = MathLib.getExponent(m_BigDecimalInput);
        
        assertEquals(-1, resultExponent);
    }
	
	@Test
	public void getExponent__Exponent_Of_Decimal_Is_Minus_35()
	{
        m_BigDecimalInput = new BigDecimal("1.9999999999E-35");
        int resultExponent = MathLib.getExponent(m_BigDecimalInput);
        
        assertEquals(-35, resultExponent);
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
