package customCameoActions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JDialog;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.ui.browser.Tree;
import com.nomagic.magicdraw.ui.browser.actions.DefaultBrowserAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIDialogs.PGDialogs.For_AC_Functions_FCGSimpleActionDialog;
import cameoUIDialogs.PGDialogs.For_No_PB_Sys_Functions_FCGActionDialog;
import cameoUIDialogs.PGDialogs.OSIRISFrame;
import elementDataExtraction.PropertiesFunction;
import elementDataExtraction.StereotypeSearch;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `BA_For_Auto_FC_Prompt_Creator` class is responsible for generating and managing failure condition prompts for automatic functional checks in aircraft systems.
 */
public class BA_For_Auto_FC_Prompt_Creator extends DefaultBrowserAction {

	private static final long serialVersionUID = 8384887777768954522L;
	StereotypeSearch StereotypeChecker = new StereotypeSearch();


/**
 * @param id The `id` parameter is a unique identifier for the created failure condition prompt.
 * @param name The `name` parameter is the descriptive label for the automatically generated failure condition prompt.
 * @return The return value of the `BA_For_Auto_FC_Prompt_Creator` method is an instance of the `BA_For_Auto_FC_Prompt_Creator` class. This instance is initialized with a specific ID and name, and it represents a new auto-generated prompt creator for failure conditions in the context of aircraft systems safety assessment. The return value encapsulates configuration details that facilitate further refinement of these failure conditions within the OSIRIS system.
 * @summary The `BA_For_Auto_FC_Prompt_Creator` method initializes an instance with a specified identifier and name, setting additional parameters to null.
 */
	public BA_For_Auto_FC_Prompt_Creator(String id, String name) {
		super(id, name, null, null);
	}

/**
 * @param actionEvent The `actionEvent` parameter represents the event that triggered this method, encapsulating information about the GUI component and action performed.
 * @summary The `actionPerformed` method handles user interactions with the selected node in the tree, displaying appropriate dialogs based on whether the selected element is an activity and has specific stereotypes.
 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		var tree = getTree();

		var selectedNode = tree.getSelectedNode().getUserObject();
//		var parent = MDDialogParentProvider.getProvider().getDialogParent(true);
		OSIRISFrame parent=new OSIRISFrame();

		if (selectedNode instanceof Activity) {
			if (StereotypeChecker.hasStereotypeBoolean((Element)selectedNode, "Function")) {

				var selectedfunction = (Element) selectedNode;
				Boolean extraProps = CheckProperties(selectedfunction);

				JDialog dialog = new JDialog();
				
				if(extraProps) {
					dialog = new For_No_PB_Sys_Functions_FCGActionDialog(parent,(Element)selectedNode);
				}else {
					dialog = new For_AC_Functions_FCGSimpleActionDialog(parent,(Element)selectedNode);
				}

				dialog.setVisible(true);
				}
			else {
				Application.getInstance().getGUILog().showMessage("Selected Element is not a Function");
			}
		}
		else {
			Application.getInstance().getGUILog().showMessage("Selected Element is not a Activity");	
		}

	}

/**
 * @param selectedfunction The `selectedfunction` parameter represents the specific SysML function element whose properties will be checked for non-empty input, output parameters, or requirements.
 * @return The return value of the `CheckProperties` method indicates whether any property lists (InputParameters, OutputParameters, or Requirements) for a given function are non-empty. It returns `true` if any of these lists contain elements and `false` otherwise.
 * @summary This method checks if the specified function element has any non-empty input parameters, output parameters, or requirements.
 */
	private Boolean CheckProperties(Element selectedfunction) {
		// Initialize value to false
		Boolean value = false;

		// Create an instance of PropertiesFunction2 and generate the property map
		var functionProperties = new PropertiesFunction();
		var propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction);

		// Retrieve the lists from the property map
		List<Element> inputList = propertyMap.get("InputParameters");
		List<Element> outputList = propertyMap.get("OutputParameters");
		List<Element> requirementList = propertyMap.get("Requirements");

		// Check if any of the lists is not empty
		if ((inputList != null && !inputList.isEmpty()) ||
				(outputList != null && !outputList.isEmpty()) ||
				(requirementList != null && !requirementList.isEmpty())) {
			value = true;
		}

		return value;
	}

/**
 * @summary The `updateState` method is responsible for enabling or disabling a component based on whether the currently selected node in the tree is an instance of `Activity`.
 */
	@Override
	public void updateState() {
		boolean enable = false;
		Tree tree = getTree();

		if (tree != null) {
			var selectedNode = tree.getSelectedNode();
			if (selectedNode != null && selectedNode.getUserObject() instanceof Activity) {
				enable = true;
			}
		}

		setEnabled(enable);
	}
}
