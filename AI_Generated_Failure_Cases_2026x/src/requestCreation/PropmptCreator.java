package requestCreation ;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element ;

import creationOfSafetyVP.NameDerivator ;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `PropmptCreator` class is responsible for generating prompts based on provided identifiers, descriptions, properties, and selected elements.
 */
public class PropmptCreator {

	private String functionName="" ;
	private String description="" ;
	private String id="" ;
	private List<String> failureTypeList= new ArrayList<>() ;
	private List<String> phaseList= new ArrayList<>() ;
	private List<String> systemList= new ArrayList<>() ;
	private List<String> input= new ArrayList<>() ;
	private List<String> output= new ArrayList<>() ;
	private List<String> Requirement= new ArrayList<>() ;
	private List<String> input_ty= new ArrayList<>() ;
	private List<String> output_ty= new ArrayList<>() ;
	private List<String> Requirement_txt= new ArrayList<>() ;
	private List<String> HLFlist= new ArrayList<>() ;
	private List<String> LLFlist= new ArrayList<>() ;
	private Map<String, List<String>> localpropertyMap = new HashMap<>() ;

	NameDerivator Name=new NameDerivator() ;
	GlossaryFinder GF= new GlossaryFinder() ;
/**
 * @param id The `id` parameter is a unique identifier for the PromptCreator instance, ensuring it can be distinctly recognized and managed within the OSIRIS plugin.
 * @param description The `description` parameter in the `PropmptCreator` method is a String that provides a textual description of the prompt being created.
 * @param propertyMap The `propertyMap` parameter in the `PropmptCreator` method is a mapping of strings to lists of strings, used to categorize and store various properties such as failure types, phases, systems, requirements, input parameters, output parameters, upper-level functions, and lower-level functions associated with the selected node.
 * @param selectedNode The `selectedNode` parameter represents the specific SysML element within the model that is currently selected for prompt creation.
 * @return The `PropmptCreator` method returns an instance of the `PropmptCreator` class. This instance is initialized with the provided parameters: a unique identifier (`id`), a description, a map containing various properties (like failure types and phases), and a selected node from which the function name is derived. The purpose of this method is to create and configure a new `PropmptCreator` object that will be used for generating and refining failure conditions in an aircraft system's functional hazard assessment.
 * @summary The `PropmptCreator` method initializes an object with properties and context from a SysML model element, setting up various lists for failure types, phases, systems, requirements, input/output parameters, and hierarchical function levels.
 */
	public PropmptCreator(String id, String description, Map<String, List<String>> propertyMap, Element selectedNode) {
		// TODO Auto-generated constructor stub

		this.localpropertyMap = propertyMap ;
		this.functionName = Name.getName(selectedNode) ;
		this.description = description ;
		this.id = id ;
		this.failureTypeList = propertyMap.get("Failure Type") ;
		this.phaseList = propertyMap.get("Phase") ;
		this.systemList = propertyMap.get("System") ;
		this.Requirement = propertyMap.get("Requirements") ;
		this.Requirement_txt = propertyMap.get("RequirementsTexts") ;
		if(propertyMap.containsKey("InputParameters")) {
			this.input = propertyMap.get("InputParameters") ;
			this.input_ty = propertyMap.get("InputTypes") ;
		}
		if(propertyMap.containsKey("OutputParameters")) {
			this.output = propertyMap.get("OutputParameters") ;
			this.output_ty = propertyMap.get("OutputTypes") ;
		}
		if(propertyMap.containsKey("UpperLevelFunctions")) {
			this.HLFlist = propertyMap.get("UpperLevelFunctions") ;
		}		
		if(propertyMap.containsKey("LowerLevelFunctions")) {
			this.LLFlist = propertyMap.get("LowerLevelFunctions") ;
		}
	}

	public Map<String, List<String>> functionInfo() {
		Map<String, List<String>> propertyMap = new HashMap<>() ;

		// Adding fields to the map
		propertyMap.put("Function", List.of(this.functionName)) ; // Wrapping functionName in a List
		propertyMap.put("Description", List.of(this.description)) ; // Wrapping Description in a List
		propertyMap.put("ID", List.of(this.id)) ; // Wrapping id in a List
		propertyMap.put("Failure Type", this.failureTypeList) ;
		propertyMap.put("Phase", this.phaseList) ;
		propertyMap.put("System", this.systemList) ;
		propertyMap.put("Input Parameters", this.input) ;
		propertyMap.put("Output Parameters", this.output) ;
		propertyMap.put("Input Parameters Types", this.input_ty) ;
		propertyMap.put("Output Parameters Types", this.output_ty) ;
		propertyMap.put("Requirements", this.Requirement) ;
		propertyMap.put("Requirement Text", this.Requirement_txt) ;
		propertyMap.put("UpperLevelFunctions", this.HLFlist) ;
		propertyMap.put("LowerLevelFunctions", this.LLFlist) ;

		return propertyMap ;
	}

/**
 * @return The `generate` method constructs a detailed prompt for identifying failure conditions using Functional Hazard Analysis principles. It returns this assembled string, which includes structured information about the function name, description, responsible systems, phases, failure types, input and output parameters, requirements, higher-level functions, and lower-level functions. The returned string is formatted to facilitate clear understanding and analysis within a safety context.
 * @summary The `generate` method constructs and returns a prompt string for failure condition generation using Functional Hazard Analysis principles, incorporating various system parameters such as function name, description, responsible systems, phases, failure types, input/output parameters, requirements, higher-level functions, and lower-level functions.
 */
	public String generate() {
		StringBuilder promptBuilder = new StringBuilder() ;

		promptBuilder.append("Using Functional Hazard Analysis principles, provide failure conditions for the following information:\n") ;

		// Check each variable and append if it has a value
		if (functionName != null && !functionName.isEmpty()) {
			promptBuilder.append(" Function Name: ").append(functionName).append( " ; \n") ;
		}

		if (description != null && !description.isEmpty()) {
			promptBuilder.append(" Description: ").append(description).append(" ; \n") ;
		}

		if (systemList != null && !systemList.isEmpty()) {
			promptBuilder.append(" System Responsible: ") ;
			for (int i = 0 ; i < systemList.size() ; i++) {
				String sys = systemList.get(i) ;
				promptBuilder.append(sys) ;

				// Check if it's not the last element
				if (i < systemList.size() - 1) {
					promptBuilder.append(" , ") ;
				}
			}
			promptBuilder.append("  ; " + "\n") ;
		}

		if (phaseList != null && !phaseList.isEmpty()) {
			promptBuilder.append(" Phases: ") ;
			for (int i = 0 ; i < phaseList.size() ; i++) {
				String sys = phaseList.get(i) ;
				promptBuilder.append(sys) ;

				// Check if it's not the last element
				if (i < phaseList.size() - 1) {
					promptBuilder.append(" , ") ;
				}
			}
			promptBuilder.append("  ; " + "\n") ;
		}

		if (failureTypeList != null && !failureTypeList.isEmpty()) {
			promptBuilder.append(" Failure Type: ") ;
			for (int i = 0 ; i < failureTypeList.size() ; i++) {
				String sys = failureTypeList.get(i) ;
				promptBuilder.append(sys) ;

				// Check if it's not the last element
				if (i < failureTypeList.size() - 1) {
					promptBuilder.append(" , ") ;
				}
			}
			promptBuilder.append("  ; " + "\n") ;
		}

		if (input != null && !input.isEmpty()) {
			promptBuilder.append(" Input Parameters: ") ;
			for (int i = 0 ; i < input.size() ; i++) {
				if (i < input_ty.size()) { // Check to avoid IndexOutOfBoundsException

					String sys_in = input.get(i) ;
					String sys_ty = input_ty.get(i) ;
					promptBuilder.append(sys_in + " type of ").append(sys_ty).append("\n") ;
				}
			}
		}

		if (output != null && !output.isEmpty()) {
			promptBuilder.append(" Output Parameters: ") ;
			for (int i = 0 ; i < output.size() ; i++) {
				if (i < output_ty.size()) { // Check to avoid IndexOutOfBoundsException					
					String sys_in = output.get(i) ;
					String sys_ty = output_ty.get(i) ;
					promptBuilder.append(sys_in + " type of ").append(sys_ty).append("\n") ;
				}
			}
		}

		if (Requirement != null && !Requirement.isEmpty()) {
			promptBuilder.append(" Requirements: ").append("\n") ;
			for (int i = 0 ; i < Requirement.size() ; i++) {
				if (i < Requirement_txt.size()) { // Check to avoid IndexOutOfBoundsException			
					String sys_in = Requirement.get(i) ;
					String sys_ty = Requirement_txt.get(i) ;
					promptBuilder.append(sys_in + " type of ").append(sys_ty).append("\n") ;
//					promptBuilder.append(Requirement.get(i) + " : ").append(Requirement_txt.get(i)).append("\n") ;
				}
			}
		}

		if (HLFlist != null && !HLFlist.isEmpty()) {
			promptBuilder.append(" Contains Higher Level Functions: ").append("\n") ;
			for (int i = 0 ; i < HLFlist.size() ; i++) {
				String key = HLFlist.get(i) ;
				if (key != null && !key.isEmpty() && localpropertyMap.containsKey(key) ) {
					var syslist =localpropertyMap.get(key) ;

					promptBuilder.append(key) ;

					if(!syslist.isEmpty()) {
						promptBuilder.append(" in context of: ").append(localpropertyMap.get(key).toString()).append("\n") ;
					}else {
						promptBuilder.append("\n") ;
					}
				}
			}
		}

		if (LLFlist != null && !LLFlist.isEmpty()) {
			promptBuilder.append(" Contains Lower Level Functions: ").append("\n") ;
			for (int i = 0 ; i < LLFlist.size() ; i++) {
				String key = LLFlist.get(i) ;
				if (key != null && !key.isEmpty() && localpropertyMap.containsKey(key) ) {
					var syslist =localpropertyMap.get(key) ;

					promptBuilder.append(key) ;

					if(!syslist.isEmpty()) {
						promptBuilder.append(" in context of: ").append(localpropertyMap.get(key).toString()).append("  ;\n") ;
					}else {
						promptBuilder.append(" ;\n") ;
					}
				}
			}
		}
		
		String Prompt= GF.updateWithGlossary(promptBuilder.toString()) ;

		return Prompt ;
	}





}
