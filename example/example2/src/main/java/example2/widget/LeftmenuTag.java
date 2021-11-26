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
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("view:leftmenu")
public class LeftmenuTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env) throws Exception {

        Context ctx = Context.current();
        String path = ctx.pathNew();


        StringBuilder buf = new StringBuilder();

        List<ResourceGroup> moduleList = GritClient.auth().getUriGroupListBySpace(Session.global().getUserId());
        long moduleId = 0;
        for (ResourceGroup p : moduleList) {
            if (path.startsWith(p.link_uri)) { //::en_name 改为 uri_path
                moduleId = p.resource_id;
                break;
            }
        }

        buf.append("<menu>");

        buf.append("<div onclick=\"$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}\"><i class='fa fa-bars'></i></div>");

        buf.append("<items>");

        List<ResourceEntity> resList = GritClient.auth().getUriListByGroup(Session.current().getUserId(), moduleId);

        for (Resource res : resList) {
            buildMenuItem(buf, res, path);
        }

        buf.append("</items>");

        buf.append("</menu>");

        env.getOut().write(buf.toString());

    }


    private void buildMenuItem(StringBuilder buf, Resource res, String path) {
        if ("$".equals(res.display_name)) {
            buf.append("<br/><br/>");
            return;
        }

        String newUrl = GritUtil.buildDockUri(res);

        if (path.indexOf(res.link_uri) >= 0) {
            buf.append("<a class='sel' href='" + newUrl + "'>");
            buf.append(res.display_name);
            buf.append("</a>");
        } else {
            buf.append("<a href='" + newUrl + "'>");
            buf.append(res.display_name);
            buf.append("</a>");
        }
    }
}
