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
 * A representation of the model object '<em><b>BNode</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.junit.model.BNode#getId <em>Id</em>}</li>
 *   <li>{@link com.junit.model.BNode#getChild <em>Child</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.junit.model.ModelPackage#getBNode()
 * @model
 * @generated
 */
public interface BNode extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see com.junit.model.ModelPackage#getBNode_Id()
	 * @model
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link com.junit.model.BNode#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Child</b></em>' containment reference list.
	 * The list contents are of type {@link com.junit.model.BNode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child</em>' containment reference list.
	 * @see com.junit.model.ModelPackage#getBNode_Child()
	 * @model containment="true"
	 *        annotation="BlankNode foo='bar'"
	 * @generated
	 */
	EList<BNode> getChild();

} // BNode
