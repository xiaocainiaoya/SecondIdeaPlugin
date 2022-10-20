# 所属版本（必须修改）
SET @gpx_source_version := '{sourceVersion}';
# 脚本ID（必须修改）
SET @gpx_change_id := '{changeId}';
# 脚本负责人（必须修改）
SET @gpx_change_user := '{changeUser}';
# 所属数据库（必须修改）
SET @gpx_database_name := '{database}';

-- 删除存储过程，临时的
DROP PROCEDURE IF EXISTS gpx_database_procedure;
delimiter //
CREATE PROCEDURE gpx_database_procedure()
BEGIN
{sqlBody}
END;
//
delimiter ;
CALL gpx_database_procedure();
DROP PROCEDURE gpx_database_procedure;
