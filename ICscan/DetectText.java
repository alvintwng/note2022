package com.zero1.idscan;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AlvinNg
 */
public class DetectText {

    public static void detectText(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request
                = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try ( ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    System.out.printf("Text: %s\n", annotation.getDescription());
                    //System.out.printf("Position : %s\n", annotation.getBoundingPoly());
                    out.printf("Text: %s\n", annotation.getDescription());
//                    out.printf("Position : %s\n", annotation.getBoundingPoly());
                }
            }
        }
    }

    public static void main(String args[]) {
        PrintStream errStream = null;
        try {
            errStream
                    = new PrintStream(
                            new FileOutputStream("errormessages.txt"));

        } catch (FileNotFoundException e) {
            System.out.println("Error opening file with FileOutputStream.");
            System.exit(0);
        }
        System.setErr(errStream); //                ****  System.setErr( ****
        System.err.println("Hello from System.err.");
        System.out.println("Hello from System.out.");
        System.err.println("Hello again from System.err.");

        errStream.close(); //                               **** errStream *****

        System.out.println(
                "Hello World!");

        try {
            IdScan.detectText("..\\..\\idScan\\sample\\zero1.png", errStream);
        } catch (Exception ex) {
            System.out.println(">>>> " + ex);
        }
    }

}
/* Sample dialogue
antw@Mac-mini chapter10 % pwd
/Users/antw/absoluteJ/chapter10/chapter10
antw@Mac-mini chapter10 % java src/RedirectionDemo.java
Hello from System.out.
antw@Mac-mini chapter10 % cat errormessages.txt
Hello from System.err.
Hello again from System.err.
*/
