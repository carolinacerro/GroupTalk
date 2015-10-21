package edu.upc.eetac.dsa.grouptalk;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by Carolina on 21/10/2015.
 */
public class GrouptalkResourceConfig extends ResourceConfig {
    public GrouptalkResourceConfig() {
        packages("edu.upc.eetac.dsa.grouptalk");
    }
}
