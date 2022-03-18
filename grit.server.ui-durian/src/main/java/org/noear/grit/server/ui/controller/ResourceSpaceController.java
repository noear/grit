package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.server.utils.JsondUtils;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * 资源空间
 *
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/resource/space")
@Controller
public class ResourceSpaceController extends BaseController{
    @Inject
    ResourceAdminService resourceAdminService;
    
    @Mapping
    public ModelAndView home() throws SQLException {
        List<ResourceSpace> list = resourceAdminService.getSpaceList();
        list.sort(ResourceComparator.instance);

        viewModel.put("list", list);

        return view("grit/ui/resource_space");
    }

    /**
     * 批量导出
     */
    @Mapping("ajax/export")
    public void exportDo(Context ctx, long space_id) throws Exception {
        if (space_id == 0) {
            return;
        }

        ONode oNode = new ONode();

        ResourceDo space = resourceAdminService.getResourceById(space_id);
        List<ResourceGroup> groups = resourceAdminService.getResourceGroupListBySpace(space_id);

        space.resource_id = null;
        space.resource_pid = null;
        space.resource_sid = null;

        ONode oSpace = oNode.getOrNew("space");
        oSpace.getOrNew("meta").fill(space);

        ONode oGroups = oSpace.getOrNew("groups").asArray();
        for(ResourceGroup g1 : groups) {
            List<Resource> engitys = resourceAdminService.getSubResourceListByPid(g1.resource_id);

            g1.resource_id = null;
            g1.resource_pid = null;
            g1.resource_sid = null;
            ONode oG1 = oGroups.addNew();
            oG1.getOrNew("meta").fill(g1);

            ONode oEngitys = oG1.getOrNew("engitys");
            for (Resource e1 : engitys) {
                e1.resource_id = null;
                e1.resource_pid = null;
                e1.resource_sid = null;

                oEngitys.addNew().fill(e1);
            }
        }


//        String jsonD = JsondUtils.encode("grit_space", oNode);
//
//        String filename = "grit_space" + space_id + "_" + LocalDate.now() + ".jsond";
//        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        ctx.outputAsJson(oNode.toJson());
    }
}
