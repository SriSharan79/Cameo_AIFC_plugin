package cameoUIPanels.PromptGenerationPannels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIDialogs.PGDialogs.OSIRISFrame;
import cameoUIDialogs.PGDialogs.SystemMessagePreviewDialog;
import dataloggers.BackgroundRunner;
import dataloggers.InternalDataRetreiver;
import requestCreation.RequestSender;
import responseCollection.ResponseCollector;
import restAPI.DLROllamaAPI;



/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `Prompt_preview_pannel` class is primarily responsible for managing and previewing generated prompts, handling user interactions, and updating UI components based on changes in the prompt properties.
 */
public class Prompt_preview_pannel extends JPanel {

    private static final long serialVersionUID = 1L;

    // UI Components
    private JFormattedTextField API_key;
    private JCheckBox chckbx_API_key;
    private JToggleButton View_btn_API;
    private JPanel Key_panel;
    private JPanel panel;

    private final String SYSMSGFILENAME = "Sys_MSG_Sys_Functions.txt";
    // Key Management
    private InternalDataRetreiver apiKeyManager;
    DLROllamaAPI DLRO;

	BackgroundRunner BR=new BackgroundRunner();
    // System Message
    private String SysMSGsys = "";

    private String ModelName = "";

    // Parent Frame
    private final Frame parent = new OSIRISFrame();

/**
 * @param generated_prompt The `generated_prompt` parameter is the initial text input that will be displayed and editable in the Prompt Preview Pane.
 * @param promptpropertyMap The `promptpropertyMap` parameter in the `Prompt_preview_pannel` method is a map that holds key-value pairs where keys are strings and values are lists of strings, used to store properties related to the prompt generation process.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a map that will be updated with the latest values and properties generated or modified during the response processing.
 * @param selectedfunction The `selectedfunction` parameter of type `Element` in the `Prompt_preview_pannel` method represents the specific SysML model element corresponding to the function for which the failure conditions are being generated or refined.
 * @return The `Prompt_preview_pannel` method returns a `Prompt_preview_pannel` object. This object is essentially the graphical user interface (GUI) that allows users to preview and send requests for generating failure conditions using the LLM-based system in OSIRIS. The GUI includes elements such as input fields, buttons, and panels necessary to manage API keys, configure context addition options, display request previews, and handle response generation.
 * @summary The `Prompt_preview_pannel` method is responsible for creating and managing a graphical user interface panel that allows users to preview and send prompts, manage API keys, and handle responses from an external service.
 */
    public Prompt_preview_pannel(String generated_prompt,
                                Map<String, List<String>> promptpropertyMap,
                                Map<String, List<String>> propertyMapUpdated,
                                Element selectedfunction) {
    	DLRO = new DLROllamaAPI(); // Assuming DLROllamaAPI has a default constructor

        setPreferredSize(new Dimension(600, 450));
        apiKeyManager = new InternalDataRetreiver(this);

        ButtonGroup BTNG = new ButtonGroup();
        panel = new JPanel();
        panel.setBounds(10, 5, 582, 433);

        JButton btn_SendPrompt = new JButton("Send Request");
        btn_SendPrompt.setBounds(463, 391, 116, 23);

        JPanel RAG_panel = new JPanel();
        RAG_panel.setBounds(0, 0, 579, 33);
        RAG_panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JLabel lbl_mainlabel = new JLabel("Request Preview Window");
        lbl_mainlabel.setBounds(2, 2, 223, 25);
        lbl_mainlabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl_mainlabel.setAlignmentX(0.5f);

        Key_panel = new JPanel();
        Key_panel.setBounds(0, 402, 457, 23);
        Key_panel.setLayout(null);
        Key_panel.setVisible(false);

        API_key = new JFormattedTextField();
        API_key.setBounds(68, 1, 288, 20);
        API_key.setVisible(false);
        Key_panel.add(API_key);
        API_key.setBackground(new Color(255, 255, 255));
        API_key.setEditable(false);

        View_btn_API = new JToggleButton("View");
        View_btn_API.setBounds(0, 0, 64, 23);
        Key_panel.add(View_btn_API);
        View_btn_API.addActionListener(new ActionListener() {
/**
 * @param e The `e` parameter represents the event instance that triggers this method, encapsulating information about the action performed.
 * @summary The `actionPerformed` method toggles the visibility and editability of an API key field based on the selection state of a button.
 */
            public void actionPerformed(ActionEvent e) {
                if (View_btn_API.isSelected()) {
                    API_key.setText(apiKeyManager.Ollamakey);
                    API_key.setVisible(true);
                    API_key.setEditable(true);
                    View_btn_API.setText("Hide");
                } else {
                    API_key.setEditable(false);
                    API_key.setVisible(false);
                    View_btn_API.setText("Show");
                }
            }
        });

        API_key.getDocument().addDocumentListener(new DocumentListener() {
/**
 * @param e The `e` parameter is a `DocumentEvent` object representing the event that occurred when text was inserted into a document.
 * @summary The `insertUpdate` method is responsible for updating and saving an API key when content is inserted in the document.
 */
            @Override public void insertUpdate(DocumentEvent e) { apiKeyManager.updateAndSavePassword(API_key);}
/**
 * @param e The `DocumentEvent` parameter 'e' represents the event that contains information about changes made to the document.
 * @summary This method responds to document updates by updating and saving an API key using the `APIKeyManager`.
 */
            @Override public void removeUpdate(DocumentEvent e) { apiKeyManager.updateAndSavePassword(API_key);}
/**
 * @param e The `DocumentEvent e` parameter represents the event that indicates a change in the document's content.
 * @summary The `changedUpdate` method is responsible for handling changes in text content triggered by events such as typing, cutting, pasting, or other document modifications in the Java Swing framework.
 */
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        API_key.addFocusListener(new java.awt.event.FocusAdapter() {
/**
 * @param e The parameter `e` of type `java.awt.event.FocusEvent` in the method `focusLost` represents the event that indicates a focus loss, which is used to determine when the API key field loses focus.
 * @summary The `focusLost` method is responsible for updating and saving the password when the API key input field loses focus, but only if it is currently editable.
 */
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (API_key.isEditable()) {
                    apiKeyManager.updateAndSavePassword(API_key);
                }
            }
        });

        setLayout(null);
        chckbx_API_key = new JCheckBox("Use Ollama with API key");
        chckbx_API_key.setBounds(0, 379, 154, 23);

        panel.setLayout(null);
        panel.add(RAG_panel);
        RAG_panel.setLayout(null);

        JPanel Context_panel = new JPanel();
        Context_panel.setBounds(257, 2, 314, 25);
        RAG_panel.add(Context_panel);
        Context_panel.setLayout(null);

        JLabel lbl_context = new JLabel("Context Addition:");
        lbl_context.setBounds(0, 8, 124, 14);
        Context_panel.add(lbl_context);
        lbl_context.setFont(new Font("Times New Roman", Font.BOLD, 16));
        JRadioButton WithRAG = new JRadioButton("Variable ");
        WithRAG.setBounds(121, 2, 80, 25);
        Context_panel.add(WithRAG);
        WithRAG.setBorder(new LineBorder(new Color(0, 0, 0)));
        WithRAG.setFont(new Font("Tahoma", Font.PLAIN, 14));
        BTNG.add(WithRAG);

        JRadioButton WORAG = new JRadioButton("Constant");
        WORAG.setFont(new Font("Tahoma", Font.PLAIN, 14));
        WORAG.setBorder(new LineBorder(Color.BLACK));
        WORAG.setBounds(211, 2, 100, 25);
        Context_panel.add(WORAG);
        BTNG.add(WORAG);

        RAG_panel.add(lbl_mainlabel);
        panel.add(chckbx_API_key);
        panel.add(btn_SendPrompt);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 39, 581, 340);
        panel.add(scrollPane);

        JTextArea textArea_prompt = new JTextArea();
        scrollPane.setViewportView(textArea_prompt);
        textArea_prompt.setText(generated_prompt);
        textArea_prompt.setWrapStyleWord(true);
        textArea_prompt.setLineWrap(true);

        panel.add(Key_panel);

        JButton btnsysmsg = new JButton("SYS MSG");
        btnsysmsg.setBounds(358, 0, 89, 23);
        Key_panel.add(btnsysmsg);

        btnsysmsg.addActionListener(new ActionListener() {
/**
 * @param e The `ActionEvent` parameter `e` in the `actionPerformed` method represents the event that triggered this method call, typically a user action like clicking a button.
 * @summary The `actionPerformed` method initializes a system message, creates a preview dialog for it, and displays the dialog.
 */
            public void actionPerformed(ActionEvent e) {
                apiKeyManager.initializeAndLoadKey( SYSMSGFILENAME);
                SysMSGsys = apiKeyManager.SysMSGsys;
                var dialog = new SystemMessagePreviewDialog(parent,
                        SysMSGsys,
                        apiKeyManager.SYSMSGFilePath,
                        apiKeyManager.targetDirPath);
                dialog.setTitle("System Message to LLM");
                dialog.setVisible(true);
            }
        });

        add(panel);

        chckbx_API_key.addItemListener(new ItemListener() {
/**
 * @param e The `ItemEvent` parameter `e` in the method `itemStateChanged` signifies a change event related to an item, used to determine if it was selected or deselected.
 * @summary The `itemStateChanged` method toggles the visibility and state of UI components based on whether an item is selected or deselected.
 */
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Key_panel.setVisible(true);
                    chckbx_API_key.setEnabled(false);
                    new KeySetupWorker().execute();
                    
                } else {
                    Key_panel.setVisible(false);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        btn_SendPrompt.addActionListener(new ActionListener() {
/**
 * @param e The `e` parameter of type `ActionEvent` in the method `actionPerformed` represents the event that triggered this action.
 * @summary The `actionPerformed` method processes user input and generates a response using an external API, handling both synchronous execution and time tracking.
 */
            public void actionPerformed(ActionEvent e) {
                String edited_prompt = textArea_prompt.getText();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
/**
 * @return The `doInBackground` method processes a request to generate and parse a response using the OSIRIS system. It starts by configuring API settings, sends a request based on user selections, handles potential errors, measures the execution time, and then parses the generated response. The method returns `null`, indicating that no specific value needs to be returned after completing its operations.
 * @summary This method orchestrates the generation of a response based on user selection and processes the result to be collected by the `ResponseCollector`.
 */
                    @Override
                    protected Void doInBackground() throws Exception {


                        boolean using_API_key = chckbx_API_key.isSelected();
                        long startTime = System.currentTimeMillis();
                        String response = "";
                        String ollama_key = apiKeyManager.Ollamakey;
                        String sys_msg = apiKeyManager.SysMSGsys;
                        RequestSender Rs = new RequestSender(using_API_key, ollama_key, sys_msg,ModelName);
                        if (WithRAG.isSelected()) {
                            response = Rs.generateResponse_With_RAG(edited_prompt, promptpropertyMap);
                        } else if (WORAG.isSelected()) {

                            response = Rs.generateResponse_Without_RAG(edited_prompt, promptpropertyMap);
                        } else {

                            response = Rs.generateResponse_Without_RAG(edited_prompt, promptpropertyMap);
                        }
                        if (response.contains("Failed with status code ")
                                || response.contains("Failed to connect to the server. Please check if the server is running.")) {
                            return null;
                        }
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
                        String timeTaken = String.format("Time taken: %02d hours %02d minutes %02d seconds", hours, minutes, seconds);
                        ResponseCollector.parseResponse(response, timeTaken, promptpropertyMap, propertyMapUpdated, selectedfunction);
                        return null;
                    }
                };
                worker.execute();
            }
        });
    }


    
    
    
    private class KeySetupWorker extends SwingWorker<Void, Void> {
        private boolean setupSuccessful = false;

/**
 * @return The `doInBackground` method initializes and loads the API key for a given system message file. If successful, it updates the setup status based on the existence of target directory path and Ollama Key File Path. The method returns `Void`, indicating that no meaningful result is produced or needed from this background operation.
 * @summary The `doInBackground` method initializes and loads the API key from a specified file, then sets up a success flag based on whether the target directory path and the Ollama key file path are both non-null.
 */
/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `KeySetupWorker` is primarily responsible for executing background processing tasks related to key setup within the OSIRIS plugin, with `doInBackground()` performing the core operations and `done()` handling post-processing activities.
 */
        @Override
        protected Void doInBackground() {
            apiKeyManager.initializeAndLoadKey(SYSMSGFILENAME);
/**
 * @return The `doInBackground` method in the provided code snippet is used to perform background initialization tasks for a key manager. Specifically, it initializes and loads an API key from a specified file and sets up the success status based on whether the target directory path and the Ollama key file path are successfully loaded. The method returns `Void`, indicating that it does not produce any meaningful return value as its primary purpose is to execute these background operations.
 * @summary The `doInBackground` method initializes the API key using the provided file name and sets up a flag indicating whether the initialization was successful.
 */
            setupSuccessful = (apiKeyManager.targetDirPath != null && apiKeyManager.OllamaKeyFilePath != null);
            return null;
        }

/**
 * @summary The `done` method in the OSIRIS Java plugin is responsible for handling the completion of an asynchronous task related to API key setup, managing UI updates based on whether the setup was successful or encountered an error.
 */
        @Override
        protected void done() {
            try {
                get();
            } catch (InterruptedException | ExecutionException ex) {
                BR.Notifyinbackground("ID3","Error during key setup: " + ex.getMessage());
                JOptionPane.showMessageDialog(Prompt_preview_pannel.this,
                        "An error occurred during API key setup: " + ex.getMessage(),
                        "Setup Error", JOptionPane.ERROR_MESSAGE);
                setupSuccessful = false;
            } finally {
                chckbx_API_key.setEnabled(true);
                if (setupSuccessful) {
                    if (apiKeyManager.Ollamakey == null || apiKeyManager.Ollamakey.isEmpty()) {
                        API_key.setEditable(true);
                        API_key.setVisible(true);
                        API_key.setText("");
                        View_btn_API.setSelected(true);
                        View_btn_API.setText("Hide");
                        API_key.requestFocusInWindow();
                    } else {
                        API_key.setEditable(false);
                        API_key.setVisible(false);
                        View_btn_API.setSelected(false);
                        View_btn_API.setText("View");
                        ModelName= DLRO.getModelName(apiKeyManager.Ollamakey);
                    }
                } else {
                    chckbx_API_key.setSelected(false);
                    Key_panel.setVisible(false);
                }
                panel.revalidate();
                panel.repaint();
            }
        }
    }
}
