package org.hazelwire.gui;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.hazelwire.modules.Option;

public class OptionsCompListener implements MouseListener, FocusListener
{

	private OptionsComposite oc;
	private GUIBuilder gUIBuilder;

	public OptionsCompListener(OptionsComposite oc, GUIBuilder gUIBuilder)
	{
		this.oc = oc;
		this.gUIBuilder = gUIBuilder;
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0)
	{
	}

	@Override
	public void mouseDown(MouseEvent arg0)
	{
	}

	@Override
	public void mouseUp(MouseEvent m)
	{
		if (m.getSource() instanceof Button)
		{
			oc.getText().setText(oc.getMod().getOption(oc.getOption()).getDefaultValue());
			
			Option tempOption = oc.getMod().getOption(oc.getOption());
			tempOption.setValue(tempOption.getDefaultValue());
			oc.getMod().editOption(tempOption);
		}
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
	}

	@Override
	public void focusLost(FocusEvent f)
	{
		if (f.getSource() instanceof Text)
		{
			//oc.getMod().getOptions().put(oc.getOption(),
			//		((Text) f.getSource()).getText());
			
			Option tempOption = oc.getMod().getOption(oc.getOption());
			tempOption.setValue(((Text) f.getSource()).getText());
			oc.getMod().editOption(tempOption);
			
			System.out.println("" + oc.getMod().getOptions().toString());
		}
	}

}
