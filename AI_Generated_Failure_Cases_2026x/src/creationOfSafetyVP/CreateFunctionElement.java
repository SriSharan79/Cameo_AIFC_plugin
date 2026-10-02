package creationOfSafetyVP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import elementDataExtraction.StereotypeFinder;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `CreateFunctionElement` class is responsible for adding and managing various properties such as descriptions, systems, failure types, phases, and IDs to elements within a SysML model.
 */
public class CreateFunctionElement {

	Project project = Application.getInstance().getProject();
	Profile profile = StereotypesHelper.getProfile(project, "HADES");
	Stereotype FctnStereotype = StereotypesHelper.getStereotype(project, "Function", profile);
	StereotypeFinder PF = new StereotypeFinder();
	Stereotype functionStereotype = PF.findstereotype("Function");

	ElementFinder_SVP elementfinder =new ElementFinder_SVP();

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML model element to which a new description will be added.
 * @param description The `description` parameter in the `AddDescription` method is a text string that represents the description to be added to the specified SysML element.
 * @summary The Java method `AddDescription` is responsible for setting the description property of a given SysML model element using the specified stereotype.
 */
	public void AddDescription(Element selectedNode, String description) {
		StereotypesHelper.setStereotypePropertyValue(selectedNode, functionStereotype, "Description", description);
	}

/**
 * @param selectedNode The `selectedNode` parameter in the `AddSystems` method is an instance of the Element class representing the SysML model node to which system blocks will be associated.
 * @param systems The parameter `systems` is a list of system names to be added as properties to the selected SysML model node.
 * @summary The `AddSystems` method is responsible for adding a list of systems to a selected SysML model element by setting the appropriate stereotype property.
 */
	public void AddSystems(Element selectedNode, List<String> systems) {
		// TODO Auto-generated method stub
		var FTs = elementfinder.getElementsByNames(systems, elementfinder.FindAllSystemBlocks(), elementfinder.FindAllSystemNames());
		StereotypesHelper.setStereotypePropertyValue(selectedNode, functionStereotype, "System", FTs);

	}

/**
 * @param selectedNode The `Element` parameter `selectedNode` in the `AddFailuretypes` method represents the SysML model element to which failure types will be added.
 * @param failureTypes The `failureTypes` parameter is a list of failure type strings to be added as default failure types for the selected SysML element.
 * @summary The `AddFailuretypes` method is responsible for adding specified failure types to a given element in the model by checking and adding values using predefined properties.
 */
	public void AddFailuretypes(Element selectedNode, List<String> failureTypes) {
		// TODO Auto-generated method stub
		var FTs = elementfinder.getElementsByNames(failureTypes, elementfinder.FindAllFailure(), elementfinder.FindAllFailureNames());
		var FTProperty = StereotypesHelper.getPropertyByName(functionStereotype, "Default Failure Type");
		CheckandAddvalues(FTs,FTProperty,selectedNode,"Default Failure Type");

	}
/**
 * @param selectedNode The `selectedNode` parameter in the `AddPhases` method is an Element that represents the SysML model element to which the phases will be added.
 * @param phases The `phases` parameter is a list of strings representing the names of phases to be added as properties in the selected element's stereotype.
 * @summary The `AddPhases` method is responsible for adding specified phases to a selected node in the model by retrieving phase elements and associating them with the node's phase property.
 */
	public void AddPhases(Element selectedNode, List<String> phases) {
		// TODO Auto-generated method stub
		var FTs = elementfinder.getElementsByNames(phases, elementfinder.FindAllPhases(), elementfinder.FindAllPhaseNames());
		var FTProperty = StereotypesHelper.getPropertyByName(functionStereotype, "Phase");
		CheckandAddvalues(FTs,FTProperty,selectedNode,"Phase");
	}

/**
 * @param list The `List<Element>` parameter named `list` represents a collection of elements that are intended to be checked and potentially added to an existing list associated with the given SysML element within the context of Functional Hazard Assessment.
 * @param fTProperty The `fTProperty` parameter in the `CheckandAddvalues` method represents a specific property from which to retrieve and update values for the given `selectedNode`.
 * @param selectedNode The `selectedNode` parameter of type `Element` in the method `CheckandAddvalues` represents the specific SysML model element to which failure conditions will be added or updated.
 * @param PropertyName A string representing the name of a property to be checked and added.
 * @summary The `CheckandAddvalues` method is responsible for merging a list of elements with an existing list obtained from a selected node's stereotype property and updating the node with the combined list.
 */
	private void CheckandAddvalues(List<Element> list, Property fTProperty, Element selectedNode, String PropertyName) {
		// TODO Auto-generated method stub
		if (list!=null&&!list.isEmpty()) {
			var existinglist = StereotypesHelper.getStereotypePropertyValue(selectedNode, fTProperty);
			List<Element> combinedlist = MergeLists(list,existinglist);
			StereotypesHelper.setStereotypePropertyValue(selectedNode, functionStereotype, PropertyName, combinedlist);
		}
	}

/**
 * @param list `list`: A `List<Element>` containing elements that need to be merged with an existing list.
 * @param existinglist A parameter `existinglist` is an additional collection of elements that may contain objects of compatible types, intended to be merged with the primary list while ensuring no duplicates.
 * @return The `MergeLists` method returns a `List<Element>` that is a merged and deduplicated combination of two input lists: the original list and an existing list. The returned list contains elements from both lists without duplicates, ensuring only those elements of matching type to the original list are included.
 * @summary The `MergeLists` method merges two lists of elements, ensuring no duplicates and maintaining type consistency with the original list.
 */
	public List<Element> MergeLists(List<Element> list, List<?> existinglist) {
		// Use a set to ensure no duplicates
		Set<Element> resultSet = new HashSet<>(list);

		// Iterate over the existinglist, adding only elements that match the type of list
		for (Object item : existinglist) {
			if (item != null && list.get(0).getClass().isInstance(item)) {
				Element castedItem = (Element) item;
				resultSet.add(castedItem);
			}
		}

		// Convert the set back to a list and return
		return new ArrayList<>(resultSet);
	}

/**
 * @param selectedNode The parameter `selectedNode` of type `Element` in the `AddID` method represents the SysML model element to which a new ID will be assigned.
 * @param iD The `iD` parameter represents the unique identifier to be assigned to the specified element in the SysML model.
 * @summary The `AddID` method assigns a given ID to the specified SysML model element.
 */
	public void AddID(Element selectedNode, String iD) {
		// TODO Auto-generated method stub

		StereotypesHelper.setStereotypePropertyValue(selectedNode, functionStereotype, "ID", iD);
	}


}
