package restAPI;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.nomagic.magicdraw.core.Application;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `DLROllamaAPI` class is responsible for interfacing with the LLMs (Large Language Models) via an API to retrieve model names and details using provided API keys.
 */
public class DLROllamaAPI {

/**
 * @param _API_Key The `_API_Key` parameter is a string that serves as an authentication token for accessing and retrieving available models.
 * @return The `getModelName` method returns a `String` representing the name of the model selected by the user from a drop-down list. If no models are found, the user cancels the selection, or closes the dialog, the method returns `null`.
 * @summary The `getModelName` method presents a list of model IDs in a drop-down dialog to the user, allowing them to select and return the chosen model name.
 */
	public String getModelName(String _API_Key) {
		List<String> idList = getModelinDLROllama(_API_Key);

		if (idList == null || idList.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No models found.");
			return null;
		}

		// Create a JComboBox with the list of ids
		JComboBox<String> comboBox = new JComboBox<>(idList.toArray(new String[0]));

		// Show drop-down dialog to user to select model
		int result = JOptionPane.showConfirmDialog(null, comboBox, "Select a Model", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			// Return the selected item from the combo box
			return (String) comboBox.getSelectedItem();
		} else {
			// User cancelled or closed dialog
			return null;
		}
	}


/**
 * @param apiKey The `apiKey` parameter is a required string that serves as the authorization token for accessing the external model API.
 * @return The return value of the `getModelinDLROllama` method is a list of strings (`List<String>`) representing the IDs of models retrieved from the specified API endpoint. This list is populated by parsing the JSON response received from the API call using the provided API key for authorization.

In case of any issues with the API request or response, an empty list may be returned. The method handles various potential errors such as missing or invalid API keys and failed HTTP responses to ensure robust operation.
 * @summary The `getModelinDLROllama` method retrieves a list of model IDs from the DLR Ollama API using the provided API key.
 */

	public List<String> getModelinDLROllama(String apiKey) {
	    final List<String> idList = new ArrayList<>();

	    // Prefer HTTPS to avoid auth being lost on http->https redirects
	    final URI startUri = URI.create("https://ollama.nimbus.dlr.de/api/models");

	    if (apiKey == null || apiKey.isBlank()) {
	        throw new IllegalArgumentException("API key is missing or empty");
	    }

	    final String authValue = normaliseAuthorizationValue(apiKey);

	    try {
	        HttpClient client = HttpClient.newBuilder()
	                .connectTimeout(Duration.ofSeconds(10))
	                .followRedirects(HttpClient.Redirect.NEVER) // manual redirect handling
	                .version(HttpClient.Version.HTTP_1_1)       // helps with some proxies/gateways
	                .build();

	        HttpResponse<String> response = sendFollowingRedirects(client, startUri, authValue);

	        if (response.statusCode() == 401) {
	            // This is the single most useful clue for why you're getting 401
	            String wwwAuth = response.headers().firstValue("www-authenticate").orElse("<missing>");
	            String body = safeSnippet(response.body(), 2000);

	            Application.getInstance().getGUILog().showMessage(
	                    "401 Unauthorised\n" +
	                    "Request URI: " + response.uri() + "\n" +
	                    "WWW-Authenticate: " + wwwAuth + "\n" +
	                    "Body (snippet):\n" + body
	            );

	            throw new IOException("API call failed with status 401");
	        }

	        if (response.statusCode() < 200 || response.statusCode() >= 300) {
	            String body = safeSnippet(response.body(), 2000);
	            throw new IOException("API call failed with status " + response.statusCode() + "\nBody:\n" + body);
	        }

	        String body = response.body();
	        Object root = new JSONTokener(body).nextValue();

	        if (root instanceof JSONObject json) {
	            JSONArray arr = null;

	            if (json.has("data") && json.get("data") instanceof JSONArray) {
	                arr = json.getJSONArray("data");
	            } else if (json.has("models") && json.get("models") instanceof JSONArray) {
	                arr = json.getJSONArray("models");
	            } else if (json.has("id")) {
	                String id = json.optString("id", "").trim();
	                if (!id.isEmpty()) idList.add(id);
	                return Collections.unmodifiableList(idList);
	            }

	            if (arr != null) {
	                for (int i = 0; i < arr.length(); i++) {
	                    JSONObject obj = arr.optJSONObject(i);
	                    if (obj != null) {
	                        String id = obj.optString("id", "").trim();
	                        if (!id.isEmpty()) idList.add(id);
	                    }
	                }
	            }
	        } else if (root instanceof JSONArray arr) {
	            for (int i = 0; i < arr.length(); i++) {
	                JSONObject obj = arr.optJSONObject(i);
	                if (obj != null) {
	                    String id = obj.optString("id", "").trim();
	                    if (!id.isEmpty()) idList.add(id);
	                }
	            }
	        }

	    } catch (InterruptedException ie) {
	        Thread.currentThread().interrupt();
	        StringWriter sw = new StringWriter();
	        ie.printStackTrace(new PrintWriter(sw));
	        Application.getInstance().getGUILog().showMessage("Interrupted:\n" + sw);

	    } catch (Exception e) {
	        StringWriter sw = new StringWriter();
	        e.printStackTrace(new PrintWriter(sw));
	        Application.getInstance().getGUILog().showMessage("Error Occurred:\n" + sw);
	    }

	    return Collections.unmodifiableList(idList);
	}

	private static HttpResponse<String> sendFollowingRedirects(
	        HttpClient client, URI startUri, String authValue
	) throws IOException, InterruptedException {

	    URI uri = startUri;

	    for (int hop = 0; hop < 5; hop++) {
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(uri)
	                .timeout(Duration.ofSeconds(20))
	                .header("Accept", "application/json")
	                // Send BOTH; harmless if one is ignored, helpful if gateway expects X-API-Key.
	                .header("Authorization", authValue)
	                .header("X-API-Key", stripBearerIfPresent(authValue))
	                .GET()
	                .build();

	        HttpResponse<String> response = client.send(
	                request,
	                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
	        );

	        int code = response.statusCode();
	        if (code == 301 || code == 302 || code == 303 || code == 307 || code == 308) {
	            Optional<String> loc = response.headers().firstValue("location");
	            if (loc.isEmpty()) throw new IOException("Redirect without Location header (HTTP " + code + ")");
	            uri = uri.resolve(loc.get());
	            continue;
	        }

	        return response;
	    }

	    throw new IOException("Too many redirects for " + startUri);
	}

	private static String normaliseAuthorizationValue(String apiKey) {
	    String t = apiKey.trim();

	    // If caller already passed a full Authorization value, keep it (but tidy casing for Bearer).
	    if (t.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
	        String token = t.substring("Bearer ".length()).trim();
	        return "Bearer " + token;
	    }
	    if (t.regionMatches(true, 0, "Token ", 0, "Token ".length())
	            || t.regionMatches(true, 0, "ApiKey ", 0, "ApiKey ".length())
	            || t.regionMatches(true, 0, "Basic ", 0, "Basic ".length())) {
	        return t;
	    }

	    // Default
	    return "Bearer " + t;
	}

	private static String stripBearerIfPresent(String authValue) {
	    if (authValue.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
	        return authValue.substring("Bearer ".length()).trim();
	    }
	    return authValue.trim();
	}

	private static String safeSnippet(String s, int max) {
	    if (s == null) return "<null>";
	    String t = s.trim();
	    return (t.length() <= max) ? t : t.substring(0, max) + "\n…(truncated)…";
	}


/**
 * @param OLLAMA_DLR_API_KEY The `OLLAMA_DLR_API_KEY` parameter is a String that serves as the API key for authenticating requests to the DLR Ollama service.
 * @return The `getModelinDLROllama2` method retrieves a list of model IDs from the DLR API. The return value is a `List<String>` containing these model IDs.
 * @summary The `getModelinDLROllama2` method retrieves a list of model IDs from the Ollama DLR API using an authorization key and parses the JSON response to extract these IDs.
 */
	public List<String> getModelinDLROllama2(String OLLAMA_DLR_API_KEY){
		// Endpoint you want to hit
		String URL = "http://ollama.nimbus.dlr.de/api/models";

		List<String> idList = new ArrayList<>();

		try {
			// 1️⃣ Build the request
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(URL))
					.header("Authorization", "Bearer " + OLLAMA_DLR_API_KEY)
					.GET()
					.build();

			// 2️⃣ Send the request
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			Application.getInstance().getGUILog().showMessage(response.body());

			// 3️⃣ Handle the response
			if (response.statusCode() == 200) {
				String body = response.body();

				// Parse JSON
				JSONObject json = new JSONObject(body);

				if (json.has("data")) {
					JSONArray array = json.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						if (obj.has("id")) {
							idList.add(obj.getString("id"));
						}
					}
				} else if (json.has("id")) {
					idList.add(json.getString("id"));
				}
				//				} 
				//			else {
				//					Application.getInstance().getGUILog()
				//					.showMessage("Request failed with status code: " + response.statusCode());
				//				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Application.getInstance().getGUILog().showMessage("Error Occurred: \n" + exceptionAsString);
		}

		return idList;

	}

}



