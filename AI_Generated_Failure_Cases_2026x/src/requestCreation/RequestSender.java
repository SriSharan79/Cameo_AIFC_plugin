package requestCreation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.task.ProgressStatus;
import com.nomagic.task.RunnableWithProgress;
import com.nomagic.ui.ProgressStatusRunner;

import dataloggers.BackgroundRunner;
import responseParser.Main_ResponseParser_For_AC_Functions;
import responseParser.Main_ResponseParser_with_HLS_Sys_Func;
import restAPI.For_AC_Functions_ToLLMNoRAG;
import restAPI.For_AC_Functions_ToLLMNoRAG_API_Key;
import restAPI.For_AC_Functions_ToLLMWithRAG;
import restAPI.For_Sys_Func_with_HLS_ToLLMNoRAG;
import restAPI.For_Sys_Func_with_HLS_ToLLMNoRAG_API_Key;
import restAPI.For_Sys_Func_with_HLS_ToLLMWithRAG;

public class RequestSender {

    String provider; 
    boolean API_key;
    String Ollama_key;
    String ModelName;
    String Sys_Msg;

    BackgroundRunner BR = new BackgroundRunner();

    Main_ResponseParser_with_HLS_Sys_Func parser_HLS = new Main_ResponseParser_with_HLS_Sys_Func();
    Main_ResponseParser_For_AC_Functions parser_AC = new Main_ResponseParser_For_AC_Functions();

    public RequestSender(String provider, boolean using_API_key, String ollama_key2, String SysMsg, String modelName) {
        this.provider = provider;
        this.API_key = using_API_key;
        this.Ollama_key = ollama_key2;
        this.Sys_Msg = SysMsg;
        this.ModelName = modelName;
    }

    public String generateResponse_With_RAG(String prompt, Map<String, List<String>> propertyMap) throws IOException {
        List<String> HLFlist = propertyMap.getOrDefault("UpperLevelFunctions", new ArrayList<>());
        
        if (API_key || "Helmholtz Blablador".equals(provider)) {
            boolean useHighLevelFunctions = (HLFlist != null && !HLFlist.isEmpty());
            return executeWithProgress(prompt, useHighLevelFunctions);
        } else {
            if (HLFlist != null && !HLFlist.isEmpty()) {
                return For_Sys_Func_with_HLS_ToLLMWithRAG.execute(prompt, propertyMap);
            } else {
                return For_AC_Functions_ToLLMWithRAG.execute(prompt, propertyMap);
            }
        }
    }

    public String generateResponse_Without_RAG(String prompt, Map<String, List<String>> propertyMap) throws IOException {
        List<String> HLFlist = propertyMap.getOrDefault("UpperLevelFunctions", new ArrayList<>());
        
        if (API_key || "Helmholtz Blablador".equals(provider)) {
            boolean useHighLevelFunctions = (HLFlist != null && !HLFlist.isEmpty());
            return executeWithProgress(prompt, useHighLevelFunctions);
        } else {
            if (HLFlist != null && !HLFlist.isEmpty()) {
                return For_Sys_Func_with_HLS_ToLLMNoRAG.execute(prompt, propertyMap);
            } else {
                return For_AC_Functions_ToLLMNoRAG.execute(prompt, propertyMap);
            }
        }
    }

    private String executeWithProgress(final String prompt, final boolean useHighLevelFunctions) throws IOException {
        final int MAX_TRIES = 10; 
        final String[] result = new String[1];
        final IOException[] error = new IOException[1];

        RunnableWithProgress task = new RunnableWithProgress() {
            @Override
            public void run(ProgressStatus progressStatus) {
                progressStatus.init("Initializing request to " + provider, 0, MAX_TRIES);
                try {
                    result[0] = generateResponseRecursivelyWithAPIKey(prompt, Ollama_key, useHighLevelFunctions, 1, MAX_TRIES, ModelName, progressStatus);
                } catch (IOException e) {
                    error[0] = e;
                }
            }
        };

        ProgressStatusRunner.runWithProgressStatus(task, "LLM Request in Progress", true, 0);

        if (error[0] != null) throw error[0];
        return result[0];
    }

 // Inside RequestSender.java
    private String generateResponseRecursivelyWithAPIKey(String prompt, String ollamaKey, boolean useHighLevelFunctions, int currentTry, final int maxTries, String modelName2, ProgressStatus progressStatus) throws IOException {
            
        if (progressStatus != null && progressStatus.isCancel()) {
            Application.getInstance().getGUILog().showMessage("Task cancelled by user.");
            return null;
        }

        if (currentTry > maxTries) {
            Application.getInstance().getGUILog().showMessage("Max tries (" + maxTries + ") reached for prompt: " + prompt + ". Could not generate a valid response.");
            return null; 
        }

        if (progressStatus != null) {
            progressStatus.setDescription("Generating response... (Try " + currentTry + " of " + maxTries + ")");
            progressStatus.setCurrent(currentTry - 1);
        }

        String response;
        
        int rowIndex = -1;
        try {
            rowIndex = BR.logRequestSent(currentTry, modelName2, Sys_Msg, prompt);
        } catch (IOException e) {
            Application.getInstance().getGUILog().showMessage("Failed to log request start: " + e.getMessage());
        }

        if (useHighLevelFunctions) {
            
            // FIX: Removed if/else provider block. Routing is now entirely handled dynamically by the executor.
            response = For_Sys_Func_with_HLS_ToLLMNoRAG_API_Key.execute(prompt, ollamaKey, Sys_Msg, modelName2, provider);

            if (parser_HLS.parseFCResponse(response) != null) {
                if (progressStatus != null) progressStatus.setCurrent(maxTries);
                BR.Notifyinbackground("ID1", " response parsing passed (Try " + currentTry + "/" + maxTries + ")");
                if (rowIndex != -1) BR.logResponseReceived(rowIndex, "passed", response);
                return response;
            } else {
                if (rowIndex != -1) BR.logResponseReceived(rowIndex, "failed", response);
                BR.Notifyinbackground("ID2", "High-level function response parsing failed. Retrying...");
                return generateResponseRecursivelyWithAPIKey(prompt, ollamaKey, useHighLevelFunctions, currentTry + 1, maxTries, modelName2, progressStatus);
            }
            
        } else {
            
            // FIX: Removed if/else provider block. Routing is now entirely handled dynamically by the executor.
            response = For_AC_Functions_ToLLMNoRAG_API_Key.execute(prompt, ollamaKey, Sys_Msg, modelName2, provider);

            if (parser_AC.parseFCResponse(response) != null) {
                if (progressStatus != null) progressStatus.setCurrent(maxTries);
                BR.Notifyinbackground("ID3", " response parsing passed (Try " + currentTry + "/" + maxTries + ")");
                if (rowIndex != -1) BR.logResponseReceived(rowIndex, "passed", response);
                return response;
            } else {
                if (rowIndex != -1) BR.logResponseReceived(rowIndex, "failed", response);
                BR.Notifyinbackground("ID4", "System function response parsing failed. Retrying...");
                return generateResponseRecursivelyWithAPIKey(prompt, ollamaKey, useHighLevelFunctions, currentTry + 1, maxTries, modelName2, progressStatus);
            }
        }
    }
}