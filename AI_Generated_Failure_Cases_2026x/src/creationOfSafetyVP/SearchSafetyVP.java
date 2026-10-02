package creationOfSafetyVP;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import elementDataExtraction.StereotypeFinder;
import elementDataExtraction.StereotypeSearch;



/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `SearchSafetyVP` class is responsible for searching and creating Safety viewpoint within SysML models, including managing their associated packages.
 */
public class SearchSafetyVP {

    String systemStereotype = "System";
    String safetyVPStereotype = "Safety Viewpoint";

	StereotypeFinder PF = new StereotypeFinder();
	Stereotype safety_VP_Stereotype = PF.findstereotype("Safety Viewpoint");
//	Stereotype system_Stereotype = PF.findstereotype("System");

    Project project = Application.getInstance().getProject();
    Element safetyVPElement = null;
	NameDerivator ND= new NameDerivator();

	ElementCreation elementCreator = new ElementCreation();

    // Method to search for a Safety VP element in the selected element's hierarchy
/**
 * @param selectedElement `selectedElement`: The Element object that is currently selected and for which a corresponding Safety Viewpoint (VP) needs to be located or created.
 * @return The `SafetyVPSearch` method returns an instance of `Element` that represents the "Safety Architecture" stereotype within a SysML model. If such an element exists, it will be returned; otherwise, it attempts to create one and returns it if successful. The method navigates through the hierarchy of elements until either the desired safety architecture is found or the search concludes without finding it.
 * @summary The `SafetyVPSearch` method recursively searches for an owned element with a "Safety Architecture" stereotype within the hierarchy of the selected element, creating it if necessary.
 */
    public Element SafetyVPSearch(Element selectedElement) throws ReadOnlyElementException {

        var stereotypeSearch = new StereotypeSearch();

        // If the selected element is null, return null
        if (selectedElement == null) {
            return null;
        }

        // Get the owner of the selected element
        var ownerElement = selectedElement.getOwner();

        // If the owner element is null, return null
        if (ownerElement == null) {
            return null;
        }

        // Check if the owner element has the "System" stereotype
        if (stereotypeSearch.hasStereotypeBoolean(ownerElement, "System")) {
            // Check if the owner element has an owned element with the "Safety Architecture" stereotype
            safetyVPElement = stereotypeSearch.hasStereotypeElement_OwnedElement(ownerElement, safetyVPStereotype);

            // If the safety element is found and is an instance of Package, return it
            if (safetyVPElement != null && safetyVPElement instanceof Package) {
                return safetyVPElement;
            } else {
                // Create a new Safety VP if not found
                safetyVPElement = createSafetyVP(ownerElement);
                if (safetyVPElement != null) {
                    return safetyVPElement;
                } else {
                    Application.getInstance().getGUILog().showMessage("Safety ViewPoint creation failed.");
                }
            }
        }

        // Recursively search the owner's hierarchy if the safety element is not found
        return SafetyVPSearch(ownerElement);
    }

    // Method to create a Safety VP element if none is found
/**
 * @param selectedElement The `selectedElement` parameter in the method `createSafetyVP(Element selectedElement)` represents the SysML element that serves as the context for creating a Safety View Point (Safety VP) package.
 * @return The `createSafetyVP` method returns an `Element` that represents a newly created "Safety View Point" (Safety VP) package within the given SysML model. This element is specifically designed to encapsulate safety-related architectural aspects of a system marked with the "System" stereotype in the SysML model. The return value will be null if no such element could be created or if certain conditions are not met, such as the selected element lacking the necessary stereotypes or being an inappropriate type for creating the Safety VP package.
 * @summary The `createSafetyVP` method is responsible for creating a new "Safety VP" package element within the specified selected element if it has the "System" stereotype.
 */
    public Element createSafetyVP(Element selectedElement) throws ReadOnlyElementException {

        var stereotypeSearch = new StereotypeSearch();
        var elementCreator = new ElementCreation();

        // If the selected element is null, return null
        if (selectedElement == null) {
            return null;
        }
        
//        SessionManager.getInstance().createSession(project, "Creating Safety VP Package");
//        try {

        // Check if the owner element has the "System" stereotype
        if (stereotypeSearch.hasStereotypeBoolean(selectedElement, "System")) {

            // If the owner element is a Package, create a new Safety VP element inside it
            if (selectedElement instanceof Package) {
                safetyVPElement = elementCreator.createPackage((Package) selectedElement, safetyVPStereotype);

                // Add the "Safety Architecture" stereotype to the newly created element
                elementCreator.addStereotype((Package) safetyVPElement, "OSIRIS", safety_VP_Stereotype);

                // If Safety VP element is successfully created, return it
                if (safetyVPElement != null) {
                    return safetyVPElement;
                } else {
                    Application.getInstance().getGUILog().showMessage("Creation of Safety View Point failed.");
                }
            }
        } 
//        } finally {
//            SessionManager.getInstance().closeSession(project);
//        }
        // If nothing was created or found, return null
        return null;
    }
/**
 * @param safetyVPElement The `safetyVPElement` parameter represents the parent package within which the method searches for or creates a "Failure Conditions" subpackage and subsequently looks for or creates a specified named subpackage.
 * @param name The `name` parameter specifies the desired name for the sub-package within the "Failure Conditions" package.
 * @return The `getFCPackage` method retrieves or creates a specific package within a given SysML model's "Failure Conditions" package. It returns the found or newly created package, which is an instance of the `Package` class. This returned package is intended to represent a collection of failure conditions related to a particular function or system component within the safety view point element (safetyVPElement) context.
 * @summary The method `getFCPackage` retrieves or creates a specific sub-package within the "Failure Conditions" package of a given safety view point element.
 */
	public Package getFCPackage(Package safetyVPElement, String name) throws ReadOnlyElementException {
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
}


