package numerik.calc;

import java.math.BigDecimal;

/**
 * MatrixIterationMethods bietet Methoden an f�r die L�sung von Iterationsaufgaben f�r Matrizen
 */
public class MatrixIterationMethods
{
    /**
     * Berechnet die Matrix M aus folgender Formel
     * M = -B^-1 * (A-B)
     * @param matrixA Ist die Matrix A, die f�r die obere Formel eigesetzt wird und B ist die Diagonalmatrix von A
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
     * @param matrixA Ist die Matrix A, die f�r die obere Formel eigesetzt wird und B ist die Diagonalmatrix von A
     * @param vectorb Vektor b ist ein Vektor, der f�r eine lineare Gleichung gebraucht wird und steht immer f�r ein Ergebnis einer Gleichung (wenn eine bessere Erkl�rung gefunden werden kann, dann erg�nzen!)
     */
    public static Vector getc(Matrix matrixA, Vector vectorb)
    {
        Matrix matrixBinversed = matrixA.getDiagonalMatrix().getInverse();
        
        return matrixBinversed.mult(vectorb);
    }
    
    /**
     * Erm�glicht das Berechnen einer einer L�sung f�r lineare Gleichungen (n�herungsweise)
     * mittels der Formel x = Mx * c
     * Gibt als Endergebnis einen Array von x-Vectoren als L�sungen aus, wobei die erste Iteration
     * des Verfahrens Matrix[0] entspricht
     * @param matrixM N�tig f�r die Formel x = Mx * c
     * @param vectorc N�tig f�r die Formel x = Mx * c
     * @param startVectorX0 Startvector f�r die Iterationen
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
     * Erm�glicht das Absch�tzen eines Spektralradiuses
     * Die Ausgabe des Spektralradiuses(angen�hert) f�r die Iterationen ist im item1 des Tupels als Array gespeichert.
     * Die Ausgabe des y-Vectors f�r die Iteration ist im item2 des Tupels als Array gespeichert.
     * Im Array ist die erste Iteration dem Index 0 zugeordnet.
     * @param matrixM Eigenwert = (y[transponiert] * matrixM * y) / Euklidische Norm von Vektor y
     * @param startVectorY0 Startvector f�r die Iterationen
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
     * @param mue Ann�herungswert --> Erm�glicht das approximieren des Eigenpaares
     * @param startVectorY0 Startvector f�r die Iterationen
     * @param maxIterations Angabe der maximalen Anzahl der Iterationen
     */
    public static Tuple<BigDecimal, Vector[]> inverseIteration(Matrix matrixA, BigDecimal mue, Vector startVectorY0, int maxIterations) throws Exception
    {
        throw new Exception("Noch nicht implementiert!");
    }
}
