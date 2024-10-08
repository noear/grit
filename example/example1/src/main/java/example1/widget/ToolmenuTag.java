package example1.widget;


import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;

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
        NvMap mapExt = NvMap.from(map);

        pack = mapExt.getOrDefault("pack", "");

        Context ctx = Context.current();
        //当前视图path
        String cPath = ctx.path();
        long userId = ctx.sessionAsLong("user_id");

        StringBuffer sb = new StringBuffer();

        Resource gPack = GritClient.global().resource().getResourceByCode(pack);

        if (gPack.resource_id > 0) {
            sb.append("<toolmenu>");
            sb.append("<tabbar>");

            forPack(ctx, userId, gPack.resource_id, sb, cPath);

            sb.append("</tabbar>");
            sb.append("</toolmenu>");

            env.getOut().write(sb.toString());
        }
    }

    private void forPack(Context ctx, long userId, long packID, StringBuffer sb, String cPath) throws SQLException {
        List<ResourceEntity> list = GritClient.global().auth().getUriListByGroup(userId, packID);

        for (Resource r : list) {
            buildItem(ctx, sb, r.display_name, r.link_uri, cPath);
        }
    }

    private void buildItem(Context ctx, StringBuffer sb, String title, String url, String cPath) {
        String url2 = url + "?" + ctx.uri().getQuery();

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
