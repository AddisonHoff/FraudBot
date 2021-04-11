/*
Addison Hoff
Applies a sentiment analysis API to Enron emails.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        System.out.println("How many emails would you like analyzed? (Enter a number between 1-5)");
        int numEmails = userInput.nextInt();

        //Actual dataset was too big
        List<String> sampleEmails = new ArrayList<String>();
        for (int i = 1; i <= numEmails; i++) {
        try {
            File myObj = new File("/Users/addisonhoff/Desktop/neuralnet/src/sampleEmail" + i);
            Scanner myReader = new Scanner(myObj);
            String currEmail = "";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                currEmail = currEmail + " " + data;
            }
            sampleEmails.add(currEmail);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        }

        for (int y = 0; y < sampleEmails.size(); y++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://twinword-sentiment-analysis.p.rapidapi.com/analyze/"))
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("x-rapidapi-key", "ad7cccc825msha5999ab40c26e2cp1ad400jsn6a1e0b331ccc")
                    .header("x-rapidapi-host", "twinword-sentiment-analysis.p.rapidapi.com")
                    .method("POST", HttpRequest.BodyPublishers.ofString("text=" + sampleEmails.get(y)))
                    .build();
            HttpResponse<String> response = null;
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String jsonString = response.body();

            System.out.println(sampleEmails.get(y));
            if (sampleEmails.get(y).contains("negative")) {
                System.out.println("Sentiment: NEGATIVE");
            } else System.out.println("Sentiment: POSITIVE");
            int beginString = jsonString.indexOf("score");
            System.out.println("SCORE " + jsonString.substring(beginString+7, beginString + 11));
        }
    }
    }
