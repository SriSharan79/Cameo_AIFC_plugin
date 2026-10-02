package creationOfSafetyVP;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ElementCreation` class is responsible for creating and managing SysML model elements such as classes and packages, as well as adding stereotypes to these elements.
 */
public class ElementCreation {
	
	 	private final Project project;
	    private final ElementsFactory factory;
	    private final ModelElementsManager manager;
	    
/**
 * @return The `ElementCreation` method returns an instance of the `ElementCreation` class, which is configured with the current project context from Cameo Systems Modeler. This object can be used to create and manage model elements within the system's environment.

In detail:
- `project`: Retrieves and sets the currently active project.
- `factory`: Initializes a factory for creating new model elements.
- `manager`: Obtains an instance of the `ModelElementsManager` for managing these elements.
 * @summary The `ElementCreation` method initializes the environment for creating new model elements by setting up the project, factory, and manager instances.
 */
	    public ElementCreation() {
	        this.project = Application.getInstance().getProject();
	        this.factory = project.getElementsFactory();
	        this.manager = ModelElementsManager.getInstance();
	        
	        }
	    
/**
 * @param parentPackage `parentPackage`: Specifies the package under which the new class will be created.
 * @param name The `name` parameter specifies the unique identifier for the newly created class instance within the given package.
 * @return The return value for the `createClass` method is a newly created instance of the `Class` type in the specified package. This class is initialized with the provided name and added to the system's element manager for further manipulation or reference within the model.
 * @summary The `createClass` method creates and adds a new class with the specified name to the given package.
 */
	    public Class createClass(Package parentPackage, String name) throws ReadOnlyElementException {

	    	var mdClass = factory.createClassInstance();
	    	mdClass.setName(name);
	    	manager.addElement(mdClass, parentPackage);

	    	return mdClass;
	    }
	    
/**
 * @param parentPackage The `parentPackage` parameter specifies the Package within which a new Package will be created.
 * @param name The `name` parameter in the method `createPackage` specifies the unique identifier to be assigned as the name of the newly created package.
 * @return The `createPackage` method returns an instance of the `Package` class representing a newly created package within the specified parent package. This package has been initialized with the given name and added to the model's element manager.
 * @summary The `createPackage` method is responsible for creating a new package with the given name under the specified parent package.
 */
	    public Package createPackage(Package parentPackage, String name) throws ReadOnlyElementException {

	    	var mdClass = factory.createPackageInstance();
	    	mdClass.setName(name);
	    	manager.addElement(mdClass, parentPackage);

	    	return mdClass;
	    }
	    
/**
 * @param mdClass The `mdClass` parameter represents the Java class to which a SysML 'Block' stereotype will be applied.
 * @summary The `addStereotype` method adds a SysML "Block" stereotype to a given class in the project.
 */
	    public void addStereotype(Class mdClass) throws ReadOnlyElementException{

	        var profile = StereotypesHelper.getProfile(project, "SysML");
	        var block = StereotypesHelper.getStereotype(project, "Block", profile);
	        StereotypesHelper.addStereotype(mdClass, block);
	        
	    }
	    
/**
 * @param Package The `Package` parameter in the `addStereotype` method represents the package to which a stereotype will be applied.
 * @param Profilename `Profilename`: The name of the profile to be used for adding a stereotype.
 * @param Sterotypename The `Sterotypename` parameter specifies the name of the stereotype to be added from the given profile.
 * @summary The `addStereotype` method applies a specified stereotype to a given package within the project.
 */
	    public void addStereotype(Package Package,String Profilename, String Sterotypename ) throws ReadOnlyElementException{

	        var profile = StereotypesHelper.getProfile(project, Profilename);
	        var block = StereotypesHelper.getStereotype(project, Sterotypename, profile);
	        StereotypesHelper.addStereotype(Package, block);
	    
	    }
	    
/**
 * @param Package The `Package` parameter of type `Element` in the method `addStereotype` represents the SysML package to which a stereotype will be applied.
 * @param Profilename The `Profilename` parameter specifies the name of the profile from which to retrieve the stereotype.
 * @param Sterotypename The `Sterotypename` parameter specifies the name of the stereotype to be added from a given profile.
 * @summary The `addStereotype` method adds a specified stereotype to an element within a package.
 */
	    public void addStereotype(Element Package,String Profilename, String Sterotypename ) throws ReadOnlyElementException{

	        var profile = StereotypesHelper.getProfile(project, Profilename);
	        var block = StereotypesHelper.getStereotype(project, Sterotypename, profile);
	        StereotypesHelper.addStereotype(Package, block);
	    
	    }

/**
 * @param safetyVPElement The `safetyVPElement` parameter of type `Package` in the `addStereotype` method represents the SysML package to which a stereotype is being added.
 * @param profilename The `profilename` parameter specifies the name of the profile from which the stereotype is being applied.
 * @param safetyVPStereotype The `safetyVPStereotype` is the stereotype that will be added to the specified package element within a given profile.
 * @summary The `addStereotype` method is responsible for adding a specified stereotype to a given package element using the provided profile name.
 */
		public void addStereotype(Package safetyVPElement, String profilename, Stereotype safetyVPStereotype) {
			// TODO Auto-generated method stub
			StereotypesHelper.addStereotype(safetyVPElement, safetyVPStereotype);
		}
	    
	    

}
