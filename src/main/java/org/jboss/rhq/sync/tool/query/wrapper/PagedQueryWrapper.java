package org.jboss.rhq.sync.tool.query.wrapper;

import org.rhq.core.domain.criteria.Criteria;
import org.rhq.core.domain.util.PageList;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public interface PagedQueryWrapper<T> {
	PageList<T> doPagedQuery(Criteria criteria);
}
