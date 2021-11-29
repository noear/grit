package org.noear.grit.client;

import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.service.*;
import org.noear.nami.Nami;
import org.noear.nami.NamiBuilder;
import org.noear.nami.channel.http.okhttp.HttpChannel;
import org.noear.nami.coder.snack3.SnackDecoder;
import org.noear.nami.coder.snack3.SnackEncoder;
import org.noear.solon.core.Bridge;
import org.noear.solon.core.LoadBalance;

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

    private SubjectService subjectService;
    private SubjectLinkService subjectLinkService;

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

            subjectService = createService(gritServer, gritToken, SubjectService.class);
            subjectLinkService = createService(gritServer, gritToken, SubjectLinkService.class);

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
        //不依赖Aop的写法，跨框架性更强。在别的框架里，也不需要启动Solon容器
        NamiBuilder namiBuilder = Nami.builder();

        String path = "/grit/api/v1/" + clz.getSimpleName();
        if (gritServer.startsWith("@")) {
            String name = gritServer.substring(1);
            namiBuilder.name(name).path(path);

            if(Bridge.upstreamFactory() != null){
                LoadBalance loadBalance = Bridge.upstreamFactory().create("grit", name);
                namiBuilder.upstream(loadBalance::getServer);
            }
        } else {
            if (gritServer.contains("://") == false) {
                gritServer = "http://" + gritServer;
            }

            namiBuilder.url(gritServer + path);
        }

        return namiBuilder.encoder(SnackEncoder.instance)
                .decoder(SnackDecoder.instance)
                .channel(HttpChannel.instance)
                .headerSet("Grit-Token", gritToken)
                .create(clz);
    }


    /**
     * 设置当前资源空间
     */
    public void setCurrentSpaceByCode(String resourceSpaceCode) {
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
    @Override
    public ResourceService resource() {
        return resourceService;
    }

    /**
     * 资源关联接口
     */
    @Override
    public ResourceLinkService resourceLink() {
        return resourceLinkService;
    }


    /////////////////////////////////////////////

    /**
     * 主体接口
     */
    @Override
    public SubjectService subject() {
        return subjectService;
    }

    /**
     * 主体关联接口
     */
    @Override
    public SubjectLinkService subjectLink() {
        return subjectLinkService;
    }


    /////////////////////////////////////////////

    /**
     * 签权接口
     */
    public AuthService auth() {
        return authService;
    }
}
