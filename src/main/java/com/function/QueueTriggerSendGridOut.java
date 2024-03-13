package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.azure.json.implementation.jackson.core.JsonParser;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Azure Storage Queue trigger.
 */
public class QueueTriggerSendGridOut {
    /**
     * This function will be invoked when a new message is received at the specified path. The message contents are provided as input to this function.
     */
    @FunctionName("QueueTriggerSendGridOut")
    public void run(
        @QueueTrigger(name = "message", queueName = "deleteddatainfo-queue", connection = "azuresampleproj2") String message,
        @SendGridOutput(name = "sendGridMessage",
        dataType = "String",
        apiKey = "SendGrid_API_Key",
        to = "lakshmihyndavi643@gmail.com",
        from = "rsujatha146@gmail.com",
        subject = "Azure Functions email with SendGrid",
        text = "Sent from Azure Functions")
            OutputBinding<String> sendGridMessage,
        final ExecutionContext context
    ) { 
        try{
            final String toAddress = "lakshmihyndavi643@gmail.com";
            final String toAddressMail = "hyndavigowdsyamala@gmail.com";
            StringBuilder builder = new StringBuilder().append("{")
                    .append("\"personalizations\": [{ \"to\": [{ \"email\": \"%s\"},{ \"email\": \"%s\"}]}],")
                    .append("\"content\": [{\"type\": \"text/plain\", \"value\": \"%s\"}]").append("}");
              String mailMessage="Deleted old blob file from blob container"+message;      
        
            final String body = String.format(builder.toString(), toAddress, toAddressMail, mailMessage);
        
            sendGridMessage.setValue(body);
        }catch(Exception e){
            context.getLogger().info("Error is " );
            e.printStackTrace();
           
          }
        context.getLogger().info("Java Queue trigger function processed a message: " + message);
    }
}
