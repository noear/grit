package org.noear.grit.server.dso;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.service.*;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import java.sql.SQLException;

/**
 * @author noear
 * @since 1.0
 */
@Component
public class GritClientLocalImpl implements GritClient {
    @Inject
    private ResourceService resourceService;
    @Inject
    private ResourceLinkService resourceLinkService;
    @Inject
    private ResourceAdminService resourceAdminService;

    @Inject
    private SubjectService subjectService;
    @Inject
    private SubjectLinkService subjectLinkService;
    @Inject
    private SubjectAdminService subjectAdminService;

    @Inject
    private AuthService authService;


    private long currentSpaceId;
    private String currentSpaceCode;


    /**
     * 设置当前资源空间代号
     */
    public void setCurrentSpaceCode(String resourceSpaceCode) {
        if (resourceSpaceCode == null || resourceSpaceCode.equals(currentSpaceCode)) {
            return;
        }

        try {
            ResourceSpace space = resource().getSpaceByCode(resourceSpaceCode);
            currentSpaceId = space.resource_id;
            currentSpaceCode = resourceSpaceCode;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前资源空间代号
     */
    public String getCurrentSpaceCode() {
        return currentSpaceCode;
    }

    /**
     * 获取当前资源空间Id
     */
    public long getCurrentSpaceId() {
        return currentSpaceId;
    }


    /////////////////////////////////////////////


    /**
     * 资源接口
     */
    public ResourceService resource() {
        return resourceService;
    }

    /**
     * 资源关联接口
     */
    public ResourceLinkService resourceLink() {
        return resourceLinkService;
    }


    /**
     * 资源管理接口
     * */
    @Override
    public ResourceAdminService resourceAdmin() {
        return resourceAdminService;
    }


    /////////////////////////////////////////////

    /**
     * 主体接口
     */
    public SubjectService subject() {
        return subjectService;
    }

    /**
     * 主体关联接口
     */
    public SubjectLinkService subjectLink() {
        return subjectLinkService;
    }

    /**
     * 主体管理接口
     * */
    @Override
    public SubjectAdminService subjectAdmin() {
        return subjectAdminService;
    }


    /////////////////////////////////////////////


    /**
     * 签权接口
     */
    public AuthService auth() {
        return authService;
    }
}
