package gritdock.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Resource;
import gritdock.Config;
import gritdock.dso.Session;

import java.io.IOException;
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
        } catch (Exception e) {
            EventBus.push(e);
        }
    }

    private void build(Environment env, TemplateDirectiveBody body) throws Exception {
        Context context = Context.current();

        String path = context.pathNew();
        ResourceSpace branch = null;

        List<ResourceGroup> list = null;
        {
            String groupCode = GritUtil.buildGroupCodeByPath(path);

            if (TextUtils.isEmpty(groupCode)) {
                list = new ArrayList<>();
            } else {
                path = GritUtil.cleanGroupCodeAtPath(path);

                branch = GritClient.resourceSpace().getSpaceByCode(groupCode);
                list = GritClient.auth().getSubjectUriGroupListBySpace(Session.current().getUserId(), branch.resource_id);
            }
        }


        StringBuilder buf = new StringBuilder();
        buf.append("<header>");

        buf.append("<label>");

        if (branch == null) {
            buf.append(Config.title());
        } else {
            if (TextUtils.isEmpty(branch.display_name)) {
                buf.append(Config.title());
            } else {
                buf.append(branch.display_name);
            }
        }

        buf.append("</label>");

        buf.append("<nav>");

        if (branch != null) {
            long userId = Session.current().getUserId();
            for (ResourceGroup module : list) {
                try {
                    Resource res = GritClient.auth().getSubjectUriFristByGroup(userId, module.resource_id);

                    if (TextUtils.isEmpty(res.link_uri) == false) {
                        buildModuleItem(buf, branch, module, res, path);
                    }
                } catch (Exception e) {
                    EventBus.push(e);
                }
            }
        }

        buf.append("</nav>");


        buf.append("<aside>");

        String temp = Session.current().getDisplayName();
        if (temp != null) {
            buf.append("<t onclick='modifyMm(); return false;'>");
            buf.append("<i class='fa fa-user'></i> ");
            buf.append(temp);
            buf.append("</t>");
        }
        buf.append("<a onclick='_dock_home_open();'><img src='/img/app_w.png'/></a>");

        buf.append("<a class='logout' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        buf.append("</aside>");

        buf.append("</header>");


        env.getOut().write(buf.toString());

    }

    private void buildModuleItem(StringBuilder buf, ResourceSpace branch, ResourceGroup module, Resource res, String path) {
        if (TextUtils.isEmpty(module.link_uri)) {
            return;
        }

        String newUrl = GritUtil.buildDockFullUri(branch, res);

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
