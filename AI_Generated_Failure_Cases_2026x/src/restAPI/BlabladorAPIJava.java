package restAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class BlabladorAPIJava {

    private static final String BLABLADOR_BASE_URL = "https://api.helmholtz-blablador.fz-juelich.de/v1";
    private String apiKey;
    private String model;

    public BlabladorAPIJava() {}

    public BlabladorAPIJava(String apiKey, String model) {
        this.apiKey = resolveApiKey(apiKey);
        // FIX: Ensure the model name is strictly trimmed so no hidden spaces break the JSON payload
        this.model = (model == null || model.trim().isEmpty()) ? "alias-code" : model.trim();
    }

    public BlabladorAPIJava(String apiKey) {
        this(apiKey, "alias-code");
    }

    private String resolveApiKey(String inputKey) {
        if (inputKey != null && inputKey.toLowerCase().endsWith(".txt")) {
            try {
                return Files.readString(Paths.get(inputKey), StandardCharsets.UTF_8).trim();
            } catch (Exception e) {
                System.err.println("Could not read API key from file: " + inputKey);
            }
        }
        
        if (inputKey == null || inputKey.trim().isEmpty()) {
             try {
                Path defaultPath = Paths.get("Blablador_API_Key.txt");
                if (Files.exists(defaultPath)) {
                     return Files.readString(defaultPath, StandardCharsets.UTF_8).trim();
                }
             } catch (Exception e) {
                 System.err.println("Default Blablador_API_Key.txt not found.");
             }
        }
        return inputKey == null ? "" : inputKey.trim();
    }

    public String getModelName(String _API_Key) {
        List<String> idList = getModelinBlablador(resolveApiKey(_API_Key));

        if (idList == null || idList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Blablador models found. Check your API key.");
            return null;
        }

        JComboBox<String> comboBox = new JComboBox<>(idList.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(null, comboBox, "Select a Blablador Model", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return (String) comboBox.getSelectedItem();
        } else {
            return null;
        }
    }

    public List<String> getModelinBlablador(String keyParam) {
        List<String> idList = new ArrayList<>();
        String actualKey = resolveApiKey(keyParam);
        
        if (actualKey == null || actualKey.isBlank()) {
            return idList;
        }

        try {
            URL url = new URL(BLABLADOR_BASE_URL + "/models");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + actualKey);
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(response.toString());
                while (matcher.find()) {
                    String extractedId = matcher.group(1);
                    if (!extractedId.isEmpty() && !idList.contains(extractedId)) {
                        idList.add(extractedId);
                    }
                }
            } else {
                System.err.println("Blablador Models API returned HTTP " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(idList);
    }

    public String generateCompletion(String systemPrompt, String userPrompt) throws Exception {
        URL url = new URL(BLABLADOR_BASE_URL + "/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        // FIX: Replaced "application/json; utf-8" with the standard compliant format.
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + this.apiKey); 
        conn.setDoOutput(true);
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(120000);

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"model\":\"").append(escapeJson(this.model)).append("\",");
        jsonBuilder.append("\"messages\":[");

        if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
            jsonBuilder.append("{\"role\":\"system\",\"content\":\"")
                       .append(escapeJson(systemPrompt))
                       .append("\"},");
        }

        jsonBuilder.append("{\"role\":\"user\",\"content\":\"")
                   .append(escapeJson(userPrompt))
                   .append("\"}");
        jsonBuilder.append("],");
        jsonBuilder.append("\"temperature\":0.2");
        jsonBuilder.append("}");

        byte[] input = jsonBuilder.toString().getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        BufferedReader reader;
        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            StringBuilder err = new StringBuilder();
            String errLine;
            while ((errLine = reader.readLine()) != null) {
                err.append(errLine);
            }
            reader.close();
            throw new RuntimeException("Blablador API returned HTTP " + responseCode + ": " + err.toString());
        }

        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            response.append(responseLine.trim());
        }
        reader.close();

        return extractContentFromOpenAIResponse(response.toString());
    }

    private String extractContentFromOpenAIResponse(String json) {
        Pattern pattern = Pattern.compile("\"content\"\\s*:\\s*\"(.*?)(?<!\\\\)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String content = matcher.group(1);
            return unescapeJson(content);
        }
        return json;
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    private String unescapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
    }

    public void setModel(String model) {
        this.model = model != null ? model.trim() : "alias-code";
    }

    public void setApiKey(String apiKey) {
        this.apiKey = resolveApiKey(apiKey);
    }
}