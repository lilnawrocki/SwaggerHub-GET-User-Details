package org.members;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String apiKey = "";
        String owner = "";
        int pageSize = 0;
        int page = 0;
        String filename = "output.csv";

        System.out.println("Enter your SwaggerHub API Key below");
        apiKey = input.nextLine();
        System.out.println("Enter your SwaggerHub Organization name:");
        owner = input.nextLine();
        System.out.println("Enter page size (number of results per page)");
        pageSize = input.nextInt();
        System.out.println("Enter page to return");
        page = input.nextInt();
        input.nextLine();

        ApiResponse users = GetUsers(apiKey, owner, pageSize, page);

        if (users == null){
            System.out.println("Error getting the users: users object is null");
        }else{
            System.out.println("Enter file name");
            filename = input.nextLine();
            filename = filename + ".csv";
            SaveUsersToFile(users, filename);
        }
        input.close();
    }

    public static ApiResponse GetUsers (String apiKey, String owner, int pageSize, int page){

        String URL = String.format("https://api.swaggerhub.com/user-management/v1/orgs/%s/members?page=%d&pageSize=%d", owner, page, pageSize);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .headers(
                        "Accept", "application/json",
                        "Authorization", apiKey
                )
                .GET()
                .build();

        HttpResponse<String> response;
        ApiResponse apiResponse = new ApiResponse();
        try{
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            apiResponse = gson.fromJson(response.body(), ApiResponse.class);
            httpClient.close();
        }catch(IOException | InterruptedException exception){
            System.out.println(exception.getMessage());
        }

        return apiResponse;
    }

    public static void SaveUsersToFile(ApiResponse apiResponse, String filename){
        try(FileWriter fileWriter = new FileWriter(filename)){

            System.out.println("Total count: " + apiResponse.totalCount + "\n");
            fileWriter.write("Name,Email,BillingType,InviteTime,StartTime\n");

            for (int i = 0; i < apiResponse.items.length; i++){
                String line = String.format("%s %s,%s,%s,%s,%s\n",
                        apiResponse.items[i].firstName,
                        apiResponse.items[i].lastName,
                        apiResponse.items[i].email,
                        apiResponse.items[i].billingType,
                        apiResponse.items[i].inviteTime,
                        apiResponse.items[i].startTime);
                fileWriter.write(line);
            }

            System.out.println("File saved to: " + filename);
        }catch(IOException exception){
            System.out.println("Error writing file: " + exception.getMessage());
        }
    }
}