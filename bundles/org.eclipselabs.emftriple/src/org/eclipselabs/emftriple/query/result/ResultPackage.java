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
package org.eclipselabs.emftriple.query.result;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipselabs.emftriple.query.result.ResultFactory
 * @model kind="package"
 * @generated
 */
public interface ResultPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "result";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipselabs.org/emftriple/result";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "result";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ResultPackage eINSTANCE = org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipselabs.emftriple.query.result.impl.QueryResultImpl <em>Query Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipselabs.emftriple.query.result.impl.QueryResultImpl
	 * @see org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl#getQueryResult()
	 * @generated
	 */
	int QUERY_RESULT = 0;

	/**
	 * The number of structural features of the '<em>Query Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_RESULT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipselabs.emftriple.query.result.impl.SingleResultImpl <em>Single Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipselabs.emftriple.query.result.impl.SingleResultImpl
	 * @see org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl#getSingleResult()
	 * @generated
	 */
	int SINGLE_RESULT = 1;

	/**
	 * The feature id for the '<em><b>Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_RESULT__RESULT = QUERY_RESULT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Single Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_RESULT_FEATURE_COUNT = QUERY_RESULT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipselabs.emftriple.query.result.impl.ListResultImpl <em>List Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipselabs.emftriple.query.result.impl.ListResultImpl
	 * @see org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl#getListResult()
	 * @generated
	 */
	int LIST_RESULT = 2;

	/**
	 * The feature id for the '<em><b>Result</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_RESULT__RESULT = QUERY_RESULT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>List Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_RESULT_FEATURE_COUNT = QUERY_RESULT_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.eclipselabs.emftriple.query.result.QueryResult <em>Query Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query Result</em>'.
	 * @see org.eclipselabs.emftriple.query.result.QueryResult
	 * @generated
	 */
	EClass getQueryResult();

	/**
	 * Returns the meta object for class '{@link org.eclipselabs.emftriple.query.result.SingleResult <em>Single Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Single Result</em>'.
	 * @see org.eclipselabs.emftriple.query.result.SingleResult
	 * @generated
	 */
	EClass getSingleResult();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipselabs.emftriple.query.result.SingleResult#getResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result</em>'.
	 * @see org.eclipselabs.emftriple.query.result.SingleResult#getResult()
	 * @see #getSingleResult()
	 * @generated
	 */
	EReference getSingleResult_Result();

	/**
	 * Returns the meta object for class '{@link org.eclipselabs.emftriple.query.result.ListResult <em>List Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>List Result</em>'.
	 * @see org.eclipselabs.emftriple.query.result.ListResult
	 * @generated
	 */
	EClass getListResult();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipselabs.emftriple.query.result.ListResult#getResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Result</em>'.
	 * @see org.eclipselabs.emftriple.query.result.ListResult#getResult()
	 * @see #getListResult()
	 * @generated
	 */
	EReference getListResult_Result();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ResultFactory getResultFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipselabs.emftriple.query.result.impl.QueryResultImpl <em>Query Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipselabs.emftriple.query.result.impl.QueryResultImpl
		 * @see org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl#getQueryResult()
		 * @generated
		 */
		EClass QUERY_RESULT = eINSTANCE.getQueryResult();

		/**
		 * The meta object literal for the '{@link org.eclipselabs.emftriple.query.result.impl.SingleResultImpl <em>Single Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipselabs.emftriple.query.result.impl.SingleResultImpl
		 * @see org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl#getSingleResult()
		 * @generated
		 */
		EClass SINGLE_RESULT = eINSTANCE.getSingleResult();

		/**
		 * The meta object literal for the '<em><b>Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SINGLE_RESULT__RESULT = eINSTANCE.getSingleResult_Result();

		/**
		 * The meta object literal for the '{@link org.eclipselabs.emftriple.query.result.impl.ListResultImpl <em>List Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipselabs.emftriple.query.result.impl.ListResultImpl
		 * @see org.eclipselabs.emftriple.query.result.impl.ResultPackageImpl#getListResult()
		 * @generated
		 */
		EClass LIST_RESULT = eINSTANCE.getListResult();

		/**
		 * The meta object literal for the '<em><b>Result</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIST_RESULT__RESULT = eINSTANCE.getListResult_Result();

	}

} //ResultPackage
