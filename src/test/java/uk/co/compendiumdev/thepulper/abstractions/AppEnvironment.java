package uk.co.compendiumdev.thepulper.abstractions;

public class AppEnvironment {

    public static String domainUrl(){
        return "https://thepulper.herokuapp.com";
        //return "http://localhost:4567";
    }
    public static String baseUrl() {
        return domainUrl() + appRootPath();
    }

    public static String appRootPath(){
        return "/apps/pulp/";
    }
}
