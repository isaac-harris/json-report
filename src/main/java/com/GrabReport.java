package com;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.Buffer;
import java.util.Scanner;
import java.io.IOException;

public class GrabReport {
    public static void main(String[] args)
    {
//        System.out.print("Input something: ");

//        Scanner inp = new Scanner(System.in);
//        String aLine = inp.nextLine();
//        System.out.printf("%n%20s%n", aLine);
//
//        System.out.println("ura ho");
//        String fReport = report();
//        System.out.println(fReport);
//        System.out.printf("%n%,20.4f%n", 12345.678);

        //System.out.printf("here's your food sir %s", content);

        //query returns data from api, jsonparser then converts into a more useful format
        String content = query();
        String parsed = jsonParser(content);
    }
    private static String report()
    {
        String response = "cake";
        return response;
    }

    private static String query() {

        //Method 1 (inefficient)
        BufferedReader reader; //reads everything from the website
        String line; //variable to temporarily hold each line
        StringBuffer responseContent = new StringBuffer(); //appends each line to the response

        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://world.openfoodfacts.org/api/v0/product/0858133006877.json");
            connection = (HttpURLConnection) url.openConnection();

            //request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            //detects if connection was successful
            int status = connection.getResponseCode();
            System.out.println(status);

            boolean error = connection.getResponseCode() > 299;
            reader = new BufferedReader(new InputStreamReader( error ? connection.getErrorStream() : connection.getInputStream()));
            while ((line = reader.readLine()) != null)
                responseContent.append(line);
            reader.close();
            //responseContent.toString(); converts the http response to a string

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
//        //Method 2
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://world.openfoodfacts.org/api/v0/product/0737628064502.json"))
//                .build();
//        //a::b refers to a lambda expression (::)
//        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body)
//                .thenAccept(System.out::println)
//                .join();
//

//        //Method 3
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://world.openfoodfacts.org/api/v0/product/0737628064502.json"))
//                .build();
//
//        String responseContent = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body).toString();
//
//        return responseContent;

        return responseContent.toString();
    }

    public static String jsonParser(String responseContent){
        String productName = "Null";
        String allergens = "Unknown";

        JSONObject foodField = new JSONObject(responseContent);
//        String productName = foodField.getString("product_name_en");

        // due to descrepencies in standardised variable names, several known variables are tested
        if (responseContent.contains("product_name")){productName = foodField.getJSONObject("product").getString("product_name"); }
        else if (responseContent.contains("product_name_en")){productName = foodField.getJSONObject("product").getString("product_name_en"); }
        else if (responseContent.contains("product_name_es")){productName = foodField.getJSONObject("product").getString("product_name_es"); }
        else if (responseContent.contains("product_name_fr")){productName = foodField.getJSONObject("product").getString("product_name_fr"); }
        //  allergens_from_ingredients needed
        if (responseContent.contains("allergens_from_ingredients")){allergens = foodField.getJSONObject("product").getString("allergens_from_ingredients");}
        System.out.printf("\n%20s\n",productName);
        System.out.printf("\n%28s\n",allergens);
// for a list of objects (e.g. 5 different users with the same data types)
//        for (int i = 0; i < albums.length(); i++){
//            JSONObject foodFields = albums.getJSONObject(i);
//            int productName = foodFields.getInt("product_name_en");
//            int userId
//        }
        return productName;
    }
}
