package com.app.andonprototype.ui.Report;

public class ReportListItems {
    public String No, MachineID,Line,Station,Repair_Start,Repair_Finish,Repair_Duration,PIC;
    public ReportListItems(String No, String MachineID, String Line, String Station, String Repair_Start, String Repair_Finish, String Repair_Duration, String PIC){
        this.No = No;
        this.MachineID = MachineID;
        this.Line = Line;
        this.Station = Station;
        this.Repair_Start = Repair_Start;
        this.Repair_Finish = Repair_Finish;
        this.Repair_Duration = Repair_Duration;
        this.PIC = PIC;
    }
    public String getNo(){
        return No;
    }
    public String getMachineID(){
        return MachineID;
    }
    public String getLine(){
        return Line;
    }
    public String getStation(){
        return Station;
    }
    public String getRepair_Start(){
        return Repair_Start;
    }
    public String getRepair_Finish(){
        return Repair_Finish;
    }
    public String getRepair_Duration(){
        return Repair_Duration;
    }
    public String getPIC(){
        return PIC;
    }
}
