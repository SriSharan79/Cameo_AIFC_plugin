package customCameoActions;

import java.awt.event.ActionEvent;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.ui.browser.actions.DefaultBrowserAction;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `TrailActionNew` class is responsible for creating instances of new trail actions with specified IDs and names, and handling the execution of these actions upon an event occurrence.
 */
public class TrailActionNew extends DefaultBrowserAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1080447125756511088L;
	//	Project project = Application.getInstance().getProject();

	int i = 0;

/**
 * @param id The parameter `id` of type `String` in the method `TrailActionNew` is a unique identifier for the trail action.
 * @param name The `name` parameter is the descriptive identifier for the new trail action.
 * @return The return value of `TrailActionNew` is an instance of the `TrailActionNew` class created with a specified ID and name.
 * @summary The `TrailActionNew` method is responsible for creating a new instance of the `TrailActionNew` class with specified ID and name.
 */
	public TrailActionNew(String id, String name) {
		super(id, name, null, null);
		// TODO Auto-generated constructor stub
	}

/**
 * @param actionEvent The `actionEvent` parameter in the `actionPerformed` method represents the event object that encapsulates details about the action performed by the user.
 * @summary The `actionPerformed` method handles the user action events, retrieves the selected tree node as an element, and logs its human type and representation text.
 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		var tree = getTree();
//
//		StereotypeSearch StereotypeChecker = new StereotypeSearch();
//		SearchSafetyVP SafetyVP = new SearchSafetyVP();
//		Project project = Application.getInstance().getProject();
//
		var selectedNode = tree.getSelectedNode().getUserObject();
		var el = (Element) selectedNode;
//		if(StereotypeChecker.hasStereotypeBoolean((Element) selectedNode, "Function")) {
//			
//			
//		}
		Application.getInstance().getGUILog().showError("Human type of element : "+ el.getHumanType() +
				" \n representation text of element : "+el.get_representationText());






	}

}








