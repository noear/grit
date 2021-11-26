package org.noear.grit.client;

import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.service.*;
import org.noear.nami.Nami;
import org.noear.nami.coder.snack3.SnackDecoder;
import org.noear.nami.coder.snack3.SnackEncoder;

import java.sql.SQLException;

/**
 * 客户端的Rpc实现
 *
 * @author noear
 * @since 1.0
 */
public class GritClientRpcImpl implements GritClient {
    private SubjectService subjectService;
    private SubjectLinkService subjectLinkService;

    private ResourceService resourceService;
    private ResourceLinkService resourceLinkService;
    private ResourceSpaceService resourceSpaceService;

    private AuthService authService;

    private long currentSpaceId;
    private String currentSpaceCode;

    public GritClientRpcImpl() {
        // gritServer=> "http://gritrpc";
        String gritServer = System.getProperty("grit.server");

        if (TextUtils.isEmpty(gritServer) == false) {
            subjectService = createService(gritServer, SubjectService.class);
            subjectLinkService = createService(gritServer, SubjectLinkService.class);

            resourceService = createService(gritServer, ResourceService.class);
            resourceLinkService = createService(gritServer, ResourceLinkService.class);
            resourceSpaceService = createService(gritServer, ResourceSpaceService.class);

            authService = createService(gritServer, AuthService.class);
        } else {
            System.out.println("[Grit] Invalid configuration: grit.server");
        }
    }

    private <T> T createService(String gritServer, Class<T> clz) {
        return Nami.builder().url(gritServer + "/api/v1/" + clz.getName())
                .encoder(SnackEncoder.instance).decoder(SnackDecoder.instance)
                .create(clz);
    }


    /**
     * 设置当前资源空间代号
     */
    public void setCurrentSpaceCode(String resourceSpaceCode) {
        if (resourceSpaceCode == null || resourceSpaceCode.equals(currentSpaceCode)) {
            return;
        }

        try {
            ResourceSpace space = resourceSpace().getSpaceByCode(resourceSpaceCode);
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
     * 资源空间接口
     */
    public ResourceSpaceService resourceSpace() {
        return resourceSpaceService;
    }

    /**
     * 签权接口
     */
    public AuthService auth() {
        return authService;
    }
}
