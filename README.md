# DBSPLIT

Dbsplit扩展了Spring的JdbcTemplate, 在JdbcTemplate上增加了分库分表，读写分离和失效转移等功能，并与Spring JDBC保持相同的风格，简单实用，避免外部依赖，不需要类似cobar的代理服务器，堪称可伸缩的Spring JdbcTemplate。

一方面，它对于单库单表扩展了JdbcTemplate模板, 使其成为一个简单的ORM框架，可以直接对领域对象模型进行持久和搜索操作，并且实现了读写分离。

另一方面，对于分库分表它与JdbcTemplate保持同样的风格，不但提供了一个简单的ORM框架，可以直接对领域对象模型进行持久和搜索操作，还是先了数据分片和读写分离等高级功能。

另外，扩展的Dbsplit保持与原有JdbcTemplate完全兼容，对于特殊需求，完全可以回溯到原有JdbcTemplate提供的功能，即使用JDBC的方式来解决，这里面体现了通用和专用原则，通用原则解决80%的事情，而专用原则解决剩余的20%的事情。

此项目也提供了一个方便的脚本，可以一次性的建立多库多表。

## TODO 

1. 三种分库分表的hash策略的支持, 水平（索引不重复），垂直（索引重复），混合（库索引不重复，表索引重复）。
2. 使用annotation来声明SimpleJdbcTemplate or SimpleSplitJdbcTemplate。
3. 对SimpleJdbcTemplate实现读写分离。
4. 对spring XML封装一个简单的FactoryBean, 使用builder模式来声明template让开发者使用起来更简单。
5. dbsplit基类的实现需要针对insert, delete, update, select单独处理sql，当前实现还不准确，使用druid库，druid库有性能问题，考虑使用cobar或者自己实现匹配功能，可以根据关键字 into, from, update, select from等等，一次匹配多个字符，例如:匹配from可以char(i) == 'f' and char(i+1) == 'r' and char(i+2) == 'o' and char(i+3) == 'm'
6. build-db-split脚本增加调试bug时候输入id，根据各种hash策略取出实例索引，库索引和表示锁，便于查找bug
7. 需要对build-db-split脚本增加建立从库，并把从库指向主库的脚本， 可参考这里面的脚本建立工具集合：https://github.com/lebronhkh/mysqlyunwei
8. 提供失效转以的方法。