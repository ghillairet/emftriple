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
 * A representation of the model object '<em><b>Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.Actor#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.Actor#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipselabs.emftriple.jena.junit.model.Actor#getActorOf <em>Actor Of</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getActor()
 * @model annotation="OWLClass uri='http://data.linkedmdb.org/resource/movie/actor'"
 * @generated
 */
public interface Actor extends EObject {
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
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getActor_Id()
	 * @model annotation="Id base='http://data.linkedmdb.org/resource/actor/'"
	 *        annotation="rdf uri='http://data.linkedmdb.org/resource/movie/actor_actorid'"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link org.eclipselabs.emftriple.jena.junit.model.Actor#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

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
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getActor_Name()
	 * @model annotation="rdf uri='http://data.linkedmdb.org/resource/movie/actor_name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipselabs.emftriple.jena.junit.model.Actor#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Actor Of</b></em>' reference list.
	 * The list contents are of type {@link org.eclipselabs.emftriple.jena.junit.model.Movie}.
	 * It is bidirectional and its opposite is '{@link org.eclipselabs.emftriple.jena.junit.model.Movie#getActors <em>Actors</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actor Of</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actor Of</em>' reference list.
	 * @see org.eclipselabs.emftriple.jena.junit.model.ModelPackage#getActor_ActorOf()
	 * @see org.eclipselabs.emftriple.jena.junit.model.Movie#getActors
	 * @model opposite="actors"
	 *        annotation="rdf uri='http://data.linkedmdb.org/resource/movie/actor'"
	 * @generated
	 */
	EList<Movie> getActorOf();

} // Actor
