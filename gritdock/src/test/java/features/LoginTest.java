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

        Subject user = GritClient.auth().login("noear", "bcf1234"); //1D050B5785B44868E6C41EE9ED990354A7FA5A55
        System.out.println(user);
        assert user.subject_id == 2;


        GritClient.switchSpace("wateradmin");

        List<ResourceGroup> groupList = GritClient.auth().getSubjectUriGroupListBySpace(user.subject_id);
        System.out.println(groupList);
        assert groupList.size() > 0;


        List<ResourceEntity> resourceList = GritClient.auth().getSubjectUriListByGroup(user.subject_id, groupList.get(0).resource_id);

        System.out.println(resourceList);
        assert resourceList.size() > 0;
    }

    @Test
    public void test2() throws Exception{

    }
}
