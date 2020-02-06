package com.app.andonprototype.ui.ProblemList;

public class ProblemListItems {
    public String Line, Station,PIC;
    public int Status;
    public ProblemListItems(int Status, String Line,String Station,String PIC){
        this.Status = Status;
        this.Line = Line;
        this.Station = Station;
        this.PIC = PIC;
    }
    public int getStatus(){
        return Status;
    }
    public String getLine(){
        return Line;
    }
    public String getStation(){
        return Station;
    }
    public String getPIC(){return PIC;}
}
