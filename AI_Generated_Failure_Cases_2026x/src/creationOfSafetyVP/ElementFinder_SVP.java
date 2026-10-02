package creationOfSafetyVP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State;

import elementDataExtraction.StereotypeSearch;



/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ElementFinder_SVP` class is primarily responsible for locating and retrieving various elements and their corresponding names within a SysML model, focusing on system blocks, phases, failures, and hazards.
 */
public class ElementFinder_SVP {

	NameDerivator ND = new NameDerivator();

	List<Element> Systems = new ArrayList<>();
	List<Element> Phases = new ArrayList<>();
	List<Element> Failures = new ArrayList<>();
	List<Element> Hazards = new ArrayList<>();

/**
 * @return The `FindAllSystemBlocks` method returns a list of all system blocks present in the project's SysML models. This includes all elements that are identified as systems within the scope of the project.
 * @summary The `FindAllSystemBlocks` method retrieves all system blocks from the project by iterating through each model and collecting instances of system elements.
 */
	public List<Element> FindAllSystemBlocks() {
		Project project = Application.getInstance().getProject();
		List<Package> models =project.getModels();
		for(Package model: models) {
			List <Element> systemsList = getAllinstancesofSystems(model);
			Systems.addAll(systemsList);
		}
		return Systems;
	}
	
/**
 * @return The return value of the `FindAllSystemNames` method is a list of string names representing all identified systems. This list is compiled from system blocks found in the model, with each entry being the name of an individual system.
 * @summary The `FindAllSystemNames` method is responsible for retrieving and returning a list of all system names from the SysML model.
 */
	public List<String> FindAllSystemNames() {	
		
		List<String> Systems_names= new ArrayList<>();
		Systems = FindAllSystemBlocks();		
		Systems_names =getAllNames(Systems);
		return Systems_names;
	}
	
/**
 * @return The `FindAllPhases` method retrieves all the state elements representing phases from a project, aggregating them into a list. The return value is a `List<Element>` that contains these phase elements.
 * @summary The `FindAllPhases` method retrieves all phase elements from the project's SysML models.
 */
	public List<Element> FindAllPhases() {
		Project project = Application.getInstance().getProject();
		List<Package> models =project.getModels();
		for(Package model: models) {
			List <Element> phasesList = getAllinstancesofStates(model);
			Phases.addAll(phasesList);
		}
		return Phases;
	}
	
/**
 * @return The method `FindAllPhaseNames` returns a list of phase names extracted from the SysML model. These phase names are relevant to the functional hazard assessment (FHA) process and represent different operational phases of the aircraft system being analyzed.
 * @summary The `FindAllPhaseNames` method retrieves a list of all phase names from the system model.
 */
	public List<String> FindAllPhaseNames() {
		
		List<String> Phases_names= new ArrayList<>();
		Phases = FindAllPhases();
		Phases_names =getAllNames(Phases);
		return Phases_names;
	}
	
/**
 * @return The return value of the `FindAllFailure` method is a list of elements representing all failure types identified within the project's models. This collection aggregates failure instances across multiple SysML packages, providing a comprehensive overview for further analysis and refinement in Functional Hazard Assessment (FHA).
 * @summary The `FindAllFailure` method identifies and compiles all instances of the "Failure type" enumeration across the models within a project.
 */
	public List<Element> FindAllFailure() {
		Project project = Application.getInstance().getProject();
		List<Package> models =project.getModels();
		for(Package model: models) {
			List<Element> FailureList = getAllinstancesofEnumeration(model,"Failure type");
			Failures.addAll(FailureList);
		}
		return Failures;
	}
	
/**
 * @return The `FindAllFailureNames` method returns a list of strings representing the names of all failure conditions identified within the SysML model.
 * @summary The `FindAllFailureNames` method retrieves and returns a list of all failure names from the system model.
 */
	public List<String> FindAllFailureNames() {
		
		List<String> Failure_names= new ArrayList<>();
		Failures = FindAllFailure();
		Failure_names =getAllNames(Failures);
		return Failure_names;
	}
	

/**
 * @return The `FindAllHazard` method returns a list of elements representing all hazards identified within the project models. Each element in this list corresponds to an instance of the "Hazard Classification" enumeration found across different SysML packages within the project.
 * @summary The `FindAllHazard` method retrieves all instances of elements with the "Hazard Classification" enumeration across all models in the current project.
 */
	public List<Element> FindAllHazard() {
		Project project = Application.getInstance().getProject();
		List<Package> models =project.getModels();
		for(Package model: models) {
			List<Element> FailureList = getAllinstancesofEnumeration(model,"Hazard Classification");
			Hazards.addAll(FailureList);
		}
		return Hazards;
	}
	
/**
 * @return The `FindAllHazardNames` method returns a list of all hazard names identified within the system model. Each element in this list is a string representing the name of a specific hazard.
 * @summary The method `FindAllHazardNames` retrieves and returns a list of all hazard names from the system model.
 */
	public List<String> FindAllHazardNames() {
		
		List<String> Failure_names= new ArrayList<>();
		Hazards = FindAllHazard();
		Failure_names =getAllNames(Hazards);
		return Failure_names;
	}

/**
 * @param Elements The `Elements` parameter is a list of `Element` objects whose names are to be extracted and returned.
 * @return The `getAllNames` method returns a `List<String>` containing the names of all elements in the input list. If an element is an instance of Class, it adds its name directly; otherwise, it retrieves and adds the name using a helper function.
 * @summary The `getAllNames` method extracts and returns a list of names from a given list of Elements, handling both Class instances and other element types using different name retrieval strategies.
 */
	private List<String> getAllNames(List<Element> Elements) {
		List<String>  ElementNamesList = new ArrayList<>();
		
		for(Element el: Elements) {
			if (el instanceof Class) {
				ElementNamesList.add(((Class)el).getName());
			}
			else {
				ElementNamesList.add(ND.getName(el));
			}
		}
		return ElementNamesList;
	}

/**
 * @param element The `element` parameter is the root SysML model element from which to recursively find all instances of system classes.
 * @return The `getAllInstancesOfSystems` method returns a list of all instances of system elements within a given SysML model. Each item in the returned list is an instance of Element that represents a system, as identified by the 'System' stereotype.
 * @summary This method recursively collects all instances of SysML classes with the 'System' stereotype starting from a given element.
 */
	private List<Element> getAllinstancesofSystems(Element element) {
	    List<Element> systemsList = new ArrayList<>();
	    var stereotypeChecker = new StereotypeSearch();

	    var ownedElements = element.getOwnedElement();

	    if (ownedElements != null) {
	        for (Element ownedElement : ownedElements) {
	            // Check if the owned element is a Class and has the 'System' stereotype
	            if (ownedElement instanceof Class && stereotypeChecker.hasStereotypeBoolean(ownedElement, "System")) {
	                systemsList.add(ownedElement);
	            }
	            // Recursively check the owned elements of the current owned element
	            systemsList.addAll(getAllinstancesofSystems(ownedElement));  // Collect recursive results
	        }
	    }
	    return systemsList;
	}

/**
 * @param element The `element` parameter represents the SysML model element from which to recursively gather all instances of states that have the 'Phase' stereotype.
 * @return The `getAllinstancesofStates` method returns a list of `Element` objects that represent all instances of `State` elements within the specified `element`, which are stereotyped as 'Phase'. This includes both direct and nested owned elements.
 * @summary The method `getAllInstancesOfStates` recursively collects all instances of `State` elements with the 'Phase' stereotype from a given SysML element and its sub-elements.
 */
	private List<Element> getAllinstancesofStates(Element element) {
	    List<Element> statesList = new ArrayList<>();
	    var stereotypeChecker = new StereotypeSearch();

	    var ownedElements = element.getOwnedElement();

	    if (ownedElements != null) {
	        for (Element ownedElement : ownedElements) {
	            // Check if the owned element is a Class and has the 'Phase' stereotype
	            if (ownedElement instanceof State && stereotypeChecker.hasStereotypeBoolean(ownedElement, "Phase")) {
	            	statesList.add(ownedElement);
	            }
	            // Recursively check the owned elements of the current owned element
	            statesList.addAll(getAllinstancesofStates(ownedElement));  // Collect recursive results
	        }
	    }
	    
	    return statesList;
	}
	
/**
 * @param element The `element` parameter represents the current SysML element being processed to find all instances of a specific enumeration.
 * @param Name The `Name` parameter in the method `getAllinstancesofEnumeration` specifies the name of the Enumeration to search for within the given Element.
 * @return The `getAllinstancesofEnumeration` method returns a `List<Element>` containing all instances of `EnumerationLiteral` elements that are part of an enumeration with the specified name within the given element and its descendants. This list represents the failure conditions extracted from the SysML model, which are essential for performing Functional Hazard Assessment (FHA).
 * @summary The `getAllinstancesofEnumeration` method retrieves all instances of a specified enumeration type from the given SysML element and its children.
 */
	private List<Element> getAllinstancesofEnumeration(Element element, String Name) {
	    List<Element> FailuresList = new ArrayList<>();

	    // Get all owned elements of the current element
	    Collection<Element> ownedElements = element.getOwnedElement();

	    if (ownedElements != null) {
	        for (Element ownedElement : ownedElements) {
	            // Check if this owned element is the required Enumeration
	            if (ownedElement instanceof Enumeration &&
	                ND.getName(ownedElement).equals(Name)) {
	                
	                Collection<Element> FailuresLiterals = ownedElement.getOwnedElement();
	                
	                for (Element literal : FailuresLiterals) {
	                    if (literal instanceof EnumerationLiteral) {
	                        FailuresList.add(literal);
	                    }
	                }
	            }

	            // Recursively process children only if there are owned elements
	            if (ownedElement.getOwnedElement() != null) {
	                FailuresList.addAll(getAllinstancesofEnumeration(ownedElement, Name));
	            }
	        }
	    }
	    return FailuresList;
	}


	
/**
 * @param names The `names` parameter is a list of strings representing the names of elements to be searched for within the `elementList`.
 * @param elementList The `elementList` parameter of type `List<Element>` in method `getElementsByNames` contains the collection of elements to be searched for matching names.
 * @param namesList The parameter `namesList` of type `List<String>` in the method `getElementsByNames` represents the list of names associated with each element in the `elementList`, used for matching and retrieving corresponding elements.
 * @return The method `getElementsByNames` returns a `List<Element>` containing elements from the input `elementList` that have names matching those specified in the `names` list. Each element is matched using case-insensitive comparison, and only unique matches are included in the returned list.
 * @summary The `getElementsByNames` method retrieves elements from an input list based on their corresponding names in another list.
 */
	public List<Element> getElementsByNames(List<String> names, List<Element> elementList, List<String> namesList) {
	    // Check for null input
	    if (names == null || elementList == null || namesList == null) {
	        throw new IllegalArgumentException("Input lists must not be null.");
	    }

	    // Ensure that the elementList and namesList are of the same size
	    if (elementList.size() != namesList.size()) {
	    	Application.getInstance().getGUILog().showMessage("Element list and names list sizes do not match.");
	    }

	    List<Element> matchingElements = new ArrayList<>();

	    // Iterate over the input names list
	    for (String name : names) {
	        // For each name, find the corresponding element
	        for (int i = 0; i < namesList.size(); i++) {
	            // Ensure namesList item is not null before calling equals
	            if (namesList.get(i) != null && namesList.get(i).equalsIgnoreCase(name)) {
	                matchingElements.add(elementList.get(i));  // Add the matching element to the result list
	                break;  // Move to the next name once a match is found
	            }
	        }
	    }

	    return matchingElements;
	}
	
/**
 * @param names The `names` parameter in the `getElementsByNames` method is a list of string identifiers used to filter and retrieve corresponding elements from an input element list.
 * @param elementList `elementList` is a collection of `Element` objects used to search for and retrieve elements that match specific names.
 * @return The return value of `getElementsByNames` is a `List<Element>` containing elements from `elementList` whose names (case-insensitive) match any in the provided `names` list.
 * @summary This method retrieves a list of elements from an input list that match given names, ignoring case sensitivity.
 */
	public List<Element> getElementsByNames(List<String> names, List<Element> elementList){
	    List<Element> matchingElements = new ArrayList<>();
	    // Iterate over the input names list
	    for (String name : names) {
	        // For each name, find the corresponding element
	        for (int i = 0; i < elementList.size(); i++) {
	            // Ensure namesList item is not null before calling equals
	            if (elementList.get(i) != null && ND.getName(elementList.get(i)).equalsIgnoreCase(name)) {
	                matchingElements.add(elementList.get(i));  // Add the matching element to the result list
	                break;  // Move to the next name once a match is found
	            }
	        }
	    }
		
		return matchingElements;
		
	}



}
