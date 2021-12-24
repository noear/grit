package example1.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;

/**
 * @author noear 2021/6/1 created
 */
@Controller
public class LoginController {
    @Mapping("login")
    public void login(Context ctx, String username, String password) {
        //登录处理；如果成功...
        ctx.sessionSet("user_id",1);
    }
}
