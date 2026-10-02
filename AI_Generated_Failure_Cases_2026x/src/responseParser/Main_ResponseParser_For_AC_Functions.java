package responseParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nomagic.magicdraw.core.Application;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `Main_ResponseParser_For_AC_Functions` class is responsible for parsing responses related to aircraft functions in the OSIRIS plugin, specifically handling inputs and outputs to generate and refine failure conditions within the context of SysML models.
 */
public class Main_ResponseParser_For_AC_Functions {


	public Map<String, Map<String, List<String>>> parseFCResponse(String response) {
		Map<String, Map<String, List<String>>> parsedOutput;

		// Attempt pattern 1
		parsedOutput = Trypattern1(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}

		// Attempt pattern 2
		parsedOutput = Trypattern2(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}

		// Attempt pattern 3
		parsedOutput = Trypattern3(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}
		
		if (!parsedOutput.isEmpty()) {
			Application.getInstance().getGUILog().showMessage("Failed for parse the response");
		}

		// Add more patterns as needed

		// Return empty map if all patterns fail
		return new HashMap<>();
	}


	private Map<String, Map<String, List<String>>> Trypattern3(String response) {
		// Dictionary to hold parsed output
		Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();

		// Updated pattern to handle optional fields and flexible formatting
		Pattern pattern = Pattern.compile(
				"\\s*\\[?(FC-\\d{3})\\]?\\s*:" +                                       // FC ID
						"\\s*(.*?)\\s*Description:\\s*(.*?)\\s*" +                             // Description
						"(?:Failure Type|Type):\\s*(.*?)\\s*" +                                // Failure Type
						"Phase:\\s*(.*?)\\s*" +                                                // Phase
						"(?:Effect On Aircraft|Effects On Aircraft):\\s*(.*?)\\s*" +           // Effect on Aircraft
						"(?:Effect On Crew|Effects On Crew)?\\s*:\\s*(.*?)\\s*" +              // Effect on Crew (optional)
						"(?:Effect On Occupants|Effects On Occupants)?\\s*:\\s*(.*?)\\s*" +    // Effect on Occupants (optional)
						"(?:Hazard Classification|Classification):\\s*(.*?)(?=\\nFC-\\d{3}|$)", // Hazard Classification
						Pattern.DOTALL | Pattern.MULTILINE
				);

		// Match the pattern against the input response
		Matcher matcher = pattern.matcher(response);

		while (matcher.find()) {
			try {
				// Match all fields
				String fcId = matcher.group(1).trim(); // FC ID
				String fcStatementLabel = matcher.group(2).trim(); // Failure condition statement
				String fcDescription = matcher.group(3).trim(); // Description
				String failureType = matcher.group(4).trim(); // Failure Type
				String phase = matcher.group(5).trim(); // Phase
				String effectAC = matcher.group(6).trim(); // Effect on Aircraft
				String effectCW = matcher.group(7) != null ? matcher.group(7).trim() : ""; // Optional
				String effectOCC = matcher.group(8) != null ? matcher.group(8).trim() : ""; // Optional
				String hazardClassification = matcher.group(9).trim(); // Hazard Classification

				// Split fields with multiple values separated by '/' or ','
				List<String> failureTypes = List.of(failureType.split("\\s*[,/]+\\s*"));
				List<String> phases = List.of(phase.split("\\s*[,/]+\\s*"));

				// Determine FC Name
				String fcName = fcStatementLabel;

				// Create a map to store the parsed details
				Map<String, List<String>> fcDetails = new HashMap<>();
				fcDetails.put("Failure Condition", List.of(fcStatementLabel));
				fcDetails.put("FC Name", List.of(fcName));
				fcDetails.put("FC Description", List.of(fcDescription));
				fcDetails.put("Failure Type", failureTypes);
				fcDetails.put("Phase", phases);
				fcDetails.put("Effect On Aircraft", List.of(effectAC));
				fcDetails.put("Effect On Crew", List.of(effectCW));
				fcDetails.put("Effect On Occupants", List.of(effectOCC));
				fcDetails.put("Hazard Classification", List.of(hazardClassification));

				// Add the details to the parsed output map
				parsedOutput.put(fcId, fcDetails);
			} catch (Exception e) {
				System.out.println("Error processing entry: " + e.getMessage());
			}
		}

		return parsedOutput;
	}


	private Map<String, Map<String, List<String>>> Trypattern2(String response) {
		// Dictionary to hold parsed output
		Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();

		// Updated pattern to handle optional fields and flexible formatting
		Pattern pattern = Pattern.compile(
				"\\s*\\[?(FC-\\d{3})\\]?\\s*:" +                                       // FC ID
						"\\s*(.*?)\\s*Description:\\s*(.*?)\\s*" +                             // Description
						"(?:Failure Type|Type):\\s*(.*?)\\s*" +                                // Failure Type
						"Phase:\\s*(.*?)\\s*" +                                                // Phase
						"(?:Effect On Aircraft|Effects On Aircraft):\\s*(.*?)\\s*" +           // Effect on Aircraft
						"(?:Effect On Crew|Effects On Crew)?:\\s*(.*?)\\s*" +                  // Effect on Crew (optional)
						"(?:Effect On Occupants|Effects On Occupants)?:\\s*(.*?)\\s*" +        // Effect on Occupants (optional)
						"(?:Hazard Classification|Classification):\\s*(.*?)(?=\\nFC-\\d{3}|$)", // Hazard Classification
						Pattern.DOTALL | Pattern.MULTILINE
				);

		// Match the pattern against the input response
		Matcher matcher = pattern.matcher(response);

		while (matcher.find()) {
			try {
				// Match all fields
				String fcId = matcher.group(1).trim(); // FC ID
				String fcStatementLabel = matcher.group(2).trim(); // Failure condition statement
				String fcDescription = matcher.group(3).trim(); // Description
				String failureType = matcher.group(4).trim(); // Failure Type
				String phase = matcher.group(5).trim(); // Phase
				String effectAC = matcher.group(6).trim(); // Effect on Aircraft
				String effectCW = matcher.group(7) != null ? matcher.group(7).trim() : ""; // Optional
				String effectOCC = matcher.group(8) != null ? matcher.group(8).trim() : ""; // Optional
				String hazardClassification = matcher.group(9).trim(); // Hazard Classification

				// Split fields with multiple values separated by '/' or ','
				List<String> failureTypes = List.of(failureType.split("\\s*[,/]+\\s*"));
				List<String> phases = List.of(phase.split("\\s*[,/]+\\s*"));

				// Determine FC Name
				String fcName = fcStatementLabel;

				// Create a map to store the parsed details
				Map<String, List<String>> fcDetails = new HashMap<>();
				fcDetails.put("Failure Condition", List.of(fcStatementLabel));
				fcDetails.put("FC Name", List.of(fcName));
				fcDetails.put("FC Description", List.of(fcDescription));
				fcDetails.put("Failure Type", failureTypes);
				fcDetails.put("Phase", phases);
				fcDetails.put("Effect On Aircraft", List.of(effectAC));
				fcDetails.put("Effect On Crew", List.of(effectCW));
				fcDetails.put("Effect On Occupants", List.of(effectOCC));
				fcDetails.put("Hazard Classification", List.of(hazardClassification));

				// Add the details to the parsed output map
				parsedOutput.put(fcId, fcDetails);
			} catch (Exception e) {
				System.out.println("Error processing entry: " + e.getMessage());
			}
		}

		return parsedOutput;
	}

	private Map<String, Map<String, List<String>>> Trypattern1(String response) {

		// Dictionary to hold parsed output
		Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();
		// Updated pattern to handle both bracketed and non-bracketed FC statements
		Pattern pattern = Pattern.compile(
				"\\s*\\[?(FC-\\d{3})\\]?:" +                                       // Entry number and FC ID
						"\\s*(?:\\[(.*?)\\]|(.*?)(?:\\.|:))?\\s*" +                        // Failure condition in brackets or as free text
						"Description:\\s*(.*?)\\s*" +                                      // Description
						"(?:Failure Type|Type):\\s*(.*?)\\s*" +                            // Flexible failure type label
						"Phase:\\s*(.*?)\\s*" +                                            // Phase
						"(?:Effect on Aircraft|Effects On Aircraft):\\s*(.*?)\\s*" +       // Flexible effect label
						"(?:Effect on Crew|Effects On Crew):\\s*(.*?)\\s*" +               // Flexible effect label
						"(?:Effect on Occupants|Effects On Occupants):\\s*(.*?)\\s*" +     // Flexible effect label
						"(?:Hazard Classification|Classification):\\s*(.*?)(?=\\n\\d+\\.|$)", // Classification with entry boundary
						Pattern.DOTALL | Pattern.MULTILINE
				);     


		// Match the pattern against the input response
		Matcher matcher = pattern.matcher(response);

		while (matcher.find()) {
			// Match all fields
			String fcId = matcher.group(1).trim(); // FC ID
			String fcStatementLabel = matcher.group(2) != null ? matcher.group(2).trim() :
				(matcher.group(3) != null ? matcher.group(3).trim() : ""); // Failure condition statement
			String fcDescription = matcher.group(4).trim(); // Description
			String failureType = matcher.group(5).trim(); // Failure Type or Type
			String phase = matcher.group(6).trim(); // Phase
			String effectAC = matcher.group(7).trim(); // Effect on Aircraft
			String effectCW = matcher.group(8).trim(); // Effect on Crew
			String effectOCC = matcher.group(9).trim(); // Effect on Occupants
			String hazardClassification = matcher.group(10).trim(); // Hazard Classification

			// Determine FC Name
			String fcName = fcStatementLabel;

			// Split fields with multiple values separated by '/' or ','
			List<String> failureTypes = List.of(failureType.split("\\s*[,/]+\\s*"));
			List<String> phases = List.of(phase.split("\\s*[,/]+\\s*"));

			// Create a map to store the parsed details
			Map<String, List<String>> fcDetails = new HashMap<>();
			fcDetails.put("Failure Condition", List.of(fcStatementLabel));
			fcDetails.put("FC Name", List.of(fcName));
			fcDetails.put("FC Description", List.of(fcDescription));
			fcDetails.put("Failure Type", failureTypes);
			fcDetails.put("Phase", phases);
			fcDetails.put("Effect On Aircraft", List.of(effectAC));
			fcDetails.put("Effect On Crew", List.of(effectCW));
			fcDetails.put("Effect On Occupants", List.of(effectOCC));
			fcDetails.put("Hazard Classification", List.of(hazardClassification));

			// Add the details to the parsed output map
			parsedOutput.put(fcId, fcDetails);
		}

		return parsedOutput;
	}


}

