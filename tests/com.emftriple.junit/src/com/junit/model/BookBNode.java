/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.junit.model;

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
 *   <li>{@link com.junit.model.BookBNode#getTitle <em>Title</em>}</li>
 *   <li>{@link com.junit.model.BookBNode#getAuthors <em>Authors</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.junit.model.ModelPackage#getBookBNode()
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
	 * @see com.junit.model.ModelPackage#getBookBNode_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link com.junit.model.BookBNode#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Authors</b></em>' reference list.
	 * The list contents are of type {@link com.junit.model.PersonBNode}.
	 * It is bidirectional and its opposite is '{@link com.junit.model.PersonBNode#getBooks <em>Books</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Authors</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Authors</em>' reference list.
	 * @see com.junit.model.ModelPackage#getBookBNode_Authors()
	 * @see com.junit.model.PersonBNode#getBooks
	 * @model opposite="books"
	 * @generated
	 */
	EList<PersonBNode> getAuthors();

} // BookBNode
