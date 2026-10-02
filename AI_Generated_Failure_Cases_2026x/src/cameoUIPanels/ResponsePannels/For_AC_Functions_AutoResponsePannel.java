package cameoUIPanels.ResponsePannels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import autoDiagramCreation.CreateFHATable;
import cameoUIDialogs.PGDialogs.OSIRISFrame;
import cameoUIDialogs.RGDialogs.For_AC_Functions_ManualFCGResponseDialog;
import cameoUISubPanels.For_AC_Functions_with_All_effects_ResponseSubPannel;
import creationOfSafetyVP.CreateFailureCondition;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-19
 * @summary The `For_AC_Functions_AutoResponsePannel` class is responsible for managing and updating a user interface panel that displays and processes failure conditions for aircraft functions based on selected SysML elements.
 */
public class For_AC_Functions_AutoResponsePannel extends JPanel {

    private static final long serialVersionUID = 1L;

    // Map to store checkboxes and panels with fcId as the key
    public Map<String, Map<JCheckBox, JPanel>> checkboxNamePanelMap = new HashMap<>();
    public Map<String, Map<JCheckBox, JPanel>> checkboxDesPanelMap = new HashMap<>();
    public Map<String, Map<JCheckBox, JPanel>> checkboxEffonACPanelMap = new HashMap<>();
    public Map<String, Map<JCheckBox, JPanel>> checkboxEffonCWPanelMap = new HashMap<>();
    public Map<String, Map<JCheckBox, JPanel>> checkboxEffonOccPanelMap = new HashMap<>();
    public Map<String, Map<JCheckBox, JPanel>> checkboxElementPanelMap = new HashMap<>();
    public Map<String, Map<JCheckBox, JPanel>> checkboxIDPanelMap = new HashMap<>();

    List<String> systemList = new ArrayList<>();
    int panelCounter = 1;
    
	// References to the UI components that hold the dynamic content
	private JScrollPane lScrollPane;
	private JPanel lMainPanel;
	private JLabel lblResponse; // Keep a reference if its text might change

	// Store current state (or a subset) if needed, to compare or rebuild
	private Element currentSelectedNode;
	private Map<String, List<String>> currentPropertyMapUpdated;
	private String currentRawResponse;
    // NEW: Field to hold the reference to the parent dialog
    private JDialog parentDialog; 

	OSIRISFrame RAWparent;

    private JDialog RAWDialog=null;

/**
 * @param parentDialog The `parentDialog` parameter of type `JDialog` in the method `For_AC_Functions_AutoResponsePannel` represents the main dialog window from which this panel is launched and to which it may need to reference or return.
 * @param formattedResponse Formatted response data containing the generated failure conditions and their associated information.
 * @param selectedNode The `selectedNode` parameter is the specific Element from the SysML model that has been selected for failure condition generation.
 * @param propertyMapUpdated The parameter `propertyMapUpdated` of type `List<String>` is a list of updated property identifiers that have been modified or added during the processing of failure conditions.
 * @param response `response`: A string containing the raw output from the language model's response.
 * @param timeTaken The `timeTaken` parameter represents the duration in which the failure conditions were generated, provided as a string.
 * @return The `For_AC_Functions_AutoResponsePannel` method returns an instance of the `For_AC_Functions_AutoResponsePannel` class. This return value represents a custom dialog panel that is used to display and manage failure conditions generated from SysML models and safety context for aircraft systems. The panel includes interactive elements such as buttons for creating failure conditions and viewing raw data, along with scrollable content for displaying detailed information.
 * @summary The `For_AC_Functions_AutoResponsePannel` method initializes a dialog panel for displaying and processing failure conditions generated from aircraft system models.
 */
    public For_AC_Functions_AutoResponsePannel(JDialog parentDialog, Map<String, Map<String, List<String>>> formattedResponse, Element selectedNode, Map<String, List<String>> propertyMapUpdated, String response, String timeTaken) {
        setPreferredSize(new Dimension(800, 800));
        
        // NEW: Store the parent dialog reference
        this.parentDialog = parentDialog;
        // Store initial data
        this.currentSelectedNode = selectedNode;
        this.currentPropertyMapUpdated = propertyMapUpdated;
        this.currentRawResponse = response;

      
        lblResponse = new JLabel("Failure Conditions Generated");
        lblResponse.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Create scroll panes and main panels
        lScrollPane = new JScrollPane();
        lMainPanel = createPanelWithScroll(lScrollPane);
     // Call the new helper method to populate and lay out the dynamic content
        populateAndLayoutPanels(formattedResponse, selectedNode, propertyMapUpdated, response);

        // Create the "Create Failure Conditions" button
        JButton btnCreate = new JButton("Create Failure Conditions");
        btnCreate.addActionListener(e -> processFailureConditions(selectedNode));
        
        JButton btnRaw_info = new JButton("Raw Data");
        btnRaw_info.addActionListener(new ActionListener() {
/**
 * @param e The `ActionEvent` parameter `e` represents the event that triggered this method execution.
 * @summary The `actionPerformed` method displays or brings to front a dialog for entering responses from the LLM, initializing it if necessary.
 */
        	public void actionPerformed(ActionEvent e) {
        	    if (RAWDialog == null) {
        	        RAWDialog = new For_AC_Functions_ManualFCGResponseDialog(RAWparent, currentRawResponse, currentPropertyMapUpdated, currentSelectedNode,timeTaken);
        	        RAWDialog.setTitle(" Raw response from LLM ");
        	        RAWDialog.setVisible(true);

        	        // Add a WindowListener to reset the field when the dialog is closed
        	        RAWDialog.addWindowListener(new java.awt.event.WindowAdapter() {
/**
 * @param windowEvent The parameter "windowEvent" represents the event object that identifies a window closing operation, containing details about the source and type of the window event.
 * @summary The `windowClosing` method is responsible for setting the `RAWDialog` to `null` when the window is closing, likely to free up resources or clean up references.
 */
        	            @Override
        	            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        	                RAWDialog = null;
        	            }
        	        });
        	    } else {
        	        // If the dialog exists but is hidden, make it visible again
        	        RAWDialog.setVisible(true);
        	        RAWDialog.setAlwaysOnTop(true);
        	    }
        	}
        });

        // Layout setup with GroupLayout
//        setupLayout(lblResponse, lScrollPane, btnCreate);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(12)
        			.addComponent(lblResponse, GroupLayout.PREFERRED_SIZE, 1571, GroupLayout.PREFERRED_SIZE))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(228)
        			.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)
        			.addGap(57)
        			.addComponent(btnRaw_info, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lScrollPane, GroupLayout.PREFERRED_SIZE, 780, GroupLayout.PREFERRED_SIZE))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(13)
        			.addComponent(lblResponse)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lScrollPane, GroupLayout.PREFERRED_SIZE, 710, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnCreate)
        				.addComponent(btnRaw_info))
        			.addContainerGap())
        );
        setLayout(groupLayout);
        
    }
    
 
/**
 * @param newFormattedResponse `newFormattedResponse` is a nested map structure containing formatted data for updating the UI panel content based on the current selection and property changes.
 * @param newSelectedNode The `newSelectedNode` parameter represents the currently selected SysML element in the Cameo Systems Modeler interface that triggers the update of the panel content based on the provided formatted response, property map, and raw response.
 * @param newPropertyMapUpdated The parameter `newPropertyMapUpdated` of type `List<String>>` represents the updated properties associated with the selected SysML element, which will be used to refresh the content displayed in the panel.
 * @param newResponse The `newResponse` parameter in the `updatePanelContent` method is a raw string representation of the latest response data used for updating the internal state of the panel content.
 * @summary The `updatePanelContent` method is responsible for updating the panel's internal state, clearing existing content, populating it with new data, and ensuring the UI updates are rendered.
 */
    public void updatePanelContent(Map<String, Map<String, List<String>>> newFormattedResponse,
                                   Element newSelectedNode,
                                   Map<String, List<String>> newPropertyMapUpdated,
                                   String newResponse) {

        // 1. Update the internal state of the panel
        this.currentSelectedNode = newSelectedNode;
        this.currentPropertyMapUpdated = newPropertyMapUpdated;
        this.currentRawResponse = newResponse;

        // 2. Clear existing content and re-populate the panel with new data
        // This is the core logic that replaces the old UI elements with new ones
        populateAndLayoutPanels(newFormattedResponse, newSelectedNode, newPropertyMapUpdated, newResponse);

        // 3. Revalidate and repaint to ensure UI updates are rendered
        revalidate();
        repaint();
    }
    
 
/**
 * @param formattedResponse The `formattedResponse` parameter in the method `populateAndLayoutPanels` is a nested map structure that contains formatted data for populating and laying out panels based on parsed failure conditions from SysML models.
 * @param selectedNode The `selectedNode` parameter is the currently selected SysML element within the model that triggers the population and layout of the panels.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter in the method `populateAndLayoutPanels` is a map that holds updated property names and their corresponding values, used to dynamically populate and layout panels with new data.
 * @param response The `response` parameter in the `populateAndLayoutPanels` method is a string that contains a user-friendly message indicating whether all failure conditions were parsed and displayed.
 * @summary The `populateAndLayoutPanels` method is responsible for refreshing and displaying the main panel content based on the provided formatted response, clearing any existing data, reinitializing panels with new data, and logging discrepancies between parsed and displayed failure conditions.
 */
    private void populateAndLayoutPanels(Map<String, Map<String, List<String>>> formattedResponse, Element selectedNode, Map<String, List<String>> propertyMapUpdated, String response) {
        // Clear existing content from the main panel
        if (lMainPanel != null) {
            lMainPanel.removeAll();
            // Clear all the maps that store references to old checkboxes and panels
            checkboxNamePanelMap.clear();
            checkboxDesPanelMap.clear();
            checkboxEffonACPanelMap.clear();
            checkboxEffonCWPanelMap.clear();
            checkboxEffonOccPanelMap.clear();
            checkboxElementPanelMap.clear();
            checkboxIDPanelMap.clear();
            panelCounter = 1; // Reset panel counter for fresh population
        }

        // Populate system list if present
        if (propertyMapUpdated.containsKey("System") && propertyMapUpdated.get("System") != null) {
            systemList = propertyMapUpdated.get("System");
        } else {
            systemList.clear(); // Clear if no system list is provided
        }

        // Re-initialize panels with the new data
        int panelCounter1 = initializePanels(lMainPanel, formattedResponse, selectedNode, panelCounter);

        int totalSize = formattedResponse.size();
        if(!(totalSize==(panelCounter1-1))) {
            String message ="The total Failure conditions Parsed: "+ totalSize
                           +"\n The Faliure Conditions Diaplayed: "+ (panelCounter1 - 1); // Corrected count
            Application.getInstance().getGUILog().showMessage(message);
        } else {
             // Optionally clear previous messages or show a success message
//             Application.getInstance().getGUILog().showMessage("All failure conditions displayed successfully.");
        }
    }

/**
 * @param scrollPane A scrollable container (`JScrollPane`) to be used for managing the scroll behavior of a newly created JPanel.
 * @return The `createPanelWithScroll` method returns a `JPanel` that has been configured with a white background and a vertical box layout. This panel is also set as the viewport view of a provided `JScrollPane`.
 * @summary This method creates and configures a JPanel with a white background, vertical box layout, and attaches it to the provided JScrollPane.
 */
    private JPanel createPanelWithScroll(JScrollPane scrollPane) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        scrollPane.setViewportView(panel);
        return panel;
    }
    

//
/**
 * @param mainPanel The `mainPanel` parameter is the primary container `JPanel` where all dynamically generated sub-panels will be added for display.
 * @param responseMap `responseMap`: A nested map structure that holds the failure condition IDs as keys, with each key mapping to another map containing details about the failure conditions.
 * @param selectedNode The `selectedNode` parameter represents the SysML element currently selected for which failure conditions are being processed and visualized within the GUI.
 * @param panelCounter The `panelCounter` parameter in the `initializePanels` method is an integer that keeps track of the number of panels initialized and added to the main panel during the execution of the method.
 * @return The `initializePanels` method returns an integer value representing the updated count of panels that have been initialized and added to the main GUI panel. This integer is used to track how many panels were successfully created during the execution of the method, starting from a given initial count (`panelCounter`) and incrementing it as new panels are added.
 * @summary The `initializePanels` method is responsible for initializing and adding response panels to a main JPanel based on the provided data from a response map, ensuring all inputs are validated and handling any exceptions that may arise during this process.
 */
    private int initializePanels(JPanel mainPanel, Map<String, Map<String, List<String>>> responseMap, Element selectedNode, int panelCounter) {
        // Initialize debug log
        StringBuilder debugLog = new StringBuilder();
        debugLog.append("Entering initializePanels method...\n");

        // Step 1: Validate mainPanel
        if (mainPanel == null) {
            Application.getInstance().getGUILog().showMessage("Error: mainPanel is null in initializePanels.\n");
            return panelCounter;
        }
        debugLog.append("mainPanel validated successfully.\n");
//        Application.getInstance().getGUILog().showMessage(debugLog.toString());

        // Step 2: Validate responseMap
        if (responseMap == null) {
            Application.getInstance().getGUILog().showMessage("Error: responseMap is null in initializePanels.\n");
            return panelCounter;
        }
        if (responseMap.isEmpty()) {
            Application.getInstance().getGUILog().showMessage("Warning: responseMap is empty in initializePanels.\n");
            return panelCounter;
        }
        debugLog.append("responseMap validated successfully. Size: ").append(responseMap.size()).append("\n");
//        Application.getInstance().getGUILog().showMessage(debugLog.toString());

        // Step 3: Validate selectedNode
        if (selectedNode == null) {
            Application.getInstance().getGUILog().showMessage("Error: selectedNode is null in initializePanels.\n");
            return panelCounter;
        }
        debugLog.append("selectedNode validated successfully.\n");
//        Application.getInstance().getGUILog().showMessage(debugLog.toString());

        // Step 4: Iterate over responseMap keys
        for (String fcId : responseMap.keySet()) {
            debugLog.append("Processing FC ID: ").append(fcId).append("\n");

            // Step 4.1: Fetch fcDetails
            Map<String, List<String>> fcDetails = responseMap.get(fcId);
            if (fcDetails == null) {
                debugLog.append("Warning: fcDetails is null for FC ID: ").append(fcId).append("\n");
                continue; // Skip to the next FC ID
            }
            debugLog.append("fcDetails fetched successfully for FC ID: ").append(fcId).append(".\n");
//            Application.getInstance().getGUILog().showMessage(debugLog.toString());

            try {
                // Step 4.2: Create ResponseSubPannel4
                debugLog.append("Creating ResponseSubPannel4 for FC ID: ").append(fcId).append("\n");
                var panel = new For_AC_Functions_with_All_effects_ResponseSubPannel(panelCounter++, selectedNode, fcId, fcDetails);

                // Step 4.3: Add panel to mainPanel
                debugLog.append("Adding panel to mainPanel for FC ID: ").append(fcId).append("\n");
                mainPanel.add(panel);

                // Step 4.4: Populate maps with panel's data
                debugLog.append("Populating maps with data from ResponseSubPannel4 for FC ID: ").append(fcId).append("\n");
                checkboxNamePanelMap.put(fcId, panel.getcheckboxNamePanelMap());
                checkboxDesPanelMap.put(fcId, panel.getcheckboxDesPanelMap());
                checkboxEffonACPanelMap.put(fcId, panel.getcheckboxEffAC());
                checkboxEffonCWPanelMap.put(fcId, panel.getcheckboxEffCW());
                checkboxEffonOccPanelMap.put(fcId, panel.getcheckboxEffOCC());
                checkboxIDPanelMap.put(fcId, panel.getCheckboxIDtPanelMap());
                checkboxElementPanelMap.put(fcId, panel.getCheckboxElementPanelMap());

                debugLog.append("Panel created and added successfully for FC ID: ").append(fcId).append("\n");
//                Application.getInstance().getGUILog().showMessage(debugLog.toString());
            } catch (Exception e) {
                // Step 4.5: Handle exceptions during panel creation or addition
                Application.getInstance().getGUILog().showMessage(
                    "Error while processing FC ID: " + fcId + ". Exception: " + e.getMessage() + "\n"
                );
                e.printStackTrace(); // Log the stack trace for further analysis
            }
        }

        // Step 5: Set alignment for mainPanel
        try {
            debugLog.append("Setting alignment for mainPanel...\n");
            mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            debugLog.append("mainPanel alignment set successfully.\n");
        } catch (Exception e) {
            Application.getInstance().getGUILog().showMessage("Error setting alignment for mainPanel: " + e.getMessage() + "\n");
            e.printStackTrace();
        }

        // Step 6: Log final debug message and return panelCounter
        debugLog.append("Exiting initializePanels method. Total panels created: ").append(panelCounter).append("\n");
//        Application.getInstance().getGUILog().showMessage(debugLog.toString());

        return panelCounter;
    }



/**
 * @param mainPanel The `mainPanel` parameter is a `JPanel` where the response subpanels for each failure condition are dynamically created and added during the initialization process.
 * @param responseMap A nested map that associates failure condition IDs with their respective details and corresponding lists of strings.
 * @param selectedNode The `selectedNode` parameter represents the SysML model element currently selected for which failure conditions are being initialized and displayed in panels.
 * @param panelCounter The `panelCounter` parameter of type `int` in the method `initializePanels` is used to keep track of the number of panels created and added to the main panel, ensuring each new panel gets a unique identifier.
 * @return The `initializePanels` method returns the updated value of the `panelCounter`, which keeps track of how many panels have been initialized and added to the main panel. This return value is useful for maintaining a consistent count across multiple invocations of the method, ensuring that each panel gets a unique identifier or position in the layout.
 * @summary The `initializePanels` method is responsible for creating and adding subpanels to a main panel based on the provided failure condition (FC) details from the response map, while also storing associated checkboxes and panels in respective maps.
 */
//    private int initializePanels(JPanel mainPanel, Map<String, Map<String, List<String>>> responseMap, Element selectedNode, int panelCounter) {
//        
//
//        StringBuilder debugLog = new StringBuilder();
//
//        debugLog.append("  In intilizing method: ").append("\n");
//        
//    	for (String fcId : responseMap.keySet()) {
//            Map<String, List<String>> fcDetails = responseMap.get(fcId);
//            debugLog.append("  Following FCid pannel is in process: ").append(fcId).append("\n");
//
//            // Create and add a ResponseSubPannel4
//            var panel = new ResponseSubPannel5(panelCounter++, selectedNode, fcId, fcDetails);
//            mainPanel.add(panel);
//
//            // Store checkboxes and associated panels
//            checkboxNamePanelMap.put(fcId, panel.getcheckboxNamePanelMap());
//            checkboxDesPanelMap.put(fcId, panel.getcheckboxDesPanelMap());
//            checkboxEffonACPanelMap.put(fcId, panel.getcheckboxEffAC());
//            checkboxEffonCWPanelMap.put(fcId, panel.getcheckboxEffCW());
//            checkboxEffonOccPanelMap.put(fcId, panel.getcheckboxEffOCC());
//            checkboxIDPanelMap.put(fcId, panel.getCheckboxIDtPanelMap());
//            checkboxElementPanelMap.put(fcId, panel.getCheckboxElementPanelMap());
//            
//
//            debugLog.append("  Following FCid pannel is in Created: ").append(fcId).append("\n");
//        }
//
//        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//
//        // Log results
////        Application.getInstance().getGUILog().showMessage(debugLog.toString());
//		return panelCounter;
//    }

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML element for which failure conditions are being processed and created.
 * @summary The `processFailureConditions` method processes each failure condition associated with a selected node by collecting and storing relevant properties for each condition.
 */
    @SuppressWarnings("unchecked")
	private void processFailureConditions(Element selectedNode) {
        StringBuilder debugLog = new StringBuilder();
        Map<String, Map<String, Object>> selectedFailures = new HashMap<>();

        // Process each failure condition
        for (String fcId : checkboxIDPanelMap.keySet()) {
            debugLog.append("Processing fcId: ").append(fcId).append("\n");

            Map<JCheckBox, JPanel> checkboxNameMap = checkboxNamePanelMap.get(fcId);
            Map<JCheckBox, JPanel> checkboxDesMap = checkboxDesPanelMap.get(fcId);
            Map<JCheckBox, JPanel> checkboxEffACMap = checkboxEffonACPanelMap.get(fcId);
            Map<JCheckBox, JPanel> checkboxEffCWMap = checkboxEffonCWPanelMap.get(fcId);
            Map<JCheckBox, JPanel> checkboxEffOCCMap = checkboxEffonOccPanelMap.get(fcId);
            Map<JCheckBox, JPanel> checkboxElementMap = checkboxElementPanelMap.get(fcId);
            Map<JCheckBox, JPanel> checkboxIDMap = checkboxIDPanelMap.get(fcId);

            Map<String, Object> combinedProperties = processSelectedCheckboxes(
                    checkboxNameMap, checkboxDesMap, checkboxEffACMap,checkboxEffCWMap,checkboxEffOCCMap, checkboxElementMap, checkboxIDMap, debugLog);

            if (combinedProperties != null && combinedProperties.containsKey("propertyMapStrings")) {
                String id = ((Map<String, String>) combinedProperties.get("propertyMapStrings")).get("ID");
                if (id != null) {
                    selectedFailures.put(id, combinedProperties);
                    debugLog.append("  Successfully stored properties for fcId: ").append(id).append("\n");
                }
            }
        }

        // Log results
//        Application.getInstance().getGUILog().showMessage(debugLog.toString());

        // Create failure conditions
        createFailureConditions(selectedNode, selectedFailures);
    }

/**
 * @param checkboxNameMap The `checkboxNameMap` parameter is a map that associates each selected JCheckBox with its corresponding JPanel containing the name information for failure conditions.
 * @param checkboxDesMap The `checkboxDesMap` parameter in the method `processSelectedCheckboxes` is a map that links each JCheckBox to its corresponding JPanel containing the description text.
 * @param checkboxEffACMap The `checkboxEffACMap` parameter represents a mapping of check boxes to their corresponding panels containing information on the effect on aircraft.
 * @param checkboxEffCWMap The `checkboxEffCWMap` parameter is a mapping of checkboxes to their associated panels containing data on the effect of the failure condition on crew.
 * @param checkboxEffOCCMap The `checkboxEffOCCMap` parameter is a map that associates each checkbox with its corresponding panel containing information about the effect on occupants.
 * @param checkboxElementMap `checkboxElementMap` is a map linking each `JCheckBox` to its associated `JPanel` that holds element-specific properties for failure types, phases, and hazard classifications.
 * @param checkboxIDMap `checkboxIDMap`: A mapping of JCheckBox components to JPanel elements that contain the ID properties for each checkbox.
 * @param debugLog The `debugLog` parameter of type `StringBuilder` is a log accumulator that records detailed information about the retrieval and processing of properties from selected checkboxes for debugging purposes.
 * @return The `processSelectedCheckboxes` method returns a `Map<String, Object>` that contains two main components: a map of lists (`propertyMap`) and a map of strings (`propertyMapStrings`). The `propertyMap` includes properties like "Failure Type", "Phase", "Hazard Classification" (and optionally "System"), each associated with a list of selected values. The `propertyMapStrings` includes string properties such as "Name", "Description", "ID", "Effect On Aircraft", "Effect On Crew", and "Effect On Occupants". This map consolidates the selected checkbox properties for further processing or display, focusing on the first selected checkbox only.
 * @summary The `processSelectedCheckboxes` method collects and processes properties from panels associated with selected check boxes, compiling them into a map for further use.
 */
    private Map<String, Object> processSelectedCheckboxes(Map<JCheckBox, JPanel> checkboxNameMap,
                                                          Map<JCheckBox, JPanel> checkboxDesMap,
                                                          Map<JCheckBox, JPanel> checkboxEffACMap,
                                                          Map<JCheckBox, JPanel> checkboxEffCWMap,
                                                          Map<JCheckBox, JPanel> checkboxEffOCCMap,
                                                          Map<JCheckBox, JPanel> checkboxElementMap, Map<JCheckBox, JPanel> checkboxIDMap, StringBuilder debugLog) {
        Map<String, Object> combinedProperties = new HashMap<>();

        for (JCheckBox checkbox : checkboxNameMap.keySet()) {
            if (checkbox.isSelected()) {
                // Retrieve panels
                JPanel namePanel = checkboxNameMap.get(checkbox);
                JPanel desPanel = checkboxDesMap.get(checkbox);
                JPanel effAcPanel = checkboxEffACMap.get(checkbox);
                JPanel effCwPanel = checkboxEffCWMap.get(checkbox);
                JPanel effOccPanel = checkboxEffOCCMap.get(checkbox);
                JPanel elementPanel = checkboxElementMap.get(checkbox);
                JPanel idPanel = checkboxIDMap.get(checkbox);

                logPanelRetrieval(debugLog, namePanel, desPanel, effAcPanel,effCwPanel,effOccPanel, elementPanel);

                // Retrieve string and list properties
                String id = getPropertyFromPanel(idPanel, "ID");
                String name = getPropertyFromPanel(namePanel, "Name");
                String description = getPropertyFromPanel(desPanel, "Description");
                String effectAc = getPropertyFromPanel(effAcPanel, "EffectAC");
                String effectCw = getPropertyFromPanel(effCwPanel, "EffectCW");
                String effectOcc = getPropertyFromPanel(effOccPanel, "EffectOCC");

                logRetrievedProperties(debugLog, id, name, description, effectAc,effectCw,effectOcc);

                List<String> selectedFT = For_AC_Functions_with_All_effects_ResponseSubPannel.getSelected(elementPanel.getClientProperty("Failure Type"), "Failure Type");
                List<String> selectedPH = For_AC_Functions_with_All_effects_ResponseSubPannel.getSelected(elementPanel.getClientProperty("Phase"), "Phase");
                List<String> selectedHC = For_AC_Functions_with_All_effects_ResponseSubPannel.getSelected(elementPanel.getClientProperty("Hazard Classification"), "Hazard Classification");

                // Populate property maps
                Map<String, List<String>> propertyMap = new HashMap<>();
                Map<String, String> propertyMapStrings = new HashMap<>();

                propertyMap.put("Failure Type", selectedFT);
                propertyMap.put("Phase", selectedPH);
                propertyMap.put("Hazard Classification", selectedHC);
                if (systemList != null) {
                    propertyMap.put("System", systemList);
                }

                propertyMapStrings.put("Name", name);
                propertyMapStrings.put("Description", description);
                propertyMapStrings.put("ID", id);
                propertyMapStrings.put("Effect On Aircraft", effectAc);
                propertyMapStrings.put("Effect On Crew", effectCw);
                propertyMapStrings.put("Effect On Occupants", effectOcc);

                combinedProperties.put("propertyMap", propertyMap);
                combinedProperties.put("propertyMapStrings", propertyMapStrings);

                break; // Process only the first selected checkbox
            }
        }

        return combinedProperties;
    }

/**
 * @param selectedNode The `selectedNode` parameter represents the SysML element in the model for which failure conditions are being generated.
 * @param selectedFailures The `selectedFailures` parameter is a map that contains failure condition IDs as keys and their corresponding properties as values.
 * @summary The `createFailureConditions` method is responsible for generating and displaying failure conditions by processing a map of selected failures and updating the functional hazard assessment table in the provided SysML model element.
 */
    @SuppressWarnings("unchecked")
	private void createFailureConditions(Element selectedNode, Map<String, Map<String, Object>> selectedFailures) {
        StringBuilder creationMessage = new StringBuilder("Failure conditions Status:\n \n");

        for (Map.Entry<String, Map<String, Object>> entry : selectedFailures.entrySet()) {
            String fcId = entry.getKey();
            Map<String, Object> combinedProperties = entry.getValue();

            Map<String, List<String>> propertyMap = (Map<String, List<String>>) combinedProperties.get("propertyMap");
            Map<String, String> propertyMapStrings = (Map<String, String>) combinedProperties.get("propertyMapStrings");

            var failureCondition = new CreateFailureCondition(propertyMapStrings, fcId);
            creationMessage.append(failureCondition.create(selectedNode, propertyMap));
        }
        
        var FHAtable = new CreateFHATable();
        try {
        	creationMessage.append("\n").append(FHAtable.createTable_AC_Functions(selectedNode));
//			FHAtable.Createtable(selectedFunction);
		} catch (ReadOnlyElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Application.getInstance().getGUILog().showMessage(creationMessage.toString(), creationMessage.toString());
     // To dispose (destroys the dialog, cannot be reused)
        if (parentDialog != null) {
            parentDialog.dispose();
        }
    }

/**
 * @param debugLog The `debugLog` parameter is a `StringBuilder` used to accumulate debug information about the retrieval of various panels in the `logPanelRetrieval` method.
 * @param namePanel The `namePanel` parameter represents the JPanel object associated with displaying the name-related information in the log panel retrieval method.
 * @param desPanel The `desPanel` parameter represents the JPanel containing descriptive information for logging retrieval in debug logs.
 * @param effAcPanel The `effAcPanel` parameter represents the JPanel associated with effective aircraft configuration details.
 * @param effCwPanel The `effCwPanel` parameter is the retrieved JPanel representing the effectiveness of the crew.
 * @param effOccPanel The `effOccPanel` parameter represents the JPanel component used for logging and debugging purposes, specifically related to the effective occupation panel.
 * @param elementPanel The `elementPanel` parameter represents the specific JPanel that contains elements related to a particular aspect of the aircraft system being logged.
 * @summary This method appends the retrieval status of various JPanel components to a debug log.
 */
    private void logPanelRetrieval(StringBuilder debugLog, JPanel namePanel, JPanel desPanel, JPanel effAcPanel, JPanel effCwPanel, JPanel effOccPanel, JPanel elementPanel) {
        debugLog.append("  Retrieved NamePanel: ").append(namePanel).append("\n")
                .append("  Retrieved DesPanel: ").append(desPanel).append("\n")
                .append("  Retrieved EffACPanel: ").append(effAcPanel).append("\n")
                .append("  Retrieved EffCWPanel: ").append(effCwPanel).append("\n")
                .append("  Retrieved EffOccPanel: ").append(effOccPanel).append("\n")
                .append("  Retrieved elementPanel: ").append(elementPanel).append("\n");
    }

/**
 * @param debugLog The `debugLog` parameter of type `StringBuilder` in the method `logRetrievedProperties` is used to append and store detailed retrieval information for debugging purposes.
 * @param id The `id` parameter in the `logRetrievedProperties` method is a unique identifier for the property being logged.
 * @param name The `name` parameter represents the name of the retrieved property.
 * @param description The `description` parameter is a detailed explanation of the retrieved property being logged.
 * @param effectAc The `effectAc` parameter represents the effect of a failure condition on the aircraft (AC).
 * @param effectCw The `effectCw` parameter represents the effect of a failure condition on the crew, captured as a string.
 * @param effectOcc The `effectOcc` parameter represents the effect of a failure condition on the occurrence probability.
 * @summary The `logRetrievedProperties` method appends the retrieved properties of a safety condition to a debug log.
 */
    private void logRetrievedProperties(StringBuilder debugLog, String id, String name, String description, String effectAc, String effectCw, String effectOcc) {
        debugLog.append("  Retrieved Name: ").append(name).append("\n")
                .append("  Retrieved Description: ").append(description).append("\n")
                .append("  Retrieved ID: ").append(id).append("\n")
                .append("  Retrieved Effect on AC: ").append(effectAc).append("\n")
        		.append("  Retrieved Effect on CW: ").append(effectCw).append("\n")
        		.append("  Retrieved Effect on Occ: ").append(effectOcc).append("\n");
    }



/**
 * @param panel The `panel` parameter of type `JPanel` in method `getPropertyFromPanel` represents the graphical user interface panel to be searched for a specific client property.
 * @param propertyKey The `propertyKey` parameter of type `String` in the method `getPropertyFromPanel(JPanel panel, String propertyKey)` specifies the client property key used to identify and retrieve text from `JTextArea` or `JTextField` components within the given `JPanel`.
 * @return The return value of `getPropertyFromPanel` is a string that represents the text content from either a `JTextArea` or a `JTextField` within the provided `JPanel`, associated with the specified property key. If no such component is found, it returns null.
 * @summary The method `getPropertyFromPanel` retrieves the text content from a specific component (either `JTextArea` or `JTextField`) identified by a given property key within a nested panel hierarchy.
 */
	private String getPropertyFromPanel(JPanel panel, String propertyKey) {
		for (Component component : panel.getComponents()) {
			if (component instanceof JPanel) {
				JPanel subPanel = (JPanel) component;

				// Check if the subPanel has the desired client property
				Object propertyValue = subPanel.getClientProperty(propertyKey);
				if (propertyValue instanceof JTextArea) {
					return ((JTextArea) propertyValue).getText(); // Return text from JTextArea
				} else if (propertyValue instanceof JTextField) {
					return ((JTextField) propertyValue).getText(); // Return text from JTextField
				}

				// Recursively search inside nested panels if needed
				String result = getPropertyFromPanel(subPanel, propertyKey);
				if (result != null) {
					return result;
				}
			}else if (component instanceof JTextArea ) {
				return ((JTextArea) component).getText();

			}else if (component instanceof JTextField ) {
				return ((JTextField) component).getText();
			}
		}
		return null; // Return null if no matching JTextArea or JTextField with the property is found
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, List<String>>>[] splitMap(Map<String, Map<String, List<String>>> formattedResponse) {
		// Determine the total number of entries in the outer map

        StringBuilder debugLog = new StringBuilder();
		int totalSize = formattedResponse.size();
		debugLog.append("  Formated Response size: ").append(formattedResponse.size()).append("\n");

		// Calculate the split point
		int leftSize = (totalSize + 1) / 2; // L will have more elements if odd
		debugLog.append(" Left Pannel size: ").append(leftSize).append("\n");

		// Create the left and right maps
		Map<String, Map<String, List<String>>> leftMap = new HashMap<>();
		Map<String, Map<String, List<String>>> rightMap = new HashMap<>();

		// Iterator over the entries in the map
		Iterator<Map.Entry<String, Map<String, List<String>>>> iterator = formattedResponse.entrySet().iterator();
		int count = 0;

		// Add to the left map until the split point
		while (count < leftSize && iterator.hasNext()) {
			Map.Entry<String, Map<String, List<String>>> entry = iterator.next();
			leftMap.put(entry.getKey(), entry.getValue());
			count++;
		}

		// Add the rest to the right map
		while (iterator.hasNext()) {
			Map.Entry<String, Map<String, List<String>>> entry = iterator.next();
			rightMap.put(entry.getKey(), entry.getValue());
		}
		

        // Log results
//        Application.getInstance().getGUILog().showMessage(debugLog.toString());

		return new Map[]{leftMap, rightMap};
	}
}

