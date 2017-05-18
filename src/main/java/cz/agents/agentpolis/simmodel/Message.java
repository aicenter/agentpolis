/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel;

/**
 *
 * @author fido
 */
public class Message {
    private final Object content;
    
    private final Enum type;

    public Object getContent() {
        return content;
    }

    public Enum getType() {
        return type;
    }
    
    

    public Message(Enum type, Object content) {
        this.content = content;
        this.type = type;
    }
    
    
}
