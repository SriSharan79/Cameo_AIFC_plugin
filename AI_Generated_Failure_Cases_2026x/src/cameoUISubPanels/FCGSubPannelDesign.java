package cameoUISubPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.magicdraw.uml.ElementIcon;
import com.nomagic.ui.ResizableIcon;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Pin;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.StereotypeSearch;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-19
 * @summary The `FCGSubPannelDesign` class is responsible for managing and creating subpanels with checkboxes and radio buttons for user interaction within the OSIRIS Java plugin.
 */
public class FCGSubPannelDesign {

	NameDerivator ND = new NameDerivator();
	StereotypeSearch STC =new StereotypeSearch();

/**
 * @param panel The `JPanel` parameter named `panel` is used as a container to hold and organize the UI components related to displaying selected items and available checkboxes for selection.
 * @param allItems The `allItems` parameter is a list of elements representing all available items to be added as checkbox buttons.
 * @param SelectedList The `SelectedList` parameter is a collection of elements that are already assigned or selected, and this method uses it to populate the left column with these pre-selected items.
 * @return The `AddCheckboxButtons_old` method returns a `JPanel` that is structured to display two columns of checkboxes. The left column shows the currently selected items with their associated icons, and the right column presents all available items for selection, each accompanied by a checkbox. This setup allows users to manage and update selections efficiently within the UI.
 * @summary The `AddCheckboxButtons_old` method is responsible for creating and populating a JPanel with checkboxes, organizing them into two columns: one for selected items and another for all available items.
 */
	public JPanel AddCheckboxButtons_old(JPanel panel, List<Element> allItems, List<Element> SelectedList) {
		panel.setLayout(new GridLayout(1, 2)); // Divide the panel into two columns

		// Column header for selected items
		JLabel selectedHeader = new JLabel("Assigned:", JLabel.CENTER);
		selectedHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		selectedHeader.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		// Create a panel to hold selected items under the header
		JPanel selectedItemPanel = new JPanel();
		selectedItemPanel.setBackground(Color.WHITE);
		selectedItemPanel.setLayout(new BoxLayout(selectedItemPanel, BoxLayout.Y_AXIS));

		// Check if the list is null or empty
		if (SelectedList == null || SelectedList.isEmpty()) {
			JLabel noElementsLabel = new JLabel("<html><body style='width: 100px;'>No Related Elements are Assigned</body></html>");
			noElementsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			noElementsLabel.setForeground(Color.GRAY);
			noElementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			selectedItemPanel.add(noElementsLabel);

		} else {
			// Display selected items in the selected panel
			for (Object item : SelectedList) {
				JLabel itemLabel = new JLabel(ND.getName((Element) item));
				ResizableIcon Icon =ElementIcon.getIcon((BaseElement) item);
				itemLabel.setIcon(Icon);
				selectedItemPanel.add(itemLabel);
			}
		}

		// Add selectedItemPanel into a JScrollPane
		JScrollPane selectedScrollPane = new JScrollPane(selectedItemPanel);
		selectedScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		// Wrap both the selected header and scrollPane in a panel
		JPanel selectedPanel = new JPanel(new BorderLayout());
		selectedPanel.setBackground(Color.WHITE);
		selectedPanel.add(selectedHeader, BorderLayout.NORTH);  // Add header
		selectedPanel.add(selectedScrollPane, BorderLayout.CENTER); // Add scrollable content


		// Column header for radio buttons (all items)
		JLabel chooseHeader = new JLabel("Select:", JLabel.CENTER);
		chooseHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		chooseHeader.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		// Create a panel to hold radio buttons
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		radioButtonPanel.setBackground(Color.WHITE);

		List<JCheckBox> radioButtonList = new ArrayList<>();
		List<String> NameList = new ArrayList<>();
		//		ButtonGroup buttonGroup = new ButtonGroup(); // Group for radio buttons //// if you want to have multiple selection option delete or comment Button group

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
				//				buttonGroup.add(radioButton); // Add each radio button to the group

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

		// Add both selectedPanel and choosePanel to the main panel
		panel.add(selectedPanel);
		panel.add(choosePanel);

		// Store the radio button list in client properties for later use
		panel.putClientProperty("radioButtonList", radioButtonList);
		panel.putClientProperty("NameList", NameList);

		return panel;
	}

/**
 * @param allElements The `allElements` parameter is a list of elements that will be used to create checkboxes, representing the full set of options available for selection.
 * @param SelectedList `SelectedList` is a parameter of type `List<Element>` that specifies the initially selected elements to be highlighted in the checkbox buttons.
 * @param Title The `Title` parameter in the method `AddCheckboxButtons` represents a descriptive label or header that will be displayed above the panel containing checkbox buttons.
 * @return The `AddCheckboxButtons` method returns a `JPanel` containing a scrollable panel of checkbox buttons, each associated with an element from the input lists. The returned panel includes functionality to select or deselect all elements at once via a "Select All" checkbox.
 * @summary The `AddCheckboxButtons` method creates a JPanel containing checkboxes for a list of elements, allowing the user to select or deselect them individually.
 */
	public JPanel AddCheckboxButtons(List<Element> allElements, List<Element> SelectedList,String Title) {
		// Remove duplicates from allItems
		Set<Element> uniqueElements = new LinkedHashSet<>(allElements);
		List<Element> allItems = new ArrayList<>(uniqueElements);
		// Create a panel to hold radio buttons
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		radioButtonPanel.setBackground(Color.WHITE);

		List<JCheckBox> radioButtonList = new ArrayList<>();
		List<String> NameList = new ArrayList<>();


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
		JScrollPane radioScrollPane = new JScrollPane();
		radioScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		radioScrollPane.setViewportView(radioButtonPanel);

		// Create a "Select All" checkbox
		JCheckBox selectAllCheckBox = new JCheckBox("Select All");
		selectAllCheckBox.setBackground(Color.WHITE);
		selectAllCheckBox.setFont(new Font("Tahoma", Font.BOLD, 12));

		// Add action listener to "Select All" checkbox
		selectAllCheckBox.addActionListener(e -> {
			boolean isSelected = selectAllCheckBox.isSelected();
			for (JCheckBox checkBox : radioButtonList) {
				checkBox.setSelected(isSelected);
			}
		});

		// Create a panel for the header containing the "Select All" checkbox
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Color.WHITE);
		headerPanel.add(selectAllCheckBox, BorderLayout.WEST);
		headerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));

		// Set the header view of the scroll pane
		radioScrollPane.setColumnHeaderView(headerPanel);

		// Wrap both the choose header and scrollPane in a panel
		JPanel choosePanel = new JPanel(new BorderLayout());
		choosePanel.setBackground(Color.WHITE);
		choosePanel.add(radioScrollPane, BorderLayout.CENTER); // Add scrollable content

		// Store the radio button list in client properties for later use
		choosePanel.putClientProperty("radioButtonList", radioButtonList);
		choosePanel.putClientProperty("NameList", NameList);

		return choosePanel;
	}


	// Method to extract the list of radio buttons from the panel
/**
 * @param scrollPane The `scrollPane` parameter is a JPanel that contains the radio buttons to be extracted.
 * @return The `extractRadioButtonList` method returns a list of JCheckBox components that were previously stored as a client property in the given JPanel.
 * @summary The `extractRadioButtonList` method retrieves a list of radio buttons associated with a given JPanel.
 */
	@SuppressWarnings("unchecked")
	public List<JCheckBox> extractRadioButtonList(JPanel scrollPane) {
		return (List<JCheckBox>) scrollPane.getClientProperty("radioButtonList");
	}

/**
 * @param radioButtonList The `radioButtonList` parameter of type `List<JCheckBox>` represents a collection of JCheckBox components that are used to capture user selection preferences.
 * @param stringList The `stringList` parameter of type `List<String>` in the method `getSelectedItem` represents a list of strings, each associated with a corresponding checkbox in `radioButtonList`, from which selected items are extracted based on user selection.
 * @return The return value of `getSelectedItem` is a `List<String>` containing the elements from `stringList` that correspond to the selected checkboxes in `radioButtonList`.
 * @summary The `getSelectedItem` method collects and returns a list of strings corresponding to the selected checkboxes in a given list.
 */
	public List<String> getSelectedItem(List<JCheckBox> radioButtonList, List<String> stringList) {
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
 * @param panel The `JPanel` parameter named `panel` is the container in which system elements are dynamically added and displayed based on the provided list of systems.
 * @param systemList The `systemList` parameter is a list of Element objects representing aircraft systems, which are used to populate and display system information within the provided JPanel.
 * @return The `createPanelWithSystems` method returns a `JPanel` that has been dynamically populated with labels representing elements from a provided list of systems. If the list is empty, the returned panel contains a single label indicating no systems are allocated. The layout of the panel is set to be vertical, and the content is centered within the panel.
 * @summary This method populates a JPanel with labels representing systems from a given list, or displays an empty message if the list is empty.
 */
	public JPanel createPanelWithSystems(JPanel panel, List<Element> systemList) {
		panel.removeAll();  // Clear the panel if it has any existing components
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Set a vertical BoxLayout

		panel.add(Box.createVerticalGlue());  // Add vertical glue to push content down

		if (systemList.isEmpty()) {
			// If the list is empty, show a message
			JLabel emptyMessage = new JLabel("No system is allocated, please check");
			emptyMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
			emptyMessage.setForeground(Color.RED); // Optional: color the message text red
			emptyMessage.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align text

			// Add the empty message to the panel
			panel.add(emptyMessage);
		} else {
			// Add each object from systemList to the panel
			for (Object obj : systemList) {
				JLabel objLabel = new JLabel(ND.getName((Element) obj));
				objLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
				ResizableIcon Icon =ElementIcon.getIcon((BaseElement) obj);
				objLabel.setIcon(Icon);

				// Set alignment and size for dynamic resizing
				objLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align text
				objLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, objLabel.getPreferredSize().height));

				// Add the label to the panel
				panel.add(objLabel);
			}
		}

		panel.add(Box.createVerticalGlue());  // Add vertical glue at the bottom to center content

		panel.revalidate(); // Refresh the panel to reflect changes
		panel.repaint();    // Repaint the panel to ensure it's visually updated

		return panel; // Return the updated panel
	}

/**
 * @param checkBoxPanel A `JPanel` containing the dynamically created checkboxes for elements.
 * @param elementList The `elementList` parameter is a list of `Element` objects that represent the items for which checkboxes will be created and added to the `checkBoxPanel`.
 * @return The `AddCheckBoxes` method returns a `JPanel` that contains a set of JCheckBoxes, each associated with an element from the provided list. The returned JPanel is configured with a vertical box layout and includes client properties storing lists of JCheckBoxes, their names, and types for further use.
 * @summary The `AddCheckBoxes` method dynamically adds checkboxes to a given panel based on the elements provided, configuring and organizing them appropriately for display.
 */
	public JPanel AddCheckBoxes(JPanel checkBoxPanel, List<Element> elementList) {
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
		checkBoxPanel.setPreferredSize(new Dimension(300, 150));
		checkBoxPanel.setSize(new Dimension(300, 150));
		List<JCheckBox> checkBoxList = new ArrayList<>();
		List<String> NameList = new ArrayList<>();
		List<String> TypeList = new ArrayList<>();

		if (elementList == null || elementList.isEmpty()) {
			JLabel noElementsLabel = new JLabel("No Related Elements are Assigned");
			noElementsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			noElementsLabel.setForeground(Color.GRAY); 
			noElementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			checkBoxPanel.add(noElementsLabel);
		} else {
			for (Object item : elementList) {
				JPanel itemPanel = new JPanel();
				itemPanel.setBackground(Color.WHITE);
				itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.LINE_AXIS));

				JPanel itemsubPanel = CreateLabelPanel(item);
				String Name = ND.getName((Element) item);  // Be sure that item is always of type `Element`
				NameList.add(Name);
				String Type = GetType((Element) item);
				TypeList.add(Type);

				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected(true);
				checkBox.setBackground(Color.WHITE);

				itemPanel.add(checkBox);
				itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
				itemPanel.add(itemsubPanel);

				itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

				checkBoxList.add(checkBox);
				checkBoxPanel.add(itemPanel);
			}
		}

		checkBoxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		checkBoxPanel.putClientProperty("checkBoxList", checkBoxList);
		checkBoxPanel.putClientProperty("NameList", NameList);
		checkBoxPanel.putClientProperty("TypeList", TypeList);

		return checkBoxPanel;
	}



/**
 * @param item The `item` parameter is an Element instance that represents a SysML model element whose type or description needs to be determined.
 * @return The `GetType` method returns a `String` that represents the type of an element in a SysML model. If the element is a `Parameter` or `Pin`, it retrieves the type name, otherwise, if the element has a requirement stereotype, it fetches the description text. For any other case, it defaults to "No value assigned".
 * @summary The `GetType` method is responsible for retrieving and returning the type or description of a given SysML element, handling cases where the element is a parameter, pin, or has a requirement stereotype.
 */
	private String GetType(Element item) {
		if (item instanceof Parameter || item instanceof Pin) {
			TypedElement typedItem = (TypedElement) item;  // Avoid casting multiple times
			return (typedItem.getType() != null) ? ND.getTypeName(typedItem) : "No type assigned";
		} else if (STC.hasRequirementStereotype(item)) {
			Stereotype req = STC.getMatchedRequirementStereotype(item);
			var prop = StereotypesHelper.getPropertyByName(req, "Text");

			for (Object value : StereotypesHelper.getStereotypePropertyValue(item, prop)) {
				if (value instanceof String && !((String) value).isEmpty()) {
					return (String) value;
				}
			}
			return "No Description for the requirement";
		}
		return "No value assigned";
	}


/**
 * @param item The `item` parameter in the `CreateLabelPanel` method represents an element (such as a Parameter or Pin) whose information will be used to create and configure a JPanel with labels.
 * @return The `CreateLabelPanel` method constructs a graphical representation of an object (such as a Parameter, Pin, or Requirement) within a JPanel. The return value is this customized JPanel which includes labels and icons that visually represent the object's name, type, and any relevant descriptions or requirements text.
 * @summary The `CreateLabelPanel` method is responsible for generating a JPanel that displays the name and type information of a given SysML model element or its associated requirement.
 */
	private JPanel CreateLabelPanel(Object item) {
		JPanel itemPanel = new JPanel();

		if (item instanceof Parameter || item instanceof Pin) {
			itemPanel.setBackground(Color.WHITE);
			itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.LINE_AXIS));
			JLabel NameLabel = new JLabel(ND.getName((Element) item));
			ResizableIcon icon = ElementIcon.getIcon((BaseElement) item);
			NameLabel.setIcon(icon);

			if (((TypedElement) item).getType() != null) {
				JLabel lblmidLabel = new JLabel("type of");
				lblmidLabel.setFont(new Font("Tahoma", Font.BOLD, 8));
				JLabel typeLabel = new JLabel(ND.getTypeName((Element) item));
				ResizableIcon typeIcon = ElementIcon.getIcon(((TypedElement) item).getType());
				typeLabel.setIcon(typeIcon);

				itemPanel.add(NameLabel);
				itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
				itemPanel.add(lblmidLabel);
				itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
				itemPanel.add(typeLabel);
			} else {
				JLabel noTypeLabel = new JLabel("No type assigned");
				noTypeLabel.setForeground(Color.RED);
				noTypeLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
				itemPanel.add(NameLabel);
				itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
				itemPanel.add(noTypeLabel);
			}
		} else if (STC.hasRequirementStereotype((Element) item)) {
			itemPanel.setBackground(Color.WHITE);
			itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
			JLabel NameLabel = new JLabel(ND.getName((Element) item) + ":");
			ResizableIcon icon = ElementIcon.getIcon((BaseElement) item);
			NameLabel.setIcon(icon);

			Stereotype req = STC.getMatchedRequirementStereotype((Element) item);
			var prop = StereotypesHelper.getPropertyByName(req, "Text");

			for (Object value : StereotypesHelper.getStereotypePropertyValue((Element) item, prop)) {
				if (value instanceof String && !((String) value).isEmpty()) {
					JLabel typeLabel = new JLabel((String) value);
					itemPanel.add(NameLabel);
					itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
					itemPanel.add(typeLabel);
				} else {
					JLabel noDescriptionLabel = new JLabel("No Description for the requirement");
					itemPanel.add(NameLabel);
					itemPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
					itemPanel.add(noDescriptionLabel);
				}
			}
		}

		return itemPanel;
	}


	// Method to extract the list of checkboxes from the panel
/**
 * @param panel The `panel` parameter is a `JPanel` containing a list of `JCheckBox` components stored as a client property.
 * @return The return value of the `extractCheckBoxList` method is a list of `JCheckBox` components extracted from the specified `JPanel`. This list represents all checkboxes that have been previously stored in the client properties of the given panel.
 * @summary The `extractCheckBoxList` method retrieves a list of JCheckBox components from the given JPanel using its client properties.
 */
	@SuppressWarnings("unchecked")
	public List<JCheckBox> extractCheckBoxList(JPanel panel) {
		return (List<JCheckBox>) panel.getClientProperty("checkBoxList");
	}

/**
 * @param checkBoxList The `checkBoxList` parameter represents a collection of `JCheckBox` components, which are used to track and retrieve the selection state for each item in the associated `stringList`.
 * @param stringList The `stringList` parameter of type `List<String>` in the method `getSelectedItems` contains the labels or values that correspond to each checkbox in the `checkBoxList`.
 * @return The `getSelectedItems` method returns a `List<String>` containing the strings corresponding to the checked checkboxes. This list represents the items that have been selected by the user from the provided string list based on the checkbox selection status.
 * @summary The `getSelectedItems` method extracts and returns a list of strings corresponding to selected checkboxes.
 */
	public List<String> getSelectedItems(List<JCheckBox> checkBoxList, List<String> stringList) {
		List<String> selectedItems = new ArrayList<>();

		// Iterate over the checkboxes with their index
		for (int i = 0; i < checkBoxList.size(); i++) {
			JCheckBox checkBox = checkBoxList.get(i);

			// Check if the checkbox is selected
			if (checkBox.isSelected()) {
				// Add the corresponding element from the string list
				selectedItems.add(stringList.get(i));
			}
		}

		return selectedItems;
	}

/**
 * @param panel The `panel` parameter is a `JPanel` instance containing client properties used to retrieve the list of strings.
 * @param string The `string` parameter in the `extractList` method is a key used to retrieve a list of strings stored as a client property on the given `JPanel`.
 * @return The `extractList` method returns a list of strings. The purpose of the method is to retrieve a previously stored list of strings from a given JPanel object, using a specific key (string) as an identifier.
 * @summary The `extractList` method retrieves a list of strings from the client properties of a given JPanel using a specified key.
 */
	@SuppressWarnings("unchecked")
	public List<String> extractList(JPanel panel, String string) {
		// TODO Auto-generated method stub
		return (List<String>) panel.getClientProperty(string);
	}

/**
 * @param allDefaultElements The parameter `allDefaultElements` is a list of SysML elements that serve as the default selection items for checkboxes in the generated UI panel.
 * @param failureTypeList The `failureTypeList` parameter is a collection of failure type elements used to determine the pre-selected state of checkboxes in the generated GUI panel.
 * @param MalfunctionList The `MalfunctionList` parameter is a list of elements representing predefined malfunctions that will be included in the checklist alongside default elements and failure types.
 * @param string The `string` parameter is a placeholder for additional contextual information or settings that may influence the creation and layout of checkboxes within the generated panel.
 * @return The method `AddCheckboxButtons` constructs and returns a JPanel that contains checkboxes, each associated with an element from the provided lists. The returned panel includes a scrollable area for the checkboxes and a "Select All" checkbox at the top, allowing users to select or deselect all elements simultaneously.
 * @summary The `AddCheckboxButtons` method is responsible for creating and returning a JPanel containing checkboxes based on the provided lists of elements, with options to select all items.
 */
	public JPanel AddCheckboxButtons(List<Element> allDefaultElements, List<Element> failureTypeList,
			List<Element> MalfunctionList, String string) {
		Set<Element> uniqueElements = new LinkedHashSet<>(allDefaultElements);
		List<Element> allItems = new ArrayList<>(uniqueElements);
		// Check for null to avoid NullPointerException
		if (failureTypeList == null) {
			failureTypeList = new ArrayList<>();
//			Application.getInstance().getGUILog().showMessage("Deafult list is empty");
		}
		if (MalfunctionList == null) {
			MalfunctionList = new ArrayList<>();
//			Application.getInstance().getGUILog().showMessage("No defined failure types");
		}

		// Combine the lists
		List<Element> combinedFTList = new ArrayList<>(allItems);
		combinedFTList.addAll(MalfunctionList);
		// Create a panel to hold radio buttons
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		radioButtonPanel.setBackground(Color.WHITE);

		List<JCheckBox> radioButtonList = new ArrayList<>();
		List<String> NameList = new ArrayList<>();
		
//        Application.getInstance().getGUILog().showMessage("Sub Window \n Deafult: "+failureTypeList.size()+'\n'+"Defined: "+MalfunctionList.size()
//		+'\n'+"All Items: "+allItems.size()+'\n'+"Combined: "+combinedFTList.size());

		// Check if the list is null or empty
		if (combinedFTList == null || combinedFTList.isEmpty()) {
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
			for (Element item : combinedFTList) {
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
				if (failureTypeList.contains(item)||MalfunctionList.contains(item) ) {
					radioButton.setSelected(true);
				}
				radioButton.setBackground(Color.WHITE);

				radioButtonList.add(radioButton);

				if (allDefaultElements.contains(item)) {
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
		JScrollPane radioScrollPane = new JScrollPane();
		radioScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		radioScrollPane.setViewportView(radioButtonPanel);

		// Create a "Select All" checkbox
		JCheckBox selectAllCheckBox = new JCheckBox("Select All");
		selectAllCheckBox.setBackground(Color.WHITE);
		selectAllCheckBox.setFont(new Font("Tahoma", Font.BOLD, 12));

		// Add action listener to "Select All" checkbox
		selectAllCheckBox.addActionListener(e -> {
			boolean isSelected = selectAllCheckBox.isSelected();
			for (JCheckBox checkBox : radioButtonList) {
				checkBox.setSelected(isSelected);
			}
		});

		// Create a panel for the header containing the "Select All" checkbox
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Color.WHITE);
		headerPanel.add(selectAllCheckBox, BorderLayout.WEST);
		headerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));

		// Set the header view of the scroll pane
		radioScrollPane.setColumnHeaderView(headerPanel);

		// Wrap both the choose header and scrollPane in a panel
		JPanel choosePanel = new JPanel(new BorderLayout());
		choosePanel.setBackground(Color.WHITE);
		choosePanel.add(radioScrollPane, BorderLayout.CENTER); // Add scrollable content

		// Store the radio button list in client properties for later use
		choosePanel.putClientProperty("radioButtonList", radioButtonList);
		choosePanel.putClientProperty("NameList", NameList);

		return choosePanel;
	}


}
