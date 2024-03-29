package example1.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Resource;

import java.io.IOException;
import java.sql.SQLException;
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
        //当前视图path //此处改过，noear，20180831
        String cPath = ctx.path();
        long userId = ctx.sessionAsLong("user_id");

        StringBuilder sb = new StringBuilder();

        List<ResourceGroup> plist = GritClient.global().auth().getUriGroupList(userId);
        long packID = 0;
        for (ResourceGroup p : plist) {
            if (cPath.indexOf(p.link_uri) == 0) {
                packID = p.resource_id;
                break;
            }
        }

        sb.append("<menu>");

        sb.append("<div onclick=\"$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}\"><i class='fa fa-bars'></i></div>");

        sb.append("<items>");

        forPack(userId, packID, sb, cPath);

        sb.append("</items>");

        sb.append("</menu>");

        env.getOut().write(sb.toString());

    }

    private void forPack(long userId, long packID, StringBuilder sb, String cPath) throws SQLException {
        List<ResourceEntity> list = GritClient.global().auth().getUriListByGroup(userId, packID);

        for (Resource res : list) {
            buildItem(sb, res, cPath);
        }
    }

    private void buildItem(StringBuilder sb, Resource res, String cPath) {
        if ("$".equals(res.display_name)) {
            sb.append("<br/><br/>");
            return;
        }

        //此处改过，noear，201811(uadmin)
        String newUrl = GritUtil.buildDockUri(res);

        //此处改过，noear，20180831
        if (cPath.indexOf(res.link_uri) >= 0) {
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
