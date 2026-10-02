package cameoUISubPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
 * @summary The FCGSimpleSubPannel class is responsible for managing and displaying a variety of checkboxes and radio buttons in a graphical user interface to facilitate the selection and extraction of items based on different element lists and configurations.
 */
public class FCGSimpleSubPannel {

	NameDerivator ND = new NameDerivator();
	StereotypeSearch STC =new StereotypeSearch();


/**
 * @param allElements **Description of `allElements` parameter:**

The `allElements` parameter represents the comprehensive list of elements that will be used to populate checkboxes within a graphical user interface panel.
 * @param SelectedList The parameter `SelectedList` of type `List<Element>` is a collection representing the elements that are currently selected for further processing or display.
 * @param Title The `Title` parameter specifies the label or heading for the panel that will display checkbox buttons.
 * @return The return value of the `AddCheckboxButtons` method is a `JPanel` that contains a user interface component for selecting elements. This panel includes:
- A scrollable area with checkboxes corresponding to each element in the provided list.
- A "Select All" checkbox at the top, allowing users to select or deselect all checkboxes simultaneously.
- Appropriate labels and icons for each element, enhancing visual clarity.
- Client properties storing the list of checkboxes and their names for later use.

This panel is designed to provide a clear and efficient way for users to manage selections from a potentially large set of elements.
 * @summary The `AddCheckboxButtons` method creates and returns a JPanel containing checkboxes for elements from the provided lists, with an option to select all.
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
 * @param scrollPane The `scrollPane` parameter represents the container panel that holds the list of radio buttons as a client property.
 * @return The return value of the `extractRadioButtonList` method is a list of JCheckBox components extracted from the provided JPanel. This list represents radio buttons that were previously stored as a client property in the JPanel.
 * @summary The `extractRadioButtonList` method retrieves a list of `JCheckBox` radio buttons stored as a client property in the provided `JPanel`.
 */
	@SuppressWarnings("unchecked")
	public List<JCheckBox> extractRadioButtonList(JPanel scrollPane) {
		return (List<JCheckBox>) scrollPane.getClientProperty("radioButtonList");
	}


/**
 * @param radioButtonList `radioButtonList`: A collection of JCheckBox components representing a group of radio buttons.
 * @param stringList The `stringList` parameter contains a list of strings that correspond to each radio button in `radioButtonList`, used for selecting and returning relevant items.
 * @return The return value of `getSelectedItem` is a List of Strings representing items that correspond to selected check boxes.
 * @summary This method retrieves a list of strings corresponding to selected JCheckBoxes in a given list.
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
 * @param panel The `panel` parameter is a `JPanel` object that will be configured and populated with system labels based on the provided list of elements.
 * @param systemList `systemList` is a list of `Element` objects representing the aircraft systems to be displayed on the panel.
 * @return The `createPanelWithSystems` method returns a `JPanel` that has been configured with a vertical layout and populated based on the content of the `systemList`. If the list is empty, the panel will display an informative message. Otherwise, it will display labels for each system in the list, ensuring they are visually centered and aligned within the panel.
 * @summary The `createPanelWithSystems` method is responsible for populating a given JPanel with system elements from a provided list, configuring the layout and adding appropriate labels or messages based on whether the list is empty.
 */
	public JPanel createPanelWithSystems( JPanel panel, List<Element> systemList) {

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
 * @param checkBoxPanel The `checkBoxPanel` parameter is a JPanel that will be populated with checkboxes and related elements for user selection.
 * @param elementList `elementList`: A list of `Element` objects representing the items for which checkboxes will be created.
 * @return The `AddCheckBoxes` method returns a configured `JPanel` that contains a series of checkboxes and labels, each representing an element from the provided list. The panel is laid out vertically with a specified size and includes either a message indicating no elements are assigned or individual panels for each element, which include a checkbox, name, and type information.
 * @summary This method dynamically creates and populates a JPanel with JCheckBoxes based on the provided list of elements, handling cases where the element list is null or empty.
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
 * @param item The `item` parameter represents a SysML model element whose type or description is to be determined and returned as a string.
 * @return The `GetType` method returns a descriptive `String` representing the type of the input `Element`. If the element is a parameter or pin, it provides the name of the typed item. For elements with a requirement stereotype, it extracts and returns relevant text from the stereotype's properties. If no meaningful information is found, it returns appropriate default messages like "No type assigned" or "No Description for the requirement."
 * @summary The `GetType` method determines and returns a type or description string based on the provided SysML model element, handling parameters, pins, and requirements with specific stereotypes.
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
 * @param item The `item` parameter represents an object that can either be a `Parameter`, `Pin`, or an element with requirement stereotypes in the context of creating a labeled JPanel.
 * @return The `CreateLabelPanel` method returns a JPanel that contains a visually formatted display of an item's name and associated details. This panel is specifically designed to show either the type information for parameters or pins, or the description text for requirements if they have a requirement stereotype. The layout and content are adjusted based on whether the item has a type or not, ensuring clear and consistent presentation within the UI.
 * @summary The `CreateLabelPanel` method is responsible for creating and returning a JPanel that displays the name and type information of an item, with special handling for items with requirement stereotypes.
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
 * @param panel The `panel` parameter is a JPanel from which the method retrieves a list of JCheckBoxes stored as a client property.
 * @return The `extractCheckBoxList` method returns a list of `JCheckBox` components that are associated with the given `JPanel`. This list is retrieved from a client property named "checkBoxList" that is stored within the `JPanel`.
 * @summary The method `extractCheckBoxList` retrieves a list of JCheckBox components stored as a client property in the given JPanel.
 */
	@SuppressWarnings("unchecked")
	public List<JCheckBox> extractCheckBoxList(JPanel panel) {
		return (List<JCheckBox>) panel.getClientProperty("checkBoxList");
	}


/**
 * @param checkBoxList The `checkBoxList` parameter represents a collection of JCheckBoxes used to determine which items from the `stringList` have been selected by the user.
 * @param stringList The `stringList` parameter is a list of strings that corresponds to the labels or values associated with each checkbox in the `checkBoxList`.
 * @return The return value for `getSelectedItems` is a List of Strings representing the items associated with selected checkboxes. It contains the strings from the provided list that correspond to the positions of checked checkboxes in the given list.
 * @summary The `getSelectedItems` method returns a list of strings corresponding to selected checkboxes in a given list.
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
 * @param panel The `panel` parameter is the Swing `JPanel` from which to retrieve a client property as a list of strings.
 * @param string The `string` parameter in the `extractList` method is used as a key to retrieve a list of strings from the client properties of the provided `JPanel`.
 * @return The `extractList` method retrieves a list of strings associated with a specific key from the client properties of a given JPanel. The return value is a List<String> that represents these extracted string values.
 * @summary The `extractList` method retrieves a list of strings from the client properties of a given JPanel using a specified key.
 */
	@SuppressWarnings("unchecked")
	public List<String> extractList(JPanel panel, String string) {
		// TODO Auto-generated method stub
		return (List<String>) panel.getClientProperty(string);
	}


/**
 * @param allDefaultElements The `allDefaultElements` parameter is a list of SysML elements that represent the default failure conditions initially assigned to an aircraft system.
 * @param failureTypeList `failureTypeList`: A list of elements representing the types of failures selected by the user, used to determine which checkboxes should be pre-selected.
 * @param MalfunctionList `MalfunctionList`: This parameter represents a list of elements that are malfunctioning within the system, used to populate checkboxes in the GUI for user selection.
 * @param string The `string` parameter is a placeholder and does not have any specific functionality within the method, potentially intended for future enhancements or customization.
 * @return The `AddCheckboxButtons` method returns a `JPanel` that contains checkboxes representing elements from the provided lists. This panel includes a "Select All" checkbox at the top for convenient bulk selection, and it is wrapped in a scrollable pane to handle large lists of elements.
 * @summary The `AddCheckboxButtons` method constructs and returns a JPanel containing checkboxes for each element in the provided lists, with an optional "Select All" feature.
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
				if (failureTypeList.contains(item)) {
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
