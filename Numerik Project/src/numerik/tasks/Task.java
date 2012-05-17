package numerik.tasks;

import numerik.expression.Value;
import numerik.ui.controls.TaskPane;
import numerik.ui.dialogs.OutputFrame;

public interface Task
{
    
    public void init(OutputFrame frame, TaskPane taskPane);
    public void run(Value... parameters);
}
