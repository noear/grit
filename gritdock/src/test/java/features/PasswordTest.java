package features;

import org.junit.Test;
import org.noear.grit.client.GritUtil;

/**
 * @author noear 2023/6/2 created
 */
public class PasswordTest {
    @Test
    public void build(){
       String tmp =  GritUtil.buildPassword("admin","123456Abc");
       System.out.println(tmp);
    }
}
