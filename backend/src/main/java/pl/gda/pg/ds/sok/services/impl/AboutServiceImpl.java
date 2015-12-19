package pl.gda.pg.ds.sok.services.impl;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import pl.gda.pg.ds.sok.services.AboutService;
import pl.gda.pg.ds.sok.utils.PropertiesUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/about")
public class AboutServiceImpl implements AboutService {

	private static final Logger logger = Logger.getLogger(AboutServiceImpl.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAbout() {
		Map<String,String> about = Maps.newHashMap();
		about.put("build", PropertiesUtil.getProperty("app.build"));
		about.put("version", PropertiesUtil.getProperty("app.version"));
		return Response.ok(about).build();
	}
}
