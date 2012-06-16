package numerik.ui.misc;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.io.DocumentLoader;

/**
 * Diese Klasse synchronisiert Änderungen an Matrizen und Vektoren
 * zwischen den Controls, etc.
 */
public final class MathDataSynchronizer
{
    private static MathDataSynchronizer instance;
    
    
    public enum MathDataType { MATRIX, VECTOR }
    
    private static final DocumentLoader docLoader = new DocumentLoader();
    
    private String[] matrixNames;
    private String[] vectorNames;
    
    private Matrix[] matrices;
    private Vector[] vectors;
    
    private ArrayList<Image> matrixImages;
    private ArrayList<Image> vectorImages;
    
    private ArrayList<ChangeListener> changeListeners;
    
    
    public static MathDataSynchronizer getInstance()
    {
        if (instance == null)
        {
            instance = new MathDataSynchronizer();
        }
        
        return instance;
    }
    
    
    private MathDataSynchronizer()
    {
        matrixImages = new ArrayList<>();
        vectorImages = new ArrayList<>();
        
        changeListeners = new ArrayList<>();
        
        refresh();
    }
    
    public void refresh()
    {
        matrixNames = docLoader.getAllMatrixNames("Data.txt");
        vectorNames = docLoader.getAllVectorNames("Data.txt");
        
        matrices = docLoader.readMatrices("Data.txt");
        vectors = docLoader.readVectors("Data.txt");
        
        matrixImages.clear();
        for (Matrix matrix : matrices)
        {
            matrixImages.add(new LatexFormula().addMatrix(matrix).toImage());
        }
        
        vectorImages.clear();
        for (Vector vector : vectors)
        {
            vectorImages.add(new LatexFormula().addVector(vector).toImage());
        }
        
        for (ChangeListener listener : changeListeners)
        {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
    
    public void add(Matrix matrix)
    {
        docLoader.addMatrixToFile(matrix, "Data.txt");
        
        refresh();
    }
    
    public void removeMatrix(String name)
    {
        docLoader.deleteMatrix(name, "Data.txt");
        
        refresh();
    }
    
    public void add(Vector vector)
    {
        docLoader.addVectorToFile(vector, "Data.txt");
        
        refresh();
    }
    
    public void removeVector(String name)
    {
        docLoader.deleteVector(name, "Data.txt");
        
        refresh();
    }
    
    
    public String[] getMatrixNames()
    {
        return matrixNames;
    }
    
    public String[] getVectorNames()
    {
        return vectorNames;
    }
    
    public Matrix[] getMatrices()
    {
        return matrices;
    }
    
    public Vector[] getVectors()
    {
        return vectors;
    }
    
    public ArrayList<Image> getMatrixImages()
    {
        return matrixImages;
    }
    
    public ArrayList<Image> getVectorImages()
    {
        return vectorImages;
    }
    
    public Matrix getMatrix(String name)
    {
        for (Matrix matrix : matrices)
        {
            if (matrix.name.equals(name))
            {
                return matrix;
            }
        }
        
        return null;
    }
    
    public Vector getVector(String name)
    {
        for (Vector vector : vectors)
        {
            if (vector.name.equals(name))
            {
                return vector;
            }
        }
        
        return null;
    }
    
    public void addChangeListeners(ChangeListener l)
    {
        changeListeners.add(l);
    }
}