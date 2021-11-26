package gritdock.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Resource;
import gritdock.dso.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("ftl:leftmenu")
public class LeftmenuTag implements TemplateDirectiveModel {
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

    public void build(Environment env, TemplateDirectiveBody body) throws Exception {

        Context ctx = Context.current();
        long userId = Session.current().getUserId();

        String path = ctx.pathNew();
        ResourceSpace branch;

        StringBuilder buf = new StringBuilder();
        List<ResourceGroup> moduleList = null;
        {
            String groupCode = GritUtil.parseSpaceCodeByPath(path);
            branch = GritClient.resourceSpace().getSpaceByCode(groupCode);

            if (Utils.isEmpty(groupCode)) {
                moduleList = new ArrayList<>();
            } else {

                path = GritUtil.cleanSpaceCodeAtPath(path);

                moduleList = GritClient.auth().getUriGroupListBySpace(userId, branch.resource_id);
            }
        }

        long moduleId = 0;
        for (ResourceGroup module : moduleList) {
            if (path.indexOf(module.link_uri) == 0) { //::en_name 改为 uri_path
                moduleId = module.resource_id;
                break;
            }
        }

        buf.append("<menu>");
        buf.append("<div onclick=\"$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}\"><img src='/img/menu_w.png'/></div>");
        buf.append("<items>");

        List<ResourceEntity> resList = GritClient.auth().getUriListByGroup(userId, moduleId);

        for (Resource res : resList) {
            buildMenuItem(buf, branch, res, path);
        }

        buf.append("</items>");
        buf.append("</menu>");


        env.getOut().write(buf.toString());
    }


    private void buildMenuItem(StringBuilder sb, ResourceSpace branch, Resource res, String path) {
        if ("$".equals(res.display_name)) {
            sb.append("<br/><br/>");
            return;
        }

        String newUrl = GritUtil.buildDockSpaceUri(branch, res);

        if (path.indexOf(res.link_uri) >= 0) {
            sb.append("<a class='sel' href='" + newUrl + "'>");
            sb.append(res.display_name);
            sb.append("</a>");
        } else {
            sb.append("<a href='" + newUrl + "'>");
            sb.append(res.display_name);
            sb.append("</a>");
        }
    }
}