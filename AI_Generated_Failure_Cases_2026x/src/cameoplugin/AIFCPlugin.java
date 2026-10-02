package cameoplugin;

import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.actions.ActionsID;
import com.nomagic.magicdraw.plugins.Plugin;
import com.nomagic.magicdraw.uml.DiagramType;

import customCameoActionConfigs.BrowserConfig;
import customCameoActionConfigs.DiagramConfig;
import customCameoActionConfigs.InsertAfterConfigurator;
import customCameoActions.BA_For_Auto_FC_Prompt_Creator;
import customCameoActions.BrowserActionForEmbedding;
import customCameoActions.DA_For_Auto_FC_Prompt_Creator;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `AIFCPlugin` class is responsible for initializing and managing user interface actions and functionalities within the OSIRIS plugin, ensuring compatibility with the host environment.
 */
public class AIFCPlugin extends Plugin {
	

/**
 * @summary The `init` method initializes the plugin by creating necessary actions and setting up an automatic failure condition prompt window.
 */
	@Override

	public void init() {
		createEmbeddModelAction();
//		createTrailBrowserAction();
		Auto_FC_Propmt_Wimdow();
	}
	

/**
 * @summary The `createEmbeddModelAction` method creates a new embedding model action and configures it for use within the system.
 */
	private void createEmbeddModelAction() {
		var action = new BrowserActionForEmbedding("Embedd_Model1", "Embedd Model");
		var browserConfiguration = new BrowserConfig(action);
		ActionsConfiguratorsManager.getInstance().addContainmentBrowserContextConfigurator(browserConfiguration);
	}

/**
 * @summary The `createTrailBrowserAction` method creates a new trail action, configures it for use in the containment browser, and adds it to the actions configurator manager.
 */
//	private void createTrailBrowserAction() {
//		var action = new TrailActionNew("Trail_names1", "Testing Action1");
//		var browserConfiguration = new BrowserConfig(action);
//		ActionsConfiguratorsManager.getInstance().addContainmentBrowserContextConfigurator(browserConfiguration);
//	}
	

/**
 * @summary The `Auto_FC_Propmt_Wimdow` method configures and registers actions for creating failure condition prompts within the Cameo Systems Modeler environment.
 */
	private void Auto_FC_Propmt_Wimdow() {
		var actionBA = new BA_For_Auto_FC_Prompt_Creator("Auto_FCG_window_bA", "Failure Condition Creator");
		var actionDA = new DA_For_Auto_FC_Prompt_Creator("Auto_FCG_window_DA", "Failure Condition Creator");
		var brConfiguration1 = new InsertAfterConfigurator(actionBA, ActionsID.GOTO);
		var Configuration = new DiagramConfig(actionDA);
		ActionsConfiguratorsManager.getInstance().addContainmentBrowserContextConfigurator(brConfiguration1);
		ActionsConfiguratorsManager.getInstance().addDiagramContextConfigurator(DiagramType.UML_ANY_DIAGRAM, Configuration );	
		

	}
	

/**
 * @return The `close` method in OSIRIS, when invoked, signals whether the system has successfully terminated its operations and cleaned up resources effectively. The return value, which is a boolean, indicates if the process was completed without any issues; returning `true` signifies successful closure, while `false` would suggest that there were complications during the closing procedure.
 * @summary The `close` method is responsible for closing resources associated with this object and returning a boolean indicating whether it was successful.
 */
	@Override

	public boolean close() {
		return true;
	}

/**
 * @return The return value of the `isSupported` method indicates whether the feature or functionality it represents is supported in the current context. It returns a boolean: `true` if the feature is supported, and `false` otherwise.
 * @summary The `isSupported` method verifies if a certain feature or capability is available in the current context, returning `true` to indicate that it is supported.
 */
	@Override

	public boolean isSupported() {
		return true;
	}

}
