package org.salespointframework.core.catalog;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.util.Objects;

public abstract class AbstractProductCatalog<T extends AbstractProductType> implements ProductCatalog<T>, ICanHasClass<T> {

	private EntityManager entityManager;
	
	public AbstractProductCatalog(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
		
	}
	
	@Override
	public void addProductType(T productType) {
		Objects.requireNonNull(productType, "productType");
		entityManager.persist(productType);
	}
	
	@Override
	public boolean removeProductType(int productIdentifier) {
		//TODO Errors
		entityManager.remove(this.findProductTypeByProductIdentifier(productIdentifier));
		return false;
	}

	@Override
	public T findProductTypeByProductIdentifier(int productIdentifier) {
		return entityManager.find(this.getClassPLZ(), productIdentifier);
	}

	@Override
	public T findProductTypeByName(String name) {
		Objects.requireNonNull(name, "name");
		return null;
	}

	@Override
	public Iterable<T> findProductTypesByCategory(String category) {
		Objects.requireNonNull(category, "category");
		TypedQuery<T> tquery = entityManager.createQuery("",this.getClassPLZ());
		return tquery.getResultList();
	}

	@Override
	public Iterable<CatalogEntry<T>> getCatalogEntries() {
		return null;
	}

	@Override
	public void clear() {
	
	}
}
