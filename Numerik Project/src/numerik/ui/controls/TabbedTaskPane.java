package numerik.ui.controls;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import numerik.tasks.Task;
import numerik.ui.dialogs.OutputFrame;

public final class TabbedTaskPane extends JTabbedPane implements ChangeListener
{
    
    OutputFrame frame;
    
    public TabbedTaskPane(OutputFrame frame)
    {
        super(JTabbedPane.BOTTOM);
        this.frame = frame;
        this.addChangeListener(this);
    }
    
    
    public void addTab(String title, Task task)
    {
        TaskPane taskPane = new TaskPane(frame, task, true);
        super.addTab(title, taskPane);
    }
    
    
    public void addTab(String title, Task task, String... toolTipLines)
    {
        TaskPane taskPane = new TaskPane(frame, task, true);
        
        StringBuilder buffer = new StringBuilder();
        
        buffer.append("<html>");
        for (int i = 0; i < toolTipLines.length; i++)
        {
            buffer.append(toolTipLines[i]);
            if (i < toolTipLines.length - 1)
            {
                buffer.append("<br/>");
            }
        }
        buffer.append("</html>");
        
        super.addTab(title, null, taskPane, buffer.toString());
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        if (this.getSelectedComponent() instanceof TaskPane)
        {
            TaskPane taskPane = (TaskPane)this.getSelectedComponent();
            taskPane.showTask();
        }
    }
}