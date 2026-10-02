package cameoUIDialogs.RGDialogs;

import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIPanels.ResponsePannels.For_AC_Functions_AutoResponsePannel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_AC_Functions_AutoFCGResponseDialog` class is responsible for managing the display and updating of response dialog content related to failure conditions of aircraft functions based on the provided data and selected node.
 */
public class For_AC_Functions_AutoFCGResponseDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private  For_AC_Functions_AutoResponsePannel contentPanel ;


/**
 * @param parent The `parent` parameter of type `Frame` in the method `For_AC_Functions_AutoFCGResponseDialog` specifies the parent frame to which the new dialog will be associated, determining its positioning and behavior.
 * @param formattedResponse The `formattedResponse` parameter contains a nested map structure where each key maps to another map of keys and corresponding lists of strings, representing the structured response data for auto-generated Failure Condition Groups (FCGs).
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a list of strings representing updated properties for auto FCG response dialog.
 * @param selectedNode The `selectedNode` parameter represents the SysML element that has been chosen for further processing or display within the dialog.
 * @param response The `response` parameter is a String that contains the formatted response data for the dialog.
 * @param timeTaken The `timeTaken` parameter represents the duration or timestamp related to a specific response, used for labeling and displaying in the dialog.
 * @return The method `For_AC_Functions_AutoFCGResponseDialog` returns an instance of `For_AC_Functions_AutoFCGResponseDialog`. This object represents a custom response dialog configured with specific information, including the response details, selected node, and other relevant data. The dialog is designed to display formatted responses in a user-friendly manner within the Cameo Systems Modeler environment, facilitating functional hazard assessment for aircraft systems.
 * @summary The `For_AC_Functions_AutoFCGResponseDialog` method is responsible for creating and configuring a response dialog with specified content, including setting font, content panel, size, position relative to parent frame, and default close operation.
 */
	public For_AC_Functions_AutoFCGResponseDialog(Frame parent, Map<String, Map<String, List<String>>> formattedResponse, Map<String, List<String>> propertyMapUpdated, Element selectedNode, String response, String timeTaken) {

		super(parent, "OSIRIS: " + timeTaken, false);
		// Set font for the dialog
		setFont(new Font("Dialog", Font.PLAIN, 12));

		// Create the content panel with the given response and selectedNode
		this.contentPanel = new For_AC_Functions_AutoResponsePannel(this, formattedResponse, selectedNode,propertyMapUpdated,response,timeTaken);

		// Set the content pane of the dialog to the content panel
		this.setContentPane(contentPanel);
	    // Resize dialog to match the size of the content panel
	    this.setSize(contentPanel.getPreferredSize());

		// Optionally pack the components to ensure layout correctness
		this.pack();

		// Position the dialog relative to the parent frame
		this.setLocationRelativeTo(parent);

		// Set default close operation to hide the dialog on close
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

	}

/**
 * @param newFormattedResponse The `newFormattedResponse` parameter represents a map of formatted responses keyed by identifiers, where each identifier maps to another map containing lists of strings representing different response formats or conditions.
 * @param newPropertyMapUpdated `newPropertyMapUpdated` is a list of string properties that have been updated or modified as part of the content update process.
 * @param newSelectedNode `newSelectedNode` is the SysML element currently selected, which influences content updates.
 * @param newResponse The `newResponse` parameter of type `String` in the `updateContent` method represents the updated content or message that needs to be displayed or utilized within the content panel.
 * @param timeTaken The `timeTaken` parameter represents the time duration for which a particular process or operation was executed, formatted as a string.
 * @summary The `updateContent` method is responsible for updating the content of a panel with new data, setting the title of the window, and refreshing the UI to reflect these changes.
 */
    public void updateContent(Map<String, Map<String, List<String>>> newFormattedResponse, 
                              Map<String, List<String>> newPropertyMapUpdated, 
                              Element newSelectedNode, 
                              String newResponse, String timeTaken) {
        
        // Delegate the update responsibility to the content panel
        // You MUST implement this 'updatePanelContent' method in your
        // For_Sys_Func_with_HLS_AutoResponsePannel_1C class.

        setTitle("OSIRIS: " + timeTaken);
        if (contentPanel != null) {
            contentPanel.updatePanelContent(newFormattedResponse, newSelectedNode, newPropertyMapUpdated, newResponse);
        }

        // Re-layout and repaint the dialog to reflect the changes in the panel
        revalidate(); // Re-layouts the components if anything changed their size
        repaint();    // Repaints the dialog
    }
}
