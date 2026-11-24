package cameoUIDialogs.PGDialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIPanels.PromptGenerationPannels.For_Auto_AC_Functions_FCGSimplePanel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_AC_Functions_FCGSimpleActionDialog` class is responsible for creating and managing a dialog window to handle simple actions related to the failure condition generation for functions using the OSIRIS plugin.
 */
public class For_AC_Functions_FCGSimpleActionDialog extends JDialog {


	private static final long serialVersionUID = -239242083582972729L;
	private JPanel  contentPanel2;

/**
 * @param parent The `parent` parameter is the parent frame that will be used for centering and managing the lifecycle of this dialog window.
 * @param selectedNode The `selectedNode` parameter represents the currently selected SysML element in the model that is being processed for failure condition generation.
 * @return The `For_AC_Functions_FCGSimpleActionDialog` method returns an instance of the `For_AC_Functions_FCGSimpleActionDialog` class. This dialog is used to prompt the user for generating failure conditions based on a selected SysML model element. The returned object represents a dialog window configured with specific size, font settings, and content panel tailored for the given SysML element.
 * @summary The `For_AC_Functions_FCGSimpleActionDialog` method is responsible for initializing and configuring a dialog window for generating prompts, including setting its size, font, content panel, and behavior upon closing.
 */
	public For_AC_Functions_FCGSimpleActionDialog(Frame parent, Element selectedNode) {
		super(parent, "Prompt Generation", false);

		//setPreferredSize(new Dimension(805, 780));
		setResizable(true); // Allow user and layout to resize dynamically

		setFont(new Font("Dialog", Font.PLAIN, 12));

		this.contentPanel2 = new For_Auto_AC_Functions_FCGSimplePanel(selectedNode);
		// Wrap in a JScrollPane so if the panel expands or is opened on a smaller screen, nothing gets cut off
        JScrollPane scrollPane = new JScrollPane(this.contentPanel2);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Automatically sizes the dialog to fit For_Auto_AC_Functions_FCGSimplePanel's preferred size
        this.pack();

        if (parent instanceof OSIRISFrame) {
            ((OSIRISFrame) parent).centerDialog(this);
        } else {
            this.setLocationRelativeTo(parent);
        }
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // Frees memory when closed
	}




}
