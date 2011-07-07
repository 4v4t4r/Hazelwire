package org.hazelwire.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.hazelwire.xml.ExportModuleConfiguration;
import org.hazelwire.xml.ImportModuleConfiguration;

/**
 * This class is used for reading an XML file containing
 * Virtual Machine configuration information (as generated in
 * {@link ConfigExportListener}) and converting it so that
 * the GUI displays and stores it correctly. It is a subclass of
 * {@link MouseListener} and it is bound to the 'import from XML'
 * button.
 */
public class ConfigImportMouseListener implements MouseListener
{

	private GUIBuilder gUIBuilder;

	/**
	 * Constructs an instance of ConfigImportMouseListener
	 * @param gUIBuilder the {@link GUIBuilder} is needed in
	 * order to store the information obtained from the 
	 * XML file into the GUI.
	 */
	public ConfigImportMouseListener(GUIBuilder gUIBuilder)
	{
		this.gUIBuilder = gUIBuilder;
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0)
	{}

	@Override
	public void mouseDown(MouseEvent arg0)
	{}

	@Override
	/**
	 * When the 'import from XML' buttton is clicked, this method
	 * is called. It creates a new {@link FileDialog}, in which the
	 * user can select the XML file to be used. If this is completed
	 * correctly, the method will parse the XML and set all configurations
	 * specified in it in the system. These settings will also be
	 * displayed in the GUI.
	 */
	public void mouseUp(MouseEvent m)
	{
		FileDialog fd = new FileDialog(((Button)m.getSource()).getShell(), SWT.OPEN);
        //Hier stond eerst export. Nu niet meer.
		fd.setText("Import configuration");

        String selected = fd.open();
        if(selected!=null){
        	try
			{
        		VMGenerationThread.synchronizeModules();
				new ImportModuleConfiguration(selected);
			}
			catch (Exception e)
			{
				// TODO proper visual errors
				e.printStackTrace();
			}
        }
	}

}
