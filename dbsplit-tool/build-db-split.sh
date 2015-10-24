#!/bin/bash

insts=localhost:3306,localhost:3306

db_prefix=test_db
table_sql_file=table.sql

db_num=2
table_num=2

user_name=test_user
password=test_password

root_user_name=root
root_password=youarebest

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
    
    sql_command="sed 's/"'$I'"/$table_no/g' $table_sql_file "
    sql_create_table=`eval "$sql_command"`
    
    if [[ $debug = 'TRUE' ]]; then
    	echo "Create Table SQL: $sql_create_table"
    fi
    mysql -h$host -P$port -u$root_user_name -p$root_password -e "$sql_create_table" $db #2> /dev/null
     
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
  mysql -h$host -P$port -u$root_user_name -p$root_password -e "$sql_delete_user" 2> /dev/null
  
  if [[ $debug = 'TRUE' ]]; then
	echo "Create User SQL: create user '$user_name'@'$conn_host' identified by '$password'"
  fi
  mysql -h$host -P$port -u$root_user_name -p$root_password -e "create user '$user_name'@'$conn_host' identified by '$password'"
    
  for ((j=0;j<$db_num;j++)); do
    ((db_no=$db_num*$inst_no+$j)) 
    
    create_database_sql="drop database if exists ${db_prefix}_${db_no};create database ${db_prefix}_${db_no}"
    
    if [[ $debug = 'TRUE' ]]; then
	  echo "Create Database SQL: $create_database_sql"
    fi    
    mysql -h$host -P$port -u$root_user_name -p$root_password -e "$create_database_sql" 2> /dev/null

	assign_rights_sql="grant all privileges on ${db_prefix}_${db_no}.* to '$user_name'@'$conn_host' identified by '$password';flush privileges"
    
    if [[ $debug = 'TRUE' ]]; then
	  echo "Assign Rights SQL: $assign_rights_sql"
    fi    
    mysql -h$host -P$port -u$root_user_name -p$root_password -e "$assign_rights_sql" 2> /dev/null    

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

main




