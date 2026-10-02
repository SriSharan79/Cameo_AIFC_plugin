package cameoUIDialogs.PGDialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIPanels.PromptGenerationPannels.For_No_PB_Sys_Functions_FCGPanel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_No_PB_Sys_Functions_FCGActionDialog` class is responsible for creating and managing a dialog interface that handles functional condition generation (FCG) actions specifically for system functions.
 */
public class For_No_PB_Sys_Functions_FCGActionDialog extends JDialog {

	private static final long serialVersionUID = -239242083582972729L;
	private JPanel  contentPanel2;

/**
 * @param parent The `parent` parameter of type `Frame` in the method `For_No_PB_Sys_Functions_FCGActionDialog` is the parent window to which this dialog will be relative for positioning.
 * @param selectedNode The `selectedNode` parameter represents the specific SysML element from the model that is currently selected for further processing within the dialog.
 * @return The `For_No_PB_Sys_Functions_FCGActionDialog` method returns an instance of the `For_No_PB_Sys_Functions_FCGActionDialog` class. This instance is a dialog window configured with specific settings such as size, font, and content panel, which is based on the provided `Element selectedNode`. The purpose of this method is to create and return a custom dialog for performing actions related to "Function No PB Sys Functions" in the OSIRIS tool.
 * @summary The `For_No_PB_Sys_Functions_FCGActionDialog` method constructs and configures a dialog window for displaying and interacting with the failure conditions generated from system functions in OSIRIS.
 */
	public For_No_PB_Sys_Functions_FCGActionDialog(Frame parent, Element selectedNode) {
		super(parent, "OSIRIS", false);
		setPreferredSize(new Dimension(800, 840));
		setSize(new Dimension(800, 840));

		setFont(new Font("Dialog", Font.PLAIN, 12));

		this.contentPanel2 = new For_No_PB_Sys_Functions_FCGPanel(selectedNode);

		this.setContentPane(contentPanel2);

		this.pack();

		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}



}
