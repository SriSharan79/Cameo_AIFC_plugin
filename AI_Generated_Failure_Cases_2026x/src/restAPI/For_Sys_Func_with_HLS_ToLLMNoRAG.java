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
 * @summary The `For_Sys_Func_with_HLS_ToLLMNoRAG` class is responsible for executing a function with High Level Safety (HLS) considerations and mapping it to an LLM's input without RAG involvement.
 */
public class For_Sys_Func_with_HLS_ToLLMNoRAG 
{

/**
 * @param Prompt The `Prompt` parameter of type `String` in the `execute` method serves as the input prompt to be sent to the Flask REST API for processing.
 * @param promptpropertyMap The `promptpropertyMap` parameter in the `execute` method is a map that associates property keys with lists of string values, used to construct the JSON payload for sending to the Flask REST API.
 * @return The return value of the `execute` method in Java is a `String`. This string represents either the response received from a Flask REST API after processing the provided prompt and project information, or an error message detailing any issues encountered during the execution process. If successful, it contains a specific key-value pair from the JSON response. If not, it provides details on what went wrong, such as connection failures or HTTP errors.
 * @summary The primary responsibility of the `execute` method is to send a given prompt and project-specific data via an HTTP PUT request to a Flask REST API and return the response received from the server.
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
			String BASE_URL = "http://127.0.0.1:5000/fc_model_Promptmap_NoRAG4";
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
