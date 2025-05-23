package org.example;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/*
 * Sample code to demonstrate the use of the Recent search endpoint
 * */
public class RecentSearchDemo {

    private static final String TELEGRAM_BOT_USERNAME = "PisosUma1_bot";

    // To set your enviornment variables in your terminal run the following line:
    // export 'BEARER_TOKEN'='<your_bearer_token>'

    public static void main(String args[]) throws IOException, URISyntaxException, InterruptedException {
        String bearerToken = System.getenv("BEARER_TOKEN");
        String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
        String TELEGRAM_CHAT_ID = System.getenv("TELEGRAM_CHAT_ID");
        TelegramBOT bot = new TelegramBOT(TELEGRAM_BOT_TOKEN, TELEGRAM_BOT_USERNAME);
        if (null != bearerToken) {
            //Replace the search term with a term of your choice
            String response = search("#pisosuma -is:retweet -is:reply lang:es", bearerToken);
            System.out.println(response);
            bot.sendMessage(TELEGRAM_CHAT_ID, response);

        } else {
            System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

    /*
     * This method calls the recent search endpoint with a the search term passed to it as a query parameter
     * */
    private static String search(String searchString, String bearerToken) throws IOException, URISyntaxException {
        String searchResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));
        //Para que me develva el ultimo desde este id que ha sido la ultima llamada
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            searchResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return searchResponse;
    }

}