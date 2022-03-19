package org.noear.grit.server.ui.controller;

import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.Valid;

/**
 * @author noear
 * @since 1.0
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
        viewModel.put("app", "Grit");

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");
        viewModel.put("title", "Grit");


        return viewModel.view(viewName + ".ftl");
    }
}
