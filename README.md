# DBSPLIT框架入门向导

## 什么是dbsplit？

Dbsplit扩展了Spring的JdbcTemplate, 在JdbcTemplate上增加了分库分表，读写分离和失效转移等功能，并与Spring JDBC保持相同的风格，简单实用，避免外部依赖，不需要类似cobar的代理服务器，堪称可伸缩的Spring JdbcTemplate。

一方面，它对于单库单表扩展了JdbcTemplate模板, 使其成为一个简单的ORM框架，可以直接对领域对象模型进行持久和搜索操作，并且实现了读写分离。

另一方面，对于分库分表它与JdbcTemplate保持同样的风格，不但提供了一个简单的ORM框架，可以直接对领域对象模型进行持久和搜索操作，还是先了数据分片和读写分离等高级功能。

另外，扩展的Dbsplit保持与原有JdbcTemplate完全兼容，对于特殊需求，完全可以回溯到原有JdbcTemplate提供的功能，即使用JDBC的方式来解决，这里面体现了通用和专用原则，通用原则解决80%的事情，而专用原则解决剩余的20%的事情。

此项目也提供了一个方便的脚本，可以一次性的建立多库多表。

## 谁应该关注dbsplit？ 

特别适合想知道互联网的分库分表是怎么实现的，也适合那些想把分库分表框架开箱即用的项目，更适合想学习互联网的小伙伴们。

如果你在寻找数据库分库分表的轻量级解决方案，请参考Dbsplit的实现和应用场景，它是一个兼容Spring JDBC的并且支持分库分表的轻量级的数据库中间件，使用起来简单方便，性能接近于直接使用JDBC，并且能够无缝的与Spring相结合，又具有很好的可维护性。

## 怎么使用dbsplit?

我们已经完整的实现了一个具有分库分表功能的框架dbsplit，现在，让我们提供一个示例演示在我们的应用中怎么来使用这个框架，大家也可以参考dbsplit项目中dbsplit-core/src/main/test中的源代码。

首先，假设我们应用中有个表需要增删改查，它的DDL脚本如下：

````sql
drop table if exists TEST_TABLE_$I;

create table TEST_TABLE_$I
(
    ID bigint not null,
    NAME varchar(128) not null,
    GENDER               smallint default 0, 
    LST_UPD_USER         varchar(128) default "SYSTEM",
    LST_UPD_TIME         timestamp default now(),
    primary key(id),
    unique key UK_NAME(NAME)
);
````

我们把这个DDL脚本保存到table.sql文件中，然后，我们需要准备好一个Mysql的数据库实例，实例端口为localhost:3307, 因为环境的限制，我们用着一个数据库实例来模拟两个数据库实例，两个数据库实例使用同一个端口，我们为TEST_TABLE设计了2个数据库实例、每个实例2个数据库、每个数据库4个表，共16个分片表。

我们使用脚本创建创建用于分片的多个数据库和表，脚本代码如下所示：


```
build-db-split.sh -i "localhost:3307,localhost:3307" -m test_db -n table.sql -x 2 -y 4 -a test_user -b test_password -c root -d youarebest -l localhost -t
```

这里，需要提供系统root用户的用户名和密码。

然后，我们登录Mysql的命令行客户端，我们看到一共创建了4个数据库，前2个数据库属于数据库实例1，后2个数据库属于数据库实例2，每个数据库有4个表。

```
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| test               |
| test_db_0          |
| test_db_1          |
| test_db_2          |
| test_db_3          |
+--------------------+
6 rows in set (0.01 sec)

mysql> use test_db_0;
Database changed
mysql> show tables;
+---------------------+
| Tables_in_test_db_0 |
+---------------------+
| TEST_TABLE_0        |
| TEST_TABLE_1        |
| TEST_TABLE_2        |
| TEST_TABLE_3        |
+---------------------+
4 rows in set (0.00 sec)
```

因此，一共我们创建了16个分片表。

然后，我们定义对应这个数据库表的领域对象模型，在这个领域对象模型中，我们不需要任何注解，这是一个绿色的POJO。

```java
public class TestTable {
	private long id;
	private String name;

	public enum Gender {
		MALE, FEMALE;

		public static Gender parse(int value) {
			for (Gender gender : Gender.values()) {
				if (value == gender.ordinal())
					return gender;
			}
			return null;
		}
	};

	private Gender gender;
	private String lstUpdUser;
	private Date lstUpdTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getLstUpdUser() {
		return lstUpdUser;
	}

	public void setLstUpdUser(String lstUpdUser) {
		this.lstUpdUser = lstUpdUser;
	}

	public Date getLstUpdTime() {
		return lstUpdTime;
	}

	public void setLstUpdTime(Date lstUpdTime) {
		this.lstUpdTime = lstUpdTime;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
```

因为我们的应用程序需要保存这个实体，这就需要生成唯一的ID，发号器的设计和使用请参考第4章如何设计一款永不重复的高性能分布式发号器，这里我们需要配置一个发号器服务即可，代码如下所示。

```java
	<bean id="idService" class="com.robert.vesta.service.factory.IdServiceFactoryBean"
		init-method="init">
		<property name="providerType" value="PROPERTY" />
		
		<property name="machineId" value="${vesta.machine}" />
	</bean>
```

接下来，我们在Spring环境中定义这个表的分片信息，这包括数据库名称、表名称、数据库分片数、表的分片数，以及读写分离等信息，本例中我们制定数据库前缀为test_db，数据库表名为TEST_TABLE，每个实例2个数据库，每个数据库4张表，分片采用采用水平下标策略，并且打开读写分离。

```java
	<bean name="splitTable" class="com.robert.dbsplit.core.SplitTable"
		init-method="init">

		<property name="dbNamePrefix" value="test_db" />
		<property name="tableNamePrefix" value="TEST_TABLE" />

		<property name="dbNum" value="2" />
		<property name="tableNum" value="4" />

		<property name="splitStrategyType" value="HORIZONTAL" />
		<property name="splitNodes">
			<list>
				<ref bean="splitNode1" />
				<ref bean="splitNode2" />
			</list>
		</property>

		<property name="readWriteSeparate" value="true" />

	</bean>
```

我们看到，这个splitTable引用了两个数据库实例节点：splitNode1和splitNode2，他们的声明如下：

```xml
	<bean name="splitNode1" class="com.robert.dbsplit.core.SplitNode">
		<property name="masterTemplate" ref="masterTemplate0" />
		<property name="slaveTemplates">
			<list>
				<ref bean="slaveTemplate00"></ref>
			</list>
		</property>
	</bean>

	<bean name="splitNode2" class="com.robert.dbsplit.core.SplitNode">
		<property name="masterTemplate" ref="masterTemplate1" />
		<property name="slaveTemplates">
			<list>
				<ref bean="slaveTemplate10"></ref>
			</list>
		</property>
	</bean>
```

每个数据库实例节点都引用了一个数据库主模板以及若干个数据库从模板，这是用来实现读写分离的，因为我们打开了读写分离设置，所有的读操作将由dbsplit路由到数据库的从模板上，数据库的主从模板的声明引用到我们生命的数据库，因为我们是在本地做测试，这些数据源都指向了本地的Mysql数据库localhost:3307。


```xml
	<bean id="masterTemplate0" class="org.springframework.jdbc.core.JdbcTemplate"
		abstract="false" lazy-init="false" autowire="default"
		dependency-check="default">
		<property name="dataSource">
			<ref bean="masterDatasource0" />
		</property>
	</bean>

	<bean id="slaveTemplate00" class="org.springframework.jdbc.core.JdbcTemplate"
		abstract="false" lazy-init="false" autowire="default"
		dependency-check="default">
		<property name="dataSource">
			<ref bean="slaveDatasource00" />
		</property>
	</bean>
```

到现在为止，我们定义好了表的分片信息，把我们把这个表加入到SplitTablesHolder的Bean中，代码如下所示：

```xml
	<bean name="splitTablesHolder" class="com.robert.dbsplit.core.SplitTablesHolder"
		init-method="init">
		<property name="splitTables">
			<list>
				<ref bean="splitTable" />
			</list>
		</property>
	</bean>
```

接下来，我们就需要声明我们的SimpleSplitJdbcTemplate的Bean，它需要引用SplitTablesHolder的Bean，以及配置读写分离的策略，配置代码如下所示，

```xml
	<bean name="simpleSplitJdbcTemplate" class="com.robert.dbsplit.core.SimpleSplitJdbcTemplate">
		<property name="splitTablesHolder" ref="splitTablesHolder" />
		<property name="readWriteSeparate" value="${dbsplit.readWriteSeparate}" />
	</bean>
```

我们有了SimpleSplitJdbcTemplate的Bean，我们就可以把它导出给我们的服务层来使用了。这里我们通过一个测试用例来演示，在测试用例中初始化刚才我们配置的Spring环境，从Spring环境中获取SimpleSplitJdbcTemplate的Bean simpleSplitJdbcTemplate，然后，示例里面的方法插入TEST_TABLE的记录，然后，再把这条记录查询出来，代码如下所示。

```java
	public void testSimpleSplitJdbcTemplate() {
		SimpleSplitJdbcTemplate simpleSplitJdbcTemplate = (SimpleSplitJdbcTemplate) applicationContext
				.getBean("simpleSplitJdbcTemplate");
		IdService idService = (IdService) applicationContext
				.getBean("idService");

		// Make sure the id generated is not align multiple of 1000
		Random random = new Random(new Date().getTime());
		for (int i = 0; i < random.nextInt(16); i++)
			idService.genId();

		long id = idService.genId();
		System.out.println("id:" + id);

		TestTable testTable = new TestTable();
		testTable.setId(id);
		testTable.setName("Alice-" + id);
		testTable.setGender(Gender.MALE);
		testTable.setLstUpdTime(new Date());
		testTable.setLstUpdUser("SYSTEM");

		simpleSplitJdbcTemplate.insert(id, testTable);

		TestTable q = new TestTable();

		TestTable testTable1 = simpleSplitJdbcTemplate.get(id, id,
				TestTable.class);

		AssertJUnit.assertEquals(testTable.getId(), testTable1.getId());
		AssertJUnit.assertEquals(testTable.getName(), testTable1.getName());
		AssertJUnit.assertEquals(testTable.getGender(), testTable1.getGender());
		AssertJUnit.assertEquals(testTable.getLstUpdUser(),
				testTable1.getLstUpdUser());
		// mysql store second as least time unit but java stores miliseconds, so
		// round up the millisends from java time
		AssertJUnit.assertEquals(
				(testTable.getLstUpdTime().getTime() + 500) / 1000 * 1000,
				testTable1.getLstUpdTime().getTime());

		System.out.println("testTable1:" + testTable1);
	}
```

## 如何使用用于创建分库分表的脚本？

这里介绍一个用于创建分库分表的脚本，这个脚本可以一次性的按照规则在多个mysql示例上创建多个数据库和表，以及在每一个数据库实例上创建一个统一的用户，并分配相应的权限给此用户。

### 1. 使用方法

```
Usage: $0 -i [INSTANCE_STR] -m [DB_PREFIX] -n [TABLE_SQL_FILE] -x [DB_SPLIT_NUM] -y [TABLE_SPLIT_NUM] -a [USER] -b [PASSWORD] -c [ROOT_USER] -d [ROOT_PASSWORD] -l [CONNECTION_HOST] -t 

Descriptions:
-i : instances string.
-m : db name.
-n : table file name.
-x : db number.
-y : table number.
-a : user name to be created.
-b : password for the user name to be created.
-c : root user.
-d : password for root user.
-l : for the connection host.
-t : debug sql output.
```

### 2. 使用示例

> Example1: $0 -i "localhost:3306,localhost:3306" -m test_db -n table.sql -x 2 -y 2 -a test_user -b test_password -c root -d youarebest -l localhost -t
Example2: $0 -i "localhost:3306,localhost:3306" -m test_db -n table.sql -x 2 -y 2 -a test_user -b test_password -c root -d youarebest -l localhost

### 3. 源码

```
#!/bin/bash

insts=localhost:3306,localhost:3306

db_prefix=test_db
table_sql_file=table.sql

db_num=2
table_num=2

user_name=test_user
password=test_password

root_user_name=root
root_password=cool

debug=FALSE

conn_host=localhost

build_db() {
  inst=$1
  inst_arr=(${inst//:/ })

  host=${inst_arr[0]}
  port=${inst_arr[1]}

  db=$2
  db_no=$3
  
  echo "info: building instance $inst db $db db no $db_no"

  for ((k=0;k<$table_num;k++)); do
    ((table_no=$table_num*$db_no+$k))

    echo "info: building instance $inst db $db db no $db_no table $table_no"    
    
    sql_command="sed 's/"'$index'"/$table_no/g' ./$table_sql_file | tr -t '\n' '\0'"
    sql_create_table=`eval "$sql_command"`
    
    if [[ $debug = 'TRUE' ]]; then
        echo "Create Table SQL: $sql_create_table"
    fi
    mysql -u$root_user_name -p$root_password -e "$sql_create_table" $db 2> /dev/null
     
  done  
}

build_inst() {
  inst=$1
  inst_arr=(${inst//:/ })

  host=${inst_arr[0]}
  port=${inst_arr[1]}

  inst_no=$2
  
  echo "info: building instance $inst no $inst_no"
  
  sql_delete_user="delete from mysql.user where user = '$user_name'; flush privileges"
  
  if [[ $debug = 'TRUE' ]]; then
    echo "Delete User SQL: $sql_delete_user"
  fi
  mysql -u$root_user_name -p$root_password -e "$sql_delete_user" 2> /dev/null
  
  mysql -u$root_user_name -p$root_password -e "create user '$user_name'@'$conn_host' identified by '$password'"
    
  for ((j=0;j<$db_num;j++)); do
    ((db_no=$db_num*$inst_no+$j)) 
    
    create_database_sql="drop database if exists ${db_prefix}_${db_no};create database ${db_prefix}_${db_no}"
    
    if [[ $debug = 'TRUE' ]]; then
      echo "Create Database SQL: $create_database_sql"
    fi    
    mysql -u$root_user_name -p$root_password -e "$create_database_sql" 2> /dev/null

    assign_rights_sql="grant all privileges on ${db_prefix}_${db_no}.* to '$user_name'@'$conn_host' identified by '$password';flush privileges"
    
    if [[ $debug = 'TRUE' ]]; then
      echo "Assign Rights SQL: $assign_rights_sql"
    fi    
    mysql -u$root_user_name -p$root_password -e "assign_rights_sql" 2> /dev/null    

    build_db $inst ${db_prefix}_${db_no} $db_no
  done   
}

main() {
    echo "properties: insts=$insts db_prefix=$db_prefix table_sql_file=$table_sql_file db_num=$db_num table_num=$table_num user_name=$user_name password=$password root_user_name=$root_user_name root_password=$root_password"

    insts_arr=(${insts//,/ })  
    insts_num=${#insts_arr[@]} 
    
    for ((i=0;i<$insts_num;i++)); do
      build_inst ${insts_arr[$i]} $i
    done
}

PrintUsage()
{
cat << EndOfUsageMessage

    Usage: $0 -i [INSTANCE_STR] -m [DB_PREFIX] -n [TABLE_SQL_FILE] -x [DB_SPLIT_NUM] -y [TABLE_SPLIT_NUM] -a [USER] -b [PASSWORD] -c [ROOT_USER] -d [ROOT_PASSWORD] -l [CONNECTION_HOST] -t 
    
    Descriptions:
    -i : instances string.
    -m : db name.
    -n : table file name.
    -x : db number.
    -y : table number.
    -a : user name to be created.
    -b : password for the user name to be created.
    -c : root user.
    -d : password for root user.
    -l : for the connection host.
    -t : debug sql output.
    
    Example1: $0 -i "localhost:3306,localhost:3306" -m test_db -n table.sql -x 2 -y 2 -a test_user -b test_password -c root -d youarebest -l localhost -t
    Example2: $0 -i "localhost:3306,localhost:3306" -m test_db -n table.sql -x 2 -y 2 -a test_user -b test_password -c root -d youarebest -l localhost
    
EndOfUsageMessage
}

InvalidCommandSyntaxExit()
{
        echo "Invalid command\n`PrintUsage`"
        exit;
}

if [ $# -eq 0 ]
then
    echo "`PrintUsage`"
    exit 1
fi


while getopts "i:m:n:x:y:a:b:c:d:l:t" arg
do
        case $arg in
             i)
                insts=$OPTARG 
                ;;
             m)
                db_prefix=$OPTARG
                ;;
             n)
                table_sql_file=$OPTARG
                ;;
             x)
                db_num=$OPTARG
                ;;
             y)
                table_num=$OPTARG
                ;;
             a)
                user_name=$OPTARG
                ;;
             b)
                password=$OPTARG
                ;;
             c)
                root_user_name=$OPTARG
                ;;
             d)
                root_password=$OPTARG
                ;;
             l)
                conn_host=$OPTARG
                ;;
             t)
                debug=TRUE
                ;;
             ?) 
                echo "`InvalidCommandSyntaxExit`"
                exit 1
                ;;
        esac
done
```

这个脚本仅仅是一个示例，计划中，这个脚本需要支持三种分库分表的策略，数据库和表下标累积的策略，数据库和表下标归零的策略与两种混合策略, 当前脚本只支持第一种。

我们需要注意，这个建库脚本不支持建立主从关系，但是可以建立主库和从库后再手工建立主从关系。

## 联系开发者艳鹏

微信：robert_lyp


