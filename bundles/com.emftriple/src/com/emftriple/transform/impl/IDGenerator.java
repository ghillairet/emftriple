package com.emftriple.transform.impl;

import static com.emftriple.util.Functions.transform;

import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.util.EntityUtil;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;

final class IDGenerator {

	private static final String ID = "Id";

	public static final String BASE = "base";

	public static String getId(Object object) {
		EntityUtil.checkState(object);

		return getId((EObject)object);
	}

	/**
	 * Return the value of object id.
	 */
	public static String getId(EObject object) {
		EAttribute attrId = EntityUtil.getId(object.eClass());
		if (isURI(attrId)) {
			return valueOf(object, attrId).toString().replaceAll(" ", "_");
		} else {
			return processId(object, attrId);
		} 
	}

	private static boolean isURI(final EAttribute id) {
		return (id != null && id.getName() != null) ? id.getName().equals("URI") : false;
	}

	/**
	 * Return the value of object id.
	 */
	private static String processId(EObject object, EAttribute id) throws IllegalArgumentException {
		Preconditions.checkNotNull(id);

		String value = null;
		final EAnnotation eAnnotation = EntityUtil.getETripleAnnotation(id, ID);
		final Boolean annotatedId = eAnnotation != null;
		final Boolean hasBase = annotatedId ? eAnnotation.getDetails().containsKey(BASE) : false;
		final Boolean conExpr = hasBase ? containsExpr(eAnnotation) : false;

		if (!annotatedId) {
			value = EntityUtil.namespace(object) + valueOf(object, id).toString();
		}

		if (hasBase) 
		{
			final String base;
			if (conExpr) 
			{
				base = transform(eAnnotation.getDetails().get(BASE), new IdParser(object, object.eClass()));
			} 
			else 
			{
				base = eAnnotation.getDetails().get(BASE);
			}
			Object val = valueOf(object, id);
			value = val == null ? null : 
				transform(base + val.toString(), new EntityUtil.URIValidator());
		}

		return value;
	}

	private static Object valueOf(EObject object, EAttribute attr) { 
		return object.eGet(attr); 
	}

	private static boolean containsExpr(EAnnotation eAnnotation) {
		return eAnnotation.getDetails().containsKey(BASE) ? 
				eAnnotation.getDetails().get(BASE).indexOf("[") > -1 : false;
	}

	private static class IdParser implements Function<String, String> {
		final EClass eClass;
		private EObject eObject;
		final String[] properties;

		IdParser(EObject eObject, EClass eClass) {
			this.eClass = eClass;
			this.eObject = eObject;
			this.properties = properties(eClass);
		}

		String[] properties(EClass eClass) {
			String[] props = new String[20];
			int i = 0;
			for (EStructuralFeature feature: eClass.getEAllStructuralFeatures())
			{
				if (i < props.length) {
					props[i++] = feature.getName();
				}
			}
			return props;
		}

		@Override
		public String apply(String from) {
			Pattern pattern = Pattern.compile("\\[");

			if (pattern.matcher(from).find()) 
			{
				for (String str: properties)
				{
					if (str != null) 
					{
						pattern = Pattern.compile("\\[" + str + "\\]");
						if (pattern.matcher(from).find())
						{
							from = from.replaceAll("\\[" + str + "\\]", 
									eObject.eGet(eClass.getEStructuralFeature(str)).toString().toLowerCase());		
						}
						else
						{
							pattern = Pattern.compile("\\[" + str.toLowerCase() + "\\]");
							if (pattern.matcher(from).find())
							{
								from = from.replaceAll("\\[" + str.toLowerCase() + "\\]", 
										eObject.eGet(eClass.getEStructuralFeature(str)).toString().toLowerCase());
							}
						}
					}
				}
				from = from.replaceAll("\\[Class\\]", eClass.getName().toLowerCase());
			}

			return from;
		}
	}

}