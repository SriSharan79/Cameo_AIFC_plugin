package responseParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `Main_ResponseParser_with_HLS_Sys_Func` class is responsible for parsing response data to integrate high-level system functions within the OSIRIS plugin framework, facilitating Functional Hazard Assessment (FHA) processes.
 */
public class Main_ResponseParser_with_HLS_Sys_Func2 {



	public Map<String, Map<String, List<String>>> parseFCResponse(String response) {
		// Try parsing with square brackets
		Map<String, Map<String, List<String>>> parsedOutput = Trypattern1(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}

		// Try parsing without square brackets
		parsedOutput = Trypattern2(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}

		// Try parsing without square brackets
		parsedOutput = Trypattern3(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}
		// Try parsing without square brackets
		parsedOutput = Trypattern4(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}
		
		// Try parsing without square brackets
		parsedOutput = Trypattern5(response);
		if (!parsedOutput.isEmpty()) {
			return parsedOutput;
		}


		// Log if parsing fails
		//        Application.getInstance().getGUILog().showMessage("Failed to parse the response");

		System.out.println("Failed to parse the response");
		return new HashMap<>();
	}

	private Map<String, Map<String, List<String>>> Trypattern5(String response) {
        // Dictionary to hold parsed output
        Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();

        // Updated pattern with flexibility for optional fields and labels
        Pattern pattern = Pattern.compile(
            "\\s*\\[?(FC-\\d{3})\\]?\\s*:" +                            // Match FC ID (e.g., [FC-001])
            "\\s*\"?(.*?)\"?\\s*" +                                    // Match Failure Condition Statement (optional quotes)
            "Description:\\s*(.*?)\\s*" +                              // Match Description
            "(?:Failure Type|Type):\\s*(.*?)\\s*" +                    // Match Failure Type (flexible label)
            "Phase:\\s*(.*?)\\s*" +                                    // Match Phase
            "(?:Effect (?:on|On) (?:Higher Level):)\\s*(.*?)\\s*" +    // Match Effect on Higher Level
            "(?:Effect (?:on|On) Aircraft:)?\\s*(.*?)\\s*" +           // Match Effect on Aircraft (optional)
            "(?:Effect (?:on|On) Crew:)?\\s*(.*?)\\s*" +               // Match Effect on Crew (optional)
            "(?:Effect (?:on|On) Occupants:)?\\s*(.*?)\\s*" +          // Match Effect on Occupants (optional)
            "(?:Hazard Classification|Classification):\\s*(.*?)" +    // Match Hazard Classification
            "(?=\\n\\[?FC-\\d{3}\\]?|$)",                             // Ensure proper boundary between entries
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
                String effectHLS = matcher.group(6).trim(); // Effect on Higher Level
                String effectAC = matcher.group(7) != null ? matcher.group(7).trim() : ""; // Effect on Aircraft
                String effectCW = matcher.group(8) != null ? matcher.group(8).trim() : ""; // Effect on Crew
                String effectOCC = matcher.group(9) != null ? matcher.group(9).trim() : ""; // Effect on Occupants
                String hazardClassification = matcher.group(10).trim(); // Hazard Classification

                // Split fields with multiple values separated by '/' or ','
                List<String> failureTypes = List.of(failureType.split("\\s*[,/]+\\s*"));
                List<String> phases = List.of(phase.split("\\s*[,/]+\\s*"));

                // Create a map to store the parsed details
                Map<String, List<String>> fcDetails = new HashMap<>();
                fcDetails.put("Failure Condition", List.of(fcStatementLabel));
                fcDetails.put("FC Description", List.of(fcDescription));
                fcDetails.put("Failure Type", failureTypes);
                fcDetails.put("Phase", phases);
                fcDetails.put("Effect On Higher Level", List.of(effectHLS));
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

	private Map<String, Map<String, List<String>>> Trypattern4(String response) {
		// Dictionary to hold parsed output
		Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();

		// Updated pattern to handle optional fields and flexible formatting
		Pattern pattern = Pattern.compile(
				"\\s*Failure Condition\\s+(FC-\\d{3}):" +                                 // FC ID
						"\\s*(.*?)\\s*" +                                                        // Failure Condition Statement
						"Failure Type:\\s*(.*?)\\s*" +                                           // Failure Type
						"Phase:\\s*(.*?)\\s*" +                                                  // Phase
						"Effect on Higher Level:\\s*(.*?)\\s*" +                                 // Effect on Higher Level
						"Effect on Aircraft:\\s*(.*?)\\s*" +                                     // Effect on Aircraft
						"(?:Effect on Crew:\\s*(.*?)\\s*)?" +                                    // Effect on Crew (optional)
						"(?:Effect on Occupants:\\s*(.*?)\\s*)?" +                               // Effect on Occupants (optional)
						"Hazard Classification:\\s*(.*?)\\s*(?=\\nFailure Condition|$)",        // Hazard Classification
						Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE           // Flags for flexibility
				);
		// Match the pattern against the input response
		Matcher matcher = pattern.matcher(response);

		while (matcher.find()) {
			try {
				// Match all fields
				String fcId = matcher.group(1).trim();
			    String failureCondition = matcher.group(2).trim();
			    String failureType = matcher.group(3).trim();
			    String phase = matcher.group(4).trim();
			    String effectHLS = matcher.group(5).trim();
			    String effectAC = matcher.group(6).trim();
			    String effectCW = matcher.group(7) != null ? matcher.group(7).trim() : "";
			    String effectOCC = matcher.group(8) != null ? matcher.group(8).trim() : "";
			    String hazardClassification = matcher.group(9).trim();

			    // Split lists
			    List<String> failureTypes = List.of(failureType.split("\\s*[,/]+\\s*"));
			    List<String> phases = List.of(phase.split("\\s*[,/]+\\s*"));

				// Determine FC Name
				String fcName = failureCondition;

				// Create a map to store the parsed details
				Map<String, List<String>> fcDetails = new HashMap<>();
				fcDetails.put("Failure Condition", List.of(failureCondition));
				fcDetails.put("FC Name", List.of(fcName));
				fcDetails.put("FC Description", List.of(failureCondition));
				fcDetails.put("Failure Type", failureTypes);
				fcDetails.put("Phase", phases);
				fcDetails.put("Effect On Higher Level", List.of(effectHLS));
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

	private Map<String, Map<String, List<String>>> Trypattern3(String response) {
		// Dictionary to hold parsed output
		Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();

		// Updated pattern to handle optional fields and flexible formatting
		Pattern pattern = Pattern.compile(
				"\\s*\\[?(FC-\\d{3})\\]?\\s*:" +                                       // FC ID
						"\\s*(.*?)\\s*Description:\\s*(.*?)\\s*" +                             // Description
						"(?:Failure Type|Type):\\s*(.*?)\\s*" +                                // Failure Type
						"Phase:\\s*(.*?)\\s*" +                                                // Phase
						"(?:Effect On Higher Level|Effects On Higher Level):\\s*(.*?)\\s*" +           // Effect On Higher Level
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
				String effectHLS = matcher.group(6).trim(); // Effect on Aircraft
				String effectAC = matcher.group(7).trim(); // Effect on Aircraft
				String effectCW = matcher.group(8) != null ? matcher.group(8).trim() : ""; // Optional
				String effectOCC = matcher.group(9) != null ? matcher.group(9).trim() : ""; // Optional
				String hazardClassification = matcher.group(10).trim(); // Hazard Classification

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
				fcDetails.put("Effect On Higher Level", List.of(effectHLS));
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
						"(?:Effect On Higher Level|Effects On Higher Level):\\s*(.*?)\\s*" +           // Effect on Aircraft
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
				String effectHLS = matcher.group(6).trim(); // Effect on Aircraft
				String effectAC = matcher.group(7).trim(); // Effect on Aircraft
				String effectCW = matcher.group(8) != null ? matcher.group(8).trim() : ""; // Optional
				String effectOCC = matcher.group(9) != null ? matcher.group(9).trim() : ""; // Optional
				String hazardClassification = matcher.group(10).trim(); // Hazard Classification

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
				fcDetails.put("Effect On Higher Level", List.of(effectHLS));
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
						"(?:Effect on Higher Level|Effects On Higher Level):\\s*(.*?)\\s*" +       // Flexible effect label
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
			String effectHLS = matcher.group(7).trim(); // Effect on Aircraf
			String effectAC = matcher.group(8).trim(); // Effect on Aircraft
			String effectCW = matcher.group(9).trim(); // Effect on Crew
			String effectOCC = matcher.group(10).trim(); // Effect on Occupants
			String hazardClassification = matcher.group(11).trim(); // Hazard Classification

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
			fcDetails.put("Effect On Higher Level", List.of(effectHLS));
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
