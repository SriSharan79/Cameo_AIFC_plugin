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
 * @summary The `For_Sys_Func_with_HLS_ResponseSubPannel` class is responsible for managing and rendering sub-panels with checkboxes for functional system components in the OSIRIS plugin, facilitating user interaction with failure condition details.
 */
public class For_Sys_Func_with_HLS_ResponseSubPannel extends JPanel {  

	private static final long serialVersionUID = 1L;
	public Map<JCheckBox, JPanel> checkboxNamePanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxElementPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxIDPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxDesPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffACPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffCWPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffOccPanelMap = new HashMap<>();
	public Map<JCheckBox, JPanel> checkboxEffHLSPanelMap = new HashMap<>();

	ElementFinder_SVP elementfinder =new ElementFinder_SVP();
	NameDerivator ND = new NameDerivator();
	StereotypeSearch STC =new StereotypeSearch();

/**
 * @param i The parameter `i` of type `int` in the method `For_Sys_Func_with_HLS_ResponseSubPannel` represents an identifier used to label and differentiate multiple failure conditions within the UI.
 * @param selectedNode The `selectedNode` parameter in the method `For_Sys_Func_with_HLS_ResponseSubPannel` is a SysML model element that represents either a `CallBehaviorAction` or an `Activity`, used to determine the properties and generate the corresponding subpanel for functional hazard assessment.
 * @param fcId The `fcId` parameter in the method `For_Sys_Func_with_HLS_ResponseSubPannel` is a unique identifier string for the failure condition being processed.
 * @param fcDetails The `fcDetails` parameter is a map that contains various attributes and their corresponding values related to failure conditions.
 * @return The return value of the `For_Sys_Func_with_HLS_ResponseSubPannel` method is an instance of the `For_Sys_Func_with_HLS_ResponseSubPannel` class. This object represents a graphical user interface panel used for configuring and displaying failure conditions within a SysML model, specifically for a system function with higher-level system response details. The panel includes various sub-components such as checkboxes, text fields, and labels that allow users to define and view failure condition properties like name, description, effects on different levels (higher level systems, aircraft, crew, occupants), and hazard classifications.
 * @summary The `For_Sys_Func_with_HLS_ResponseSubPannel` method is responsible for creating and configuring a graphical user interface (GUI) panel that displays detailed information about failure conditions associated with a selected system function or activity in the OSIRIS tool, utilizing SysML elements and properties to populate and organize the UI components.
 */
	public For_Sys_Func_with_HLS_ResponseSubPannel(int i, Element selectedNode, String fcId, Map<String, List<String>> fcDetails) {

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
		setPreferredSize(new Dimension(770, 390));
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		List<String> name = fcDetails.getOrDefault("FC Name", new ArrayList<>());
		if(name.isEmpty()) {
			name = fcDetails.getOrDefault("Failure Condition", new ArrayList<>());
		}
		List<String> description = fcDetails.getOrDefault("FC Description", new ArrayList<>());
		List<String> effectHLS = fcDetails.getOrDefault("Effect On Higher Level", new ArrayList<>());
		List<String> effectAC = fcDetails.getOrDefault("Effect On Aircraft", new ArrayList<>());
		List<String> effectCW = fcDetails.getOrDefault("Effect On Crew", new ArrayList<>());
		List<String> effectOCC = fcDetails.getOrDefault("Effect On Occupants", new ArrayList<>());

		List<String> failureTypes = fcDetails.get("Failure Type");
		List<String> phases = fcDetails.get("Phase");
		List<String> Hazards = fcDetails.get("Hazard Classification");

		var allFailures = elementfinder.FindAllFailure();
		if (allFailures == null) allFailures = new ArrayList<>();
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


		var EffectHLS_subpannel = CreateSubpanneldes("Effect on Higher Level System", effectHLS);
		Main_panel.putClientProperty("Effect on Higher Level System", EffectHLS_subpannel.getClientProperty("Effect on Higher Level System"));

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
		Main_panel.add(EffectHLS_subpannel);
		Main_panel.add(EffectAC_subpannel);
		Main_panel.add(EffectCW_subpannel);
		Main_panel.add(EffectOCC_subpannel);

		checkboxDesPanelMap.put(chckbx_FC, des_subpannel);
		checkboxEffACPanelMap.put(chckbx_FC, EffectAC_subpannel);
		checkboxEffCWPanelMap.put(chckbx_FC, EffectCW_subpannel);
		checkboxEffOccPanelMap.put(chckbx_FC, EffectOCC_subpannel);
		checkboxEffHLSPanelMap.put(chckbx_FC, EffectHLS_subpannel);
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
						.addComponent(Sup_panel, GroupLayout.PREFERRED_SIZE, 322, Short.MAX_VALUE)
						.addContainerGap())
				);
		GroupLayout gl_Sup_panel = new GroupLayout(Sup_panel);
		gl_Sup_panel.setHorizontalGroup(
				gl_Sup_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Sup_panel.createSequentialGroup()
						.addGroup(gl_Sup_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(Selectpanel, GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
								.addComponent(Main_panel, GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE))
						.addGap(0))
				);
		gl_Sup_panel.setVerticalGroup(
				gl_Sup_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Sup_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(Main_panel, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(Selectpanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(3))
				);
		Sup_panel.setLayout(gl_Sup_panel);
		setLayout(groupLayout);

	}

/**
 * @param allElements A concise description: The `allElements` parameter is a list of all relevant elements that need to be processed and potentially displayed as checkboxes in the generated panel.
 * @param SelectedList A concise, single-sentence description for the Java parameter `SelectedList` of type `List<Element>` in method `AddCheckboxs`:

The `SelectedList` parameter is a list of elements that have been selected and need to be marked as checked in the generated checkboxes.
 * @param MalfunctionList The `MalfunctionList` parameter is a collection of elements representing malfunction conditions to be included and displayed in the checkbox interface.
 * @param Title The `Title` parameter specifies the header text to be displayed above the panel containing checkboxes, providing a context or category for the items listed.
 * @return The `AddCheckboxs` method returns a `JPanel` that serves as the main container for displaying a list of elements, each associated with a checkbox. This panel includes a header label and uses a scroll pane to manage the layout of the checkboxes, ensuring that all items can be viewed even if they exceed the visible area. The checkboxes are dynamically generated based on input lists, and their selection states reflect predefined conditions from provided data structures.
 * @summary The `AddCheckboxs` method creates and returns a JPanel containing checkboxes (or radio buttons) for a list of elements, with optional grouping and selection highlighting.
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
 * @param allElements The parameter `allElements` represents a list of SysML elements that are candidates for checkbox creation.
 * @param SelectedList The `SelectedList` parameter represents a list of elements that are pre-selected and should be marked as checked in the generated checkbox panel.
 * @param Title The `Title` parameter specifies the header text to be displayed at the top of the checkbox panel.
 * @return The `AddCheckboxs` method returns a `JPanel` containing a list of checkboxes (or radio buttons if the title matches specific criteria) for the provided elements. The panel includes a header label with a specified title and is designed to be scrollable, making it suitable for displaying multiple items in a compact space. This returned panel can be directly added to a user interface for interaction.
 * @summary The method `AddCheckboxs` creates and returns a JPanel containing checkboxes (or radio buttons) for a list of elements, with an optional title and selection handling.
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
 * @param scrollPane The `scrollPane` parameter is a JPanel that contains the list of JCheckBox components as a client property, which is extracted and returned by this method.
 * @return The `extractRadioButtonList` method returns a list of `JCheckBox` components that are stored as client properties within the provided `scrollPane`. This list represents the radio buttons used for user interaction.
 * @summary The `extractRadioButtonList` method retrieves a list of radio buttons stored as a client property in the provided scroll pane.
 */
	@SuppressWarnings("unchecked")
	public static List<JCheckBox> extractRadioButtonList(JPanel scrollPane) {
		return (List<JCheckBox>) scrollPane.getClientProperty("radioButtonList");
	}

/**
 * @param panel The `panel` parameter is a reference to the JPanel object from which the client property specified by the `string` parameter will be retrieved.
 * @param string The `string` parameter is a key used to retrieve a list of strings from the client properties of the provided `JPanel`.
 * @return The `extractList` method returns a list of strings extracted from the client properties of a given JPanel object. The purpose of this method is to retrieve and return a specific list associated with a provided key, which identifies the desired data within the panel's client properties.
 * @summary The `extractList` method retrieves a list of strings from the client properties of a given JPanel based on a specified key.
 */
	@SuppressWarnings("unchecked")
	public static List<String> extractList(JPanel panel, String string) {
		// TODO Auto-generated method stub
		return (List<String>) panel.getClientProperty(string);
	}

/**
 * @param radioButtonList `radioButtonList`: A list of JCheckBox components representing radio buttons used for user selection input.
 * @param stringList The `stringList` parameter contains the text labels associated with each radio button in `radioButtonList`, used to populate the result when a radio button is selected.
 * @return The `getSelectedItem` method returns a list of strings (`List<String>`) representing the elements from `stringList` that correspond to the checked checkboxes in `radioButtonList`.
 * @summary The `getSelectedItem` method extracts and returns a list of strings corresponding to the selected checkboxes in a given list.
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
 * @param text The `text` parameter is a label that will be displayed on the panel to identify the associated text field.
 * @param name The `name` parameter is a list of strings representing individual components or items to be displayed in the text field within the created subpanel.
 * @return The `CreateSubpanneldes` method returns a `JPanel` that serves as a customized input panel for the OSIRIS plugin. This panel includes a label and a text field, both of which are dynamically generated based on the provided parameters. The label displays a specific heading (which can be modified from "Name" to "Failure Condition Statement"), while the text field contains concatenated values from a list of strings. This `JPanel` is designed for integrating into larger user interfaces within OSIRIS, facilitating interactive input for failure condition statements during Functional Hazard Assessment.
 * @summary The `CreateSubpanneldes` method constructs and returns a `JPanel` that contains a label and text field, with the label text and content derived from the input parameters.
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
 * @return The return value of the `getCheckboxNamePanelMap` method is a map that associates each JCheckBox with its corresponding JPanel. This map provides a structured way to retrieve the associated JPanel for any given JCheckBox within the current context.
 * @summary The method `getcheckboxNamePanelMap` retrieves the map that associates JCheckBox components with their corresponding JPanel containers.
 */
	public Map<JCheckBox, JPanel> getcheckboxNamePanelMap() {
		return checkboxNamePanelMap;
	}

/**
 * @return The return value of `getCheckboxDesPanelMap` is a mapping that associates each JCheckBox with its corresponding JPanel. This map is used to manage and retrieve the relationship between check boxes and their associated panels within the user interface.
 * @summary The `getcheckboxDesPanelMap` method retrieves a mapping of checkboxes to their corresponding description panels.
 */
	public Map<JCheckBox, JPanel> getcheckboxDesPanelMap() {
		return checkboxDesPanelMap;
	}

/**
 * @return The `getcheckboxEffAC` method returns a map where each key is a JCheckBox and the corresponding value is a JPanel. This map represents the association between checkboxes and their respective panels in the user interface, facilitating easy access and manipulation of these UI components for further processing or display purposes.
 * @summary The `getcheckboxEffAC` method returns the mapping of JCheckBox components to their corresponding JPanel components.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffAC() {
		return checkboxEffACPanelMap;
	}

/**
 * @return The `getcheckboxEffCW` method returns a map that associates each `JCheckBox` with its corresponding `JPanel`. This map represents the mapping of effect check boxes to their respective panels, facilitating the organization and retrieval of these GUI components.
 * @summary This method retrieves the map of JCheckBox components and their associated JPanel containers used in the effectiveness configuration window.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffCW() {
		return checkboxEffCWPanelMap;
	}

/**
 * @return The `getcheckboxEffOCC` method returns a map that associates each `JCheckBox` with its corresponding `JPanel`. This map represents the user interface components related to the effective OCC (Operator Control Center) settings.
 * @summary The `getcheckboxEffOCC` method returns the map of JCheckBoxes and their associated panels for effect on OCC.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffOCC() {
		return checkboxEffOccPanelMap;
	}

/**
 * @return The `getcheckboxEffHLS` method returns a map that associates each JCheckBox component with its corresponding JPanel. This mapping is used to organize and manage the graphical user interface elements related to failure condition checks within OSIRIS for better GUI management in aircraft system safety analysis tasks.
 * @summary The `getCheckboxEffHLS` method returns the map of check boxes and their associated panels used for effective Hazard Level Sizes in OSIRIS.
 */
	public Map<JCheckBox, JPanel> getcheckboxEffHLS() {
		return checkboxEffHLSPanelMap;
	}

/**
 * @return The `getCheckboxElementPanelMap` method returns a map that associates each JCheckBox with its corresponding JPanel. This map is used to manage the relationship between checkboxes and their associated panels in the user interface, allowing for efficient retrieval and manipulation of these components.
 * @summary This method returns the map that associates each JCheckBox with its corresponding JPanel element.
 */
	public Map<JCheckBox, JPanel> getCheckboxElementPanelMap() {
		// TODO Auto-generated method stub
		return checkboxElementPanelMap;
	}
/**
 * @return The `getCheckboxIDtPanelMap` method returns a map that associates each `JCheckBox` with its corresponding `JPanel`. This map is used to manage the relationship between check boxes and their associated panels, enabling efficient access and manipulation of these UI components.
 * @summary This method retrieves the mapping of checkboxes to their corresponding panels.
 */
	public Map<JCheckBox, JPanel> getCheckboxIDtPanelMap() {
		// TODO Auto-generated method stub
		return checkboxIDPanelMap;
	}


/**
 * @param clientProperty The `clientProperty` parameter represents a UI component or object used to identify and extract relevant checkboxes for item selection.
 * @param Type The `Type` parameter specifies the type of data to be retrieved from the client property's components.
 * @return The `getSelected` method's return value is a list of strings that represents the names of the selected items from a JPanel. The method processes the given client property and type to identify and collect these item names based on user selections.
 * @summary The `getSelected` method retrieves a list of selected item names from a JPanel.
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
