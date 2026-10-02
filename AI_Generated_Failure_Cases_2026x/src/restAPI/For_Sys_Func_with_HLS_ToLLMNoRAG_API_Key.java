package restAPI;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nomagic.magicdraw.core.Application;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_Sys_Func_with_HLS_ToLLMNoRAG_API_Key` class is responsible for executing prompts and facilitating communication with a language model using provided API keys and system messages.
 */
public class For_Sys_Func_with_HLS_ToLLMNoRAG_API_Key 
{

/**
 * @param Prompt The `Prompt` parameter is the input text provided by the user that will be sent to the language model for processing.
 * @param Ollama_API_Key The `Ollama_API_Key` parameter is a String that contains the API key required to authenticate and authorize access to the Ollama model for processing messages.
 * @param sys_Msg The `sys_Msg` parameter represents the system message content used to set context for the AI model's interaction, ensuring consistent and relevant responses.
 * @param modelName2 `modelName2`: The name of the model to be used for processing the input prompt.
 * @return The `execute` method returns a `String` representing the response from the model after processing the provided prompt and system message. This response is generated through an interaction with an external API, using the given API key and model name. If an error occurs during this process, it returns null.
 * @summary The `execute` method prepares a chat request with system and user messages and invokes an external model to process this request, returning the response.
 */
public static String execute(String Prompt, String Ollama_API_Key, String sys_Msg, String modelName2) {
		
		String ollamaDLRAPIKey =Ollama_API_Key;
		String sysPrompt=sys_Msg;

//        Application.getInstance().getGUILog().showMessage("\n reached passing method");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode messages = objectMapper.createArrayNode();
//        Application.getInstance().getGUILog().showMessage("\n Object Mapper and Array Node Created");
        
        
        if (sysPrompt != null && !sysPrompt.isEmpty()) {
            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", sysPrompt);
            messages.add(systemMessage);
        }
        
        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", Prompt);
        messages.add(userMessage);
//        Application.getInstance().getGUILog().showMessage("\n Messages are populated: "+ messages);
        
        try {
            return chatWithModel(ollamaDLRAPIKey, messages, objectMapper,modelName2);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
		

	}

/**
 * @param token The `token` parameter in the method `chatWithModel` is a String that represents the authorization token required for authenticating API requests to the chat model service.
 * @param messages The `messages` parameter is an ArrayNode representing the sequence of messages to be sent in the chat request.
 * @param objectMapper `ObjectMapper objectMapper`: Jackson library instance used to serialize request data into JSON format and deserialize response data from JSON format.
 * @param modelName2 `modelName2`: The name of the model to use for generating chat completions.
 * @return The `chatWithModel` method sends a request to a specified model API, processes the response, and returns the content of the message received from the model. If the content is successfully extracted as text, it will be returned as a String; otherwise, it returns `null`.
 * @summary The `chatWithModel` method sends a chat request to a specified model via an HTTP POST request, processes the JSON response to extract and return the content of the message.
 */
	private static String chatWithModel(String token, ArrayNode messages, ObjectMapper objectMapper, String modelName2)throws IOException, InterruptedException {

//        Application.getInstance().getGUILog().showMessage("\n Transfer to model started");
        	HttpClient client = HttpClient.newHttpClient();
	        String url = "https://ollama.nimbus.dlr.de/api/chat/completions";

	        ObjectNode requestBody = objectMapper.createObjectNode();
	        requestBody.put("model", modelName2);
	        requestBody.set("messages", messages);
	        requestBody.put("max_tokens", 150);
	        


	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(url))
	                .header("Authorization", "Bearer " + token)
	                .header("Content-Type", "application/json")
	                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
	                .build();
//	        Application.getInstance().getGUILog().showMessage("\n request is created");

	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	        String responseBody = response.body();

//	        Application.getInstance().getGUILog().showMessage("\n Model provided response");
	        
//            Application.getInstance().getGUILog().showMessage(responseBody);

	        JsonNode responseJson = objectMapper.readTree(responseBody);

	        JsonNode contentNode = responseJson
	                                .path("choices")
	                                .path(0)
	                                .path("message")
	                                .path("content");

	        if (contentNode.isTextual()) {
	            String content = contentNode.asText();
//	            Application.getInstance().getGUILog().showMessage("\nThe safely accessed value of 'content' is: " + content);
	            return content;
	        } else {
	            Application.getInstance().getGUILog().showMessage(responseJson.toPrettyString()); // Print the full JSON for debugging
	            Application.getInstance().getGUILog().showMessage("\nCould not find 'content' value using safe access (one or more keys/indices were missing).");
	            return null;
	        }
	    }
	

}
