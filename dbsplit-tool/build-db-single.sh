#!/bin/bash

inst=localhost:3306

db=test_db
table_sql_file=table.sql

user_name=test_user
password=test_password

root_user_name=root
root_password=youarebest

debug=FALSE

conn_hosts=localhost

main() {
  echo "properties: inst=$inst db=$db table_sql_file=$table_sql_file user_name=$user_name password=$password root_user_name=$root_user_name root_password=$root_password conn_hosts=$conn_hosts"

  inst_arr=(${inst//:/ })  
  host=${inst_arr[0]}
  port=${inst_arr[1]}
  
  create_database_sql="drop database if exists $db; create database $db;"

  if [[ $debug = 'TRUE' ]]; then
    echo "1. Delete and create Database SQL: $create_database_sql"
  fi    
  mysql -h $host -P $port -u$root_user_name -p$root_password -e "$create_database_sql" 2> /dev/null

  if [[ $debug = 'TRUE' ]]; then
    echo -e "2. Create Table SQL: \n`cat $table_sql_file`"
  fi
  mysql -h $host -P $port -u$root_user_name -p$root_password -e "source $table_sql_file" $db 2> /dev/null


  sql_delete_user="delete from mysql.user where user = '$user_name'; commit; flush privileges;"
  
  if [[ $debug = 'TRUE' ]]; then
	echo "3. Delete User SQL: $sql_delete_user"
  fi
  mysql -h $host -P $port -u$root_user_name -p$root_password -e "$sql_delete_user" 2> /dev/null

  conn_hosts_arr=(${conn_hosts//,/ })  
  conn_hosts_num=${#conn_hosts_arr[@]} 

  for ((j=0;j<$conn_hosts_num;j++)); do
    if [[ $debug = 'TRUE' ]]; then
      echo "4.$j. Create user for host: ${conn_hosts_arr[j]} by SQL: create user '$user_name'@'${conn_hosts_arr[j]}' identified by '$password';"
    fi
    

    mysql -h $host -P $port -u$root_user_name -p$root_password -e "create user '$user_name'@'${conn_hosts_arr[j]}' identified by '$password';"  2> /dev/null
 
    assign_rights_sql="grant all privileges on ${db}.* to '$user_name'@'${conn_hosts_arr[j]}' identified by '$password'; commit; flush privileges;"
    
    if [[ $debug = 'TRUE' ]]; then
      echo "5.$j. Assign Rights SQL: $assign_rights_sql"
    fi
    mysql -h $host -P $port -u$root_user_name -p$root_password -e "$assign_rights_sql" 2> /dev/null    
  done   
}

PrintUsage()
{
cat << EndOfUsageMessage

	Usage: $0 -i [INSTANCE_STR] -m [DB_NAME] -n [TABLE_SQL_FILE] -a [USER] -b [PASSWORD] -c [ROOT_USER] -d [ROOT_PASSWORD] -l [CONNECTION_HOSTS] -t
	
	Descriptions:
	-i : instances string.
	-m : db name.
	-n : table file name.
	-a : user name to be created.
	-b : password for the user name to be created.
	-c : root user.
	-d : password for root user.
	-l : for the connection hosts.
	-t : debug sql output.
	
	Example1: $0 -i "localhost:3306" -m test_db -n table.sql -a test_user -b test_password -c root -d youarebest -l 192.168.0.1,192.168.0.2 -t
	Example2: $0 -i "localhost:3306" -m test_db -n table.sql -a test_user -b test_password -c root -d youarebest -l 192.168.0.1,192.168.0.2
	
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


while getopts "i:m:n:a:b:c:d:l:t" arg
do
        case $arg in
             i)
                inst=$OPTARG 
                ;;
             m)
             	db=$OPTARG
                ;;
             n)
                table_sql_file=$OPTARG
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
                conn_hosts=$OPTARG
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




