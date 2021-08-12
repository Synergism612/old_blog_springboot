package com.project.blog.service.impl;

import com.project.blog.entity.Version;
import com.project.blog.mapper.VersionMapper;
import com.project.blog.service.VersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-30
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {

}
