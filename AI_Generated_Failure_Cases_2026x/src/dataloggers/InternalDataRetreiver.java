package dataloggers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class InternalDataRetreiver {
    private final String CONFIGFILENAME = ".Api_key_filepath_config.txt";
    
    // Default file names, but can be updated based on provider
    private String APIKEYFILENAME = "Ollama_API_Key.txt";

    public Path targetDirPath;
    public Path ApiKeyFilePath;
    public Path SYSMSGFilePath;
    public String currentApiKey; // Replaces 'Ollamakey'
    public String SysMSGsys;
    public String SysMSGac;
    public String currentProvider = "Ollama (Local)"; // Default

    private final javax.swing.JPanel owner;

    public InternalDataRetreiver(javax.swing.JPanel owner) {
        this.owner = owner;
    }

    /**
     * Initializes keys dynamically based on the active provider.
     */
    public void initializeAndLoadKey(String SYSMSGFILENAME, String provider) {
        this.currentProvider = provider;
        
        // Dynamically assign the file name based on provider
        if ("Helmholtz Blablador".equals(provider)) {
            APIKEYFILENAME = "Blablador_API_Key.txt";
        } else {
            APIKEYFILENAME = "Ollama_API_Key.txt";
        }

        String storedDirPathStr = readFilePathFromConfig();
        Path tempTargetDirPath = null;
        Path tempKeyFilePath = null;
        Path tempACSYSMSGFilePath = null;
        boolean needToPromptForNewDir = false;

        if (storedDirPathStr != null && !storedDirPathStr.isEmpty()) {
            tempTargetDirPath = Paths.get(storedDirPathStr);
            tempKeyFilePath = tempTargetDirPath.resolve(APIKEYFILENAME);
            tempACSYSMSGFilePath = tempTargetDirPath.resolve(SYSMSGFILENAME);
            
            if (Files.exists(tempTargetDirPath) && Files.isDirectory(tempTargetDirPath) && Files.exists(tempKeyFilePath)) {
                this.targetDirPath = tempTargetDirPath;
                this.ApiKeyFilePath = tempKeyFilePath;
            } else {
                JOptionPane.showMessageDialog(owner,
                        "The API key file for " + provider + " is missing: " + tempKeyFilePath.toAbsolutePath() +
                        "\nPlease enter or save your new API key.", "Key Missing", JOptionPane.INFORMATION_MESSAGE);
                this.targetDirPath = tempTargetDirPath;
                this.ApiKeyFilePath = tempKeyFilePath; // Keep path to create it later
            }
            
            if (Files.exists(tempTargetDirPath) && Files.isDirectory(tempTargetDirPath) && Files.exists(tempACSYSMSGFilePath)) {
                this.SYSMSGFilePath = tempACSYSMSGFilePath;
            } else {
                JOptionPane.showMessageDialog(owner,
                        "System message file is missing: " + tempACSYSMSGFilePath.toAbsolutePath(), "Configuration Invalid", JOptionPane.INFORMATION_MESSAGE);
                this.SYSMSGFilePath = tempACSYSMSGFilePath;
            }
        } else {
            JOptionPane.showMessageDialog(owner,
                    "No API key storage directory found. Please select a directory.", "Select Directory", JOptionPane.INFORMATION_MESSAGE);
            needToPromptForNewDir = true;
        }

        if (needToPromptForNewDir) {
            Path selectedPath = promptForDirectorySelection();
            if (selectedPath != null) {
                this.targetDirPath = selectedPath;
                this.ApiKeyFilePath = this.targetDirPath.resolve(APIKEYFILENAME);
                this.SYSMSGFilePath = this.targetDirPath.resolve(SYSMSGFILENAME);
                writeFilePathToConfig(this.targetDirPath.toAbsolutePath().toString());

                try {
                    Files.createDirectories(this.targetDirPath);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(owner,
                            "Error creating selected directory: " + e.getMessage(), "Directory Creation Error", JOptionPane.ERROR_MESSAGE);
                    this.targetDirPath = null;
                    this.ApiKeyFilePath = null;
                }
            } else {
                JOptionPane.showMessageDialog(owner,
                        "No directory selected. API key functionality will be disabled.", "Selection Cancelled", JOptionPane.WARNING_MESSAGE);
                this.targetDirPath = null;
                this.ApiKeyFilePath = null;
            }
        }
        
        loadKeyFromFileInternal();
        loadSysMsgFromFileInternal();
    }

    public Path promptForDirectorySelection() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select Directory for API Key Storage");
        if (targetDirPath != null && Files.exists(targetDirPath) && Files.isDirectory(targetDirPath)) {
            fileChooser.setCurrentDirectory(targetDirPath.toFile());
        } else {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        }

        int userSelection = fileChooser.showSaveDialog(owner);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        }
        return null;
    }

    private Path getConfigFileLocation() {
        return Paths.get(System.getProperty("user.home"), CONFIGFILENAME);
    }

    public void writeFilePathToConfig(String path) {
        try {
            Files.writeString(getConfigFileLocation(), path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner, "Error writing config file: " + e.getMessage(), "Config Write Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String readFilePathFromConfig() {
        Path configFile = getConfigFileLocation();
        if (Files.exists(configFile)) {
            try {
                return Files.readString(configFile).trim();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public void loadKeyFromFileInternal() {
        currentApiKey = "";
        if (ApiKeyFilePath == null) return;
        
        if (Files.exists(ApiKeyFilePath)) {
            try {
                currentApiKey = Files.readString(ApiKeyFilePath, StandardCharsets.UTF_8).trim();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(owner, "Error reading API key file: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
                currentApiKey = "";
            }
        }
    }

    public void loadSysMsgFromFileInternal() {
        SysMSGsys = "";
        if (SYSMSGFilePath == null) return;
        
        if (Files.exists(SYSMSGFilePath)) {
            try {
                SysMSGsys = Files.readString(SYSMSGFilePath, StandardCharsets.UTF_8).trim();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(owner, "Error reading System message file.", "File Read Error", JOptionPane.ERROR_MESSAGE);
                SysMSGsys = "";
            }
        }
    }

    public void updateAndSavePassword(javax.swing.JFormattedTextField apiKeyField) {
        if (apiKeyField.isEditable()) {
            currentApiKey = apiKeyField.getText().trim();
            savePasswordToFile(currentApiKey);
        }
    }

    public void savePasswordToFile(String keyToSave) {
        if (targetDirPath == null || ApiKeyFilePath == null) {
            JOptionPane.showMessageDialog(owner, "Cannot save API Key: Storage directory is not configured.", "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Files.createDirectories(targetDirPath);
            Files.writeString(ApiKeyFilePath, keyToSave, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner, "Error writing API key to file: " + e.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}