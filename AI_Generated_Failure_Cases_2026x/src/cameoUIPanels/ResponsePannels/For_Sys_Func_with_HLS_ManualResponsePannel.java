package cameoUIPanels.ResponsePannels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.magicdraw.uml.ElementIcon;
import com.nomagic.ui.ResizableIcon;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;


import creationOfSafetyVP.CreateFailureCondition;
import creationOfSafetyVP.ElementFinder_SVP;
import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.PropertiesFunction;
import elementDataExtraction.StereotypeSearch;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-19
 * @summary The `For_Sys_Func_with_HLS_ManualResponsePannel` class is responsible for managing and processing user interactions within the OSIRIS plugin, particularly focusing on handling manual responses related to system functions with high-level safety (HLS) context in a graphical user interface.
 */
public class For_Sys_Func_with_HLS_ManualResponsePannel extends JPanel {

	private static final long serialVersionUID = 1L;


	ElementFinder_SVP elementfinder =new ElementFinder_SVP();
	NameDerivator ND = new NameDerivator();
	StereotypeSearch STC =new StereotypeSearch();

	List<String> systemList =new ArrayList<String>();
	
/**
 * @param Response The `Response` parameter is a string containing the generated response text to be displayed and edited in the manual response panel for the Functional Hazard Assessment.
 * @param selectedNode The parameter `selectedNode` of type `Element` in the method `For_Sys_Func_with_HLS_ManualResponsePannel` represents the specific SysML model element selected for generating or refining failure conditions.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter is a map containing key-value pairs where each key represents a property type and its corresponding value is a list of strings, used to provide updated system properties for the manual response panel.
 * @return The return value of the `For_Sys_Func_with_HLS_ManualResponsePannel` method is an instance of the `For_Sys_Func_with_HLS_ManualResponsePannel` class. This returned object represents a graphical user interface panel that facilitates the creation and configuration of failure conditions for aircraft systems, based on input parameters such as response text, selected SysML model element, and updated property map. The panel includes various subpanels and components to capture detailed information about the failure condition, including its ID, name, description, effects on higher-level systems, aircraft, crew, and occupants, as well as associated failure types, phases, and hazard classifications.
 * @summary The `For_Sys_Func_with_HLS_ManualResponsePannel` method is responsible for creating a graphical user interface panel to generate and manage failure conditions based on selected system functions and properties.
 */
	public For_Sys_Func_with_HLS_ManualResponsePannel(String Response, Element selectedNode, Map<String, List<String>> propertyMapUpdated) {

	    StringBuilder debugLog = new StringBuilder(); // Debug log accumulator
	    debugLog.append("ManualResponsePannel3 creation started.\n");

//	    Application.getInstance().getGUILog().showMessage("ManualResponsePannel3 creation in process...");

	    // Retrieve system list if available
	    if (propertyMapUpdated.containsKey("System") && propertyMapUpdated.get("System") != null) {
	        systemList = propertyMapUpdated.get("System");
	        debugLog.append("System list retrieved: ").append(systemList).append("\n");
	    }
	    


	    JLabel lblNewLabel_Response = new JLabel("Response Generated");
	    lblNewLabel_Response.setFont(new Font("Tahoma", Font.BOLD, 20));

	    JTextArea textArea = new JTextArea(Response);
	    textArea.setWrapStyleWord(true);
	    textArea.setLineWrap(true);

	    JScrollPane textScrollPane = new JScrollPane(textArea);
	    textScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

	    JScrollPane R_panel = new JScrollPane();
	    R_panel.setBackground(Color.WHITE);

	    JPanel Sup_panel = new JPanel();
	    Sup_panel.setPreferredSize(new Dimension(10, 200));
	    Sup_panel.setBackground(Color.WHITE);

	    JPanel stringPanel = new JPanel();
	    stringPanel.setAlignmentY(Component.TOP_ALIGNMENT);
	    stringPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    

	    // Create subpanels and add to stringPanel
	    debugLog.append("Initializing subpanels...\n");
	    var id_subpanel = CreateSubpanneldes("ID", "FC-XXX");
	    
	    debugLog.append("  ID subpanel created.\n");

	    var name_subpanel = CreateSubpanneldes("Failure Condition Statement", "Failure Condition Statement");
	    debugLog.append("  Name subpanel created.\n");

	    var des_subpanel = CreateSubpanneldes("Description", "Failure Condition Description");
	    debugLog.append("  Description subpanel created.\n");
	    
	    var effectHLS_subpanel = CreateSubpanneldes("Effect On Higher Level System", "Effect On Higher Level System");
	    debugLog.append("  Effect subpanel created.\n");

	    var effectAC_subpanel = CreateSubpanneldes("Effect On Aicraft", "Effect On Aicraft");
	    debugLog.append("  Effect subpanel created.\n");
	    
	    var effectCW_subpanel = CreateSubpanneldes("Effect On Crew", "Effect On Crew");
	    debugLog.append("  Effect subpanel created.\n");
	    
	    var effectOCC_subpanel = CreateSubpanneldes("Effect On Occupants", "Effect On Occupants");
	    debugLog.append("  Effect subpanel created.\n");

	    

	    stringPanel.setLayout(new BoxLayout(stringPanel, BoxLayout.PAGE_AXIS));
	    stringPanel.add(id_subpanel);
	    stringPanel.add(name_subpanel);
	    stringPanel.add(des_subpanel);
	    stringPanel.add(effectHLS_subpanel);
	    stringPanel.add(effectAC_subpanel);
	    stringPanel.add(effectCW_subpanel);
	    stringPanel.add(effectOCC_subpanel);
	    debugLog.append("All subpanels added to stringPanel.\n");

	    
	    JPanel ElementPanel = new JPanel();
		ElementPanel.setBackground(Color.WHITE);
		ElementPanel.setPreferredSize(new Dimension(10, 150));
		ElementPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		ElementPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		ElementPanel.setLayout(new BoxLayout(ElementPanel, BoxLayout.LINE_AXIS));
		
		var functionProperties = new PropertiesFunction();

		Map<String, List<Element>> propertyMap= new HashMap<String, List<Element>>();

		Element selectedfunction = null;
		if (selectedNode instanceof CallBehaviorAction) {		
			// Get the behavior of the CallBehaviorAction
			var selectedfunctionAction =selectedNode;
			var function = ((CallBehaviorAction) selectedNode).getBehavior();

			// Check if the function is not null and cast it to Element
			if (function != null) {
				 selectedfunction = (Element) function;
			}
			 propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction,selectedfunctionAction);

		} else if (selectedNode instanceof Activity) {
			// If selectedNode is an Activity, cast it to Element
			 selectedfunction = (Element) selectedNode;
			 propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction);
		}


		List<Element> MalfunctionList = propertyMap.get("Defined Failure Type");
		
		var AllFTs =elementfinder.FindAllFailure();
		AllFTs.addAll(MalfunctionList);
		
		JPanel FT_panel =AddCheckboxs(AllFTs, "Failure Type");

	    debugLog.append("  FT_panelt subpanel created.\n");
		JPanel PH_panel =AddCheckboxs(elementfinder.FindAllPhases(), "Phase");

	    debugLog.append("  PH_panel subpanel created.\n");
		JPanel HC_panel =AddCheckboxs(elementfinder.FindAllHazard(),  "Hazard Classification");

	    debugLog.append("  HC_panel subpanel created.\n");
		
		ElementPanel.add(PH_panel);
		ElementPanel.add(FT_panel);
		ElementPanel.add(HC_panel);

	    debugLog.append("  All subpanels added to ElementPanel.\\n\n");



	    R_panel.setViewportView(Sup_panel);
	    Sup_panel.setLayout(new BoxLayout(Sup_panel, BoxLayout.PAGE_AXIS));
	    Sup_panel.add(stringPanel);
	    Sup_panel.add(ElementPanel);
	    R_panel.setBorder(new BevelBorder(BevelBorder.LOWERED));


	    JButton btn_CreateFC = new JButton("Create Failure Condition");
	    btn_CreateFC.addActionListener(new ActionListener() {
/**
 * @param e The Java parameter `e` of type `ActionEvent` in the method `actionPerformed` represents the event that triggers this method, specifically when a button is clicked.
 * @summary The `actionPerformed` method handles the creation of a failure condition in OSIRIS by retrieving selected properties, populating maps, and invoking the failure condition creation process.
 */
	        public void actionPerformed(ActionEvent e) {
	            debugLog.append("Create Failure Condition button clicked.\n");

	            // Initialize property maps
	            Map<String, List<String>> propertyMap = new HashMap<>();
	            Map<String, String> propertyMapStrings = new HashMap<>();
	            // Retrieve failure type, phase, and hazard classification
	            List<String> selectedFT = getSelected(FT_panel, "Failure Type");
	            List<String> selectedPH = getSelected(PH_panel, "Phase");
	            List<String> selectedHC = getSelected(HC_panel, "Hazard Classification");
	            

	            // Log retrieval of failure type, phase, and hazard classification
	            debugLog.append("  Retrieved Failure Type: ").append(selectedFT).append("\n");
	            debugLog.append("  Retrieved Phase: ").append(selectedPH).append("\n");
	            debugLog.append("  Retrieved Hazard Classification: ").append(selectedHC).append("\n");
//	            
//
	            // Populate propertyMap
	            propertyMap.put("Failure Type", selectedFT);
	            propertyMap.put("Phase", selectedPH);
	            propertyMap.put("Hazard Classification", selectedHC);

//	            // Populate property maps
	            propertyMap.put("System", systemList);
	            debugLog.append("System list added to propertyMap.\n");

	            // Retrieve string properties
	            String id = (String) getPropertyFromPanel(id_subpanel, "ID");
	            String name = (String) getPropertyFromPanel(name_subpanel, "Name");
	            String description = (String) getPropertyFromPanel(des_subpanel, "Description");
	            String effectHLS = (String) getPropertyFromPanel(effectAC_subpanel, "Effect On Higher Level System");
	            String effectAC = (String) getPropertyFromPanel(effectAC_subpanel, "Effect On Aicraft");
	            String effectCW = (String) getPropertyFromPanel(effectCW_subpanel, "Effect On Crew");
	            String effectOCC = (String) getPropertyFromPanel(effectOCC_subpanel, "Effect On Occupants");

	            debugLog.append("  Retrieved ID: ").append(id).append("\n");
	            debugLog.append("  Retrieved Name: ").append(name).append("\n");
	            debugLog.append("  Retrieved Description: ").append(description).append("\n");
	            debugLog.append("  Retrieved Effect on Higher Level: ").append(effectHLS).append("\n");
	            debugLog.append("  Retrieved Effect On Aicraft: ").append(effectAC).append("\n");
	            debugLog.append("  Retrieved Effect On Crew: ").append(effectCW).append("\n");
	            debugLog.append("  Retrieved Effect On Occupants: ").append(effectOCC_subpanel).append("\n");

	            propertyMapStrings.put("ID", id);
	            propertyMapStrings.put("Name", name);
	            propertyMapStrings.put("Description", description);
	            propertyMapStrings.put("Effect On Higher Level System", effectHLS);
	            propertyMapStrings.put("Effect On Aicraft", effectAC);
	            propertyMapStrings.put("Effect On Crew", effectCW);
	            propertyMapStrings.put("Effect On Occupants", effectOCC);

	            // Create the failure condition
	            try {
	                debugLog.append("Creating failure condition...\n");
	                var failureCondition = new CreateFailureCondition(propertyMapStrings, id);
	                String creationMessage = failureCondition.create(selectedNode, propertyMap);
	                debugLog.append("Failure condition created successfully.\n")
	                        .append("Creation message: ").append(creationMessage).append("\n");
	                Application.getInstance().getGUILog().showMessage(creationMessage);
	            } catch (Exception ex) {
	                debugLog.append("Error during failure condition creation: ").append(ex.getMessage()).append("\n");
	                Application.getInstance().getGUILog().showError("Error: " + ex.getMessage());
	            }

	            // Display full debug log
//	            Application.getInstance().getGUILog().showMessage(debugLog.toString());
	        }
	    });

	    JLabel lblNewLabel = new JLabel("Add Data to Create Failure Condition");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    R_panel.setColumnHeaderView(lblNewLabel);
	    

	    GroupLayout groupLayout = new GroupLayout(this);
	    groupLayout.setHorizontalGroup(
	    	groupLayout.createParallelGroup(Alignment.TRAILING)
	    		.addGroup(groupLayout.createSequentialGroup()
	    			.addContainerGap()
	    			.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
	    				.addComponent(R_panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
	    				.addComponent(textScrollPane, Alignment.LEADING)
	    				.addComponent(lblNewLabel_Response, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
	    				.addComponent(btn_CreateFC, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE))
	    			.addContainerGap())
	    );
	    groupLayout.setVerticalGroup(
	    	groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	    			.addGap(7)
	    			.addComponent(lblNewLabel_Response)
	    			.addGap(8)
	    			.addComponent(textScrollPane, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
	    			.addPreferredGap(ComponentPlacement.RELATED)
	    			.addComponent(R_panel, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE)
	    			.addPreferredGap(ComponentPlacement.UNRELATED)
	    			.addComponent(btn_CreateFC)
	    			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	    );
	    setLayout(groupLayout);

	    // Final debug message
	    debugLog.append("ManualResponsePannel3 creation completed.\n");
//	    Application.getInstance().getGUILog().showMessage(debugLog.toString());
	}

	
/**
 * @param panel The `panel` parameter of type `JPanel` represents the top-level container panel from which to recursively search and extract text properties based on the given key.
 * @param propertyKey The `propertyKey` parameter in the method `getPropertyFromPanel` specifies the client property key to search for within the components of the given JPanel.
 * @return The `getPropertyFromPanel` method returns a string that represents the text content of either a `JTextArea` or a `JTextField` component found within the provided `JPanel`. The search for the desired component is based on the specified property key. If no matching component with the given property is found, it returns null.
 * @summary The `getPropertyFromPanel` method recursively searches through a given JPanel and its sub-components to find and retrieve text from any JTextArea or JTextField that matches a specified client property key.
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
	
/**
 * @param allElements The `allElements` parameter is a list of SysML model elements that will be used to create checkboxes within the generated JPanel.
 * @param Title The `Title` parameter specifies the title displayed at the top of the panel containing checkboxes, which helps categorize or describe the elements being listed.
 * @return The return value of the `AddCheckboxs` method is a `JPanel`. This panel includes:
- A header label centered at the top, displaying the given title.
- A scrollable panel containing checkboxes for each unique element in the provided list. The checkboxes are grouped into buttons if specified by the title.
- If no elements are present, a message indicating "No Related Elements are Assigned" is displayed instead of checkboxes.
This panel serves as a visual and interactive component to display and select from a list of elements, formatted with appropriate styling and layout adjustments.
 * @summary The `AddCheckboxs` method generates a JPanel containing checkboxes based on a list of elements, organizing them with a specified title and handling cases where the list is empty.
 */
	public JPanel AddCheckboxs(List<Element> allElements, String Title) {
		List<String> stringList = Arrays.asList("Failure Type", "Hazard Classification");
		// Remove duplicates from allItems
		Set<Element> uniqueElements = new LinkedHashSet<>(allElements);
		List<Element> allItems = new ArrayList<>(uniqueElements);
		// Column header for radio buttons (all items)
		JLabel chooseHeader = new JLabel(Title, JLabel.CENTER);
		chooseHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		chooseHeader.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		// Create a panel to hold radio buttons
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		radioButtonPanel.setBackground(Color.WHITE);

		List<JCheckBox> radioButtonList = new ArrayList<>();
		List<String> NameList = new ArrayList<>();

		ButtonGroup buttonGroup = null;
		if (stringList.contains(Title)) {
			buttonGroup = new ButtonGroup(); // Group for radio buttons //// if you want to have multiple selection option delete or comment Button group
		}

		// Check if the list is null or empty
		if (allItems == null || allItems.isEmpty()) {
			// Create a label with the message
			JLabel noElementsLabel = new JLabel("<html><body style='width: 100px;'>No Related Elements are Assigned</body></html>");
			noElementsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			noElementsLabel.setForeground(Color.GRAY); // Optional: Set the color to gray to distinguish it

			// Align the label to the left side (optional)
			noElementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

			// Add the label to the panel
			radioButtonPanel.add(noElementsLabel);
		} else {
			// Add radio buttons for all items in the right panel
			for (Element item : allItems) {
				// Create a panel to hold the label (with icon) and the radio button
				JPanel itemPanel = new JPanel();
				itemPanel.setBackground(Color.WHITE);
				itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.LINE_AXIS)); // Horizontal layout for icon, label, and radio button

				// Create a label and set the icon for the item
				JLabel itemLabel = new JLabel(ND.getName(item));  
				ResizableIcon icon = ElementIcon.getIcon((BaseElement) item);
				itemLabel.setIcon(icon);

				String Name = ND.getName((Element) item);  // Be sure that item is always of type `Element`
				NameList.add(Name);

				// Create a radio button for the item
				JCheckBox radioButton = new JCheckBox();
				
				radioButton.setBackground(Color.WHITE);
				
				radioButtonList.add(radioButton);
				if (stringList.contains(Title)) {
					buttonGroup.add(radioButton); // Add each radio button to the group
				}

				// Add the label (with icon) and the radio button to the item panel
				itemPanel.add(radioButton);
				itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add space between label and radio button if needed
				itemPanel.add(itemLabel);
				// Set left alignment for the item panel itself
				itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

				// Add the item panel to the main radio button panel
				radioButtonPanel.add(itemPanel);
			}
		}
		// Optionally, set the alignment of the radioButtonPanel to left if needed
		radioButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Add radioButtonPanel into a JScrollPane
		JScrollPane radioScrollPane = new JScrollPane(radioButtonPanel);
		radioScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		// Wrap both the choose header and scrollPane in a panel
		JPanel choosePanel = new JPanel(new BorderLayout());
		choosePanel.setBackground(Color.WHITE);
		choosePanel.add(chooseHeader, BorderLayout.NORTH);  // Add header
		choosePanel.add(radioScrollPane, BorderLayout.CENTER); // Add scrollable content

		// Store the radio button list in client properties for later use
		choosePanel.putClientProperty("radioButtonList", radioButtonList);
		choosePanel.putClientProperty("NameList", NameList);

		return choosePanel;
	}

	// Method to extract the list of radio buttons from the panel
/**
 * @param scrollPane The `scrollPane` parameter is a JPanel that contains the list of JCheckBox components stored as a client property.
 * @return The `extractRadioButtonList` method retrieves a list of radio buttons (`JCheckBox` instances) that have been previously stored as a client property on the provided `JPanel` object.
 * @summary The `extractRadioButtonList` method retrieves a list of JCheckBox components stored as a client property in the given JPanel.
 */
	@SuppressWarnings("unchecked")
	public static List<JCheckBox> extractRadioButtonList(JPanel scrollPane) {
		return (List<JCheckBox>) scrollPane.getClientProperty("radioButtonList");
	}
	
/**
 * @param panel The `panel` parameter is a reference to the JPanel from which client properties are retrieved and converted into a list of strings.
 * @param string The `string` parameter in the `extractList` method is a key used to retrieve a list of strings from the client properties of the given `JPanel`.
 * @return The `extractList` method returns a list of strings that have been stored as client properties on the provided `JPanel`. This list is identified by the given key string.
 * @summary The `extractList` method retrieves a list of strings from the client properties of a given JPanel using a specified key.
 */
	@SuppressWarnings("unchecked")
	public static List<String> extractList(JPanel panel, String string) {
		// TODO Auto-generated method stub
		return (List<String>) panel.getClientProperty(string);
	}

/**
 * @param radioButtonList The `radioButtonList` parameter represents a list of `JCheckBox` objects used to determine which items are selected for generating the resulting list.
 * @param stringList The `stringList` parameter contains the strings associated with each JCheckBox in radioButtonList, and its elements are added to the result if their corresponding checkbox is selected.
 * @return The `getSelectedItem` method returns a `List<String>` containing the items from `stringList` that correspond to the selected checkboxes in `radioButtonList`.
 * @summary The `getSelectedItem` method returns a list of strings corresponding to the selected checkboxes in a given list.
 */
	public static List<String> getSelectedItem(List<JCheckBox> radioButtonList, List<String> stringList) {
		List<String> selectedItems = new ArrayList<>();

		// Iterate over the checkboxes with their index
		for (int i = 0; i < radioButtonList.size(); i++) {
			JCheckBox checkBox = radioButtonList.get(i);
			// Check if the checkbox is selected
			if (checkBox.isSelected()) {
				// Add the corresponding element from the string list
				selectedItems.add(stringList.get(i));
			}
		}
		return selectedItems;
	}

/**
 * @param text The `text` parameter in the method `CreateSubpanneldes` is a `String` representing the label text to be displayed for the input field on the created JPanel.
 * @param name The `name` parameter in the `CreateSubpanneldes` method is a `String` that represents the initial text value to be displayed in a text field within the created JPanel.
 * @return The `CreateSubpanneldes` method returns a `JPanel` that contains a labeled text field. The panel is designed with specific styling properties such as border, background color, and alignment settings. It includes a `JLabel` displaying the provided text and a `JTextField` initialized with the given name.
 * @summary The `CreateSubpanneldes` method is responsible for creating and configuring a JPanel with a label and text field based on the provided text and name inputs.
 */
	public JPanel CreateSubpanneldes(String text, String name) {
		JPanel subMain_panel_1 = new JPanel();
		subMain_panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		subMain_panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		subMain_panel_1.setBackground(Color.WHITE);
		subMain_panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblId_1 = new JLabel(text + ":");
		lblId_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblId_1.setBackground(Color.WHITE);

		JTextField textArea_1 = new JTextField(name);
		textArea_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textArea_1.setBorder(null);
		
		subMain_panel_1.putClientProperty(text, textArea_1);
		
		GroupLayout gl_subMain_panel_1 = new GroupLayout(subMain_panel_1);
		gl_subMain_panel_1.setHorizontalGroup(
				gl_subMain_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_subMain_panel_1.createSequentialGroup()
						.addComponent(lblId_1)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textArea_1, GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_subMain_panel_1.setVerticalGroup(
				gl_subMain_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_subMain_panel_1.createSequentialGroup()
						.addGroup(gl_subMain_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_subMain_panel_1.createSequentialGroup()
										.addGap(3)
										.addComponent(lblId_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(textArea_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addGap(1))
				);
		subMain_panel_1.setLayout(gl_subMain_panel_1);

		return subMain_panel_1;
	}

/**
 * @param clientProperty The `clientProperty` parameter in the `getSelected` method is an object that represents a graphical user interface component, specifically a JPanel, from which the method extracts relevant checkboxes and their names to determine selected items.
 * @param Type The `Type` parameter of type `String` in the method `getSelected(Object clientProperty, String Type)` specifies the type or category of items to be selected from the panel.
 * @return The `getSelected` method returns a list of strings that represent the names of the currently selected items within a panel. This is determined by checking the state of checkboxes and extracting their corresponding labels from a predefined name list associated with the panel. The returned list, therefore, contains the textual representations of these selected elements.
 * @summary The `getSelected` method retrieves a list of names corresponding to the selected checkboxes within a given JPanel.
 */
	public List<String> getSelected(Object clientProperty, String Type) {
		// TODO Auto-generated method stub
		JPanel panel = null;
		List<String> selectedItems = new ArrayList<>();
		if(clientProperty instanceof JPanel) {
			panel = (JPanel)clientProperty;
		}
		List<JCheckBox>Req_checkBoxList = extractRadioButtonList(panel);
		List<String>Req_Names = extractList(panel,"NameList");
		selectedItems= getSelectedItem(Req_checkBoxList,Req_Names);

		
		return selectedItems;
	}
}
