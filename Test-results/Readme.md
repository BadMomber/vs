# Testresults
The database only saves unique values in the "valueset" column, so our database only contains unique rows.
## persistance level data in sync after 125 minutes runtime
System keeps running for 125 minutes without getting out of sync.  
This means that the 3 independent databases are all saving the exact same messages while the  
HAProxy sends the messages round robin to 3 different grpc servers each with its own database server.  
  
Check the screenshots below for documentation that the messages where in sync after 3479 messages and the system ran for 125 minutes.  
  
Database 1 Message received time.
![](125_Minuten_Laufzeit-erste_Nachrichten1.png)
Database 2 Message received time.
![](125_Minuten_Laufzeit-erste_Nachrichten2.png)
Database 3 Message received time.
![](125_Minuten_Laufzeit-erste_Nachrichten3.png)
Number of messages in database 1 after 125 minutes runtime
![](125_Minuten_Laufzeit_sync1.png)
Number of messages in database 2 after 125 minutes runtime
![](125_Minuten_Laufzeit_sync2.png)
Number of messages in database 3 after 125 minutes runtime
![](125_Minuten_Laufzeit_sync3.png)

## Shut down one grpc server for 40 seconds - reboot down grpc server - System recovers lost messages from other databases and returns to be nearly in sync

Database 1 - number of messages - no downtime
![](shutdown-lost-messages-1.png)
Database 3 - number of messages - no downtime
![](shutdown-lost-messages-3.png)
Database 2 - number of messages - 40 seconds downtime - messages lost
![](shutdown-lost-messages-2.png)
  
  
Database 1 - number of messages - didn't have downtime
![](after-shutdown-returned-messages-1.png)
Database 3 - number of messages - didn't have downtime
![](after-shutdown-returned-messages-3.png)
Database 2 - number of messages - has been down for ~40 seconds, did recover nearly all messages - difference of 3 messages
![](after-shutdown-returned-messages-2.png)

## After grpc server 2 returned and system ran for a while, the messages continue to stay in sync
Database 1
![](after-shutdown-1.png)
Database 2 - still only 3 messages missing
![](after-shutdown-2.png)
Database 3
![](after-shutdown-3.png)

## Loadtest
System ran for 1 minute and processed 1301 messages. Each message was saved in the persistance cluster and each database in the cluster has the same number of messages saved.
  
  
Please check the screenshots below for checking the results and compare the first and the last time value of the received messages.
Loadtest result database 1
![](loadtest_1.png)
Loadtest result database 2
![](loadtest_2.png)
Loadtest result database 3
![](loadtest_3.png)
Loadtest result database 1
![](loadtest_1-1.png)
Loadtest result database 2
![](loadtest_2-1.png)
Loadtest result database 3
![](loadtest_3-1.png)
