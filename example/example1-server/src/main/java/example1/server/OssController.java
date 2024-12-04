package example1.server;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.server.admin.dso.Session;
import org.noear.grit.server.admin.util.ImageUtils;
import org.noear.grit.server.admin.util.RandomUtils;
import org.noear.grit.server.api.utils.TokenUtils;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author noear 2024/12/4 created
 */
@Controller
public class OssController {
    @Mapping("oss")
    public ModelAndView oss(Context ctx, String url) throws Exception {
        if(url == null){
            ctx.output("缺少 url");
            return null;
        }

        long user_id = ctx.sessionAsLong("user_id");

        if (user_id > 0L) {
            if (url.indexOf('?') > 0) {
                url = url + "&token=" + TokenUtils.encode(user_id);
            } else {
                url = url + "?token=" + TokenUtils.encode(user_id);
            }
            ctx.redirect(url);
            return null;
        } else {
            ModelAndView mv = new ModelAndView("login.ftl");
            mv.put("url", url);
            return mv;
        }
    }


    @Mapping("/login/ajax/check")  // Map<,> 返回[json]  (ViewModel 是 Map<String,Object> 的子类)
    public Result login_ajax_check(Context ctx, String userName, String passWord, String captcha, String url) throws Exception {

        //验证码检查
        if (!captcha.toLowerCase().equals(Session.current().getValidation())) {
            return Result.failure("提示：验证码错误！");
        }

        if (Utils.isEmpty(userName) || Utils.isEmpty(passWord)) {
            return Result.failure("提示：请输入账号和密码！");
        }

        Subject subject = GritClient.global().auth().login(userName, passWord);

        if (Subject.isEmpty(subject)) {
            return Result.failure("提示：账号或密码不对！");
        } else {
            long user_id = subject.subject_id;

            ctx.sessionSet("user_id", user_id);

            if (url.indexOf('?') > 0) {
                url = url + "&token=" + TokenUtils.encode(user_id);
            } else {
                url = url + "?token=" + TokenUtils.encode(user_id);
            }

            return Result.succeed(url);
        }
    }

    /*
     * 获取验证码图片
     */
    @Mapping(value = "/login/validation/img", method = MethodType.GET, produces = "image/jpeg")
    public void getValidationImg(Context ctx) throws IOException {
        // 生成验证码存入session
        String validation = RandomUtils.code(4);
        Session.current().setValidation(validation);

        // 获取图片
        BufferedImage bufferedImage = ImageUtils.getValidationImage(validation);

        // 禁止图像缓存
        ctx.headerSet("Pragma", "no-cache");
        ctx.headerSet("Cache-Control", "no-cache");
        ctx.headerSet("Expires", "0");

        // 图像输出
        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "jpeg", ctx.outputStream());
    }
}
