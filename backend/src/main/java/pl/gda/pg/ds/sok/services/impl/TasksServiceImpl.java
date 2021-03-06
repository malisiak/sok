package pl.gda.pg.ds.sok.services.impl;

import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import pl.gda.pg.ds.sok.beans.TaskBean;
import pl.gda.pg.ds.sok.beans.interfaces.TaskBeanInterface;
import pl.gda.pg.ds.sok.entities.Candidate;
import pl.gda.pg.ds.sok.entities.Task;
import pl.gda.pg.ds.sok.services.TasksService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tasks")
@Api(description = "List and create tasks")
public class TasksServiceImpl extends AbstractService implements TasksService {
	
	private static final Logger logger = Logger.getLogger(TasksServiceImpl.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("unchecked")
	public Response getTasks() {
		try {
			Query query = session.createQuery("select id as id, title as title, type as type, content as content, difficulty as difficulty from Task");
			query.setResultTransformer(Transformers.aliasToBean(TaskBean.class));
			List<TaskBeanInterface> resultList = query.list();
			if (resultList.size() > 0) {
				return Response.ok(resultList).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			logger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			session.close();
		}
	}

	@PUT
	@Path("/{authToken}/newTask")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTask(@Context HttpServletRequest request, @PathParam("authToken") String authToken, TaskBean task) {
		try {
			if (!canAdmin(authToken)) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

			session.beginTransaction();

			Query query;
			StringBuilder queryString = new StringBuilder("from Candidate where token = :token");
			query = session.createQuery(queryString.toString());
			query.setParameter("token", authToken);
			Candidate admin = (Candidate) query.uniqueResult();

			session.save(new Task(task.getTitle(), task.getType(), task.getContent(), task.getDifficulty(), admin));
			session.getTransaction().commit();
			return Response.status(Response.Status.CREATED).build();
		} catch (ConstraintViolationException e) {
			logger.error(e);
			session.getTransaction().rollback();
			return Response.status(Response.Status.CONFLICT).build();
		} catch (Exception e) {
			logger.error(e);
			session.getTransaction().rollback();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			session.close();
		}
	}
}
