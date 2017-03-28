/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.util.Date;

/**
 *
 * @author Mikkel
 */
public class Message {
    
    private long id;
    private String msg;
    private Message next;
    private Date timestamp;
 
    
    public Message getNext() {
        return next;
    }
    
    public void setNext(Message next) {
        this.next = next;
    }
    
    public void setID(long id) {
        this.id = id;
    }
    
    public long getID() {
        return id;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return msg;
    }
}
