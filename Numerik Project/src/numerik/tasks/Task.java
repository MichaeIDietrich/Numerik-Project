package numerik.tasks;

import numerik.expression.Value;
import numerik.ui.*;

public interface Task
{
    
    public void init(OutputFrame frame, TaskPane taskPane);
    public void run(Value... values);
}
