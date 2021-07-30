package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.grit.client.model.User;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import gritdock.DockApp;

import java.util.List;

/**
 * @author noear 2021/7/23 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(DockApp.class)
public class LoginTest {

    @Test
    public void login() throws Exception {

        User user = GritClient.login("noear", "bcf1234"); //1D050B5785B44868E6C41EE9ED990354A7FA5A55
        System.out.println(user);
        assert user.user_id == 2;


        GritClient.branched("wateradmin");

        List<Group> groupList = GritClient.getUserModules(user.user_id);
        System.out.println(groupList);
        assert groupList.size() > 0;


        List<Resource> resourceList = GritClient.getUserMenus(user.user_id, groupList.get(0).group_id);

        System.out.println(resourceList);
        assert resourceList.size() > 0;
    }

    @Test
    public void test2() throws Exception{

    }
}
