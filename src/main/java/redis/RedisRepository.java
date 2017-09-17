package redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import twitter.TwitterEventHandler;

public class RedisRepository implements TwitterEventHandler {

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;

    private boolean disposed = false;

    public void connect(){
        redisClient = RedisClient.create("redis://192.168.0.106:6379/0");
        connection = redisClient.connect();
        syncCommands = connection.sync();

        System.out.println("Connected to Redis");
    }

    public void close(){
        connection.close();
        redisClient.shutdown();
        disposed = true;
        System.out.println("Redis disposed");
    }

    @Override
    protected void finalize() throws Throwable {
        if(!disposed){
            close();
            super.finalize();
        }
    }

    public void handleEvent(String keyword, String twitterMsg) {
        syncCommands.lpush(keyword, twitterMsg);
    }
}

