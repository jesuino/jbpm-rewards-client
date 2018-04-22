package org.fxapps.rewardsapp.conf.jbpm;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.cdi.Kjar;
import org.jbpm.services.task.audit.JPATaskLifeCycleEventListener;
import org.jbpm.services.task.lifecycle.listeners.TaskLifeCycleEventListener;

@ApplicationScoped
public class EnvironmentProducer {

	@PersistenceContext(unitName = "org.jbpm.domain")
	private EntityManagerFactory emf;

	@Produces
	@ApplicationScoped
	public EntityManagerFactory produceEntityManagerFactory() {
        if (this.emf == null) {
            this.emf = Persistence
                    .createEntityManagerFactory("org.jbpm.domain");
        }
        return this.emf;
    }

	@Inject
	@Kjar
	private Instance<DeploymentService> deploymentService;

	@Produces
	public DeploymentService produceDeploymentService() {
		return deploymentService.select(new AnnotationLiteral<Kjar>() {
			private static final long serialVersionUID = 1L;
		}).get();
	}
	
    /**
     * 
     * Add this method if you want that audit events will be stored in the database.
     * 
     * @return
     */
    @Produces
    public TaskLifeCycleEventListener produceAuditListener() {
    	return new JPATaskLifeCycleEventListener(true);
    }

}