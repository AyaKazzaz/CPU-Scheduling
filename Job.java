/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author WinDows
 */
public class Job {
private String type;
private int ID;
private int ArrT;
private int memory;
private int dev;
private int R;
private int p;
private double ppr;
private int finishTimeOutOfReady;
 private int startTimeInReady;
 private double pwt;
 private int finishTime;
 private int startTime;
 private int accuredTime;
 //private boolean PToSort;
private int turnArround=0;  

public Job(int ArrT, int ID,int memory,int dev,int R,int p)   {
 this.ArrT=ArrT;
 this.ID=ID;
 this.memory= memory;
 this.dev=dev;
 this.p=p;
 this.R=R;
 this.startTime=0;
 this.finishTime=0;
 this.accuredTime=0;
 this.ppr=-1;
 //this.PToSort=true;
 this.turnArround=this.finishTime-this.ArrT;
 this.pwt=0;
}

    public int getTurnArround() {
        return turnArround;
    }

    public void setTurnArround(int turnArround) {
        this.turnArround = turnArround;
    }

    
//
//    public void setPToSort(boolean PToSort) {
//        this.PToSort = PToSort;
//    }
//    public boolean getPToSort() {
//        return PToSort;
//    }
    public void setPpr(double ppr) {
        this.ppr = ppr;
    }

    public double getPpr() {
        return ppr;
    }

    public int getFinishTimeOutOfReady() {
        return finishTimeOutOfReady;
    }

    public int getStartTimeInReady() {
        return startTimeInReady;
    }

    public double getPwt() {
        return pwt;
    }

    public void setFinishTimeOutOfReady(int finishTimeOutOfReady) {
        this.finishTimeOutOfReady = finishTimeOutOfReady;
    }

    public void setStartTimeInReady(int startTimeInReady) {
        this.startTimeInReady = startTimeInReady;
    }

    public void setPwt(double pwt) {
        this.pwt = pwt;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getArrT() {
        return ArrT;
    }

    public void setArrT(int ArrT) {
        this.ArrT = ArrT;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDev() {
        return dev;
    }

    public void setDev(int dev) {
        this.dev = dev;
    }

    public int getR() {
        return R;
    }

    public void setR(int R) {
        this.R = R;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public int getAccuredTime() {
        return accuredTime;
    }

    public void setAccuredTime(int accuredTime) {
        this.accuredTime = accuredTime;
    }
}



