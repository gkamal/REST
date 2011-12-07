package org.rest.common.dao.jpa;

import java.io.Serializable;

import org.rest.common.dao.IGenericDAO;
import org.springframework.stereotype.Repository;

@Repository
public class GenericJpaDAO< T extends Serializable > extends AbstractJpaDAO< T > implements IGenericDAO< T >{
	
	public GenericJpaDAO(){
		super();
	}
	
}
