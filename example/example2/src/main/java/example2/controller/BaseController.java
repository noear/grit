package example2.controller;


import example2.model.ViewModel;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;


/**
 * 基础控制器
 *
 * @author noear 2014-10-19
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
        viewModel.put("app", Solon.cfg().appTitle());

        viewModel.put("css", "/css");
        viewModel.put("js", "/js");
        viewModel.put("img", "/img");

        return viewModel.view(viewName + ".ftl");
    }

    /*
    * @return 输出一个跳转视图
    * @prarm  url 可以是任何URL地址
    * */
    public void redirect(String url) {
        try {
            Context.current().redirect(url);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
