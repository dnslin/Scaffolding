import in.dnsl.Application;
import in.dnsl.repository.WallhavenRepository;
import in.dnsl.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = Application.class)
public class RedisTest {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private WallhavenRepository wallhavenRepository;

    @Test
    public void testRedis() {
        redisUtils.set("name", "DnsLin");
        System.out.println(redisUtils.get("name"));
    }

    @Test
    public void testWallhaven() {
        System.out.println(wallhavenRepository.findWallhavenByStatus(0));
    }
}
