package example2.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;

/**
 * @author noear 2021/6/1 created
 */
@Controller
public class LoginController {
    @Mapping("login")
    public void login(Context ctx, String username, String password) throws Exception {
        //登录处理；如果成功...
        Subject subject = GritClient.global().auth().login(username, password);

        if (Subject.isNotEmpty(subject)) {
            ctx.sessionSet("user_id", subject.subject_id);
        }
    }
}