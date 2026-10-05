package responseParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.nomagic.magicdraw.core.Application;

/**
 * @author kata_du
 * @version 2.0.0
 * @summary A robust parser that splits LLM responses into blocks and extracts fields dynamically for HLS Sys Functions.
 */
public class Main_ResponseParser_with_HLS_Sys_Func {

    public Map<String, Map<String, List<String>>> parseFCResponse(String response) {
        Map<String, Map<String, List<String>>> parsedOutput = new HashMap<>();

        // Sanitize markdown
        String sanitizedResponse = response.replaceAll("\\*\\*", "");

        // Split into FC blocks
        Pattern blockPattern = Pattern.compile("(\\[?FC-\\d{3}\\]?\\s*:.*?)(?=\\[?FC-\\d{3}\\]?\\s*:|$)", Pattern.DOTALL);
        Matcher blockMatcher = blockPattern.matcher(sanitizedResponse);

        while (blockMatcher.find()) {
            String block = blockMatcher.group(1);

            try {
                // Extract FC ID
                Matcher idMatcher = Pattern.compile("\\[?(FC-\\d{3})\\]?").matcher(block);
                if (!idMatcher.find()) continue;
                String fcId = idMatcher.group(1).trim();

                // Extract Fields dynamically
                String statementRaw = extractField(block, "\\[?FC-\\d{3}\\]?\\s*:");
                String fcStatementLabel = statementRaw.replaceAll("(?i)^Failure Condition statement\\s*:\\s*", "").trim();
                
                String fcDescription = extractField(block, "(?i)Description\\s*:");
                String failureType = extractField(block, "(?i)(?:Failure Type|Type)\\s*:");
                String phase = extractField(block, "(?i)Phase\\s*:");
                String effectHLS = extractField(block, "(?i)(?:Effect [oO]n Higher Level|Effects [oO]n Higher Level)\\s*:");
                String effectAC = extractField(block, "(?i)(?:Effect [oO]n Aircraft|Effects [oO]n Aircraft)\\s*:");
                String effectCW = extractField(block, "(?i)(?:Effect [oO]n Crew|Effects [oO]n Crew)\\s*:");
                String effectOCC = extractField(block, "(?i)(?:Effect [oO]n Occupants|Effects [oO]n Occupants)\\s*:");
                String hazardClassification = extractField(block, "(?i)(?:Hazard Classification|Classification)\\s*:");

                List<String> failureTypes = List.of(failureType.split("\\s*[,/]+\\s*"));
                List<String> phases = List.of(phase.split("\\s*[,/]+\\s*"));

                Map<String, List<String>> fcDetails = new HashMap<>();
                fcDetails.put("Failure Condition", List.of(fcStatementLabel));
                fcDetails.put("FC Name", List.of(fcStatementLabel));
                fcDetails.put("FC Description", List.of(fcDescription));
                fcDetails.put("Failure Type", failureTypes);
                fcDetails.put("Phase", phases);
                fcDetails.put("Effect On Higher Level", List.of(effectHLS));
                fcDetails.put("Effect On Aircraft", List.of(effectAC));
                fcDetails.put("Effect On Crew", List.of(effectCW));
                fcDetails.put("Effect On Occupants", List.of(effectOCC));
                fcDetails.put("Hazard Classification", List.of(hazardClassification));

                parsedOutput.put(fcId, fcDetails);
            } catch (Exception e) {
                System.out.println("Error processing entry: " + e.getMessage());
            }
        }

        if (parsedOutput.isEmpty()) {
            Application.getInstance().getGUILog().showMessage("Failed to parse the response");
        }
        return parsedOutput;
    }

    private String extractField(String block, String startRegex) {
        String nextBoundary = "(?i)(?:Description|Failure Type|Type|Phase|Effect [oO]n Higher Level|Effects [oO]n Higher Level|Effect [oO]n Aircraft|Effects [oO]n Aircraft|Effect [oO]n Crew|Effects [oO]n Crew|Effect [oO]n Occupants|Effects [oO]n Occupants|Hazard Classification|Classification)\\s*:|$";
        
        Pattern p = Pattern.compile(startRegex + "(.*?)(?=" + nextBoundary + ")", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(block);
        if (m.find()) {
            String match = m.group(1).trim();
            if (match.startsWith("\"") && match.endsWith("\"") && match.length() > 1) {
                match = match.substring(1, match.length() - 1).trim();
            }
            return match;
        }
        return "";
    }
}