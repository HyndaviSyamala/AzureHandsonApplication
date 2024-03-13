package com.function;

import java.net.URI;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParams;

import com.microsoft.azure.functions.annotation.*;

import io.netty.util.Constant;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class TweetsAnalyzeFunction {
    String TWITTER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAHqJsgEAAAAAL7VUDYB5foJ8o5U9NU0YGoJHZ%2FA%3Drz6SQClvTa18TR30BTIHr8IwgCLErIxwIuEbPixWU7M2mimU7b";
    /**
     * This function listens at endpoint "/api/TweetsAnalyzeFunction". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/TweetsAnalyzeFunction
     * 2. curl {your host}/api/TweetsAnalyzeFunction?name=HTTP%20Query
     */
    @FunctionName("TweetsAnalyzeFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("hastag");
        String name = request.getBody().orElse(query);

        String status=searchTweets(query);
        System.out.println("result is "+ status);



        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    /**
     * @param hastag
     * @return
     */
    public String searchTweets(String hastag){

        String haSTag="#"+hastag;

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
         try{
            //String endpoint="https://api.twitter.com/2/tweets/search/recent"+"?query="+haSTag;
            String endpoint="https://api.twitter.com/1.1/search/tweets.json/?query="+haSTag;
            System.out.println(endpoint);
            URIBuilder builder = new URIBuilder(endpoint);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Bearer", TWITTER_TOKEN );
            
            HttpResponse response =  httpClient.execute(request);
            
            HttpEntity entity = response.getEntity();

            System.out.println(entity.toString());
            

        return entity.toString();
         }
         catch (Exception e)
         {
             System.out.println(e.getMessage());
         }

         return null;
    }
}
