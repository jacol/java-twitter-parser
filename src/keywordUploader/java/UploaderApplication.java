import rabbit.RabbitConnectionFactory;
import rabbit.RabbitPoster;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UploaderApplication {
    public static void main(String [] args) throws IOException, TimeoutException {

        System.out.println("Starting uploading");

        RabbitConnectionFactory rabbitConnectionFactory = new RabbitConnectionFactory();
        RabbitPoster rabbitPoster = new RabbitPoster(rabbitConnectionFactory);

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
            rabbitConnectionFactory.close();
        }

        System.out.println("Done uploading");
    }
}
