package gritadmin.controller;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.Valid;

/**
 * Created by noear on 14-9-11.
 */
@Valid
@Singleton(false)
public class BaseController {
    /*视图数据模型*/
    protected ModelAndView viewModel = new ModelAndView();

    /*
     * @return 输出一个视图（自动放置viewModel）
     * @param viewName 视图名字(内部uri)
     * */
    public ModelAndView view(String viewName) {
        //设置必要参数
        viewModel.put("app", Solon.cfg().appTitle());

        viewModel.put("css", "/css");
        viewModel.put("js", "/js");
        viewModel.put("img", "/img");
        viewModel.put("title", Solon.cfg().appTitle());


        return viewModel.view(viewName + ".ftl");
    }
}
