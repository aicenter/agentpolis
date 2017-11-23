/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

/**
 *
 * @author fido
 */
public class VisualTests {
    public static boolean SHOW_VISIO = false;
    
    public static void runVisualTest(Class testClass){
        SHOW_VISIO = true;
//        JUnitCore.runClasses(testClass);
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(testClass);
    }
}
