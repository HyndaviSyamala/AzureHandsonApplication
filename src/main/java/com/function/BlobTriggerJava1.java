package com.function;

import com.microsoft.azure.functions.annotation.*;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class BlobTriggerJava1 {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("BlobTriggerJava1")
    @StorageAccount("azuresampleproj2")
    public void run(
        @BlobTrigger(name = "content", path = "input-image/{name}", dataType = "binary")  byte[] content,
        @BindingName("name") String name,
        @BlobOutput(name="outputItem", path="output-image/{name}") OutputBinding<byte[]> outputItem, 
        final ExecutionContext context 
    ) {

        context.getLogger().info("Java Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
   
          try{
            InputStream is = new ByteArrayInputStream(content); 
            BufferedImage bi = ImageIO.read(is);
            System.out.println("width is " +bi.getWidth());
            
           
           ByteArrayOutputStream res=new ByteArrayOutputStream();
          
    
           BufferedImage bufferedImage = Thumbnailator.createThumbnail(bi,90,90);
           System.out.println( "buffered image size is :" +bufferedImage.getWidth());
           boolean var=ImageIO.write(bufferedImage, "png",res);
           System.out.println( "process executed is :" + var);
           System.out.println( "output image size is :" +res.size());
           outputItem.setValue(res.toByteArray());
           
           
           
          }
          catch(Exception e){
            context.getLogger().info("Error is " );
            e.printStackTrace();
           
          }
   
   
   
    }
}
