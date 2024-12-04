package features;

import example3.App;
import org.junit.jupiter.api.Test;
import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.core.handle.Result;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.util.List;

/**
 * @author noear 2021/7/23 created
 */
@SolonTest(App.class)
public class LoginTest extends HttpTester {

    @Test
    public void loginLocal() throws Exception {

        Subject user = GritClient.global().auth().login("noear", "1234");
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

    @Test
    public void loingHttp() throws Exception {
        Result<Subject> rs = path("/login")
                .data("username", "noear")
                .data("password", "1234")
                .getAs(new Result<Subject>() {}.getClass());

        assert rs.getCode() == Result.SUCCEED_CODE;
        assert rs.getData().subject_id > 0;
    }
}
