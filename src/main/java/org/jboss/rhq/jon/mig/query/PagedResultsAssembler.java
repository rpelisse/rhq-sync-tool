package org.jboss.rhq.jon.mig.query;

import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.query.wrapper.PagedQueryWrapper;
import org.rhq.core.domain.criteria.Criteria;
import org.rhq.core.domain.util.PageList;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class PagedResultsAssembler {

	private static final Logger logger = Logger.getLogger(PagedResultsAssembler.class);

	private static final int START_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	
	private int pageStart = START_PAGE;
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	public PagedResultsAssembler(){};
	
	public PagedResultsAssembler(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public PagedResultsAssembler(int pageStart, int pageSize) {
		this.pageSize = pageSize;
		this.pageSize = pageStart;
	}

	public <T> Collection<T> gatherPaginatedResults(Criteria criteria,
			PagedQueryWrapper<T> wrapper) {
		long starTimestamp = System.currentTimeMillis();
		PageList<T> gatheredResults = null;
		int pageId = pageStart;
		Collection<T> existingItems = new HashSet<T>(0);
		do {
			gatheredResults = wrapper.doPagedQuery(criteria);
			for (T existing : gatheredResults)
				existingItems.add(existing);
			criteria.setPaging(++pageId, pageSize);
			// FIXME: isn't there a better way to handle paging than that ?
		} while (gatheredResults != null && !gatheredResults.isEmpty()
				&& gatheredResults.size() == pageSize);
		logger.warn("Gathering paged results took: "
				+ (System.currentTimeMillis() - starTimestamp) + "ms, with "
				+ existingItems.size());
		return existingItems;
	}
}
