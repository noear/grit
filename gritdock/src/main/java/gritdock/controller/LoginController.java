package gritdock.controller;

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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 14-9-10.
 */
@Controller
public class LoginController extends BaseController {

    @Mapping("/login") //视图 返回
    public ModelAndView login(Context ctx) {
        ctx.sessionClear();

        return view("login");
    }

    @Mapping("/")
    public void index(Context ctx) throws Exception {
        ctx.redirect("/login");
    }
    //-----------------

    //ajax.path like "{view}/ajax/{cmd}"

    @Mapping("/login/ajax/check")
    public ViewModel login_ajax_check(Context ctx, String userName, String passWord, String validationCode) throws Exception {

        //验证码检查
        if (!validationCode.toLowerCase().equals(Session.current().getValidation())) {
            return viewModel.set("code", 0).set("msg", "提示：验证码错误！");
        }

        if (Utils.isEmpty(userName) || Utils.isEmpty(passWord)) {
            return viewModel.set("code", 0).set("msg", "提示：请输入账号和密码！");
        }

        //用户登录
        Subject subject = GritClient.auth().login(userName, passWord);

        if (subject.subject_id == 0)
            //用户登录::失败
            //
            return viewModel.set("code", 0).set("msg", "提示：账号或密码不对！");
        else {
            //用户登录::成功
            //
            Session.current().loadSubject(subject);

            //最后一次使用的连接系统
            String branchCode = ctx.cookie("_lLnQIO4W");
            ResourceSpace branch = null;

            Resource res = null;


            //1.确定分支组
            if (Utils.isEmpty(branchCode) == false) {
                branch = GritClient.resourceSpace().getSpaceByCode(branchCode);
            }

            if (branch == null || branch.resource_id == null) {
                branch = GritClient.resourceSpace().getSpaceFristByUser(subject.subject_id);
            }

            //2.如果没有，找自己默认的权限
            res = GritClient.auth().getSubjectUriFristBySpace(subject.subject_id, branch.resource_id);

            //3.再没有，提示错误
            if (Utils.isEmpty(res.link_uri)) {
                return viewModel.set("code", 0).set("msg", "提示：请联系管理员开通权限");
            }

            String newUrl = GritUtil.buildDockFullUri(branch, res);

            return viewModel.set("code", 1)
                    .set("msg", "ok")
                    .set("url", newUrl);

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
        int success = GritClient.subject().modSubjectPassword(loginName, oldPass, newPass);

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
