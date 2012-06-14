package numerik;

public final class Configuration
{
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
        fontSize = 20;
        maximized = false;
    }
    
    public void save()
    {
        // noch nicht implementiert
    }
    
    public void load()
    {
        // noch nicht implementiert
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