package org.rest.common.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.rest.common.dao.ICommonOperations;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

@Transactional( propagation = Propagation.SUPPORTS )
public abstract class AbstractJpaDAO< T extends Serializable > implements ICommonOperations< T >, InitializingBean{
	private Class< T > clazz;
	
	// @Autowired
	// EntityManagerFactory emf;
	
	private EntityManager em;
	
	public AbstractJpaDAO(){
		super();
	}
	public AbstractJpaDAO( final Class< T > clazzToSet ){
		this.clazz = clazzToSet;
	}
	
	//
	@PersistenceContext( unitName = "pUnit" )
	public void setEm( final EntityManager emToSet ){
		this.em = emToSet;
	}
	
	public void setClazz( final Class< T > clazzToSet ){
		this.clazz = clazzToSet;
	}
	
	// get
	
	@Override
	@Transactional( readOnly = true )
	public T getById( final Long id ){
		Preconditions.checkArgument( id != null );
		
		return this.getCurrentEntityManager().find( this.clazz, id );
	}
	
	@Override
	@Transactional( readOnly = true )
	public List< T > getAll(){
		return this.getCurrentEntityManager().createQuery( "from " + this.clazz.getName() ).getResultList();
	}
	
	// create/persist
	
	@Override
	public void create( final T entity ){
		Preconditions.checkNotNull( entity );
		
		this.getCurrentEntityManager().persist( entity );
	}
	
	// update
	
	@Override
	public void update( final T entity ){
		Preconditions.checkNotNull( entity );
		
		this.getCurrentEntityManager().merge( entity );
	}
	
	// delete
	
	@Override
	public void delete( final T entity ){
		Preconditions.checkNotNull( entity );
		
		this.getCurrentEntityManager().remove( entity );
	}
	
	public void deleteById( final Long entityId ){
		final T entity = this.getById( entityId );
		Preconditions.checkState( entity != null );
		
		this.delete( entity );
	}
	
	// util
	
	protected EntityManager getCurrentEntityManager(){
		// return this.emf.createEntityManager();
		return this.em;
	}
	
	//
	@Override
	public final void afterPropertiesSet(){
		// Preconditions.checkNotNull( this.emf );
		Preconditions.checkNotNull( this.em );
	}
	
}
