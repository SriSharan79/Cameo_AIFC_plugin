package restAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_Sys_Func_with_HLS_ToLLMWithRAG` class is responsible for executing a given prompt with associated properties to generate or refine failure conditions using LLM (Language Learning Model) with RAG (Redundancy and Analysis Generation).
 */
public class For_Sys_Func_with_HLS_ToLLMWithRAG 
{
	
/**
 * @param Prompt The `Prompt` parameter of type `String` in the method `execute` represents the input string that serves as a prompt for generating failure conditions from SysML models and safety context.
 * @param promptpropertyMap A map containing property names and their corresponding values as lists of strings, used to populate a JSON payload for a REST API request.
 * @return The return value of the `execute` method is a `String` that provides feedback on the result of the HTTP request to the Flask REST API. It can be one of the following:

1. A success message containing the response from the server if the request was successful (HTTP status code 200).
2. An error message indicating the status code and response message if the request failed.
3. A failure message stating that the server is unreachable or there was a connection issue.

The returned `String` aims to inform the caller about the outcome of the HTTP operation, making it easier to handle successes and troubleshoot failures effectively.
 * @summary Executes an HTTP PUT request to a specified URL with provided prompt and project details, returning the API's response or error message.
 */
	public static String execute(String Prompt, Map<String, List<String>> promptpropertyMap) {
		Project project = Application.getInstance().getProject();
		String projectName =project.getFileName();
		
		// Get the file name without the folder path
        File file = new File(projectName);
        String fileName = file.getName(); // Extracts the file name with extension

        // Remove the file extension
        int lastDotIndex = fileName.lastIndexOf('.');
        String fileNameWithoutExt = (lastDotIndex > 0) ? fileName.substring(0, lastDotIndex) : fileName;
		
		HttpURLConnection conn = null; // Declare the connection outside of the try block to handle it in the finally block.
		promptpropertyMap.put("Prompt", List.of(Prompt));
		promptpropertyMap.put("ProjectName", List.of(fileNameWithoutExt));
		try {
			// URL of the Flask REST API
			String BASE_URL = "http://127.0.0.1:5000/fc_model_Promptmap_WithRAG4";
			URL url = new URL(BASE_URL);

			// Create connection
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json"); // Set content type to JSON


			// Convert promptpropertyMap to JSON format
			JSONObject payload = new JSONObject();
			for (Map.Entry<String, List<String>> entry : promptpropertyMap.entrySet()) {
				JSONArray jsonArray = new JSONArray(entry.getValue()); // Convert List<String> to JSONArray
				payload.put(entry.getKey(), jsonArray); // Add to the JSON object
			}

			// Enable output for the connection (since we are sending data)
			conn.setDoOutput(true);

			// Write the payload to the output stream
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.writeBytes(payload.toString());
				wr.flush();
			}

			// Get the response code
			int responseCode = conn.getResponseCode();
			//			Application.getInstance().getGUILog().showMessage("Response Code: " + responseCode);

			// Check if the request was successful
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				// Read the response line by line
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// Assuming the response is in JSON format
				JSONObject jsonResponse = new JSONObject(response.toString());

				//				// Print the response
				//				Application.getInstance().getGUILog().showMessage("Response: " + jsonResponse.getString("response"));
				return "Response: " + jsonResponse.getString("response");
			} else {
				Application.getInstance().getGUILog().showMessage("Failed with status code " + responseCode + ": " + conn.getResponseMessage());
				return "Failed with status code " + responseCode + ": " + conn.getResponseMessage();
			}

		} catch (IOException e) {
			// Handle the exception when the server is unreachable or there is a connection issue
			Application.getInstance().getGUILog().showMessage("Failed to connect to the server. Please check if the server is running.");
			e.printStackTrace();
			return "Failed to connect to the server. Please check if the server is running.";
		} finally {
			if (conn != null) {
				// Disconnect the connection
				conn.disconnect();
			}
		}
	}

}
