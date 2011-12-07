package org.rest.spring.persistence.jpa;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceJPAConfig{
	
	@Value( "${driverClassName}" )
	private String driverClassName;
	
	@Value( "${url}" )
	private String url;
	
	@Value( "${hibernate.dialect}" )
	String hibernateDialect;
	
	@Value( "${hibernate.show_sql}" )
	boolean hibernateShowSql;
	
	@Value( "${hibernate.hbm2ddl.auto}" )
	String hibernateHbm2ddlAuto;
	
	// beans
	
	/*
	<property name="jpaVendorAdapter">
	   <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
	      <property name="databasePlatform" value="org.hibernate.dialect.MySQL5InnoDBDialect" />
	      <property name="showSql" value="true" />
	      <property name="generateDdl" value="true" />
	      </bean>
	</property>
	 */
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource( this.restDataSource() );
		factoryBean.setPackagesToScan( new String[ ] { "org.rest" } );
		factoryBean.setPersistenceUnitName( "pUnit" );
		
		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter(){
			{
				this.setDatabasePlatform( PersistenceJPAConfig.this.hibernateDialect );
				this.setShowSql( PersistenceJPAConfig.this.hibernateShowSql );
				this.setGenerateDdl( true );
			}
		};
		factoryBean.setJpaVendorAdapter( vendorAdapter );
		return factoryBean;
	}
	
	@Bean
	public DataSource restDataSource(){
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName( this.driverClassName );
		dataSource.setUrl( this.url );
		dataSource.setUsername( "restUser" );
		dataSource.setPassword( "restmy5ql" );
		return dataSource;
	}
	
	@Bean
	public JpaTransactionManager transactionManager(){
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( this.entityManagerFactory().getObject() );
		
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	// util
	
	final Properties persistenceProperties(){
		return new Properties(){
			{
				this.put( "hibernate.dialect", PersistenceJPAConfig.this.hibernateDialect );
				this.put( "hibernate.hbm2ddl.auto", PersistenceJPAConfig.this.hibernateHbm2ddlAuto );
				this.put( "hibernate.show_sql", PersistenceJPAConfig.this.hibernateShowSql );
			}
		};
	}
	
}
