package cn.com.xiaocainiaoya.core.dbTool.common.generator;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 15:16
 */
public interface GeneratorHandler {

    /**
     * 插入单条数据
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String insertData(DataConfig config);

    /**
     * 插入多条数据
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String insertBatchData(DataConfig config);

    /**
     * 插入字段
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    //String insertField(DataConfig config);

    /**
     * 批量插入字段，可能不是同一个表
     *
     * @author jiangjiamin
     * @date 2022/7/22 16:51:55
     * @param config
     * @return: java.lang.String
     */
    String insertFieldBatch(DataConfig config);

    /**
     * 创建表
     *
     * @author jiangjiamin
     * @date 2023/3/2 20:47:20
     * @param config
     * @return: java.lang.String
     */
    String createTable(DataConfig config);

    /**
     * 删除字段
     *
     * @author jiangjiamin
     * @date 2023/3/28 15:02:18
     * @param config
     * @return: java.lang.String
     */
    String deleteField(DataConfig config);

    /**
     * 修改字段长度、类型等
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String updateFieldLength(DataConfig config);

    /**
     * 添加索引
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String insertIndex(DataConfig config);

    /**
     * 修改索引
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String updateIndex(DataConfig config);

    /**
     * 多行SQL
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String multilineSql(DataConfig config);

    /**
     * 空文件处理
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    String other(DataConfig config);
}
