package gritdock.controller;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.weed.cache.CacheUsing;
import gritdock.Config;
import gritdock.dso.Session;
import gritdock.dso.MenuUtil;
import gritdock.models.MenuViewModel;

import java.net.URLDecoder;
import java.sql.SQLException;

/**
 * Created by noear on 18-10-10.
 */
@Controller
public class DockController extends BaseController {

    /**
     * 显示所有权限
     */
    @Mapping("/dock/home") //视图 返回
    public ModelAndView dock_home() throws SQLException {

        long userId = Session.current().getUserId();

        //获取所有模块菜单
        CacheUsing cu = new CacheUsing(Config.cache());
        MenuViewModel vm = cu.getEx("user_menus_x_" + userId, () -> MenuUtil.buildSystemMenus());

        int section_margin = 20;
        int header_margin = 5;

        if (vm.count > 5) {
            section_margin = 20 - vm.count * 2;

            if (section_margin < 5) {
                section_margin = 5;
            }

            header_margin = 5 - (vm.count / 2);
            if (header_margin < 0) {
                header_margin = 0;
            }
        }

        viewModel.put("section_margin", section_margin);
        viewModel.put("header_margin", header_margin);
        viewModel.put("code", vm.code);

        return view("dock_home");
    }


    //支持外部url
    @Mapping("/**/$*") //视图 返回
    public ModelAndView dock1(Context ctx) {
        String path = ctx.path();
        String query = ctx.queryString();

        path = GritUtil.cleanGroupCodeAtPath(path);

        try {
            Resource res = GritClient.resource().getResourceByPath(path);
            viewModel.set("fun_name", res.display_name);
            viewModel.set("fun_url", optimizeUrl(res.link_uri));

            if (query != null && query.indexOf("@=") >= 0) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception ex) {

        }

        return view("dock");
    }

    /**
     * 支持内部虚拟地址（之前的$*已不需要了）
     */
    @Mapping("/**/@*") //视图 返回
    public ModelAndView dock2(Context ctx) throws Exception {
        String uri = ctx.path();
        String query = ctx.queryString();

        String fun_name = uri.split("/@")[1]; // /x/x/@x   =>  /x/x   +  服务监控
        String fun_url = uri.split("/@")[0];

        fun_url = GritUtil.cleanGroupCodeAtPath(fun_url);

        String newUrl = fun_url;
        String p = GritUtil.buildGroupCodeByPath(uri);
        String r = ctx.param("__r");

        //如果有r参数传入，则用r.note获取域 (r = res_id)
        if (Utils.isEmpty(r) == false) {
            Resource res = GritClient.resource().getResourceById(Integer.parseInt(r));
            if (res.link_uri != null && res.link_uri.indexOf("://") > 0) {
                newUrl = res.link_uri + fun_url;
            }
        }

        if (Utils.isEmpty(p) == false) {

            //如果还没有域尝试从根包获取
            if (newUrl.indexOf("://") < 0) {
                Group pack = GritClient.group().getGroupByCode(p);

                if (Utils.isEmpty(pack.link_uri) == false) {
                    newUrl = pack.link_uri + fun_url;
                    //记录cookie
                }
            }

            ctx.cookieSet("_lLnQIO4W", p, 60 * 60 * 24 * 365);
        }

        //传递参数
        if (Utils.isEmpty(query) == false) {
            if (newUrl.indexOf("?") > 0) {
                newUrl = newUrl + "&" + query;
            } else {
                newUrl = newUrl + "?" + query;
            }
        }


        try {


            viewModel.set("fun_name", URLDecoder.decode(fun_name, "utf-8"));
            viewModel.set("fun_url", optimizeUrl(newUrl));

            if (query != null && query.indexOf("@=") >= 0) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception ex) {

        }

        return view("dock");
    }

    public String optimizeUrl(String url) throws Exception {
        if (url == null) {
            return null;
        }

        if (url.indexOf("{{") > 0) {
            ONode tmp = GritClient.user().getUserMeta(Session.current().getUserId());
            for (String key : tmp.obj().keySet()) {
                url = url.replace("{{" + key + "}}", tmp.get(key).getString());
            }

            return url;
        } else {
            return url;
        }

    }
}
