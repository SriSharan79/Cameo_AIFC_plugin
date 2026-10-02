package customCameoActions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JDialog;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.ui.actions.DefaultDiagramAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
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
 * @summary The `DA_For_Auto_FC_Prompt_Creator` class is primarily responsible for creating and managing the automatic generation of failure condition prompts in the OSIRIS plugin by handling user actions, validating properties, and updating its state.
 */
public class DA_For_Auto_FC_Prompt_Creator extends DefaultDiagramAction {

	private static final long serialVersionUID = 8384887777768954522L;
	StereotypeSearch StereotypeChecker = new StereotypeSearch();


/**
 * @param id The `id` parameter is a unique identifier for the auto-generated failure condition prompt within the system.
 * @param name The `name` parameter in the `DA_For_Auto_FC_Prompt_Creator` method represents the identifier or label used for naming the failure condition prompt.
 * @return The `DA_For_Auto_FC_Prompt_Creator` method returns an instance of the `DA_For_Auto_FC_Prompt_Creator` class. This instance is initialized with a specified ID and name, and it represents a prompt creator for automatically generating failure conditions in the context of Functional Hazard Assessment (FHA) for aircraft systems.
 * @summary This method initializes an instance of the class responsible for creating automatic failure condition prompts within the OSIRIS plugin.
 */
	public DA_For_Auto_FC_Prompt_Creator(String id, String name) {
		super(id, name, null, null);
	}

/**
 * @param actionEvent The `actionEvent` parameter represents the event object that encapsulates the details of the action performed.
 * @summary The `actionPerformed` method handles the user action by checking if a selected SysML model element is either a CallBehaviorAction or a Function, and then launches an appropriate dialog to manage failure conditions.
 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		// Get the selected node
		var selectedNode = getFirstSelected().getElement();

//		var parent = MDDialogParentProvider.getProvider().getDialogParent(true);
		OSIRISFrame parent=new OSIRISFrame();

		// Check if selected node is an Action and not null
		if (selectedNode instanceof Action && selectedNode != null) {

			// Check if selected node is a CallBehaviorAction
			if (selectedNode instanceof CallBehaviorAction) {
				// Get the behavior of the CallBehaviorAction
				var function = ((CallBehaviorAction) selectedNode).getBehavior();

				// Check if the behavior is an Activity
				if (function instanceof Activity) {

					Boolean extraProps = CheckProperties(function);


					JDialog dialog = new JDialog();
					if(extraProps) {
						dialog = new For_No_PB_Sys_Functions_FCGActionDialog(parent,(Element)selectedNode);
					}else {
						dialog = new For_AC_Functions_FCGSimpleActionDialog(parent,(Element)selectedNode);
					}

					dialog.setVisible(true);

				} else {
					Application.getInstance().getGUILog().showMessage("Behavior is not an instance of Activity.");
				}
			} else {
				Application.getInstance().getGUILog().showMessage("Selected node is not an instance of CallBehaviorAction.");
			}
		}else {
			StereotypeSearch checkstereotype= new StereotypeSearch();
			if(checkstereotype.hasStereotypeBoolean(selectedNode, "Function")) {
				var selectedfunction = (Element) selectedNode;
				Boolean extraProps = CheckProperties(selectedfunction);


				JDialog dialog = new JDialog();
				
				if(extraProps) {
					dialog = new For_No_PB_Sys_Functions_FCGActionDialog(parent,(Element)selectedNode);
				}else {
					dialog = new For_AC_Functions_FCGSimpleActionDialog(parent,(Element)selectedNode);
				}

				dialog.setVisible(true);

			}else {
				Application.getInstance().getGUILog().showMessage("Selected node is not Function.");
			}

		}

	}

/**
 * @param selectedfunction The `selectedfunction` parameter is an Element representing the SysML function selected for property validation in the `CheckProperties` method.
 * @return The return value of the `CheckProperties` method is a `Boolean` that indicates whether the selected function element has non-empty input parameters, output parameters, or requirements.
 * @summary The `CheckProperties` method determines if a given function element has any non-empty lists of input parameters, output parameters, or requirements by generating and inspecting a property map.
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
 * @summary The `updateState` method determines if the current selection is an action or a function and enables or disables itself based on this condition.
 */
	@Override
	public void updateState() {

		boolean enable = false;
		Element selectedNode = null;
		if(getSelected().size() > 0) {
			selectedNode = getFirstSelected().getElement();
		}
		StereotypeSearch checkstereotype= new StereotypeSearch();

		if ((selectedNode instanceof Action && selectedNode != null)||(checkstereotype.hasStereotypeBoolean(selectedNode, "Function"))) {
			enable = true;
		}
		setEnabled (enable);
	}
}
