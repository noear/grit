package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.server.utils.JsondEntity;
import org.noear.grit.server.utils.JsondUtils;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.handle.UploadedFile;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源实体管理
 *
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/resource/entity")
@Controller
public class ResourceEntityController extends BaseController {
    @Inject
    ResourceAdminService resourceAdminService;

    @Mapping
    public ModelAndView home(long space_id, Long group_id) throws SQLException {
        List<ResourceSpace> spaceList = resourceAdminService.getSpaceList();
        spaceList.sort(ResourceComparator.instance);
        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        List<ResourceGroup> groupList = resourceAdminService.getResourceGroupListBySpace(space_id);
        groupList = ResourceTreeUtils.build(groupList, space_id);

        if (group_id == null) {
            if (groupList.size() > 0) {
                group_id = groupList.get(0).resource_id;
            }
        }

        viewModel.put("space_id", space_id);
        viewModel.put("group_id", group_id);
        viewModel.put("spaceList", spaceList);
        viewModel.put("groupList", groupList);

        return view("grit/ui/resource_entity");
    }

    @Mapping("s")
    public ModelAndView home_s(String spaceCode, long space_id, Long group_id) throws SQLException {
        if (space_id == 0) {
            Long tmp = resourceAdminService.getResourceByCode(spaceCode).resource_id;
            if (tmp != null) {
                space_id = tmp;
            }
        }
        List<ResourceGroup> groupList;

        if (space_id > 0) {
            ResourceSpaceCookie.set(space_id);

            groupList = resourceAdminService.getResourceGroupListBySpace(space_id);
            groupList.removeIf(g -> g.is_visibled == false);

            groupList = ResourceTreeUtils.build(groupList, space_id);

            if (group_id == null) {
                if (groupList.size() > 0) {
                    group_id = groupList.get(0).resource_id;
                }
            }
        } else {
            groupList = new ArrayList<>();
        }

        viewModel.put("space_id", space_id);
        viewModel.put("group_id", group_id);
        viewModel.put("groupList", groupList);

        return view("grit/ui/resource_entity_s");
    }

    @Mapping("inner")
    public ModelAndView inner(long group_id, int state) throws SQLException {
        boolean disabled = (state == 1);

        List<Resource> list = resourceAdminService.getSubResourceListByPid(group_id);
        list = list.stream().filter(r -> r.resource_type == 0 && r.is_disabled == disabled)
                .sorted(ResourceComparator.instance)
                .collect(Collectors.toList());

        viewModel.put("group_id", group_id);
        viewModel.put("state", state);
        viewModel.put("list", list);

        return view("grit/ui/resource_entity_inner");
    }

    ////////////////////

    /**
     * 批量导出
     */
//    @Mapping("ajax/export")
    public void exportDo(Context ctx, long group_id, String ids) throws Exception {
        if (group_id == 0) {
            return;
        }

        List<Resource> list = resourceAdminService.getSubResourceListByPidAndIds(group_id, ids);

        String jsonD = JsondUtils.encode("grit_resource", list);

        String filename = "grit_resource_" + group_id + "_" + LocalDate.now() + ".jsond";
        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        ctx.output(jsonD);
    }


    /**
     * 批量导入
     */
//    @Mapping("ajax/import")
    public Result importDo(Context ctx, long group_id, UploadedFile file) throws Exception {
        if (group_id == 0) {
            return Result.failure();
        }


        try {
            String jsonD = Utils.transferToString(file.getContent(), "UTF-8");
            JsondEntity entity = JsondUtils.decode(jsonD);

            if (entity == null || "grit_resource".equals(entity.table) == false) {
                return Result.failure("数据不对！");
            }

            List<ResourceDo> list = entity.data.toObjectList(ResourceDo.class);
            Resource group = resourceAdminService.getResourceById(group_id);


            for (ResourceDo m : list) {
                m.resource_sid = group.resource_sid;
                m.resource_pid = group_id;

                resourceAdminService.synResourceByGuid(m);
            }

            return Result.succeed();
        } catch (Throwable e) {
            return Result.failure(e.getLocalizedMessage());
        }
    }

    /**
     * 批量处理（删除，禁用，启用）
     */
    @Mapping("ajax/batch")
    public Result batchDo(Context ctx, long group_id, int act, String ids) throws Exception {
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
