package numerik.tasks;

import javax.swing.JComponent;

public class Argument
{
    public enum ArgType { MATRIX, VECTOR, DECIMAL, RUN_BUTTON }
    
    public final static Argument RUN_BUTTON = new Argument(null, ArgType.RUN_BUTTON); 
    
    private String name;
    private ArgType argumentType;
    private JComponent relatedControl;
    
    
    public Argument(String name, ArgType argumentType)
    {
        this.name = name;
        this.argumentType = argumentType;
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
}
