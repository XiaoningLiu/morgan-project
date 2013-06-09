/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author liuxiaoning
 */
public class Trader {
    
    public String traderId;
    private String psd;
    
    public Trader(){
        this.traderId = null;
        this.psd = null;
    }
    
    // get Trader from database
    public Trader(Con con, String traderId){
        
    }
    
    public boolean checkPsd(String psd){
        if (psd.equals(this.psd)){
            return true;
        }
        return false;
    }
}
