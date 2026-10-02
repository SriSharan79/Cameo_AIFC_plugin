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
 * @summary The `For_AC_Functions_ToLLMNoRAG` class is responsible for executing and handling prompts with associated properties to interface with a language model, generating relevant outputs based on the provided input.
 */
public class For_AC_Functions_ToLLMNoRAG 
{

/**
 * @param Prompt The `Prompt` parameter in the `execute` method represents a string input that serves as the initial text prompt for generating failure conditions, which is sent to an external API endpoint.
 * @param promptpropertyMap The `promptpropertyMap` parameter in the `execute` method is a map that associates property names with their corresponding values as lists of strings, used to construct the JSON payload for the API request.
 * @return The `execute` method's return value is a `String` that represents the response from the Flask REST API. This can be either the actual response content if the request was successful, or an error message describing the failure reason (e.g., status code, connection issues).
 * @summary The `execute` method sends a prompt and project name to a Flask REST API, retrieves the response, and returns it as a string.
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
			String BASE_URL = "http://127.0.0.1:5000/fc_model_Promptmap_NoRAG3";
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
