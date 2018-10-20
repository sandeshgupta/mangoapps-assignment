set ROOT="C:\\Users\\Sandesh\\Desktop\\MangoApps\\TestExecutionEnvironment\\Application\\"
set SOURCE_DIR_ROOT_CLIENT="C:\\Users\\Sandesh\\Desktop\\MangoApps\\TestExecutionEnvironment\\FileSystem\\Client\\"
set SOURCE_DIR_ROOT_SERVER="C:\\Users\\Sandesh\\Desktop\\MangoApps\\TestExecutionEnvironment\\FileSystem\\Server\\"

cd %ROOT%

cd Server
start cmd /k "java -jar AppSyncServer.jar %ROOT%Server\\properties\\ %SOURCE_DIR_ROOT_SERVER%"

cd ../Client/
cd adminDevice1
start cmd /k "java -jar AppSyncClient.jar admin admin %ROOT%Client\\adminDevice1\\properties\\ %SOURCE_DIR_ROOT_CLIENT%admin\\device1\\" 
cd ../adminDevice2
start cmd /k "java -jar AppSyncClient.jar admin admin %ROOT%Client\\adminDevice2\\properties\\ %SOURCE_DIR_ROOT_CLIENT%admin\\device2\\"
cd ../adminDevice3
start cmd /k "java -jar AppSyncClient.jar admin admin %ROOT%Client\\adminDevice3\\properties\\ %SOURCE_DIR_ROOT_CLIENT%admin\\device3\\"
cd ../clientDevice1
start cmd /k "java -jar AppSyncClient.jar client2 admin %ROOT%Client\\client2Device1\\properties\\ %SOURCE_DIR_ROOT_CLIENT%client2\\device1\\"
cd ../clientDevice2
start cmd /k "java -jar AppSyncClient.jar client2 admin %ROOT%Client\\client2Device2\\properties\\ %SOURCE_DIR_ROOT_CLIENT%client2\\device2\\"