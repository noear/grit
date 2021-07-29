package clienttest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.grit.client.model.User;
import gritdock.App;

/**
 * @author noear 2021/7/23 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(App.class)
public class BaseTest {
    @Test
    public void getUser() throws Exception {
        User user = GritClient.user().getUserById(2);

        System.out.println(user);
        assert user.user_id == 2;


        User user2 = GritClient.user().getUserByLoginName("noear");

        assert user.user_id == user2.user_id;
    }

    @Test
    public void login() throws Exception {
        User user = GritClient.login("noear", "bcf1234");

        System.out.println(user);
        assert user.user_id == 2;
    }

    @Test
    public void getGroupById() throws Exception {
        Group group = GritClient.group().getGroupById(50);

        System.out.println(group);
        assert group.group_id == 50;
    }


    @Test
    public void getResourceById() throws Exception {
        Resource resource = GritClient.resource().getResourceById(10);

        System.out.println(resource);
        assert resource.resource_id == 10;
    }
}
