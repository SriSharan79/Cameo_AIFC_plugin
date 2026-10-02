package elementDataExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.InputPin;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Relationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.Behavior;

import creationOfSafetyVP.NameDerivator;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ActionDataCollector` class is responsible for collecting and providing data related to system elements, inputs, outputs, and hierarchical levels from a SysML model.
 */
public class ActionDataCollector {


	StereotypeSearch checkstereotype=new StereotypeSearch();
	NameDerivator Name=new NameDerivator();
	int i = 0;

/**
 * @param selectedElement `selectedElement`: The specific SysML element from which to retrieve related system names.
 * @return The `GetRelatedSystemName` method returns a list of system names (`List<String>`) that are related to the given selected element. The returned list contains the names of systems identified by elements with the "PartProperty" stereotype connected through relationships to the specified element.
 * @summary Retrieve and return a list of related system names associated with a given SysML element.
 */
	public List<String> GetRelatedSystemName(Element selectedElement) {

		// TODO Auto-generated method stub
		List<String> syslist = new ArrayList<>(); // Initialize an empty list

		var relationships = selectedElement.get_relationshipOfRelatedElement();

		// Check if the relationships list is not null
		if (relationships != null) {
			for (Relationship el : relationships) {
				// Iterate through related elements
				for (Element rel : el.getRelatedElement()) {
					// Check if the element has the "System" stereotype
					if (checkstereotype.hasStereotypeBoolean(rel, "PartProperty")) {
						if (rel instanceof TypedElement)
							// Add the element's name to the system list
							syslist.add(Name.getName(((TypedElement) rel).getType()));
					}
				}
			}
		}
		// Return the list of systems (even if it's empty)
		//        Application.getInstance().getGUILog().showMessage(syslist.toString());
		return syslist;
	}

/**
 * @param selectedElement The `selectedElement` parameter of type `Element` in the method `GetRelatedSystemElement` represents the SysML element for which related system elements need to be identified and retrieved.
 * @return The return value of the `GetRelatedSystemElement` method is a list of elements (`List<Element>`) that are related to the given input element (`selectedElement`). The returned list includes either:
- Elements with the "System" stereotype directly related to the selected element,
- Types of elements with the "PartProperty" stereotype if they have a type, or
- If no direct relationships exist, it fetches elements from partitions related to the selected element.

This list represents the hierarchical or relational context of the input element within the SysML model.
 * @summary The `GetRelatedSystemElement` method retrieves a list of elements that are related to the selected element and have either "System" or "PartProperty" stereotypes.
 */
	public List<Element> GetRelatedSystemElement(Element selectedElement) {

		// TODO Auto-generated method stub
		List<Element> syslist = new ArrayList<>(); // Initialize an empty list

		var relationships = selectedElement.get_relationshipOfRelatedElement();

		// Check if the relationships list is not null
		if (relationships != null) {
			for (Relationship el : relationships) {
				// Iterate through related elements
				for (Element rel : el.getRelatedElement()) {
					// Check if the element has the "System" stereotype
					if (checkstereotype.hasStereotypeBoolean(rel, "PartProperty")) {
						if (rel instanceof TypedElement)
							// Add the element's name to the system list
							syslist.add(((TypedElement) rel).getType());
					}else if (checkstereotype.hasStereotypeBoolean(rel, "System")) {
						syslist.add(rel);
					}
				}
			}
		}
		if(syslist.isEmpty()) {
			syslist =GetsystemElementfromPartions(selectedElement);
		}

		return syslist;
	}

/**
 * @param selectedElement `selectedElement`: The specific SysML `Element` from which to retrieve partitioned system elements.
 * @return The return value of the `GetsystemElementfromPartions` method is a list of elements (`List<Element>`) that represents system elements derived from partitions. These can either be types associated with "PartProperty" or direct elements marked as "System".
 * @summary The method `GetsystemElementfromPartions` is responsible for retrieving and returning a list of elements from the partitions of a given selected element that are either tagged with the stereotype "PartProperty" or directly represent a system.
 */
	private List<Element> GetsystemElementfromPartions(Element selectedElement) {
		List<Element> syslist = new ArrayList<>(); // Initialize an empty list
		var PartionList= ((ActivityNode)selectedElement).getInPartition();
		for(ActivityPartition part:PartionList) 
		{
			var partion =part.getRepresents();
			if (checkstereotype.hasStereotypeBoolean(partion, "PartProperty")) {
				if (partion instanceof TypedElement)
					// Add the element's name to the system list
					syslist.add(((TypedElement) partion).getType());
			}else if (checkstereotype.hasStereotypeBoolean(partion, "System")) {
				syslist.add(partion);
			}


		}
		return syslist;

	}

/**
 * @param selectedFunctionAction The `selectedFunctionAction` parameter represents the action element from which input pins are retrieved to generate a list of input types.
 * @return The `getInputList` method returns a list of strings representing the names or types of input pins associated with a given function action.
 * @summary The method `getInputList` retrieves and returns a list of input pin names or types for the given selected function action in SysML models.
 */
	public List<String> getInputList(Element selectedFunctionAction) {
		List<String> pinTypes = new ArrayList<>();
		List<InputPin> inputPins = ((Action) selectedFunctionAction).getInput();

		for (InputPin pin : inputPins) {
			String pinName = Name.getName(pin);

			if (!pinName.equals("")) {
				pinTypes.add(pinName);
			} else {
				String typeName = Name.getName(((TypedElement) pin).getType());
				pinTypes.add(typeName);
			}
		}

		return pinTypes;
	}

/**
 * @param selectedFunctionAction The `selectedFunctionAction` parameter represents the action element from which to retrieve the list of input pins in the SysML model.
 * @return The return value for the `getInputListElement` method is a list of `Element` objects representing all input pins associated with a selected function action in the SysML model. This list is constructed by iterating over the input pins of the given function action and collecting them into an array list, which is then returned.
 * @summary This method retrieves a list of input pins associated with the specified function action.
 */
	public List<Element> getInputListElement(Element selectedFunctionAction) {
		List<Element> pinTypes = new ArrayList<>();
		List<InputPin> inputPins = ((Action) selectedFunctionAction).getInput();

		for (InputPin pin : inputPins) {
			pinTypes.add(pin);
		}

		return pinTypes;
	}

/**
 * @param selectedFunctionAction `selectedFunctionAction`: The specific action element from which the output pins are to be retrieved.
 * @return The return value of the `getOutputListElement` method is a list of output pins associated with the provided action element, representing the outputs that need to be considered for further safety assessment.
 * @summary The `getOutputListElement` method retrieves and returns a list of output pins associated with the given selected function action.
 */
	public List<Element> getOutputListElement(Element selectedFunctionAction) {
		List<Element> pinTypes = new ArrayList<>();
		List<OutputPin> inputPins = ((Action) selectedFunctionAction).getOutput();

		for (OutputPin pin : inputPins) {
			pinTypes.add(pin);
		}

		return pinTypes;
	}

/**
 * @param selectedFunctionAction The `selectedFunctionAction` parameter in the `getOutputList` method is an Element object representing the selected action from which output pins will be retrieved and processed.
 * @return The `getOutputList` method returns a list of strings representing the names of output pins for a given action element. If a pin has no name, it includes the type's name instead.
 * @summary The `getOutputList` method retrieves the names of output pins associated with a given function action, returning them as a list of strings.
 */
	public List<String> getOutputList(Element selectedFunctionAction) {
		List<String> pinTypes = new ArrayList<>();
		List<OutputPin> inputPins = ((Action) selectedFunctionAction).getOutput();

		for (OutputPin pin : inputPins) {
			String pinName = Name.getName(pin);

			if (!pinName.equals("")) {
				pinTypes.add(pinName);
			} else {
				String typeName = Name.getName(((TypedElement) pin).getType());
				pinTypes.add(typeName);
			}
		}

		return pinTypes;
	}

/**
 * @param selectedfunctionAction The `selectedfunctionAction` parameter represents the currently selected function element from which to retrieve its parent or higher-level elements in the SysML model.
 * @return The `getUpperLvlListElement` method returns a list of elements that are at the next higher level in the hierarchy than the provided `selectedfunctionAction`. This list includes the parent element (owner) of the given function action.
 * @summary The `getUpperLvlListElement` method identifies and retrieves the immediate upper-level element associated with a given function action in the SysML model.
 */
	public List<Element> getUpperLvlListElement(Element selectedfunctionAction) {

		// TODO Auto-generated method stub

		List<Element> upperfunctions = new ArrayList<>();
		upperfunctions.add(selectedfunctionAction.getOwner());
		return upperfunctions;
	}

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML element currently selected for analysis in the `getLowerLvlListElement` method.
 * @return The `getLowerLvlListElement` method returns a list of elements that are lower-level functions associated with the given `selectedNode`. These elements are identified by checking if they have a specific stereotype, ensuring only relevant lower-level functions are included in the returned list.
 * @summary Retrieves a list of lower-level functions from the elements owned by a behavior associated with a CallBehaviorAction.
 */
	public List<Element> getLowerLvlListElement(Element selectedNode) {
		List<Element> Lowerfunctions = new ArrayList<>();
		if (selectedNode instanceof CallBehaviorAction) {		
			// Get the behavior of the CallBehaviorAction
			Behavior function = ((CallBehaviorAction) selectedNode).getBehavior();


			if(function!=null) {
				Collection<Element> ownedelements = function.getOwnedElement();
				for(Element el:ownedelements) {
					if (el instanceof CallBehaviorAction) {

						Behavior elfunction = ((CallBehaviorAction) el).getBehavior();
						if (checkstereotype.hasStereotypeBoolean(elfunction, "Function"))
						
						Lowerfunctions.add(elfunction);

					}
				}
			}
		}

		return Lowerfunctions;
	}


}
