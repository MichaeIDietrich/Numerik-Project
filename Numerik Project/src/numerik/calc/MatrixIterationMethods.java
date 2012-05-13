package numerik.calc;

import java.math.BigDecimal;

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
        
        return matrixBinversed.mult(new BigDecimal("-1")).mult(matrixB);
    }
    
    /**
     * Berechnet die Vektor M aus folgender Formel
     * c = B^-1 * b
     * @param matrixA Ist die Matrix A, die für die obere Formel eigesetzt wird und B ist die Diagonalmatrix von A
     * @param vectorb Vektor b ist ein Vektor, der für eine lineare Gleichung gebraucht wird und steht immer für ein Ergebnis einer Gleichung (wenn eine bessere Erklärung gefunden werden kann, dann ergänzen!)
     */
    public static Vector getc(Matrix matrixA, Vector vectorb)
    {
        Matrix matrixBinversed = matrixA.getDiagonalMatrix().getInverse();
        
        return matrixBinversed.mult(vectorb);
    }
    
    /**
     * Ermöglicht das Berechnen einer einer Lösung für lineare Gleichungen (näherungsweise)
     * mittels der Formel x = Mx * c
     * Gibt als Endergebnis einen Array von x-Vectoren als Lösungen aus, wobei die erste Iteration
     * des Verfahrens Matrix[0] entspricht
     * @param matrixM Nötig für die Formel x = Mx * c
     * @param vectorc Nötig für die Formel x = Mx * c
     * @param startVectorX0 Startvector für die Iterationen
     * @param maxIterations Angabe der maximalen Anzahl der Iterationen
     */
    public static Vector[] jacobiIteration(Matrix matrixM, Vector vectorc, Vector startVectorX0, int maxIterations)
    {
        Vector[] resultVectorArray = new Vector[maxIterations];
        
        Vector startVector = startVectorX0;
        
        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            Vector multipliedVector = matrixM.mult(startVector);
            startVector = multipliedVector.add(vectorc);
            
            resultVectorArray[iteration] = startVector;
        }
        
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
        Vector startVector = startVectorY0;
        
        BigDecimal[] lam = new BigDecimal[maxIterations];
        Vector[] yVectors = new Vector[maxIterations];
        
        MathLib.setNorm(1);
        
        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            startVector = matrixM.mult(startVector);
            
            BigDecimal euclidicNorm = startVector.norm();
            
            startVector = startVector.divide(euclidicNorm);
            
            yVectors[iteration] = startVector;
            
            lam[iteration] = startVector.toMatrix().getTransposed().mult(matrixM).mult(startVector).get(0);
        }
        
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
        throw new Exception("Noch nicht implementiert!");
    }
}
