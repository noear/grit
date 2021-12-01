package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Subject;
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

        Subject user = GritClient.global().auth().login("test", "1234");
        System.out.println(user);
        assert user.subject_id > 0;


        GritClient.global().setCurrentSpaceByCode("wateradmin");

        List<ResourceGroup> groupList = GritClient.global().auth().getUriGroupList(user.subject_id);
        System.out.println(groupList);
        assert groupList.size() > 0;


        List<ResourceEntity> resourceList = GritClient.global().auth().getUriListByGroup(user.subject_id, groupList.get(0).resource_id);

        System.out.println(resourceList);
        assert resourceList.size() > 0;
    }
}
