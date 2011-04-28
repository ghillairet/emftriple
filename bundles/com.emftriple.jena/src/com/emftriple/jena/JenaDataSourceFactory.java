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
package com.emftriple.jena;

import java.util.Map;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IDataSourceFactory;

/**
 * Factory class for Jena based {@link IDataSource}.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 *
 */
public class JenaDataSourceFactory implements IDataSourceFactory {

	@Override
	public boolean canCreate(Map<String, Object> options) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDataSource create(Map<String, Object> options) {
		// TODO Auto-generated method stub
		return null;
	}

//	public static final String JENA_FILE_CLASS_NAME = JenaFile.class.getName();
//
//	public static final String JENA_TDB_CLASS_NAME = JenaTDB.class.getName();
//
//	public static final String JENA_SDB_CLASS_NAME = JenaSDB.class.getName();
//
//	public static final String JENA_SERVICE_CLASS_NAME = JenaService.class.getName();
//
//	JenaDataSourceFactory() {}
//
//	@Override
//	public boolean canCreate(Map<String, Object> options) {
//		return canCreateJenaFile(options)
//			|| canCreateJenaSDB(options)
//			|| canCreateJenaTDB(options)
//			|| canCreateJenaService(options);
//		
//	}
//
//	private boolean canCreateJenaService(Map<String, Object> options) {
//		return options.get("DATA_SOURCE_URL") != null;
//	}
//
//	private boolean canCreateJenaTDB(Map<String, Object> options) {
//		return options.get("DATA_SOURCE_URL") != null;
//	}
//
//	private boolean canCreateJenaSDB(Map<String, Object> options) {
//		final String aDbType = getSDBType(options);
//		final Object aURL = options.get("DATA_SOURCE_URL");
//		final String aPassword = getPassword(options);
//		final String aUser = getUser(options);
//
//		return aDbType != null && aURL != null && aPassword != null && aUser != null;
//	}
//
//	private String getUser(Map<String, Object> options) {
//		return "";
//	}
//
//	private String getPassword(Map<String, Object> options) {
//		return "";
//	}
//
//	private String getSDBType(Map<String, Object> options) {
//		return "";
//	}
//
//	private boolean canCreateJenaFile(Map<String, Object> options) {
//		return options.get("DATA_SOURCE_URL") != null;
//	}
//
//	@Override
//	public IDataSource create(Map<String, Object> options) {
//		return doCreate(options);
//	}
//
//	private IDataSource doCreate(Map<String, Object> options) {
//		IDataSource dataSource = null;
//		if (config.getClass_().equals(JENA_FILE_CLASS_NAME)) {
//			dataSource = createJenaFile(config);
//		}
//		else if (config.getClass_().equals(JENA_SDB_CLASS_NAME)) {
//			dataSource = createJenaSDB(config);
//		}
//		else if (config.getClass_().equals(JENA_TDB_CLASS_NAME)) {
//			dataSource = createJenaTDB(config);
//		}
//		else if (config.getClass_().equals(JENA_SERVICE_CLASS_NAME)) {
//			dataSource = createJenaService(config);
//		}
//		descriptors.put(config, dataSource);
//
//		return dataSource;
//	}
//
//	private IDataSource createJenaService(Map<String, Object> options) {
//		return new JenaService(config.getName(), config.getUrl());
//	}
//
//	private IDataSource createJenaTDB(Map<String, Object> options) {
//		return new JenaTDB(config.getName(), config.getUrl());
//	}
//
//	private IDataSource createJenaSDB(Map<String, Object> options) {
//		final String aDbType = getSDBType(config.getProperty());
//		final String aURL = config.getUrl();
//		final String aPassword = getPassword(config.getProperty());
//		final String aUser = getUser(config.getProperty());
//
//		final StoreDesc storeDesc = new StoreDesc(
//				LayoutType.LayoutTripleNodesHash, 
//				dbTypes.get(aDbType)) ;
//
//		loadDriver( dbTypes.get(aDbType) );
//
//		// String jdbcURL = "jdbc:derby:DB/SDB2";
//		final SDBConnection conn = new SDBConnection(aURL, aUser, aPassword) ; 
//		final Store store = SDBFactory.connectStore(conn, storeDesc) ;
//
//		return new JenaSDB(config.getName(), store);
//	}
//
//	private IDataSource createJenaFile(Map<String, Object> options) {
//		final Model model = ModelFactory.createDefaultModel();
//		final File file = new File(config.getUrl());
//
//		if (!file.exists()) {
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		String fileFormat = null;
//		if (config.getProperty() != null) {
//			fileFormat = Functions.transform(config.getProperty().getProperties(), new Find("file.format"));
//		}
//
//		if (fileFormat == null) {
//			fileFormat = "RDF/XML-ABBREV";
//			RDFReader reader = model.getReader();
//			reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");
//			try {
//				reader.read(model, new FileInputStream(file), "");
//			} catch (Exception e1) {
//				reader = model.getReader("N3");
//				reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");
//				try {
//					reader.read(model, new FileInputStream(file), "N3");
//					fileFormat = "N3";
//				} catch (Exception e2) {
//					reader = model.getReader("N-TRIPLES");
//					reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");
//					try {
//						reader.read(model, new FileInputStream(file), "N-TRIPLES");
//						fileFormat = "N-TRIPLES";
//					} catch (Exception e3) {
//						
//					}
//				}
//			}
//		}
//		return new JenaFile(config.getName(), model, config.getUrl(), fileFormat);
//	}
//
//	private static final Map<String, DatabaseType> dbTypes = new HashMap<String, DatabaseType>();
//
//	static {
//		dbTypes.put("DB2", DatabaseType.DB2);
//		dbTypes.put("H2", DatabaseType.H2);
//		dbTypes.put("HSQLDB", DatabaseType.HSQLDB);
//		dbTypes.put("MySQL", DatabaseType.MySQL);
//		dbTypes.put("Oracle", DatabaseType.Oracle);
//		dbTypes.put("PostgreSQL", DatabaseType.PostgreSQL);
//		dbTypes.put("SQLServer", DatabaseType.SQLServer);
//		dbTypes.put("Derby", DatabaseType.Derby);
//	}
//
//	private void loadDriver(final DatabaseType dbType) {
//		if (dbType.equals(DatabaseType.DB2)) {
//			JDBC.loadDriverDB2();
//		}
//		else if (dbType.equals(DatabaseType.Derby)) {
//			JDBC.loadDriverDerby();
//		}
//		else if (dbType.equals(DatabaseType.H2)) {
//			JDBC.loadDriverH2();
//		}
//		else if (dbType.equals(DatabaseType.HSQLDB)) {
//			JDBC.loadDriverHSQL();
//		}
//		else if (dbType.equals(DatabaseType.MySQL)) {
//			JDBC.loadDriverMySQL();
//		}
//		else if (dbType.equals(DatabaseType.Oracle)) {
//			JDBC.loadDriverOracle();
//		}
//		else if (dbType.equals(DatabaseType.PostgreSQL)) {
//			JDBC.loadDriverPGSQL();
//		}
//		else if (dbType.equals(DatabaseType.SQLServer)) {
//			JDBC.loadDriverSQLServer();
//		}
//	}

}
