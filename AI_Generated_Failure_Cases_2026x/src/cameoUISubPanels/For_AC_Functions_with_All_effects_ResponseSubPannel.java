package cameoUISubPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.magicdraw.uml.ElementIcon;
import com.nomagic.ui.ResizableIcon;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.PropertiesFunction;
import elementDataExtraction.StereotypeSearch;

import creationOfSafetyVP.ElementFinder_SVP;
import javax.swing.border.BevelBorder;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-19
 * @summary The `For_AC_Functions_with_All_effects_ResponseSubPannel` class is responsible for managing and displaying checkboxes and subpanels related to aircraft functions and their effects in a graphical user interface.
 */
public class For_AC_Functions_with_All_effects_ResponseSubPannel extends JPanel {  

	private static final long serialVersionUID = 1L;
	public Map<JCheckBox, JPanel> checkboxNamePanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxElementPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxIDPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxDesPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffACPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffCWPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffOccPanelMap = new HashMap<>();

	ElementFinder_SVP elementfinder =new ElementFinder_SVP();
	NameDerivator ND = new NameDerivator();
	StereotypeSearch STC =new StereotypeSearch();

/**
 * @param i The parameter `i` of type `int` in the method `For_AC_Functions_with_All_effects_ResponseSubPannel` represents the index number used to uniquely identify and label each failure condition checkbox within the generated GUI panel.
 * @param selectedNode The parameter `selectedNode` of type `Element` represents the specific SysML model element (such as a CallBehaviorAction or Activity) that has been selected for analysis within the OSIRIS plugin.
 * @param fcId The `fcId` parameter is a unique identifier for the failure condition being processed in the method.
 * @param fcDetails `fcDetails`: A map containing lists of strings that detail various aspects of failure conditions such as names, descriptions, and effects on aircraft, crew, and occupants.
 * @return The return value of the `For_AC_Functions_with_All_effects_ResponseSubPannel` method is a configured and initialized instance of `For_AC_Functions_with_All_effects_ResponseSubPannel`. This instance contains various UI components such as panels, checkboxes, and text fields that are set up based on the provided inputs like selected node, failure condition ID, and failure condition details. The purpose of this method is to generate a graphical user interface (GUI) sub-panel for managing and displaying aircraft functions with all their effects, which can be used within the OSIRIS plugin for Cameo Systems Modeler in performing Functional Hazard Assessments (FHA).
 * @summary The method `For_AC_Functions_with_All_effects_ResponseSubPannel` is responsible for initializing and configuring a GUI subpanel for aircraft functions with all effects, incorporating failure conditions, descriptions, and their respective impacts on the aircraft, crew, and occupants.
 */
	public For_AC_Functions_with_All_effects_ResponseSubPannel(int i, Element selectedNode, String fcId, Map<String, List<String>> fcDetails) {

		if (selectedNode == null) {
			throw new IllegalArgumentException("selectedNode cannot be null.");
		}

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

		setSize(new Dimension(805, 260));
		setPreferredSize(new Dimension(770, 350));
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		List<String> name = fcDetails.getOrDefault("FC Name", new ArrayList<>());
		if(name.isEmpty()) {
			name = fcDetails.getOrDefault("Failure Condition", new ArrayList<>());
		}
		List<String> description = fcDetails.getOrDefault("FC Description", new ArrayList<>());
		List<String> effectAC = fcDetails.getOrDefault("Effect On Aircraft", new ArrayList<>());
		List<String> effectCW = fcDetails.getOrDefault("Effect On Crew", new ArrayList<>());
		List<String> effectOCC = fcDetails.getOrDefault("Effect On Occupants", new ArrayList<>());

		List<String> failureTypes = fcDetails.get("Failure Type");
		List<String> phases = fcDetails.get("Phase");
		List<String> Hazards = fcDetails.get("Hazard Classification");

		var allFailures = elementfinder.FindAllFailure();
		if (allFailures == null) allFailures = new ArrayList<>();
		//		allFailures.addAll(MalfunctionList);
		var allFailureNames = elementfinder.FindAllFailureNames();
		if (allFailureNames == null) allFailureNames = new ArrayList<>();

		var allphs = elementfinder.FindAllPhases();
		if (allphs == null) allphs = new ArrayList<>();
		var allphNames = elementfinder.FindAllPhaseNames();
		if (allphNames == null) allphNames = new ArrayList<>();

		var FTs = elementfinder.getElementsByNames(failureTypes, allFailures, allFailureNames);
		var PHs = elementfinder.getElementsByNames(phases, allphs, allphNames);
		List<Element> Fts_Mal = new ArrayList<>();
		if (MalfunctionList!=null) {
			Fts_Mal=elementfinder.getElementsByNames(failureTypes,MalfunctionList);
		}
		FTs.addAll(Fts_Mal);

		var allHCs = elementfinder.FindAllHazard();
		if (allHCs == null) allHCs = new ArrayList<>();
		var allHCsNames = elementfinder.FindAllHazardNames();
		if (allHCsNames == null) allHCsNames = new ArrayList<>();

		var HCs = elementfinder.getElementsByNames(Hazards, allHCs, allHCsNames);

		//
		//		var FTs = elementfinder.getElementsByNames(failureTypes, elementfinder.FindAllFailure(), elementfinder.FindAllFailureNames());
		//		var PHs = elementfinder.getElementsByNames(phases, elementfinder.FindAllPhases(), elementfinder.FindAllPhaseNames());
		//		var HCs = elementfinder.getElementsByNames(Hazards, elementfinder.FindAllHazard(), elementfinder.FindAllHazardNames());

		JPanel Sup_panel = new JPanel();
		JPanel Selectpanel = new JPanel();
		Selectpanel.setPreferredSize(new Dimension(10, 150));
		Selectpanel.setAlignmentY(Component.TOP_ALIGNMENT);
		Selectpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		Selectpanel.setLayout(new GridLayout(1, 3));

		JPanel FT_panel = new JPanel();
		FT_panel =AddCheckboxs(elementfinder.FindAllFailure(), FTs,MalfunctionList, "Failure Type");
		Selectpanel.putClientProperty("Failure Type", FT_panel);
		JPanel PH_panel = new JPanel();
		PH_panel =AddCheckboxs(elementfinder.FindAllPhases(), PHs, "Phase");
		Selectpanel.putClientProperty("Phase", PH_panel);
		JPanel HC_panel = new JPanel();
		HC_panel =AddCheckboxs(elementfinder.FindAllHazard(), HCs, "Hazard Classification");
		Selectpanel.putClientProperty("Hazard Classification", HC_panel);



		JPanel Main_panel = new JPanel();
		Main_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		Main_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		var Name_subpannel = CreateSubpanneldes("Name", name);
		Main_panel.putClientProperty("Name", Name_subpannel.getClientProperty("Name"));

		var des_subpannel = CreateSubpanneldes("Description", description);
		Main_panel.putClientProperty("Description", des_subpannel.getClientProperty("Description"));

		var EffectAC_subpannel = CreateSubpanneldes("Effect On Aircraft", effectAC);
		Main_panel.putClientProperty("Effect On Aircraft", EffectAC_subpannel.getClientProperty("Effect On Aircraft"));

		var EffectCW_subpannel = CreateSubpanneldes("Effect On Crew", effectCW);
		Main_panel.putClientProperty("Effect On Crew", EffectCW_subpannel.getClientProperty("Effect On Crew"));

		var EffectOCC_subpannel = CreateSubpanneldes("Effect On Occupants", effectOCC);
		Main_panel.putClientProperty("Effect On Occupants", EffectOCC_subpannel.getClientProperty("Effect On Occupants"));

		JCheckBox chckbx_FC = new JCheckBox("Failure Condition "+i);
		chckbx_FC.setBorder(new LineBorder(new Color(0, 0, 0)));
		chckbx_FC.setSelected(true);
		chckbx_FC.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel ID_panel_1 = new JPanel();
		ID_panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		ID_panel_1.setBackground(Color.WHITE);
		ID_panel_1.setAlignmentY(0.0f);
		ID_panel_1.setAlignmentX(0.0f);

		JLabel lblId_1 = new JLabel("ID:");
		lblId_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblId_1.setBackground(Color.WHITE);

		JTextField textField = new JTextField(fcId);
		textField.setBorder(null);

		ID_panel_1.putClientProperty("ID", textField);

		GroupLayout gl_ID_panel_1 = new GroupLayout(ID_panel_1);
		gl_ID_panel_1.setHorizontalGroup(
				gl_ID_panel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 780, Short.MAX_VALUE)
				.addGroup(gl_ID_panel_1.createSequentialGroup()
						.addComponent(lblId_1)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_ID_panel_1.setVerticalGroup(
				gl_ID_panel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 28, Short.MAX_VALUE)
				.addGroup(gl_ID_panel_1.createSequentialGroup()
						.addGroup(gl_ID_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_ID_panel_1.createSequentialGroup()
										.addGap(3)
										.addComponent(lblId_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(textField, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addGap(1))
				);
		ID_panel_1.setLayout(gl_ID_panel_1);


		Selectpanel.add(PH_panel);
		Selectpanel.add(FT_panel);
		Selectpanel.add(HC_panel);

		Main_panel.setLayout(new BoxLayout(Main_panel, BoxLayout.PAGE_AXIS));
		Main_panel.add(Name_subpannel);
		Main_panel.add(des_subpannel);
		Main_panel.add(EffectAC_subpannel);
		Main_panel.add(EffectCW_subpannel);
		Main_panel.add(EffectOCC_subpannel);

		checkboxDesPanelMap.put(chckbx_FC, des_subpannel);
		checkboxEffACPanelMap.put(chckbx_FC, EffectAC_subpannel);
		checkboxEffCWPanelMap.put(chckbx_FC, EffectCW_subpannel);
		checkboxEffOccPanelMap.put(chckbx_FC, EffectOCC_subpannel);
		checkboxNamePanelMap.put(chckbx_FC, Name_subpannel);
		checkboxIDPanelMap.put(chckbx_FC, ID_panel_1);
		checkboxElementPanelMap.put(chckbx_FC, Selectpanel);


		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addGap(9)
										.addComponent(Sup_panel, GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE))
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addContainerGap()
										.addComponent(chckbx_FC, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(ID_panel_1, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap())
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(5)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(chckbx_FC, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(ID_panel_1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(Sup_panel, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
						.addContainerGap())
				);
		GroupLayout gl_Sup_panel = new GroupLayout(Sup_panel);
		gl_Sup_panel.setHorizontalGroup(
				gl_Sup_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Sup_panel.createSequentialGroup()
						.addGroup(gl_Sup_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(Main_panel, GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
								.addComponent(Selectpanel, GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE))
						.addGap(0))
				);
		gl_Sup_panel.setVerticalGroup(
				gl_Sup_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Sup_panel.createSequentialGroup()
						.addComponent(Main_panel, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(Selectpanel, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
				);
		Sup_panel.setLayout(gl_Sup_panel);
		setLayout(groupLayout);

	}

/**
 * @param allElements The `allElements` parameter is a List of Element objects that represent all potential elements available for selection in the checkbox panel.
 * @param SelectedList SelectedList represents a subset of elements that have been selected by the user from the overall list of malfunctions and unique elements.
 * @param MalfunctionList The `MalfunctionList` parameter of type `List<Element>` represents a collection of malfunction elements that will be included in the list of all items for checkbox creation.
 * @param Title The `Title` parameter specifies the header text for the checkbox panel, indicating the type of elements being displayed.
 * @return The `AddCheckboxs` method generates a `JPanel` that contains a set of checkboxes, each corresponding to an element in the provided lists. The panel includes a header label with the specified title and is designed to be scrollable if there are many elements. If no elements are present, it displays a message indicating that no related elements are assigned.
 * @summary The `AddCheckboxs` method creates and returns a JPanel containing checkboxes for selecting elements from provided lists, organized with a header label and scrollable content.
 */
	private JPanel AddCheckboxs(List<Element> allElements, List<Element> SelectedList, List<Element> MalfunctionList,
			String Title) {
		List<String> stringList = Arrays.asList("Failure Type", "Hazard Classification");
		// Remove duplicates from allItems
		if (allElements == null) allElements = new ArrayList<>();
		Set<Element> uniqueElements = new LinkedHashSet<>(allElements);

		List<Element> allItems = new ArrayList<>(MalfunctionList);

		allItems.addAll(uniqueElements);
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
				if (SelectedList.contains(item)) {
					radioButton.setSelected(true);
				}
				radioButton.setBackground(Color.WHITE);

				radioButtonList.add(radioButton);
				if (stringList.contains(Title)) {
					buttonGroup.add(radioButton); // Add each radio button to the group
				}

				if (allElements.contains(item)) {
					itemPanel.add(radioButton);
					itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add space between label and radio button if needed
					itemPanel.add(itemLabel);
					itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add space between label and radio button if needed

					JLabel lblmidLabel = new JLabel("(default)");

					lblmidLabel.setForeground(Color.GRAY);
					lblmidLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
					itemPanel.add(lblmidLabel);
				}else {	
					// Add the label (with icon) and the radio button to the item panel
					itemPanel.add(radioButton);
					itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add space between label and radio button if needed
					itemPanel.add(itemLabel);
				}
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
		if(SelectedList.isEmpty()||SelectedList==null) {
			choosePanel.setBackground(Color.RED);
		}

		// Store the radio button list in client properties for later use
		choosePanel.putClientProperty("radioButtonList", radioButtonList);
		choosePanel.putClientProperty("NameList", NameList);

		return choosePanel;
	}

/**
 * @param allElements The parameter `allElements` is a list of `Element` objects representing all available elements that can be used to create checkboxes in the GUI.
 * @param SelectedList The `SelectedList` parameter represents a list of currently selected elements that should be pre-checked in the checkboxes.
 * @param Title The `Title` parameter specifies the heading text displayed at the top of the checkbox panel.
 * @return The `AddCheckboxs` method returns a `JPanel` that contains a set of checkboxes based on the provided list of elements. This panel includes a title label at the top and arranges the checkboxes vertically within a scrollable pane, ensuring all items are displayed even if they exceed the visible area. The panel is designed to be easily integrated into a larger user interface for selection purposes.
 * @summary The `AddCheckboxs` method creates and returns a JPanel containing checkboxes for elements, with an optional title and grouping functionality based on specific conditions.
 */
	public JPanel AddCheckboxs(List<Element> allElements, List<Element> SelectedList,String Title) {
		List<String> stringList = Arrays.asList("Failure Type", "Hazard Classification");
		// Remove duplicates from allItems
		if (allElements == null) allElements = new ArrayList<>();
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
				if (SelectedList.contains(item)) {
					radioButton.setSelected(true);
				}
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
		if(SelectedList.isEmpty()||SelectedList==null) {
			choosePanel.setBackground(Color.RED);
		}

		// Store the radio button list in client properties for later use
		choosePanel.putClientProperty("radioButtonList", radioButtonList);
		choosePanel.putClientProperty("NameList", NameList);

		return choosePanel;
	}

	// Method to extract the list of radio buttons from the panel
/**
 * @param scrollPane The `scrollPane` parameter is a `JPanel` that contains the list of radio buttons to be extracted.
 * @return The `extractRadioButtonList` method returns a list of `JCheckBox` objects. This list is retrieved from the client properties of a given `JPanel`, specifically looking for a property named `"radioButtonList"`. The method's purpose is to extract and return this predefined list of radio buttons that are associated with the provided scroll pane component, allowing further manipulation or display of these components in the user interface.
 * @summary The `extractRadioButtonList` method retrieves a list of JCheckBoxes associated with a given JPanel, stored under the client property key "radioButtonList".
 */
	@SuppressWarnings("unchecked")
	public static List<JCheckBox> extractRadioButtonList(JPanel scrollPane) {
		return (List<JCheckBox>) scrollPane.getClientProperty("radioButtonList");
	}

/**
 * @param panel The `panel` parameter is a reference to the JPanel from which the client property specified by `string` will be retrieved.
 * @param string The `string` parameter represents the key used to retrieve a list of strings from the client properties of the given JPanel.
 * @return The `extractList` method returns a list of strings retrieved from the client properties of a given JPanel object.
 * @summary The `extractList` method retrieves a list of strings from the client properties of a given JPanel using a specified key.
 */
	@SuppressWarnings("unchecked")
	public static List<String> extractList(JPanel panel, String string) {
		// TODO Auto-generated method stub
		return (List<String>) panel.getClientProperty(string);
	}

/**
 * @param radioButtonList The `radioButtonList` parameter represents a list of JCheckBox components used to determine which items are currently selected by the user.
 * @param stringList The `stringList` parameter of type `List<String>` in the method `getSelectedItem` represents a collection of strings that corresponds to each checkbox in the provided radio button list.
 * @return The `getSelectedItem` method returns a list of strings (`List<String>`) representing the elements from `stringList` that correspond to the selected checkboxes in `radioButtonList`. Each string in the returned list is associated with a checked JCheckBox.
 * @summary The `getSelectedItem` method returns a list of strings corresponding to the selected checkboxes in the provided list.
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
 * @param text The `text` parameter is a `String` that specifies the label to be displayed for the input field, defaulting to "Failure Condition Statement" if it is "Name".
 * @param name The `name` parameter is a list of strings representing the names or identifiers to be displayed in the text area.
 * @return The `CreateSubpanneldes` method constructs a `JPanel` containing a label and a text field. The label's text is based on the input parameter `text`, with special handling if `text` equals "Name". The text field displays concatenated values from the `name` list. The panel is styled with specific border, alignment, and background color settings, and uses GroupLayout for component arrangement.

Return value: A `JPanel` containing a label and a text field with concatenated values from the input list, formatted according to specified styling and layout rules.
 * @summary Creates and configures a JPanel containing a label and text field for displaying failure condition statements.
 */
	private JPanel CreateSubpanneldes(String text, List<String> name) {
		String text1=text;

		if(text =="Name") {
			text= "Failure Condition Statement";
		}

		JPanel subMain_panel_1 = new JPanel();
		subMain_panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		subMain_panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		subMain_panel_1.setBackground(Color.WHITE);
		subMain_panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblId_1 = new JLabel(text + ":");
		lblId_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblId_1.setBackground(Color.WHITE);

		StringBuilder StringBuilder = new StringBuilder();
		for(String sys : name) {
			StringBuilder.append(sys+"");
		}

		JTextField textArea_1 = new JTextField(StringBuilder.toString());
		textArea_1.setBorder(null);

		subMain_panel_1.putClientProperty(text1, textArea_1);

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

	// Method to return the map of checkboxes and their associated main panels
/**
 * @return This method returns a map that associates each `JCheckBox` with its corresponding `JPanel`. This mapping allows for easy retrieval of the panel associated with any given checkbox.
 * @summary The `getcheckboxNamePanelMap` method returns the map that associates JCheckBox components with their corresponding JPanel containers.
 */
	public Map<JCheckBox, JPanel> getcheckboxNamePanelMap() {
		return checkboxNamePanelMap;
	}	
/**
 * @return The return value of the `getcheckboxDesPanelMap` method is a map that associates each JCheckBox with its corresponding JPanel. This mapping facilitates the retrieval and management of user interface components related to safety check boxes in OSIRIS, ensuring an organized structure for functional hazard assessment tasks within the application.
 * @summary The `getcheckboxDesPanelMap` method returns the current mapping of check boxes to their associated description panels.
 */
	public Map<JCheckBox, JPanel> getcheckboxDesPanelMap() {
		return checkboxDesPanelMap;
	}	
/**
 * @return The `getCheckboxEffAC` method returns a map that associates each checkbox (JCheckBox) with its corresponding panel (JPanel). This map is used to manage the relationship between user interface components in the Functional Hazard Assessment tool.
 * @summary The `getCheckboxEffAC` method returns a mapping of JCheckBox objects to their corresponding JPanel components.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffAC() {
		return checkboxEffACPanelMap;
	}	
/**
 * @return The `getcheckboxEffCW` method returns a map that associates each JCheckBox with its corresponding JPanel. This allows for the retrieval of the graphical components that are used to display and interact with effective check boxes within the user interface, facilitating the management and manipulation of these components programmatically.
 * @summary The `getcheckboxEffCW` method returns a map that associates `JCheckBox` components with their corresponding `JPanel` containers.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffCW() {
		return checkboxEffCWPanelMap;
	}	
/**
 * @return The `getCheckboxEffOCC` method returns a map that associates each `JCheckBox` with its corresponding `JPanel`. This map represents the relationship between check boxes and their associated panels within the user interface, allowing for easy retrieval of the panel associated with any given check box.
 * @summary The method `getcheckboxEffOCC` returns a map that associates JCheckBox components with their corresponding JPanel containers.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffOCC() {
		return checkboxEffOccPanelMap;
	}

/**
 * @return The return value of the `getCheckboxElementPanelMap` method is a `Map` that associates each `JCheckBox` with its corresponding `JPanel`. This map is used to manage and retrieve the panels associated with specific checkboxes, facilitating the organization and retrieval of UI elements in the OSIRIS plugin.
 * @summary This method returns a map that associates JCheckBox objects with their corresponding JPanel elements.
 */
	public Map<JCheckBox, JPanel> getCheckboxElementPanelMap() {
		// TODO Auto-generated method stub
		return checkboxElementPanelMap;
	}
/**
 * @return The return value of `getCheckboxIDtPanelMap` is a map that associates each `JCheckBox` with its corresponding `JPanel`. This allows for efficient lookup of the panel associated with a particular checkbox.
 * @summary This method retrieves the map that associates `JCheckBox` components with their corresponding `JPanel` containers.
 */
	public Map<JCheckBox, JPanel> getCheckboxIDtPanelMap() {
		// TODO Auto-generated method stub
		return checkboxIDPanelMap;
	}


/**
 * @param clientProperty `clientProperty`: Represents a graphical panel object that contains the list of items to be processed for selection.
 * @param Type The `Type` parameter specifies the category or classification of items to be considered when selecting elements.
 * @return The `getSelected` method returns a list of strings representing the names of selected items. These are derived from checkboxes within a specified JPanel and their corresponding names extracted from the panel.
 * @summary The `getSelected` method retrieves a list of selected items' names from a JPanel based on the state of checkboxes and predefined names.
 */
	public static List<String> getSelected(Object clientProperty, String Type) {
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
