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
 * A representation of the model object '<em><b>Person</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.Person#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.Person#getBooks <em>Books</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getPerson()
 * @model
 * @generated
 */
public interface Person extends EObject {
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
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getPerson_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipselabs.emftriple.jena.junit.model.Person#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Books</b></em>' reference list.
	 * The list contents are of type {@link org.eclipselabs.emftriple.jena.junit.model.Book}.
	 * It is bidirectional and its opposite is '{@link org.eclipselabs.emftriple.jena.junit.model.Book#getAuthors <em>Authors</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Books</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Books</em>' reference list.
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getPerson_Books()
	 * @see org.eclipselabs.emftriple.jena.junit.model.Book#getAuthors
	 * @model opposite="authors"
	 * @generated
	 */
	EList<Book> getBooks();

} // Person
