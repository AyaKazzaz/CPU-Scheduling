/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author WinDows
 */
 public class CPU {
  Job JInCPU;

  boolean free;

    public CPU(Job JInCPU, boolean free) {
        this.JInCPU = JInCPU;
        this.free = free;
    }

 

    public Job getJobs() {
        return JInCPU;
    }

    public void setJobs(Job jobs) {
        this.JInCPU = jobs;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
 
    
}

