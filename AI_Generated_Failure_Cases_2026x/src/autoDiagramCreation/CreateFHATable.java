package autoDiagramCreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Namespace;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import elementDataExtraction.StereotypeSearch;
import creationOfSafetyVP.SearchSafetyVP;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `CreateFHATable` class is responsible for generating Functional Hazard Assessment (FHA) tables by processing SysML function elements, with methods to handle general functions and air conditioning-specific functions.
 */
public class CreateFHATable {


/**
 * @param selectedFunction The `selectedFunction` parameter represents the SysML model element that is expected to be a function, from which an FHA table will be created or checked.
 * @return The `createTable` method returns a `String` that provides feedback on the result of attempting to create an FHA table diagram. The possible return values include:

- `"Stereotype not found."`: Indicates that the required DiagramTable stereotype could not be located.
- `"SafetyVP element not found."`: Signals that no SafetyVP element was found for the selected function.
- `"Package not found."`: Denotes that the package for failure conditions was not identified.
- `"FHA Table already exists."`: Specifies that an FHA table diagram with the necessary stereotypes is already present.
- `"Invalid element type."`: Suggests that the selected element does not have the required Function stereotype.
- `"FHA Table for generated failure conditions created."`: Confirms successful creation of the FHA table.

These messages are intended to guide users and developers in diagnosing issues or confirming success during the execution of the `createTable` method.
 * @summary The `createTable` method generates an FHA table diagram in the project's package for a given selected function, ensuring it has the correct stereotypes and handling errors appropriately.
 */
	public String createTable(Element selectedFunction) throws ReadOnlyElementException {
	    // Constants
	    final String DIAGRAM_TYPE = "FHA Table";
	    final String STEREOTYPE_FUNCTION = "Function";
	    final String STEREOTYPE_FAILURE_CONDITION = "Failure condition";
	    final String STEREOTYPE_DiagramTable = "DiagramTable";
	    final String STEREOTYPE_SafetyVP = "Safety VP Dia";

	    // Initialize helpers
	    StereotypeSearch stereotypeChecker = new StereotypeSearch();
	    SearchSafetyVP safetyVP = new SearchSafetyVP();
	    Project project = Application.getInstance().getProject();

	    // Get profile and stereotype
	    Profile profile = StereotypesHelper.getProfile(project, "MagicDraw Profile");
	    Stereotype dtStereotype = StereotypesHelper.getStereotype(project, "DiagramTable", profile);
	    if (dtStereotype == null) {
	        Application.getInstance().getGUILog().showError("DiagramTable stereotype not found!");
	        return "Stereotype not found.";
	    }
	    Property scopeProperty = StereotypesHelper.getPropertyByName(dtStereotype, "scope");

	    // Find safetyVP element and associated package
	    Element safetyVPElement = safetyVP.SafetyVPSearch(selectedFunction);
	    if (safetyVPElement == null) {
	        Application.getInstance().getGUILog().showError("SafetyVP element not found for the selected function.");
	        return "SafetyVP element not found.";
	    }

	    Package packageForFC = safetyVP.getFCPackage((Package) safetyVPElement, ((Class) selectedFunction).getName());
	    if (packageForFC == null) {
	        Application.getInstance().getGUILog().showError("Package for Failure Condition not found.");
	        return "Package not found.";
	    }

	    // Check owned elements for existing FHA table or failure conditions
	    Collection<Element> ownedElements = packageForFC.getOwnedElement();
	    List<Element> fcElements = new ArrayList<>();
	    for (Element el : ownedElements) {
	        if (stereotypeChecker.hasStereotypeBoolean(el, STEREOTYPE_FAILURE_CONDITION)) {
	            fcElements.add(el);
	        }
	        if (stereotypeChecker.hasStereotypeBoolean(el, STEREOTYPE_DiagramTable) && stereotypeChecker.hasStereotypeBoolean(el, STEREOTYPE_SafetyVP) ) {
	            // Open the created diagram
	            project.getDiagram((Diagram) el).open();
	            return "FHA Table already exists.";
	        }
	    }

	    // Validate function stereotype and create the diagram
	    if (stereotypeChecker.hasStereotypeBoolean(selectedFunction, STEREOTYPE_FUNCTION)) {
	        SessionManager.getInstance().createSession(project, "Create a diagram");
	        try {
	            Diagram diagramTable = ModelElementsManager.getInstance().createDiagram(DIAGRAM_TYPE, (Namespace) packageForFC);
	            if (!fcElements.isEmpty()) {
	                StereotypesHelper.setStereotypePropertyValue(diagramTable, dtStereotype, scopeProperty, packageForFC);
	            } else {
	                Application.getInstance().getGUILog().showMessage("No Failure conditions for the function.");
	            }
	            // Open the created diagram
	            project.getDiagram(diagramTable).open();
	        } catch (ReadOnlyElementException e) {
	            Application.getInstance().getGUILog().showError("Error creating diagram: " + e.getMessage());
	            throw e; // Re-throw to signal the error
	        } finally {
	            SessionManager.getInstance().closeSession(project);
	        }
	    } else {
	        Application.getInstance().getGUILog().showError("Selected element does not have the required Function stereotype.");
	        return "Invalid element type.";
	    }

	    return "FHA Table for generated failure conditions created.";
	}
	
/**
 * @param selectedFunction The `selectedFunction` parameter is the SysML function element that will be used to determine and generate associated Failure Condition elements in the FHA table.
 * @return The `createTable_AC_Functions` method returns a `String` that indicates the result of attempting to create an FHA table diagram for aircraft functions. The possible return values include error messages like "Stereotype not found.", "SafetyVP element not found.", "Package not found.", and "Invalid element type." If successful, it returns "FHA Table already exists." or "FHA Table for generated failure conditions created.".
 * @summary The `createTable_AC_Functions` method is responsible for generating an FHA table diagram within a given package based on the selected function, ensuring the necessary stereotypes and safety contexts are validated and applied.
 */
	public String createTable_AC_Functions(Element selectedFunction) throws ReadOnlyElementException {
	    // Constants
	    final String DIAGRAM_TYPE = "FHA Table (Aircraft level)";
	    final String STEREOTYPE_FUNCTION = "Function";
	    final String STEREOTYPE_FAILURE_CONDITION = "Failure condition";
	    final String STEREOTYPE_DiagramTable = "DiagramTable";
	    final String STEREOTYPE_SafetyVP = "Safety VP Dia";

	    // Initialize helpers
	    StereotypeSearch stereotypeChecker = new StereotypeSearch();
	    SearchSafetyVP safetyVP = new SearchSafetyVP();
	    Project project = Application.getInstance().getProject();

	    // Get profile and stereotype
	    Profile profile = StereotypesHelper.getProfile(project, "MagicDraw Profile");
	    Stereotype dtStereotype = StereotypesHelper.getStereotype(project, "DiagramTable", profile);
	    if (dtStereotype == null) {
	        Application.getInstance().getGUILog().showError("DiagramTable stereotype not found!");
	        return "Stereotype not found.";
	    }
	    Property scopeProperty = StereotypesHelper.getPropertyByName(dtStereotype, "scope");

	    // Find safetyVP element and associated package
	    Element safetyVPElement = safetyVP.SafetyVPSearch(selectedFunction);
	    if (safetyVPElement == null) {
	        Application.getInstance().getGUILog().showError("SafetyVP element not found for the selected function.");
	        return "SafetyVP element not found.";
	    }

	    Package packageForFC = safetyVP.getFCPackage((Package) safetyVPElement, ((Class) selectedFunction).getName());
	    if (packageForFC == null) {
	        Application.getInstance().getGUILog().showError("Package for Failure Condition not found.");
	        return "Package not found.";
	    }

	    // Check owned elements for existing FHA table or failure conditions
	    Collection<Element> ownedElements = packageForFC.getOwnedElement();
	    List<Element> fcElements = new ArrayList<>();
	    for (Element el : ownedElements) {
	        if (stereotypeChecker.hasStereotypeBoolean(el, STEREOTYPE_FAILURE_CONDITION)) {
	            fcElements.add(el);
	        }
	        if (stereotypeChecker.hasStereotypeBoolean(el, STEREOTYPE_DiagramTable) && stereotypeChecker.hasStereotypeBoolean(el, STEREOTYPE_SafetyVP) ) {
	            // Open the created diagram
	            project.getDiagram((Diagram) el).open();
	            return "FHA Table already exists.";
	        }
	    }

	    // Validate function stereotype and create the diagram
	    if (stereotypeChecker.hasStereotypeBoolean(selectedFunction, STEREOTYPE_FUNCTION)) {
	        SessionManager.getInstance().createSession(project, "Create a diagram");
	        try {
	            Diagram diagramTable = ModelElementsManager.getInstance().createDiagram(DIAGRAM_TYPE, (Namespace) packageForFC);
	            if (!fcElements.isEmpty()) {
	                StereotypesHelper.setStereotypePropertyValue(diagramTable, dtStereotype, scopeProperty, packageForFC);
	            } else {
	                Application.getInstance().getGUILog().showMessage("No Failure conditions for the function.");
	            }
	            // Open the created diagram
	            project.getDiagram(diagramTable).open();
	        } catch (ReadOnlyElementException e) {
	            Application.getInstance().getGUILog().showError("Error creating diagram: " + e.getMessage());
	            throw e; // Re-throw to signal the error
	        } finally {
	            SessionManager.getInstance().closeSession(project);
	        }
	    } else {
	        Application.getInstance().getGUILog().showError("Selected element does not have the required Function stereotype.");
	        return "Invalid element type.";
	    }

	    return "FHA Table for generated failure conditions created.";
	}


}
