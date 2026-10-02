package cameoUIDialogs.PGDialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIPanels.PromptGenerationPannels.Prompt_preview_pannel;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `PromptPreviewDialog` class is responsible for displaying a dialog that previews the generated prompt along with its properties and allows users to interact with or modify these details within the context of a SysML model in Cameo Systems Modeler.
 */
public class PromptPreviewDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private  JPanel contentPanel ;

/**
 * @param parent The `parent` parameter of type `Frame` is the parent window that positions and displays the prompt preview dialog, ensuring it is modality relative to this frame.
 * @param generated_prompt The `generated_prompt` parameter is a string representing the prompt that will be displayed and reviewed within the PromptPreviewDialog.
 * @param promptpropertyMap The `promptpropertyMap` parameter is a list of property strings used for generating and displaying preview prompts within the PromptPreviewDialog.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a list of strings representing the updated properties for each prompt, used to populate and update the content panel with new data.
 * @param selectedFunction The `selectedFunction` parameter represents the SysML element corresponding to the function whose failure conditions are being previewed.
 * @return The `PromptPreviewDialog` method returns a `PromptPreviewDialog` object, which is a custom dialog window designed for previewing prompts and their properties within the OSIRIS plugin. This dialog is used to display and interact with generated prompts and their associated property maps, allowing users to review and validate them in the context of the aircraft system being modeled.
 * @summary The `PromptPreviewDialog` method initializes a dialog window for previewing a generated prompt and its properties within the OSIRIS plugin.
 */
	public PromptPreviewDialog(Frame parent, String generated_prompt, Map<String, List<String>> promptpropertyMap, Map<String, List<String>> propertyMapUpdated, Element selectedFunction) {

		super(parent, "OSRIS", false);
	    // Set font for the dialog
	    setFont(new Font("Dialog", Font.PLAIN, 12));

//		setPreferredSize(new Dimension(820, 550));

//		Application.getInstance().getGUILog().showMessage("ManualResponsePannel3 will be created.");


	    // Create the content panel with the given response and selectedNode
	    this.contentPanel = new Prompt_preview_pannel(generated_prompt, promptpropertyMap,propertyMapUpdated,selectedFunction);

	    // Set the content pane of the dialog to the content panel
	    this.setContentPane(contentPanel);
	    

	    this.setSize(new Dimension(672, 561));

	    // Optionally pack the components to ensure layout correctness
	    this.pack();

	    // Position the dialog relative to the parent frame
	    this.setLocationRelativeTo(parent);

	    // Set default close operation to hide the dialog on close
	    this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

//		Application.getInstance().getGUILog().showMessage("Buttn Creation started creation in process.");
	}
}
