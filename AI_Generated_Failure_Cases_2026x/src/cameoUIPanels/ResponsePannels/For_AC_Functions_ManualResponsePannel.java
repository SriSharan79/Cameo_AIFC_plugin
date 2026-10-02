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

import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.PropertiesFunction;
import elementDataExtraction.StereotypeSearch;
import creationOfSafetyVP.CreateFailureCondition;
import creationOfSafetyVP.ElementFinder_SVP;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-19
 * @summary The `For_AC_Functions_ManualResponsePannel` class is responsible for managing and processing user interactions within a graphical panel to facilitate the selection and retrieval of aircraft function properties in OSIRIS.
 */
public class For_AC_Functions_ManualResponsePannel extends JPanel {

	private static final long serialVersionUID = 1L;


	ElementFinder_SVP elementfinder =new ElementFinder_SVP();
	NameDerivator ND = new NameDerivator();
	StereotypeSearch STC =new StereotypeSearch();

	List<String> systemList =new ArrayList<String>();
	
/**
 * @param Response The `Response` parameter in the method `For_AC_Functions_ManualResponsePannel` is a `String` that represents the failure condition response to be displayed and edited within a text area component of the user interface.
 * @param selectedNode The `selectedNode` parameter is the Element from the SysML model that represents the context or function being analyzed for failure conditions.
 * @param propertyMapUpdated A `Map` containing updated property names as keys and their corresponding list of values, used to initialize the system list and other properties for creating a failure condition.
 * @return The return value of the `For_AC_Functions_ManualResponsePannel` method is a panel that allows users to input data necessary for creating failure conditions in an aircraft system. This panel includes text areas, labels, and subpanels for various properties such as ID, Failure Condition Statement, Description, Effects on Aircraft, Crew, and Occupants. It also provides checkboxes for selecting Failure Types, Phases, and Hazard Classifications. Upon submitting the data via a button click, it processes the input to create a failure condition and displays relevant messages or errors in the GUI log.
 * @summary The `For_AC_Functions_ManualResponsePannel` method is responsible for creating and configuring a graphical user interface panel that allows users to input data required for generating failure conditions.
 */
	public For_AC_Functions_ManualResponsePannel(String Response, Element selectedNode, Map<String, List<String>> propertyMapUpdated) {

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
 * @param e The Java parameter `e` of type `ActionEvent` in the method `actionPerformed` represents the event that triggered this method.
 * @summary The `actionPerformed` method handles the event when the user clicks a button to create a failure condition, retrieving relevant properties and logging each step.
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
	            String effectAC = (String) getPropertyFromPanel(effectAC_subpanel, "Effect On Aicraft");
	            String effectCW = (String) getPropertyFromPanel(effectAC_subpanel, "Effect On Crew");
	            String effectOCC = (String) getPropertyFromPanel(effectAC_subpanel, "Effect On Occupants");

	            debugLog.append("  Retrieved ID: ").append(id).append("\n");
	            debugLog.append("  Retrieved Name: ").append(name).append("\n");
	            debugLog.append("  Retrieved Description: ").append(description).append("\n");
	            debugLog.append("  Retrieved Effect On Aicraft: ").append(effectAC).append("\n");
	            debugLog.append("  Retrieved Effect On Crew: ").append(effectCW).append("\n");
	            debugLog.append("  Retrieved Effect On Occupants: ").append(effectOCC_subpanel).append("\n");

	            propertyMapStrings.put("ID", id);
	            propertyMapStrings.put("Name", name);
	            propertyMapStrings.put("Description", description);
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
	    			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	    				.addComponent(R_panel, GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
	    				.addComponent(textScrollPane)
	    				.addComponent(lblNewLabel_Response, GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
	    				.addComponent(btn_CreateFC, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE))
	    			.addContainerGap())
	    );
	    groupLayout.setVerticalGroup(
	    	groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	    			.addGap(7)
	    			.addComponent(lblNewLabel_Response)
	    			.addGap(8)
	    			.addComponent(textScrollPane, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE)
	    			.addPreferredGap(ComponentPlacement.UNRELATED)
	    			.addComponent(R_panel, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE)
	    			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	    			.addComponent(btn_CreateFC)
	    			.addContainerGap())
	    );
	    setLayout(groupLayout);

	    // Final debug message
	    debugLog.append("ManualResponsePannel3 creation completed.\n");
//	    Application.getInstance().getGUILog().showMessage(debugLog.toString());
	}

	
/**
 * @param panel The `panel` parameter represents the JPanel from which a specific client property is retrieved.
 * @param propertyKey The `propertyKey` parameter specifies the client property key used to identify and retrieve text values from nested panels, text areas, or text fields within a given JPanel.
 * @return The `getPropertyFromPanel` method returns a `String` value representing the text content of a `JTextArea` or `JTextField` component associated with the given `propertyKey` within the specified `panel`. If such a component is not found, it returns `null`.
 * @summary This Java method `getPropertyFromPanel` retrieves text from a specific property key within nested panels, focusing on `JTextArea` and `JTextField` components.
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
 * @param allElements The `allElements` parameter is a list of SysML elements that will be processed to create checkboxes within the generated panel.
 * @param Title The `Title` parameter specifies the header label text for the checkbox panel, which should be one of "Failure Type" or "Hazard Classification".
 * @return The `AddCheckboxs` method generates and returns a `JPanel` that contains a labeled section with a list of checkboxes. Each checkbox corresponds to an element from the provided list, and they are organized into a scrollable panel for easy navigation. This panel is used in the context of user interfaces where elements need to be selected or deselected based on their presence in the given list.
 * @summary The `AddCheckboxs` method is responsible for creating and returning a JPanel containing checkboxes for each element in the provided list, with an optional header title.
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
 * @param scrollPane The `scrollPane` parameter is a JPanel that serves as the container holding the list of JCheckBox components to be extracted.
 * @return The return value of the `extractRadioButtonList` method is a list of JCheckBox components that were previously stored as a client property on a given JPanel.
 * @summary The method `extractRadioButtonList` retrieves a list of JCheckBoxes associated with a given JPanel, specifically from its client properties.
 */
	@SuppressWarnings("unchecked")
	public static List<JCheckBox> extractRadioButtonList(JPanel scrollPane) {
		return (List<JCheckBox>) scrollPane.getClientProperty("radioButtonList");
	}
	
/**
 * @param panel The `panel` parameter of type `JPanel` in the method `extractList` is a Swing component that stores client properties from which a list of strings will be extracted.
 * @param string The `string` parameter in the `extractList` method represents the key used to retrieve a list of strings from the client properties of the given `JPanel`.
 * @return The `extractList` method retrieves a list of strings from the client properties of a given JPanel object using a specified key. The return value is the list associated with that key, or null if no such list exists. This allows for easy extraction and use of string lists stored within the panel's client properties.
 * @summary The `extractList` method retrieves a list of strings from the client properties of a given JPanel using the specified key.
 */
	@SuppressWarnings("unchecked")
	public static List<String> extractList(JPanel panel, String string) {
		// TODO Auto-generated method stub
		return (List<String>) panel.getClientProperty(string);
	}

/**
 * @param radioButtonList `radioButtonList`: A list of JCheckBox components representing user-selectable options.
 * @param stringList The `stringList` parameter contains strings that correspond to each item in `radioButtonList`, providing labels or values for selected checkboxes.
 * @return The `getSelectedItem` method returns a list of strings (`List<String>`) that correspond to the elements in the provided string list where their respective checkboxes are selected.
 * @summary The `getSelectedItem` method extracts and returns a list of strings corresponding to selected checkboxes from given lists of JCheckBox and String elements.
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
 * @param text The `text` parameter of type `String` in the method `CreateSubpanneldes` is used as a label prefix for the text field within the created subpanel.
 * @param name The `name` parameter represents the initial text value to be displayed in the text field of the created sub-panel.
 * @return The return value of the `CreateSubpanneldes` method is a `JPanel`. This `JPanel` contains a label displaying the provided text and a text field initialized with the provided name. The panel is formatted with specific alignment, background color, and border settings to ensure consistent visual presentation.
 * @summary The `CreateSubpanneldes` method constructs and returns a `JPanel` with a label and text field, configured based on the provided text and name.
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
 * @param clientProperty A generic object used to pass a JPanel containing UI components whose selection state needs to be evaluated.
 * @param Type The `Type` parameter specifies the category of items to be filtered when retrieving the selected elements from a panel.
 * @return The `getSelected` method returns a list of strings representing the names of selected items from a JPanel containing checkboxes and corresponding item names. The purpose is to retrieve user-selected options based on their interaction with the graphical user interface components.
 * @summary The `getSelected` method retrieves a list of selected items from a given JPanel based on the state of its checkboxes.
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
