package numerik.tests;

import numerik.calc.*;

import java.math.BigDecimal;
import org.junit.*;
import org.junit.rules.*;

import static org.junit.Assert.*;
import static numerik.calc.Vector.*;

public class Vector_TestFixture
{
    private Vector vectorInput;
    private Matrix matrixInput;
    
    private Matrix matrixOutput;
    private Vector vectorOutput;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void mult__Multipliziere_eine_3x3_Matrix_mit_einem_Skalaren()
    {
        vectorInput = new Vector(3);
        
        vectorInput.set(0, new BigDecimal("4.332412341"));
        vectorInput.set(1, new BigDecimal("5.111112"));
        vectorInput.set(2, new BigDecimal("-9399999"));
        
        vectorOutput = vectorInput.mult(new BigDecimal("9999995"));
        
        assertEquals("43324100", vectorOutput.get(0).toPlainString());
        assertEquals("51111100", vectorOutput.get(1).toPlainString());
        assertEquals("-94000000000000", vectorOutput.get(2).toPlainString());
    }
    
    @Test
    public void divide_Dividiere_eine_3x3_Matrix_mit_einem_Skalaren()
    {
        vectorInput = new Vector(3);
        
        vectorInput.set(0, new BigDecimal("4.332412341"));
        vectorInput.set(1, new BigDecimal("5.111112"));
        vectorInput.set(2, new BigDecimal("-9399999"));
        
        vectorOutput = vectorInput.divide(new BigDecimal("0.00000009999995"));
        
        assertEquals("43324100", vectorOutput.get(0).toPlainString());
        assertEquals("51111100", vectorOutput.get(1).toPlainString());
        assertEquals("-94000000000000", vectorOutput.get(2).toPlainString());
    }
    
    @Test
    public void divide_Dividiere_eine_3x3_Matrix_mit_einem_Skalaren_Zero()
    {
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage("Bei der Skalardivision kann nicht durch 0 geteilt werden.");
        
        vectorInput = new Vector(3);
        
        vectorInput.set(0, new BigDecimal("4.332412341"));
        vectorInput.set(1, new BigDecimal("5.111112"));
        vectorInput.set(2, new BigDecimal("-9399999"));
        
        vectorOutput = vectorInput.divide(new BigDecimal("0.00000000"));
    }
    
    /* Setzen von allgemeinen Werten, die bei jedem Test verwendet werden */
    @Before
    public void setUp()
    {
        MathLib.setPrecision(6);
        MathLib.enableRound(true);
    }
    
    /*
     * Zurücksetzen von Werten, um Ausgangszustand eines Tests zu erreichen -->
     * wichtig für weitere Tests
     */
    @After
    public void tearDown()
    {
        vectorInput = null;
        matrixInput = null;
        
        matrixOutput = null;
        
        MathLib.enableRound(false);
    }
}
