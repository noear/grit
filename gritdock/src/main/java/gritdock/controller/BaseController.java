package gritdock.controller;


import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.ModelAndView;
import gritdock.Config;
import gritdock.dso.Session;
import gritdock.util.Datetime;

/**
 * Grit dock 控制器基类
 *
 * @author noear
 * @since 1.0
 */
@Singleton(false)
public abstract class BaseController {

    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    /*
     * @return 输出一个视图（自动放置viewModel）
     * @param viewName 视图名字(内部uri)
     * */
    public ModelAndView view(String viewName) {
        //设置必要参数
        viewModel.put("root", "");

        viewModel.put("title", Config.title());
        viewModel.put("app", Config.title());
        viewModel.put("env", Config.env());

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");


        viewModel.put("timenow", Datetime.Now().toString("(yyyy-MM-dd HH:mm Z)"));


        //当前用户信息(示例)
        viewModel.put("userId", Session.current().getSubjectId());
        viewModel.put("userDisplayName", Session.current().getDisplayName());


        return new ModelAndView("/" + viewName + ".ftl", viewModel);
    }
}
