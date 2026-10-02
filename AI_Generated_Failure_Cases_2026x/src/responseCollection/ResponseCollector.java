package responseCollection;

import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

import cameoUIDialogs.PGDialogs.OSIRISFrame;
import cameoUIDialogs.RGDialogs.For_AC_Functions_AutoFCGResponseDialog;
import cameoUIDialogs.RGDialogs.For_AC_Functions_ManualFCGResponseDialog;
import cameoUIDialogs.RGDialogs.For_Sys_Func_with_HLS_AutoFCGResponseDialog;
import cameoUIDialogs.RGDialogs.For_Sys_Func_with_HLS_ManualFCGResponseDialog;
import dataloggers.BackgroundRunner;
import elementDataExtraction.IdGenerator;
import responseParser.Main_ResponseParser_For_AC_Functions;
import responseParser.Main_ResponseParser_with_HLS_Sys_Func;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ResponseCollector` class is responsible for handling and processing responses from safety assessment tasks in the OSIRIS plugin.
 */
public class ResponseCollector {

	// Declare static fields to hold dialog instances
	// These will be null initially and populated on the first call
	private static For_Sys_Func_with_HLS_AutoFCGResponseDialog autoHLSResponseDialog = null;
	private static For_Sys_Func_with_HLS_ManualFCGResponseDialog manualHLSResponseDialog = null;
	private static For_AC_Functions_AutoFCGResponseDialog autoACResponseDialog = null;
	private static For_AC_Functions_ManualFCGResponseDialog manualACResponseDialog = null;      
	static String FC_ID_Perfix;
	static String Function_ID;
	static int FC_ID_Num=0;
	static Map<String, Map<String, List<String>>> formattedResponse;
	static IdGenerator IDG = new IdGenerator();

	private static final BackgroundRunner BR = new BackgroundRunner();

	static Frame parent = new OSIRISFrame();

	// Helper method to dispose and nullify a dialog if it's visible, for cases
	// where you might switch dialog types and want to ensure old ones are gone.
	// This is optional but good for robustness if your logic can switch between auto/manual
/**
 * @param dialog The `dialog` parameter of type `JDialog` represents the dialog window that will be disposed and set to `null`.
 * @summary The `disposeAndNullify` method safely disposes of a given `JDialog`, ensuring it is no longer referenced by nullifying it.
 */
	private static void disposeAndNullify(JDialog dialog) {
		if (dialog != null) {
			dialog.dispose();
		}
	}

/**
 * @param response The `response` parameter in the method `parseResponse` is a string that contains the text output from an external process or system, which needs to be parsed and processed according to its content.
 * @param timeTaken The `timeTaken` parameter is a string representing the duration it took to process the response.
 * @param PromptPropertyMap `PromptPropertyMap` is a map containing the mapping of specific keywords to their corresponding lists of strings extracted from the safety context and SysML models used for generating failure conditions in FHA.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter in the method `parseResponse` is a map containing updated properties and their corresponding values to be used for further processing of the response.
 * @param selectedfunction `selectedfunction`: The Element representing the specific function being analyzed for failure conditions within the SysML model.
 * @summary The `parseResponse` method is responsible for parsing a given response string and updating the properties map based on whether the response contains higher-level functions or not.
 */
	public static void parseResponse(String response, String timeTaken, Map<String, List<String>> PromptPropertyMap, Map<String, List<String>> propertyMapUpdated, Element selectedfunction) {
//		List<String> HLFlist = new ArrayList<>();
//
//		if (PromptPropertyMap.containsKey("UpperLevelFunctions")) {
//			HLFlist = PromptPropertyMap.get("UpperLevelFunctions");
//		}
		
		String HL="Higher Level";

		List<String> IDList = PromptPropertyMap.get("ID");

		StringBuilder Func_ID=new StringBuilder();

		for (String n : IDList) {
			Func_ID.append(n);
		}
		Function_ID=Func_ID.toString();
		FC_ID_Perfix =Function_ID+"_FC_";		
		FC_ID_Num= IDG.get_highest_FC_ID_number_for_function(selectedfunction,Function_ID);

		BR.Notifyinbackground("IDxx","Response Parsing Started");

		if (response.toLowerCase().contains(HL.toLowerCase())) {
			Parse_for_HLE(response, timeTaken, propertyMapUpdated, selectedfunction);
		} else {
			Parse_for_Without_HLE(response, timeTaken, propertyMapUpdated, selectedfunction);
		}
	}

/**
 * @param response `response`: The raw output from the functional hazard assessment tool, to be parsed and processed for displaying dialog content.
 * @param timeTaken The `timeTaken` parameter of type `String` in the `Parse_for_HLE` method represents the duration or timestamp associated with the response, used for logging and dialog title updates.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter in the `Parse_for_HLE` method is a map that tracks updated property values, keyed by their identifiers.
 * @param selectedfunction The `selectedfunction` parameter represents the specific SysML element corresponding to a function whose failure conditions are being processed.
 * @summary The `Parse_for_HLE` method is responsible for parsing a response and managing dialogs based on the presence of formatted content.
 */
	private static void Parse_for_HLE(String response, String timeTaken, Map<String, List<String>> propertyMapUpdated,
			Element selectedfunction) {

		Main_ResponseParser_with_HLS_Sys_Func parser = new Main_ResponseParser_with_HLS_Sys_Func();

		formattedResponse = parser.parseFCResponse(response);

		formattedResponse=IDG.update_response_with_new_IDS(formattedResponse,FC_ID_Perfix,FC_ID_Num+1);
//		Application.getInstance().getGUILog().showMessage("With HLE:\n"+formattedResponse.toString());

		if (!formattedResponse.isEmpty()) {
			// Dispose any other potentially open dialogs of different types
			disposeAndNullify(manualHLSResponseDialog);
			manualHLSResponseDialog = null; // Important to nullify after dispose

			disposeAndNullify(autoACResponseDialog);
			autoACResponseDialog = null;

			disposeAndNullify(manualACResponseDialog);
			manualACResponseDialog = null;

			if (autoHLSResponseDialog == null) {
				// Create dialog for the first time
				autoHLSResponseDialog = new For_Sys_Func_with_HLS_AutoFCGResponseDialog(parent, formattedResponse, propertyMapUpdated, selectedfunction, response,timeTaken);
//				autoHLSResponseDialog.setTitle("Response Received - " + timeTaken);
			} else {
				// Dialog already exists, update its content (assuming your dialog class has an update method)
				// You will NEED to add a method like 'updateContent' to your dialog classes
				autoHLSResponseDialog.updateContent(formattedResponse, propertyMapUpdated, selectedfunction, response,timeTaken);
//				autoHLSResponseDialog.setTitle("Response Received - " + timeTaken); // Update title as well
			}

			autoHLSResponseDialog.setVisible(true);
/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `has` class is responsible for parsing the response of an operation that lacks HLE (Human Language Equivalent) and updating a property map with relevant data for a selected function.
 */
			autoHLSResponseDialog.toFront(); // Bring to front if already visible

		} else {
			// Dispose any other potentially open dialogs of different types
			disposeAndNullify(autoHLSResponseDialog);
			autoHLSResponseDialog = null;

			disposeAndNullify(autoACResponseDialog);
			autoACResponseDialog = null;

			disposeAndNullify(manualACResponseDialog);
			manualACResponseDialog = null;

			if (manualHLSResponseDialog == null) {
				// Create dialog for the first time
				manualHLSResponseDialog = new For_Sys_Func_with_HLS_ManualFCGResponseDialog(parent, response, propertyMapUpdated, selectedfunction,timeTaken);
//				manualHLSResponseDialog.setTitle("Response Received - " + timeTaken);
			} else {
				//                // Dialog already exists, update its content
				//                manualHLSResponseDialog.updateContent(response, propertyMapUpdated, selectedfunction);
				//                manualHLSResponseDialog.setTitle("Response Received - " + timeTaken);
			}

			manualHLSResponseDialog.setVisible(true);
			manualHLSResponseDialog.toFront();
		}
	}

/**
 * @param response The `response` parameter is a raw string response from the function call, which will be parsed and displayed in appropriate dialogs based on its content.
 * @param timeTaken The `timeTaken` parameter is a `String` that represents the duration or timestamp of the response processing, used for displaying context-specific information in the dialog.
 * @param propertyMapUpdated The `propertyMapUpdated` parameter in the `Parse_for_Without_HLE` method is a map that tracks the updated properties corresponding to failure conditions, used for initializing or updating the response dialogs based on the received SysML model data.
 * @param selectedfunction The `selectedfunction` parameter represents the SysML element corresponding to the function currently being analyzed in the context of the Functional Hazard Assessment (FHA) process.
 * @summary The `Parse_for_Without_HLE` method processes the response for aircraft functions without HLE, updates the dialogs, and displays the relevant information to the user.
 */
/**
 * @param response The `response` parameter is a String containing the raw output from the system, which needs to be parsed and displayed in the appropriate dialog.
 * @param timeTaken A string representing the time taken to process the function or command, used for logging and dialog display purposes.
 * @param propertyMapUpdated A map that holds the updated properties used to manage and display failure condition responses in the dialog.
 * @param selectedfunction The `selectedfunction` parameter of type `Element` in the method `Parse_for_Without_HLE` represents the specific SysML element corresponding to the aircraft function being analyzed and updated with parsed failure condition data.
 * @summary The method `Parse_for_Without_HLE` is responsible for processing a response string to generate and display dialogs containing the parsed failure conditions for aircraft functions, handling both automated and manual responses based on whether the formatted response is empty or not.
 */
	private static void Parse_for_Without_HLE(String response, String timeTaken, Map<String, List<String>> propertyMapUpdated, Element selectedfunction) {

		Main_ResponseParser_For_AC_Functions parser = new Main_ResponseParser_For_AC_Functions();
		formattedResponse = parser.parseFCResponse(response);


		formattedResponse=IDG.update_response_with_new_IDS(formattedResponse,FC_ID_Perfix,FC_ID_Num+1);

//		Application.getInstance().getGUILog().showMessage("Without HLE:\n"+formattedResponse.toString());

		if (!formattedResponse.isEmpty()) {
			// Dispose any other potentially open dialogs of different types
			disposeAndNullify(autoHLSResponseDialog);
			autoHLSResponseDialog = null;

			disposeAndNullify(manualHLSResponseDialog);
			manualHLSResponseDialog = null;

			disposeAndNullify(manualACResponseDialog);
			manualACResponseDialog = null;

			if (autoACResponseDialog == null) {
				// Create dialog for the first time
				autoACResponseDialog = new For_AC_Functions_AutoFCGResponseDialog(parent, formattedResponse, propertyMapUpdated, selectedfunction, response,timeTaken);
//				autoACResponseDialog.setTitle("Response Received - " + timeTaken);
			} else {
				// Dialog already exists, update its content
				autoACResponseDialog.updateContent(formattedResponse, propertyMapUpdated, selectedfunction, response,timeTaken);
//				autoACResponseDialog.setTitle("Response Received - " + timeTaken);
			}

			autoACResponseDialog.setVisible(true);
			autoACResponseDialog.toFront();

		} else {
			// Dispose any other potentially open dialogs of different types
			disposeAndNullify(autoHLSResponseDialog);
			autoHLSResponseDialog = null;

			disposeAndNullify(manualHLSResponseDialog);
			manualHLSResponseDialog = null;

			disposeAndNullify(autoACResponseDialog);
			autoACResponseDialog = null;

			if (manualACResponseDialog == null) {
				// Create dialog for the first time
				manualACResponseDialog = new For_AC_Functions_ManualFCGResponseDialog(parent, response, propertyMapUpdated, selectedfunction,timeTaken);
//				manualACResponseDialog.setTitle("Response Received - " + timeTaken);
			} else {
				// Dialog already exists, update its content
				//                manualACResponseDialog.updateContent(response, propertyMapUpdated, selectedfunction);
				//                manualACResponseDialog.setTitle("Response Received - " + timeTaken);
			}

			manualACResponseDialog.setVisible(true);
			manualACResponseDialog.toFront();
		}
	}
}
