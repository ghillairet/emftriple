/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package com.emftriple.transform;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

import com.emftriple.vocabularies.RDFS;
import com.emftriple.vocabularies.XSD;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class DatatypeConverter {

	private static final Map<String, EDataType> datatypes = new HashMap<String, EDataType>();

	private static Map<EDataType, String> xsdmap = new HashMap<EDataType, String>();
	
	public static Object convert(EDataType datatype, String value) {		
		if (value != null && !value.trim().isEmpty()) {
			if (xsdmap.containsKey(datatype)) {
				return EcoreFactory.eINSTANCE.createFromString(datatype, value);
			}
		}
		return null;
	}

	public static Object convert(String datatype, String value) {
		if (value != null && !value.trim().isEmpty()) {
			if (datatypes.containsKey(datatype)) {
				return EcoreFactory.eINSTANCE.createFromString(datatypes.get(datatype), value);
			}
		}
		return null;
	}
	
	public static String toString(String datatype, Object object) {
		if (datatype.equals("EDate") || datatype.equals("Date")) {
			final GregorianCalendar c = new GregorianCalendar();
			c.setTime( ((Date) object) );
			XMLGregorianCalendar date = null;
			try {
				date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			if (date != null) {
				return date.toXMLFormat();	
			}
		} 
		else if (datatypes.containsKey(datatype)) {
			return EcoreFactory.eINSTANCE.convertToString(datatypes.get(datatype), object);
		}
		
		return object.toString();
	}

	public static String get(EDataType aType) {
		return aType instanceof EEnum ? RDFS.Literal :
				xsdmap.containsKey(aType) ?
					xsdmap.get(aType) : RDFS.Literal;
	}

	static {
		datatypes.put("Literal", EcorePackage.eINSTANCE.getEString());
		// String
		datatypes.put("String", EcorePackage.eINSTANCE.getEString());
		datatypes.put("EString", EcorePackage.eINSTANCE.getEString());
		// Date
		datatypes.put("Date", EcorePackage.eINSTANCE.getEDate());
		datatypes.put("EDate", EcorePackage.eINSTANCE.getEDate());
		// integer
		datatypes.put("Int", EcorePackage.eINSTANCE.getEInt());
		datatypes.put("EInt", EcorePackage.eINSTANCE.getEInt());
		datatypes.put("Integer", EcorePackage.eINSTANCE.getEIntegerObject());
		datatypes.put("EInteger", EcorePackage.eINSTANCE.getEIntegerObject());
		datatypes.put("EIntegerObject", EcorePackage.eINSTANCE.getEIntegerObject());
		// double
		datatypes.put("Double", EcorePackage.eINSTANCE.getEDouble());
		datatypes.put("EDouble", EcorePackage.eINSTANCE.getEDouble());
		datatypes.put("EDoubleObject", EcorePackage.eINSTANCE.getEDoubleObject());
		// long
		datatypes.put("Long", EcorePackage.eINSTANCE.getELong());
		datatypes.put("ELong", EcorePackage.eINSTANCE.getELong());
		datatypes.put("ELongObject", EcorePackage.eINSTANCE.getELongObject());
		// float
		datatypes.put("Float", EcorePackage.eINSTANCE.getEFloat());
		datatypes.put("EFloat", EcorePackage.eINSTANCE.getEFloat());
		datatypes.put("EFloatObject", EcorePackage.eINSTANCE.getEFloatObject());
		// short
		datatypes.put("Short", EcorePackage.eINSTANCE.getEShort());
		datatypes.put("EShort", EcorePackage.eINSTANCE.getEShort());
		datatypes.put("EShortObject", EcorePackage.eINSTANCE.getEShortObject());
		// boolean
		datatypes.put("Boolean", EcorePackage.eINSTANCE.getEBoolean());
		datatypes.put("EBoolean", EcorePackage.eINSTANCE.getEBoolean());
		datatypes.put("EBooleanObject", EcorePackage.eINSTANCE.getEBooleanObject());
		// byte
		datatypes.put("Byte", EcorePackage.eINSTANCE.getEByte());
		datatypes.put("EByte", EcorePackage.eINSTANCE.getEByte());
		datatypes.put("EByteObject", EcorePackage.eINSTANCE.getEByteObject());
		datatypes.put("EByteArray", EcorePackage.eINSTANCE.getEByteArray());
		// char
		datatypes.put("EChar", EcorePackage.eINSTANCE.getEChar());
		datatypes.put("ECharacterObject", EcorePackage.eINSTANCE.getECharacterObject());
		datatypes.put("EBigDecimal", EcorePackage.eINSTANCE.getEBigDecimal());
		datatypes.put("EBigInteger", EcorePackage.eINSTANCE.getEBigInteger());
	}
	
	static {
		xsdmap.put(EcorePackage.eINSTANCE.getEString(), XSD.xstring);
		xsdmap.put(EcorePackage.eINSTANCE.getEInt(), XSD.xint);
//		xsdmap.put(EcorePackage.eINSTANCE.getEString(), XSD.anyURI);
//		xsdmap.put(EcorePackage.eINSTANCE.getEDate(), XSD.date );
		xsdmap.put(EcorePackage.eINSTANCE.getEDate(), XSD.dateTime);
		xsdmap.put(EcorePackage.eINSTANCE.getEFloat(), XSD.negativeInteger);
		xsdmap.put(EcorePackage.eINSTANCE.getEBoolean(), XSD.xboolean);
		xsdmap.put(EcorePackage.eINSTANCE.getEDouble(), XSD.xdouble);
	}
}
