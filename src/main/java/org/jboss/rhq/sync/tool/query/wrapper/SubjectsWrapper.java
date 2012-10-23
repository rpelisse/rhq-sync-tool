package org.jboss.rhq.sync.tool.query.wrapper;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.criteria.Criteria;
import org.rhq.core.domain.criteria.SubjectCriteria;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.server.auth.SubjectManagerRemote;


public class SubjectsWrapper extends AbstractQueryWrapper<Subject> {

    private SubjectManagerRemote subjectManager;
	
	public SubjectsWrapper(SubjectManagerRemote subjectManagerRemote, Subject subject) {
		super(subject);
		this.subjectManager = subjectManagerRemote;
	}
	
	@Override
	public PageList<Subject> doPagedQuery(Criteria criteria) {
		return ( criteria instanceof SubjectCriteria ) ? subjectManager.findSubjectsByCriteria(this.subject, (SubjectCriteria)criteria) : invalidCriteriaInstance(criteria);
	}
}
