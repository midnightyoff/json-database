package com.projects.client;


import com.beust.jcommander.Parameter;

public class Request {
    @Parameter(names = "-t", description = "type of the request", required = true)
    private String type;

    @Parameter(names = "-k", description = "index of the cell")
    private String key;

    @Parameter(names = "-v", description = "value/message to save in the database")
    private String value;
}
