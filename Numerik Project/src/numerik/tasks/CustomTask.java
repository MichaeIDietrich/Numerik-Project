package numerik.tasks;

import java.math.BigDecimal;

import numerik.calc.MathLib;
import numerik.expression.Value;
import numerik.ui.controls.*;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;

public class CustomTask implements Task
{
    private TaskPane taskPane;
    
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.taskPane = taskPane;
    }
    
    
    @Override
    public void run(Value... parameters)
    {
        LatexFormula formula = new LatexFormula();
        
        formula.addText("Hier kann man selbst Code hinzufügen :)").addNewLine(4);
        // hier dein Zeugs machen
        
        
        taskPane.setViewPortView(new TaskScrollPane(formula));
    }
    
}
