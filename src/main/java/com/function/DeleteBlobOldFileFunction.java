package com.function;

import java.time.*;
import com.microsoft.azure.functions.annotation.*;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.ctc.wstx.shaded.msv_core.datatype.xsd.DateTimeType;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer trigger.
 */
public class DeleteBlobOldFileFunction {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("DeleteBlobOldFileFunction")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */2 * * * *") String timerInfo,
        @QueueOutput(name = "message", queueName = "deleteddatainfo-queue", connection = "azuresampleproj2") OutputBinding<String> message,
        final ExecutionContext context

    ) {
        ZonedDateTime utc_timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        
        String storage_connection_string="DefaultEndpointsProtocol=https;AccountName=azuresampleproj2;AccountKey=3zzHj6uPTHi/oRA7P/k1WN9sm9sJvHIZAltWSbD5zOP5xl3/oxLmxL2z1ENkj0QFXwx5VW7fcIVo+AStzSx3jw==;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storage_connection_string).buildClient();
       BlobContainerClient blobClient = blobServiceClient.getBlobContainerClient("input-image");

       for (BlobItem blobItem : blobClient.listBlobs()) {
            
           Duration diff=Duration.between(utc_timestamp , blobItem.getProperties().getCreationTime().toZonedDateTime());
           
          if(diff.negated().getSeconds()>120){
          
           BlobClient blob=blobClient.getBlobClient(blobItem.getName());
           message.setValue(blobItem.getName());
            blob.delete();
            
            context.getLogger().info("Message stored in queue");
          }
       }
        
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());
    }
}
