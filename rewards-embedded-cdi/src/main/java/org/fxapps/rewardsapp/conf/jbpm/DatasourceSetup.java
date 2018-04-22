package org.fxapps.rewardsapp.conf.jbpm;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class DatasourceSetup {

	private static final String DS_JNDI = "jdbc/jbpm-ds";
	private PoolingDataSource ds;

	public void buildH2Datasource() {
		ds = new PoolingDataSource();
		ds.setUniqueName(DS_JNDI);

		// NON XA CONFIGS
		ds.setClassName("org.h2.jdbcx.JdbcDataSource");
		ds.setMaxPoolSize(3);
		ds.setAllowLocalTransactions(true);
		ds.getDriverProperties().put("user", "sa");
		ds.getDriverProperties().put("password", "sasa");
		ds.getDriverProperties().put("URL", "jdbc:h2:mem:mydb");
		ds.init();
	}
	
	public void buildMySQLDatasource() {
		ds = new PoolingDataSource();
		ds.setUniqueName(DS_JNDI);

		// NON XA CONFIGS
		ds.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
		ds.setMaxPoolSize(3);
		ds.setAllowLocalTransactions(true);
		ds.getDriverProperties().put("user", "root");
		ds.getDriverProperties().put("password", "");
		ds.getDriverProperties().setProperty("databaseName", "jbpm64_embedded");
		ds.init();
	}


	public void closeDataSource() {
		if (ds != null) {
			ds.close();
		}
	}

}
