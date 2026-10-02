package customCameoActionConfigs;

import javax.annotation.CheckForNull;

import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.ui.actions.BaseDiagramContextAMConfigurator;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;


/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `DiagramConfig` class is responsible for configuring and managing the presentation of diagrams within a model by associating priorities with specific actions and setting up the configuration through the provided managers and elements.
 */
public class DiagramConfig extends BaseDiagramContextAMConfigurator {

	private NMAction Action ;



/**
 * @param category The `category` parameter defines the specific action type that the diagram configuration is associated with.
 * @return The `DiagramConfig` method returns an instance of `DiagramConfig` configured with a specific `NMAction` category. This returned object encapsulates the configuration settings for diagrams within OSIRIS, tailored to the provided action category, enabling consistent and standardized diagram generation during functional hazard assessment (FHA).
 * @summary The `DiagramConfig` method is responsible for initializing an instance of the diagram configuration with a specified action category, which likely pertains to the nature or type of actions represented in the diagrams.
 */
	public DiagramConfig(NMAction category) {
		// TODO Auto-generated constructor stub
		this.Action = category;
	}

/**
 * @return The `getPriority()` method returns an integer representing the priority level of the task or process. In this context, it specifically indicates a high-priority assignment by returning the constant `HIGH_PRIORITY`.
 * @summary This method returns a constant high priority value used by OSIRIS to prioritize tasks or events within the Functional Hazard Assessment process.
 */
	@Override
	public int getPriority() {
		return HIGH_PRIORITY;
	}

/**
 * @param actionsManager The `actionsManager` parameter is an instance of `ActionsManager` used to register and manage the custom actions for a diagram context.
 * @param Diagram The `Diagram` parameter in the `configure` method represents the graphical diagram element within which context-specific actions are being configured for the OSIRIS Java plugin.
 * @param selected The `selected` parameter represents the array of currently selected presentation elements in the diagram.
 * @param requester The `requester` parameter in the `configure` method represents an optional `PresentationElement` that initiated the configuration action, allowing for context-specific customization.
 * @summary The `configure` method configures the action manager by adding a new action category with the specified action.
 */
	@Override
	public void configure(ActionsManager actionsManager, DiagramPresentationElement Diagram, PresentationElement[] selected,
			@CheckForNull PresentationElement requester) {
        ActionsCategory category = new ActionsCategory();
        category.addAction(Action);
        actionsManager.addCategory(category);
     }

}
