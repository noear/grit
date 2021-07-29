package gritdock.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.StoneClient;
import org.noear.grit.client.StoneUtil;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import gritdock.dso.Session;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env, TemplateDirectiveBody body) throws Exception {

        Context context = Context.current();
        //当前视图path //此处改过，20180831
        String cPath = context.path();
        long userId = Session.global().getUserId();

        StringWriter sb = new StringWriter();

        List<Group> plist = null;
        {
            String p = StoneUtil.buildGroupCodeByPath(cPath);
            StoneClient.branched(p);

            if (Utils.isEmpty(p)) {
                plist = new ArrayList<>();
            } else {

                cPath = StoneUtil.cleanGroupCodeAtPath(cPath);

                plist = StoneClient.getUserModules(userId);
            }
        }

        long packID = 0;
        for (Group p : plist) {
            if (cPath.indexOf(p.link_uri) == 0) { //::en_name 改为 uri_path
                packID = p.group_id;
                break;
            }
        }

        sb.append("<menu>");
        sb.append("<div onclick=\"$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}\"><img src='/_static/img/menu_w.png'/></div>");
        sb.append("<items>");

        forPack(packID, sb, cPath);

        sb.append("</items>");
        sb.append("</menu>");


        env.getOut().write(sb.toString());

    }

    private void forPack(long packID, StringWriter sb, String cPath) throws SQLException {

        List<Resource> list = StoneClient.getUserMenus(Session.current().getUserId(), packID);

        for (Resource res : list) {
            buildItem(sb, res, cPath);
        }
    }

    private void buildItem(StringWriter sb, Resource res, String cPath) {
        if ("$".equals(res.display_name)) {
            sb.append("<br/><br/>");
            return;
        }

        //此处改过，201811(uadmin)
        String newUrl = StoneUtil.buildDockuri(res);

        //此处改过，20180831
        if (cPath.indexOf(res.link_uri) >= 0) //  /x/x   => /x/x/@x
        {
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
