package requestCreation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;

import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.StereotypeSearch;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `GlossaryFinder` class is responsible for updating and validating textual data against a glossary, extracting terms from SysML elements, and processing strings into individual words.
 */
public class GlossaryFinder {

	NameDerivator ND= new NameDerivator();

	StereotypeSearch StereotypeChecker=new StereotypeSearch();

/**
 * @param Data The `Data` parameter of type `String` in the method `updateWithGlossary` is a text input that may contain glossary terms to be updated or replaced based on the project's glossary.
 * @return The `updateWithGlossary` method returns a String that has been processed to replace words found in the glossary with their corresponding definitions or updated forms. If the input is null or empty, it returns the original input unchanged. The purpose of this method is to update the given text based on predefined glossary entries.
 * @summary The `updateWithGlossary` method updates a given input string by replacing its words with their glossary equivalents based on a project-specific glossary.
 */
	public String updateWithGlossary(String Data) {
        if (Data == null || Data.isEmpty()) {
            return Data;
        }

        String[] individualWords = splitTextIntoWords(Data);
        StringBuilder resultBuilder = new StringBuilder();
        int lastIndex = 0;    Map<String, List<String>> Map_of_All_Glossary_elements = new HashMap<>();
        Map_of_All_Glossary_elements = getAllGlossaryInProject();

        for (String word : individualWords) {
            int wordStartIndex = Data.indexOf(word, lastIndex);

            if (wordStartIndex != -1) {
                resultBuilder.append(Data.substring(lastIndex, wordStartIndex));
                String updatedWord = checkGlossaryAndAppend(word,Map_of_All_Glossary_elements);
                resultBuilder.append(updatedWord);
                lastIndex = wordStartIndex + word.length();
            } else {
                resultBuilder.append(word);
            }
        }

        if (lastIndex < Data.length()) {
            resultBuilder.append(Data.substring(lastIndex));
        }

        return resultBuilder.toString();
    }

    
/**
 * @param word `word`: The input word to be checked against and potentially appended with glossary references.
 * @param Map_of_All_Glossary_elements The `Map_of_All_Glossary_elements` parameter is a list of strings representing the glossary entries associated with each term.
 * @return The `checkGlossaryAndAppend` method returns a `String` that represents the original input word with appended glossary references if any are found and selected. If no references are found or none are selected, it returns the original word unchanged.
 * @summary The `checkGlossaryAndAppend` method checks if a given word exists in a glossary map and appends corresponding references to the word based on user selection from a dialog.
 */
    private String checkGlossaryAndAppend(String word, Map<String, List<String>> Map_of_All_Glossary_elements) {
//        // 1. Strip trailing punctuation for glossary lookup
//        String cleanedWord = word.replaceAll("[.,;:]+$", ""); // Remove one or more of .,; at the end
//        // You might want to remove leading punctuation too, e.g., word.replaceAll("^[.,;:]+|[.,;:]+$", "");
//        // Or remove all punctuation if that's the desired behavior: word.replaceAll("\\p{Punct}", "");
//
//        // 2. Convert to lowercase for case-insensitive lookup
////        String wordToLookup = cleanedWord.toLowerCase();

        if (Map_of_All_Glossary_elements.containsKey(word)) {
            List<String> references = Map_of_All_Glossary_elements.get(word);

            if (references.isEmpty()) {
                // If the list is empty, it shall not change the word string at all
                return word;
            } else if (references.size() == 1) {
                // If it only one, then it shall change the word string in following Word(value of Key)
                return word + " (" + references.get(0) + ") ";
            } else {
                // If the list has multiple values of strings, then it shall prompt JOption pane
                // where all the strings are presented the following way:
                // "the Following word has respective reference : value1, value 2..etc"
                // and use check boxes for each value selection.
                // make sure that user can select multiple options.

                JCheckBox[] checkBoxes = new JCheckBox[references.size()];
                StringBuilder message = new StringBuilder("The following word \"" + word + "\" has respective references\nPlease Choose relevant reference from the options:\n");

                for (int i = 0; i < references.size(); i++) {
                    checkBoxes[i] = new JCheckBox(references.get(i));
//                    message.append("- ").append(references.get(i)).append("\n"); // Optional: for display only if not using checkboxes directly in message
                }

                // Create a panel to hold the message and checkboxes
                Object[] uiComponents = new Object[1 + checkBoxes.length];
                uiComponents[0] = message.toString(); // The descriptive message
                System.arraycopy(checkBoxes, 0, uiComponents, 1, checkBoxes.length);

                int option = JOptionPane.showConfirmDialog(
                    null,
                    uiComponents,
                    "Select Reference(s) for " + word,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if (option == JOptionPane.OK_OPTION) {
                    List<String> selectedValues = new ArrayList<>();
                    for (JCheckBox checkBox : checkBoxes) {
                        if (checkBox.isSelected()) {
                            selectedValues.add(checkBox.getText());
                        }
                    }

                    if (!selectedValues.isEmpty()) {
                        // If multiple are selected, then the Word string shall change Word (Value1 ;Value2;...)
                        // If only one is selected, it will be Word(Value1)
                        return word + " (" + String.join(" ; ", selectedValues) + ") ";
                    } else {
                        // If user clicks OK but selects nothing, return original word
                        return word;
                    }
                } else {
                    // If user clicks Cancel, it shall not change the word string at all
                    return word;
                }
            }
        }
        // If the key is not found in the map, it shall not change the word string at all
        return word;
    }

	private Map<String, List<String>> getAllGlossaryInProject() {
		// TODO Auto-generated method stub

		Project project = Application.getInstance().getProject();

//		StringBuilder Logger = new StringBuilder();

		   Map<String, List<String>> Map_of_All_Glossary_elements = new HashMap<>(); 
		   List<Element> Term_Element_List = new ArrayList<>();

	        if (project != null) {
	            List<Package> models = project.getModels();
	            if (models != null) {
	                for (Package model : models) {
	                	Term_Element_List.addAll(getAllTerms(model));
	                }
	            }
	        }
	        
	        for (Element term : Term_Element_List) {
				List<String> Comments_Body_List = new ArrayList<>();
				String term_Name = ND.getName(term);
				var Comments = term.getOwnedComment();
				for (Comment comment : Comments) {
					Comments_Body_List.add(comment.getBody());
				}
				Map_of_All_Glossary_elements.put(term_Name, Comments_Body_List);
			}
	        
	        // Iterate through the map's entry set
//	        for (Map.Entry<String, List<String>> entry : Map_of_All_Glossary_elements.entrySet()) {
//	            String key = entry.getKey();
//	            List<String> values = entry.getValue();
//
//	            Logger.append("Key: ").append(key);
//	            Logger.append(", Values: ");
//
//	            if (values.isEmpty()) {
//	                Logger.append("[No values]");
//	            } else {
//	                Logger.append("[");
//	                for (int i = 0; i < values.size(); i++) {
//	                    Logger.append(values.get(i));
//	                    if (i < values.size() - 1) {
//	                        Logger.append(", "); // Add comma for all but the last value
//	                    }
//	                }
//	                Logger.append("]");
//	            }
//	            Logger.append(System.lineSeparator()); // Add a new line for each entry
//	        }
////	        Application.getInstance().getGUILog().showMessage("Glossary terms: \n"+Logger);
			return Map_of_All_Glossary_elements;
	}

/**
 * @param element The `element` parameter in the method `getAllTerms` is a SysML model element that serves as the root for recursively gathering all term elements.
 * @return The `getAllTerms` method returns a list of elements that are considered terms. These elements are collected recursively from the given element and its owned elements, based on some internal criteria for what constitutes a term element (likely determined by the `IsATermElement` method). If the input element is null, an empty list is returned instead.
 * @summary The `getAllTerms` method recursively collects all term elements related to a given element within a SysML model.
 */
	private List<Element> getAllTerms(Element element) {
		// TODO Auto-generated method stub

		   List<Element> Term_Element_List = new ArrayList<>();
	        if (element == null) {
	            return Term_Element_List; // Return empty list if element is null
	        }
	        if(IsATermElement(element)) {
	        	Term_Element_List.add(element);
	        	return Term_Element_List;
	         }

	        // Get owned elements of the current element
	        var ownedElements = element.getOwnedElement();  
	        if (ownedElements != null) {
	            for (Element ownedElement : ownedElements) {
	    	        if(IsATermElement(ownedElement)) {
	    	        	Term_Element_List.add(ownedElement);
	    	        }
                	Term_Element_List.addAll(getAllTerms(ownedElement));
	            }
	        }
		return Term_Element_List;
	}

/**
 * @param element The `element` parameter is a SysML model element that the method checks for a specific stereotype to determine if it is associated with a term.
 * @return The return value of the `IsATermElement` method indicates whether the provided SysML model element has the "Term" stereotype applied. If it does, the method returns `true`, otherwise it returns `false`.
 * @summary The `IsATermElement` method checks if a given Element has the stereotype "Term".
 */
	private boolean IsATermElement(Element element) {
		// TODO Auto-generated method stub
		if(StereotypeChecker.hasStereotypeBoolean(element, "Term")) {
			return true;
		}
		return false;
	}


/**
 * @param text The `text` parameter is the input string that needs to be divided into individual words.
 * @return The `splitTextIntoWords` method returns an array of strings (`String[]`) containing the individual words extracted from the input string. Each word is separated by one or more whitespace characters (such as spaces, tabs, and newlines). If the input string is null or empty after trimming, an empty array is returned.
 * @summary This Java method splits a given string into an array of words based on whitespace separation.
 */
    public static String[] splitTextIntoWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new String[0]; // Return an empty array for null or empty input
        }
        // Use regex "\\s+" to split by one or more whitespace characters
        // This handles spaces, tabs, newlines, etc., effectively splitting words in paragraphs too.
        return text.trim().split("\\s+");
    }
}
