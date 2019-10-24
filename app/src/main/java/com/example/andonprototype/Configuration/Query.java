package com.example.andonprototype.Configuration;

import com.example.andonprototype.ReportActivity;

public class Query {
    public static final String getdataquery =
            "select MachineID, Line, Station, Status " +
                    "from machinedashboard";
    public static final String problemquery =
            "Select MachineID,Line,Station,Status " +
                    "from machinedashboard " +
                    "where Status = 2 or Status = 3";
}
