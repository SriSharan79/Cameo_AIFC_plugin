package restAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ToEmbeddModel` class is responsible for executing the embedding of provided strings into a model, returning the resulting string representation.
 */
public class ToEmbeddModel 
{
/**
 * @param stringsForEmbedding `stringsForEmbedding`: A list of strings containing model data that will be embedded and sent as part of the JSON payload to the Flask REST API.
 * @return The `execute` method returns a String that describes the result of sending an HTTP PUT request to a specified Flask REST API. The return value can be one of the following:

1. A success message containing the response from the server if the request was successful.
2. An error message with the status code and response message from the server if the request failed.
3. An error message indicating that the server is unreachable or there is a connection issue.

This return value provides feedback on the outcome of the HTTP PUT request, allowing for further handling or logging based on its content.
 * @summary The `execute` method sends a list of strings for embedding to a Flask REST API, handles the response, and returns it as a string.
 */
	public static String execute(List<String> stringsForEmbedding) {

		Project project = Application.getInstance().getProject();
		String projectName =project.getFileName();
		 // Add a new string at the 1st index (index 0)
		stringsForEmbedding.add(0, projectName);
		
		HttpURLConnection conn = null; // Declare the connection outside of the try block to handle it in the finally block.
		try {
			// URL of the Flask REST API
			String BASE_URL = "http://127.0.0.1:5000/fc_model_ModelEmbedding";
			URL url = new URL(BASE_URL);

			// Create connection
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json"); // Set content type to JSON


	        // Create a JSONObject
	        JSONObject payload = new JSONObject();

	        // Convert the list to a JSONArray and add it to the JSONObject
	        payload.put("Model Data", new JSONArray(stringsForEmbedding));

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
