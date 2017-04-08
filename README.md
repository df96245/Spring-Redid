# Spring-Redid
spring 集成redis redid集群的搭建  spring配置redid集群  
在一台主机下模拟集群的搭建。分别在7001至7006六个端口启动六个redis服务端用来搭建redis集群
Linux下redis 集群的搭建步骤：
一.准备工作
1.在/usr/local/目录下创建redis-cluster文件夹，然后分别创建7001，7002，7003，7004，7005，7006六个文件夹，用来分别存放集群节点的配置文件。
2.将redis安装目录下的redis.conf分别复制到7001至7006六个文件夹中。
3.分别修改六个文件夹中的redis.conf：
daemonize   yes
port 700*
bind *.*.*.*(当前机器的ip)
dir /usr/local/redis-cluster/700*
cluster-enabled yes
cluster-config-file nodes-700*.conf
cluster-node-timeout 5000
appendonly yes  
4.需要安装下面的工具：
执行命令：1.yum install ruby 2.install rubygems 3.gem install redis
5.启动六个redis服务，查看是否成功
/usr/local/redis/bin/redis-server /usr/local/redis-cluster/700*/redis.conf
6.在redis的安装目录下进入src
7.执行 ./redis-trib.rb create --replicas 1 172.16.*.*：port ....(分别为六台redis的ip和端口)
其中参数1表示主节点与从节点的比例，会根据这个参数来设置后面的redis节点的主从特性。
8.客户端的连接：redis-cli -c -h (host) -p (port)
查看集群信息：cluster info 查看节点列表： cluster nodes



在已知集群中增加节点：
与以上一样，先创建两个新的redis节点7007，7008
1.新增一个主节点：
进入到redis安装目录下的src中，执行：./redis-trib.rb 172.16.34.136:7007(新增节点) 172.16.34.136:7001(集群中任意一个节点即可)
2.为7007进行分槽 只有主节点有槽，从节点没有
./redis-trib.rb reshard 172.16.34.136:7001（集群中任意一个主节点）
3.根据提示完成操作

若增加从节点
1.进入到redis安装目录下的src中，执行：./redis-trib.rb 172.16.34.136:7008(新增节点) 172.16.34.136:7001(集群中任意一个节点即可)
2.指定所属主节点
：进入客户端
redis-cli -c -h 172.16.34.136 -p 7001
执行命令：cluster replicate 382634a4025778c040b7213453fd42a709f79e28（主节点的id）


删除节点：
一.删除从节点7008，输入del-node命令，指定删除节点ip和端口，以及节点id（7008节点id）
进入安装目录src下 执行./redis-trib.rb del-node 172.16.34.136:7008 97b0e0115326833724eb0ffe1d0574ee34618e9f


二.删除主节点：
删除7007（master）节点之前，我们需要先把其全部的数据（slot槽）移动到其他节点上去（目前只能把master的数据迁移到一个节点上，暂时做不了平均分配功能）：./redis-trib.rb reshard 172.16.34.136:7007    
在删除过程中根据终端相应提示完成操作
删除：./redis-trib.rb del-node 
172.16.34.136:7007 382634a4025778c040b7213453fd42a709f79e28







