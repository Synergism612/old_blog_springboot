package com.project.blog.service.impl;

import com.project.blog.entity.Type;
import com.project.blog.mapper.TypeMapper;
import com.project.blog.service.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-28
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

}
