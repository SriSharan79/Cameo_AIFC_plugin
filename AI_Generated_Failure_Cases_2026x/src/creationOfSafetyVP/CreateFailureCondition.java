package creationOfSafetyVP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import elementDataExtraction.PropertiesFunction;
import elementDataExtraction.StereotypeFinder;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `CreateFailureCondition` class is responsible for generating and managing failure conditions in SysML models by creating, updating, and checking properties of failure condition elements.
 */
public class CreateFailureCondition {
	String FCName = "";
	String FCDescription = "";
	String FCid = "";
	String FCeffectHLS = "";
	String FCeffectAC = "";
	String FCeffectCW = "";
	String FCeffectOCC = "";
	Project project = Application.getInstance().getProject();
	StereotypeFinder PF = new StereotypeFinder();
	Stereotype FCStereotype =  PF.findstereotype( "Failure condition");
	ElementFinder_SVP elementfinder =new ElementFinder_SVP();
	NameDerivator ND= new NameDerivator();
	SearchSafetyVP SafetyVP = new SearchSafetyVP();
	ElementCreation elementCreator = new ElementCreation();

/**
 * @param edited_FCName The `edited_FCName` parameter in the method `CreateFailureCondition` represents the name of the failure condition that has been modified or refined.
 * @param edited_FCDescription The `edited_FCDescription` parameter contains the updated descriptive text for a failure condition.
 * @return The `CreateFailureCondition` method returns an instance of the `CreateFailureCondition` class. This instance encapsulates a newly created failure condition with the specified name and description, which is used in the Functional Hazard Assessment (FHA) process for aircraft systems within the OSIRIS plugin.
 * @summary Creates and initializes a new failure condition with the given name and description.
 */
	public CreateFailureCondition(String edited_FCName, String edited_FCDescription) {
		this.FCName = edited_FCName;
		this.FCDescription = edited_FCDescription;
	}

/**
 * @param edited_FCName The `edited_FCName` parameter represents the user-provided name for the failure condition being created.
 * @param edited_FCDescription The `edited_FCDescription` parameter represents the updated description for a failure condition in OSIRIS.
 * @param fcId The `fcId` parameter is a unique identifier for the failure condition being created.
 * @return The return value of the `CreateFailureCondition` method is an instance of the `CreateFailureCondition` class. This instance represents a newly created failure condition with specified attributes such as name, description, and identifier, which are essential for functional hazard assessment in aircraft systems within OSIRIS.
 * @summary This method initializes a new failure condition with the given name, description, and identifier.
 */
	public CreateFailureCondition(String edited_FCName, String edited_FCDescription, String fcId) {
		this.FCName = edited_FCName;
		this.FCDescription = edited_FCDescription;
		this.FCid = fcId;
	}

/**
 * @param propertyMapStrings The `propertyMapStrings` parameter is a map containing string properties required to create and define a failure condition.
 * @return The `CreateFailureCondition` method returns an instance of the `CreateFailureCondition` class, which represents a failure condition in the FHA process. This object encapsulates key attributes such as the name, description, ID, and effect associated with the failure condition, as derived from the provided property map strings.
 * @summary The `CreateFailureCondition` method initializes a failure condition object using properties provided in a map.
 */
	public CreateFailureCondition(Map<String, String> propertyMapStrings) {
		// TODO Auto-generated constructor stub
		this.FCName =propertyMapStrings.get("Name");

		this.FCDescription =propertyMapStrings.get("Description");

		this.FCid =propertyMapStrings.get("ID");

		this.FCeffectAC =propertyMapStrings.get("Effect");
		
	}

/**
 * @param propertyMapStrings A mapping of failure condition properties to their respective string values.
 * @param fcId The `fcId` parameter is a unique identifier for the failure condition to be created, facilitating its tracking and management within the system.
 * @return The `CreateFailureCondition` method returns an instance of the `CreateFailureCondition` class. This object encapsulates a failure condition with specified properties such as name, description, ID, and effects on higher-level systems, aircraft, crew, and occupants. The returned object is designed to represent a specific failure condition based on input data provided in the form of key-value pairs within a map and an explicit failure condition identifier.
 * @summary The `CreateFailureCondition` method initializes a failure condition object using the provided property map and unique identifier.
 */
	public CreateFailureCondition(Map<String, String> propertyMapStrings, String fcId) {
		// TODO Auto-generated constructor stub
		this.FCName =propertyMapStrings.get("Name");
		this.FCDescription =propertyMapStrings.get("Description");
		this.FCid =fcId;
		this.FCeffectHLS =propertyMapStrings.get("Effect On Higher Level System");
		this.FCeffectAC =propertyMapStrings.get("Effect On Aircraft");
		this.FCeffectCW =propertyMapStrings.get("Effect On Crew");
		this.FCeffectOCC =propertyMapStrings.get("Effect On Occupants");
	}

/**
 * @param selectedNode The `selectedNode` parameter in the `create` method represents the SysML element chosen by the user for which a failure condition is to be generated.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter of type `List<String>` in method `create` contains a list of properties that have been updated and are used to generate the failure condition.
 * @return The `create` method returns a `String` that provides feedback on the result of creating a Failure Condition element. It can indicate success with a descriptive message or failure due to null inputs, exceptions, or other issues encountered during execution.
 * @summary The `create` method generates a failure condition element within the OSIRIS plugin by processing a selected node and its associated properties, ensuring session management and handling exceptions to provide feedback through log messages.
 */
	public String create(Element selectedNode, Map<String, List<String>> propertyMapUpdated) {
		// Check for null inputs first
		if (selectedNode == null) {
			Application.getInstance().getGUILog().showMessage("selectedNode is null.");
			return "Failed to create Failure Condition: selectedNode is null.";
		}
		if (propertyMapUpdated == null) {
			Application.getInstance().getGUILog().showMessage("propertyMapUpdated is null.");
			return "Failed to create Failure Condition: propertyMapUpdated is null.";
		}

		String message = "";

		try {
			// Start session
			SessionManager.getInstance().createSession(project, "Creating Failure Condition Element");

			// Call the method that creates the failure condition
			message = CreateFC(selectedNode, propertyMapUpdated);

		} catch (Exception e) {
			// Log the exception message
			Application.getInstance().getGUILog().showMessage("Exception occurred: " + e.getMessage());
			e.printStackTrace(); // This will print the stack trace to the console for debugging

			// Cancel session in case of an exception
			SessionManager.getInstance().cancelSession(project);
		} finally {
			// Ensure the session is closed
			if (SessionManager.getInstance().isSessionCreated(project)) {
				SessionManager.getInstance().closeSession(project);
			}
		}

		return message;
	}

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML model element selected by the user to create or update a failure condition (FC) within.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter in the `CreateFC` method represents a map of properties that have been updated, where keys are property names and values are lists of associated string data.
 * @return The `CreateFC` method returns a `String` that provides feedback about the result of attempting to create or update a Failure Condition (FC) element in the safety model. The string typically contains either a success message with details about the created or updated FC, or an error message indicating any issues encountered during the process. This return value helps users understand the outcome of the operation and facilitates debugging or maintenance efforts.
 * @summary The `CreateFC` method is responsible for creating or updating a Failure Condition (FC) element in the SysML model based on the selected node and its properties.
 */
	private String CreateFC(Element selectedNode, Map<String, List<String>> propertyMapUpdated) throws ReadOnlyElementException {
	    // Search for Safety VP element
	    var safetyVPElement = SafetyVP.SafetyVPSearch(selectedNode);

	    var packageforFC = createFCPackage((Package) safetyVPElement, ((Class) selectedNode).getName());

	    if (packageforFC != null) {
	        var ownedFCs = packageforFC.getOwnedElement();
	        if (ownedFCs != null) {
	            // Check if an FC element with the same name already exists
	            for (Element FC : ownedFCs) {
	                if (ND.getName(FC).trim().equalsIgnoreCase(FCName.trim())) {
	                    // FC element with the name already exists, return a message and skip creation
	                	 CheckAndUpdateProperties(FC,propertyMapUpdated);
	                    return "Updated Existing : "+ FCid + " - "  + FCName + "; \n"; 
	                }
	            }
	            // If no existing FC element is found, proceed with creation
	            return FCElementCreation(selectedNode, packageforFC, propertyMapUpdated);
	        }
	    }
	    return "Problem encountered for : "+ FCid + " - " + FCName + "; \n" ;
	}

/**
 * @param fC The `fC` parameter of type `Element` represents the SysML element whose properties are being checked and updated within the `CheckAndUpdateProperties` method.
 * @param propertyMapUpdated `propertyMapUpdated`: A map containing updated properties for failure conditions, where keys are property names and values are lists of associated strings.
 * @summary This method is responsible for checking and updating properties of a given element based on updated property maps.
 */
	private void CheckAndUpdateProperties(Element fC, Map<String, List<String>> propertyMapUpdated) {
		// TODO Auto-generated method stub
		var defaultFTs = elementfinder.getElementsByNames(propertyMapUpdated.get("Failure Type"), elementfinder.FindAllFailure(), elementfinder.FindAllFailureNames());
		var FTProperty = StereotypesHelper.getPropertyByName(FCStereotype, "Failure Type");
		var Phs = elementfinder.getElementsByNames(propertyMapUpdated.get("Phase"), elementfinder.FindAllPhases(), elementfinder.FindAllPhaseNames());
		var PhProperty = StereotypesHelper.getPropertyByName(FCStereotype, "Flight Phases");
		var HCs = elementfinder.getElementsByNames(propertyMapUpdated.get("Hazard Classification"), elementfinder.FindAllPhases(), elementfinder.FindAllPhaseNames());
		var HCProperty = StereotypesHelper.getPropertyByName(FCStereotype, "Classification");
		CheckandAddvalues(HCs,HCProperty, fC ,"Classification");
		CheckandAddvalues(defaultFTs,FTProperty, fC , "Default Failure Type");
		CheckandAddvalues(Phs,PhProperty, fC , "Flight Phases");
		
	}

/**
 * @param list The `List<Element>` parameter named `list` in the `CheckandAddvalues` method is used to store the elements that will be merged with an existing list from a selected node.
 * @param fTProperty The `fTProperty` parameter is a `Property` object that represents the specific property of a stereotype which is being checked and updated.
 * @param selectedNode The `selectedNode` parameter represents the specific SysML element whose properties are being checked and updated with new values.
 * @param PropertyName `PropertyName`: The name of the property to be updated with the combined list of elements.
 * @summary The `CheckandAddvalues` method merges two lists of elements and updates the stereotype property value of a selected node with the merged list.
 */
	private void CheckandAddvalues(List<Element> list, Property fTProperty, Element selectedNode, String PropertyName) {
		// TODO Auto-generated method stub
		var existinglist = StereotypesHelper.getStereotypePropertyValue(selectedNode, fTProperty);
		List<Element> combinedlist = MergeLists(list,existinglist);
		StereotypesHelper.setStereotypePropertyValue(selectedNode, FCStereotype, PropertyName, combinedlist);
	}

/**
 * @param list **Parameter Description**: `list` is an input parameter representing a collection of `Element` objects that are intended for merging with another existing list, ensuring no duplicates in the merged result.
 * @param existinglist The `existinglist` parameter in the `MergeLists` method is a generic list containing elements of any type that will be merged with another list, ensuring no duplicates.
 * @return The `MergeLists` method returns a consolidated list of elements, free from duplicates, by merging two input lists: the primary list and an existing list. If the primary list is null or empty, it returns a filtered version of the existing list containing only instances of the Element class.
 * @summary The `MergeLists` method merges two lists of elements, ensuring no duplicates are present in the resulting list.
 */
	public List<Element> MergeLists(List<Element> list, List<?> existinglist) {
	    if (list == null || list.isEmpty()) {
	        // Return the existing list if the input list is empty
	        return existinglist.stream()
	                .filter(Element.class::isInstance)
	                .map(Element.class::cast)
	                .collect(Collectors.toList());
	    }

	    // Use a set to ensure no duplicates
	    Set<Element> resultSet = new HashSet<>(list);

	    for (Object item : existinglist) {
	        if (item != null && list.get(0).getClass().isInstance(item)) {
	            resultSet.add((Element) item);
	        }
	    }

	    // Convert the set back to a list and return
	    return new ArrayList<>(resultSet);
	}



/**
 * @param selectedNode The `selectedNode` parameter is the SysML element for which a failure condition is being created.
 * @param packageforFC The `packageforFC` parameter specifies the package within which the Failure Condition element will be created.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a map containing the updated properties for failure conditions and their corresponding values.
 * @return The `FCElementCreation` method returns a `String` that indicates the result of creating a Failure Condition element. If the creation is successful, it provides details such as the ID and name of the created element along with additional properties. If the creation fails, it returns an error message stating "Failure Condition Creation Failed".
 * @summary Creates a Failure Condition element within a specified package and assigns it properties from the given map.
 */
	private String FCElementCreation(Element selectedNode, Package packageforFC, Map<String, List<String>> propertyMapUpdated) throws ReadOnlyElementException {
	    // Create the Failure Condition element
	    var fcElement = elementCreator.createClass((Package) packageforFC, FCName);


	    // Check if the Failure Condition element is null after creation
	    if (fcElement == null) {
	        return "Failure Condition Creation Failed";
	    }

	    // Add stereotypes and set properties
	    var OSIRISprofile = StereotypesHelper.getProfile(project, "OSIRIS");
	    elementCreator.addStereotype(fcElement, "OSIRIS", "Failure condition");

	    var fcStereotype = StereotypesHelper.getStereotype(project, "Failure condition", OSIRISprofile);
	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "Description", FCDescription);

	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "ID", FCid);

	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "Function", selectedNode);

	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "Effect On Higher Level System", FCeffectHLS);

	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "Effect On Aicraft", FCeffectAC);

	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "Effect On Crew", FCeffectCW);

	    StereotypesHelper.setStereotypePropertyValue(fcElement, fcStereotype, "Effect On Occupants", FCeffectOCC);

	    String Mes =addProperties(propertyMapUpdated, fcElement,selectedNode);

	    // Return success message
	    return "Created : " + FCid + " - " + FCName + " \n "+ Mes + " \n ";
	}


/**
 * @param safetyVPElement The `safetyVPElement` parameter in the method `createFCPackage(Package safetyVPElement, String name)` is a `Package` instance representing the safety Viewpoint's package from which to locate or create failure condition sub-packages.
 * @param name The `name` parameter specifies the unique identifier for a sub-package to be created or retrieved within the "Failure Conditions" package.
 * @return The `createFCPackage` method returns a `Package` object that represents either an existing or newly created package within the "Failure Conditions" package. This returned package has the specified name and is intended to contain failure conditions for a particular function in the aircraft system being modeled.
 * @summary The `createFCPackage` method is responsible for finding or creating a specific sub-package named "Failure Conditions" within the given parent package (`safetyVPElement`) and subsequently locating or generating another sub-package with the provided name inside it.
 */
	private Package createFCPackage(Package safetyVPElement, String name) throws ReadOnlyElementException {
	    Package fcPackage = null;
	    Package fcFunctionPackage = null;

	    // Search for "Failure Conditions" package in safetyVPElement's owned elements
	    for (Element element : safetyVPElement.getOwnedElement()) {
	        if (element instanceof Package && "Failure Conditions".equals(ND.getName(element))) {
	            fcPackage = (Package) element;
	            break;  // Found the "Failure Conditions" package, no need to continue
	        }
	    }

	    // Create "Failure Conditions" package if it does not exist
	    if (fcPackage == null) {
	        fcPackage = elementCreator.createPackage(safetyVPElement, "Failure Conditions");
	    }

	    // Search for the specific package by name within "Failure Conditions"
	    for (Element element : fcPackage.getOwnedElement()) {
	        if (element instanceof Package && name.equals(ND.getName(element))) {
	        	fcFunctionPackage = (Package) element;
	            break;  // Found the package with the given name, no need to continue
	        }
	    }

	    // Create the package with the specified name if it does not exist
	    if (fcFunctionPackage == null) {
	    	fcFunctionPackage = elementCreator.createPackage(fcPackage, name);
	    }

	    return fcFunctionPackage;
	}


/**
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a list of strings representing the updated properties to be added or modified in the functional context element.
 * @param fcElement The `fcElement` parameter in the method `addProperties` represents a Java class type corresponding to the failure condition element used for checking and adding properties.
 * @param selectedNode The `selectedNode` parameter represents the SysML element currently selected for processing and analyzing its properties within the method.
 * @return The `addProperties` method returns a `String` that represents the log message detailing the properties added to the function class element based on the selected node and its associated stereotypes. This string is constructed by appending the results of various property checks and additions, providing a comprehensive record of the operations performed during the processing of properties.
 * @summary The method `addProperties` processes and adds system properties to the given failure condition element based on the type of selected node (function or activity) and updates a provided property map.
 */
	public String addProperties(Map<String, List<String>> propertyMapUpdated, Class fcElement, Element selectedNode) {
		// Check and process properties

	    StringBuilder creationMessage = new  StringBuilder();
	    var functionProperties = new PropertiesFunction();

		Map<String, List<Element>> propertyMap= new HashMap<String, List<Element>>();

		Element selectedfunction = null;
		if (selectedNode instanceof CallBehaviorAction) {		
			// Get the behavior of the CallBehaviorAction
			var selectedfunctionAction =selectedNode;
			var function = ((CallBehaviorAction) selectedNode).getBehavior();

			// Check if the function is not null and cast it to Element
			if (function != null) {
				 selectedfunction = (Element) function;
			}
			 propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction,selectedfunctionAction);

		} else if (selectedNode instanceof Activity) {
			// If selectedNode is an Activity, cast it to Element
			 selectedfunction = (Element) selectedNode;
			 propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction);
		}

		List<Element> MalfunctionList = propertyMap.get("Defined Failure Type");
		

	    creationMessage.append(checkAndAddProperties_Defined_FT(fcElement, MalfunctionList, "Defined Failure Type", propertyMapUpdated.get("Failure Type")));
		
	    creationMessage.append(checkAndAddProperties(fcElement, "Default Failure Type", propertyMapUpdated.get("Failure Type")));
	    creationMessage.append(checkAndAddProperties(fcElement, "Flight Phases", propertyMapUpdated.get("Phase")));
	    creationMessage.append(checkAndAddProperties(fcElement, "System", propertyMapUpdated.get("System")));
	    creationMessage.append(checkAndAddProperties(fcElement, "Classification", propertyMapUpdated.get("Hazard Classification")));
		return creationMessage.toString();
	}

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML element currently being processed for which failure conditions are being defined.
 * @param malfunctionList A `List<Element>` representing the collection of malfunction nodes within the SysML model.
 * @param propertyName The `propertyName` parameter specifies the name of the stereotype property to be set or checked on the selected node.
 * @param propertyList The `propertyList` parameter is a list of strings representing the names of properties to be assigned to the selected node's stereotype.
 * @return The method `checkAndAddProperties_Defined_FT` checks if a specified property list is non-empty and assigns corresponding elements to a given element's stereotype property. If the property list is empty, it returns a message indicating that the property is not assigned; otherwise, it returns an empty string upon successful assignment.
 * @summary The `checkAndAddProperties_Defined_FT` method verifies if a given property list is non-empty and assigns its corresponding elements as the stereotype property value for a selected SysML model element.
 */
	private Object checkAndAddProperties_Defined_FT(Element selectedNode, List<Element> malfunctionList, String propertyName,
			List<String> propertyList) {
		// Check if the property list is empty
		if (propertyList == null || propertyList.isEmpty()) {
//			Application.getInstance().getGUILog().showMessage(propertyName + " list is empty.");
			return propertyName + " is not Assigned";
		}
		// Determine the corresponding elements
		List<Element> elements = new ArrayList<>();
		
        elements = elementfinder.getElementsByNames(
	            propertyList,
	            malfunctionList,
	            ND.getStringListForElements(malfunctionList)
	        );
		// Set the stereotype property value
		StereotypesHelper.setStereotypePropertyValue(selectedNode, FCStereotype, propertyName, elements);
		return "";
	}

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML model element that will have its properties checked and potentially updated based on the specified property name and list.
 * @param propertyName The `propertyName` parameter specifies which property of a SysML element is being checked and set within the context of safety assessment.
 * @param propertyList The `propertyList` parameter is a list of strings representing potential values for the given property name to be set on the selected node in the SysML model.
 * @return The `checkAndAddProperties` method returns a `String` that indicates whether properties were successfully assigned or if there was an issue. If the provided property list is empty or null, it returns a message indicating that the specific property (e.g., "Default Failure Type", "Flight Phases") is not assigned. Otherwise, it returns an empty string to indicate successful assignment of the stereotype property value to the selected SysML element.
 * @summary The `checkAndAddProperties` method is responsible for verifying if a given property list is non-empty and assigning corresponding SysML elements to the specified stereotype property on the selected node.
 */
	private String checkAndAddProperties(Element selectedNode, String propertyName, List<String> propertyList) {
		// Check if the property list is empty
		if (propertyList == null || propertyList.isEmpty()) {
//			Application.getInstance().getGUILog().showMessage(propertyName + " list is empty.");
			return propertyName + " is not Assigned";
		}
		// Determine the corresponding elements
		List<Element> elements = new ArrayList<>();

		switch (propertyName) {
	    case "Default Failure Type":
	        elements = elementfinder.getElementsByNames(
	            propertyList,
	            elementfinder.FindAllFailure(),
	            elementfinder.FindAllFailureNames()
	        );
	        break;

	    case "Flight Phases":
	        elements = elementfinder.getElementsByNames(
	            propertyList,
	            elementfinder.FindAllPhases(),
	            elementfinder.FindAllPhaseNames()
	        );
	        break;

	    case "System":
	        elements = elementfinder.getElementsByNames(
	            propertyList,
	            elementfinder.FindAllSystemBlocks(),
	            elementfinder.FindAllSystemNames()
	        );
	        break;

	    case "Classification":
	        elements = elementfinder.getElementsByNames(
	            propertyList,
	            elementfinder.FindAllHazard(),
	            elementfinder.FindAllHazardNames()
	        );
	        break;

	    default:
	        throw new IllegalArgumentException("Invalid property name: " + propertyName);
	}


		// Set the stereotype property value
		StereotypesHelper.setStereotypePropertyValue(selectedNode, FCStereotype, propertyName, elements);
		return "";
	}


}

