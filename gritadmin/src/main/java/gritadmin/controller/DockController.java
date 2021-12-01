package gritadmin.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

/**
 * @author noear
 * @since 1.0
 */
@Controller
public class DockController extends BaseController {
    @Mapping("/grit/")
    public ModelAndView dock() {
        return view("dock");
    }
}
