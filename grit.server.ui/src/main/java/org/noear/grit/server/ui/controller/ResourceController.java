package org.noear.grit.server.ui.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource/")
@Controller
public class ResourceController extends BaseController{
    @Mapping
    public Object home(){
        return view("grit/ui/resource");
    }

    @Mapping
    public Object inner(){
        return view("grit/ui/resource_inner");
    }
}
