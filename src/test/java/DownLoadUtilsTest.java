import me.kuku.utils.OkHttpUtils;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Path;

public class DownLoadUtilsTest {
    public static void main(String[] args) {
        OkHttpUtils.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("150.230.121.194", 1080)));
        File file = OkHttpUtils.getFile("https://w.wallhaven.cc/full/we/wallhaven-werowr.png", Path.of("C:\\Users\\dongshilin\\Pictures\\Screenshots"));
        System.out.println(file.getName());
    }
}
