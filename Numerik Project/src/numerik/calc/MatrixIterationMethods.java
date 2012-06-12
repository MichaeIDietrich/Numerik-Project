package numerik.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

import numerik.calc.Matrix.SubstitutionDirection;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;

/**
 * MatrixIterationMethods bietet Methoden an für die Lösung von Iterationsaufgaben für Matrizen
 */
public class MatrixIterationMethods
{
    /**
     * Berechnet die Matrix M aus folgender Formel
     * M = -B^-1 * (A-B)
     * @param matrixA Ist die Matrix A, die für die obere Formel eigesetzt wird und B ist die Diagonalmatrix von A
     */
    public static Matrix getM(Matrix matrixA)
    {
        Matrix matrixB = matrixA.getDiagonalMatrix();
        Matrix matrixBinversed = matrixB.getInverse();
        
        matrixB = matrixA.subtract(matrixB);
        
        return matrixBinversed.mult(new BigDecimal(-1)).mult(matrixB);
    }
    
    /**
     * Berechnet die Vektor M aus folgender Formel
     * c = B^-1 * b
     * @param matrixA Ist die Matrix A, die für die obere Formel eigesetzt wird und B ist die Diagonalmatrix von A
     * @param vectorb Vektor b ist ein Vektor, der für eine lineare Gleichung gebraucht wird und steht immer für ein
     *        Ergebnis einer Gleichung (wenn eine bessere Erklärung gefunden werden kann, dann ergänzen!)
     */
    public static Vector getc(Matrix matrixA, Vector vectorb)
    {
        Matrix matrixBinversed = matrixA.getDiagonalMatrix().getInverse();
        
        return matrixBinversed.mult(vectorb);
    }
    
    /**
     * Ermöglicht das Berechnen einer einer Lösung für lineare Gleichungen (näherungsweise)
     * mittels der Formel x = Mx* + c
     * Gibt als Endergebnis einen Array von x-Vektoren als Lösungen aus, wobei die erste Iteration
     * des Verfahrens Matrix[0] entspricht
     * @param matrixM Nötig für die Formel x = Mx* + c
     * @param vectorC Nötig für die Formel x = Mx* + c
     * @param startVectorX0 Startvector für die Iterationen
     * @param maxIterations Angabe der maximalen Anzahl der Iterationen
     */
    public static Vector[] jacobiIteration(Matrix matrixM, Vector vectorC, Vector startVectorX0, int maxIterations)
    {
        LatexFormula formula = new LatexFormula();
        
        Vector[] resultVectorArray = new Vector[maxIterations];
        
        Vector startVector = startVectorX0;
        
        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            Vector multipliedVector = matrixM.mult(startVector);
            startVector = multipliedVector.add(vectorC);
            
            resultVectorArray[iteration] = startVector;
            
            formula.addLatexString("x_{"+(iteration+1)+"} = ");
            formula.addVector(startVector).addNewLine();
        }
        
        Recorder.getInstance().add(formula);
        
        return resultVectorArray;
    }
    
    /**
     * Ermöglicht das Abschätzen eines Spektralradiuses
     * Die Ausgabe des Spektralradiuses(angenähert) für die Iterationen ist im item1 des Tupels als Array gespeichert.
     * Die Ausgabe des y-Vectors für die Iteration ist im item2 des Tupels als Array gespeichert.
     * Im Array ist die erste Iteration dem Index 0 zugeordnet.
     * @param matrixM Eigenwert = (y[transponiert] * matrixM * y) / Euklidische Norm von Vektor y
     * @param startVectorY0 Startvector für die Iterationen
     * @param maxIterations Angabe der maximalen Anzahl der Iterationen
     */
    public static Tuple<BigDecimal[], Vector[]> vectorIteration(Matrix matrixM, Vector startVectorY0, int maxIterations)
    {
        LatexFormula formula = new LatexFormula();
        
        Vector startVector = startVectorY0;
        
        BigDecimal[]  lam = new BigDecimal[maxIterations];
        Vector[] yVectors = new Vector[maxIterations];
        
        MathLib.setNorm(1);
        
        formula.addLatexString("\\begin{tabular}{c|c|c}");
        formula.addLatexString("$\\bf{n}$ & $\\bf{\\lambda}$ & $\\bf{y-Vectors}$ \\\\ \\hline");
        
        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            startVector = matrixM.mult(startVector);
            
            BigDecimal euclidicNorm = startVector.norm();
            
            startVector = startVector.divide(euclidicNorm);
            
            yVectors[iteration] = startVector;
            
            lam[iteration] = MathLib.round(startVector.toMatrix().getTransposed().mult(matrixM).mult(startVector).get(0));
            
            formula.addLatexString((iteration+1 + " & " + lam[iteration] + " & ")).addVector(yVectors[iteration]).addLatexString("\\\\");
        }
        
        formula.addLatexString("\\end{tabular}").addNewLine(3);
        formula.addText("Der größte Eigenwert ist \\lambda = " + lam[maxIterations-1]);
        Recorder.getInstance().add(formula);
        
        return new Tuple<BigDecimal[], Vector[]>(lam, yVectors);
    }
    
    /**
     * Ziel: ein beliebiges Eigenpaar (Eigenwert, Eigenvektor) zu approximieren
     * item1 des Tupels gibt den Eigenwert einer Iteration aus
     * item2 des Tupels gibt den Eigenvektor einer Iteration aus
     * Index 0 entspricht wieder der ersten Iteration
     * @param matrixA (A-mue * IdentityMatrix) * y^(k+1) = y^(k)
     * @param mue Annäherungswert --> Ermöglicht das approximieren des Eigenpaares
     * @param startVectorY0 Startvector für die Iterationen
     * @param maxIterations Angabe der maximalen Anzahl der Iterationen
     */
    public static Tuple<BigDecimal, Vector[]> inverseIteration(Matrix matrixA, BigDecimal mue, Vector startVectorY0, int maxIterations) throws Exception
    {
        Vector[] resultOfIteration = new Vector[maxIterations];
        
        Vector y = startVectorY0.clone();
        
        //set pivotstrategy on or off here
        MathLib.setPivotStrategy(true);
        
        Matrix matrixForLU = matrixA.subtract(matrixA.identity().mult(mue));
        
        Matrix L = matrixForLU.getL();
        Matrix U = matrixForLU.getU();
        Vector lperm = matrixForLU.getLPerm();
        
        for (int i = 0; i < maxIterations; i++)
        {
            Vector forwardSubstituted = forwardSubstitution(L, y, lperm);
            
            // Substitutionsmethode ist nicht static!! Muss noch verbessert werden.
            y = U.substitution(U, forwardSubstituted, SubstitutionDirection.BACKWARD);
            
            MathLib.setNorm(1);
            
            y = y.divide(y.norm());
            
            resultOfIteration[i] = y.clone();
        }
        
        BigDecimal resultingEigenvalue = y.toMatrix().getTransposed().mult(matrixA).mult(y.toMatrix()).get(0, 0);
        resultingEigenvalue = MathLib.round(resultingEigenvalue.divide(y.toMatrix().getTransposed().mult(y.toMatrix()).get(0, 0), MathLib.getPrecision(), MathLib.getRoundingMode()));
        
        return new Tuple<BigDecimal, Vector[]>(resultingEigenvalue, resultOfIteration);
    }
    
    /**
     * Ermöglicht die Vorwärts von einer Matrix mit einem Vector b in
     * Abhängigkeit von einem Startvektor y0 
     * Speziell angepasst für die inverse Iteration!!!
     * @param matrix Matrix, die man Substituieren will
     * @param b Vektor, den man Substituieren will
     * @param y0 Vektor, der für die bsp. Inversen Iteration benutzt wird (bei null wird dies optional)
     */
    private static Vector forwardSubstitution(Matrix matrix, Vector b, Vector y0)
    {
        BigDecimal term0 = BigDecimal.ZERO;
        BigDecimal term1 = BigDecimal.ZERO;
        BigDecimal term2 = BigDecimal.ZERO;
        Vector         y = new Vector(b.getLength());
        Matrix     mtemp = matrix.clone();
        Vector     btemp = b.clone();
        
        if (y0 != null)
        {
            for (int i = 0; i < matrix.getRows(); i++)
            {
                BigDecimal bigDecimalToDivide = y0.get(i);
                
                btemp.set(i, MathLib.round(btemp.get(i).divide(MathLib.round(bigDecimalToDivide), MathLib.getPrecision(), MathLib.getRoundingMode())));
                
                for (int j = 0; j < matrix.getCols(); j++)
                {
                    mtemp.set(i, j, MathLib.round(mtemp.get(i, j).divide(MathLib.round(bigDecimalToDivide), MathLib.getPrecision(), MathLib.getRoundingMode())));
                }
            }
        }
        
        y.set(0, btemp.get(0));
        
        for(int row=1; row<mtemp.getRows(); row++)
        {
            term0 = BigDecimal.ZERO;
            
            for(int i=0; i < y.getLength() - 1; i++)
            {
                term0 = MathLib.round( mtemp.values[row][i].multiply( y.get(i) )).add( term0 );
            }
            term1 = MathLib.round( btemp.get(row).subtract( term0 ));
            term2 = MathLib.round( term1.divide( mtemp.values[row][row], MathLib.getInversePrecision(), RoundingMode.HALF_UP ));
            
            y.set( row, term2);
        }
        
        return y;
    }
}
