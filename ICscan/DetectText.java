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

    public static void detectText(String filePath, PrintStream out) {
 
        try {
            List<AnnotateImageRequest> requests = new ArrayList<>();

            var imgBytes = ByteString.readFrom(new FileInputStream(filePath));

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
