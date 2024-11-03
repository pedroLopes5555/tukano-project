package tukano.impl.rest;


import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;
import tukano.impl.Token;
import utils.IP;

public class TukanoRestServerTomcat extends Application
{
	private Set<Object> singletons = new HashSet<>();
	private Set<Class<?>> resources = new HashSet<>();


	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
	}
	
	
	public TukanoRestServerTomcat() {
	
		TukanoRestServer.serverURI = String.format(TukanoRestServer.SERVER_BASE_URI, IP.hostname(), TukanoRestServer.PORT);
		System.out.println(String.format("################SERVER URI : %s\n",  TukanoRestServer.serverURI));

		resources.add(RestBlobsResource.class);
		resources.add(RestUsersResource.class); 
		resources.add(RestShortsResource.class);

		Token.setSecret("");
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
