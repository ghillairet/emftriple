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
package com.junit.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Primary Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.junit.model.PrimaryObject#getName <em>Name</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getSingleNonContainmentReference <em>Single Non Containment Reference</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getMultipleNonContainmentReference <em>Multiple Non Containment Reference</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getSingleContainmentReferenceNoProxies <em>Single Containment Reference No Proxies</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getMultipleContainmentReferenceNoProxies <em>Multiple Containment Reference No Proxies</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getSingleContainmentReferenceProxies <em>Single Containment Reference Proxies</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getMultipleContainmentReferenceProxies <em>Multiple Containment Reference Proxies</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getFeatureMapReferenceType2 <em>Feature Map Reference Type2</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getFeatureMapReferenceType1 <em>Feature Map Reference Type1</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getFeatureMapReferenceCollection <em>Feature Map Reference Collection</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getFeatureMapAttributeType1 <em>Feature Map Attribute Type1</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getFeatureMapAttributeType2 <em>Feature Map Attribute Type2</em>}</li>
 *   <li>{@link com.junit.model.PrimaryObject#getFeatureMapAttributeCollection <em>Feature Map Attribute Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.junit.model.ModelPackage#getPrimaryObject()
 * @model
 * @generated
 */
public interface PrimaryObject extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.junit.model.ModelPackage#getPrimaryObject_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.junit.model.PrimaryObject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Single Non Containment Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Non Containment Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Non Containment Reference</em>' reference.
	 * @see #setSingleNonContainmentReference(TargetObject)
	 * @see com.junit.model.ModelPackage#getPrimaryObject_SingleNonContainmentReference()
	 * @model
	 * @generated
	 */
	TargetObject getSingleNonContainmentReference();

	/**
	 * Sets the value of the '{@link com.junit.model.PrimaryObject#getSingleNonContainmentReference <em>Single Non Containment Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Non Containment Reference</em>' reference.
	 * @see #getSingleNonContainmentReference()
	 * @generated
	 */
	void setSingleNonContainmentReference(TargetObject value);

	/**
	 * Returns the value of the '<em><b>Multiple Non Containment Reference</b></em>' reference list.
	 * The list contents are of type {@link com.junit.model.TargetObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Non Containment Reference</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Non Containment Reference</em>' reference list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_MultipleNonContainmentReference()
	 * @model
	 * @generated
	 */
	EList<TargetObject> getMultipleNonContainmentReference();

	/**
	 * Returns the value of the '<em><b>Single Containment Reference No Proxies</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Containment Reference No Proxies</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Containment Reference No Proxies</em>' containment reference.
	 * @see #setSingleContainmentReferenceNoProxies(TargetObject)
	 * @see com.junit.model.ModelPackage#getPrimaryObject_SingleContainmentReferenceNoProxies()
	 * @model containment="true"
	 * @generated
	 */
	TargetObject getSingleContainmentReferenceNoProxies();

	/**
	 * Sets the value of the '{@link com.junit.model.PrimaryObject#getSingleContainmentReferenceNoProxies <em>Single Containment Reference No Proxies</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Containment Reference No Proxies</em>' containment reference.
	 * @see #getSingleContainmentReferenceNoProxies()
	 * @generated
	 */
	void setSingleContainmentReferenceNoProxies(TargetObject value);

	/**
	 * Returns the value of the '<em><b>Multiple Containment Reference No Proxies</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.TargetObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Containment Reference No Proxies</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Containment Reference No Proxies</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_MultipleContainmentReferenceNoProxies()
	 * @model containment="true"
	 * @generated
	 */
	EList<TargetObject> getMultipleContainmentReferenceNoProxies();

	/**
	 * Returns the value of the '<em><b>Single Containment Reference Proxies</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Containment Reference Proxies</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Containment Reference Proxies</em>' containment reference.
	 * @see #setSingleContainmentReferenceProxies(TargetObject)
	 * @see com.junit.model.ModelPackage#getPrimaryObject_SingleContainmentReferenceProxies()
	 * @model containment="true"
	 * @generated
	 */
	TargetObject getSingleContainmentReferenceProxies();

	/**
	 * Sets the value of the '{@link com.junit.model.PrimaryObject#getSingleContainmentReferenceProxies <em>Single Containment Reference Proxies</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Containment Reference Proxies</em>' containment reference.
	 * @see #getSingleContainmentReferenceProxies()
	 * @generated
	 */
	void setSingleContainmentReferenceProxies(TargetObject value);

	/**
	 * Returns the value of the '<em><b>Multiple Containment Reference Proxies</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.TargetObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Containment Reference Proxies</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Containment Reference Proxies</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_MultipleContainmentReferenceProxies()
	 * @model containment="true"
	 * @generated
	 */
	EList<TargetObject> getMultipleContainmentReferenceProxies();

	/**
	 * Returns the value of the '<em><b>Feature Map Reference Type2</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.TargetObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Map Reference Type2</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Map Reference Type2</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_FeatureMapReferenceType2()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#featureMapReferenceCollection'"
	 * @generated
	 */
	EList<TargetObject> getFeatureMapReferenceType2();

	/**
	 * Returns the value of the '<em><b>Feature Map Reference Type1</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.TargetObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Map Reference Type1</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Map Reference Type1</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_FeatureMapReferenceType1()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#featureMapReferenceCollection'"
	 * @generated
	 */
	EList<TargetObject> getFeatureMapReferenceType1();

	/**
	 * Returns the value of the '<em><b>Feature Map Reference Collection</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Map Reference Collection</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Map Reference Collection</em>' attribute list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_FeatureMapReferenceCollection()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getFeatureMapReferenceCollection();

	/**
	 * Returns the value of the '<em><b>Feature Map Attribute Type1</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Map Attribute Type1</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Map Attribute Type1</em>' attribute list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_FeatureMapAttributeType1()
	 * @model transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#featureMapAttributeCollection'"
	 * @generated
	 */
	EList<String> getFeatureMapAttributeType1();

	/**
	 * Returns the value of the '<em><b>Feature Map Attribute Type2</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Map Attribute Type2</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Map Attribute Type2</em>' attribute list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_FeatureMapAttributeType2()
	 * @model transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#featureMapAttributeCollection'"
	 * @generated
	 */
	EList<String> getFeatureMapAttributeType2();

	/**
	 * Returns the value of the '<em><b>Feature Map Attribute Collection</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Map Attribute Collection</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Map Attribute Collection</em>' attribute list.
	 * @see com.junit.model.ModelPackage#getPrimaryObject_FeatureMapAttributeCollection()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getFeatureMapAttributeCollection();

} // PrimaryObject
