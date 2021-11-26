package gritadmin.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

/**
 * @author noear 2021/11/27 created
 */
@Controller
public class DockController extends BaseController{
    @Mapping("/dock")
    public ModelAndView dock() {
        return view("dock");
    }
}
