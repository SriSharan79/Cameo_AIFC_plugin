package elementDataExtraction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.*;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `IdGenerator` class is primarily responsible for generating and managing unique identifiers for various functions within the OSIRIS plugin.
 */
public class IdGenerator {
	ElementCollector EC= new ElementCollector();

/**
 * @param functionName The `functionName` parameter specifies the name of the function for which an ID abbreviation will be suggested and potentially customized.
 * @return The `setID` method returns a `String` that represents the final identifier for a given function name. This identifier can be either a suggested abbreviation derived from the initial characters of each word in the function name, a custom ID entered by the user, or the suggested abbreviation if the operation is canceled. The return value ensures that an ID is always provided unless explicitly canceled by the user.
 * @summary The `setID` method generates an abbreviation based on a given function name and allows the user to either select this suggested ID or enter a custom one through a graphical dialog.
 */
	public String setID(String functionName) {
		// TODO Auto-generated method stub
		StringBuilder abbreviationBuilder = new StringBuilder();
		String[] words = functionName.split(" "); // Split the function name into words
		for (String word : words) {
			if (!word.isEmpty()) { // Ensure the word is not empty (e.g., from multiple spaces)
				abbreviationBuilder.append(word.charAt(0)); // Append the first character
			}
		}
		// Convert to uppercase for consistency
		String suggestedAbbreviation = abbreviationBuilder.toString().toUpperCase();

		// --- Step 2: Prepare components for the JOptionPane ---
		// Create a JTextField for the user to input their custom ID
		JTextField customIdInput = new JTextField(20); // 20 columns wide for better visibility

		// Create a panel to hold the message and the custom input field
		JPanel panel = new JPanel(new BorderLayout(5, 5)); // Use BorderLayout for better layout control
		panel.add(new JLabel("The ID is empty. Please choose an option or enter a new ID:"), BorderLayout.NORTH);
		panel.add(customIdInput, BorderLayout.CENTER);

		// Define the options (buttons) that will appear in the dialog
		Object[] options = {
				"Use Suggested ID: " + suggestedAbbreviation, // Option 1: Uses the generated abbreviation
				"Enter Custom ID",                               // Option 2: Allows user to type
				"Cancel"                                         // Option 3: To close the dialog
		};

		// --- Step 3: Show the JOptionPane to the user ---
		int choice = JOptionPane.showOptionDialog(
				null, // Parent component (null centers the dialog on screen)
				panel, // The custom panel containing message and text field
				functionName+" ID Suggestion", // Title of the dialog window
				JOptionPane.DEFAULT_OPTION, // Specifies that we're providing custom options
				JOptionPane.QUESTION_MESSAGE, // Type of message (determines the icon)
				null, // Icon (null uses the default icon for QUESTION_MESSAGE)
				options, // The array of button labels
				options[0] // The default selected option (first button)
				);

		// --- Step 4: Process the user's choice ---
		String finalID="";
		switch (choice) {
		case 0: // User clicked "Use Suggested ID: PCODO"
			finalID = suggestedAbbreviation;
			break;
		case 1: // User clicked "Enter Custom ID"
			finalID = customIdInput.getText(); // Get text from the custom input field
			break;
		case JOptionPane.CLOSED_OPTION: // User closed the dialog without selecting any button
		case 2: // User clicked "Cancel"
			finalID = suggestedAbbreviation; // Or you can set it to null, or handle as an error/cancellation
			JOptionPane.showMessageDialog(null, "ID generation cancelled.");
			break;
		default:
			finalID = suggestedAbbreviation; // Fallback for unexpected choices
			break;
		}

/**
 * @param purposes For the parameter `purposes` of type `String` in method `ID`: The `purposes` parameter specifies the intended use case for displaying the final generated ID.
 * @summary This Java method displays a dialog box showing the final chosen ID to the user.
 */
		// --- Display the final ID (for demonstration purposes) ---
		if (!finalID.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The chosen ID is: " + finalID);
		}


		return finalID;
	}

/**
 * @param selectedfunction The `selectedfunction` parameter represents the specific SysML model element (e.g., a block or activity) for which the method will retrieve the highest Failure Condition ID number.
 * @param function_ID The `function_ID` parameter of type `String` in the method `get_highest_FC_ID_number_for_function` is used to identify and retrieve the highest failure condition ID number associated with a specific aircraft system function.
 * @return The return value of the `get_highest_FC_ID_number_for_function` method is an integer representing the highest Failure Condition (FC) ID number associated with a specified function in the SysML model.
 * @summary This method retrieves the highest failure condition (FC) ID number for a specified aircraft system function.
 */
	public int get_highest_FC_ID_number_for_function(Element selectedfunction, String function_ID) {
		// TODO Auto-generated method stub
		String StereotypeName="Failure condition";
		Element Value_to_be_verified=selectedfunction;
		List<Element> ALL_FCs =new ArrayList<Element>();

		ALL_FCs=EC.get_all_elements_with_stereotype("Failure condition");
//	    List<Element> ElementList = new ArrayList<>();

		StereotypeFinder PF = new StereotypeFinder();
		Stereotype FC_Stereotype = PF.findstereotype(StereotypeName);
		Property Search_property = StereotypesHelper.getPropertyByName(FC_Stereotype, "Function");

		List<Element> FCs_oF_Function =new ArrayList<Element>();
		if(!(ALL_FCs==null)) {

//			FCs_oF_Function=EC.get_all_elements_with_specific_strereotype_property_value(ALL_FCs,StereotypeName,Property_of_stereotype,Value_to_be_verified);
			
			FCs_oF_Function=EC.get_all_elements_with_search_property_value(ALL_FCs,Search_property,Value_to_be_verified);
		}
		List<String> ID_Values =new ArrayList<String>();
		
		Property Value_collection_property=StereotypesHelper.getPropertyByName(FC_Stereotype, "ID");
		
		if(!(FCs_oF_Function==null)) {
			for(Element FC: FCs_oF_Function) {
				ID_Values.addAll(EC.get_String_value_specific_Property(FC,Value_collection_property));
			}
		}
		
		return highest_Value_of_fc_num_of_function(ID_Values, function_ID);
	}

	 /**
     * Finds the highest numerical value from IDs that match a specific function ID pattern.
     * IDs are expected to be in the format: function_ID + "_FC_" + number.
     *
     * @param iD_Values A list of ID strings to search through.
     * @param function_ID The base function ID string (e.g., "PCODO").
     * @return The highest numerical value found, or 0 if no matching IDs are found
     * or if parsing fails for all matching IDs.
     */
/**
 * @param iD_Values The `iD_Values` parameter represents a list of string identifiers used to determine the highest failure condition numerical value associated with a specific function.
 * @param function_ID The `function_ID` parameter in the method `highest_Value_of_fc_num_of_function` specifies the base identifier string used to match and filter relevant failure condition IDs for determining the highest numerical value associated with it.
 * @return The `highest_Value_of_fc_num_of_function` method returns the maximum numerical value found in a list of IDs that match a specific function prefix. This value represents the highest failure condition number associated with the given function ID within the provided list of IDs.
 * @summary The `highest_Value_of_fc_num_of_function` method determines the maximum numerical value associated with failure conditions of a given function in a list of string IDs.
 */
    private int highest_Value_of_fc_num_of_function(List<String> iD_Values, String function_ID) {
        int highestNum = 0; // Initialize with 0, as per the requirement

        // Construct the prefix pattern for the IDs we are looking for
        // We escape the function_ID to handle special characters if they were present
        String prefix = Pattern.quote(function_ID) + "_FC_";
        // Create a regex pattern to match the prefix followed by one or more digits
        Pattern pattern = Pattern.compile("^" + prefix + "(\\d+)$"); // ^ for start, $ for end, \\d+ for digits

        for (String id : iD_Values) {
            if (id == null) {
                continue; // Skip null IDs
            }

            Matcher matcher = pattern.matcher(id);
            if (matcher.find()) {
                try {
                    // Extract the numerical part (group 1 from the regex)
                    int currentNum = Integer.parseInt(matcher.group(1));
                    if (currentNum > highestNum) {
                        highestNum = currentNum; // Update if current number is higher
                    }
                } catch (NumberFormatException e) {
                    // This catch block handles cases where the extracted part is not a valid integer.
                    // For example, "PCODO_FC_ABC". We just ignore such IDs and continue.
                    System.err.println("Warning: Could not parse number from ID: " + id + " - " + e.getMessage());
                }
            }
        }
        return highestNum;
    }

    /**
     * Updates the keys of the formattedResponse map with new IDs based on a prefix
     * and an incrementally increasing number. The original map remains unchanged.
     *
     * @param formattedResponse The map whose keys are to be updated.
     * @param fC_ID_Prefix The prefix for the new IDs (e.g., "PVCO_FC_").
     * @param fC_ID_Num The starting number for the IDs. This number will be
     * incremented for each subsequent key.
     * @return A new Map with updated keys and the original values.
     */
    public Map<String, Map<String, List<String>>> update_response_with_new_IDS(
            Map<String, Map<String, List<String>>> formattedResponse, String fC_ID_Prefix, int fC_ID_Num) {

        // Create a new HashMap to store the results with the new IDs as keys
        Map<String, Map<String, List<String>>> updatedMap = new HashMap<>();

        // Iterate through each entry in the original formattedResponse map
        for (Map.Entry<String, Map<String, List<String>>> entry : formattedResponse.entrySet()) {
            // Get the value (the inner map) from the current entry
            Map<String, List<String>> value = entry.getValue();

            // Construct the new key using the provided prefix and the current number
            // The number is converted to a String and appended.
            String newKey = fC_ID_Prefix + fC_ID_Num;

            // Add the new key and the original value to the updated map
            updatedMap.put(newKey, value);

            // Increment the number for the next key
            fC_ID_Num++;
        }
        return updatedMap;
    }

}
