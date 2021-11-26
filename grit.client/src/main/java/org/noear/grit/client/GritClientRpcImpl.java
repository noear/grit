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
    private ResourceService resourceService;
    private ResourceLinkService resourceLinkService;
    private ResourceAdminService resourceAdminService;

    private SubjectService subjectService;
    private SubjectLinkService subjectLinkService;
    private SubjectAdminService subjectAdminService;

    private AuthService authService;

    private long currentSpaceId;
    private String currentSpaceCode;

    public GritClientRpcImpl() {
        // gritServer=> "http://gritrpc";
        String gritServer = System.getProperty("grit.server");
        String gritToken = System.getProperty("grit.token");

        if (TextUtils.isEmpty(gritServer) == false || TextUtils.isEmpty(gritToken) == false) {
            resourceService = createService(gritServer, gritToken, ResourceService.class);
            resourceLinkService = createService(gritServer, gritToken, ResourceLinkService.class);
            resourceAdminService = createService(gritServer, gritToken, ResourceAdminService.class);

            subjectService = createService(gritServer, gritToken, SubjectService.class);
            subjectLinkService = createService(gritServer, gritToken, SubjectLinkService.class);
            subjectAdminService = createService(gritServer, gritToken, SubjectAdminService.class);


            authService = createService(gritServer, gritToken, AuthService.class);
        } else {
            if (TextUtils.isEmpty(gritServer)) {
                System.out.println("[Grit] Invalid configuration: grit.server");
            }

            if (TextUtils.isEmpty(gritToken)) {
                System.out.println("[Grit] Invalid configuration: grit.token");
            }
        }
    }

    private <T> T createService(String gritServer, String gritToken, Class<T> clz) {
        return Nami.builder().url(gritServer + "/grit/api/" + clz.getName())
                .encoder(SnackEncoder.instance)
                .decoder(SnackDecoder.instance)
                .headerSet("Grit-Token", gritToken)
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


    @Override
    public ResourceAdminService resourceAdmin() {
        return null;
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

    @Override
    public SubjectAdminService subjectAdmin() {
        return null;
    }

    /////////////////////////////////////////////

    /**
     * 签权接口
     */
    public AuthService auth() {
        return authService;
    }
}
