package example2.widget;


import example2.dso.Session;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 14-9-10.
 */
@Component("view:toolmenu")
public class ToolmenuTag implements TemplateDirectiveModel {
    private String pack;

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env, map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env, Map map) throws Exception {
        NvMap mapExt = new NvMap(map);

        pack = mapExt.get("pack");

        Context request = Context.current();
        //当前视图path
        String cPath = request.pathNew();
        StringBuffer sb = new StringBuffer();

        Group gPack = GritClient.group().getGroupByCode(pack);

        if (gPack.group_id > 0) {
            sb.append("<toolmenu>");
            sb.append("<tabbar>");

            forPack(request, gPack.group_id, sb, cPath);

            sb.append("</tabbar>");
            sb.append("</toolmenu>");

            env.getOut().write(sb.toString());
        }
    }

    private void forPack(Context request, long packID, StringBuffer sb, String cPath) throws SQLException {
        List<Resource> list = GritClient.getUserMenus(Session.current().getUserId(), packID);

        for (Resource r : list) {
            buildItem(request, sb, r.display_name, r.link_uri, cPath);
        }
    }

    private void buildItem(Context request, StringBuffer sb, String title, String url, String cPath) {
        String url2 = url + "?" + request.uri().getQuery();

        if (cPath.indexOf(url) > 0) {
            sb.append("<button onclick=\"location='" + url2 + "'\" class='sel'>");
            sb.append(title);
            sb.append("</button>");
        } else {
            sb.append("<button onclick=\"location='" + url2 + "'\">");
            sb.append(title);
            sb.append("</button>");
        }
    }
}