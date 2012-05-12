package numerik.ui;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import numerik.tasks.Task;

public class TabbedTaskPane extends JTabbedPane implements ChangeListener
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
        super.add(title, taskPane);
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        System.out.println(e.getSource());
        if (this.getSelectedComponent() instanceof TaskPane)
        {
            TaskPane taskPane = (TaskPane)this.getSelectedComponent();
            taskPane.showTask();
        }
    }
}
