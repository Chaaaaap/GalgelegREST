/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highscore;

import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;


/**
 *
 * @author Mikkel
 */

@Path("/highscore")
public class HighscoreResource {
    
    @PUT
    @Consumes("text/plain")
    public void saveHighscore() {
        
    }
    
    @GET
    @Produces("text/plain")
    public Score getHighscore() {
        return new Score();
    }
    
    
}
