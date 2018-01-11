# DBSPLIT

Dbsplit扩展了Spring的JdbcTemplate, 在JdbcTemplate上增加了分库分表，读写分离和失效转移等功能，并与Spring JDBC保持相同的风格，简单实用，避免外部依赖，不需要类似cobar的代理服务器，堪称可伸缩的Spring JdbcTemplate。

一方面，它对于单库单表扩展了JdbcTemplate模板, 使其成为一个简单的ORM框架，可以直接对领域对象模型进行持久和搜索操作，并且实现了读写分离。

另一方面，对于分库分表它与JdbcTemplate保持同样的风格，不但提供了一个简单的ORM框架，可以直接对领域对象模型进行持久和搜索操作，还是先了数据分片和读写分离等高级功能。

另外，扩展的Dbsplit保持与原有JdbcTemplate完全兼容，对于特殊需求，完全可以回溯到原有JdbcTemplate提供的功能，即使用JDBC的方式来解决，这里面体现了通用和专用原则，通用原则解决80%的事情，而专用原则解决剩余的20%的事情。

此项目也提供了一个方便的脚本，可以一次性的建立多库多表。


