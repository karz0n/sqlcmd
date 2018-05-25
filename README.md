# SqlCmd

Implementation of [JuJa](https://juja.com.ua/) core course.

## Getting Started

To run test successfully you have to specify credentials and database url in _src/main/resources/test.properties_ file.

### Prerequisites

Installed and configured PostgreSQL database and [IntelliJ IDEA](https://www.jetbrains.com/idea/) as IDE.

# User stories

------

**STR00** As USER I can obtain list of commands

**STR00.01** ```print list of commands``` 
    I see request to enter the command  
        => enter command ```help```  
        => see list of commands

------

**STR01** As USER I want to connect to existing database and amend some data.

**STR01.01** ```connection was successful```  
    I see request to enter the command  
        => enter database name, username and password  
        => see some message, that connection was successful  
        
**STR01.02** ```wrong password or username```  
    I see request to enter the command  
        => enter database name, username and password (incorrect)  
        => see some message, that password was incorrect  
        => offered enter password once more, enter password (correct)  
        => see some message, that connection was successful  
        
**STR01.03** ```non-existent database```  
    I see request to enter the command  
        => enter database name (incorrect), username and password  
        => see some message, that database isn't exist  
        => offered enter correct database name, enter database name (correct)    
        => see some message, that connection was successful   

------
        
**STR02** As USER I want to get list of all tables  

**STR02.01** ```list of tables```  
    I see request to enter the command  
        => enter command ```tables```  
        => see list of tables in format [tableName1, tableName2, ...]  

**STR02.02** ```incorrect command```  
    I see request to enter the command  
        => enter incorrect command  
        => see error message  
        
------
        
**STR03** As USER I wand to get table content  

**STR03.01** ```table content```  
    I see request to enter the command  
        => enter command ```find <tableName>```  
        => see table content in table view  
        
        
**STR03.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```find <non_exists_table_name>```  
        => see messave, that table table isn't exist  
        => see list of existent tables  
 
------

**STR04** As USER I want to delete table  

**STR04.01** ```table deleting```  
    I see request to enter the command  
        => enter command ```drop <tableName>```  
        => see message, that command was successful
        
**STR04.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```drop <non_exists_table_name>```  
        => see message, that table isn't exist
        => see list of tables  
        
------

**STR05** As USER I want to clear table  

**STR05.01** ```table clearing```  
    I see request to enter the command  
        => enter command ```clear <tableName>```  
        => see message, that command was successful  
        
**STR05.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```clear <non_exists_table_name>```  
        => see message, that table isn't exist  
        => see list of tables  
        
------ 

**STR06** As USER I want to create table  

**STR06.01** ```table already exists```  
    I see request to enter the command  
        => enter command ```create <tableName> <column1> <column2> ...```  
        => see message, that command was successful  
        
**STR06.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```create <exists_table_name> <column1> <column2> ...```  
        => see message, that table is already exist  
        
------ 

**STR07** As USER I want to append data to table  

**STR07.01** ```appending data to table```  
    I see request to enter the command  
        => enter command ```insert <tableName> <column1> <value1> <column2> <value2> ...```  
        => see message, that command was successful  
        
**STR07.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```insert <non_exists_table_name> <column1> <value1> <column2> <value2> ...```     
        => see message, that table isn't exist  
        => see list of tables  
        
------   

**STR08** As USER I want to update data in table  

**STR08.01** ```updating data in table```  
    I see request to enter the command  
        => enter command ```update <tableName> <column1> <value1> <column2> <value2> ...```  
        => вижу сообщение об успешном выполнении команды  
        
**STR08.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```update <non_exists_table_name> <column1> <value1> <column2> <value2> ...```    
        => see message, that table isn't exist  
        => see list of tables  
        
------   

**STR09** As USER I want to delete data in table  

**STR09.01** ```deleteting data in table```  
    I see request to enter the command  
        => enter command ```delete <tableName> <tableName> <column> <value>```  
        => see message, that command was successful  
        
**STR09.02** ```non-existent table```  
    I see request to enter the command  
        => enter command ```delete <non_exists_table_name> <column1> <value1> <column2> <value2> ...```    
        => see message, that table isn't exist  
        => see list of tables  
       
