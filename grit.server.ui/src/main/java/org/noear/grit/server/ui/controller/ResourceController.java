package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.utils.ResourceComparator;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.ArrayList;
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
    public Object inner(long space_id, String key, int state) throws SQLException {

        List<Resource> list = GritClient.global().resourceSpace().getAdminResourceListBySpace(space_id);
        List<Resource> list2 = new ArrayList<>(list.size());

        list.stream().filter(r -> r.resource_pid == space_id)
                .sorted(new ResourceComparator())
                .forEachOrdered(r -> {
                    list2.add(r);
                    list.stream().filter(r2 -> r2.resource_pid == r.resource_id)
                            .sorted(new ResourceComparator())
                            .forEachOrdered(r2 -> list2.add(r2));
                });


        viewModel.put("key", key);
        viewModel.put("state", state);
        viewModel.put("space_id", space_id);
        viewModel.put("list", list2);

        return view("grit/ui/resource_inner");
    }
}
