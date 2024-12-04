package features;

import example1.client.ClientApp;
import org.junit.jupiter.api.Test;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.core.handle.Result;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

/**
 * @author noear 2024/12/4 created
 */
@SolonTest(ClientApp.class)
public class LoginTest extends HttpTester {
    @Test
    public void test() throws Exception {
        Result<Subject> rs = path("/login")
                .data("username", "test")
                .data("password", "1234")
                .getAs(new Result<Subject>() {}.getClass());

        assert rs.getCode() == Result.SUCCEED_CODE;
        assert rs.getData().subject_id > 0;
    }
}
