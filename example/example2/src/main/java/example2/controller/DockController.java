package example2.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;

import java.net.URLDecoder;

/**
 * 界面容器
 *
 * @author noear 2014-10-19
 * @since 1.0
 */
@Controller
public class DockController extends BaseController {
    //支持外部url
    @Mapping("/**/$*") //视图 返回
    public ModelAndView dock1(Context ctx) throws Exception {
        String uri = ctx.pathNew().toLowerCase();
        String query = ctx.queryString();

        Resource res = GritClient.resource().getResourceByUri(uri);
        viewModel.put("fun_name", res.display_name);
        viewModel.put("fun_url", res.remark);

        if (res.is_fullview) {
            viewModel.put("fun_type", 1);
        } else {
            viewModel.put("fun_type", 0);
        }


        return view("dock");
    }

    //此处改过，noear，201811(uadmin) //增加内部url支持
    @Mapping("/**/@*") //视图 返回
    public ModelAndView dock2(Context ctx) throws Exception {
        String uri = ctx.pathNew();
        String query = ctx.queryString();

        String fun_name = uri.split("/@")[1];
        String fun_url = uri.split("/@")[0].toLowerCase();

        if (Utils.isEmpty(query) == false) {
            fun_url = fun_url + "?" + query;
        }

        viewModel.put("fun_name", URLDecoder.decode(fun_name, "utf-8"));
        viewModel.put("fun_url", fun_url);

        if (query != null && query.indexOf("@=") >= 0) {
            viewModel.put("fun_type", 1);
        } else {
            viewModel.put("fun_type", 0);
        }


        return view("dock");
    }
}
