import rabbit.RabbitPoster;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UploaderApplication {
    public static void main(String [] args) throws IOException, TimeoutException {

        System.out.println("Starting uploading");

        RabbitPoster rabbitPoster = new RabbitPoster();

        try{
            rabbitPoster.publishKeywords(new String []{
                    "putin",
                    "trump",
                    "nikitiuk",
                    "java",
                    "docker"
            });
        }
        finally{
            rabbitPoster.close();
        }

        System.out.println("Done uploading");
    }
}
