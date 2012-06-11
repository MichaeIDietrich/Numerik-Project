package numerik.io;

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
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return names.toArray(new String[names.size()]);
    }
    
    
    public Matrix readMatrix(String file, String name) 
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.equals("Matrix#" + name))
                {
                    Matrix matrix = readMatrix(br);
                    matrix.name = line.substring(7);
                    br.close();
                    
                    return matrix;
                }
            }
            br.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        throw new RuntimeException("Matrix '" + name + "' nicht gefunden in Datei '" + file + "'");
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
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return names.toArray(new String[names.size()]);
    }
    
    
    public void addMatrixToFile(Matrix matrix, String file)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write("\n\n");
            bw.write("Matrix#");
            bw.write(matrix.name);
            
            for (int row = 0; row < matrix.getRows(); row++)
            {
                bw.write("\n");
                for (int col = 0; col < matrix.getCols(); col++)
                {
                    bw.write(matrix.get(row, col).toPlainString());
                    if (col < matrix.getCols() - 1)
                    {
                        bw.write(",");
                    }
                }
            }
            bw.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void addVectorToFile(Vector vector, String file)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write("\n\n");
            bw.write("Vector#");
            bw.write(vector.name);
            bw.write("\n");
            
            for (int i = 0; i < vector.getLength(); i++)
            {
                bw.write(vector.get(i).toPlainString());
                if (i < vector.getLength() - 1)
                {
                    bw.write(",");
                }
            }
            bw.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}