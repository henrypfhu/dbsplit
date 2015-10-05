# dbsplit

Dbsplit扩展了Spring的JdbcTemplate, 使JdbcTemplate就有分库分表的功能，简单实用。

TODO 

1. 三种分库分表的hash策略的支持。
2. 使用annotation来声明SimpleJdbcTemplate or SimpleSplitJdbcTemplate。
3. 对SimpleJdbcTemplate实现读写分离。
4. 对spring XML封装一个简单的FactoryBean, 使用builder模式来声明template让开发者使用起来更简单。