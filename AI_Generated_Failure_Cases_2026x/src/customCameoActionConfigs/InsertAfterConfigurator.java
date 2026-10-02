package customCameoActionConfigs;

import com.nomagic.actions.AMConfigurator;
import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.BrowserContextAMConfigurator;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.ui.browser.Tree;

import java.util.List;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `InsertAfterConfigurator` class is responsible for creating and configuring insertion points for actions within a hierarchical structure, using specified IDs or categories to determine the placement order.
 */
public class InsertAfterConfigurator implements BrowserContextAMConfigurator {

	private final NMAction actionToInsert;
	private final String insertAfterThisID;

/**
 * @param actionToInsert The `actionToInsert` parameter specifies the MDAction object to be inserted after another action identified by its ID.
 * @param insertAfterThisID The `insertAfterThisID` parameter specifies the unique identifier of the existing MDAction after which the new action (`actionToInsert`) will be inserted.
 * @return The `InsertAfterConfigurator` method returns an instance of the `InsertAfterConfigurator` class, which is configured to insert a specified MDAction after another element identified by its ID.
 * @summary The `InsertAfterConfigurator` method is responsible for configuring the insertion of a specified MDAction after another element identified by its unique ID.
 */
	public InsertAfterConfigurator(MDAction actionToInsert, String insertAfterThisID) {
		this.actionToInsert = actionToInsert;
		this.insertAfterThisID = insertAfterThisID;
	}

/**
 * @param category The `category` parameter in the method signature of `InsertAfterConfigurator` determines the type of action to be inserted.
 * @param ID The `ID` parameter in the `InsertAfterConfigurator` method is a unique identifier string used to specify where the new action should be inserted relative to an existing action.
 * @return The `InsertAfterConfigurator` method returns an instance of the `InsertAfterConfigurator` class that is configured to associate a specific action (`NMAction`) with a given identifier (`String ID`). This object can be used to specify the insertion point for new actions in the system model, ensuring they are placed after the identified element.
 * @summary The `InsertAfterConfigurator` method is responsible for initializing an instance of the configurator with a specified action and target insertion position.
 */
	public InsertAfterConfigurator(NMAction category, String ID) {
		// TODO Auto-generated constructor stub
		this.actionToInsert = category;
		this.insertAfterThisID = ID;
	}

/**
 * @return The `getPriority` method returns an integer representing the priority level of a task or process, specifically returning the constant defined for low priority in the configuration (`AMConfigurator.LOW_PRIORITY`).
 * @summary The `getPriority` method returns the priority level of the aircraft component as defined in the safety configuration.
 */
	@Override
	public int getPriority() {
		return AMConfigurator.LOW_PRIORITY;
	}

/**
 * @param manager The `manager` parameter of type `ActionsManager` is used to search for a specific action within the context of an actions category and facilitate the insertion of a new action.
 * @param arg1 The `arg1` parameter of type `Tree` in the `configure` method is not used within the method's implementation.
 * @summary The `configure` method inserts a specified action after another identified action within an ActionsManager and its associated category.
 */
	@Override
	public void configure(ActionsManager manager, Tree arg1) {
		// searching for action after which insert should be done.
		final NMAction found = manager.getActionFor(insertAfterThisID);
		if (found != null)
		{
			// action found, inserting
			final ActionsCategory category = (ActionsCategory) manager.getActionParent(found);
			if (category != null)
			{
				final List<NMAction> actionsInCategory = category.getActions();
				final int indexOfFound = actionsInCategory.indexOf(found);
				actionsInCategory.add(indexOfFound + 1, actionToInsert);
				category.setActions(actionsInCategory);
			}
		}
	}
}
