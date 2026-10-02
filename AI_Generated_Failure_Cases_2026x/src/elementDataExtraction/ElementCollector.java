package elementDataExtraction;

import java.util.ArrayList;
import java.util.List;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ElementCollector` class is responsible for retrieving and filtering SysML elements based on stereotypes, properties, or specific values within a model.
 */
public class ElementCollector {

	Project project = Application.getInstance().getProject();
    StereotypeSearch stereotypeChecker = new StereotypeSearch();

	StereotypeFinder PF = new StereotypeFinder();
/**
 * @param steotypeName The `steotypeName` parameter specifies the name of the stereotype to filter elements within the project's models.
 * @return The `get_all_elements_with_stereotype` method returns a list of elements that have the specified stereotype.
 * @summary The method `get_all_elements_with_stereotype` retrieves all elements in the project that have a specified stereotype.
 */
	public List<Element> get_all_elements_with_stereotype(String steotypeName) {
		// TODO Auto-generated method stub

		List<Element> Stereotyped_Elements = new ArrayList<>();
		List<Package> models =project.getModels();
		for(Package model: models) {
			List <Element> elementList = getAllinstancesofSterotypeelements(model,steotypeName);
			Stereotyped_Elements.addAll(elementList);
		}
		return Stereotyped_Elements;
	}
/**
 * @param element The `element` parameter represents the root SysML model element from which the method recursively searches for instances of elements with a specific stereotype.
 * @param steotypeName The `stereotypeName` parameter specifies the name of the stereotype to search for within the SysML model elements.
 * @return The `getAllinstancesofSterotypeelements` method returns a list of elements that match a specified stereotype within the hierarchy of a given SysML element.
 * @summary The `getAllInstancesOfStereotypeElements` method recursively collects and returns a list of elements from a SysML model that are instances of a specified stereotype.
 */
	private List<Element> getAllinstancesofSterotypeelements(Element element, String steotypeName) {
		// TODO Auto-generated method stub

	    List<Element> ElementList = new ArrayList<>();

	    var ownedElements = element.getOwnedElement();
	    
	    if (ownedElements != null) {
	        for (Element ownedElement : ownedElements) {
	        	if (stereotypeChecker.hasStereotypeBoolean(ownedElement, steotypeName)) {
	        		ElementList.add(ownedElement);
	            }
	            // Recursively check the owned elements of the current owned element
	        	ElementList.addAll(getAllinstancesofSterotypeelements(ownedElement,steotypeName));  // Collect recursive results
	        }
	    }
		return ElementList;
	}
/**
 * @param ALL_FCs The `ALL_FCs` parameter is a list of elements from which the method filters and retrieves those with a specific stereotype property value.
 * @param stereotypeName The `stereotypeName` parameter specifies the name of the stereotype to be used for filtering elements based on their property values.
 * @param property_of_stereotype The `property_of_stereotype` parameter specifies the name of the property within the given stereotype that will be used to filter elements.
 * @param value_to_be_verified The `value_to_be_verified` parameter is an instance of the `Element` class representing the specific value to match against for the property specified in `property_of_stereotype`.
 * @return The method `get_all_elements_with_specific_stereotype_property_value` returns a list of elements that have a specific stereotype property value. The list includes all elements from the input list that match the specified stereotype and property value criteria, ensuring that only those elements are returned which have the exact value for the given property within their associated stereotype.
 * @summary The method `get_all_elements_with_specific_strereotype_property_value` filters a list of elements to return only those with a specific stereotype property value.
 */
	public List<Element> get_all_elements_with_specific_strereotype_property_value(List<Element> ALL_FCs,
			String stereotypeName, String property_of_stereotype, Element value_to_be_verified) {

	    List<Element> ElementList = new ArrayList<>();

		Stereotype FC_Stereotype = PF.findstereotype(stereotypeName);

		var property = StereotypesHelper.getPropertyByName(FC_Stereotype, property_of_stereotype);
	    
	    for (Element fc: ALL_FCs) {
	    	var prop_values =StereotypesHelper.getStereotypePropertyValue(fc, property);
	    	if(!(prop_values==null)) {
	    		for(Object val: prop_values) {
	    			if(val==value_to_be_verified) {
	    				ElementList.add(fc);
	    			}
	    		}
	    	}
	    }
		
		return ElementList;
	}
	
/**
 * @param ALL_FCs A list of all failure conditions to be searched for matching property values.
 * @param Search_property The `Search_property` parameter is a property used to filter elements in the list `ALL_FCs` based on their specific property value.
 * @param value_to_be_verified The `value_to_be_verified` parameter is the specific SysML element whose presence in the property values of other elements needs to be checked and verified.
 * @return The method `get_all_elements_with_search_property_value` returns a list of elements from the input list (`ALL_FCs`) that have the specified property value (`value_to_be_verified`).
 * @summary The method `get_all_elements_with_search_property_value` returns a list of elements from an input list that have a specific property value equal to the given verification element.
 */
	public List<Element> get_all_elements_with_search_property_value(List<Element> ALL_FCs,
			Property Search_property, Element value_to_be_verified){
	    List<Element> ElementList = new ArrayList<>();
	    for (Element fc: ALL_FCs) {
	    	var prop_values =StereotypesHelper.getStereotypePropertyValue(fc, Search_property);
	    	if(!(prop_values==null)) {
	    		for(Object val: prop_values) {
	    			if(val==value_to_be_verified) {
	    				ElementList.add(fc);
	    			}
	    		}
	    	}
	    }
				return ElementList;
		
	}
/**
 * @param fC The `fC` parameter represents the SysML element from which to retrieve property values.
 * @param value_collection_property The `value_collection_property` parameter is a `Property` object representing the specific property whose string values are to be retrieved from the given `Element` in the SysML model.
 * @return The return value of the `get_String_value_specific_Property` method is a list containing all the string values associated with a specific property within an element.
 * @summary The method `get_String_value_specific_Property` extracts and returns a list of string values from a specified property of an element in the SysML model.
 */
	public List<String> get_String_value_specific_Property(Element fC, Property value_collection_property) {
		// TODO Auto-generated method stub
	    List<String> StringList = new ArrayList<>();

	    	var prop_values =StereotypesHelper.getStereotypePropertyValue(fC, value_collection_property);
	    	if(!(prop_values==null)) {
	    		for(Object val: prop_values) {
	    			if(val instanceof String) {
	    				StringList.add((String) val);
	    			}
	    		}
	    	}
	    
		return StringList;
	}
	

}
