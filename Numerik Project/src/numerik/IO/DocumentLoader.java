package numerik.IO;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import numerik.calc.Matrix;
import numerik.calc.Vector;

public class DocumentLoader
{
    
    public Matrix[] readMatrices(String file)
    {
        ArrayList<Matrix> matrices = new ArrayList<Matrix>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("Matrix#"))
                {
                    Matrix matrix = readMatrix(br);
                    matrix.name = line.substring(7);
                    matrices.add(matrix);
                }
            }
            br.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return matrices.toArray(new Matrix[matrices.size()]);
    }
    
    
    private Matrix readMatrix(BufferedReader br) throws IOException
    {
        int cols = 0;
        String line;
        ArrayList<BigDecimal> entries = new ArrayList<BigDecimal>();
        while ((line = br.readLine()) != null && !line.equals(""))
        {
            String[] values = line.split(",");
            if (cols > 0 && cols != values.length)
            {
                System.err.println("Fehlerhafte Matrix!");
            }
            cols = values.length;
            
            for (String value : values)
            {
                entries.add(new BigDecimal(value));
            }
        }
        return new Matrix(entries, cols);
    }
    
    
    public String[] getAllMatrixNames(String file)
    {
        ArrayList<String> names = new ArrayList<String>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("Matrix#"))
                {
                    names.add(line.substring(7));
                }
            }
            br.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return names.toArray(new String[names.size()]);
    }
    
    
    public Vector[] readVectors(String file)
    {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("Vector#"))
                {
                    Vector vector = readVector(br);
                    vector.name = line.substring(7);
                    vectors.add(vector);
                }
            }
            br.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return vectors.toArray(new Vector[vectors.size()]);
    }
    
    
    private Vector readVector(BufferedReader br) throws IOException
    {
        String line;
        ArrayList<BigDecimal> entries = new ArrayList<BigDecimal>();
        if ((line = br.readLine()) != null && !line.equals(""))
        {
            String[] values = line.split(",");
            
            for (String value : values)
            {
                entries.add(new BigDecimal(value));
            }
        }
        return new Vector(entries.toArray(new BigDecimal[entries.size()]));
    }
    
    
    public String[] getAllVectorNames(String file)
    {
        ArrayList<String> names = new ArrayList<String>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("Vector#"))
                {
                    names.add(line.substring(7));
                }
            }
            br.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return names.toArray(new String[names.size()]);
    }
}