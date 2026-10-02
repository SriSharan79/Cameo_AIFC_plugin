package requestCreation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nomagic.magicdraw.core.Application;

import dataloggers.BackgroundRunner;
import responseParser.Main_ResponseParser_For_AC_Functions;
import responseParser.Main_ResponseParser_with_HLS_Sys_Func;
import restAPI.For_AC_Functions_ToLLMNoRAG;
import restAPI.For_AC_Functions_ToLLMNoRAG_API_Key;
import restAPI.For_AC_Functions_ToLLMWithRAG;
import restAPI.For_Sys_Func_with_HLS_ToLLMNoRAG;
import restAPI.For_Sys_Func_with_HLS_ToLLMNoRAG_API_Key;
import restAPI.For_Sys_Func_with_HLS_ToLLMWithRAG;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `RequestSender` class is responsible for sending requests to an external API and generating responses using different configurations based on the provided parameters.
 */
public class RequestSender {

	boolean API_key;
	String Ollama_key;
	String ModelName;
	String Sys_Msg;

	BackgroundRunner BR=new BackgroundRunner();

	Main_ResponseParser_with_HLS_Sys_Func parser_HLS = new Main_ResponseParser_with_HLS_Sys_Func();

	Main_ResponseParser_For_AC_Functions parser_AC = new Main_ResponseParser_For_AC_Functions();

/**
 * @param using_API_key The `using_API_key` parameter indicates whether to use the provided API key for authentication in this request sender.
 * @param ollama_key2 `The `ollama_key2` parameter is a String representing the API key for accessing Ollama's services, used only if `using_API_key` is true.`
 * @param SysMsg The `SysMsg` parameter in the `RequestSender` method represents the system message used to initiate or modify an API request.
 * @param modelName The `modelName` parameter specifies the name of the model to be used for processing the request.
 * @return The `RequestSender` method returns an instance of the `RequestSender` class, initialized with specified parameters such as whether to use an API key, the API key itself, a system message, and the model name. This object is used to manage and send requests to the LLM system within the OSIRIS plugin for Cameo Systems Modeler.
 * @summary The `RequestSender` method initializes an object to handle requests using specified parameters for API key usage, Ollama key, system message, and model name.
 */
	public RequestSender(boolean using_API_key, String ollama_key2,String SysMsg, String modelName) {
		// TODO Auto-generated constructor stub
		this.API_key=using_API_key;
		this.Ollama_key=ollama_key2;
		this.Sys_Msg=SysMsg;
		this.ModelName=modelName;
	}

/**
 * @param prompt The `prompt` parameter is a string input that specifies the query or context for generating a response from the safety assessment model.
 * @param propertyMap The parameter `propertyMap` is a map containing key-value pairs where the key is a String and the value is a list of Strings, used to configure specific properties for generating responses with the RAG API.
 * @return The return value of the `generateResponse_With_RAG` method is a `String` that represents the response generated based on the provided prompt and safety context. This string can either be a detailed functional analysis using high-level functions or a more generalized analysis depending on the availability and configuration of an API key and high-level function lists in the given property map.
 * @summary The method `generateResponse_With_RAG` is responsible for generating a response based on the provided prompt and property map, using either an API key or specific system functions, depending on the presence of high-level functions.
 */
	public  String generateResponse_With_RAG(String prompt, Map<String, List<String>> propertyMap) throws IOException {

		List<String> HLFlist= new ArrayList<>();

		if(propertyMap.containsKey("UpperLevelFunctions")) {
			HLFlist = propertyMap.get("UpperLevelFunctions");
		}
		// Currently the RAG with API is need to be updated
		if(API_key) {
			boolean useHighLevelFunctions = (HLFlist != null && !HLFlist.isEmpty());
			final int MAX_TRIES = 10; // Define the maximum number of attempts here
			// Start the recursion with the first attempt (currentTry = 1)

			BR.Notifyinbackground("IDxx","Request Sent to Selected  Model");
			return generateResponseRecursivelyWithAPIKey(prompt, Ollama_key.toString(), useHighLevelFunctions, 1, MAX_TRIES,ModelName);
		}
		else {
			if ((HLFlist != null && !HLFlist.isEmpty())){
				return For_Sys_Func_with_HLS_ToLLMWithRAG.execute(prompt, propertyMap);
			}else {
				return For_AC_Functions_ToLLMWithRAG.execute(prompt, propertyMap);
			}
		}
	}

/**
 * @param prompt The `prompt` parameter is a string that represents the input text used to generate responses from the language model without using Retrieval-Augmented Generation (RAG).
 * @param propertyMap The `propertyMap` parameter is a map containing key-value pairs where the keys are strings and the values are lists of strings, used to pass specific properties or configurations required for generating responses without using Retrieval-Augmented Generation (RAG).
 * @return The return value of the `generateResponse_Without_RAG` method is a `String`. This string represents the generated response based on the given prompt and properties map. If an API key is available, it uses recursive generation with high-level functions if applicable. Otherwise, it generates a response without using Retrieval-Augmented Generation (RAG) for either system functions or aircraft functions depending on the provided property map.
 * @summary The `generateResponse_Without_RAG` method is responsible for generating a response based on the given prompt and property map, either by using an API key for recursive generation or executing specific functions depending on the presence of high-level functions.
 */
	public  String generateResponse_Without_RAG(String prompt, Map<String, List<String>> propertyMap) throws IOException {
//		Application.getInstance().getGUILog().showMessage("Decision on API Key:"+API_key+"\n With API Key: "+Ollama_key.toString());

		List<String> HLFlist= new ArrayList<>();

		if(propertyMap.containsKey("UpperLevelFunctions")) {
			HLFlist = propertyMap.get("UpperLevelFunctions");
		}
		if(API_key) {
			boolean useHighLevelFunctions = (HLFlist != null && !HLFlist.isEmpty());
			final int MAX_TRIES = 10; // Define the maximum number of attempts here
			// Start the recursion with the first attempt (currentTry = 1)

			BR.Notifyinbackground("IDxx","Request Sent to Selected  Model");
			return generateResponseRecursivelyWithAPIKey(prompt, Ollama_key.toString(), useHighLevelFunctions, 1, MAX_TRIES,ModelName);

		}
		else {

			if ((HLFlist != null && !HLFlist.isEmpty())){
				return For_Sys_Func_with_HLS_ToLLMNoRAG.execute(prompt, propertyMap);
			}else {
				return For_AC_Functions_ToLLMNoRAG.execute(prompt, propertyMap);
			}
		}
	}

	/**
	 * Recursively generates a response using the API key, with a maximum retry limit.
	 * This method handles the logic when API_key is true.
	 *
	 * @param prompt The input prompt for the language model.
	 * @param ollamaKey The Ollama API key as a String.
	 * @param useHighLevelFunctions A boolean indicating whether to use high-level functions logic.
	 * @param currentTry The current attempt number (starts from 1).
	 * @param maxTries The maximum number of attempts allowed.
	 * @param modelName2 
	 * @return The generated response string, or null/error message if maxTries is reached.
	 * @throws IOException 
	 */
/**
 * @param prompt The `prompt` parameter is a String representing the input query or command sent to the LLM for generating a response.
 * @param ollamaKey The `ollamaKey` parameter is the API key used for authenticating requests to the Ollama LLM services.
 * @param useHighLevelFunctions `useHighLevelFunctions`: Determines whether to utilize high-level functions for processing the response.
 * @param currentTry The `currentTry` parameter in the method `generateResponseRecursivelyWithAPIKey` represents the current attempt number during recursive API calls to generate a valid response.
 * @param maxTries The `maxTries` parameter specifies the maximum number of recursive attempts allowed to generate a valid response for the given prompt.
 * @param modelName2 The `modelName2` parameter specifies the identifier of the language model to be used in generating the response.
 * @return The `generateResponseRecursivelyWithAPIKey` method returns a `String` that represents the response generated from an external API call. If the response is successfully parsed and valid, this `String` will contain the parsed response data. If parsing fails after all retry attempts, it returns `null`.
 * @summary The `generateResponseRecursivelyWithAPIKey` method is responsible for recursively generating and validating a response to a prompt using an API key, with support for both high-level functions and fallback mechanisms.
 */
	private String generateResponseRecursivelyWithAPIKey(String prompt, String ollamaKey, boolean useHighLevelFunctions, int currentTry, final int maxTries, String modelName2) throws IOException {
		// Base case: If we've exceeded the maximum number of tries, stop recursion.
		if (currentTry > maxTries) {
			Application.getInstance().getGUILog().showMessage("Max tries (" + maxTries + ") reached for prompt: " + prompt + ". Could not generate a valid response.");
			return null; // Or throw a specific exception, or return a predefined error message.
		}
//		Application.getInstance().getGUILog().showMessage("Recursive method");
		String response;

		if (useHighLevelFunctions) {
			response = For_Sys_Func_with_HLS_ToLLMNoRAG_API_Key.execute(prompt, ollamaKey,Sys_Msg,modelName2);
			if (parser_HLS.parseFCResponse(response) != null) {
				BR.Notifyinbackground("ID1"," response parsing passed (Try " + currentTry + "/" + maxTries + ")");
				BR.logModelInbackground(currentTry, modelName2, Sys_Msg, prompt, "passed ",response);

				return response;
			} else {

//				Application.getInstance().getGUILog().showMessage("log Start");
				BR.logModelInbackground(currentTry, modelName2, Sys_Msg, prompt, "failed ",response);
				BR.Notifyinbackground("ID2","High-level function response parsing failed (Try " + currentTry + "/" + maxTries + "). Retrying...");
				// Recursive call, incrementing currentTry
				return generateResponseRecursivelyWithAPIKey(prompt, ollamaKey, useHighLevelFunctions, currentTry + 1, maxTries,modelName2);
			}
		} else {
			response = For_AC_Functions_ToLLMNoRAG_API_Key.execute(prompt, ollamaKey,Sys_Msg,modelName2);
			if (parser_AC.parseFCResponse(response) != null) {

//				Application.getInstance().getGUILog().showMessage("log Start");
				BR.Notifyinbackground("ID3"," response parsing passed (Try " + currentTry + "/" + maxTries + ")");
				BR.logModelInbackground(currentTry, modelName2, Sys_Msg, prompt, "passed ",response);
				return response;
			} else {

//				Application.getInstance().getGUILog().showMessage("log Start");

				BR.logModelInbackground(currentTry, modelName2, Sys_Msg, prompt, "passed ",response);

				BR.Notifyinbackground("ID4","System function response parsing failed (Try " + currentTry + "/" + maxTries + "). Falling back to AC Functions...");

				return generateResponseRecursivelyWithAPIKey(prompt, ollamaKey, useHighLevelFunctions, currentTry + 1, maxTries,modelName2);
			}
		}
	}

}
