package com.brightcode.mediadeck.movie_rec;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Movie_rec {



    public Movie_rec() {}

    public boolean isWindows(){
        String sysname = System.getProperty("os.name");
        return sysname.startsWith("Windows");
    }

    public List<String> convertCsvToStrings() throws FileNotFoundException {
        String csvFile  ="movie_rec_output.csv";
        String line = "";

        List<String> apiList = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // read each line of the CSV file
            while ((line = br.readLine()) != null) {
                // split the line into tokens using semicolon as the delimiter
                String[] tokens = line.split(";");
                // print each token
                for (String token : tokens) {
                    System.out.println(token);
                    apiList.add(token);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiList;

    }




    public String returnAPiOfUser(int id, String line){
        String api = null;
        boolean isId = false;
        //only return if it matches
        String[] b = line.split(",");
        for (String a : b){
            //if it matches next will be api returned
            if (isId==true){
                api = a;
            }
            //check if number matches with user id
            if (Integer.parseInt(a)==id) {
                isId = true;
            }
        }
        return api;
    }

    public String makeApiSearchable(String unsearchable){

        int sLength = unsearchable.length();
        String prefix = "tt";

        if (sLength == 6){
            unsearchable = "0" + unsearchable;
        }
        if (sLength == 5){
            unsearchable = "00" + unsearchable;
        }
        if (sLength == 4){
            unsearchable = "000" + unsearchable;
        }

        String searchable = prefix + unsearchable;


        return searchable;
    }

    public String runFromJava() throws IOException {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        //check OS
        System.out.println(isWindows());

        String start;
        String flag;
        String command;

        //for mac
        // "/bin/sh"
        // -c
        // ./src/main/java/org/launchcode/moviedock/movie_rec/movie_rec.py
        Process proc = null;
        try {

            //String command = "NUL > EmptyFile.txt";

            if (isWindows()) {
                command = "python3 .\\src\\main\\java\\org\\launchcode\\moviedock\\movie_rec\\movie_rec.py";
                start = "cmd";
                flag = "/c";

            }
            else{
                command = "python3 ./src/main/java/org/launchcode/moviedock/movie_rec/movie_rec.py";
                start = "/bin/sh";
                flag = "-c";
            }

            proc = Runtime.getRuntime().exec(new String[] { start//$NON-NLS-1$
                    , flag, command });//$NON-NLS-1$
            if (proc != null) {
                System.out.println("proc exists");
                proc.waitFor();
            }
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            System.out.println(proc.getInputStream());
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            while (read.ready()) {
                System.out.println(read.readLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());



        } catch (Exception e) {
            //Handle

        }
        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        System.out.println("goodbye from java");


        return "a";
    }




}
