package com.app.andonprototype.Background;

public class Query {
    public static final String getdataquery =
            "select MachineID, Line, Station, Status " +
                    "from machinedashboard";
    public static final String problemquery =
            "Select * " +
                    "from stationdashboard " +
                    "where Status = 2 or Status = 3 or Status = 4";
}
