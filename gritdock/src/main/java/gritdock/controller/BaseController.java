package gritdock.controller;


import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.ModelAndView;
import gritdock.Config;
import gritdock.dso.Session;
import gritdock.util.Datetime;

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
        viewModel.put("env", Config.evn());

        viewModel.put("css", "/css");
        viewModel.put("js", "/js");
        viewModel.put("img", "/img");


        viewModel.put("timenow", Datetime.Now().toString("(yyyy-MM-dd HH:mm Z)"));


        //当前用户信息(示例)
        viewModel.put("userId", Session.current().getUserId());
        viewModel.put("userDisplayName", Session.current().getDisplayName());


        return new ModelAndView("/" + viewName + ".ftl", viewModel);
    }
}
