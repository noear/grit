package features;

import org.junit.jupiter.api.Test;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.test.SolonTest;
import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import gritdock.DockApp;

/**
 * @author noear 2021/7/23 created
 */
@SolonTest(DockApp.class)
public class BaseTest {
    @Test
    public void getUser() throws Exception {
        Subject user = GritClient.global().subject().getSubjectById(2);

        System.out.println(user);
        assert user.subject_id == 2;


        Subject user2 = GritClient.global().subject().getSubjectByLoginName("noear");

        assert user.subject_id == user2.subject_id;
    }

    @Test
    public void getGroupById() throws Exception {
        Resource group = GritClient.global().resource().getResourceById(50);

        System.out.println(group);
        assert group.resource_id == 50;
    }


    @Test
    public void getResourceById() throws Exception {
        Resource resource = GritClient.global().resource().getResourceById(10);

        System.out.println(resource);
        assert resource.resource_id == 10;
    }
}
