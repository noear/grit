package example1.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("view:header")
public class HeaderTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void build(Environment env) throws Exception {
        //当前视图path //此处改过，noear，20180831
        Context ctx = Context.current();
        String cPath = ctx.path();
        long userId = ctx.sessionAsLong("user_id");
        String userDisplayName = ctx.session("user_display_name", "");


        if (userId == 0) {   //检查用户是已登录
            ctx.redirect("/login");
            return;
        }

        List<ResourceGroup> list = GritClient.global().auth().getUriGroupList(userId);

        if (list.size() == 0) {
            ctx.redirect("/login");
            return;
        }


        StringBuffer sb = new StringBuffer();
        sb.append("<header>");

        sb.append("<label>"); //new
        sb.append(Solon.cfg().appTitle());
        sb.append("</label>\n");//new


        sb.append("<nav>");

        for (ResourceGroup g : list) {
            List<ResourceEntity> resourceList = GritClient.global().auth().getUriListByGroup(userId, g.resource_id);

            if (resourceList.size() > 0) {
                if (Utils.isEmpty(resourceList.get(0).link_uri) == false) {
                    buildItem(sb, g.display_name, resourceList.get(0), cPath, g.link_uri); //::en_name 改为 uri_path
                }
            }
        }

        sb.append("</nav>\n");

        sb.append("<aside>");//new

        if (Utils.isNotEmpty(userDisplayName)) {
            sb.append("<i class='fa fa-user'></i> ");
            sb.append(userDisplayName);
        }

        sb.append("<a class='logout' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        sb.append("</aside>");//new

        sb.append("</header>\n");

        env.getOut().write(sb.toString());
    }

    private void buildItem(StringBuffer sb, String title, Resource res, String cPath, String pack) {

        //此处改过，noear，201811(uadmin)
        String newUrl = GritUtil.buildDockUri(res);

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