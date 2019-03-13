import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.UserTopupEntity;
import com.mdmd.enums.RedisChannelEnum;
import com.mdmd.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import sun.awt.windows.ThemeReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TopupService topupService;

    @Test
    public void testRedis()  {
        dataService.test();
    }

    @Test
    public void testService() throws Exception {
//        topupService.getParametersForTopup(1,50,1);
        boolean s = topupService.updateUserTopupEntityData("20190313164644000001418", "222", "陈", 1000, 1, true);
        
    }


    @Test
    public void testService2(){
        try {
            List list = dataService.listDatas(1, 1);
            System.out.println(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
