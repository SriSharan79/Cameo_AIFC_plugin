package cameoUIDialogs.PGDialogs;

import java.awt.Frame;
import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import cameoUIPanels.PromptGenerationPannels.SystemMessagePreviewPanel;
import java.awt.Dimension;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `SystemMessagePreviewDialog` class is responsible for creating and managing a dialog window that allows users to preview system messages and associated files within a given directory context.
 */
public class SystemMessagePreviewDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final SystemMessagePreviewPanel contentPanel ;


/**
 * @param parent The `parent` parameter in the `SystemMessagePreviewDialog` method is a reference to the parent window that will position and manage this dialog.
 * @param sys_MSG `sys_MSG`: The message string to be previewed in the dialog.
 * @param sYS_MSG_FilePath The `sYS_MSG_FilePath` parameter of type `Path` in the method `SystemMessagePreviewDialog` specifies the file path of the system message content that will be previewed within the dialog.
 * @param targetDirPath The `targetDirPath` parameter specifies the directory path where the target files will be saved or accessed from within the SystemMessagePreviewDialog.
 * @return The `SystemMessagePreviewDialog` method returns an instance of `SystemMessagePreviewDialog`, a custom dialog window designed for previewing system messages within the OSIRIS framework. This dialog is configured with specific dimensions, content panel setup, positioning relative to its parent frame, and default close operation behavior. The returned object encapsulates these settings and provides a user interface component that can be utilized to display system message previews effectively in safety assessment contexts.
 * @summary The `SystemMessagePreviewDialog` method constructs and configures a dialog window for previewing system messages, initializing it with specified parent frame, message content, file paths, and setting up its size, position, and behavior.
 */
	public SystemMessagePreviewDialog(Frame parent, String sys_MSG, Path sYS_MSG_FilePath, Path targetDirPath) {
		super(parent, "OSIRIS", false);
		setPreferredSize(new Dimension(830, 360));
		
		this.contentPanel=new SystemMessagePreviewPanel(this,sys_MSG,sYS_MSG_FilePath,targetDirPath);
		
		// Set the content pane of the dialog to the content panel
		this.setContentPane(contentPanel);
	    // Resize dialog to match the size of the content panel
	    this.setSize(new Dimension(830, 369));

		// Optionally pack the components to ensure layout correctness
		this.pack();

		// Position the dialog relative to the parent frame
		this.setLocationRelativeTo(parent);

		// Set default close operation to hide the dialog on close
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

	}
	



}
