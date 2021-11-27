package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource/space")
@Controller
public class ResourceSpaceController extends BaseController{
    @Mapping
    public Object home(Integer state) throws SQLException {
        if(state == null){
            state = 1;
        }

        List<ResourceSpace> list = GritClient.global().resourceAdmin().getSpaceList();

        viewModel.put("state", state);
        viewModel.put("list", list);

        return view("grit/ui/resource_space");
    }
}
