package gritadmin.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;

/**
 * @author noear
 * @since 1.0
 */
@Controller
public class DockController extends BaseController {
    @Mapping("/")
    public void home(Context ctx) {
        ctx.redirect("/dock");
    }

    @Mapping("/dock")
    public ModelAndView dock() {
        return view("dock");
    }
}
