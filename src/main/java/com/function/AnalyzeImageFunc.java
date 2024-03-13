package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.azure.core.http.HttpClient;
import com.microsoft.azure.functions.*;
import java.net.URI;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
/**
 * Azure Functions with Azure Blob trigger.
 */
public class AnalyzeImageFunc {

    private static String key = "05706948756c4038ac86713e29713690";
    private static String endpoint = "https://afccomputervisionapi.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview";

    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("AnalyzeImageFunc")
    @StorageAccount("azuresampleproj2")
    public void run(
        @BlobTrigger(name = "content", path = "analyzeimage-input/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        @CosmosDBOutput(name = "database",
        databaseName = "TestDB",
        partitionKey = "/requestId",
        containerName="TestContainer",
        connection = "azurecosmosdbconnection")
        OutputBinding<String> Item,
        
        final ExecutionContext context
    ) {


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try
        {
            URIBuilder builder = new URIBuilder(endpoint);
            builder.setParameter("features", "tags");
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", key);
            // Request body
            ByteArrayEntity reqEntity=new ByteArrayEntity(content);
           
            request.setEntity(reqEntity);

            HttpResponse response =  httpClient.execute(request);
            
            HttpEntity entity = response.getEntity();
    
             String partitionKey=context.getInvocationId();
            if (entity != null) 
            {  
               String entityresponse=EntityUtils.toString(entity);
                

                System.out.println(entityresponse.length());
                String result=entityresponse.substring(1, entityresponse.lastIndexOf('}'));
                final String jsonDocument = "{\"id\":\"" + context.getInvocationId() + ", \n "+ "\", \n "  +result+"}";
                
                System.out.println(jsonDocument);
                Item.setValue(jsonDocument);
                
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    
        
        
        context.getLogger().info("Java Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
    }
}
