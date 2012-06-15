package numerik;

import java.io.*;

public final class Configuration implements Serializable
{
    private static final long serialVersionUID = 262243528513865929L;
    
    
    private static Configuration activeConfiguration = null;
    
    private int fontSize;
    private boolean maximized;
    
    
    public static Configuration getActiveConfiguration()
    {
        if (activeConfiguration == null)
        {
            activeConfiguration = new Configuration();
        }
        
        return activeConfiguration;
    }
    
    
    public static void setActiveConfiguration(Configuration config)
    {
        activeConfiguration = config;
    }
    
    
    public Configuration()
    {
        // Standardwerte einer neuen Configuration
        fontSize = 18;
        maximized = false;
    }
    
    
    public void save()
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("config.dat"));
            oos.writeObject(this);
            
            oos.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void load()
    {
        if (new File("config.dat").exists())
        {
            try
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("config.dat"));
                Configuration config = (Configuration) ois.readObject();
                
                this.fontSize = config.getFontSize();
                this.maximized = config.isMaximized();
                
                ois.close();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    
    public int getFontSize()
    {
        return fontSize;
    }
    
    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
    }
    
    
    public boolean isMaximized()
    {
        return maximized;
    }
    
    public void setMaximized(boolean maximized)
    {
        this.maximized = maximized;
    }
}