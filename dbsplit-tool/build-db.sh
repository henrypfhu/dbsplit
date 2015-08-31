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

build_db() {
  inst=$1
  inst_arr=(${inst//:/ })

  host=${inst_arr[0]}
  port=${inst_arr[1]}

  db=$2
  db_no=$3

  for ((k=0;k<$table_num;k++)); do
    ((table_no=$table_num*$db_no+$k))
    
    #reg="s/\$index/$table_no/g"
    #echo $reg
    
    sql_command="sed 's/"'$index'"/$table_no/g' ./$table_sql_file | tr -t '\n' '\0'"
    sql=`eval "$sql_command"`
    
    mysql -u$root_user_name -p$root_password -e "$sql" $db > /dev/null
     
  done  
}

build_inst() {
  inst=$1
  inst_arr=(${inst//:/ })

  host=${inst_arr[0]}
  port=${inst_arr[1]}

  inst_no=$2

  
  mysql -u$root_user_name -p$root_password -e "delete from mysql.user where user = '$user_name'; flush privileges"
  #mysql -u$root_user_name -p$root_password -e "create user '$user_name'@'localhost' identified by '$password'"
    
  for ((j=0;j<$db_num;j++)); do
    ((db_no=$db_num*$inst_no+$j)) 
    
    mysql -u$root_user_name -p$root_password -e "drop database if exists ${db_prefix}_${db_no}"
    mysql -u$root_user_name -p$root_password -e "create database ${db_prefix}_${db_no}"

    mysql -u$root_user_name -p$root_password -e "grant all privileges on ${db_prefix}_${db_no}.* to '$user_name'@'localhost' identified by '$password'"
    mysql -u$root_user_name -p$root_password -e "flush privileges"    

    build_db $inst ${db_prefix}_${db_no} $db_no
  done   
}

main() {
	insts_arr=(${insts//,/ })  
	insts_num=${#insts_arr[@]} 
	
	for ((i=0;i<$insts_num;i++)); do
	  build_inst ${insts_arr[$i]} $i
	done
}

PrintUsage()
{
cat << EndOfUsageMessage

	Usage: $0 -i [INSTANCE_STR] -m [DB_PREFIX] -n [TABLE_SQL_FILE] -x [DB_SPLIT_NUM] -y [TABLE_SPLIT_NUM] -a [USER] -b [PASSWORD] -c [ROOT_USER] -d [ROOT_PASSWORD]
	
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
	
	Example: $0 -i "localhost:3306,localhost:3306" -m test_db -n table.sql -x 2 -y 2 -a test_user -b test_password -c root -d youarebest
	
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


while getopts "i:m:n:x:y:a:b:c:d:" arg
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
             ?) 
            	echo "`InvalidCommandSyntaxExit`"
        		exit 1
        		;;
        esac
done

main




