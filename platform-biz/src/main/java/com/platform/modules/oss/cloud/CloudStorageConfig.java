/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.oss.cloud;


import com.platform.common.validator.group.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 云存储配置信息
 *
 * @author cxd
 * @since 2019-01-17 16:21:01
 */
@Data
public class CloudStorageConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类型 1：七牛  2：阿里云  3：腾讯云  4：服务器存储 5: MINIO
     */
    @Range(min = 1, max = 5, message = "类型错误")
    private Integer type;

    /**
     * 七牛绑定的域名
     */
    @NotBlank(message = "七牛绑定的域名不能为空", groups = QiniuGroup.class)
    @URL(message = "七牛绑定的域名格式不正确", groups = QiniuGroup.class)
    private String qiniuDomain;
    /**
     * 七牛路径前缀
     */
    private String qiniuPrefix;
    /**
     * 七牛ACCESS_KEY
     */
    @NotBlank(message = "七牛AccessKey不能为空", groups = QiniuGroup.class)
    private String qiniuAccessKey;
    /**
     * 七牛SECRET_KEY
     */
    @NotBlank(message = "七牛SecretKey不能为空", groups = QiniuGroup.class)
    private String qiniuSecretKey;
    /**
     * 七牛存储空间名
     */
    @NotBlank(message = "七牛空间名不能为空", groups = QiniuGroup.class)
    private String qiniuBucketName;

    /**
     * 阿里云绑定的域名
     */
    @NotBlank(message = "阿里云绑定的域名不能为空", groups = AliyunGroup.class)
    @URL(message = "阿里云绑定的域名格式不正确", groups = AliyunGroup.class)
    private String aliyunDomain;
    /**
     * 阿里云路径前缀
     */
    private String aliyunPrefix;
    /**
     * 阿里云EndPoint
     */
    @NotBlank(message = "阿里云EndPoint不能为空", groups = AliyunGroup.class)
    private String aliyunEndPoint;
    /**
     * 阿里云AccessKeyId
     */
    @NotBlank(message = "阿里云AccessKeyId不能为空", groups = AliyunGroup.class)
    private String aliyunAccessKeyId;
    /**
     * 阿里云AccessKeySecret
     */
    @NotBlank(message = "阿里云AccessKeySecret不能为空", groups = AliyunGroup.class)
    private String aliyunAccessKeySecret;
    /**
     * 阿里云BucketName
     */
    @NotBlank(message = "阿里云BucketName不能为空", groups = AliyunGroup.class)
    private String aliyunBucketName;

    /**
     * 腾讯云绑定的域名
     */
    @NotBlank(message = "腾讯云绑定的域名不能为空", groups = QcloudGroup.class)
    @URL(message = "腾讯云绑定的域名格式不正确", groups = QcloudGroup.class)
    private String qcloudDomain;
    /**
     * 腾讯云路径前缀
     */
    private String qcloudPrefix;
    /**
     * 腾讯云AppId
     */
    @NotNull(message = "腾讯云AppId不能为空", groups = QcloudGroup.class)
    private Integer qcloudAppId;
    /**
     * 腾讯云SecretId
     */
    @NotBlank(message = "腾讯云SecretId不能为空", groups = QcloudGroup.class)
    private String qcloudSecretId;
    /**
     * 腾讯云SecretKey
     */
    @NotBlank(message = "腾讯云SecretKey不能为空", groups = QcloudGroup.class)
    private String qcloudSecretKey;
    /**
     * 腾讯云BucketName
     */
    @NotBlank(message = "腾讯云BucketName不能为空", groups = QcloudGroup.class)
    private String qcloudBucketName;
    /**
     * 腾讯云COS所属地区
     */
    @NotBlank(message = "所属地区不能为空", groups = QcloudGroup.class)
    private String qcloudRegion;
    /**
     * 服务器存储
     */
    @NotBlank(message = "本地存储路径不能为空", groups = DiskGroup.class)
    private String diskPath;
    @URL(message = "代理服务器需要是一个合法的URL")
    @NotBlank(message = "本地存储代理服务器不能为空", groups = DiskGroup.class)
    private String proxyServer;

    /**
     * MinIO存储
     */
    @NotBlank(message = "AccessKey不能为空", groups = MinioGroup.class)
    private String minioAccessKey;
    @NotBlank(message = "SecretKey不能为空", groups = MinioGroup.class)
    private String minioSecretKey;
    @NotBlank(message = "Url不能为空", groups = MinioGroup.class)
    private String minioUrl;
    @NotBlank(message = "BucketName不能为空", groups = MinioGroup.class)
    private String minioBucketName;
}
