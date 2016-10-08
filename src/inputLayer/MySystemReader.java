package inputLayer;

import java.util.Scanner;

public class MySystemReader {
    private String lastRead;
    private boolean loopQ=true;
    private Scanner readLine;
    //private String loopMessageTrue="If an error is detected, you will be prompted to try again.";
    //private String loopMessageFalse="If an error is detected, the program will exit.";
    //private String loopMessage="";
    private String stringMessage="Please type in a string:";
    private String intMessage="Please type in an integer:";
    private String floatMessage="Please type in a float:";
    private String doubleMessage="Please type in a double:";
    private String yesNoMessage="Please type yes or no:";
    //private String charMessage="Please type in a character:";
    private String escapeString="esc";
    
    public MySystemReader(){
        readLine=new Scanner(System.in);
        //loopMessage=loopMessageTrue;
    }
    
    public MySystemReader(boolean loop){
        readLine=new Scanner(System.in);
        loopQ=loop;
        //if(loopQ)
            //loopMessage=loopMessageTrue;
        //else
            //loopMessage=loopMessageFalse;
    }
    
    public String getLastRead(){
        return this.lastRead;
    }
    
    public String getString(){
        System.out.println(this.stringMessage);
        lastRead=readLine.nextLine();
        return lastRead;
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    public int getInt() throws Exception{
        //doesn't throw IOException, throws something else but I'm in Dallas and can't connect
        int i;
        try{
            System.out.println(this.intMessage);
            lastRead=readLine.nextLine();
            i = Integer.parseInt(lastRead);
            return i;
        }
        catch(Exception e){
            e.getMessage();
            if(!loopQ){
                System.out.println("Error: integer could not be parsed, returning 0");
            }
            else if(this.lastRead.equalsIgnoreCase(this.escapeString) ){
                System.out.println("User aborted, returning 0");
            }
            else{
                System.out.println("Error: integer could not be parsed. Try again, or enter "+this.escapeString+" to escape.");
                i=getInt();
                return i;
            }
        }
        return 0; //is this right? Helps with compiling but it really should skip it
    }
    
    public float getFloat() throws Exception{
        //doesn't throw IOException, throws something else but I'm in Dallas and can't connect
        float f;
        try{
            System.out.println(this.floatMessage);
            lastRead=readLine.nextLine();
            f = Float.parseFloat(lastRead);
            return f;
        }
        catch(Exception e){
            e.getMessage();
            if(!loopQ){
                System.out.println("Error: float could not be parsed, returning 0");
            }
            else if(this.lastRead.equalsIgnoreCase(this.escapeString) ){
                System.out.println("User aborted, returning 0");
            }
            else{
                System.out.println("Error: float could not be parsed. Try again, or enter "+this.escapeString+" to escape.");
                f=getFloat();
                return f;
            }
        }
        return 0f; //is this right? Helps with compiling but it really should skip it
    }
    
    public double getDouble() throws Exception{
        //doesn't throw IOException, throws something else but I'm in Dallas and can't connect
        double d;
        try{
            System.out.println(this.doubleMessage);
            lastRead=readLine.nextLine();
            d = Double.parseDouble(lastRead);
            return d;
        }
        catch(Exception e){
            e.getMessage();
            if(!loopQ){
                System.out.println("Error: double could not be parsed, returning 0");
            }
            else if(this.lastRead.equalsIgnoreCase(this.escapeString) ){
                System.out.println("User aborted, returning 0.0");
            }
            else{
                System.out.println("Error: double could not be parsed. Try again, or enter "+this.escapeString+" to escape.");
                d=getDouble();
                return d;
            }
        }
        return 0.0; //is this right? Helps with compiling but it really should skip it
    }
    
    public String getYesNo() {
    	int lim=5;
    	for(int i=0;i<lim;i++) {
            System.out.println(this.yesNoMessage);
            lastRead=readLine.nextLine();
            if("yes".equalsIgnoreCase(lastRead.trim()))
            	return "yes";
            else if("no".equalsIgnoreCase(lastRead.trim()))
            	return "no";
            
            if(!loopQ){
                System.out.println("Error: double could not be parsed, returning 0");
            }
            else if(this.lastRead.equalsIgnoreCase(this.escapeString) ){
                System.out.println("User aborted.");
                return "";
            }
            else{
                System.out.println("Error: yes/no could not be parsed. Try again, or enter "+this.escapeString+" to escape.");
            }

        }
    	System.out.println("Cycle limit reached; operation aborted.");
    	return "";
        
    }
    
    //get it right for int, then write methods for all useful data types

}
