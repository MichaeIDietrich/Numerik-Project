package numerik.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
            
            lam[iteration] = MathLib.round(startVector.toMatrix().getTransposed().mult(matrixM).mult(startVector).get(0));
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
        Vector[] resultOfIteration = new Vector[maxIterations];
        
        Vector y = startVectorY0.clone();
        
        //set pivotstrategy on or off here
        MathLib.setPivotStrategy(true);
        
        Matrix matrixForLU = matrixA.subtract(matrixA.identity().mult(mue));
        
        Matrix L = matrixForLU.getL();
        Matrix U = matrixForLU.getU();
        Vector lperm = matrixForLU.getlperm();
        
        for (int i = 0; i < maxIterations; i++)
        {
            Vector forwardSubstituted = forwardSubstitution(L, y, lperm);
            
            //Substitutionsmethode ist nicht static!! muss noch verbessert werden
            y = U.substitution(U, forwardSubstituted, "backward");
            
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
