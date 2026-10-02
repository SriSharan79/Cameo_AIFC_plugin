package elementDataExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import creationOfSafetyVP.ElementFinder_SVP;
import creationOfSafetyVP.NameDerivator;

/**
 * @author kata_du
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ElementDataProcessor` class is responsible for extracting and processing data from SysML elements, including their stereotypes, properties, and specifications.
 */
public class ElementDataProcessor {

	static StereotypeSearch StereotypeChecker = new StereotypeSearch();

	NameDerivator ND = new NameDerivator();

	ElementFinder_SVP elementfinder =new ElementFinder_SVP();

/**
 * @param element `element`: The `element` parameter represents the SysML model element from which data is being extracted and analyzed for Functional Hazard Assessment (FHA).
 * @return The `getElementData` method returns a `List<String>` containing unique data strings extracted from an element's owned elements. These strings include information and specifications, filtered by specific criteria such as skipping profiles, diagrams, certain tagged values, and matching stereotypes. The list is ordered based on the insertion order of elements and ensures that each string is unique within the list.
 * @summary The `getElementData` method retrieves and processes all relevant data from an element's owned elements, ensuring uniqueness and maintaining insertion order.
 */
	public List<String> getElementData(Element element) {
		// Use a LinkedHashSet to maintain insertion order and ensure uniqueness
		Set<String> uniqueStringsForEmbedding = new LinkedHashSet<>();

		// Retrieve owned elements of the current element
		var ownedElements = element.getOwnedElement();
		for (Element el : ownedElements) {
			// Skip profiles
			String name = ND.getName(el);
			String[] validNames = {"Tagged Value", ""};
			boolean tagval = Arrays.asList(validNames).contains(name);
			List<String> stereotypes = List.of("auxiliaryResource", "Safety Viewpoint");
			boolean hasMatchingStereotype = hasAnyStereotype(el, stereotypes);

			if (!(el instanceof Profile) && !(el instanceof Diagram) && !tagval && !hasMatchingStereotype) {
				// Extract data for the current element
				String ELinfo = ExtractElementData(el);
				uniqueStringsForEmbedding.add(ELinfo);

				// Extract specifications from stereotypes
				List<String> ELSpecList = ExtractSpecification(el);
				uniqueStringsForEmbedding.addAll(ELSpecList);

				// Recursively process owned elements
				uniqueStringsForEmbedding.addAll(getElementData(el));
			}
		}

		// Convert the set back to a list before returning
		return new ArrayList<>(uniqueStringsForEmbedding);
	}

/**
 * @param el The `el` parameter of type `Element` represents the SysML model element being checked for the presence of any specified stereotypes.
 * @param stereotypes The `stereotypes` parameter is a list of strings representing the types of SysML stereotypes that are checked for presence on the given element.
 * @return The `hasAnyStereotype` method returns a boolean value indicating whether the given SysML element possesses any of the specified stereotypes. The return value is true if at least one of the provided stereotypes is found on the element; otherwise, it is false.
 * @summary This method checks if an Element possesses any of the specified stereotypes by evaluating a list of stereotype strings.
 */
	public static boolean hasAnyStereotype(Element el, List<String> stereotypes) {
		return stereotypes.stream().anyMatch(stereotype -> StereotypeChecker.hasStereotypeBoolean(el, stereotype));
	}
	
	
/**
 * @param el The `el` parameter of type `Element` in the method `ExtractSpecification` represents the SysML model element from which stereotypes and their properties will be extracted to generate specification strings.
 * @return The `ExtractSpecification` method returns a `List<String>` containing specification strings that describe the properties of stereotypes applied to an element in a SysML model. Each string provides detailed information about how specific properties of stereotypes are instantiated on the given element, formatted as "element name has property name as property value".
 * @summary The `ExtractSpecification` method is responsible for extracting specification strings from the properties of applied stereotypes on a given SysML model element, and returning them as a list.
 */
	private List<String> ExtractSpecification(Element el) {
		List<String> SpecStringsForEmbedding = new ArrayList<>();

		// Get stereotypes applied to the element
		List<Stereotype> stereotypes = el.getAppliedStereotype();
		for (Stereotype st : stereotypes) {
			// Get properties of the stereotype
			List<Property> properties = StereotypesHelper.getPropertiesWithDerivedOrdered(st);
			for (Property prop : properties) {
				// Skip processing if property values are empty
				String propValue = getPropValue(el, st, prop);
				if (propValue == null || propValue.isEmpty()) {
					continue; // Skip this property
				}

				// Build specification string
				StringBuilder ELSpec = new StringBuilder();
				ELSpec.append(el.getHumanName()).append(" has ")
				.append(prop.getHumanName()).append(" as ")
				.append(propValue);

				// Add to specifications list
				SpecStringsForEmbedding.add(ELSpec.toString());
			}
		}


		return SpecStringsForEmbedding;
	}

/**
 * @param el The `el` parameter represents the SysML element from which to retrieve property values for a given stereotype and property.
 * @param st The `st` parameter of type `Stereotype` in the `getPropValue` method specifies which stereotype to retrieve property values from.
 * @param prop The `prop` parameter is the specific property of a SysML element whose value needs to be retrieved for analysis.
 * @return The `getPropValue` method returns a concatenated string of all property values associated with the specified stereotype and property for the given element. The returned string is formatted by appending each value followed by " ; ".
 * @summary The `getPropValue` method retrieves and concatenates the values of a specified property from a given stereotype within an element, returning them as a single string.
 */
	@SuppressWarnings("deprecation")
	private String getPropValue(Element el, Stereotype st, Property prop) {
		StringBuilder PrSpec = new StringBuilder();

		// Get property values for the given stereotype and property
		List<?> propValues = StereotypesHelper.getStereotypePropertyValue(el, st, prop);
		for (Object pv : propValues) {
			if (pv instanceof String) {
				PrSpec.append(pv).append(" ; ");
			} else if (pv instanceof Element) {
				PrSpec.append(((Element) pv).getHumanName()).append(" ; ");
			}
		}

		return PrSpec.toString();
	}

	/**
	 * Extracts string data for a single element.
	 */
/**
 * @param el The `el` parameter of type `Element` in the method `ExtractElementData` is a SysML element whose details are extracted and formatted into a string.
 * @return The return value of the `ExtractElementData` method is a `String` that contains detailed information about an element. This includes the owner's name (or "No Owner" if there is none) and the element's name, along with additional details specifying whether it is an input or output parameter when applicable.
 * @summary The `ExtractElementData` method is responsible for extracting and formatting the data of a given SysML model element, including its owner's name and whether it's an input or output parameter.
 */
	private String ExtractElementData(Element el) {
		StringBuilder ELData = new StringBuilder();

		// Append owner and element details
		ELData.append(el.getOwner() != null ? el.getOwner().getHumanName() : "No Owner")
		.append(" Contains ")
		.append(ND.getName(el));

		if(el instanceof Parameter) {

			Parameter elPin= (Parameter) el;
			String paramDirection = elPin.getDirection().toString();

			if(paramDirection.equals("in")) {
				ELData.append(" as ").append("Input parameter");
			}
			if(paramDirection.equals("out")) {
				ELData.append(" as ").append("Output Parameter");
			}

		}

		return ELData.toString();
	}

/**
 * @param failureTypes The `failureTypes` parameter is a list of strings representing the different types of failures to be checked for default conditions in the FHA process.
 * @return The return value of the `hasdefaultFT` method is a boolean that indicates whether there are any default failure types within the provided list. It returns true if at least one default failure type exists, and false otherwise.
 * @summary The `hasdefaultFT` method checks if the provided list of failure types contains any default failure type.
 */
	public boolean hasdefaultFT(List<String> failureTypes) {
		// TODO Auto-generated method stub
		elementfinder.FindAllFailureNames();
		return false;
	}
}
