package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.model.User;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import gritdock.App;

/**
 * @author noear 2021/7/23 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(App.class)
public class LoginTest {

    @Test
    public void login() throws Exception {
        User user = GritClient.login("noear", "bcf1234"); //1D050B5785B44868E6C41EE9ED990354A7FA5A55

        System.out.println(user);
        assert user.user_id == 2;
    }

}
