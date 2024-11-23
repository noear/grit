package org.noear.grit.server.admin.controller;

import org.noear.grit.Grit;
import org.noear.grit.server.GritServerConfig;
import org.noear.grit.server.admin.AdminConfig;
import org.noear.grit.server.admin.dso.Session;
import org.noear.grit.server.admin.util.ImageUtils;
import org.noear.grit.server.admin.util.RandomUtils;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Valid;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author noear
 * @since 1.0
 */
@Valid
@Singleton(false)
@Mapping("grit")
@Controller
public class GritController {
    /*视图数据模型*/
    protected ModelAndView viewModel = new ModelAndView();

    /*
     * @return 输出一个视图（自动放置viewModel）
     * @param viewName 视图名字(内部uri)
     * */
    public ModelAndView view(String viewName) {
        String title = AdminConfig.title();

        //设置必要参数
        viewModel.put("app", "Grit");

        viewModel.put("css", GritServerConfig.staticPrefix() + "/css");
        viewModel.put("js", GritServerConfig.staticPrefix() + "/js");
        viewModel.put("img", GritServerConfig.staticPrefix() + "/img");

        viewModel.put("title", title);

        viewModel.put("_version", Grit.version());


        return viewModel.view(viewName + ".ftl");
    }

    @Mapping("/")
    public ModelAndView home() {
        return view("grit/dock");
    }

    @Mapping("/login") //视图 返回
    public ModelAndView login(Context ctx) {
        ctx.sessionSet(Session.GRIT_ADMIN_TOKEN,"");

        return view("grit/login");
    }
    //-----------------

    //ajax.path like "{view}/ajax/{cmd}"
    @Mapping("/login/ajax/check")
    public Result login_ajax_check(Context ctx, String admin_userName, String admin_passWord, String captcha) throws Exception {

        //验证码检查
        if (!captcha.toLowerCase().equals(getValidation(ctx))) {
            return Result.failure("提示：验证码错误！");
        }

        if (Utils.isEmpty(admin_userName) || Utils.isEmpty(admin_passWord)) {
            return Result.failure("提示：请输入账号和密码！");
        }

        String user0 = Solon.cfg().get("gritadmin.user", "admin");
        String password0 = Solon.cfg().get("gritadmin.password", "");

        boolean isLogin = (user0.equals(admin_userName) && password0.equals(admin_passWord));


        if (isLogin == false)
            return Result.failure("提示：账号或密码不对！"); //set 直接返回；有利于设置后直接返回，不用另起一行
        else {
            String admin_token = Utils.md5(user0 + "#" + password0);
            ctx.sessionSet(Session.GRIT_ADMIN_TOKEN, admin_token);

            return Result.succeed("/grit/");
        }
    }


    /*
     * 获取验证码图片
     */
    @Mapping(value = "/login/validation/img", method = MethodType.GET, produces = "image/jpeg")
    public void getValidationImg(Context ctx) throws IOException {
        // 生成验证码存入session
        String code = RandomUtils.code(4);
        setValidation(ctx, code);

        // 获取图片
        BufferedImage bufferedImage = ImageUtils.getValidationImage(code);

        // 禁止图像缓存
        ctx.headerSet("Pragma", "no-cache");
        ctx.headerSet("Cache-Control", "no-cache");
        ctx.headerSet("Expires", "0");

        // 图像输出
        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "jpeg", ctx.outputStream());
    }

    protected final String getValidation(Context ctx) {
        return (String) ctx.session("grit_validation_string");
    }

    protected final void setValidation(Context ctx, String validation) {
        ctx.sessionSet("grit_validation_string", validation.toLowerCase());
    }
}
