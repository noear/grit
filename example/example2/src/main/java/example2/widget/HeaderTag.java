package example2.widget;

import example2.dso.Session;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Resource;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("view:header")
public class HeaderTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env);
        } catch (Exception e) {
            EventBus.push(e);
        }
    }

    private void build(Environment env) throws Exception {
        Context ctx = Context.current();
        long userId = Session.current().getUserId();

        String path = ctx.pathNew();

        if (userId == 0) {   //检查用户是已登录
            ctx.redirect("/login");
            return;
        }

        List<ResourceGroup> moduleList = GritClient.auth().getUriGroupListBySpace(userId);

        if (moduleList.size() == 0) {
            ctx.redirect("/login");
            return;
        }


        StringBuilder buf = new StringBuilder();
        buf.append("<header>");

        buf.append("<label>"); //new
        buf.append(Solon.cfg().appTitle());
        buf.append("</label>\n");//new


        buf.append("<nav>");

        for (ResourceGroup module : moduleList) {
            ResourceEntity res = GritClient.auth().getUriFristByGroup(userId, module.resource_id);

            if (Utils.isEmpty(res.link_uri) == false) {
                buildModuleItem(buf, module, res, path);
            }
        }

        buf.append("</nav>\n");

        buf.append("<aside>");

        String userDisplayName = Session.current().getDisplayName();
        if (Utils.isNotEmpty(userDisplayName)) {
            buf.append("<i class='fa fa-user'></i> ");
            buf.append(userDisplayName);
        }

        buf.append("<a class='logout' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        buf.append("</aside>");

        buf.append("</header>\n");

        env.getOut().write(buf.toString());
    }

    private void buildModuleItem(StringBuilder buf, ResourceGroup module, Resource res, String path) {
        String newUrl = GritUtil.buildDockUri(res);

        if (path.indexOf(module.link_uri) == 0) {
            buf.append("<a class='sel' href='" + newUrl + "'>");
            buf.append(module.display_name);
            buf.append("</a>");
        } else {
            buf.append("<a href='" + newUrl + "'>");
            buf.append(module.display_name);
            buf.append("</a>");
        }
    }
}
