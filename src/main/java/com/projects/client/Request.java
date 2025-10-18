package com.projects.client;


import com.beust.jcommander.Parameter;

public class Request {
    @Parameter(names = "-in", description = "read a request from a file")
    private String fileName;

    @Parameter(names = "-t", description = "type of the request")
    private String type;

    @Parameter(names = "-k", description = "index of the cell")
    private String key;

    @Parameter(names = "-v", description = "value/message to save in the database")
    private String value;

    public String getFileName() {
        return fileName;
    }
}
