# dbsplit

Dbsplit扩展了Spring的JdbcTemplate, 使JdbcTemplate就有分库分表的功能，简单实用。

TODO 

1. 三种分库分表的hash策略的支持, 水平（索引不重复），垂直（索引重复），混合（库索引不重复，表索引重复）。
2. 使用annotation来声明SimpleJdbcTemplate or SimpleSplitJdbcTemplate。
3. 对SimpleJdbcTemplate实现读写分离。
4. 对spring XML封装一个简单的FactoryBean, 使用builder模式来声明template让开发者使用起来更简单。
5. dbsplit基类的实现需要针对insert, delete, update, select单独处理sql，当前实现还不准确，使用druid库，druid库有性能问题，考虑使用cobar或者自己实现匹配功能，可以根据关键字 into, from, update, select from等等，一次匹配多个字符，例如:匹配from可以char(i) == 'f' and char(i+1) == 'r' and char(i+2) == 'o' and char(i+3) == 'm'
6. build-db-split脚本增加调试bug时候输入id，根据各种hash策略取出实例索引，库索引和表示锁，便于查找bug
7. 需要对build-db-split脚本增加建立从库，并把从库指向主库的脚本， 可参考这里面的脚本建立工具集合：https://github.com/lebronhkh/mysqlyunwei