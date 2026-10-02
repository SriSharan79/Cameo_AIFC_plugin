package customCameoActions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;

import creationOfSafetyVP.NameDerivator;
import elementDataExtraction.StereotypeSearch;
import elementDataExtraction.ElementDataProcessor;
import restAPI.ToEmbeddModel;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `BrowserActionForEmbedding` class is responsible for managing browser-related actions within the OSIRIS plugin, facilitating user interactions and state updates in a SysML model context.
 */
public class BrowserActionForEmbedding extends MDAction {

	private static final long serialVersionUID = 8384887777768954522L;

	StereotypeSearch StereotypeChecker = new StereotypeSearch();

	NameDerivator ND = new NameDerivator();
	ElementDataProcessor EDP = new ElementDataProcessor();


/**
 * @param id The `id` parameter represents the unique identifier for the BrowserActionForEmbedding instance.
 * @param name The parameter "name" of type String in the method BrowserActionForEmbedding(String id, String name) represents the display or descriptive name for the action being created.
 * @return The `BrowserActionForEmbedding` method returns an instance of the `BrowserActionForEmbedding` class, which represents a specific action related to embedding functionality within a browser context.
 * @summary The `BrowserActionForEmbedding` method is responsible for initializing an action with a specified ID and name in the context of embedding.
 */
	public BrowserActionForEmbedding(String id, String name) {
		super(id, name, null, null);
	}
	
/**
 * @param actionEvent `ActionEvent actionEvent`: The event that triggers this method, containing information about the event source and type.
 * @summary The `actionPerformed` method handles background processing of project models by extracting data, embedding it using a specified method, and displaying execution status and elapsed time in the GUI log.
 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
	    Project project = Application.getInstance().getProject();

	    // Define and execute a SwingWorker for background processing
	    SwingWorker<Void, Void> worker = new SwingWorker<>() {

/**
 * @return The `doInBackground` method is a background task that processes all top-level models in the project, extracts data from each model recursively, and then executes an embedding process. The return value of this method is always `null`, indicating that the primary purpose of this method is to perform its operations asynchronously without returning any specific result.
 * @summary The `doInBackground` method retrieves and processes all top-level models from a project to generate an embedding message, which it then logs along with the execution time.
 */
	        @Override
	        protected Void doInBackground() {
	            // Retrieve all top-level models in the project
	            List<Package> models = project.getModels();
	            List<String> StringsForEmbedding = new ArrayList<>();

	            // Process each model to extract data recursively
	            for (Package model : models) {
	                Element ModelEl = (Element) model;
	                StringsForEmbedding.addAll(EDP.getElementData(ModelEl));
	            }

	            if (StringsForEmbedding == null || StringsForEmbedding.isEmpty()) {
	                Application.getInstance().getGUILog().showError("No data to write");
	                return null;
	            }

	            long startTime = System.currentTimeMillis();
	            String Message = ToEmbeddModel.execute(StringsForEmbedding);
	            long endTime = System.currentTimeMillis();

	            // Calculate elapsed time
	            long elapsedTime = endTime - startTime;
	            long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
	            long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
	            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;

	            String timeTaken = String.format("Time taken: %02d hours %02d minutes %02d seconds", hours, minutes, seconds);

	            // Show results in GUI log
	            Application.getInstance().getGUILog().showMessage("Status: " + Message + "\n \n" + timeTaken);
	            return null;
	        }

/**
 * @summary The `done` method completes background tasks and handles any potential errors that occur during execution.
 */
	        @Override
	        protected void done() {
	            try {
	                // Ensure all background tasks are completed
	                get(); // Can be used to handle exceptions, if any
	            } catch (Exception e) {
	                Application.getInstance().getGUILog().showError("An error occurred: " + e.getMessage());
	            }
	        }
	    };

	    // Execute the SwingWorker
	    worker.execute();
	}

//	@Override
/**
 * @param actionEvent The `actionEvent` parameter represents the event that triggered the `actionPerformed` method, encapsulating details such as source and command.
 * @summary The `actionPerformed` method handles the extraction and embedding of model data from a project, logging the status and execution time.
 */
//	public void actionPerformed(ActionEvent actionEvent) {
//		Project project = Application.getInstance().getProject();
//
//		// Retrieve all top-level models in the project
//		List<Package> models = project.getModels();
//		List<String> StringsForEmbedding = new ArrayList<>();
//
//		// Process each model to extract data recursively
//		for (Package model : models) {
//			Element ModelEl = (Element) model;
//			StringsForEmbedding.addAll(EDP.getElementData(ModelEl));
//		}
//		if (StringsForEmbedding == null || StringsForEmbedding.isEmpty()) {
//		    Application.getInstance().getGUILog().showError("No data to write");
//		    return;
//		}
//
//		long startTime = System.currentTimeMillis();
//		String Message = ToEmbeddModel.execute(StringsForEmbedding);
//		// Calculate elapsed time
//		long endTime = System.currentTimeMillis();
//		long elapsedTime = endTime - startTime;
//        // Convert milliseconds to hours, minutes, and seconds
//        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
//		
//		String timeTaken = String.format("Time taken: %02d hours %02d minutes %02d seconds", hours, minutes, seconds);
//		
//
//	    Application.getInstance().getGUILog().showMessage("Status: "+ Message +"\n \n"+ timeTaken);
//
//	}


/**
 * @summary The `updateState` method is responsible for updating the state of the component by setting it to an enabled state.
 */
	@Override
	public void updateState() {
		boolean enable = true;


		setEnabled(enable);
	}
}
