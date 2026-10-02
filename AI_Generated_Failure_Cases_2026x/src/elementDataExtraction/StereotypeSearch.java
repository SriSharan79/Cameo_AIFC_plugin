package elementDataExtraction;

import java.util.Arrays;
import java.util.List;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `StereotypeSearch` class is responsible for identifying and retrieving stereotype information from SysML elements within the OSIRIS plugin.
 */
public class StereotypeSearch {
	Project project = Application.getInstance().getProject();
    public static final List<String> REQUIREMENTS = Arrays.asList(
            "Requirement", "Maintainability Requirement", "Safety Requirement", 
            "Derived Requirement", "Customer Requirement", "Operational Requirement", 
            "Functional Requirement", "Performance Requirement", "Physical Requirement", 
            "Interface Requirement", "functionalRequirement", "performanceRequirement", 
            "physicalRequirement", "interfaceRequirement");

	// Method to check if the element has the given stereotype, and return the element if true
/**
 * @param element The `element` parameter of type `Element` in the method `hasStereotypeElement` represents the SysML model element being checked for a specific applied stereotype.
 * @param stereotypeName The `stereotypeName` parameter specifies the name of the stereotype to check for on the given element.
 * @return The return value for `hasStereotypeElement` is an Element that contains the specified stereotype. If the input Element does not exist, lacks stereotypes, or does not possess the given stereotype, the method returns null.
 * @summary This method checks whether a given SysML element possesses a specific stereotype.
 */
	public Element hasStereotypeElement(Element element, String stereotypeName) {
		// Check if the element is not null and has stereotypes
		if (element != null) {
			var stereotypes = element.getAppliedStereotype();

			if (stereotypes != null) {
				boolean hasStereotype = stereotypes.stream()
						.anyMatch(stereotype -> stereotypeName.equals(stereotype.getName()));

				if (hasStereotype) {
					return element; // Return element if it has the specified stereotype
				}
			}
		}

		// Return null if the element is null, has no stereotypes, or doesn't have the specified stereotype
		return null;
	}

	// Method to check if the element has the given stereotype, returning a boolean
/**
 * @param element The `element` parameter represents a SysML model Element to be checked for the presence of a specific applied stereotype.
 * @param stereotypeName The parameter `stereotypeName` specifies the name of the stereotype to check for on the given SysML element.
 * @return The `hasStereotypeBoolean` method returns a boolean value indicating whether an element has a specific stereotype applied. It checks if the element exists and then looks for the specified stereotype name within its list of applied stereotypes. If found, it returns true; otherwise, it returns false.
 * @summary The `hasStereotypeBoolean` method checks if a given SysML model element has an applied stereotype with the specified name.
 */
	public boolean hasStereotypeBoolean(Element element, String stereotypeName) {
		// Check if the element is not null and has stereotypes
		if (element != null) {
			var stereotypes = element.getAppliedStereotype();

			if (stereotypes != null) {
				return stereotypes.stream()
						.anyMatch(stereotype -> stereotypeName.equals(stereotype.getName()));
			}
		}

		// Return false if the element is null, or it has no stereotypes
		return false;
	}


	// Method to search through the owned elements of a given element for the specified stereotype
/**
 * @param element The `element` parameter of type `Element` in the method `hasStereotypeElement_OwnedElement` represents the SysML model element whose owned elements are being checked for a specific stereotype.
 * @param stereotypeName The `stereotypeName` parameter specifies the name of the stereotype to search for among the owned elements of the given element.
 * @return The `hasStereotypeElement_OwnedElement` method searches through the owned elements of a given element to find one that possesses the specified stereotype. If such an element is found, it returns this element; otherwise, it returns `null`.
 * @summary The `hasStereotypeElement_OwnedElement` method checks if any of an element's owned elements have a specific stereotype and returns that element, or returns null if not found.
 */
	public Element hasStereotypeElement_OwnedElement(Element element, String stereotypeName) {

		var ownedElements = element.getOwnedElement();
		for (Element el : ownedElements) {
			// Check if the owned element has the given stereotype
			if (hasStereotypeBoolean(el, stereotypeName)) {
				return el; // Return the owned element if it has the specified stereotype
			}
		}

		// Return null if no owned element with the given stereotype is found
		return null;
	}

/**
 * @param element The `element` parameter of type `Element` represents the SysML model element to be checked for the presence of a requirement stereotype.
 * @return The `hasRequirementStereotype` method returns a boolean value indicating whether the given element has any applied stereotypes that match predefined requirement stereotypes. The return value is true if at least one matching stereotype is found, and false otherwise.
 * @summary The method `hasRequirementStereotype` determines whether a given SysML model element has any applied stereotypes that match known requirement names.
 */
	public boolean hasRequirementStereotype(Element element) {
		// TODO Auto-generated method stub

		if (element != null) {
		    // Get the applied stereotypes from the element
		    var stereotypes = element.getAppliedStereotype();

		    // Check if there are any stereotypes applied
		    if (stereotypes != null) {
		        // Use stream and anyMatch to check if any stereotype matches a requirement
		        return stereotypes.stream()
		                .anyMatch(stereotype -> REQUIREMENTS.stream()
		                        .anyMatch(req -> req.equals(stereotype.getName())));
		    }
		}

		return false;  // Return false if no match is found
	}
	
/**
 * @param element The `element` parameter represents a SysML model element for which the method searches for an applied stereotype that matches predefined requirement types.
 * @return The `getMatchedRequirementStereotype` method returns a `Stereotype` that matches one of the requirement names from a predefined list. If such a stereotype exists, it will be returned; otherwise, the method returns `null`.
 * @summary The `getMatchedRequirementStereotype` method retrieves the first stereotype applied to a given element that matches any of the predefined requirement stereotypes.
 */
	public Stereotype getMatchedRequirementStereotype(Element element) {
	    if (element != null) {
	        // Get the applied stereotypes from the element
	        var stereotypes = element.getAppliedStereotype();

	        // Check if there are any stereotypes applied
	        if (stereotypes != null) {
	            // Use stream to find the first matching stereotype
	            return stereotypes.stream()
	                    .filter(stereotype -> REQUIREMENTS.stream()
	                            .anyMatch(req -> req.equals(stereotype.getName())))
	                    .findFirst()
	                    .orElse(null);  // Return the first match, or null if no match is found
	        }
	    }

	    return null;  // Return null if no match is found
	}



}

