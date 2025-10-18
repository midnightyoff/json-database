package com.projects.client;


import com.beust.jcommander.Parameter;

public class JArgs {
    @Parameter(names = "-t", description = "type of the request", required = true)
    private String type;

    @Parameter(names = "-i", description = "index of the cell")
    private Integer index;

    @Parameter(names = "-m", description = "value/message to save in the database")
    private String message;

    public String sentMsg(){
        return type + (index != null ? (" " + index) : "") + (message != null ? " " + message : "");
    }
}
