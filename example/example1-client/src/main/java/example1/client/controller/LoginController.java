package example1.client.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

/**
 * @author noear 2024/12/4 created
 */
@Controller
public class LoginController {
    @Mapping("login")
    public Result login(String username, String password) throws Exception {
        Subject subject = GritClient.global().auth().login(username, password);

        if (subject == null || subject.subject_id == 0) {
            return Result.failure();
        } else {
            return Result.succeed(subject);
        }
    }
}