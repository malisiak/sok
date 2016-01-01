package pl.gda.pg.ds.sok.services.impl;

import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import pl.gda.pg.ds.sok.entities.Task;
import pl.gda.pg.ds.sok.services.TasksService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tasks")
@Api(description = "List Tasks")
public class TasksServiceImpl extends AbstractService implements TasksService {
	
	private static final Logger logger = Logger.getLogger(TasksServiceImpl.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("unchecked")
	public Response getTasks() {
		try {
			Query query = session.createQuery("from Task");

			List<Task> resultList = query.list();
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
}
