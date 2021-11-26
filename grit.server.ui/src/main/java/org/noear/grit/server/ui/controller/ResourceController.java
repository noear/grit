package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource/")
@Controller
public class ResourceController extends BaseController {
    @Mapping
    public Object home(Long space_id) throws SQLException {
        List<ResourceSpace> list = GritClient.global().resourceSpace().getAdminSpaceList();

        if (space_id == null) {
            if (list.size() > 0) {
                space_id = list.get(0).resource_id;
            }
        }

        viewModel.put("space_id", space_id);
        viewModel.put("list", list);

        return view("grit/ui/resource");
    }

    @Mapping("inner")
    public Object inner(Long space_id, String key, int state) throws SQLException{
        if(space_id == null){
            space_id = 0L;
        }

        List<Resource> list = GritClient.global().resource().getSubResourceListById(space_id);

        viewModel.put("key",key);
        viewModel.put("state", state);
        viewModel.put("space_id", space_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_inner");
    }
}
