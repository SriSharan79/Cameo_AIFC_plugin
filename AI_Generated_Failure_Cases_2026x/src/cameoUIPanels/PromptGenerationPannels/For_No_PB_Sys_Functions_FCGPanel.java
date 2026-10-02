package cameoUIPanels.PromptGenerationPannels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.uml.ElementIcon;
import com.nomagic.ui.ResizableIcon;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIDialogs.PGDialogs.OSIRISFrame;
import cameoUIDialogs.PGDialogs.PromptPreviewDialog;
import cameoUISubPanels.FCGSubPannelDesign;
import creationOfSafetyVP.CreateFunctionElement;
import creationOfSafetyVP.ElementFinder_SVP;
import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.IdGenerator;
import elementDataExtraction.PropertiesFunction;
import requestCreation.PropmptCreator;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_No_PB_Sys_Functions_FCGPanel` class is responsible for managing the user interface and interactions related to failure condition generation for system functions.
 */
public class For_No_PB_Sys_Functions_FCGPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public Map<String, List<String>> promptpropertyMap;
	public Map<String, List<String>> propertyMapUpdated;
	private JTextField textFieldDescription;
	Element selectedfunction;
	Element selectedfunctionAction;
	Map<String, List<Element>> propertyMap= new HashMap<String, List<Element>>();

	NameDerivator ND = new NameDerivator();
	Map<String, List<String>> propertyMapStrings;
	private JTextField Func_ID_textField;

	String functionName; 

    static IdGenerator IDG = new IdGenerator();

	/**
	 * Create the panel.
	 */
/**
 * @param selectedNode The `selectedNode` parameter of type `Element` in the method `For_No_PB_Sys_Functions_FCGPanel(Element selectedNode)` represents the SysML model element (such as a CallBehaviorAction or Activity) that has been selected for analysis and display within the Functional Hazard Assessment panel.
 * @return The `For_No_PB_Sys_Functions_FCGPanel` method returns an instance of the `For_No_PB_Sys_Functions_FCGPanel`. This panel is designed to provide a detailed view and interaction interface for functional hazard assessment in OSIRIS. It includes various sub-panels, checkboxes, labels, and scroll panes that collectively allow users to input and select relevant data points such as failure types, phases, systems responsible, and requirements related to the selected function or activity within the SysML model.
 * @summary The `For_No_PB_Sys_Functions_FCGPanel` method initializes and configures a GUI panel for performing functional hazard assessment on selected aircraft system functions.
 */
	public For_No_PB_Sys_Functions_FCGPanel(Element selectedNode) {
		setSize(new Dimension(800, 800));
		setPreferredSize(new Dimension(780, 800));

		var splitpannel = new FCGSubPannelDesign();
		var functionProperties = new PropertiesFunction();
		var elementfinder =new ElementFinder_SVP();
		// Check if selected node is a CallBehaviorAction
		if (selectedNode instanceof CallBehaviorAction) {		
			// Get the behavior of the CallBehaviorAction
			selectedfunctionAction =selectedNode;
			var function = ((CallBehaviorAction) selectedNode).getBehavior();

			// Check if the function is not null and cast it to Element
			if (function != null) {
				selectedfunction = (Element) function;
			}
			propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction,selectedfunctionAction);
			propertyMapStrings = functionProperties.StereotypeStringPropertiesMapGenerate(selectedfunction);

		} else if (selectedNode instanceof Activity) {
			// If selectedNode is an Activity, cast it to Element
			selectedfunction = (Element) selectedNode;
			propertyMap = functionProperties.StereotypePropertiesElementsMapGenerate(selectedfunction);
			propertyMapStrings = functionProperties.StereotypeStringPropertiesMapGenerate(selectedfunction);
		}

		List<Element> failureTypeList = propertyMap.get("Default Failure Type");
		List<Element> MalfunctionList = propertyMap.get("Defined Failure Type");
		// Check for null to avoid NullPointerException
        if (failureTypeList == null) {
            failureTypeList = new ArrayList<>();
            Application.getInstance().getGUILog().showMessage("Default Failure Type list is empty");
        }
        if (MalfunctionList == null) {
        	MalfunctionList = new ArrayList<>();
            Application.getInstance().getGUILog().showMessage("No defined failure types");
        }

        // Combine the lists
        List<Element> combinedFTList = new ArrayList<>(failureTypeList);
        combinedFTList.addAll(MalfunctionList);
		functionName= ND.getName(selectedfunction);
		List<Element> phaseList = propertyMap.get("Phase");
		List<Element> systemList = propertyMap.get("System");
		List<String> descriptionList = propertyMapStrings.get("Description");
		List<String> IDList = propertyMapStrings.get("ID");
		List<Element> inputList = propertyMap.get("InputParameters");
		List<Element> outputList = propertyMap.get("OutputParameters");
		List<Element> RequirementList = propertyMap.get("Requirements");

		List<Element> UpperLevelList = propertyMap.get("UpperLevelFunctions");
		List<String> UpperLevelFuncStringList=ND.getStringListForElements(UpperLevelList);

		List<Element> LowerLevelList = propertyMap.get("LowerLevelFunctions");
		List<String> LowerLevelFuncStringList=ND.getStringListForElements(LowerLevelList);

		List<Element> AllphaseList = elementfinder.FindAllPhases();
		List<Element> AllfailureTypeList = elementfinder.FindAllFailure();
		

//        Application.getInstance().getGUILog().showMessage("Main Window \n Deafult: "+failureTypeList.size()+'\n'+"Defined: "+MalfunctionList.size()
//        													+'\n'+"Combined: "+combinedFTList.size());

		StringBuilder Description = new StringBuilder();
		for (String n : descriptionList) {
			Description.append(n);
		}	
		
		StringBuilder ID = new StringBuilder();
		for (String n : IDList) {
			ID.append(n);
		}

		JPanel L_panel = new JPanel();
		L_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		L_panel.setSize(new Dimension(780, 780));
		L_panel.setPreferredSize(new Dimension(780, 780));

		JPanel subpanel_3 = new JPanel();

		JScrollPane scrollPane_IO = new JScrollPane();
		scrollPane_IO.setBorder(new LineBorder(Color.BLACK));
		GroupLayout gl_subpanel_3 = new GroupLayout(subpanel_3);
		gl_subpanel_3.setHorizontalGroup(
				gl_subpanel_3.createParallelGroup(Alignment.LEADING)
				.addGap(0, 760, Short.MAX_VALUE)
				.addGroup(gl_subpanel_3.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_IO, GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_subpanel_3.setVerticalGroup(
				gl_subpanel_3.createParallelGroup(Alignment.LEADING)
				.addGap(0, 173, Short.MAX_VALUE)
				.addComponent(scrollPane_IO, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
				);

		JCheckBox rdbtnNewRadioButton_IO = new JCheckBox("Input & Output Parameters");
		rdbtnNewRadioButton_IO.setSelected(true);
		rdbtnNewRadioButton_IO.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnNewRadioButton_IO.setFont(new Font("Tahoma", Font.BOLD, 12));
		scrollPane_IO.setColumnHeaderView(rdbtnNewRadioButton_IO);

		JPanel panel_IO = new JPanel();
		panel_IO.setPreferredSize(new Dimension(200, 100));
		panel_IO.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_IO.setBackground(Color.WHITE);
		scrollPane_IO.setViewportView(panel_IO);

		JScrollPane scrollPane_In = new JScrollPane();
		scrollPane_In.setPreferredSize(new Dimension(100, 200));

		JScrollPane scrollPane_Out = new JScrollPane();
		scrollPane_Out.setPreferredSize(new Dimension(100, 200));
		scrollPane_Out.setBorder(new LineBorder(new Color(130, 135, 144)));
		GroupLayout gl_panel_IO = new GroupLayout(panel_IO);
		gl_panel_IO.setHorizontalGroup(
				gl_panel_IO.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 738, Short.MAX_VALUE)
				.addGroup(gl_panel_IO.createSequentialGroup()
						.addComponent(scrollPane_In, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_Out, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
				);
		gl_panel_IO.setVerticalGroup(
				gl_panel_IO.createParallelGroup(Alignment.LEADING)
				.addGap(0, 156, Short.MAX_VALUE)
				.addComponent(scrollPane_Out, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
				.addComponent(scrollPane_In, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
				);

		JLabel lblNewLabel_Out = new JLabel("Output Parameters");
		lblNewLabel_Out.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_Out.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_Out.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_Out.setColumnHeaderView(lblNewLabel_Out);

		JPanel panel_Out = new JPanel();
		panel_Out.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_Out.setBackground(Color.WHITE);
		//		need to add elements
		panel_Out = splitpannel.AddCheckBoxes(panel_Out, outputList);

		scrollPane_Out.setViewportView(panel_Out);

		JLabel lblNewLabel_in = new JLabel("Input Parameters");
		lblNewLabel_in.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_in.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_in.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_In.setColumnHeaderView(lblNewLabel_in);

		JPanel panel_in = new JPanel();
		panel_in.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_in.setBackground(Color.WHITE);

		//		need to add elements
		panel_in = splitpannel.AddCheckBoxes(panel_in, inputList);

		scrollPane_In.setViewportView(panel_in);
		panel_IO.setLayout(gl_panel_IO);
		subpanel_3.setLayout(gl_subpanel_3);

		JPanel subpanel_4 = new JPanel();

		JScrollPane scrollPane_Requirements = new JScrollPane();
		scrollPane_Requirements.setPreferredSize(new Dimension(400, 600));

		JCheckBox LabelRequirements = new JCheckBox("Requirements");
		LabelRequirements.setSelected(true);
		LabelRequirements.setBorder(new LineBorder(new Color(0, 0, 0)));
		LabelRequirements.setHorizontalAlignment(SwingConstants.CENTER);
		LabelRequirements.setFont(new Font("Tahoma", Font.BOLD, 12));
		scrollPane_Requirements.setColumnHeaderView(LabelRequirements);

		JPanel panel_requirements = new JPanel();

		panel_requirements.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_requirements.setBackground(Color.WHITE);

		panel_requirements = splitpannel.AddCheckBoxes(panel_requirements, RequirementList);

		panel_requirements.setPreferredSize(new Dimension(380, 1200));  // Adjust height as needed

		scrollPane_Requirements.setViewportView(panel_requirements);

		// Add scrollPane_Requirements to subpanel_4 using GroupLayout
		GroupLayout gl_subpanel_4 = new GroupLayout(subpanel_4);
		gl_subpanel_4.setHorizontalGroup(
				gl_subpanel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_subpanel_4.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_Requirements, GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_subpanel_4.setVerticalGroup(
				gl_subpanel_4.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Requirements, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
				);
		subpanel_4.setLayout(gl_subpanel_4);;

		JPanel subpanel_1 = new JPanel();

		JScrollPane scrollPane_ft = new JScrollPane();
		scrollPane_ft.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_ft.setBackground(Color.WHITE);

		JScrollPane scrollPane_phases = new JScrollPane();
		scrollPane_phases.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_subpanel_1 = new GroupLayout(subpanel_1);
		gl_subpanel_1.setHorizontalGroup(
				gl_subpanel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 760, Short.MAX_VALUE)
				.addGroup(gl_subpanel_1.createSequentialGroup()
						.addGap(20)
						.addComponent(scrollPane_ft, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
						.addGap(81)
						.addComponent(scrollPane_phases, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
						.addGap(19))
				);
		gl_subpanel_1.setVerticalGroup(
				gl_subpanel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 161, Short.MAX_VALUE)
				.addComponent(scrollPane_ft, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
				.addComponent(scrollPane_phases, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
				);

		JLabel lblNewLabel_phases = new JLabel("Phases");
		lblNewLabel_phases.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_phases.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_phases.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblNewLabel_phases.setBackground(Color.WHITE);
		scrollPane_phases.setColumnHeaderView(lblNewLabel_phases);

		JPanel panel_phases = splitpannel.AddCheckboxButtons(AllphaseList, phaseList,"Phases");
		panel_phases.setBackground(Color.WHITE);
		scrollPane_phases.setViewportView(panel_phases);

		JLabel lblNewLabel_Failuretypes = new JLabel("Failure Types");
		lblNewLabel_Failuretypes.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_Failuretypes.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_Failuretypes.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblNewLabel_Failuretypes.setBackground(Color.WHITE);
		scrollPane_ft.setColumnHeaderView(lblNewLabel_Failuretypes);

		JPanel panel_ft = splitpannel.AddCheckboxButtons(AllfailureTypeList, failureTypeList,MalfunctionList,"Failure Type");
		panel_ft.setBackground(Color.WHITE);
		scrollPane_ft.setViewportView(panel_ft);

		subpanel_1.setLayout(gl_subpanel_1);

		textFieldDescription = new JTextField(Description.toString());
		textFieldDescription.setColumns(10);

		JLabel lbl_Description = new JLabel(functionName +" Description");
		lbl_Description.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl_Description.setFont(new Font("Tahoma", Font.BOLD, 14));
		ResizableIcon icon =ElementIcon.getIcon(selectedfunction);
		lbl_Description.setIcon(icon);
		
		
		JPanel Func_ID_Panel = new JPanel();
		Func_ID_Panel.setLayout(null);
		
		JLabel lbl_ID = new JLabel("ID");
		lbl_ID.setFont(new Font("Tahoma", Font.BOLD, 14));
		lbl_ID.setBounds(0, 2, 40, 14);
		Func_ID_Panel.add(lbl_ID);

		Func_ID_textField = new JTextField(ID.toString());
		Func_ID_textField.setColumns(10);
		Func_ID_textField.setBounds(34, 1, 131, 20);
		Func_ID_Panel.add(Func_ID_textField);

		JPanel subpanel_2 = new JPanel();
		subpanel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JLabel lblNewLabel_system = new JLabel("System Responsible");
		lblNewLabel_system.setPreferredSize(new Dimension(100, 20));
		lblNewLabel_system.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_system.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_system.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblNewLabel_system.setBackground(Color.WHITE);

		JPanel panel_systems = new JPanel();
		panel_systems.setBackground(Color.WHITE);

		panel_systems = splitpannel.createPanelWithSystems(panel_systems, systemList);// comment this to check layout

		GroupLayout gl_subpanel_2 = new GroupLayout(subpanel_2);
		gl_subpanel_2.setHorizontalGroup(
				gl_subpanel_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 759, Short.MAX_VALUE)
				.addGroup(gl_subpanel_2.createSequentialGroup()
						.addComponent(lblNewLabel_system, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_systems, GroupLayout.PREFERRED_SIZE, 535, GroupLayout.PREFERRED_SIZE)
						.addGap(28))
				);
		gl_subpanel_2.setVerticalGroup(
				gl_subpanel_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 32, Short.MAX_VALUE)
				.addGroup(gl_subpanel_2.createSequentialGroup()
						.addGroup(gl_subpanel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_systems, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_subpanel_2.createSequentialGroup()
										.addGap(1)
										.addComponent(lblNewLabel_system, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)))
						.addContainerGap())
				);
		subpanel_2.setLayout(gl_subpanel_2);

		List<JCheckBox>Phases_checkBoxList = splitpannel.extractRadioButtonList(panel_phases);
		List<JCheckBox>FT_checkBoxList = splitpannel.extractRadioButtonList(panel_ft);

		List<String>FTs = splitpannel.extractList(panel_ft,"NameList");

		List<String>Phs = splitpannel.extractList(panel_phases,"NameList");

		List<JCheckBox>Input_checkBoxList = splitpannel.extractCheckBoxList(panel_in);


		List<JCheckBox>Output_checkBoxList = splitpannel.extractCheckBoxList(panel_Out);
		
		rdbtnNewRadioButton_IO.addActionListener(e -> {
			boolean isSelected = rdbtnNewRadioButton_IO.isSelected();
			for (JCheckBox checkBox : Input_checkBoxList) {
				checkBox.setSelected(isSelected);
			}
			for (JCheckBox checkBox : Output_checkBoxList) {
				checkBox.setSelected(isSelected);
			}
		});
		
		List<String>Output_Names = splitpannel.extractList(panel_Out,"NameList");
		List<String>Output_Types = splitpannel.extractList(panel_Out,"TypeList");
		List<String>Input_Names = splitpannel.extractList(panel_in,"NameList");
		List<String>Input_Types = splitpannel.extractList(panel_in,"TypeList");
		
		rdbtnNewRadioButton_IO.addActionListener(e -> {
			boolean isSelected = rdbtnNewRadioButton_IO.isSelected();
			for (JCheckBox checkBox : Input_checkBoxList) {
				checkBox.setSelected(isSelected);
			}
			for (JCheckBox checkBox : Output_checkBoxList) {
				checkBox.setSelected(isSelected);
			}
		});

		List<JCheckBox>Req_checkBoxList = splitpannel.extractCheckBoxList(panel_requirements);
		// Add action listener to "Select All" checkbox
		LabelRequirements.addActionListener(e -> {
			boolean isSelected = LabelRequirements.isSelected();
			for (JCheckBox checkBox : Req_checkBoxList) {
				checkBox.setSelected(isSelected);
			}
		});
		List<String>Req_Names = splitpannel.extractList(panel_requirements,"NameList");
		List<String>Req_Types = splitpannel.extractList(panel_requirements,"TypeList");
		JPanel ExtraInfoPannel_1 = new JPanel();
		ExtraInfoPannel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
//		ExtraInfoPannel_1.setBackground(new Color(255, 255, 255));
		
		JLabel lblNewLabel_1 = new JLabel("Additional Information:");
		lblNewLabel_1.setBounds(1, 1, 734, 30);
		lblNewLabel_1.setBackground(SystemColor.control);
		lblNewLabel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JCheckBox chckbxUpperLevelFunctions_1 = new JCheckBox("Upper Level Functions");
		chckbxUpperLevelFunctions_1.setBounds(1, 32, 356, 32);
		chckbxUpperLevelFunctions_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxUpperLevelFunctions_1.setBackground(Color.WHITE);
		
		JCheckBox chckbxLowerLevelFunctions_1 = new JCheckBox("Lower Level Functions");
		chckbxLowerLevelFunctions_1.setBounds(356, 32, 379, 32);
		chckbxLowerLevelFunctions_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxLowerLevelFunctions_1.setBackground(Color.WHITE);

		JButton btn_GeneratePrompt = new JButton("Generate Request");
		btn_GeneratePrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

		btn_GeneratePrompt.addActionListener(new ActionListener() {
/**
 * @param e The parameter `e` of type `ActionEvent` in the method `actionPerformed` represents the event that triggered the action to be performed.
 * @summary The `actionPerformed` method handles the logic for creating and updating function elements within OSIRIS based on user selections from various input fields and checkboxes.
 */
			public void actionPerformed(ActionEvent e) {// Get selected items from each list

				propertyMapUpdated = new HashMap<String, List<String>>();
				promptpropertyMap= new HashMap<String, List<String>>();

				String Description =textFieldDescription.getText();		
				String ID =Func_ID_textField.getText();
				if (ID == null || ID.trim().isEmpty()) {
					ID = IDG.setID(functionName);
					Func_ID_textField.setText(ID);
				}
				List<String> FailureTypes = splitpannel.getSelectedItem(FT_checkBoxList,FTs);
				List<String> Phases = splitpannel.getSelectedItem(Phases_checkBoxList,Phs);

				List<String> Requirements = splitpannel.getSelectedItems(Req_checkBoxList,Req_Names);
				List<String> Inputs = splitpannel.getSelectedItems(Input_checkBoxList,Input_Names);
				List<String> Outputs = splitpannel.getSelectedItems(Output_checkBoxList,Output_Names);

				List<String> Requirements_types = splitpannel.getSelectedItems(Req_checkBoxList,Req_Types);
				List<String> Inputs_types = splitpannel.getSelectedItems(Input_checkBoxList,Input_Types);
				List<String> Outputs_types = splitpannel.getSelectedItems(Output_checkBoxList,Output_Types);
				List<String> Systems =new ArrayList<String>();
				for (Element obj : systemList) {
					Systems.add(ND.getName((Element) obj));
				}
				var Function = new CreateFunctionElement();
				Function.AddDescription(selectedfunction,Description);
				Function.AddID(selectedfunction,ID);
				Function.AddSystems(selectedfunction, Systems);
				if(FailureTypes!=null && !FailureTypes.isEmpty()) {
					Function.AddFailuretypes(selectedfunction, FailureTypes);
				}
				if(Phases!=null && !Phases.isEmpty()) {
					Function.AddPhases(selectedfunction, Phases);
				}

				if(chckbxUpperLevelFunctions_1.isSelected()) {

					propertyMapUpdated.put("UpperLevelFunctions", UpperLevelFuncStringList);

					propertyMapUpdated= ND.getSystemNames_For_Functions(UpperLevelFuncStringList, propertyMap,propertyMapUpdated);
					

				}		
				if(chckbxLowerLevelFunctions_1.isSelected()) {

					propertyMapUpdated.put("LowerLevelFunctions", LowerLevelFuncStringList);
					propertyMapUpdated= ND.getSystemNames_For_Functions(LowerLevelFuncStringList, propertyMap,propertyMapUpdated);

				}


				propertyMapUpdated.put("Failure Type", FailureTypes);
				propertyMapUpdated.put("Phase", Phases);
				propertyMapUpdated.put("System", Systems);

				if(rdbtnNewRadioButton_IO.isSelected()) {
					propertyMapUpdated.put("InputParameters", Inputs);
					propertyMapUpdated.put("OutputParameters", Outputs);
					propertyMapUpdated.put("InputTypes", Inputs_types);
					propertyMapUpdated.put("OutputTypes", Outputs_types);
				}

				if(LabelRequirements.isSelected()) {
					propertyMapUpdated.put("Requirements", Requirements);
					propertyMapUpdated.put("RequirementsTexts", Requirements_types);
				}


				var PromptGenerator=new PropmptCreator(ID,Description,propertyMapUpdated,selectedfunction);
				promptpropertyMap = PromptGenerator.functionInfo();
				promptpropertyMap.put("Requirements", Requirements);
				promptpropertyMap.put("RequirementsTexts", Requirements_types);
				promptpropertyMap.put("InputParameters", Inputs);
				promptpropertyMap.put("OutputParameters", Outputs);
				promptpropertyMap.put("InputTypes", Inputs_types);
				promptpropertyMap.put("OutputTypes", Outputs_types);
				
				promptpropertyMap= ND.getSystemNames_For_Functions(UpperLevelFuncStringList, propertyMap,promptpropertyMap);

				promptpropertyMap= ND.getSystemNames_For_Functions(LowerLevelFuncStringList, propertyMap,promptpropertyMap);

				// Generate the prompt
				String generated_prompt = PromptGenerator.generate();
			
//				Frame parent = MDDialogParentProvider.getProvider().getDialogParent(true);

				Frame parent = new OSIRISFrame();
				var dialog = new PromptPreviewDialog(parent, generated_prompt,promptpropertyMap, propertyMapUpdated, selectedfunction);
				dialog.setTitle("OSIRIS" );
				dialog.setVisible(true);
			}
		});

		JPanel RAG_panel = new JPanel();
		RAG_panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lbl_mainlabel = new JLabel("Request Generation Window");
		lbl_mainlabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl_mainlabel.setAlignmentX(0.5f);

		GroupLayout gl_RAG_panel = new GroupLayout(RAG_panel);
		gl_RAG_panel.setHorizontalGroup(
			gl_RAG_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_RAG_panel.createSequentialGroup()
					.addComponent(lbl_mainlabel, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
					.addGap(363))
		);
		gl_RAG_panel.setVerticalGroup(
			gl_RAG_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_RAG_panel.createSequentialGroup()
					.addComponent(lbl_mainlabel, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
					.addGap(4))
		);
		RAG_panel.setLayout(gl_RAG_panel);


		// Layout setup
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(L_panel, GroupLayout.PREFERRED_SIZE, 756, Short.MAX_VALUE)
					.addGap(754))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(L_panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		

		GroupLayout gl_L_panel = new GroupLayout(L_panel);
		gl_L_panel.setHorizontalGroup(
			gl_L_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_L_panel.createSequentialGroup()
					.addGroup(gl_L_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_L_panel.createSequentialGroup()
							.addGap(10)
							.addComponent(RAG_panel, GroupLayout.PREFERRED_SIZE, 736, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_L_panel.createSequentialGroup()
							.addGap(10)
							.addComponent(textFieldDescription, GroupLayout.PREFERRED_SIZE, 736, GroupLayout.PREFERRED_SIZE))
						.addComponent(subpanel_1, GroupLayout.PREFERRED_SIZE, 746, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_L_panel.createSequentialGroup()
							.addGap(10)
							.addComponent(subpanel_2, GroupLayout.PREFERRED_SIZE, 736, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_L_panel.createSequentialGroup()
							.addGap(10)
							.addComponent(ExtraInfoPannel_1, GroupLayout.PREFERRED_SIZE, 736, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_L_panel.createSequentialGroup()
							.addGap(569)
							.addComponent(btn_GeneratePrompt, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
						.addComponent(subpanel_3, GroupLayout.PREFERRED_SIZE, 746, GroupLayout.PREFERRED_SIZE)
						.addComponent(subpanel_4, GroupLayout.PREFERRED_SIZE, 746, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_L_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lbl_Description, GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(Func_ID_Panel, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_L_panel.setVerticalGroup(
			gl_L_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_L_panel.createSequentialGroup()
					.addGap(10)
					.addComponent(RAG_panel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_L_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(Func_ID_Panel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbl_Description, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldDescription, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(subpanel_1, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(subpanel_2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(subpanel_3, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(subpanel_4, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(ExtraInfoPannel_1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(btn_GeneratePrompt))
		);
		ExtraInfoPannel_1.setLayout(null);
		ExtraInfoPannel_1.add(chckbxUpperLevelFunctions_1);
		ExtraInfoPannel_1.add(chckbxLowerLevelFunctions_1);
		ExtraInfoPannel_1.add(lblNewLabel_1);
		L_panel.setLayout(gl_L_panel);
		setLayout(groupLayout);

	}
}

