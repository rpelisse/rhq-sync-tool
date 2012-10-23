package org.jboss.rhq.jon.mig.actions.impl.impex.templates;

import static org.jboss.rhq.jon.mig.actions.impl.impex.RoleSerializer.loadJsonRolesCollections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.BaseRemote;
import org.jboss.rhq.jon.mig.LoginConfiguration;
import org.jboss.rhq.jon.mig.actions.JonActionResult.JonActionResultType;
import org.jboss.rhq.jon.mig.actions.impl.AbstractJONImportAction;
import org.jboss.rhq.jon.mig.query.SubjectsAndRolesQueryImpl;
import org.jboss.rhq.jon.mig.util.DomainCollectionsUtils;
import org.jboss.rhq.jon.mig.util.FileUtils;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class RolesImportAction extends AbstractJONImportAction<Collection<Role>> implements ImportStrategy<Role> {

    private static Logger logger = Logger.getLogger(RolesImportAction.class);

    private ImportStrategy<Subject> subjectImporter = new SubjectImporter();
    
	/**
	 * @param loginConfiguration
	 * @param baseRemote
	 */
	public RolesImportAction(LoginConfiguration loginConfiguration,
			BaseRemote baseRemote) {
		super(loginConfiguration, baseRemote);
	}

	/**
	 * <p>Default constructor - inherits remote login and base</p>
	 */
	public RolesImportAction() {}

	@Override
    protected Collection<Role> loadFromFile(String filename) {
    	if ( logger.isDebugEnabled() )
    		logger.debug("invoked with " + filename);
        return loadJsonRolesCollections(FileUtils.read(filename));
    }
	
	@Override
	protected JonActionResultType doImport(Collection<Role> roles) {
		long startTime = System.currentTimeMillis();
		if ( logger.isDebugEnabled() )
			logger.debug("1. Regroups all users of all roles, index by their username.");
		// *BE CAREFUL* Huge simplification is done here, we assume
		// that 'username' are UNIQUES.
		Map<String /*username*/, Subject> rolesSubjects = DomainCollectionsUtils.indexSubjectsFromRolesByName(roles);
		if ( logger.isDebugEnabled() )
			logger.debug("2. Fetch all \"existing user\" from the previous list (one remote call, hopefully).");
		Map<String, Subject> existingSubject = subjectImporter.retrieveExistingItems(rolesSubjects);
		if ( logger.isDebugEnabled() )
			logger.debug("3. Create missing Subjects (one remote call) - if any.");
		Map<String,Subject> createdSubjects = subjectImporter.importItem(subjectImporter.determineItemsToCreate(rolesSubjects,existingSubject));
		Map<String,Role> providedRoles = DomainCollectionsUtils.indexRoleByName(roles);
		Map<String,Role> existingRoles = retrieveExistingItems(providedRoles);		
		Map<String,Role> unexistingRoles = determineItemsToCreate(providedRoles,existingRoles);

		Set<Role> addedRoles = new HashSet<Role>(roles.size());
		int id = 0;
		for ( Role role : roles ) {
			if ( logger.isDebugEnabled() )
				logger.debug("4.(" + id + ") Create Role " + role.getName() + " without subjects (one remote call, could be concurrent to the previous remote call, but most likely not worth it)");

			// Removes Subjects as having subjects in the role leads to a transport error !
			Collection<Subject> subjects = DomainCollectionsUtils.extractsSubjectsFromRole(role);
			// create role and fetch id back
			if ( unexistingRoles.containsKey(role.getName()) && checkIfRoleCanBeAdded(role) ) {
				role.setId(0);
				role = new SubjectsAndRolesQueryImpl().addRole(role);
			}
			else {
				if ( ! existingRoles.containsKey(role.getName())) {
					throw new IllegalStateException("Role " + role.getName() + "is neither existing, neither to create...");
				} else
					role = existingRoles.get(role.getName());
			}
			addedRoles.add(role);
			if ( logger.isDebugEnabled() )
				logger.debug("5. (" + id++ + ") Add subjects to appropriate role");
			if ( ! subjects.isEmpty() ) {
				new SubjectsAndRolesQueryImpl().addSubjectsToRole(role, subjectToAlter(subjects, existingSubject, createdSubjects));
			}
		}
		logger.warn("Overall import operation execution time:" + (System.currentTimeMillis() - startTime) + "ms.");		
		if ( logger.isDebugEnabled() ) {
			logger.debug("The following roles have been successfully imported:");
			for ( Role role : addedRoles )
				logger.debug(role);
		}
		return JonActionResultType.SUCCESS;
	}

	//FIXME: Cut'n'paste from org.rhq.enterprise.server.authz.AuthorizationManagerBean (sadly not public)
	// 		 import and use proper bean ?
    private static final int SUBJECT_ID_OVERLORD = 1;
    private static final int SUBJECT_ID_RHQADMIN = 2;

	private boolean subjectCanBeAltered(Subject subjectToAlter) {
		return subjectToAlter.getId() != SUBJECT_ID_OVERLORD && subjectToAlter.getId() != SUBJECT_ID_RHQADMIN;
	}

	private boolean checkIfRoleCanBeAdded(Role role) {
		boolean fsystemRole = role.getFsystem();
		if ( fsystemRole )
			logger.warn("Role " + role.getName() + " is a system role and can't be imported");
		return ! fsystemRole;
	}
		
	private Set<Subject> subjectToAlter(Collection<Subject> subjects, Map<String, Subject> existingSubject,Map<String, Subject> createdSubjects) {
		Set<Subject> subjectsToAlter = new HashSet<Subject>(subjects.size());
		for ( Subject subject : subjects ) {
			Subject subjectToAlter = DomainCollectionsUtils.findSubject(subject, existingSubject, createdSubjects);
			if ( subjectCanBeAltered(subjectToAlter) )
				subjectsToAlter.add(subjectToAlter);
		}
		return subjectsToAlter;
	}

	@Override
	public Map<String, Role> retrieveExistingItems(
			Map<String, Role> providedRoles) {
		Map<String,Role> existingRolesCollection = new HashMap<String,Role>(0);
		// FIXME: find a away to do this search in one request
		for ( Role role : providedRoles.values() ) {
			existingRolesCollection.putAll(DomainCollectionsUtils.indexRoleByName(new SubjectsAndRolesQueryImpl().findRolesByNames(role)));
		}
		return existingRolesCollection;
	}

	@Override
	public Map<String, Role> determineItemsToCreate(
			Map<String, Role> providedItems,
			Map<String, Role> existingRoles) {
		return DomainCollectionsUtils.removeExistingItemsFromProvided(existingRoles,providedItems);
	}

	@Override
	public Map<String, Role> importItem(Map<String, Role> rolesToCreate) {
		throw new UnsupportedOperationException("Not implemented.");
	}

}
