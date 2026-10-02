package cameoUIDialogs.RGDialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIPanels.ResponsePannels.For_AC_Functions_ManualResponsePannel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_AC_Functions_ManualFCGResponseDialog` class is responsible for handling and presenting a manual response dialog within the context of functional hazard assessment (FHA) in an aircraft systems modeler.
 */
public class For_AC_Functions_ManualFCGResponseDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private  For_AC_Functions_ManualResponsePannel contentPanel ;

	/**
	 * Create the dialog.
	 * @param timeTaken 
	 */
/**
 * @param parent The `parent` parameter is the parent frame that positions and owns the created dialog.
 * @param Response The `Response` parameter is a string representing the user-provided response for a manual Failure Condition Generation (FCG) scenario.
 * @param propertyMapUpdated The parameter `propertyMapUpdated` of type `List<String>>` is a mapping of properties that have been updated, used to configure the content panel for the dialog.
 * @param selectedNode The `selectedNode` parameter specifies the SysML element from which failure conditions are to be generated or refined within the dialog.
 * @param timeTaken The `timeTaken` parameter is a `String` that represents the duration required for a specific action or process, used to set the title of the dialog window.
 * @return The `For_AC_Functions_ManualFCGResponseDialog` method returns an instance of the `For_AC_Functions_ManualFCGResponseDialog` class. This dialog is used to display a custom panel that allows users to interact with a given response and selected node within a parent frame. The dialog includes configurations for font settings, preferred size, content pane setup, positioning relative to the parent frame, and default close operations.
 * @summary The `For_AC_Functions_ManualFCGResponseDialog` method is responsible for creating and configuring a dialog window that displays the response panel for manual Functional Condition Generation (FCG) processes, positioning it relative to its parent frame.
 */
	public For_AC_Functions_ManualFCGResponseDialog(Frame parent, String Response, Map<String, List<String>> propertyMapUpdated, Element selectedNode, String timeTaken) {

		super(parent, "OSIRIS: " + timeTaken, false);
	    // Set font for the dialog
	    setFont(new Font("Dialog", Font.PLAIN, 12));

		setPreferredSize(new Dimension(1030, 720));

//		Application.getInstance().getGUILog().showMessage("ManualResponsePannel3 will be created.");


	    // Create the content panel with the given response and selectedNode
	    this.contentPanel = new For_AC_Functions_ManualResponsePannel(Response, selectedNode,propertyMapUpdated);

	    // Set the content pane of the dialog to the content panel
	    this.setContentPane(contentPanel);

	    // Optionally pack the components to ensure layout correctness
	    this.pack();

	    // Position the dialog relative to the parent frame
	    this.setLocationRelativeTo(parent);

	    // Set default close operation to hide the dialog on close
	    this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

//		Application.getInstance().getGUILog().showMessage("Buttn Creation started creation in process.");
	}
	

}
