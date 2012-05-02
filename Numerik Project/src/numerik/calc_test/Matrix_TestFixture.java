package numerik.calc_test;


import numerik.calc.*;

import java.math.BigDecimal;
import org.junit.*;

import static org.junit.Assert.*;

public class Matrix_TestFixture
{
    private Matrix M1;
    private Matrix M2;
    private Matrix MAdd;
    private Matrix MSub;
    private Matrix MMul;
    private Matrix MDiv;
    
    @Test
    public void add__2x2_Matrizen_addieren()
    {
        M1 = new Matrix(2,2);
        M2 = new Matrix(2,2);
        
        M1.set(0, 0, new BigDecimal("1"));
        M1.set(0, 1, new BigDecimal("2"));
        M1.set(1, 0, new BigDecimal("3"));
        M1.set(1, 1, new BigDecimal("4"));
        
        M2.set(0, 0, new BigDecimal("5"));
        M2.set(0, 1, new BigDecimal("6"));
        M2.set(1, 0, new BigDecimal("7"));
        M2.set(1, 1, new BigDecimal("8"));
        
        
        MAdd = M1.add(M2);
                
        assertEquals("6", MAdd.get(0,0).toPlainString());
        assertEquals("8", MAdd.get(0,1).toPlainString());
        assertEquals("10", MAdd.get(1,0).toPlainString());
        assertEquals("12", MAdd.get(1,1).toPlainString());
   }
    
    @Test
    public void mult__2x2_Matrizen_multiplizieren()
    {
        M1 = new Matrix(2,2);
        M2 = new Matrix(2,2);

        M1.set(0, 0, new BigDecimal("1"));
        M1.set(0, 1, new BigDecimal("2"));
        M1.set(1, 0, new BigDecimal("3"));
        M1.set(1, 1, new BigDecimal("4"));
        
        M2.set(0, 0, new BigDecimal("5"));
        M2.set(0, 1, new BigDecimal("6"));
        M2.set(1, 0, new BigDecimal("7"));
        M2.set(1, 1, new BigDecimal("8"));
        
        MMul = M1.mult(M2);
        
        assertEquals("19", MMul.get(0,0).toPlainString());
        assertEquals("22", MMul.get(0,1).toPlainString());
        assertEquals("43", MMul.get(1,0).toPlainString());
        assertEquals("50", MMul.get(1,1).toPlainString());
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
    public void tearDown()
    {
        M1 = null;
        M2 = null;
        MAdd = null;
        MMul = null;
        MathLib.enableRound(false);
    }
    
}
