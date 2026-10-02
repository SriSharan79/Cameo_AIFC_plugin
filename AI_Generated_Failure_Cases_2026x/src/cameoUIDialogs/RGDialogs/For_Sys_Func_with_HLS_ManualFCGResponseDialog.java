package cameoUIDialogs.RGDialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIPanels.ResponsePannels.For_Sys_Func_with_HLS_ManualResponsePannel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_Sys_Func_with_HLS_ManualFCGResponseDialog` class is responsible for creating and managing a dialog window in OSIRIS that allows users to manually input or review  generated failure conditions for system functions with high-level safety context, providing an interface to update property maps and selected elements within the SysML model.
 */
public class For_Sys_Func_with_HLS_ManualFCGResponseDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private  JPanel contentPanel ;

	/**
	 * Create the dialog.
	 * @param timeTaken 
	 */
/**
 * @param parent The `parent` parameter of type `Frame` in the method `For_Sys_Func_with_HLS_ManualFCGResponseDialog` is used to specify the parent window that will display this dialog, allowing it to be positioned relative to and managed by the parent frame.
 * @param Response `Response` is a string representing the user's manual response for failure condition generation.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a map where each key corresponds to a property name and its value is a list of updated strings representing changes or modifications.
 * @param selectedNode The `selectedNode` parameter represents the SysML element currently selected by the user, which will be used to generate and refine failure conditions within the response panel.
 * @param timeTaken The `timeTaken` parameter represents the time duration for which a specific function or process has taken place, formatted as a string.
 * @return The `For_Sys_Func_with_HLS_ManualFCGResponseDialog` method returns an instance of the `For_Sys_Func_with_HLS_ManualFCGResponseDialog` class, which is a dialog window configured to display and manage a response panel for manual functional condition generation (FCG) in the OSIRIS plugin. This dialog is customized with specified properties such as font, size, content panel setup, and positioning relative to a parent frame.
 * @summary The `For_Sys_Func_with_HLS_ManualFCGResponseDialog` method creates and configures a dialog for manual Failure Condition Generation (FCG) responses in the OSIRIS plugin, displaying necessary information based on provided inputs such as response details and selected node.
 */
	public For_Sys_Func_with_HLS_ManualFCGResponseDialog(Frame parent, String Response, Map<String, List<String>> propertyMapUpdated, Element selectedNode, String timeTaken) {

		super(parent, "OSIRIS: " + timeTaken, false);
	    // Set font for the dialog
	    setFont(new Font("Dialog", Font.PLAIN, 12));

		setPreferredSize(new Dimension(1030, 720));

//		Application.getInstance().getGUILog().showMessage("ManualResponsePannel3 will be created.");


	    // Create the content panel with the given response and selectedNode
	    this.contentPanel = new For_Sys_Func_with_HLS_ManualResponsePannel(Response, selectedNode,propertyMapUpdated);

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
