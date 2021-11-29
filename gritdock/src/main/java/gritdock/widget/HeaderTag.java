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
import org.noear.grit.client.utils.TextUtils;
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
        if (Session.current().getSubjectId() == 0) {   //检查用户是已登录
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
        ResourceSpace resourceSpace = null;

        List<ResourceGroup> list = null;
        {
            String spaceCode = GritUtil.parseSpaceCodeByPath(path);

            if (TextUtils.isEmpty(spaceCode)) {
                list = new ArrayList<>();
            } else {
                path = GritUtil.cleanSpaceCodeAtPath(path);

                resourceSpace = GritClient.global().resource().getSpaceByCode(spaceCode);
                list = GritClient.global().auth().getUriGroupListBySpace(Session.current().getSubjectId(), resourceSpace.resource_id);
            }
        }


        StringBuilder buf = new StringBuilder();
        buf.append("<header>");

        buf.append("<label>");

        if (resourceSpace == null) {
            buf.append(Config.title());
        } else {
            if (TextUtils.isEmpty(resourceSpace.display_name)) {
                buf.append(Config.title());
            } else {
                buf.append(resourceSpace.display_name);
            }
        }

        buf.append("</label>");

        buf.append("<nav>");

        if (resourceSpace != null) {
            long userId = Session.current().getSubjectId();
            for (ResourceGroup resourceGroup : list) {
                try {
                    Resource res = GritClient.global().auth().getUriFristByGroup(userId, resourceGroup.resource_id);

                    if (TextUtils.isEmpty(res.link_uri) == false) {
                        buildGroupItem(buf, resourceSpace, resourceGroup, res, path);
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

    private void buildGroupItem(StringBuilder buf, ResourceSpace resourceSpace, ResourceGroup resourceGroup, Resource res, String path) {
        if (TextUtils.isEmpty(resourceGroup.link_uri)) {
            return;
        }

        String newUrl = GritUtil.buildDockSpaceUri(resourceSpace, res);

        if (path.indexOf(resourceGroup.link_uri) == 0) {
            buf.append("<a class='sel' href='" + newUrl + "'>");
            buf.append(resourceGroup.display_name);
            buf.append("</a>");
        } else {
            buf.append("<a href='" + newUrl + "'>");
            buf.append(resourceGroup.display_name);
            buf.append("</a>");
        }
    }
}
