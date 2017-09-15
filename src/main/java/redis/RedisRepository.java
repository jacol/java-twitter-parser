package redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

public class RedisRepository {

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

    public void addData(String key, String data){
        syncCommands.set(key, data);
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
}

