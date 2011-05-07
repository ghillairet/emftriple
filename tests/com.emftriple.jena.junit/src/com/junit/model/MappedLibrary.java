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
 * A representation of the model object '<em><b>Mapped Library</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.junit.model.MappedLibrary#getLocation <em>Location</em>}</li>
 *   <li>{@link com.junit.model.MappedLibrary#getRareBooks <em>Rare Books</em>}</li>
 *   <li>{@link com.junit.model.MappedLibrary#getRegularBooks <em>Regular Books</em>}</li>
 *   <li>{@link com.junit.model.MappedLibrary#getBooks <em>Books</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.junit.model.ModelPackage#getMappedLibrary()
 * @model
 * @generated
 */
public interface MappedLibrary extends EObject {
	/**
	 * Returns the value of the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' containment reference.
	 * @see #setLocation(Location)
	 * @see com.junit.model.ModelPackage#getMappedLibrary_Location()
	 * @model containment="true"
	 * @generated
	 */
	Location getLocation();

	/**
	 * Sets the value of the '{@link com.junit.model.MappedLibrary#getLocation <em>Location</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' containment reference.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Location value);

	/**
	 * Returns the value of the '<em><b>Rare Books</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.Book}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rare Books</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rare Books</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getMappedLibrary_RareBooks()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#books'"
	 * @generated
	 */
	EList<Book> getRareBooks();

	/**
	 * Returns the value of the '<em><b>Regular Books</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.Book}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regular Books</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regular Books</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getMappedLibrary_RegularBooks()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#books'"
	 * @generated
	 */
	EList<Book> getRegularBooks();

	/**
	 * Returns the value of the '<em><b>Books</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Books</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Books</em>' attribute list.
	 * @see com.junit.model.ModelPackage#getMappedLibrary_Books()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getBooks();

} // MappedLibrary
