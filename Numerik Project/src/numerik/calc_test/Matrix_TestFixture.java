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
    //private Matrix MSub;
    private Matrix MMul;
    //private Matrix MDiv;
    
    @Test
    public void add__2x2_Matrizen_addieren()
    {
        M1 = new Matrix(2,2);
        M2 = new Matrix(2,2);
        
        M1.set(0, 0, new BigDecimal("1.53578"));
        M1.set(0, 1, new BigDecimal("2.83493"));
        M1.set(1, 0, new BigDecimal("3.86645"));
        M1.set(1, 1, new BigDecimal("4.64339"));
        
        M2.set(0, 0, new BigDecimal("5.24865"));
        M2.set(0, 1, new BigDecimal("6.98699"));
        M2.set(1, 0, new BigDecimal("7.12556"));
        M2.set(1, 1, new BigDecimal("8.18253"));
        
        
        MAdd = M1.add(M2);
                
        assertEquals("6.78443", MAdd.get(0,0).toPlainString());
        assertEquals("9.82192", MAdd.get(0,1).toPlainString());
        assertEquals("10.99201", MAdd.get(1,0).toPlainString());
        assertEquals("12.82592", MAdd.get(1,1).toPlainString());
   }
    
    @Test
    public void mult__2x2_Matrizen_multiplizieren()
    {
        M1 = new Matrix(2,2);
        M2 = new Matrix(2,2);

        M1.set(0, 0, new BigDecimal("1.53578"));
        M1.set(0, 1, new BigDecimal("2.83493"));
        M1.set(1, 0, new BigDecimal("3.86645"));
        M1.set(1, 1, new BigDecimal("4.64339"));
        
        M2.set(0, 0, new BigDecimal("5.24865"));
        M2.set(0, 1, new BigDecimal("6.98699"));
        M2.set(1, 0, new BigDecimal("7.12556"));
        M2.set(1, 1, new BigDecimal("8.18253"));
        
        MMul = M1.mult(M2);
        
        assertEquals("28.2612", MMul.get(0,0).toPlainString());
        assertEquals("33.9274", MMul.get(0,1).toPlainString());
        assertEquals("53.3804", MMul.get(1,0).toPlainString());
        assertEquals("65.0095", MMul.get(1,1).toPlainString());
    }
    
    @Test
    public void getDiagonalMatrix__diagonalMatrix_Of_A_3x3_Matrix()
    {
        Matrix diagonalMatrix;
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("1.53578"));
        M1.set(0, 1, new BigDecimal("2.83493"));
        M1.set(0, 2, new BigDecimal("2.83493"));
        
        M1.set(1, 0, new BigDecimal("3.86645"));
        M1.set(1, 1, new BigDecimal("4.64339"));
        M1.set(1, 2, new BigDecimal("2.83493"));
        
        M1.set(2, 0, new BigDecimal("2.83493"));
        M1.set(2, 1, new BigDecimal("2.83493"));
        M1.set(2, 2, new BigDecimal("2.83493"));
        
        diagonalMatrix = M1.getDiagonalMatrix();
        
        assertEquals("1.536", diagonalMatrix.get(0, 0).toPlainString());
        assertEquals("0", diagonalMatrix.get(0, 1).toPlainString());
        assertEquals("0", diagonalMatrix.get(0, 2).toPlainString());
        
        assertEquals("0", diagonalMatrix.get(1, 0).toPlainString());
        assertEquals("4.643", diagonalMatrix.get(1, 1).toPlainString());
        assertEquals("0", diagonalMatrix.get(1, 2).toPlainString());
        
        assertEquals("0", diagonalMatrix.get(2, 0).toPlainString());
        assertEquals("0", diagonalMatrix.get(2, 1).toPlainString());
        assertEquals("2.835", diagonalMatrix.get(2, 2).toPlainString());
    }
    
    /* Setzen von allgemeinen Werten, die bei jedem Test verwendet werden */
    @Before
    public void setUp()
    {
        MathLib.setPrecision(4);
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