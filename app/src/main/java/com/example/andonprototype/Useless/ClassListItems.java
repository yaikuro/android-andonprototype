package com.example.andonprototype.Useless;

public class ClassListItems {
    public String MachineID;
    public String Station;
    public String Line;

    public ClassListItems(String MachineID, String Station, String Line)
    {
        this.MachineID = MachineID;
        this.Line = Line;
        this.Station = Station;
    }
    public String getMachineID()
    {
        return MachineID;
    }

    public String getStation()
    {
        return Station;
    }

    public String getLine()
    {
        return Line;
    }
}
