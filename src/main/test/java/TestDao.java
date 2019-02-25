import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.GameResultJO;
import com.mdmd.service.DataService;
import com.mdmd.service.GameRuleService;
import com.mdmd.service.SysPropService;
import com.mdmd.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.util.List;
import java.util.Map;

public class TestDao extends TestBase{


    @Autowired
    private DataService dataService;
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

    @Test
    public void testRedis(){
    }

    @Test
    public void testService(){
        try {
            GameResultJO fishResult = gameRuleService.getFishResult(5, 10, 33);
            System.out.println(fishResult);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sss");
    }


    @Test
    public void testService2(){
        sysPropService.isDoubleCommissionTime();
    }
}
