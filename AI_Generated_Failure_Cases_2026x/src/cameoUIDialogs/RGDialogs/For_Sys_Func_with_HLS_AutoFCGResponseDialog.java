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

import cameoUIPanels.ResponsePannels.For_Sys_Func_with_HLS_AutoResponsePannel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_Sys_Func_with_HLS_AutoFCGResponseDialog` class is responsible for managing the dialog interface that displays and updates formatted responses, property maps, selected nodes, and time taken information in response to system function analysis with High-Level Safety (HLS) considerations.
 */
public class For_Sys_Func_with_HLS_AutoFCGResponseDialog extends JDialog {

	private static final long serialVersionUID = 1L;
    private JPanel contentPanel;


/**
 * @param parent The `parent` parameter in the method `For_Sys_Func_with_HLS_AutoFCGResponseDialog` is a reference to the main frame from which this dialog should be centered and managed.
 * @param formattedResponse The `formattedResponse` parameter is a map that stores structured failure condition data with their corresponding properties and possible outcomes.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a list of strings representing updated properties associated with the selected node in the SysML model, used to populate and manage the UI components within the response dialog.
 * @param selectedNode The `selectedNode` parameter represents the SysML element currently selected within the Cameo Systems Modeler interface, which serves as a reference point for the dialog's context and data presentation.
 * @param response The `response` parameter is a String that contains the generated failure conditions for the selected SysML element.
 * @param timeTaken The `timeTaken` parameter in the method signature represents the duration for which a specific operation or response was active.
 * @return The return value of `For_Sys_Func_with_HLS_AutoFCGResponseDialog` is an instance of `For_Sys_Func_with_HLS_AutoFCGResponseDialog`, which represents a dialog window configured to display and manage the formatted response for the selected system function with auto-generated failure conditions. This dialog includes user interface components that present detailed information based on the provided maps, element, response, and time taken.
 * @summary The `For_Sys_Func_with_HLS_AutoFCGResponseDialog` method constructs and configures a dialog window for displaying auto-generated failure conditions from SysML models in OSIRIS, using various input parameters to set up the UI components.
 */
    public For_Sys_Func_with_HLS_AutoFCGResponseDialog(
            Frame parent,
            Map<String, Map<String, List<String>>> formattedResponse,
            Map<String, List<String>> propertyMapUpdated,
            Element selectedNode,
            String response,
            String timeTaken) {

        super(parent, "OSIRIS: " + timeTaken, false);

        setFont(new Font("Dialog", Font.PLAIN, 12));

        this.contentPanel = new For_Sys_Func_with_HLS_AutoResponsePannel(
                this,
                formattedResponse,
                selectedNode,
                propertyMapUpdated,
                response,timeTaken);

        setContentPane(contentPanel);

        /* Desired fixed size – you can adjust the numbers if you want */
        setPreferredSize(new Dimension(825, 840));

        /* Pack to honour the preferred size of the content panel */
        pack();

        /* Show dialog centred on the parent */
        setLocationRelativeTo(parent);

        /* Hide when the user closes the window */
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

/**
 * @param newFormattedResponse The `newFormattedResponse` parameter is a hierarchical map containing formatted failure conditions for updating the content display.
 * @param newPropertyMapUpdated The `newPropertyMapUpdated` parameter is a list of strings representing updated properties to be forwarded for content updates in the panel.
 * @param newSelectedNode The `newSelectedNode` parameter represents the currently selected SysML element that will be updated with the new content.
 * @param newResponse The `newResponse` parameter is a string containing the latest response content to be updated in the panel.
 * @param timeTaken The `timeTaken` parameter represents the duration taken for a specific operation, formatted as a string.
 * @summary The `updateContent` method updates the content of a dialog by setting its title, forwarding the update to a panel, and adjusting the layout, repaint, and packing.
 */
    public void updateContent(
            Map<String, Map<String, List<String>>> newFormattedResponse,
            Map<String, List<String>> newPropertyMapUpdated,
            Element newSelectedNode,
            String newResponse,
            String timeTaken) {

        setTitle("OSIRIS: " + timeTaken);

        if (contentPanel != null) {
            ((For_Sys_Func_with_HLS_AutoResponsePannel) contentPanel)
                    .updatePanelContent(newFormattedResponse,
                                        newSelectedNode,
                                        newPropertyMapUpdated,
                                        newResponse);
        }

        revalidate();   // re‑layout if component sizes changed
        repaint();      // repaint the dialog
        pack();         // honour the new title length
    }
}
