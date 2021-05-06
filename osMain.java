import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

//processor:Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz 2.00 GHz
//Installed memory(RAM): 8.00 GB(7.90 GB usable)
//compiler name and version:java version "1.8.0_191" ,Java(TM) SE Runtime Environment

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author WinDows
 */
public class osMain {
 static int memorySize;
static int devices;
static int time;
static Queue<Job>allJobQ=new LinkedList();
static Queue<Job>holdQ1=new LinkedList();
static Queue<Job>holdQ2=new LinkedList();
static Queue<Job>sortedHoldQ2=new LinkedList();
static CPU cpu=new CPU(null,true);
static Queue<Job>completeQ=new LinkedList();
static int cStartTime=0;
static int availableMem=0;
static int availbleDev=0;
static int cThrehold=0;
static int numOfJob=0;
static int Quantum=0;
public static int i;
public static int e;
public static int sr;
public static int ar;
 

    public static void main(String[] args) throws FileNotFoundException {
    java.io.File file = new java.io.File("input3.txt");
     // java.io.File file = new java.io.File("input2.txt");you can change between these 3 input files to check outputs files
    //java.io.File file = new java.io.File("input3.txt");
    java.io.File outputFile = new java.io.File("output.txt");
    PrintWriter output=new PrintWriter(outputFile);//print writer to write to the outputfile
   
    Scanner input=new Scanner(file);
    while (input.hasNext()) {
        
     String command1=input.next();
       
     time=input.nextInt();
     
     memorySize=Integer.parseInt(input.next().substring(2));
     availableMem=memorySize;
     
     devices=Integer.parseInt(input.next().substring(2));
      
       availbleDev=devices;
     Job job;
     numOfJob=0;
     int number = 0;
     do{
         
         String command2=input.next();
       
         if(command2.equals("A")){
           int ArrT= Integer.parseInt(input.next());
          
           int ID=Integer.parseInt(input.next().substring(2));
         
           int memory=Integer.parseInt(input.next().substring(2));
         
           int dev=Integer.parseInt(input.next().substring(2));
           
           int R=Integer.parseInt(input.next().substring(2));
          
           int p=Integer.parseInt(input.next().substring(2));
          
           if(memory<=memorySize&&dev<=devices){
             job=new Job(ArrT,ID,memory,dev,R,p);
           
             allJobQ.add(job);
             numOfJob++;//counter to count the number of jobs

           }
         }else if(command2.equals("D")){
             
             int time1=input.nextInt();
            
             if(time1<999999){
              job=new Job(time1,-1,-1,-1,-1,-1); 
                 
              allJobQ.add(job);
             }
             else{
            
             break;
         }
         }
         } while(input.hasNext());
           Job j=allJobQ.poll();
         
           int currentTime=j.getArrT();
          
           availableMem-=j.getMemory();
           Quantum=j.getR();//take the first job and set the quantum to its burst time
        
           getIntoCPU(j,currentTime);
           
            i = 0;
            e = 0;

            do {
               
                if (!(allJobQ.isEmpty())) {
                 
                    i = allJobQ.peek().getArrT();
                } else {
                    i = Integer.MAX_VALUE;
                }
               
               
                if (cpu.isFree()==false) {
                    e = cpu.JInCPU.getFinishTime();
                   
                } else {
                    e = Integer.MAX_VALUE;
                }
                currentTime = Math.min(i, e);
               
                if (i < e) {
                    
                    External(currentTime, output);
                } else if (e < i) {
                   
                    Internal(currentTime);
                } else {
                    
                    Internal(currentTime);
                    External(currentTime, output);
                    
                    
                }
                Iterator<Job> value = completeQ.iterator();
      
      
            } while (completeQ.size() != numOfJob);
            DisplayFinalState(currentTime,output);//displaying the final state
            completeQ.clear();
    }
   
    input.close();
    output.close();
    }
    public static void External(int currentTime, PrintWriter output){
      
        if(!allJobQ.isEmpty()){
            if(allJobQ.peek().getID()== -1){
               
                allJobQ.poll();
              
                    Display(currentTime,output);
               
            }else {
                    Job job=allJobQ.poll();
                    if(job.getMemory()>availableMem || job.getDev()>availbleDev){
                       
                        holdQ2.add(job);
                        job.setStartTimeInReady(currentTime);//start timer when a job enter the holdQ2
                      
                    }else if(job.getMemory()<=availableMem && job.getDev()<=availbleDev){
                       
                        holdQ1.add(job);
                        availableMem-=job.getMemory();
                        availbleDev-=job.getDev();
                        
                        update_SR_AR();
//                        
//                      
                    }
            }
        }
    }
    public static void Internal(int CurrentTime) {

        getOutOfCPU(CurrentTime);
        
       
        if (!holdQ1.isEmpty()) {
            
            update_SR_AR();
            Quantum=ar;
            getIntoCPU(holdQ1.poll(), CurrentTime);
        }

    }
    public static void update_SR_AR() {
        if (holdQ1.isEmpty()) {//if no element , to avoid dividing over 0
            ar = sr = 0;
            return;
        }
       
        Iterator<Job> value = holdQ1.iterator();        
        Job o ;
        
       
        while (value.hasNext()) {
             o = value.next();  
            if (o.getP() == 1) {
                sr += (o.getR()-o.getAccuredTime()) * 2;
            } else {
                sr += (o.getR()-o.getAccuredTime()) * 1;
            }  
               
        }

        
        ar = sr / (holdQ1.size());  
        
        Quantum=ar;
        sr=0;
    }
    public static void Update_OUT_Hold(int currentTime){
        Iterator<Job> value = holdQ2.iterator();
        Job o ;
      
       
        double avgWT;
        double sumWT=0;
        while (value.hasNext()) {
             o = value.next();
                           
                o.setFinishTimeOutOfReady(currentTime);//set finish time in holdQ2
               
                o.setPwt(o.getFinishTimeOutOfReady()-o.getStartTimeInReady());//set proccess waiting time

                sumWT+=o.getPwt();
              
             }
        
              avgWT=sumWT/holdQ2.size();   
          
       
         value = holdQ2.iterator();
        while (value.hasNext()) {
             o = value.next();
             if(o.getPpr()==-1){
                 o.setPpr(o.getP());
              
             }else{                 
                if(o.getPwt()-avgWT>0){
               double DP = (o.getPwt()-avgWT)*0.2+o.getPpr()*0.8;
               o.setPpr(DP);
                
            }
                

             }
                                               
        }
        
       
       
    }
    public static void Get_OUT_Hold(int currentTime) {

        if(!holdQ2.isEmpty()){
            
            Update_OUT_Hold(currentTime);
       
       Job [] sortingArray= new Job[holdQ2.size()];
       Job o;
        Iterator<Job> value =holdQ2.iterator();
        while (value.hasNext()){
                  
          o=value.next();
          
        }
         value =holdQ1.iterator();
        while (value.hasNext()){//copy element to array
                  
          o=value.next();
            
        }
        CopyQueueToArray(sortingArray , holdQ2);
        
            for (int j = 0; j < sortingArray.length; j++) {
              
            }
        sortArray(1,sortingArray, holdQ2);
        holdQ2.clear();
            for (int j = 0; j < sortingArray.length; j++) {
              
                holdQ2.add(sortingArray[j]);
            }
            int sizeq2=holdQ2.size();
       for(int i=0;i<sizeq2;i++){
           Job temp=holdQ2.poll();
            if(temp.getMemory()<=availableMem && temp.getDev()<=availbleDev){
                 
                  
                holdQ1.add(temp);
                availableMem-=temp.getMemory();
                availbleDev-=temp.getDev();
                     
               update_SR_AR();
            }else{
                holdQ2.add(temp);
              
            }
            

        }
   
       
       
        }
    }
    public static void CopyQueueToArray(Job arr[] , Queue<Job> h){
      
        int k =0;
        Job o;
        Iterator<Job> value = h.iterator();
        while (value.hasNext()){
                  
          o=value.next();
           arr[k]=o;
         
          k++;
        }
      
    }
    public static void sortArray(int type,Job arr[], Queue<Job> h) {
        
        Job temp;
       if(type==1){
           for (int i = 0; i < arr.length; i++) 
        {
            for (int j = i+ 1; j < arr.length; j++) 
            {
                
                if (arr[i].getPpr() < arr[j].getPpr()) 
                {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
                else if(arr[i].getPpr() == arr[j].getPpr()){//if same jobs have the same ppr then check the IDs
                    if (arr[i].getID() > arr[j].getID()) {
                
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                
                }
            }
        }
       }
       }else{
            for (int i = 0; i < arr.length; i++) 
        {
            for (int j = i + 1; j < arr.length; j++) 
            {
                if (arr[i].getID() > arr[j].getID()) 
                {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
       }
        
        h.clear();
        for (int j = 0; j < arr.length; j++) {
           
            h.add(arr[j]);
        }

    }
  
    public static void getIntoCPU(Job job,int currentTime){
    cpu.setJobs(job);
        
    cpu.setFree(false);
       
    cpu.JInCPU.setStartTime(currentTime);
    int RemainingTime=cpu.JInCPU.getR()-cpu.JInCPU.getAccuredTime();//set remaining time
     
    if(RemainingTime>= Quantum){
        cpu.JInCPU.setFinishTime(currentTime+Quantum);
    }else{
       
        cpu.JInCPU.setFinishTime(currentTime+RemainingTime);
    }
       
       
    }
   
    public static void getOutOfCPU(int CurrentTime){
        
        if (cpu.isFree()== false) {
          
            
            cpu.JInCPU.setAccuredTime(cpu.JInCPU.getAccuredTime()+(cpu.JInCPU.getFinishTime() - cpu.JInCPU.getStartTime()));
           
            if (cpu.JInCPU.getAccuredTime() == cpu.JInCPU.getR()) {
                availableMem += cpu.JInCPU.getMemory();
                availbleDev += cpu.JInCPU.getDev();
              
               
                completeQ.add(cpu.JInCPU);

                cpu.setFree(true);
               
                cpu.JInCPU=null;
                if(!holdQ2.isEmpty()){//when the job left the cpu it release its memory and devices so we can move job from holdQ2 to holdQ1
                   
                   
                    Get_OUT_Hold(CurrentTime);
                }
            }else {//if the process not terminated
                
             
                Quantum=ar;
                holdQ1.add(cpu.JInCPU);
             
                cpu.setFree(true);
                update_SR_AR();
                
                
            }

    }
}
    public static void DisplayFinalState(int currentTime,PrintWriter output){
                                   //print all the system
                            output.println("<< Final state of system: ");
                            output.println("  Current Available Main Memory = "+availableMem);
                            output.println("  Current Devices               = "+availbleDev+"\n");
                            output.println("  Completed jobs:");
                            output.println("  ----------------");
                            output.println("  Job ID   Arrival Time    Finish Time  Turnaround Time ");
                            output.println("  ==================================================================");
                            Job [] sortingArray= new Job[completeQ.size()];
                            CopyQueueToArray(sortingArray , completeQ);
                            sortArray(2,sortingArray, completeQ);
                            completeQ.clear();
            for (int j = 0; j < sortingArray.length; j++) {
               
                completeQ.add(sortingArray[j]);
            }
            Iterator<Job> value = completeQ.iterator();
            double turaround=0;
            value = completeQ.iterator();
       while (value.hasNext()){
          Job job = value.next();
            output.printf("%5d %10d %15d %15d \n", job.getID(), job.getArrT(), job.getFinishTime(), (job.getFinishTime()-job.getArrT()));
             turaround+=job.getFinishTime()-job.getArrT();
            }
                           
                            output.println("");
                         
                            output.println("");
                            output.printf("System Turnaround Time =  %.3f\n",turaround/completeQ.size());
                            output.println("\n*********************************************************************\n");
                        
    }
 public static void Display(int CurrentTime, PrintWriter output) {
        output.println("<< At time " + CurrentTime + ": ");
        output.println("  Current Available Main Memory =" + availableMem );
        output.println("  Current Devices               =" + availbleDev);
        output.println("");
        output.println(" Completed jobs:");
        output.println(" ----------------");
        output.println("  Job ID   Arrival Time    Finish Time  Turnaround Time  ");
        output.println("  =================================================================");
         Job [] sortingArray1= new Job[completeQ.size()];
                            CopyQueueToArray(sortingArray1 , completeQ);
                            sortArray(2,sortingArray1, completeQ);
                            completeQ.clear();
            for (int j = 0; j < sortingArray1.length; j++) {
               
                completeQ.add(sortingArray1[j]);
            }
        Queue<Job> Temp = new LinkedList();
         Iterator<Job> value = completeQ.iterator();
      
       value = completeQ.iterator();
       while (value.hasNext()){
          Job job = value.next();
            output.printf("%5d %10d %13d %15d \n", job.getID(), job.getArrT(), job.getFinishTime(), (job.getFinishTime()-job.getArrT()));
             
            }
       
      
        output.println("\n");
        output.println("  Hold Queue 2: ");
        output.println( "  --------------");
        Job [] sortingArray= new Job[holdQ2.size()];
                            CopyQueueToArray(sortingArray , holdQ2);
                            sortArray(2,sortingArray, holdQ2);
                            holdQ2.clear();
            for (int j = 0; j < sortingArray.length; j++) {
               
                holdQ2.add(sortingArray[j]);
            }
        value = holdQ2.iterator();
       while (value.hasNext()){
          Job job = value.next();
            output.printf(" %3d  ", job.getID());
            }
        output.println("");
        output.println("");
        output.println("");
        output.println("  Hold Queue 1 (Ready Queue):");
        output.println( "  ----------------");
        output.println("  JobID    NeedTime    Total Execution Time ");
        output.println(" ================================");value = holdQ1.iterator();
         value = holdQ1.iterator();
       while (value.hasNext()){
          Job job = value.next();
            output.printf("%5d %10d %10d\n", job.getID(), (job.getR()-job.getAccuredTime()),  job.getAccuredTime());//run time, time accrued
            }
        output.println("");
        output.println("");
       
        output.println("  Process running on the CPU: ");
        output.println("  ----------------------------");
        output.println("  Job ID   NeedTime    Total Execution Time");
        output.printf("%4d %8d %10d \n", cpu.JInCPU.getID(), (cpu.JInCPU.getR()-cpu.JInCPU.getAccuredTime()),  cpu.JInCPU.getAccuredTime());//run time, time accrued
        output.println("");
        output.println("");
        
  
    } 
 
}
