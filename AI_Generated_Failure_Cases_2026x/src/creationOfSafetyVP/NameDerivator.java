package creationOfSafetyVP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `NameDerivator` class is responsible for deriving and processing names and type information from SysML elements within the OSIRIS plugin.
 */
public class NameDerivator {

	// Method to derive name
/**
 * @param selectedElement The parameter `selectedElement` is an instance of `Element` that represents the specific model element for which the method will determine and return a descriptive name.
 * @return The `getName` method returns a String representing the human-readable name of the specified element. If the element does not have a valid name, it derives and returns a fallback based on its type. If no name can be determined, an empty string is returned.
 * @summary The `getName` method retrieves and returns the name of the specified `Element`, handling various types such as `NamedElement`, `BaseElement`, and `TypedElement`, and provides fallback mechanisms to ensure a meaningful name is derived or logs an appropriate message when necessary.
 */
	public String getName(Element selectedElement) {
		// Check if selectedElement is null to avoid NullPointerException
		if (selectedElement == null) {
			Application.getInstance().getGUILog().showMessage("Selected element is null");
			return "";
		}

		// Check for the kind of instance and derive name accordingly
		String name = "";

		if (selectedElement instanceof NamedElement) {
			// Handle NamedElement
			name = ((NamedElement) selectedElement).getName();

			// If the name is empty or null, derive type name
			if (name == null || name.isEmpty()) {
				//	            Application.getInstance().getGUILog().showMessage("NamedElement's name is empty, deriving type name.");
				name = getTypeName(selectedElement);
			}

		} else if (selectedElement instanceof BaseElement) {
			// Handle BaseElement
			BaseElement element = (BaseElement) selectedElement;
			String humanName = getRemainingString(element.getHumanName(), element.getHumanType());

			// Use human name if available, otherwise derive type name
			if (humanName != null && !humanName.isEmpty()) {
				name = humanName;
			} else {
				//	            Application.getInstance().getGUILog().showMessage("BaseElement's human name is empty, deriving type name.");
				name = getTypeName(selectedElement);
			}

		} else if (selectedElement instanceof TypedElement) {
			// Handle TypedElement or any other supported type
			Element type = ((TypedElement) selectedElement).getType();
			name = getTypeName(type); // Fallback to type name if it's a TypedElement

		} else {
			// Unsupported element type
//			Application.getInstance().getGUILog().showMessage("Selected element does not have a supported type or human-readable name.");
		}

/**
 * @param name.isEmpty( The `name` parameter of type `String` is checked if it is either `null` or an empty string in order to ensure that a valid name has been derived before proceeding with further operations.
 * @return The return value of the `if` method is an empty string (`""`) which represents that no meaningful message could be derived for the given input.
 * @summary The `if` method is responsible for logging a failure message and returning an empty string if the `name` is either null or an empty string after all validation checks.
 */
		// If name is still empty after all checks, log a final message
		if (name == null || name.isEmpty()) {
//			Application.getInstance().getGUILog().showMessage("Failed to derive a name or type for the selected element.");
			return "";
		}

		return name;
	}


	// Method to derive type name
/**
 * @param selectedElement The `selectedElement` parameter in the `getTypeName` method represents an element from which the method attempts to retrieve its type's name, typically a `TypedElement`.
 * @return The `getTypeName` method returns a `String` that represents the name of the type associated with a given SysML element. If the element has an associated type, its name is returned; otherwise, an empty string is returned indicating that no type name is available or the selected element is not a TypedElement.
 * @summary The `getTypeName` method retrieves the name of the type associated with a given SysML element, if it exists.
 */
	public String getTypeName(Element selectedElement) {
		if (selectedElement instanceof TypedElement) {
			Element type = ((TypedElement) selectedElement).getType();

			// Check if the type is non-null before calling getName()
			if (type != null) {
				return getName(type); // Recursive call to get the type's name
			}
		}
//		Application.getInstance().getGUILog().showMessage("Type name not available or selectedElement is not a TypedElement.");
		return "";
	}



	// Helper method to derive the remaining string
/**
 * @param fullString The `fullString` parameter represents the complete string from which a specific substring (`partString`) will be removed if it appears at the start.
 * @param partString The `partString` parameter represents the substring to be removed from the start of `fullString`.
 * @return The `getRemainingString` method returns a trimmed substring that remains after removing any occurrence of the specified `partString` from the start of the `fullString`.
 * @summary The `getRemainingString` method extracts and returns the substring from `fullString` that comes after `partString`, if `fullString` starts with `partString`.
 */
	public static String getRemainingString(String fullString, String partString) {
		// Trim leading and trailing spaces from both strings
		fullString = fullString.trim();
		partString = partString.trim();

		// Check if fullString starts with partString
		if (fullString.startsWith(partString)) {
			// Remove the partString from fullString and trim the result
			return fullString.substring(partString.length()).trim();
		} else {
			// If partString is not at the beginning of fullString, return fullString unchanged
			return fullString;
		}
	}
	
	// Assuming ND.getName is a static method that retrieves the name of an Element.
    public Map<String, List<String>> getSystemNames_For_Functions(List<String> FuncStringList, Map<String, List<Element>> propertyMap,Map<String, List<String>> propertyMapUpdated) {
        // Initialize a new list to hold system names
        List<String> llSystems = new ArrayList<>();
    	for (String funcName : FuncStringList) {
            // Retrieve the lower-level system list from the property map
            List<Element> lowerLevelSysList = propertyMap.get(funcName);

            // Add names to the LLSystems list
            if (lowerLevelSysList != null) {
                for (Element obj : lowerLevelSysList) {
                    llSystems.add(getName(obj));
                }
            }
            // Update the property map with the processed list
            propertyMapUpdated.put(funcName, llSystems);
    	}

        return propertyMapUpdated; // Return the populated list of lower-level systems.
    }


/**
 * @param upperLevelList A list of elements representing the upper level components in the system hierarchy.
 * @return The return value of the `getStringListForElements` method is a list of strings (`List<String>`), where each string represents the name of an element from the input list.
 * @summary The `getStringListForElements` method converts a list of `Element` objects to a list of their string representations by extracting and collecting their names.
 */
	public List<String> getStringListForElements(List<Element> upperLevelList) {
		// TODO Auto-generated method stub
		List<String> namelist = new ArrayList<>();
		for(Element el:upperLevelList) {
			namelist.add(getName(el));
		}
		return namelist;
	}
}
