package org.jboss.rhq.sync.tool.query.wrapper;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.criteria.Criteria;
import org.rhq.core.domain.util.PageList;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public abstract class AbstractQueryWrapper<T> implements PagedQueryWrapper<T> {
   protected Subject subject;
	
	public AbstractQueryWrapper(Subject subject) {
		this.subject = subject;
	}
	
	protected PageList<T> invalidCriteriaInstance(Criteria criteria) {
		throw new IllegalStateException("Invoked with an invalid " + Criteria.class + ":" + criteria.getClass().getName());
	}

}
