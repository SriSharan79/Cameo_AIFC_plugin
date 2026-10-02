package elementDataExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import creationOfSafetyVP.NameDerivator;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `StereotypeFinder` class is responsible for locating and managing stereotypes within a SysML model, providing methods to find specific stereotypes, retrieve all stereotypes, get instances of stereotypes associated with an element, and select a stereotype from a list of matching names.
 */
public class StereotypeFinder {

	Project project = Application.getInstance().getProject();
	List<Stereotype> St = new ArrayList<>();

	NameDerivator ND= new NameDerivator();

/**
 * @param Stereotype_Name `Stereotype_Name`: The name of the stereotype to be searched within the project.
 * @return The return value of the `findstereotype` method is a `Stereotype`. This represents the specific stereotype that matches the given name. If multiple stereotypes match, the user will be prompted to select one. If no matching stereotype is found, an exception message is displayed.
 * @summary The `findstereotype` method searches for a stereotype by name within the project's stereotypes, returning the matching stereotype if found or handling cases where no match is found or multiple matches exist.
 */
	public Stereotype findstereotype(String Stereotype_Name) {
		// TODO Auto-generated method stub
//		var All_Stereotypes= StereotypesHelper.getAllStereotypes(project);
		List<Stereotype> All_Stereotypes= getAllStereotypes();
		StringBuilder St_names= new StringBuilder();
		ArrayList<Stereotype> Matching_Names = new ArrayList<>();
		int i=0;

		for (Stereotype st : All_Stereotypes) {
			String Name = ND.getName(st);
			St_names.append(i). append("-").append(Name).append("\n");
			if(Name.equals(Stereotype_Name)) {
				Matching_Names.add(st);	
			}
			i++;
		}

		if (Matching_Names.size()==1) {
			return Matching_Names.get(0);
		}else if (Matching_Names.size()==0) {
			Application.getInstance().getGUILog().showMessage("Exception occurred: No Stereotpye with name " + Stereotype_Name+" Found in "+ project.getName()+"\n Existing stereotypes:\n"+St_names);
		}else {
			return choosestereotypeFromSameName(Matching_Names,Stereotype_Name);
		}

		return null;
	}

 
/**
 * @return The `getAllStereotypes` method returns a list of all stereotype instances found within the project's models. This includes stereotypes from top-level packages and their sub-packages, aggregated into a single list for comprehensive access.
 * @summary The `getAllStereotypes` method collects and returns a list of all stereotypes present in the project's models.
 */
    public List<Stereotype> getAllStereotypes() {
        List<Stereotype> allStereotypes = new ArrayList<>(); // Initialize the list to collect all stereotypes

        if (project != null) {
            List<Package> models = project.getModels();
            if (models != null) {
                for (Package model : models) {
                    // Recursively get all stereotypes from each top-level model/package
                    allStereotypes.addAll(getAllInstancesOfStereotypes(model));
                }
            }
        }
        return allStereotypes;
    }

  
/**
 * @param element The `element` parameter represents the SysML element for which the method retrieves all instances of stereotypes.
 * @return The method `getAllInstancesOfStereotypes` returns a list of all instances of Stereotype objects that are directly or indirectly owned by the given element. If the input element is null, it returns an empty list.
 * @summary The method `getAllInstancesOfStereotypes` retrieves all instances of `Stereotype` from a given element and its nested owned elements in a SysML model.
 */
    private List<Stereotype> getAllInstancesOfStereotypes(Element element) {
        List<Stereotype> stList = new ArrayList<>();

        if (element == null) {
            return stList; // Return empty list if element is null
        }

        // Get owned elements of the current element
        var ownedElements = element.getOwnedElement();

        if (ownedElements != null) {
            for (Element ownedElement : ownedElements) {
                // 1. Check if the current owned element itself is a Stereotype
                if (ownedElement instanceof Stereotype) {
                    stList.add((Stereotype) ownedElement); // Correctly add the single Stereotype
                }

                // 2. Recursively check the owned elements of the current owned element
                // This will traverse deeper into the model hierarchy (e.g., packages within packages, classes within packages)
                stList.addAll(getAllInstancesOfStereotypes(ownedElement));
            }
        }

        return stList;
    }
/**
 * @param matching_Names The `matching_Names` parameter is an ArrayList of Stereotypes that share the same name as the given `stereotype_Name`.
 * @param stereotype_Name The `stereotype_Name` parameter represents the name of the stereotype being sought in a list of potential matches.
 * @return The `choosestereotypeFromSameName` method returns the `Stereotype` object that the user selects from a list of potential matches with the same name. This selection is presented to the user through a dialog, and the chosen `Stereotype` is returned to be used for further processing in the OSIRIS plugin functionality.
 * @summary This method is responsible for selecting a stereotype from a list of options with the same name by presenting them to the user in a dialog box.
 */
	private Stereotype choosestereotypeFromSameName(ArrayList<Stereotype> matching_Names, String stereotype_Name) {
		// TODO Auto-generated method stub

		// Prepare the options for JOptionPane
		// We'll use a String array for the display options and a separate map
		// to link the chosen index back to the Stereotype object.
		String[] options = new String[matching_Names.size()];
		HashMap<Integer, Stereotype> indexToStereotypeMap = new HashMap<>();

		for (int i = 0; i < matching_Names.size(); i++) {
			Stereotype st = matching_Names.get(i);
			options[i] = "Stereotype: " + ND.getName(st) + " (Profile: " + ND.getName(st.getProfile()) + ")";
			indexToStereotypeMap.put(i, st); // Map the index in the options array to the Stereotype object
		}

		// Display the JOptionPane for user selection
		int choice = JOptionPane.showOptionDialog(
				null,                                        // Parent component (null for desktop centered)
				"Multiple stereotypes found with the name '" + stereotype_Name + "'. WHich Sterotype element are you considering and Please choose one:", // Message
				"Select Stereotype",                         // Title
				JOptionPane.DEFAULT_OPTION,                  // Option type
				JOptionPane.QUESTION_MESSAGE,                // Message type (icon)
				null,                                        // Icon (null for default)
				options,                                     // Array of options to display
				options[0]                                   // Initial value (default selected option)
				);

		// Handle user's choice
		if (choice == JOptionPane.CLOSED_OPTION) {
			// User closed the dialog or clicked Cancel
			JOptionPane.showMessageDialog(null,
					"Stereotype selection cancelled.",
					"Selection Cancelled",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		} else {
			// A valid option was chosen
			Stereotype selectedStereotype = indexToStereotypeMap.get(choice);
			JOptionPane.showMessageDialog(null,
					"You selected: " + ND.getName(selectedStereotype) + " (Profile: " + ND.getName(selectedStereotype.getProfile()) + ")",
					"Selection Confirmed",
					JOptionPane.INFORMATION_MESSAGE);
			return selectedStereotype;
		}
	}
}
