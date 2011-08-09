/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipselabs.emftriple.jena.junit.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Person BNode</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.PersonBNode#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.PersonBNode#getBooks <em>Books</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getPersonBNode()
 * @model
 * @generated
 */
public interface PersonBNode extends EObject {
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
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getPersonBNode_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipselabs.emftriple.jena.junit.model.PersonBNode#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Books</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipselabs.emftriple.jena.junit.model.BookBNode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Books</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Books</em>' containment reference list.
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getPersonBNode_Books()
	 * @model containment="true"
	 * @generated
	 */
	EList<BookBNode> getBooks();

} // PersonBNode
