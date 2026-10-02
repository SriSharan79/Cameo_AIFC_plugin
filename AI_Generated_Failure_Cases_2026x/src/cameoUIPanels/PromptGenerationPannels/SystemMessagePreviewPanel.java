package cameoUIPanels.PromptGenerationPannels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.nomagic.magicdraw.core.Application;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `SystemMessagePreviewPanel` class is responsible for rendering and managing the preview of system messages within a dialog in the OSIRIS plugin.
 */
public class SystemMessagePreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

/**
 * @param parentDialog The `parentDialog` parameter is a reference to the `JDialog` that initiated this panel, allowing it to be closed or disposed of programmatically.
 * @param sys_MSG The `sys_MSG` parameter in the method `SystemMessagePreviewPanel` is a string that contains the initial system message to be displayed and edited.
 * @param sYS_MSG_FilePath The `sYS_MSG_FilePath` parameter is the file path where the system message will be saved.
 * @param targetDirPath The `targetDirPath` parameter in the `SystemMessagePreviewPanel` method is a `Path` object that specifies the directory where the system message file will be saved.
 * @return The return value of the `SystemMessagePreviewPanel` method is an instance of a custom panel that displays and allows for the editing of system messages. This panel includes:
- A label indicating "System Message"
- A scrollable text area pre-populated with a given message
- An update button to save changes to the specified file path
- A close button to dismiss the dialog.

The primary purpose is to provide an interactive interface for viewing and updating system messages within a GUI application.
 * @summary The `SystemMessagePreviewPanel` method is responsible for creating and managing a GUI panel that allows users to preview and update system messages within the OSIRIS plugin.
 */
	public SystemMessagePreviewPanel(JDialog parentDialog, String sys_MSG,
			Path sYS_MSG_FilePath, Path targetDirPath) {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("System Message:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 211, 14);
		add(lblNewLabel);
		
		JScrollPane Text_scrollPane = new JScrollPane();
		Text_scrollPane.setBounds(10, 32, 780, 255);
		add(Text_scrollPane);
		
		JTextArea textArea_Sys_Msg = new JTextArea(sys_MSG);
		Text_scrollPane.setViewportView(textArea_Sys_Msg);
		textArea_Sys_Msg.setWrapStyleWord(true);
		textArea_Sys_Msg.setLineWrap(true);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
/**
 * @param e The `ActionEvent` parameter `e` in the `actionPerformed` method represents the event that triggered this callback, typically a user interface action such as a button click.
 * @summary The `actionPerformed` method initiates a background task to save system messages by writing their contents to a specified file path, while ensuring that all I/O operations are conducted on a separate thread to avoid blocking the Event Dispatch Thread (EDT).
 */
		    public void actionPerformed(ActionEvent e) {
		        new SwingWorker<Void, Void>() {
/**
 * @return The `doInBackground` method performs I/O operations in a background thread. It saves system messages to a file if the storage path is set; otherwise, it logs an error message. The return value of this method is Void, indicating that it does not produce any output or result.
 * @summary The `doInBackground` method is responsible for performing I/O operations in a background thread to save system messages from the text area to a specified file path.
 */
		            @Override
		            protected Void doInBackground() throws Exception {
		                // Perform the I/O operations in this background thread
		                if (sYS_MSG_FilePath == null) {
		                    Application.getInstance().getGUILog().showMessage("Cannot save System Message: Storage paths are not set.");
		                    return null;
		                }
		                Files.createDirectories(targetDirPath);
		                Files.writeString(sYS_MSG_FilePath, textArea_Sys_Msg.getText(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		                return null;
		            }

/**
 * @summary The `done` method in Java is responsible for handling the completion of a background task, displaying success or error messages to the user, and disposing of any associated dialogs.
 */
		            @Override
		            protected void done() {
		                try {
		                    // This code runs on the EDT after doInBackground() is finished
		                    get(); // This will rethrow any exceptions from doInBackground
		                    Application.getInstance().getGUILog().showMessage("System Message saved successfully.");
		    		        if (parentDialog != null) {
		    		            parentDialog.dispose();
		    		        }
		                } catch (Exception ex) {
		                    Application.getInstance().getGUILog().showMessage("Error saving System Message: " + ex.getMessage());
		                    JOptionPane.showMessageDialog(SystemMessagePreviewPanel.this,
		                            "Error writing file: " + ex.getMessage(),
		                            "File Write Error", JOptionPane.ERROR_MESSAGE);
		                }
		            }
		            
		            
		        }.execute(); // Start the background task
		    }
		    
		    
		});
		btnUpdate.setBounds(576, 298, 89, 23);
		add(btnUpdate);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
/**
 * @param e The `ActionEvent` parameter `e` represents the event that triggered this method, typically a user action like a button click or menu selection.
 * @summary The `actionPerformed` method is responsible for closing the parent dialog when an action event occurs, ensuring that the UI remains clean and avoids potential memory leaks.
 */
			public void actionPerformed(ActionEvent e) {
		        if (parentDialog != null) {
		            parentDialog.dispose();
		        }
			}
		});
		btnClose.setBounds(675, 298, 89, 23);
		add(btnClose);

	}
}
