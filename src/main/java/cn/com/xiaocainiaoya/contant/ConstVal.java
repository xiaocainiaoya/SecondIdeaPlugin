/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cn.com.xiaocainiaoya.contant;

import java.nio.charset.Charset;

/**
 * 定义常量
 *
 * @author YangHu, tangguo
 * @since 2016/8/31
 */
public class ConstVal {

    public static final String MODULENAME = "ModuleName";

    public static final String ENTITY = "Entity";
    public static final String SERIVCE = "Service";
    public static final String SERVICEIMPL = "ServiceImpl";
    public static final String MAPPER = "Mapper";
    public static final String XML = "Xml";
    public static final String CONTROLLER = "Controller";

    public static final String ENTITY_PATH = "entity_path";
    public static final String SERIVCE_PATH = "serivce_path";
    public static final String SERVICEIMPL_PATH = "serviceimpl_path";
    public static final String MAPPER_PATH = "mapper_path";
    public static final String XML_PATH = "xml_path";
    public static final String CONTROLLER_PATH = "controller_path";

    public static final String JAVA_TMPDIR = "java.io.tmpdir";
    public static final String UTF8 = Charset.forName("UTF-8").name();
    public static final String UNDERLINE = "_";

    public static final String JAVA_SUFFIX = ".java";
    public static final String KT_SUFFIX = ".kt";
    public static final String XML_SUFFIX = ".xml";

    /** %s是模板组占位符 */
    public static final String TEMPLATE_ENTITY_JAVA = "/template/%s/entity.java.vm";
    public static final String TEMPLATE_ENTITY_KT = "/template/%s/entity.kt.vm";
    public static final String TEMPLATE_MAPPER = "/template/%s/mapper.java.vm";
    public static final String TEMPLATE_XML = "/template/%s/mapper.xml.vm";
    public static final String TEMPLATE_SERVICE = "/template/%s/service.java.vm";
    public static final String TEMPLATE_SERVICEIMPL = "/template/%s/serviceImpl.java.vm";
    public static final String TEMPLATE_CONTROLLER = "/template/%s/controller.java.vm";

    public static final String VM_LOADPATH_KEY = "file.resource.loader.class";
    public static final String VM_LOADPATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    public static final String SUPERD_MAPPER_CLASS = "com.baomidou.mybatisplus.mapper.BaseMapper";
    public static final String SUPERD_SERVICE_CLASS = "com.baomidou.mybatisplus.service.IService";
    public static final String SUPERD_SERVICEIMPL_CLASS = "com.baomidou.mybatisplus.service.impl.ServiceImpl";

}
