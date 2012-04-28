package numerik.calc_test;

import numerik.calc.*;

import java.math.BigDecimal;
import org.junit.*;
import static org.junit.Assert.*;

public class MathLib_TestFixture {
	private BigDecimal m_BigDecimalInput;
	private BigDecimal m_BigDecimalOutput;

	private String m_BigDecimalStringOutput;

	@Test
	public void round__Exponent_Of_Decimal_Is_30_And_Precision_Is_12() {
		MathLib.setPrecision(12);

		m_BigDecimalInput = new BigDecimal(
				"1.1111111111191234123412341234132E+30");
		m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

		assertEquals("1.11111111112E+30",
				m_BigDecimalOutput.toEngineeringString());
	}

	@Test
	public void round__Exponent_Of_Decimal_Is_1_And_Precision_Is_6() {
		MathLib.setPrecision(6);

		m_BigDecimalInput = new BigDecimal("1.111119");
		m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

		assertEquals("1.11112", m_BigDecimalOutput.toEngineeringString());
	}
	
    @Test
    public void round__Exponent_Of_Decimal_Is_10_And_Precision_Is_6() {
        MathLib.setPrecision(6);

        m_BigDecimalInput = new BigDecimal("1111119000");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

        assertEquals("1111120000", m_BigDecimalOutput.toPlainString());
    }

	@Test
	public void round__Exponent_Of_Decimal_Is_Minus_20_And_Precision_Is_10() {
		MathLib.setPrecision(10);

		m_BigDecimalInput = new BigDecimal("1.1111111119E-20");
		m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

		assertEquals("11.11111112E-21",
				m_BigDecimalOutput.toEngineeringString());
	}

	@Test
	public void round__Exponent_Of_Decimal_Is_Minus_One_And_Precision_Is_20() {
		MathLib.setPrecision(20);

		m_BigDecimalInput = new BigDecimal("0.111111111111111111119");
		m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

		assertEquals("0.11111111111111111112",
				m_BigDecimalOutput.toEngineeringString());
	}

    @Test
    public void round__Exponent_Of_Decimal_Is_Minus_5_And_Precision_Is_20() {
        MathLib.setPrecision(20);

        m_BigDecimalInput = new BigDecimal("0.0000111111111111111111119");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

        assertEquals("0.000011111111111111111112",
                m_BigDecimalOutput.toPlainString());
    }
	
    @Test
    public void round__Exponent_Of_Negative_Decimal_Is_Minus_5_And_Precision_Is_20() {
        MathLib.setPrecision(20);

        m_BigDecimalInput = new BigDecimal("-0.0000111111111111111111119");
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

        assertEquals("-0.000011111111111111111112",
                m_BigDecimalOutput.toPlainString());
    }
    
    @Test
    public void round__Exponent_Of_Decimal_Is_Zero_And_Precision_Is_20() {
        MathLib.setPrecision(20);

        m_BigDecimalInput = BigDecimal.ZERO;
        m_BigDecimalOutput = MathLib.round(m_BigDecimalInput);

        assertTrue(BigDecimalExtension.equals(BigDecimal.ZERO, m_BigDecimalOutput));
    }
	
	@Test
	public void getExponent__Exponent_Of_Decimal_Is_40() {
		m_BigDecimalInput = new BigDecimal("1.93423423423324234E+40");
		int resultExponent = MathLib.getExponent(m_BigDecimalInput);

		assertEquals(40, resultExponent);
	}

	@Test
	public void getExponent__Exponent_Of_Decimal_Is_Zero() {
		m_BigDecimalInput = new BigDecimal("0.000000000");
		int resultExponent = MathLib.getExponent(m_BigDecimalInput);

		assertEquals(0, resultExponent);
	}

	@Test
	public void getExponent__Exponent_Of_Decimal_Is_One() {
		m_BigDecimalInput = new BigDecimal("9.9999999999");
		int resultExponent = MathLib.getExponent(m_BigDecimalInput);

		assertEquals(1, resultExponent);
	}

	@Test
	public void getExponent__Exponent_Of_Decimal_Is_Minus_One() {
		m_BigDecimalInput = new BigDecimal("0.9999999999");
		int resultExponent = MathLib.getExponent(m_BigDecimalInput);

		assertEquals(-1, resultExponent);
	}

	@Test
	public void getExponent__Exponent_Of_Decimal_Is_Minus_35() {
		m_BigDecimalInput = new BigDecimal("1.9999999999E-35");
		int resultExponent = MathLib.getExponent(m_BigDecimalInput);

		assertEquals(-35, resultExponent);
	}

	@Test
	public void log10__Logarithmus_Base_10_Test_One() {
		MathLib.setPrecision(12);
		m_BigDecimalInput = new BigDecimal("3.2154");
		m_BigDecimalOutput = MathLib.log10(m_BigDecimalInput);

		m_BigDecimalStringOutput = m_BigDecimalOutput.toEngineeringString();

		assertTrue(m_BigDecimalStringOutput.contains("0.507235007427"));
	}

	@Test
	public void log10__Logarithmus_Base_10_Test_Two() {
		m_BigDecimalInput = new BigDecimal("0");
		m_BigDecimalOutput = MathLib.log10(m_BigDecimalInput);

		m_BigDecimalStringOutput = m_BigDecimalOutput.toEngineeringString();

		assertTrue(m_BigDecimalStringOutput.contains("undef"));
	}

	@Test
	public void log10__Logarithmus_Base_10_Test_Three() {
		m_BigDecimalInput = new BigDecimal("0");
		m_BigDecimalOutput = MathLib.log10(m_BigDecimalInput);

		m_BigDecimalStringOutput = m_BigDecimalOutput.toEngineeringString();

		assertTrue(m_BigDecimalStringOutput.contains("undef"));
	}

	@Test
	public void log10__Logarithmus_Base_10_Test_Four() {
		m_BigDecimalInput = new BigDecimal("4.32544E+30");
		m_BigDecimalOutput = MathLib.log10(m_BigDecimalInput);

		m_BigDecimalStringOutput = m_BigDecimalOutput.toEngineeringString();

		assertTrue(m_BigDecimalStringOutput.contains("30.6360302921"));
	}

	@Test
	public void log10__Logarithmus_Base_10_Test_Five() {
		m_BigDecimalInput = new BigDecimal("4.32544E-30");
		m_BigDecimalOutput = MathLib.log10(m_BigDecimalInput);

		m_BigDecimalStringOutput = m_BigDecimalOutput.toEngineeringString();

		assertTrue(m_BigDecimalStringOutput.contains("-29.93639697079"));
	}

	@Test
	public void log10__Logarithmus_Base_10_Test_Six() {
		m_BigDecimalInput = new BigDecimal("-1");
		m_BigDecimalOutput = MathLib.log10(m_BigDecimalInput);

		m_BigDecimalStringOutput = m_BigDecimalOutput.toEngineeringString();

		assertTrue(m_BigDecimalStringOutput.contains("nicht-reelles Ergebnis"));
	}

	/* Setzen von allgemeinen Werten, die bei jedem Test verwendet werden */
	@Before
	public void setUp()
	{
	    MathLib.enableRound(true);
	}

	/*
	 * Zurücksetzen von Werten, um Ausgangszustand eines Tests zu erreichen -->
	 * wichtig für weitere Tests
	 */
	@After
	public void tearDown() {
		m_BigDecimalInput = null;
		m_BigDecimalOutput = null;

		m_BigDecimalStringOutput = null;
		
		MathLib.enableRound(false);
	}
}
