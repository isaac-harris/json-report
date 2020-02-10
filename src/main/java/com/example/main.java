package com.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;

public class main {
    public static void main(String[] args)
    {
        //query returns data from api, jsonparser then converts into a more useful format
        String content = query();
        String parsed = jsonParser(content);
        System.out.println(parsed);
    }

    private static String query() {
        //Method 1 (there were several good methods for getting json data, however this method was the most successful
        // this method was also the easiest to use and least prone to errors.
        BufferedReader reader; //reads everything from the website
        String line; //variable to temporarily hold each line
        StringBuffer responseContent = new StringBuffer(); //appends each line to the response, this is a non static string object

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

            //connection.getInputStream is the chunk that actually retrieves the data.
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

        return responseContent.toString();
    }

    public static String jsonParser(String responseContent){
        String productName = "Null";
        String allergens = "Unknown";

        JSONObject foodField = new JSONObject(responseContent);

        // due to a surprising lack of standardised variable names, several known variables are tested
        // it is entirely possible that some edge case variables are not included, but the majority of products my user base
        // will input will use these names.
        if (responseContent.contains("product_name")){productName = foodField.getJSONObject("product").getString("product_name"); }
        else if (responseContent.contains("product_name_en")){productName = foodField.getJSONObject("product").getString("product_name_en"); }
        else if (responseContent.contains("product_name_es")){productName = foodField.getJSONObject("product").getString("product_name_es"); }
        else if (responseContent.contains("product_name_fr")){productName = foodField.getJSONObject("product").getString("product_name_fr"); }
        //  allergens_from_ingredients needed
        if (responseContent.contains("allergens_from_ingredients")){allergens = foodField.getJSONObject("product").getString("allergens_from_ingredients");}
        System.out.printf("\n%20s\n",productName);
        System.out.printf("\n%28s\n",allergens);

        return productName;
    }
}
