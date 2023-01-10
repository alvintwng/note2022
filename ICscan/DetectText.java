package com.zero1.idscan;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AlvinNg
 */
public class DetectText {

    /**
     * ref: https://cloud.google.com/vision/docs/libraries#use
     *
     * @param filePath
     * @param out
     */
    public static void detectText(String filePath, PrintStream out) {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try {

            // Reads the image file into memory
            var imgBytes = ByteString.readFrom(new FileInputStream(filePath));

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            var img = Image.newBuilder().setContent(imgBytes).build();
            var feat = Feature
                    .newBuilder()
                    .setType(Feature.Type.TEXT_DETECTION)
                    .build();

            var request = AnnotateImageRequest
                    .newBuilder()
                    .addFeatures(feat)
                    .setImage(img).build();
            requests.add(request);

            // Performs label detection on the image file
            var client = ImageAnnotatorClient.create();
            var response = client.batchAnnotateImages(requests);
            var responses = response.getResponsesList();

            for (var res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for (var annotation : res.getTextAnnotationsList()) {
                    out.printf("Text: %s%n", annotation.getDescription());
                    out.printf("Position : %s%n", annotation.getBoundingPoly());
                }
            }
        } catch (IOException ex) {
            System.out.println(">>>>  IOException: " + ex);
        }
    }
}
