/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel;

import com.google.inject.Inject;

/**
 *
 * @author fido
 */
public class IdGenerator {
    
    private int currentId;

    
    
    @Inject
    public IdGenerator() {
        this.currentId = 0;
    }
        

    public int getId(){
        int id = currentId;
        currentId++;
        return id;
    }
}
