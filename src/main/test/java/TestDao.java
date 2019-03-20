import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.UserResultJO;
import com.mdmd.entity.UserTakeoutEntity;
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
        gameRuleService.calcuCommission(7,10);
    }

    @Test
    public void testService() throws Exception {
        PageJO pageJO = new PageJO();
        pageJO.setPageSize(10);
        pageJO.setPageNo(1);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("userid","1");
        List<GameRecordEntity> gameRecordEntities = sysPropService.listDatas_page(GameRecordEntity.class, pageJO, stringObjectHashMap);
    }


    @Test
    public void testService2(){


    }
}
