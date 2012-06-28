package numerik.tasks;

import javax.swing.JComponent;

public final class Argument
{
    public enum ArgType { MATRIX, VECTOR, DECIMAL, DECIMAL_EX, INTEGER, EXPRESSION, BOOLEAN, CHOICE, 
        PRECISION, DOUBLEPRECISION, RUN_BUTTON, STOP_BUTTON }
    
    public final static Argument RUN_BUTTON = new Argument(null, ArgType.RUN_BUTTON);
    public final static Argument STOP_BUTTON = new Argument(null, ArgType.STOP_BUTTON);
    public final static Argument PRECISION = new Argument("Mantissenlänge:", ArgType.PRECISION, "16");
    
    private String name;
    private ArgType argumentType;
    private JComponent relatedControl;
    private String defaultValue;
    private String[] choices;
    private int controlWidth;
    
    
    public Argument(String name, ArgType argumentType)
    {
        this(name, argumentType, null, 50);
    }
    
    
    public Argument(String name, ArgType argumentType, String defaultValue)
    {
        this(name, argumentType, defaultValue, 50);
    }
    
    
    public Argument(String name, ArgType argumentType, int controlWidth)
    {
        this(name, argumentType, null, controlWidth);
    }
    
    
    public Argument(String name, ArgType argumentType, String defaultValue, int controlWidth)
    {
        this.name = name == null ? "" : name;
        this.argumentType = argumentType;
        this.defaultValue = defaultValue == null ? "" : defaultValue;
        this.controlWidth = controlWidth;
        // die Standardgröße des JSpinners auf 100 setzen
        if (argumentType == ArgType.PRECISION && controlWidth == 50)
        {
            this.controlWidth = 100;
        }
    }
    
    
    public Argument(String name, String... choices)
    {
        this.name = name == null ? "" : name;
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
    
    
    public int getControlWidth()
    {
        return controlWidth;
    }
}
