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
public class CosmosDBSendGrid {


    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("CosmosDBSendGrid")
    @StorageAccount("azuresampleproj2")
    public void run(
        @CosmosDBTrigger(
            name = "item",
            databaseName = "TestDB",
            containerName = "TestContainer",
            leaseContainerName="leases",
            connection = "azurecosmosdbconnection",
            createLeaseContainerIfNotExists = true
        )String item,
        @BindingName("item") String name,
        
        final ExecutionContext context
    ) {


       
        
        context.getLogger().info("Cosmos DB trigger function processed a blob. Name: " + name + "\n  Size: " + item + " Bytes");
    }
}
