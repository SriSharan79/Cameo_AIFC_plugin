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
import com.nomagic.magicdraw.core.Application;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `InternalDataRetreiver` class is responsible for managing and retrieving configuration data, including handling API keys and system messages from specified file locations.
 */
public class InternalDataRetreiver {
    private final String CONFIGFILENAME = ".Api_key_filepath_config.txt";
    private final String APIKEYFILENAME = "Ollama_API_Key.txt";

    public Path targetDirPath;
    public Path OllamaKeyFilePath;
    public Path SYSMSGFilePath;
    public String Ollamakey;
    public String SysMSGsys;

    public String SysMSGac;

    // The owner panel for dialogs
    private final javax.swing.JPanel owner;

/**
 * @param owner The parameter `owner` is a reference to the main graphical panel that will host the InternalDataRetriever's components and manage their layout.
 * @return The return value of the Java method `InternalDataRetriever` represents an instance of `InternalDataRetriever` that has been initialized with a specified owner component, typically a graphical user interface panel. This object is responsible for managing and retrieving internal data within the context of the provided GUI element.
 * @summary The `InternalDataRetriever` class is responsible for initializing and managing data retrieval operations within the context of a given JPanel component in the GUI.
 */
    public InternalDataRetreiver(javax.swing.JPanel owner) {
        this.owner = owner;
    }

/**
 * @param SYSMSGFILENAME SYSMSGFILENAME: The name of the system message file to be loaded or verified.
 * @summary Initializes the system by loading the API key and System message file paths from a configuration, validating their existence, prompting for directory selection if necessary, and ensuring they are correctly stored for future use.
 */
    public void initializeAndLoadKey(String SYSMSGFILENAME) {
        String storedDirPathStr = readFilePathFromConfig();
        Path tempTargetDirPath = null;
        Path tempOllamaKeyFilePath = null;
        Path tempACSYSMSGFilePath = null;
        boolean needToPromptForNewDir = false;

        if (storedDirPathStr != null && !storedDirPathStr.isEmpty()) {
            tempTargetDirPath = Paths.get(storedDirPathStr);
            tempOllamaKeyFilePath = tempTargetDirPath.resolve(APIKEYFILENAME);
            tempACSYSMSGFilePath = tempTargetDirPath.resolve(SYSMSGFILENAME);
            if (Files.exists(tempTargetDirPath) && Files.isDirectory(tempTargetDirPath) && Files.exists(tempOllamaKeyFilePath)) {
                this.targetDirPath = tempTargetDirPath;
                this.OllamaKeyFilePath = tempOllamaKeyFilePath;
//                //Application.getInstance().getGUILog().showMessage("Loaded valid API key path from config: " + this.OllamaKeyFilePath.toAbsolutePath());
            } else {
                //Application.getInstance().getGUILog().showMessage("Configured API key path or file is invalid/missing: " + tempOllamaKeyFilePath.toAbsolutePath());
                JOptionPane.showMessageDialog(owner,
                        "The previously configured directory or API key file is missing or invalid: " + tempOllamaKeyFilePath.toAbsolutePath() +
                        "\nSelect a new directory for your API key.", "Configuration Invalid", JOptionPane.INFORMATION_MESSAGE);
                needToPromptForNewDir = true;
            }
            if (Files.exists(tempTargetDirPath) && Files.isDirectory(tempTargetDirPath) && Files.exists(tempACSYSMSGFilePath)) {
                this.targetDirPath = tempTargetDirPath;
                this.SYSMSGFilePath = tempACSYSMSGFilePath;
                //Application.getInstance().getGUILog().showMessage("Loaded valid Sys msg from config: " + this.SYSMSGFilePath.toAbsolutePath());
            } else {
                //Application.getInstance().getGUILog().showMessage("Configured API key path or file is invalid/missing: " + tempOllamaKeyFilePath.toAbsolutePath());
                JOptionPane.showMessageDialog(owner,
                        "The previously configured directory or System message file is missing or invalid: " + tempACSYSMSGFilePath.toAbsolutePath() +
                        "\nUpdate the System Message.", "Configuration Invalid", JOptionPane.INFORMATION_MESSAGE);
                needToPromptForNewDir = true;
            }
            this.SYSMSGFilePath = tempACSYSMSGFilePath;
        } else {
            //Application.getInstance().getGUILog().showMessage("No API key path found in config file.");
            JOptionPane.showMessageDialog(owner,
                    "No Ollama API key storage directory found. Please select a directory.", "Select Directory", JOptionPane.INFORMATION_MESSAGE);
            needToPromptForNewDir = true;
        }

        if (needToPromptForNewDir) {
            Path selectedPath = promptForDirectorySelection();
            if (selectedPath != null) {
                this.targetDirPath = selectedPath;
                this.OllamaKeyFilePath = this.targetDirPath.resolve(APIKEYFILENAME);
                writeFilePathToConfig(this.targetDirPath.toAbsolutePath().toString());

                try {
                    Files.createDirectories(this.targetDirPath);
                    //Application.getInstance().getGUILog().showMessage("New API key storage directory created: " + this.targetDirPath.toAbsolutePath());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(owner,
                            "Error creating selected directory: " + e.getMessage() +
                            "\nAPI key functionality may not work without a valid directory.", "Directory Creation Error", JOptionPane.ERROR_MESSAGE);
                    //Application.getInstance().getGUILog().showMessage("Error creating selected directory: " + e.getMessage());
                    this.targetDirPath = null;
                    this.OllamaKeyFilePath = null;
                }
            } else {
                JOptionPane.showMessageDialog(owner,
                        "No directory selected. API key functionality will be disabled.", "Selection Cancelled", JOptionPane.WARNING_MESSAGE);
                //Application.getInstance().getGUILog().showMessage("Directory selection cancelled and no valid directory was set.");
                this.targetDirPath = null;
                this.OllamaKeyFilePath = null;
            }
        }
        loadKeyFromFileInternal();
        loadSysMsgFromFileInternal();
    }

/**
 * @return The `promptForDirectorySelection` method returns a `Path` object representing the directory selected by the user for storing an Ollama API key. If no directory is selected, it returns `null`.
 * @summary This method prompts the user to select a directory for storing Ollama API keys and returns the chosen directory path.
 */
    public Path promptForDirectorySelection() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select Directory for Ollama API Key Storage");
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

/**
 * @return The method `getConfigFileLocation` returns a `Path` object representing the location of the configuration file in the user's home directory. This path is constructed using the system property for the user's home directory and the predefined constant `CONFIGFILENAME`.
 * @summary The `getConfigFileLocation` method returns the file path of the configuration file located in the user's home directory.
 */
    private Path getConfigFileLocation() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, CONFIGFILENAME);
    }

/**
 * @param path A string representing the file path to be written into the configuration file.
 * @summary The `writeFilePathToConfig` method is responsible for saving a given file path to the configuration file.
 */
    public void writeFilePathToConfig(String path) {
        Path configFile = getConfigFileLocation();
        try {
            Files.writeString(configFile, path);
            //Application.getInstance().getGUILog().showMessage("Directory path saved to config: " + configFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner,
                    "Error writing config file: " + e.getMessage(), "Config Write Error", JOptionPane.ERROR_MESSAGE);
            //Application.getInstance().getGUILog().showMessage("Error writing config file: " + e.getMessage());
        }
    }

/**
 * @return The method `readFilePathFromConfig` reads and returns the file path specified in a configuration file as a String. If the configuration file exists, it attempts to read its content; otherwise, it returns null.
 * @summary The `readFilePathFromConfig` method reads and returns the file path from a configuration file if it exists.
 */
    public String readFilePathFromConfig() {
        Path configFile = getConfigFileLocation();
        if (Files.exists(configFile)) {
            try {
                return Files.readString(configFile).trim();
            } catch (IOException e) {
                //Application.getInstance().getGUILog().showMessage("Error reading config file: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

/**
 * @summary The method `loadKeyFromFileInternal` reads and loads the Ollama API key from a specified file path, handling any potential IOExceptions by displaying an error message.
 */
    public void loadKeyFromFileInternal() {
        Ollamakey = "";
        if (OllamaKeyFilePath == null) {
            //Application.getInstance().getGUILog().showMessage("Storage file path is null, cannot load API key.");
            return;
        }
        if (Files.exists(OllamaKeyFilePath)) {
            try {
                Ollamakey = Files.readString(OllamaKeyFilePath, StandardCharsets.UTF_8).trim();
                //Application.getInstance().getGUILog().showMessage("Ollama API Key loaded from " + OllamaKeyFilePath.toAbsolutePath());
            } catch (IOException e) {
                //Application.getInstance().getGUILog().showMessage("Error reading password from file: " + e.getMessage());
                JOptionPane.showMessageDialog(owner,
                        "Error reading API key file: " + e.getMessage() + "\nThe file might be corrupted or inaccessible. Please re-enter the key.",
                        "File Read Error", JOptionPane.ERROR_MESSAGE);
                Ollamakey = "";
            }
        } else {
            Application.getInstance().getGUILog().showMessage(
                    "Ollama API Key file does not exist at " + OllamaKeyFilePath.toAbsolutePath() +
                    ". It will be created when you save a key"
            );
        }
    }

/**
 * @summary The `loadSysMsgFromFileInternal` method reads the system message from a specified file path, handling any potential errors and updating the GUI accordingly.
 */
    public void loadSysMsgFromFileInternal() {
        SysMSGsys = "";
        if (SYSMSGFilePath == null) {
            //Application.getInstance().getGUILog().showMessage("No System Message File. Please Update.");
            return;
        }
        if (Files.exists(SYSMSGFilePath)) {
            try {
                SysMSGsys = Files.readString(SYSMSGFilePath, StandardCharsets.UTF_8).trim();
                //Application.getInstance().getGUILog().showMessage("System message loaded from " + SYSMSGFilePath.toAbsolutePath());
            } catch (IOException e) {
                //Application.getInstance().getGUILog().showMessage("Error reading system message from file: " + e.getMessage());
                JOptionPane.showMessageDialog(owner,
                        "Error reading System message file: " + e.getMessage() + "\nThe file might be corrupted or inaccessible. Please re-enter the system message.",
                        "File Read Error", JOptionPane.ERROR_MESSAGE);
                SysMSGsys = "";
            }
        } else {
            Application.getInstance().getGUILog().showMessage(
                    "System message file does not exist at " + SYSMSGFilePath.toAbsolutePath() +
                    ". It will be created when you save a system message."
            );
        }
    }

/**
 * @param apiKeyField The `apiKeyField` parameter represents the input field where users can enter their API key, which is then trimmed of extraneous whitespace and saved if editable.
 * @summary The `updateAndSavePassword` method retrieves the trimmed text from an editable `JFormattedTextField`, stores it as `Ollamakey`, and then saves this value to a file.
 */
    public void updateAndSavePassword(javax.swing.JFormattedTextField apiKeyField) {
        if (apiKeyField.isEditable()) {
            Ollamakey = apiKeyField.getText().trim();
            savePasswordToFile(Ollamakey);
        }
    }

/**
 * @param ollamakey The `ollamakey` parameter is a string that represents the API key to be saved into the specified file.
 * @summary The `savePasswordToFile` method is responsible for saving a given Ollama API key as a string to a specified file, ensuring that the target directory exists and handling any potential IO exceptions with appropriate error messages.
 */
    public void savePasswordToFile(String ollamakey) {
        if (targetDirPath == null || OllamaKeyFilePath == null) {
            //Application.getInstance().getGUILog().showMessage("Cannot save API Key: Storage paths are not set.");
            JOptionPane.showMessageDialog(owner,
                    "Cannot save API Key: Storage directory is not configured.", "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Files.createDirectories(targetDirPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner,
                    "Error ensuring directory exists for saving: " + e.getMessage() + "\nPlease check permissions or select a different directory.",
                    "Directory Access Error", JOptionPane.ERROR_MESSAGE);
            //Application.getInstance().getGUILog().showMessage("Error ensuring directory exists for saving: " + e.getMessage());
            return;
        }
        try {
            Files.writeString(OllamaKeyFilePath, ollamakey, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            //Application.getInstance().getGUILog().showMessage("Ollama API Key saved successfully to " + OllamaKeyFilePath.toAbsolutePath());
        } catch (IOException e) {
            //Application.getInstance().getGUILog().showMessage("Error writing password to file: " + e.getMessage());
            JOptionPane.showMessageDialog(owner,
                    "Error writing API key to file: " + e.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
