package gritdock.controller;

import lombok.extern.slf4j.Slf4j;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.Resource;
import gritdock.dso.Session;
import gritdock.util.ImageUtils;
import gritdock.util.RandomUtils;
import org.noear.solon.core.handle.Result;
import org.slf4j.MDC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Grit dock 登录控制器
 *
 * @author noear
 * @since 1.0
 */
@Slf4j
@Controller
public class LoginController extends BaseController {

    @Mapping("/")
    public void home(Context ctx) throws Exception {
        ctx.forward("/login");
    }

    @Mapping("/login") //视图 返回
    public ModelAndView login() {
        Session.current().clear();

        return view("login");
    }
    //-----------------

    //ajax.path like "{view}/ajax/{cmd}"

    @Mapping("/login/ajax/check")
    public Result login_ajax_check(Context ctx, String userName, String passWord, String captcha) throws Exception {

        //空内容检查
        if (Utils.isEmpty(captcha)) {
            return Result.failure("提示：请输入验证码！");
        }

        if (Utils.isEmpty(userName) || Utils.isEmpty(passWord)) {
            return Result.failure("提示：请输入账号和密码！");
        }

        //验证码检查
        MDC.put("tag1", ctx.path());
        MDC.put("tag2", userName);

        String captchaOfSessoin = Session.current().getValidation();
        if (captcha.equalsIgnoreCase(captchaOfSessoin) == false) {
            log.info("userName={}, captcha={}, captchaOfSessoin={}", userName, captcha, captchaOfSessoin);
            return Result.failure("提示：验证码错误！");
        }

        //用户登录
        Subject subject = GritClient.global().auth().login(userName, passWord);

        if (Subject.isEmpty(subject)) {
            return Result.failure("提示：账号或密码不对！");
        } else {
            log.info("userName={}, ip={}, 登录成功...", userName, ctx.realIp());

            //1.用户登录::成功
            Session.current().loadSubject(subject);

            //2.获取权限资源地址
            String resUrl = getUriFrist(subject.subject_id, ctx);

            //3.返回提示
            if (Utils.isEmpty(resUrl)) {
                return Result.failure("提示：请联系管理员开通权限！");
            } else {
                return Result.succeed(resUrl);
            }
        }
    }

    //$共享SESSOIN$::自动跳转
    @Mapping("/login/auto")
    public void login_auto(Context ctx) throws Exception {
        long subjectId = Session.current().getSubjectId();

        if (subjectId > 0) {
            //获取权限资源地址
            String resUrl = getUriFrist(subjectId, ctx);

            if (Utils.isNotEmpty(resUrl)) {
                //日志一下
                String userName = Session.current().getLoginName();

                MDC.put("tag1", ctx.path());
                MDC.put("tag2", userName);

                log.info("userName={}, ip={}, 自动登录成功...", userName, ctx.realIp());

                //跳转目标页

                String[] ss = resUrl.split("@");
                if (ss.length == 2) {
                    resUrl = ss[0] + "@" + URLEncoder.encode(ss[1], "UTF-8");
                }

                ctx.redirect(resUrl);

                return;
            }
        }

        ctx.redirect("/login");
    }

    public String getUriFrist(long subjectId, Context ctx) throws SQLException {
        //最后一次使用的连接系统
        String spaceCode = ctx.cookie("_lLnQIO4W");
        ResourceSpace space = null;

        Resource res = null;


        //1.确定分支组
        if (Utils.isEmpty(spaceCode) == false) {
            space = GritClient.global().resource().getSpaceByCode(spaceCode);
        }

        if (space == null || space.resource_id == null) {
            space = GritClient.global().auth().getSpaceFrist(subjectId);
        }

        //2.如果没有，找自己默认的权限
        res = GritClient.global().auth().getUriFristBySpace(subjectId, space.resource_id);

        //3.再没有，提示错误
        if (Utils.isEmpty(res.link_uri)) {
            return null;
        } else {
            return GritUtil.buildDockSpaceUri(space, res);
        }
    }

    /*
     * 获取验证码图片
     */
    @Mapping(value = "/login/validation/img", method = MethodType.GET, produces = "image/jpeg")
    public void getValidationImg(Context ctx) throws IOException {
        // 生成验证码存入session
        String code = RandomUtils.code(4);
        Session.current().setValidation(code);
        ctx.sessionState().sessionPublish();

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

    @Mapping("/user/modifymm")
    public ModelAndView modifyPassword() {
        return view("passwordModify");
    }

    //确认修改密码
    @Mapping("/user/confirmModify")
    public Map<String, String> confirmModify(String newPass, String oldPass) throws SQLException {
        HashMap<String, String> result = new HashMap<>();

        String loginName = Session.current().getLoginName();
        int success = GritClient.global().subject().modSubjectPassword(loginName, oldPass, newPass);

        //0:出错；1：旧密码不对；2：修改成功
        if (0 == success) {
            result.put("code", "0");
            result.put("msg", "出错了");
        }

        if (1 == success) {
            result.put("code", "0");
            result.put("msg", "旧密码不对");
        }

        if (2 == success) {
            result.put("code", "1");
            result.put("msg", "修改成功");
        }

        return result;
    }
}
