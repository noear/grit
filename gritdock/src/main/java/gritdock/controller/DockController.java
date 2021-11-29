package gritdock.controller;

import gritdock.dso.MenuService;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.Resource;
import gritdock.dso.Session;
import gritdock.model.MenuViewModel;

import java.net.URLDecoder;
import java.sql.SQLException;

/**
 * @author noear
 * @since 1.0
 */
@Controller
public class DockController extends BaseController {

    @Inject
    MenuService menuService;

    /**
     * 显示所有权限
     */
    @Mapping("/dock/home") //视图 返回
    public ModelAndView dock_home() throws SQLException {

        long userId = Session.current().getSubjectId();

        //获取所有模块菜单
        MenuViewModel vm = menuService.getSpaceMenus(userId);

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
        String path = ctx.pathNew();
        path = GritUtil.cleanSpaceCodeAtPath(path);

        try {
            Resource res = GritClient.global().resource().getResourceByUri(path);
            viewModel.set("fun_name", res.display_name);
            viewModel.set("fun_url", res.link_uri);

            if (res.is_fullview) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception e) {
            EventBus.push(e);
        }

        return view("dock");
    }

    /**
     * 支持内部虚拟地址（之前的$*已不需要了）
     */
    @Mapping("/**/@*") //视图 返回
    public ModelAndView dock2(Context ctx) throws Exception {
        String path = ctx.pathNew();
        String query = ctx.queryString();

        String fun_name = path.split("/@")[1];
        String fun_url = path.split("/@")[0];

        fun_url = GritUtil.cleanSpaceCodeAtPath(fun_url);

        String newUrl = fun_url;
        String p = GritUtil.parseSpaceCodeByPath(path);
        String r = ctx.param("__r");

        //如果有r参数传入，则用r.note获取域 (r = res_id)
        if (Utils.isEmpty(r) == false) {
            Resource res = GritClient.global().resource().getResourceById(Integer.parseInt(r));
            if (res.link_uri != null && res.link_uri.indexOf("://") > 0) {
                newUrl = res.link_uri + fun_url;
            }
        }

        if (Utils.isEmpty(p) == false) {

            //如果还没有域尝试从根包获取
            if (newUrl.indexOf("://") < 0) {
                Resource pack = GritClient.global().resource().getResourceByUri(p);

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
            viewModel.set("fun_url", newUrl);

            if (query != null && query.indexOf("@=") >= 0) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception e) {
            EventBus.push(e);
        }

        return view("dock");
    }
}
