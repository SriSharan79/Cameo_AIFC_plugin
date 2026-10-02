package customCameoActionConfigs;

import com.nomagic.actions.ActionsManager;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.BrowserContextAMConfigurator;
import com.nomagic.magicdraw.actions.MDActionsCategory;
import com.nomagic.magicdraw.ui.browser.Tree;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The BrowserConfig class is responsible for configuring the browser behavior based on specific actions and managing the priority of these configurations.
 */
public class BrowserConfig implements BrowserContextAMConfigurator {

    private final NMAction browserAction;
	
    MDActionsCategory category = new MDActionsCategory("", "");

    

/**
 * @param action This sentence provides a concise, single-sentence description for the Java parameter:

action is an instance of NMAction that defines the specific behavior or operation to be performed by the BrowserConfig object.
 * @return The return value of the method `BrowserConfig` is an instance of `BrowserConfig`. This object encapsulates configurations specific to a given `NMAction`, facilitating the management and retrieval of settings necessary for executing actions in OSIRIS's browser context.
 * @summary The `BrowserConfig` constructor initializes an instance with the specified NMAction, preparing it for configuration within the OSIRIS plugin context.
 */
	public BrowserConfig(NMAction action) {
		this.browserAction = action;
		// TODO Auto-generated constructor stub
	}

/**
 * @param actionsManager The `actionsManager` parameter is an instance of the `ActionsManager` class used to register a new category of actions within the system.
 * @param tree The parameter `tree` is a data structure representing the hierarchical organization of elements in the SysML model used by OSIRIS for context-specific safety assessment.
 * @summary This method configures the actions manager by adding a specified browser action to an existing category and then adds this category to the actions manager.
 */
	@Override
    public void configure(ActionsManager actionsManager, Tree tree) {

        category.addAction(browserAction);
        actionsManager.addCategory(category);
    }

/**
 * @return The `getPriority` method returns an integer representing the priority level of the component, where it indicates that this component has a high priority.
 * @summary The `getPriority` method returns the priority level of the task, specifically indicating that it has high priority.
 */
    @Override
    public int getPriority() {
        return HIGH_PRIORITY;
    }

}
