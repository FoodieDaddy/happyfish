import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.UserDao;
import com.mdmd.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.List;

public class TestDao extends TestBase{


    @Autowired
    private DataService dataService;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private UserService userService;
    @Autowired
    private GameRuleService gameRuleService;
    @Autowired
    private SysPropService sysPropService;
    @Autowired
    private TakeoutService dealService;

    @Test
    public void testRedis()  {
    }

    @Test
    public void testService(){
    }


    @Test
    public void testService2(){
        try {
            List list = dataService.listDatas(1, 34);
            System.out.println(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
