package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.server.utils.JsondEntity;
import org.noear.grit.server.utils.JsondUtils;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源组管理
 *
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/resource/group")
@Controller
public class ResourceGroupController extends BaseController {
    @Inject
    ResourceAdminService resourceAdminService;

    @Mapping
    public ModelAndView home(long space_id) throws SQLException {

        List<ResourceSpace> list = resourceAdminService.getSpaceList();
        list.sort(ResourceComparator.instance);

        space_id = ResourceSpaceCookie.build(space_id, list);

        viewModel.put("space_id", space_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_group");
    }

    @Mapping("inner")
    public ModelAndView inner(long space_id, int state) throws SQLException {
        boolean disabled = (state == 1);

        List<ResourceGroup> list = resourceAdminService.getResourceGroupListBySpace(space_id);
        list = list.stream().filter(r -> r.is_disabled == disabled).collect(Collectors.toList());
        list = ResourceTreeUtils.build(list, space_id);

        ResourceSpaceCookie.set(space_id);

        viewModel.put("space_id", space_id);
        viewModel.put("state", state);
        viewModel.put("list", list);

        return view("grit/ui/resource_group_inner");
    }

    ////////////////////

    /**
     * 批量导出
     * */
    @Mapping("ajax/export")
    public void exportDo(Context ctx, long space_id, String ids) throws Exception {
        if (space_id == 0) {
            return;
        }

        List<ResourceGroup> list = resourceAdminService.getResourceGroupListBySpaceAndIds(space_id, ids);

        String jsonD = JsondUtils.encode("grit_resource_group", list);

        String filename = "grit_resource_group_" + space_id + "_" + LocalDate.now() + ".jsond";
        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        ctx.output(jsonD);
    }


    /**
     * 批量导入
     * */
    @Mapping("ajax/import")
    public Result importDo(Context ctx, long space_id, UploadedFile file) throws Exception {
        if (space_id == 0) {
            return Result.failure();
        }

        try {
            String jsonD = Utils.transferToString(file.content, "UTF-8");
            JsondEntity entity = JsondUtils.decode(jsonD);

            if (entity == null || "grit_resource_group".equals(entity.table) == false) {
                return Result.failure("数据不对！");
            }

            List<ResourceDo> list = entity.data.toObjectList(ResourceDo.class);

            for (ResourceDo m : list) {
                m.resource_sid = space_id;
                m.resource_pid = space_id;

                resourceAdminService.putResourceByGuid(m);
            }

            return Result.succeed();
        } catch (Throwable e) {
            return Result.failure(e.getLocalizedMessage());
        }
    }

    /**
     * 批量处理（删除，禁用，启用）
     * */
    @Mapping("ajax/batch")
    public Result batchDo(Context ctx, long space_id, int act, String ids) throws Exception {
        try {
            if (act == 9) {
                resourceAdminService.delResourceByIds(ids);
            } else {
                resourceAdminService.desResourceByIds(ids, (act == 1));
            }

            return Result.succeed();
        } catch (Throwable e) {
            return Result.failure(e.getLocalizedMessage());
        }
    }
}
