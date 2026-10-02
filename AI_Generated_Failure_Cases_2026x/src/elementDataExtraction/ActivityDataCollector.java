package elementDataExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DirectedRelationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Relationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.Behavior;

import creationOfSafetyVP.NameDerivator;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ActivityDataCollector` class is responsible for collecting and organizing data related to activities, inputs, outputs, system elements, requirements, and actions within a SysML model.
 */
public class ActivityDataCollector {

	StereotypeSearch checkstereotype= new StereotypeSearch();
	NameDerivator Name = new NameDerivator();

/**
 * @param selectedElement The `selectedElement` parameter represents the SysML element currently selected for analysis in the context of retrieving related systems.
 * @return The `GetRelatedSystem` method returns a `List<String>` that contains the names of all elements with the "System" stereotype related to the input `selectedElement`. The method iterates through the relationships of the selected element and checks for the "System" stereotype in related elements. If such an element is found, its name is added to the list. This list can be empty if no related systems are found.
 * @summary The `GetRelatedSystem` method retrieves and returns a list of system names that are related to the given SysML element, specifically focusing on elements with the "System" stereotype.
 */
	public List<String> GetRelatedSystem(Element selectedElement) {
		List<String> syslist = new ArrayList<>(); // Initialize an empty list


		// Check if the selected element is an instance of Activity
		if (selectedElement instanceof Activity) {
			// Retrieve the list of relationships
			var relationships = selectedElement.get_relationshipOfRelatedElement();

			// Check if the relationships list is not null
			if (relationships != null) {
				for (Relationship el : relationships) {
					// Iterate through related elements
					for (Element rel : el.getRelatedElement()) {
						// Check if the element has the "System" stereotype
						if (checkstereotype.hasStereotypeBoolean(rel, "System")) {
							// Add the element's name to the system list
							syslist.add(Name.getName(rel));
						}
					}
				}
			}
		}

		// Return the list of systems (even if it's empty)
		return syslist;
	}

/**
 * @param selectedElement The `selectedElement` parameter represents the Activity from which input parameters are extracted.
 * @return The `getInputList` method returns a list of input parameter names. The list contains the names of all parameters with an "in" direction that are associated with the specified activity element, ensuring that only non-empty parameter names are included in the returned list.
 * @summary The `getInputList` method retrieves a list of input parameter names from the specified SysML Activity element.
 */
	public List<String> getInputList(Activity selectedElement) {

		List<String> values =new ArrayList<>();

		var parameters = selectedElement.getOwnedParameter();
		for(Parameter par : parameters) {
			String paramDirection = par.getDirection().toString();
			if(paramDirection.equals("in")) {
				String parname = Name.getName(par);
				if (!parname.isEmpty()) {
					values.add(parname);
				} 
			}
		}
		return values;

	}

/**
 * @param selectedElement The `selectedElement` parameter in the method `getOutputList(Activity selectedElement)` is an Activity object representing the SysML model element from which output parameters are being extracted.
 * @return The `getOutputList` method returns a list of parameter names that are marked as outputs for the given `Activity`.
 * @summary The `getOutputList` method extracts and returns a list of output parameter names from the given SysML activity element.
 */
	public List<String> getOutputList(Activity selectedElement) {
		// TODO Auto-generated method stub
		List<String> values =new ArrayList<>();

		var parameters = selectedElement.getOwnedParameter();
		for(Parameter par : parameters) {
			String paramDirection = par.getDirection().toString();
			if(paramDirection.equals("out")) {
				String parname = Name.getName(par);
				if (!parname.isEmpty()) {
					values.add(parname);
				} 
			}

		}
		return values;
	}



/**
 * @param selectedElement `selectedElement`: The `Activity` instance whose input parameters are to be extracted and returned as a list of elements.
 * @return The `getInputListElement` method returns a list of input parameters from the specified activity element. These input parameters are represented as instances of the `Element` class and are filtered based on their direction, ensuring only those with an "in" direction are included in the returned list.
 * @summary The `getInputListElement` method retrieves and returns a list of input parameters for a given activity element.
 */
	public List<Element> getInputListElement(Activity selectedElement) {
		List<Element> values =new ArrayList<>();

		var parameters = selectedElement.getOwnedParameter();
		for(Parameter par : parameters) {
			String paramDirection = par.getDirection().toString();
			if(paramDirection.equals("in")) {
				values.add(par); 
			}
		}
		return values;
	}



/**
 * @param selectedElement The `selectedElement` parameter represents the specific activity from which to retrieve output list elements.
 * @return The `getOutputListElement` method returns a list of `Element` objects that represent the output parameters of the given selected activity element. These are the parameters which have their direction set to "out".
 * @summary The `getOutputListElement` method retrieves a list of output parameters from the provided activity element.
 */
	public List<Element> getOutputListElement(Activity selectedElement) {
		List<Element> values =new ArrayList<>();

		var parameters = selectedElement.getOwnedParameter();
		for(Parameter par : parameters) {
			String paramDirection = par.getDirection().toString();
			if(paramDirection.equals("out")) {
				values.add(par); 
			}
		}
		return values;
	}



/**
 * @param selectedElement The `selectedElement` parameter represents the currently selected SysML element for which related system elements are to be retrieved.
 * @return The `GetRelatedSystemElement` method returns a list of elements that are related to the given selected element, specifically those with the "PartProperty" or "System" stereotypes. The list includes system elements directly related through relationships and their types if they are instances of `TypedElement`.
 * @summary The method `GetRelatedSystemElement` retrieves a list of elements related to the selected element that have either "PartProperty" or "System" stereotypes.
 */
	public List<Element> GetRelatedSystemElement(Element selectedElement) {
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
		// Return the list of systems (even if it's empty)
		//        Application.getInstance().getGUILog().showMessage(syslist.toString());
		return syslist;
	}

/**
 * @param selectedElement The `selectedElement` parameter in the `GetRelatedRequirementsElement` method represents the SysML element from which related requirements elements are to be identified and retrieved.
 * @return The `GetRelatedRequirementsElement` method returns a list of `Element` objects that are related to the input `selectedElement`. Specifically, it collects elements that have the "Requirement" stereotype from the relationships associated with the selected element. This list is intended for further processing or analysis within the context of Functional Hazard Assessment (FHA).
 * @summary The `GetRelatedRequirementsElement` method retrieves and returns a list of elements that are related to the given element and have the "Requirement" stereotype.
 */
	public List<Element> GetRelatedRequirementsElement(Element selectedElement) {

		List<Element> reqlist = new ArrayList<>(); // Initialize an empty list

		var relationships = selectedElement.get_relationshipOfRelatedElement();

		// Check if the relationships list is not null
		if (relationships != null) {
			for (Relationship el : relationships) {
				// Iterate through related elements
				for (Element rel : el.getRelatedElement()) {
					// Check if the element has the "Requirement" stereotype
					if (checkstereotype.hasRequirementStereotype(rel)) {
						reqlist.add(rel);
//						Application.getInstance().getGUILog().showMessage(Name.getName(rel));
					}
				}
			}
			return reqlist;
		}else {

			return reqlist;
		}
	}
/**
 * @param function The parameter `function` of type `Element` is the SysML function element from which lower-level functions or behaviors are extracted.
 * @return The return value of the `getLowerLevelListElement` method is a list of elements that represent lower-level functions or behaviors associated with the given input element. These elements are identified based on specific stereotypes and relationships defined within the SysML model, providing a hierarchical breakdown of functional components for hazard assessment purposes.
 * @summary The `getLowerLevelListElement` method collects and returns a list of elements that are direct lower-level functions or related through lower-level relationships for a given function element.
 */
	public List<Element> getLowerLevelListElement(Element function) {
		List<Element> Lowerfunctions = new ArrayList<>();
		

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

		Lowerfunctions.addAll(lowerlevel_relations(function));
		return Lowerfunctions;
	}



	// Returns a list of elements that own a CallBehaviorAction referencing the selected function
/**
 * @param selectedFunction The `selectedFunction` parameter is the currently chosen function from which to retrieve its direct parent elements in the hierarchy.
 * @return The `getUpperLevelListElement` method returns a list of elements that represent the immediate higher-level containers or owners of the given selected function within the SysML model. This includes direct owners from CallBehaviorActions and additional related elements obtained through the `upperlevel_relations` method. The returned list is used to identify the contextually relevant upper-level components for functional hazard assessment purposes in aircraft system modeling.
 * @summary The `getUpperLevelListElement` method retrieves a list of elements that are one level above the selected function in the system hierarchy.
 */
	public List<Element> getUpperLevelListElement(Element selectedFunction) {
		List<Element> upperLevelList = new ArrayList<>(); // List to store results
		List<Element> allActions = getAllActions(); // Retrieve all actions in the project

		for (Element action : allActions) {
			// Check if the action is a CallBehaviorAction
			if (action instanceof CallBehaviorAction) {
				Element behaviorElement = (Element) ((CallBehaviorAction) action).getBehavior();

				// Compare behavior with the selected function
				if (behaviorElement == selectedFunction) {
					// Add the owner of this action to the upperLevelList
					Element owner = action.getOwner();
					if (owner != null) { // Null safety check
						upperLevelList.add(owner);
					}
				}
			}
		}
		upperLevelList.addAll(upperlevel_relations(selectedFunction));
		return upperLevelList;
	}

	private Collection<? extends Element> upperlevel_relations(Element selectedFunction) {
		// TODO Auto-generated method stub

		List<Element> upperLevelList = new ArrayList<>(); // List to store results
		var targetrelations =selectedFunction.get_directedRelationshipOfTarget();
		for(DirectedRelationship tr:targetrelations) {
			var Source_collection=tr.getSource();
			for(Element el:Source_collection) {
				if(checkstereotype.hasStereotypeBoolean(el, "Function")) {
					upperLevelList.add(el);
				}
			}
		}
		return upperLevelList;
	}
	
	private Collection<? extends Element> lowerlevel_relations(Element selectedFunction) {
		List<Element> lowerLevelList = new ArrayList<>(); // List to store results
		var targetrelations =selectedFunction.get_directedRelationshipOfSource();
		for(DirectedRelationship tr:targetrelations) {
			var Source_collection=tr.getTarget();
			for(Element el:Source_collection) {
				if(checkstereotype.hasStereotypeBoolean(el, "Function")) {
					lowerLevelList.add(el);
				}
			}
		}
		return lowerLevelList;
	}

	// Retrieves all actions recursively from the project
/**
 * @return The `getAllActions` method returns a list of `Element` objects representing all the action elements in the project's top-level SysML models. If there is no current project, it returns an empty list.
 * @summary The `getAllActions` method retrieves and returns a list of all action elements from the top-level models within the current project.
 */
	public List<Element> getAllActions() {
		List<Element> allActions = new ArrayList<>();

		// Retrieve the current project
		Project project = Application.getInstance().getProject();
		if (project == null) {
			return allActions; // Return empty list if project is null
		}

		// Retrieve all top-level models in the project
		List<Package> models = project.getModels();

		// Process each model to collect actions
		for (Package model : models) {
			Element modelElement = (Element) model;
			allActions.addAll(collectActionElements(modelElement));
		}
		return allActions;
	}

	// Recursively collects all Action elements from a given element
/**
 * @param element The `element` parameter represents the current SysML model element being processed to identify and collect action elements.
 * @return The `collectActionElements` method returns a list of `Element` objects that represent all the actions found within the specified SysML model element, including nested elements. If the input element is null or contains no actions, it returns an empty list.
 * @summary The method `collectActionElements` recursively collects all action elements within a given SysML element.
 */
	public List<Element> collectActionElements(Element element) {
		List<Element> actions = new ArrayList<>();

		if (element == null) {
			return actions; // Return empty list if element is null
		}

		// If the element is an Action, add it to the list
		if (element instanceof Action) {
			actions.add(element);
		}

		// Recursively process owned elements
		for (Element ownedElement : element.getOwnedElement()) {
			actions.addAll(collectActionElements(ownedElement));
		}
		return actions;
	}
}
