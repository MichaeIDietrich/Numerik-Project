package numerik.calc_test;

import numerik.calc.*;
import java.math.BigDecimal;

import org.junit.*;

import static org.junit.Assert.*;
import static numerik.calc.MatrixIterationMethods.*;

public class MatrixIterationMethods_TestFixture
{
    private Tuple<BigDecimal[], Vector[]> tupleOutput;
    private Vector[] vectorArrayOutput;
    private Matrix matrixOutput;
    private Vector vectorOutput;
    
    private Matrix matrixInput;
    private Vector vectorInput;
    
    @Test
    public void getM__get_the_matrix_M()
    {
        matrixInput = new Matrix(4, 4);
        
        matrixInput.set(0, 0, new BigDecimal("10"));
        matrixInput.set(0, 1, new BigDecimal("-1"));
        matrixInput.set(0, 2, new BigDecimal("2"));
        matrixInput.set(0, 3, new BigDecimal("0"));
        
        matrixInput.set(1, 0, new BigDecimal("-1"));
        matrixInput.set(1, 1, new BigDecimal("11"));
        matrixInput.set(1, 2, new BigDecimal("-1"));
        matrixInput.set(1, 3, new BigDecimal("3"));
        
        matrixInput.set(2, 0, new BigDecimal("2"));
        matrixInput.set(2, 1, new BigDecimal("-1"));
        matrixInput.set(2, 2, new BigDecimal("10"));
        matrixInput.set(2, 3, new BigDecimal("-1"));
        
        matrixInput.set(3, 0, new BigDecimal("0"));
        matrixInput.set(3, 1, new BigDecimal("3"));
        matrixInput.set(3, 2, new BigDecimal("-1"));
        matrixInput.set(3, 3, new BigDecimal("8"));
        
        matrixOutput = getM(matrixInput);
        
        assertEquals("0", matrixOutput.get(0, 0).toPlainString());
        assertEquals("0.1", matrixOutput.get(0, 1).toPlainString());
        assertEquals("-0.2", matrixOutput.get(0, 2).toPlainString());
        assertEquals("0", matrixOutput.get(0, 3).toPlainString());
        
        assertEquals("0.0909091", matrixOutput.get(1, 0).toPlainString());
        assertEquals("0", matrixOutput.get(1, 1).toPlainString());
        assertEquals("0.0909091", matrixOutput.get(1, 2).toPlainString());
        assertEquals("-0.272727", matrixOutput.get(1, 3).toPlainString());
        
        assertEquals("-0.2", matrixOutput.get(2, 0).toPlainString());
        assertEquals("0.1", matrixOutput.get(2, 1).toPlainString());
        assertEquals("0", matrixOutput.get(2, 2).toPlainString());
        assertEquals("0.1", matrixOutput.get(2, 3).toPlainString());
        
        assertEquals("0", matrixOutput.get(3, 0).toPlainString());
        assertEquals("-0.375", matrixOutput.get(3, 1).toPlainString());
        assertEquals("0.125", matrixOutput.get(3, 2).toPlainString());
        assertEquals("0", matrixOutput.get(3, 3).toPlainString());
    }
    
    @Test
    public void getc__get_the_Vector_c()
    {
        matrixInput = new Matrix(4, 4);
        
        matrixInput.set(0, 0, new BigDecimal("10"));
        matrixInput.set(0, 1, new BigDecimal("-1"));
        matrixInput.set(0, 2, new BigDecimal("2"));
        matrixInput.set(0, 3, new BigDecimal("0"));
        
        matrixInput.set(1, 0, new BigDecimal("-1"));
        matrixInput.set(1, 1, new BigDecimal("11"));
        matrixInput.set(1, 2, new BigDecimal("-1"));
        matrixInput.set(1, 3, new BigDecimal("3"));
        
        matrixInput.set(2, 0, new BigDecimal("2"));
        matrixInput.set(2, 1, new BigDecimal("-1"));
        matrixInput.set(2, 2, new BigDecimal("10"));
        matrixInput.set(2, 3, new BigDecimal("-1"));
        
        matrixInput.set(3, 0, new BigDecimal("0"));
        matrixInput.set(3, 1, new BigDecimal("3"));
        matrixInput.set(3, 2, new BigDecimal("-1"));
        matrixInput.set(3, 3, new BigDecimal("8"));
        
        vectorInput = new Vector(4);
        
        vectorInput.set(0, new BigDecimal("6"));
        vectorInput.set(1, new BigDecimal("25"));
        vectorInput.set(2, new BigDecimal("-11"));
        vectorInput.set(3, new BigDecimal("15"));
        
        vectorOutput = getc(matrixInput, vectorInput);
        
        assertEquals("0.6", vectorOutput.get(0).toPlainString());
        assertEquals("2.27273", vectorOutput.get(1).toPlainString());
        assertEquals("-1.1", vectorOutput.get(2).toPlainString());
        assertEquals("1.875", vectorOutput.get(3).toPlainString());
    }
    
    @Test
    public void jacobiIteration__OutputVectorArray_Should_Contain_Correct_Iterations()
    {
        matrixInput = new Matrix(4, 4);
        
        matrixInput.set(0, 0, new BigDecimal("0"));
        matrixInput.set(0, 1, new BigDecimal("0.1"));
        matrixInput.set(0, 2, new BigDecimal("-0.2"));
        matrixInput.set(0, 3, new BigDecimal("0"));
        
        matrixInput.set(1, 0, new BigDecimal("0.0909091"));
        matrixInput.set(1, 1, new BigDecimal("0"));
        matrixInput.set(1, 2, new BigDecimal("0.0909091"));
        matrixInput.set(1, 3, new BigDecimal("-0.272727"));
        
        matrixInput.set(2, 0, new BigDecimal("-0.2"));
        matrixInput.set(2, 1, new BigDecimal("0.1"));
        matrixInput.set(2, 2, new BigDecimal("0"));
        matrixInput.set(2, 3, new BigDecimal("0.1"));
        
        matrixInput.set(3, 0, new BigDecimal("0"));
        matrixInput.set(3, 1, new BigDecimal("-0.375"));
        matrixInput.set(3, 2, new BigDecimal("0.125"));
        matrixInput.set(3, 3, new BigDecimal("0"));
        
        vectorInput = new Vector(4);
        
        vectorInput.set(0, new BigDecimal("0.6"));
        vectorInput.set(1, new BigDecimal("2.27273"));
        vectorInput.set(2, new BigDecimal("-1.1"));
        vectorInput.set(3, new BigDecimal("1.875"));
        
        Vector startVectorX0 = new Vector(4);
        
        startVectorX0.set(0, new BigDecimal("0"));
        startVectorX0.set(1, new BigDecimal("0"));
        startVectorX0.set(2, new BigDecimal("0"));
        startVectorX0.set(3, new BigDecimal("0"));
        
        vectorArrayOutput = jacobiIteration(matrixInput, vectorInput, startVectorX0, 6);
        
        assertEquals("0.6", vectorArrayOutput[0].get(0).toPlainString());
        assertEquals("2.27273", vectorArrayOutput[0].get(1).toPlainString());
        assertEquals("-1.1", vectorArrayOutput[0].get(2).toPlainString());
        assertEquals("1.875", vectorArrayOutput[0].get(3).toPlainString());
        
        assertEquals("0.932636", vectorArrayOutput[2].get(0).toPlainString());
        assertEquals("2.05331", vectorArrayOutput[2].get(1).toPlainString());
        assertEquals("-1.04934", vectorArrayOutput[2].get(2).toPlainString());
        assertEquals("1.13088", vectorArrayOutput[2].get(3).toPlainString());
        
        assertEquals("1.0032", vectorArrayOutput[5].get(0).toPlainString());
        assertEquals("1.99224", vectorArrayOutput[5].get(1).toPlainString());
        
        //hier kommt ein rundungsfehler zustande --> scheinbar ist bei irgendeiner rundungsoperation was schief gegangen
        assertEquals("-0.994521", vectorArrayOutput[5].get(2).toPlainString());
        assertEquals("0.994431", vectorArrayOutput[5].get(3).toPlainString());
    }
    
    @Test
    public void vectorIteration__OutputTuple_Should_Contain_Correct_Iterations()
    {
        matrixInput = new Matrix(4, 4);
        
        matrixInput.set(0, 0, new BigDecimal("0"));
        matrixInput.set(0, 1, new BigDecimal("0.1"));
        matrixInput.set(0, 2, new BigDecimal("-0.2"));
        matrixInput.set(0, 3, new BigDecimal("0"));
        
        matrixInput.set(1, 0, new BigDecimal("0.0909091"));
        matrixInput.set(1, 1, new BigDecimal("0"));
        matrixInput.set(1, 2, new BigDecimal("0.0909091"));
        matrixInput.set(1, 3, new BigDecimal("-0.272727"));
        
        matrixInput.set(2, 0, new BigDecimal("-0.2"));
        matrixInput.set(2, 1, new BigDecimal("0.1"));
        matrixInput.set(2, 2, new BigDecimal("0"));
        matrixInput.set(2, 3, new BigDecimal("0.1"));
        
        matrixInput.set(3, 0, new BigDecimal("0"));
        matrixInput.set(3, 1, new BigDecimal("-0.375"));
        matrixInput.set(3, 2, new BigDecimal("0.125"));
        matrixInput.set(3, 3, new BigDecimal("0"));
        
        vectorInput = new Vector(4);
        
        vectorInput.set(0, new BigDecimal("-1"));
        vectorInput.set(1, new BigDecimal("1"));
        vectorInput.set(2, new BigDecimal("-1"));
        vectorInput.set(3, new BigDecimal("1"));
        
        tupleOutput = vectorIteration(matrixInput, vectorInput, 20);
        
        assertEquals("-0.425913", tupleOutput.item1[0].toPlainString());
        assertEquals("0.356887", tupleOutput.item2[0].get(0).toPlainString());
        assertEquals("-0.540738", tupleOutput.item2[0].get(1).toPlainString());
        assertEquals("0.47585", tupleOutput.item2[0].get(2).toPlainString());
        assertEquals("-0.594812", tupleOutput.item2[0].get(3).toPlainString());
        
        assertEquals("-0.426376", tupleOutput.item1[9].toPlainString());
        assertEquals("-0.335259", tupleOutput.item2[9].get(0).toPlainString());
        assertEquals("0.560319", tupleOutput.item2[9].get(1).toPlainString());
        assertEquals("-0.433956", tupleOutput.item2[9].get(2).toPlainString());
        assertEquals("0.620744", tupleOutput.item2[9].get(3).toPlainString());
        
        assertEquals("-0.42643", tupleOutput.item1[19].toPlainString());
        assertEquals("-0.335115", tupleOutput.item2[19].get(0).toPlainString());
        assertEquals("0.56068", tupleOutput.item2[19].get(1).toPlainString());
        assertEquals("-0.434105", tupleOutput.item2[19].get(2).toPlainString());
        assertEquals("0.620394", tupleOutput.item2[19].get(3).toPlainString());
    }
    
    @Test
    public void inverseIteration__OutputTuple_Should_Contain_Correct_Iterations() throws Exception
    {
        Tuple<BigDecimal, Vector[]> inverseIterationResult;
        
        matrixInput = new Matrix(4, 4);
        
        matrixInput.set(0, 0, new BigDecimal("10"));
        matrixInput.set(0, 1, new BigDecimal("-1"));
        matrixInput.set(0, 2, new BigDecimal("2"));
        matrixInput.set(0, 3, new BigDecimal("0"));
        
        matrixInput.set(1, 0, new BigDecimal("-1"));
        matrixInput.set(1, 1, new BigDecimal("11"));
        matrixInput.set(1, 2, new BigDecimal("-1"));
        matrixInput.set(1, 3, new BigDecimal("3"));
        
        matrixInput.set(2, 0, new BigDecimal("2"));
        matrixInput.set(2, 1, new BigDecimal("-1"));
        matrixInput.set(2, 2, new BigDecimal("10"));
        matrixInput.set(2, 3, new BigDecimal("-1"));
        
        matrixInput.set(3, 0, new BigDecimal("0"));
        matrixInput.set(3, 1, new BigDecimal("3"));
        matrixInput.set(3, 2, new BigDecimal("-1"));
        matrixInput.set(3, 3, new BigDecimal("8"));
        
        vectorInput = new Vector(4);
        
        vectorInput.set(0, new BigDecimal("1"));
        vectorInput.set(1, new BigDecimal("1"));
        vectorInput.set(2, new BigDecimal("-1"));
        vectorInput.set(3, new BigDecimal("-1"));
        
        BigDecimal mue = new BigDecimal("5");
        
        inverseIterationResult = inverseIteration(matrixInput, mue, vectorInput, 4);
        
        assertEquals("5.96408", inverseIterationResult.item1.toPlainString());
        
        assertEquals("0.324957", inverseIterationResult.item2[0].get(0).toPlainString());
        assertEquals("0.477065", inverseIterationResult.item2[0].get(1).toPlainString());
        assertEquals("-0.297302", inverseIterationResult.item2[0].get(2).toPlainString());
        assertEquals("-0.760539", inverseIterationResult.item2[0].get(3).toPlainString());
        
        assertEquals("0.249532", inverseIterationResult.item2[1].get(0).toPlainString());
        assertEquals("0.485983", inverseIterationResult.item2[1].get(1).toPlainString());
        assertEquals("-0.22219", inverseIterationResult.item2[1].get(2).toPlainString());
        assertEquals("-0.807578", inverseIterationResult.item2[1].get(3).toPlainString());
        
        assertEquals("0.225818", inverseIterationResult.item2[2].get(0).toPlainString());
        assertEquals("0.491438", inverseIterationResult.item2[2].get(1).toPlainString());
        assertEquals("-0.198408", inverseIterationResult.item2[2].get(2).toPlainString());
        assertEquals("-0.817388", inverseIterationResult.item2[2].get(3).toPlainString());
        
        assertEquals("0.218641", inverseIterationResult.item2[3].get(0).toPlainString());
        assertEquals("0.493511", inverseIterationResult.item2[3].get(1).toPlainString());
        assertEquals("-0.190985", inverseIterationResult.item2[3].get(2).toPlainString());
        assertEquals("-0.819863", inverseIterationResult.item2[3].get(3).toPlainString());
        

    }
    
    /* Setzen von allgemeinen Werten, die bei jedem Test verwendet werden */
    @Before
    public void setUp()
    {
        MathLib.setPrecision(6);
        MathLib.enableRound(true);
        MathLib.setRoundingMode(MathLib.EXACT);
    }
    
    /*
     * Zur�cksetzen von Werten, um Ausgangszustand eines Tests zu erreichen -->
     * wichtig f�r weitere Tests
     */
    @After
    public void tearDown()
    {
        tupleOutput = null;
        vectorArrayOutput = null;
        matrixOutput = null;
        vectorOutput = null;
        
        MathLib.enableRound(false);
    }
}