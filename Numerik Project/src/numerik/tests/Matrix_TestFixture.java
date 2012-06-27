package numerik.tests;

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
    //private Matrix MDiv;
    private Vector V1;
    private Vector V2;
    
    private String S1;
    
    @Test
    public void add__3x3_Matrizen_addieren()
    {
        MathLib.setPrecision(6);
        
        M1 = new Matrix(3,3);
        M2 = new Matrix(3,3);
        
        M1.set(0, 0, new BigDecimal("1.53578"));
        M1.set(0, 1, new BigDecimal("2.83493"));
        M1.set(0, 2, new BigDecimal("0"));
        
        M1.set(1, 0, new BigDecimal("3.86645"));
        M1.set(1, 1, new BigDecimal("4.64339"));
        M1.set(1, 2, new BigDecimal("9000000"));
        
        M1.set(2, 0, new BigDecimal("9400000"));
        M1.set(2, 1, new BigDecimal("9400000"));
        M1.set(2, 2, new BigDecimal("9400000"));
        
        M2.set(0, 0, new BigDecimal("5.24865"));
        M2.set(0, 1, new BigDecimal("6.98699"));
        M2.set(0, 2, new BigDecimal("0.00000001"));
        
        M2.set(1, 0, new BigDecimal("7.12556"));
        M2.set(1, 1, new BigDecimal("8.18253"));
        M2.set(1, 2, new BigDecimal("0.00001"));
        
        M2.set(2, 0, new BigDecimal("4.332412341"));
        M2.set(2, 1, new BigDecimal("5.111112"));
        M2.set(2, 2, new BigDecimal("-9399999"));
        
        
        MAdd = M1.add(M2);
                
        assertEquals("6.78443", MAdd.get(0,0).toPlainString());
        assertEquals("9.82192", MAdd.get(0,1).toPlainString());
        assertEquals("0.00000001", MAdd.get(0,2).toPlainString());
        
        assertEquals("10.992", MAdd.get(1,0).toPlainString());
        assertEquals("12.8259", MAdd.get(1,1).toPlainString());
        assertEquals("9000000", MAdd.get(1,2).toPlainString());
        
        assertEquals("9400000", MAdd.get(2,0).toPlainString());
        assertEquals("9400010", MAdd.get(2,1).toPlainString());
        assertEquals("0", MAdd.get(2,2).toPlainString());
    }
    
    
    
    @Test
    public void subtr_3x3_Matrizen_mit_Praezision_6()
    {
        MathLib.setPrecision(6);
        
        M1 = new Matrix(3,3);
        M2 = new Matrix(3,3);
        
        M1.set(0, 0, new BigDecimal("1.53578"));
        M1.set(0, 1, new BigDecimal("2.83493"));
        M1.set(0, 2, new BigDecimal("1.13472"));
        M1.set(1, 0, new BigDecimal("3.86645"));
        M1.set(1, 1, new BigDecimal("4.64339"));
        M1.set(1, 2, new BigDecimal("1.94389"));
        M1.set(2, 0, new BigDecimal("1.59453"));
        M1.set(2, 1, new BigDecimal("4.39971"));
        M1.set(2, 2, new BigDecimal("5.03243"));
        
        
        M2.set(0, 0, new BigDecimal("5.24865"));
        M2.set(0, 1, new BigDecimal("6.98699"));
        M2.set(0, 2, new BigDecimal("9.68713"));
        M2.set(1, 0, new BigDecimal("7.12556"));
        M2.set(1, 1, new BigDecimal("8.18253"));
        M2.set(1, 2, new BigDecimal("6.64871"));
        M2.set(2, 0, new BigDecimal("5.18689"));
        M2.set(2, 1, new BigDecimal("9400000"));
        M2.set(2, 2, new BigDecimal("9400000"));
        
        MSub = M2.subtract(M1);
        assertEquals("3.71287", MSub.get(0,0).toPlainString());
        assertEquals("4.15206", MSub.get(0,1).toPlainString());
        assertEquals("8.55241", MSub.get(0,2).toPlainString());
        assertEquals("3.25911", MSub.get(1,0).toPlainString());
        assertEquals("3.53914", MSub.get(1,1).toPlainString());
        assertEquals("4.70482", MSub.get(1,2).toPlainString());
        assertEquals("3.59236", MSub.get(2,0).toPlainString());
        assertEquals("9400000", MSub.get(2,1).toPlainString());
        assertEquals("9399990", MSub.get(2,2).toPlainString());
    }
    
    @Test
    public void mult__2x2_Matrizen_multiplizieren()
    {
        MathLib.setPrecision(6);
        
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
        
        assertEquals("28.2613", MMul.get(0,0).toPlainString());
        assertEquals("33.9274", MMul.get(0,1).toPlainString());
        assertEquals("53.3804", MMul.get(1,0).toPlainString());
        assertEquals("65.0095", MMul.get(1,1).toPlainString());
    }
    
    @Test
    public void mult__2x2_Matrizen_multiplizieren_extreme_faelle()
    {
        MathLib.setPrecision(6);
        
        M1 = new Matrix(2,2);
        M2 = new Matrix(2,2);

        M1.set(0, 0, new BigDecimal("9400000"));
        M1.set(0, 1, new BigDecimal("1"));
        M1.set(1, 0, new BigDecimal("1"));
        M1.set(1, 1, new BigDecimal("1"));
        
        M2.set(0, 0, new BigDecimal("0.0000003"));
        M2.set(0, 1, new BigDecimal("1"));
        M2.set(1, 0, new BigDecimal("1"));
        M2.set(1, 1, new BigDecimal("1"));
        
        MMul = M1.mult(M2);
        
        assertEquals("3.82", MMul.get(0,0).toPlainString());
        assertEquals("9400000", MMul.get(0,1).toPlainString());
        assertEquals("1", MMul.get(1,0).toPlainString());
        assertEquals("2", MMul.get(1,1).toPlainString());
    }
    
    @Test
    public void mult__Multiplizere_eine_1x4_Matrix_mit_einer_4x1_Matrix_mit_Prezision_6()
    {
        MathLib.setPrecision(6);
        
        M1 = new Matrix(1, 4);
        M2 = new Matrix(4, 1);

        M1.set(0, 0, new BigDecimal("0.356887"));
        M1.set(0, 1, new BigDecimal("-0.540738"));
        M1.set(0, 2, new BigDecimal("0.47585"));
        M1.set(0, 3, new BigDecimal("-0.594812"));
        
        M2.set(0, 0, new BigDecimal("0.1"));
        M2.set(1, 0, new BigDecimal("0"));
        M2.set(2, 0, new BigDecimal("0.1"));
        M2.set(3, 0, new BigDecimal("-0.375"));
        
        MMul = M1.mult(M2);
        
        assertEquals("0.306329",  MMul.get(0, 0).toPlainString());
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
    
    @Test
    public void getL__Test_To_Get_Lower_Triangular_Matrix_Including_Pivotstrategy_From_A_Matrix()
    {
        MathLib.setPivotStrategy(true);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("2.1"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        M2 = M1.getL();
        
        assertEquals("1", M2.get(0, 0).toPlainString());
        assertEquals("0", M2.get(0, 1).toPlainString());
        assertEquals("0", M2.get(0, 2).toPlainString());
        
        assertEquals("-0.61905", M2.get(1, 0).toPlainString());
        assertEquals("1", M2.get(1, 1).toPlainString());
        assertEquals("0", M2.get(1, 2).toPlainString());
        
        assertEquals("0.42857", M2.get(2, 0).toPlainString());
        assertEquals("-0.69237", M2.get(2, 1).toPlainString());
        assertEquals("1", M2.get(2, 2).toPlainString());
    }
    
    @Test
    public void getU__Test_To_Get_Upper_Triangular_Matrix_Including_Pivotstrategy_From_A_Matrix()
    {
        MathLib.setPivotStrategy(true);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("2.1"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        M2 = M1.getU();
        
        assertEquals("2.1", M2.get(0, 0).toPlainString());
        assertEquals("2512.0", M2.get(0, 1).toPlainString());
        assertEquals("-2516.0", M2.get(0, 2).toPlainString());
        
        assertEquals("0", M2.get(1, 0).toPlainString());
        assertEquals("1563.9", M2.get(1, 1).toPlainString());
        assertEquals("-1565.1", M2.get(1, 2).toPlainString());
        
        assertEquals("0", M2.get(2, 0).toPlainString());
        assertEquals("0", M2.get(2, 1).toPlainString());
        assertEquals("-0.7", M2.get(2, 2).toPlainString());
    }
    
    @Test
    public void getlperm__Test_To_Get_Permutationmatrix_Including_Pivotstrategy_From_A_Matrix()
    {
        MathLib.setPivotStrategy(true);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("0.8"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        V2 = M1.getLPerm();
        
        assertEquals("2", V2.get(0).toPlainString());
        assertEquals("1", V2.get(1).toPlainString());
        assertEquals("3", V2.get(2).toPlainString());
    }
    
    @Test
    public void getL__Test_To_Get_Lower_Triangular_Matrix_Including_Pivotstrategy_From_A_Matrix_And_Corresponding_Vector()
    {
        MathLib.setPivotStrategy(true);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("2.1"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        V1 = new Vector(3);
        
        V1.set(0, new BigDecimal("6.5"));
        V1.set(1, new BigDecimal("-5.3"));
        V1.set(2, new BigDecimal("2.9"));
        
        M2 = M1.getL(V1);
        
        assertEquals("1", M2.get(0, 0).toPlainString());
        assertEquals("0", M2.get(0, 1).toPlainString());
        assertEquals("0", M2.get(0, 2).toPlainString());
        
        assertEquals("-0.61905", M2.get(1, 0).toPlainString());
        assertEquals("1", M2.get(1, 1).toPlainString());
        assertEquals("0", M2.get(1, 2).toPlainString());
        
        assertEquals("0.42857", M2.get(2, 0).toPlainString());
        assertEquals("-0.69237", M2.get(2, 1).toPlainString());
        assertEquals("1", M2.get(2, 2).toPlainString());
        
        assertEquals("6.5", V1.get(0).toPlainString());
        assertEquals("-5.3", V1.get(1).toPlainString());
        assertEquals("2.9", V1.get(2).toPlainString());
    }
    
    @Test
    public void getU__Test_To_Get_Upper_Triangular_Matrix_Including_Pivotstrategy_From_A_Matrix_And_Corresponding_Vector()
    {
        MathLib.setPivotStrategy(true);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("2.1"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        V1 = new Vector(3);
        
        V1.set(0, new BigDecimal("6.5"));
        V1.set(1, new BigDecimal("-5.3"));
        V1.set(2, new BigDecimal("2.9"));
        
        M2 = M1.getU(V1);
        
        assertEquals("2.1", M2.get(0, 0).toPlainString());
        assertEquals("2512.0", M2.get(0, 1).toPlainString());
        assertEquals("-2516.0", M2.get(0, 2).toPlainString());
        
        assertEquals("0", M2.get(1, 0).toPlainString());
        assertEquals("1563.9", M2.get(1, 1).toPlainString());
        assertEquals("-1565.1", M2.get(1, 2).toPlainString());
        
        assertEquals("0", M2.get(2, 0).toPlainString());
        assertEquals("0", M2.get(2, 1).toPlainString());
        assertEquals("-0.7", M2.get(2, 2).toPlainString());
        
        assertEquals("6.5", V1.get(0).toPlainString());
        assertEquals("-5.3", V1.get(1).toPlainString());
        assertEquals("2.9", V1.get(2).toPlainString());
    }
    
    @Test
    public void getlperm__Test_To_Get_Permutationmatrix_Including_Pivotstrategy_From_A_Matrix_And_Corresponding_Vector()
    {
        MathLib.setPivotStrategy(true);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("0.8"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        V1 = new Vector(3);
        
        V1.set(0, new BigDecimal("6.5"));
        V1.set(1, new BigDecimal("-5.3"));
        V1.set(2, new BigDecimal("2.9"));
        
        V2 = M1.getlperm(V1);
        
        assertEquals("2", V2.get(0).toPlainString());
        assertEquals("1", V2.get(1).toPlainString());
        assertEquals("3", V2.get(2).toPlainString());
        
        assertEquals("-5.3", V1.get(0).toPlainString());
        assertEquals("6.5", V1.get(1).toPlainString());
        assertEquals("2.9", V1.get(2).toPlainString());
    }
    
    @Test
    public void getlperm__Test_To_Get_Permutationmatrix_Excluding_Pivotstrategy_From_A_Matrix_And_Corresponding_Vector()
    {
        MathLib.setPivotStrategy(false);
        MathLib.setPrecision(5);
        
        M1 = new Matrix(3, 3);
        
        M1.set(0, 0, new BigDecimal("2.1"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
        
        V1 = M1.getLPerm();
        
        assertEquals("1", V1.get(0).toPlainString());
        assertEquals("2", V1.get(1).toPlainString());
        assertEquals("3", V1.get(2).toPlainString());
    }
    
  //toString-Test
    @Test
    public void toString_3x5_Matrix_Ausgabe_mit_toString()
    {
        MathLib.setPrecision(6);
        
        M1 = new Matrix(3,5);
       
        M1.set(0, 0, new BigDecimal("1.53578"));
        M1.set(0, 1, new BigDecimal("2.83493"));
        M1.set(0, 2, new BigDecimal("0"));
        M1.set(0, 3, new BigDecimal("0"));
        M1.set(0, 4, new BigDecimal("0"));
                
        M1.set(1, 0, new BigDecimal("3.86645"));
        M1.set(1, 1, new BigDecimal("4.64339"));
        M1.set(1, 2, new BigDecimal("9000000"));
        M1.set(1, 3, new BigDecimal("9"));
        M1.set(1, 4, new BigDecimal("9"));
        
        M1.set(2, 0, new BigDecimal("9400000"));
        M1.set(2, 1, new BigDecimal("9400000"));
        M1.set(2, 2, new BigDecimal("9400000"));
        M1.set(2, 3, new BigDecimal("5"));
        M1.set(2, 4, new BigDecimal("5"));
        
        S1=new String("[[1.53578,2.83493,0,0,0],[3.86645,4.64339,9000000,9,9],[9400000,9400000,9400000,5,5]]");
        assertEquals(S1,M1.toString());
    }
    
    @Test
    public void get_und_set__tests_fuer_String()
    {
        M1 = new Matrix(3,3);
        
        M1.set(0, 0, new BigDecimal("2.16578958"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6")); 
        
        assertEquals("2.16578958",  M1.get(0,0).toPlainString());
        assertEquals("2512.0",      M1.get(0,1).toPlainString());
        assertEquals("-2516.0",     M1.get(0,2).toPlainString());
        
        assertEquals("-1.3",        M1.get(1,0).toPlainString());
        assertEquals("8.8",         M1.get(1,1).toPlainString());
        assertEquals("-7.6",        M1.get(1,2).toPlainString());
        
        assertEquals("0.9",         M1.get(2,0).toPlainString());
        assertEquals("-6.2",        M1.get(2,1).toPlainString());
        assertEquals("4.6",         M1.get(2,2).toPlainString());

    }
    
    @Test
    public void aa()
    {
        
        M1 = new Matrix(3,3);
        
        M1.set(0, 0, new BigDecimal("2.16578958"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6"));
    
        System.out.println(M1.getScaleOf());
    }
    
    @Test
    public void get_und_set__test_getter_und_setter_Reihen_und_Spalten()
    {
        M1 = new Matrix(3,5);
        
        M1.set(0, 0, new BigDecimal("2.16578958"));
        M1.set(0, 1, new BigDecimal("2512.0"));
        M1.set(0, 2, new BigDecimal("-2516.0"));
        
        M1.set(1, 0, new BigDecimal("-1.3"));
        M1.set(1, 1, new BigDecimal("8.8"));
        M1.set(1, 2, new BigDecimal("-7.6"));
        
        M1.set(2, 0, new BigDecimal("0.9"));
        M1.set(2, 1, new BigDecimal("-6.2"));
        M1.set(2, 2, new BigDecimal("4.6")); 
        
        //Test getter transponierte Matrix
        assertEquals("[[2.166,-1.3,0.9],[2512,8.8,-6.2],[-2516,-7.6,4.6],[0,0,0],[0,0,0]]",M1.getTransposed().toString());
        
        assertEquals(Integer.parseInt("3"),M1.getRows());
        assertEquals(Integer.parseInt("5"),M1.getCols());
    }
    
    /* Setzen von allgemeinen Werten, die bei jedem Test verwendet werden */
    @Before
    public void setUp()
    {
        MathLib.setPrecision(4);
        MathLib.enableRound(true);
    }
    
    /*
     * Zur�cksetzen von Werten, um Ausgangszustand eines Tests zu erreichen -->
     * wichtig f�r weitere Tests
     */
    @After
    public void tearDown()
    {
        M1 = null;
        M2 = null;
        MAdd = null;
        MMul = null;
        
        V1 = null;
        V2 = null;
        
        MathLib.enableRound(false);
        MathLib.setPivotStrategy(false);
    }
    
}