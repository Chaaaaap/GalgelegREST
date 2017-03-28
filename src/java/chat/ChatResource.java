/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chat;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Mikkel
 */
@Path("/chat")
public class ChatResource {
    
    protected Message first;
    protected Message last;
    
    protected int maxMessages = 10;
    protected LinkedHashMap<String, Message> messages = new LinkedHashMap<String, Message>() {
        
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Message> eldest) {
            boolean remove = size() > maxMessages;
            if(remove) first = eldest.getValue().getNext();
            return remove;
        }
    };
    
    protected AtomicLong counter = new AtomicLong(0);
    protected LinkedList<AsyncResponse> listeners = new LinkedList<AsyncResponse>();
    protected ExecutorService writer = Executors.newSingleThreadExecutor();
    
    
    @Context
    protected UriInfo uriInfo;
    
    @POST
    @Consumes("text/plain")
    public void post(final String msg) {
        final UriBuilder base = uriInfo.getBaseUriBuilder();
        writer.submit(new Runnable() {
            @Override
            public void run() {
                synchronized(messages) {
                    Message message = new Message();
                    message.setID(counter.incrementAndGet());
                    message.setMsg(msg);
                    
                    if(messages.isEmpty()) {
                        first = message;
                    } else {
                        last.setNext(message);
                    }
                    
                    messages.put(message.getID()+"", message);
                    last = message;
                    
                    for(AsyncResponse async: listeners) {
                        try {
                            send(base, async, message);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listeners.clear();
                }
            }

            @GET
            public void recieve(@QueryParam("current") String id, @Suspended AsyncResponse async) {
                final UriBuilder base = uriInfo.getBaseUriBuilder();
                Message message = null;
                synchronized(messages) {
                    Message current = messages.get(id);
                    if(current == null) message = first;
                    else message = current.getNext();
                    
                    if(message == null) {
                        queue(async);
                    }
                }
                if(message != null) {
                    send(base, async, message);
                }
            }
            
            private void send(UriBuilder base, AsyncResponse async, Message message) {
                URI nextUri = base.clone().path(ChatResource.class).queryParam("current", message.getID()).build();
                Link next = Link.fromUri(nextUri).rel("next").build();
                Response response = Response.ok(message.getMsg(), MediaType.TEXT_PLAIN_TYPE).links(next).build();
                async.resume(response);
            }

            private void queue(AsyncResponse async) {
                listeners.add(async);
            }
        }); 
        
    }
}
