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
 * A representation of the model object '<em><b>Movie</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.junit.model.Movie#getId <em>Id</em>}</li>
 *   <li>{@link com.junit.model.Movie#getTitle <em>Title</em>}</li>
 *   <li>{@link com.junit.model.Movie#getActors <em>Actors</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.junit.model.ModelPackage#getMovie()
 * @model annotation="OWLClass uri='http://data.linkedmdb.org/resource/movie/film'"
 * @generated
 */
public interface Movie extends EObject {
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
	 * @see com.junit.model.ModelPackage#getMovie_Id()
	 * @model annotation="Id base='http://data.linkedmdb.org/resource/film/'"
	 *        annotation="rdf uri='http://data.linkedmdb.org/resource/movie/filmid'"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link com.junit.model.Movie#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

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
	 * @see com.junit.model.ModelPackage#getMovie_Title()
	 * @model annotation="rdf uri='http://purl.org/dc/terms/title'"
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link com.junit.model.Movie#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Actors</b></em>' reference list.
	 * The list contents are of type {@link com.junit.model.Actor}.
	 * It is bidirectional and its opposite is '{@link com.junit.model.Actor#getActorOf <em>Actor Of</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actors</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actors</em>' reference list.
	 * @see com.junit.model.ModelPackage#getMovie_Actors()
	 * @see com.junit.model.Actor#getActorOf
	 * @model opposite="actorOf"
	 *        annotation="rdf uri='http://data.linkedmdb.org/resource/movie/actor'"
	 * @generated
	 */
	EList<Actor> getActors();

} // Movie
