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
 * A representation of the model object '<em><b>Book BNode</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.BookBNode#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.BookBNode#getAuthors <em>Authors</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getBookBNode()
 * @model annotation="BlankNode foo='bar'"
 * @generated
 */
public interface BookBNode extends EObject {
	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getBookBNode_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipselabs.emftriple.jena.junit.model.BookBNode#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Authors</b></em>' reference list.
	 * The list contents are of type {@link org.eclipselabs.emftriple.jena.junit.model.PersonBNode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Authors</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Authors</em>' reference list.
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getBookBNode_Authors()
	 * @model
	 * @generated
	 */
	EList<PersonBNode> getAuthors();

} // BookBNode
