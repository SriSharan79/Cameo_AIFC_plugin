package cameoUIPanels.PromptGenerationPannels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.uml.ElementIcon;
import com.nomagic.ui.ResizableIcon;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIDialogs.PGDialogs.OSIRISFrame;
import cameoUIDialogs.PGDialogs.SystemMessagePreviewDialog;
import cameoUISubPanels.FCGSimpleSubPannel;
import creationOfSafetyVP.CreateFunctionElement;
import creationOfSafetyVP.ElementFinder_SVP;
import creationOfSafetyVP.NameDerivator;
import dataloggers.InternalDataRetreiver;
import elementDataExtraction.ElementDataProcessor;
import elementDataExtraction.IdGenerator;
import elementDataExtraction.PropertiesFunction;
import requestCreation.PropmptCreator;
import requestCreation.RequestSender;
import responseCollection.ResponseCollector;
import restAPI.BlabladorAPIJava;
import restAPI.DLROllamaAPI;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `For_Auto_AC_Functions_FCGSimplePanel` class is responsible for managing user interactions and updates within a specific graphical panel related to the FHA of an aircraft's functions.
 */
public class For_Auto_AC_Functions_FCGSimplePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public Map<String, List<String>> promptpropertyMap;
	public Map<String, List<String>> propertyMapUpdated;
	private JTextField textFieldDescription;
	Element selectedfunction;
	Element selectedfunctionAction;

	private String ModelName = "";

	DLROllamaAPI DLRO;
	Map<String, List<Element>> propertyMap= new HashMap<String, List<Element>>();
	// Key Management
	private InternalDataRetreiver apiKeyManager;

//	JCheckBox chckbx_API_key;
	JToggleButton View_btn_API;
	JPanel Key_panel;
	JPanel L_panel;
	String functionName; 
	JComboBox<String> providerDropdown;

	static IdGenerator IDG = new IdGenerator();

	String SYS_MSG_FILE_NAME = "Sys_MSG_AC_Functions.txt";
	String Sys_MSG_AC = "";
	//	Frame parent = MDDialogParentProvider.getProvider().getDialogParent(true);

	Frame parent = new OSIRISFrame();

	ElementDataProcessor EDP = new ElementDataProcessor();

	NameDerivator ND = new NameDerivator();
	Map<String, List<String>> propertyMapStrings= new HashMap<String, List<String>>();
	private JFormattedTextField API_key;
	private JTextField Func_ID_textField;

	/**
	 * @param selectedNode The `selectedNode` parameter of type `Element` in the method `For_Auto_AC_Functions_FCGSimplePanel(Element selectedNode)` represents the SysML model element (such as a CallBehaviorAction or Activity) that has been selected for analysis and display its properties in the panel.
	 * @return The `For_Auto_AC_Functions_FCGSimplePanel` method returns an instance of the `For_Auto_AC_Functions_FCGSimplePanel`. This panel is designed to provide a detailed view and interaction interface for functional hazard assessment in OSIRIS. It includes various sub-panels, checkboxes, labels, and scroll panes that collectively allow users to input and select relevant data points such as failure types, phases, systems responsible related to the selected function or activity within the SysML model.
	 * @summary The `For_Auto_AC_Functions_FCGSimplePanel` method initializes and configures a GUI panel for performing functional hazard assessment on selected aircraft system functions.
	 */
	public For_Auto_AC_Functions_FCGSimplePanel(Element selectedNode) {
		setPreferredSize(new Dimension(780, 730));
		apiKeyManager = new InternalDataRetreiver(this);
		DLRO = new DLROllamaAPI(); // Assuming DLROllamaAPI has a default constructor

		var splitpannel = new FCGSimpleSubPannel();
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

		List<Element> AllphaseList = elementfinder.FindAllPhases();
		List<Element> AllfailureTypeList = elementfinder.FindAllFailure();

		StringBuilder Description = new StringBuilder();
		for (String n : descriptionList) {
			Description.append(n);
		}

		StringBuilder ID = new StringBuilder();
		for (String n : IDList) {
			ID.append(n);
		}
		 providerDropdown = new JComboBox<>(new String[]{"Local", "Ollama (DLR Server)", "Helmholtz Blablador"});

		L_panel = new JPanel();
		L_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JPanel subpanel_3 = new JPanel();
		subpanel_3.setBounds(2, 361, 746, 165);

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

		JLabel rdbtnNewRadioButton_IO = new JLabel("Input & Output Parameters");
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
		subpanel_4.setBounds(2, 536, 746, 205);

		JScrollPane scrollPane_Requirements = new JScrollPane();
		scrollPane_Requirements.setPreferredSize(new Dimension(400, 600));

		JLabel LabelRequirements = new JLabel("Requirements");
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
		subpanel_1.setBounds(10, 133, 736, 176);

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

		JPanel panel_ft = splitpannel.AddCheckboxButtons( AllfailureTypeList, failureTypeList,MalfunctionList,"Failure Type");
		panel_ft.setBackground(Color.WHITE);
		scrollPane_ft.setViewportView(panel_ft);

		subpanel_1.setLayout(gl_subpanel_1);

		textFieldDescription = new JTextField(Description.toString());
		textFieldDescription.setBounds(12, 70, 736, 53);
		textFieldDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldDescription.setColumns(10);

		JLabel lbl_Description = new JLabel(functionName+" Description");
		lbl_Description.setBounds(12, 45, 495, 19);
		lbl_Description.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl_Description.setFont(new Font("Tahoma", Font.BOLD, 14));
		ResizableIcon icon =ElementIcon.getIcon(selectedfunction);
		lbl_Description.setIcon(icon);


		JPanel Func_ID_Panel = new JPanel();
		Func_ID_Panel.setBounds(573, 43, 175, 20);

		Func_ID_Panel.setLayout(null);

		JLabel lbl_ID = new JLabel("ID");
		lbl_ID.setBounds(0, 2, 40, 14);
		Func_ID_Panel.add(lbl_ID);
		lbl_ID.setFont(new Font("Tahoma", Font.BOLD, 14));

		Func_ID_textField = new JTextField(ID.toString());
		Func_ID_textField.setBounds(34, 1, 131, 20);
		Func_ID_Panel.add(Func_ID_textField);
		Func_ID_textField.setColumns(10);

		JPanel subpanel_2 = new JPanel();
		subpanel_2.setBounds(12, 319, 736, 32);
		subpanel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JLabel lblNewLabel_system = new JLabel("System Responsible");
		lblNewLabel_system.setPreferredSize(new Dimension(100, 20));
		lblNewLabel_system.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_system.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_system.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblNewLabel_system.setBackground(Color.WHITE);

		JPanel panel_systems = new JPanel();
		panel_systems.setBackground(Color.WHITE);

		//		panel_systems = splitpannel.createPanelWithSystems(panel_systems, systemList);                                              // comment this to check layout

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

		JPanel ExtraInfoPannel_1 = new JPanel();
		ExtraInfoPannel_1.setBounds(12, 359, 736, 61);
		ExtraInfoPannel_1.setBorder(new LineBorder(null));
		//		ExtraInfoPannel_1.setBackground(Color.WHITE);

		JLabel lblNewLabel_1 = new JLabel("Additional Information:");
		lblNewLabel_1.setBounds(1, 1, 734, 25);
		lblNewLabel_1.setBackground(SystemColor.control);
		lblNewLabel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));

		JCheckBox chckbxUpperLevelFunctions_1 = new JCheckBox("Upper Level Functions");
		chckbxUpperLevelFunctions_1.setBounds(1, 27, 356, 33);
		chckbxUpperLevelFunctions_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		chckbxUpperLevelFunctions_1.setBackground(Color.WHITE);

		JCheckBox chckbxLowerLevelFunctions_1 = new JCheckBox("Lower Level Functions");
		chckbxLowerLevelFunctions_1.setBounds(356, 27, 379, 33);
		chckbxLowerLevelFunctions_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		chckbxLowerLevelFunctions_1.setBackground(Color.WHITE);

		JTextArea textArea_prompt = new JTextArea();
		textArea_prompt.setBounds(12, 449, 736, 222);
		textArea_prompt.setLineWrap(true);
		textArea_prompt.setWrapStyleWord(true);

		List<JCheckBox>Phases_checkBoxList = splitpannel.extractRadioButtonList(panel_phases);
		List<JCheckBox>FT_checkBoxList = splitpannel.extractRadioButtonList(panel_ft);

		List<String>FTs = splitpannel.extractList(panel_ft,"NameList");

		List<String>Phs = splitpannel.extractList(panel_phases,"NameList");

		List<JCheckBox>Input_checkBoxList = splitpannel.extractCheckBoxList(panel_in);
		List<String>Input_Names = splitpannel.extractList(panel_in,"NameList");
		List<String>Input_Types = splitpannel.extractList(panel_in,"TypeList");

		List<JCheckBox>Output_checkBoxList = splitpannel.extractCheckBoxList(panel_Out);
		List<String>Output_Names = splitpannel.extractList(panel_Out,"NameList");
		List<String>Output_Types = splitpannel.extractList(panel_Out,"TypeList");

		List<JCheckBox>Req_checkBoxList = splitpannel.extractCheckBoxList(panel_requirements);
		List<String>Req_Names = splitpannel.extractList(panel_requirements,"NameList");
		List<String>Req_Types = splitpannel.extractList(panel_requirements,"TypeList");

		JButton btn_GeneratePrompt = new JButton("Generate Request");
		btn_GeneratePrompt.setBounds(571, 424, 177, 21);
		btn_GeneratePrompt.setAlignmentX(Component.CENTER_ALIGNMENT);


		List<Element> UpperLevelList = propertyMap.get("UpperLevelFunctions");
		List<String> UpperLevelFuncStringList=ND.getStringListForElements(UpperLevelList);

		List<Element> LowerLevelList = propertyMap.get("LowerLevelFunctions");
		List<String> LowerLevelFuncStringList=ND.getStringListForElements(LowerLevelList);

		btn_GeneratePrompt.addActionListener(new ActionListener() {
			/**
			 * @param e The `e` parameter represents an event object that encapsulates state information about an action event triggered by a user interaction.
			 * @summary The `actionPerformed` method handles user actions to collect and process function-related data from various input fields and checkbox lists, updating property maps and generating a prompt string based on the collected information.
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
				if(FailureTypes!=null && !FailureTypes.isEmpty())
					//						&& EDP.hasdefaultFT(FailureTypes)) 
				{
					Function.AddFailuretypes(selectedfunction, FailureTypes);
				}
				if(Phases!=null && !Phases.isEmpty()) {
					Function.AddPhases(selectedfunction, Phases);
				}


				propertyMapUpdated.put("Failure Type", FailureTypes);
				propertyMapUpdated.put("Phase", Phases);
				propertyMapUpdated.put("System", Systems);

				propertyMapUpdated.put("InputParameters", Inputs);
				propertyMapUpdated.put("OutputParameters", Outputs);
				propertyMapUpdated.put("InputTypes", Inputs_types);
				propertyMapUpdated.put("OutputTypes", Outputs_types);

				propertyMapUpdated.put("Requirements", Requirements);
				propertyMapUpdated.put("RequirementsTexts", Requirements_types);

				if(chckbxUpperLevelFunctions_1.isSelected()) {
					propertyMapUpdated.put("UpperLevelFunctions", UpperLevelFuncStringList);
					propertyMapUpdated= ND.getSystemNames_For_Functions(UpperLevelFuncStringList, propertyMap,propertyMapUpdated);
				}
				if(chckbxLowerLevelFunctions_1.isSelected()) {
					propertyMapUpdated.put("LowerLevelFunctions", LowerLevelFuncStringList);
					propertyMapUpdated= ND.getSystemNames_For_Functions(LowerLevelFuncStringList, propertyMap,propertyMapUpdated);
				}


				var PromptGenerator=new PropmptCreator(ID,Description,propertyMapUpdated,selectedfunction);
				promptpropertyMap = PromptGenerator.functionInfo();
				promptpropertyMap.put("Requirements", Requirements);
				promptpropertyMap.put("RequirementsTexts", Requirements_types);
				promptpropertyMap.put("InputParameters", Inputs);
				promptpropertyMap.put("OutputParameters", Outputs);
				promptpropertyMap.put("InputTypes", Inputs_types);
				promptpropertyMap.put("OutputTypes", Outputs_types);
				promptpropertyMap.put("UpperLevelFunctions", UpperLevelFuncStringList);
				promptpropertyMap.put("LowerLevelFunctions", LowerLevelFuncStringList);				
				promptpropertyMap= ND.getSystemNames_For_Functions(UpperLevelFuncStringList, propertyMap,promptpropertyMap);
				promptpropertyMap= ND.getSystemNames_For_Functions(LowerLevelFuncStringList, propertyMap,promptpropertyMap);
				// Generate the prompt
				String generated_prompt = PromptGenerator.generate();

				textArea_prompt.setText(generated_prompt);
			}
		});

		JPanel RAG_panel = new JPanel();
		RAG_panel.setBounds(12, 12, 736, 27);
		RAG_panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JRadioButton WithRAG = new JRadioButton("With RAG");
		WithRAG.setFont(new Font("Tahoma", Font.PLAIN, 12));

		//		WithRAG.setSelected(true);

		JRadioButton WORAG = new JRadioButton("Without RAG");
		WORAG.setFont(new Font("Tahoma", Font.PLAIN, 12));

		ButtonGroup BTNG = new ButtonGroup();
		BTNG.add(WORAG);
		BTNG.add(WithRAG);

		JLabel lbl_mainlabel = new JLabel("Request Generation Window");
		lbl_mainlabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lbl_mainlabel.setAlignmentX(0.5f);

		GroupLayout gl_RAG_panel = new GroupLayout(RAG_panel);
		gl_RAG_panel.setHorizontalGroup(
				gl_RAG_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_RAG_panel.createSequentialGroup()
						.addComponent(lbl_mainlabel, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
						.addGap(126)
						.addComponent(WithRAG, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
						.addGap(10)
						.addComponent(WORAG, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
						.addGap(21))
				);
		gl_RAG_panel.setVerticalGroup(
				gl_RAG_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_RAG_panel.createSequentialGroup()
						.addGroup(gl_RAG_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(WORAG, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(WithRAG, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lbl_mainlabel, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
						.addGap(4))
				);
		RAG_panel.setLayout(gl_RAG_panel);

		Key_panel = new JPanel();
		Key_panel.setBounds(165, 677, 401, 23);
		Key_panel.setLayout(null);
		JButton btn_sysmsg = new JButton("SYS MSG");
		btn_sysmsg.addActionListener(new ActionListener() {
			/**
			 * @param e The parameter `e` represents the event that triggered the method, containing details about the action performed.
			 * @summary This method handles user actions by initializing the API key, loading system messages, and displaying a preview dialog.
			 */
			public void actionPerformed(ActionEvent e) {
				String selectedProvider = (String) providerDropdown.getSelectedItem();
				apiKeyManager.initializeAndLoadKey( SYS_MSG_FILE_NAME,selectedProvider);
				Sys_MSG_AC = apiKeyManager.SysMSGsys;
				var dialog = new SystemMessagePreviewDialog(parent,
						Sys_MSG_AC,
						apiKeyManager.SYSMSGFilePath,
						apiKeyManager.targetDirPath);
				dialog.setTitle("System Message to LLM");
				dialog.setVisible(true);
			}
		});
		btn_sysmsg.setBounds(302, 0, 89, 23);
		Key_panel.add(btn_sysmsg);

		Key_panel.setVisible(false);

		API_key = new JFormattedTextField();
		API_key.setBounds(68, 1, 231, 20);
		API_key.setVisible(false);
		Key_panel.add(API_key);
		API_key.setBackground(new Color(255, 255, 255));
		API_key.setEditable(false); // Make it not editable when masked


		View_btn_API = new JToggleButton("View");
		View_btn_API.setBounds(0, 0, 64, 23);
		Key_panel.add(View_btn_API);
		View_btn_API.addActionListener(new ActionListener() {
			/**
			 * @param e The `ActionEvent` parameter `e` represents the event that triggered this action.
			 * @summary The `actionPerformed` method updates the visibility and editability of an API key field based on the selection state of a view button.
			 */
			public void actionPerformed(ActionEvent e) {
				if (View_btn_API.isSelected()) {
					API_key.setText(apiKeyManager.currentApiKey);
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
			 * @param e The `DocumentEvent e` parameter represents the event that signals a change in the document, specifically an insertion of text.
			 * @summary The `insertUpdate` method is responsible for updating and saving an API key when text in the document is inserted or modified.
			 */
			@Override public void insertUpdate(DocumentEvent e) { apiKeyManager.updateAndSavePassword(API_key);}
			/**
			 * @param e The `e` parameter represents a document event that occurs when text is removed from the document.
			 * @summary The method `removeUpdate` is responsible for updating and saving an API key when an update event occurs.
			 */
			@Override public void removeUpdate(DocumentEvent e) { apiKeyManager.updateAndSavePassword(API_key);}
			/**
			 * @param e The `e` parameter is a `DocumentEvent` object that represents changes made to a document's content.
			 * @summary The `changedUpdate` method is responsible for handling changes in text data within a document event, typically to update or validate input fields dynamically based on user modifications.
			 */
			@Override public void changedUpdate(DocumentEvent e) {}
		});

		API_key.addFocusListener(new java.awt.event.FocusAdapter() {
			/**
			 * @param e The parameter `e` represents the event that indicates a component has lost input focus.
			 * @summary The `focusLost` method is responsible for updating and saving the password when the API key field loses focus, provided that it is in an editable state.
			 */
			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (API_key.isEditable()) {
					apiKeyManager.updateAndSavePassword(API_key);
				}
			}
		});


		//		chckbx_API_key = new JCheckBox("Use Ollama with API key");
		//		chckbx_API_key.setBounds(12, 677, 153, 23);


		providerDropdown.setBounds(12, 677, 153, 23); 

//		chckbx_API_key.addItemListener(new ItemListener() {
//			/**
//			 * @param e The `ItemEvent e` parameter represents the event object that describes the state change of an item, typically a checkbox.
//			 * @summary The `itemStateChanged` method toggles the visibility of a panel and manages checkbox state based on item selection.
//			 */
//			public void itemStateChanged(ItemEvent e) {
//				if (e.getStateChange() == ItemEvent.SELECTED) {
//					Key_panel.setVisible(true);
//					// Disable checkbox temporarily while async operation runs
//					chckbx_API_key.setEnabled(false);
//					// Use SwingWorker for blocking operations
//					new KeySetupWorker().execute();
//
//
//				} else {
//					Key_panel.setVisible(false);
//					//		            View_btn_API.setVisible(false);
//					L_panel.revalidate();
//					L_panel.repaint();
//					// Always ensure re-enable just in case
//					chckbx_API_key.setEnabled(true);
//				}
//				//				L_panel.revalidate();
//				//				L_panel.repaint();
//			}
//		});

		providerDropdown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selected = (String) e.getItem();

					if ("Local".equals(selected)) {
						Key_panel.setVisible(false);
						if (L_panel != null) { L_panel.revalidate(); L_panel.repaint(); } // Use 'panel' for Prompt_preview_pannel
					} else {
						Key_panel.setVisible(true);
						providerDropdown.setEnabled(false);
						new KeySetupWorker().execute();
					}
				}

			}
		});


		JButton btn_SendPrompt = new JButton("Send Prompt");
		btn_SendPrompt.setBounds(573, 679, 175, 21);
		btn_SendPrompt.addActionListener(new ActionListener() {
			/**
			 * @param e The parameter `e` of type `ActionEvent` in the method `actionPerformed` represents the event object that holds information about the action performed by a user, such as a button click.
			 * @summary The `actionPerformed` method processes user input from a text area, executing a background task to generate and handle responses based on the selected options.
			 */
			public void actionPerformed(ActionEvent e) {
				String edited_prompt = textArea_prompt.getText();
				// Create a SwingWorker to execute the task in the background
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					/**
					 * @return The `doInBackground` method is designed to perform a long-running background task, specifically handling the generation of responses from an external API. The primary purpose of this method is to execute the response generation process asynchronously and manage any potential errors or exceptions that may arise during this operation.

The return value of this method is `Void`, which indicates that it does not produce any meaningful output directly. Instead, its role is to carry out the background task and handle the outcome internally by either generating a response or logging an error message if the API call fails.

In summary, the return value of `Void` signifies that the primary objective of this method is to perform an asynchronous operation rather than to return a specific result.
					 * @summary The `doInBackground` method executes a long-running background task to generate and process a response from an external server based on user selections, handling API key usage and measuring the time taken for the operation.
					 */
					@Override
					protected Void doInBackground() throws Exception {  
		                long startTime = System.currentTimeMillis();
		                String response = "";
		                
		                String selectedProvider = (String) providerDropdown.getSelectedItem();
		                boolean using_API_key = !"Local".equals(selectedProvider);
		                
		                String apiKey = apiKeyManager.currentApiKey;
		                String sys_msg = apiKeyManager.SysMSGsys;
		                
		                // Note: Ensure RequestSender constructor is updated to accept 'selectedProvider' as the first argument
		                RequestSender Rs = new RequestSender(selectedProvider, using_API_key, apiKey, sys_msg, ModelName);

		                if (WithRAG.isSelected()) {				
		                    response = Rs.generateResponse_With_RAG(edited_prompt, promptpropertyMap);
		                } else if (WORAG.isSelected()) {
		                    response = Rs.generateResponse_Without_RAG(edited_prompt, promptpropertyMap);							
		                } else {
		                    response = Rs.generateResponse_Without_RAG(edited_prompt, promptpropertyMap);				
		                }

		                if (response.contains("Failed with status code ") ||
		                        response.contains("Failed to connect to the server.")) {
		                    return null;
		                }

		                long endTime = System.currentTimeMillis();
		                long elapsedTime = endTime - startTime;
		                
		                long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
		                long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
		                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;

		                String timeTaken = String.format("Time taken: %02d hours %02d minutes %02d seconds", hours, minutes, seconds);

		                // Note: Ensure ResponseCollector references the correct property maps
		                ResponseCollector.parseResponse(response, timeTaken, promptpropertyMap, propertyMapUpdated, selectedfunction);

						return null;
					}
				};

				// Start the background task
				worker.execute();
			}
		});



		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(L_panel, GroupLayout.PREFERRED_SIZE, 756, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(14, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(L_panel, GroupLayout.PREFERRED_SIZE, 711, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(38, Short.MAX_VALUE))
				);
		L_panel.setLayout(null);
		L_panel.add(RAG_panel);
		L_panel.add(lbl_Description);
		L_panel.add(textFieldDescription);
		L_panel.add(subpanel_1);
		L_panel.add(subpanel_2);
		L_panel.add(ExtraInfoPannel_1);
		ExtraInfoPannel_1.setLayout(null);
		ExtraInfoPannel_1.add(chckbxUpperLevelFunctions_1);
		ExtraInfoPannel_1.add(chckbxLowerLevelFunctions_1);
		ExtraInfoPannel_1.add(lblNewLabel_1);
//		L_panel.add(chckbx_API_key);

		L_panel.add(providerDropdown);
		L_panel.add(Key_panel);


		L_panel.add(btn_SendPrompt);
		L_panel.add(btn_GeneratePrompt);
		L_panel.add(textArea_prompt);

		L_panel.add(Func_ID_Panel);



		setLayout(groupLayout);

	}

	/**
	 * SwingWorker to handle the blocking file I/O and dialogs off the EDT.
	 */
	private class KeySetupWorker extends SwingWorker<Void, Void> {
		private boolean setupSuccessful = false;

		/**
		 * @return The return value for the `doInBackground` method is `Void`, indicating that this method does not produce a meaningful result. Its purpose is to initialize an API key manager and validate certain paths in the background, without returning any data to the caller.
		 * @summary The `doInBackground` method initializes and loads the API key, then validates necessary file paths in the background.
		 */
		/**
		 * @author kata_du
		 * @version 1.0.0
		 * @date 2025-12-18
		 * @summary The `KeySetupWorker` class is responsible for setting up key operations asynchronously, with the `doInBackground()` method performing background tasks and the `done()` method handling completion of these tasks.
		 */
		@Override
		protected Void doInBackground() throws Exception {
			// This runs in a background 
			/**
			 * @return The `doInBackground` method returns a `Void`, which signifies that it does not produce any meaningful result. The primary purpose of this method is to perform background operations essential for initializing the API key manager and validating certain paths, as indicated by its internal logic.
			 * @summary The primary responsibility of the `doInBackground` method is to initialize and load an API key from a specified file, then check if certain paths are valid after this initialization process.
			 */
			String selectedProvider = (String) providerDropdown.getSelectedItem(); // Fetch the dropdown value
			apiKeyManager.initializeAndLoadKey(SYS_MSG_FILE_NAME, selectedProvider);

			setupSuccessful = (apiKeyManager.targetDirPath != null && apiKeyManager.ApiKeyFilePath != null);
			return null;
		}

		/**
		 * @summary The `done` method processes the completion of an asynchronous task, handling any exceptions that occurred during its execution and updating the UI components accordingly.
		 */
		/**
		 * @summary The `done` method handles the completion of an asynchronous task by managing UI updates based on the result, displaying error messages if exceptions occur, and ensuring the proper state of components.
		 */
		@Override
		protected void done() {
			try {
				get(); 
			} catch (InterruptedException | ExecutionException ex) {
				JOptionPane.showMessageDialog(null,
						"An error occurred during API key setup: " + ex.getMessage(),
						"Setup Error", JOptionPane.ERROR_MESSAGE);
				setupSuccessful = false; 
			} finally {
				providerDropdown.setEnabled(true);

				if (setupSuccessful) {
					if (apiKeyManager.currentApiKey == null || apiKeyManager.currentApiKey.isEmpty()) {
						API_key.setEditable(true);
						API_key.setVisible(true);
						API_key.setText(""); 
						View_btn_API.setSelected(true); 
						View_btn_API.setText("Hide");
						API_key.requestFocusInWindow(); 
					} else {
						API_key.setEditable(false);
						API_key.setVisible(false); // Can be removed if you want the field visible but masked
						View_btn_API.setSelected(false);
						View_btn_API.setText("View");

						String selectedProvider = (String) providerDropdown.getSelectedItem();

						if ("Helmholtz Blablador".equals(selectedProvider)) {
							BlabladorAPIJava blabladorAPI = new BlabladorAPIJava();
							ModelName = blabladorAPI.getModelName(apiKeyManager.currentApiKey);
						} else {
							// Safe to call the DLR endpoint for Ollama (DLR Server)
							ModelName = DLRO.getModelName(apiKeyManager.currentApiKey);
						}
					}
				} else {
					providerDropdown.setSelectedItem("Local");
					Key_panel.setVisible(false);
				}

				// In Prompt_preview_pannel use: panel.revalidate(); panel.repaint();
				if (L_panel != null) {
					L_panel.revalidate();
					L_panel.repaint();
				}
			}
		}
	}
}

