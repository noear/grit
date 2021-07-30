package gritdock.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.client.model.Branch;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import gritdock.Config;
import gritdock.dso.Session;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component("ftl:header")
public class HeaderTag implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (Session.current().getUserId() == 0) {   //检查用户是已登录
            Context.current().redirect("/login");
            return;
        }

        try {
            build(env, body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void build(Environment env, TemplateDirectiveBody body) throws Exception {
        Context context = Context.current();
        //当前视图path //此处改过，20180831
        String cPath = context.path();

        Branch groupBranched = null;
        List<Group> list = null;
        {
            String p = GritUtil.buildGroupCodeByPath(cPath);

            if (TextUtils.isEmpty(p)) {
                list = new ArrayList<>();
            } else {

                cPath = GritUtil.cleanGroupCodeAtPath(cPath);

                groupBranched = GritClient.branched().getBranchByCode(p);
                list = GritClient.getUserModules(Session.current().getUserId(), groupBranched.group_id);
            }
        }


        StringWriter sb = new StringWriter();
        sb.append("<header>");

        sb.append("<label>"); //new
        //cls1
        if (groupBranched == null) {
            sb.append(Config.title());
        } else {
            if (TextUtils.isEmpty(groupBranched.display_code)) {
                sb.append(Config.title());
            } else {
                sb.append(groupBranched.display_code);
            }
        }

        sb.append("</label>");


        sb.append("<nav>");


        if (groupBranched != null) {
            long userId = Session.current().getUserId();
            for (Group g : list) {
                try {
                    Resource res = GritClient.getUserMenusFirstOfModule(userId, g.group_id);

                    if (TextUtils.isEmpty(res.link_uri) == false) {
                        buildItem(sb, g.display_name, groupBranched, res, cPath, g.link_uri); //::en_name 改为 uri_path
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); //以防万一；万一出错头都看不见了
                }
            }
        }

        sb.append("</nav>");


        sb.append("<aside>");

        String temp = Session.current().getDisplayName();
        if (temp != null) {
            sb.append("<t onclick='modifyMm(); return false;'>");
            sb.append("<i class='fa fa-user'></i> ");
            sb.append(temp);
            sb.append("</t>");
        }
        sb.append("<a onclick='_dock_home_open();'><img src='/img/app_w.png'/></a>");

        sb.append("<a class='logout' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        sb.append("</aside>");

        sb.append("</header>");


        env.getOut().write(sb.toString());

    }

    private void buildItem(StringWriter sb, String title, Branch groupBranched, Resource res, String cPath, String pack) {

        if (TextUtils.isEmpty(pack)) {
            return;
        }

        //此处改过，201811(uadmin)
        String newUrl = GritUtil.buildDockurl(groupBranched, res);

        if (cPath.indexOf(pack) == 0) {
            sb.append("<a class='sel' href='" + newUrl + "'>");
            sb.append(title);
            sb.append("</a>");
        } else {
            sb.append("<a href='" + newUrl + "'>");
            sb.append(title);
            sb.append("</a>");
        }

    }
}
