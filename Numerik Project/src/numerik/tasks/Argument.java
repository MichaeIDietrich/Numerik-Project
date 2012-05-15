package numerik.tasks;

import javax.swing.JComponent;

public class Argument
{
    public enum ArgType { MATRIX, VECTOR, DECIMAL, INTEGER, BOOLEAN, CHOICE, PRECISION, DOUBLEPRECISION, RUN_BUTTON }
    
    public final static Argument RUN_BUTTON = new Argument(null, ArgType.RUN_BUTTON); 
    public final static Argument PRECISION = new Argument("Genauigkeit:", ArgType.PRECISION, "12"); 
    public final static Argument DOUBLEPRECISION = new Argument("Genauigkeit:", ArgType.DOUBLEPRECISION, "12"); 
    
    private String name;
    private ArgType argumentType;
    private JComponent relatedControl;
    private String defaultValue;
    private String[] choices;
    
    
    public Argument(String name, ArgType argumentType)
    {
        this.name = name;
        this.argumentType = argumentType;
        this.defaultValue = "";
    }
    
    
    public Argument(String name, ArgType argumentType, String defaultValue)
    {
        this.name = name;
        this.argumentType = argumentType;
        this.defaultValue = defaultValue;
    }
    
    
    public Argument(String name, String... choices)
    {
        this.name = name;
        this.argumentType = ArgType.CHOICE;
        this.choices = choices;
    }
    
    
    public String getName()
    {
        return name;
    }
    
    
    public ArgType getArgumentType()
    {
        return argumentType;
    }
    
    
    public JComponent getRelatedControl()
    {
        return relatedControl;
    }
    
    
    public void setRelatedControl(JComponent relatedControl)
    {
        this.relatedControl = relatedControl;
    }
    
    
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    
    public String[] getChoices()
    {
        return choices;
    }
}
