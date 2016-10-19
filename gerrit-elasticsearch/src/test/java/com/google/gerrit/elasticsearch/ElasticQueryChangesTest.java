begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|ElasticChangeIndex
operator|.
name|CLOSED_CHANGES
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|ElasticChangeIndex
operator|.
name|OPEN_CHANGES
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|ElasticChangeIndex
operator|.
name|ChangeMapping
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|IndexModule
operator|.
name|IndexType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeSchemaDefinitions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|AbstractQueryChangesTest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
operator|.
name|InMemoryModule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|FieldNamingPolicy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|GsonBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Guice
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|admin
operator|.
name|cluster
operator|.
name|node
operator|.
name|info
operator|.
name|NodesInfoRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|common
operator|.
name|settings
operator|.
name|Settings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|NodeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_class
DECL|class|ElasticQueryChangesTest
specifier|public
class|class
name|ElasticQueryChangesTest
extends|extends
name|AbstractQueryChangesTest
block|{
DECL|field|gson
specifier|private
specifier|static
specifier|final
name|Gson
name|gson
init|=
operator|new
name|GsonBuilder
argument_list|()
operator|.
name|setFieldNamingPolicy
argument_list|(
name|FieldNamingPolicy
operator|.
name|LOWER_CASE_WITH_UNDERSCORES
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|node
specifier|private
specifier|static
name|Node
name|node
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
name|String
name|port
decl_stmt|;
DECL|field|elasticDir
specifier|private
specifier|static
name|File
name|elasticDir
decl_stmt|;
DECL|class|NodeInfo
specifier|static
class|class
name|NodeInfo
block|{
DECL|field|httpAddress
name|String
name|httpAddress
decl_stmt|;
block|}
DECL|class|Info
specifier|static
class|class
name|Info
block|{
DECL|field|nodes
name|Map
argument_list|<
name|String
argument_list|,
name|NodeInfo
argument_list|>
name|nodes
decl_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|startIndexService ()
specifier|public
specifier|static
name|void
name|startIndexService
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
comment|// do not start Elasticsearch twice
return|return;
block|}
name|elasticDir
operator|=
name|Files
operator|.
name|createTempDir
argument_list|()
expr_stmt|;
name|Path
name|elasticDirPath
init|=
name|elasticDir
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|Settings
name|settings
init|=
name|Settings
operator|.
name|settingsBuilder
argument_list|()
operator|.
name|put
argument_list|(
literal|"cluster.name"
argument_list|,
literal|"gerrit"
argument_list|)
operator|.
name|put
argument_list|(
literal|"node.name"
argument_list|,
literal|"Gerrit Elasticsearch Test Node"
argument_list|)
operator|.
name|put
argument_list|(
literal|"node.local"
argument_list|,
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"discovery.zen.ping.multicast.enabled"
argument_list|,
literal|false
argument_list|)
operator|.
name|put
argument_list|(
literal|"index.store.fs.memory.enabled"
argument_list|,
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"index.gateway.type"
argument_list|,
literal|"none"
argument_list|)
operator|.
name|put
argument_list|(
literal|"index.max_result_window"
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|put
argument_list|(
literal|"gateway.type"
argument_list|,
literal|"default"
argument_list|)
operator|.
name|put
argument_list|(
literal|"http.port"
argument_list|,
literal|0
argument_list|)
operator|.
name|put
argument_list|(
literal|"discovery.zen.ping.unicast.hosts"
argument_list|,
literal|"[\"localhost\"]"
argument_list|)
operator|.
name|put
argument_list|(
literal|"path.home"
argument_list|,
name|elasticDirPath
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"path.data"
argument_list|,
name|elasticDirPath
operator|.
name|resolve
argument_list|(
literal|"data"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"path.work"
argument_list|,
name|elasticDirPath
operator|.
name|resolve
argument_list|(
literal|"work"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"path.logs"
argument_list|,
name|elasticDirPath
operator|.
name|resolve
argument_list|(
literal|"logs"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"transport.tcp.connect_timeout"
argument_list|,
literal|"60s"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// Start the node
name|node
operator|=
name|NodeBuilder
operator|.
name|nodeBuilder
argument_list|()
operator|.
name|settings
argument_list|(
name|settings
argument_list|)
operator|.
name|node
argument_list|()
expr_stmt|;
comment|// Wait for it to be ready
name|node
operator|.
name|client
argument_list|()
operator|.
name|admin
argument_list|()
operator|.
name|cluster
argument_list|()
operator|.
name|prepareHealth
argument_list|()
operator|.
name|setWaitForYellowStatus
argument_list|()
operator|.
name|execute
argument_list|()
operator|.
name|actionGet
argument_list|()
expr_stmt|;
name|createIndexes
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|node
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|port
operator|=
name|getHttpPort
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|cleanupIndex ()
specifier|public
name|void
name|cleanupIndex
parameter_list|()
block|{
name|node
operator|.
name|client
argument_list|()
operator|.
name|admin
argument_list|()
operator|.
name|indices
argument_list|()
operator|.
name|prepareDelete
argument_list|(
literal|"gerrit"
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|createIndexes
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopElasticsearchServer ()
specifier|public
specifier|static
name|void
name|stopElasticsearchServer
parameter_list|()
block|{
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|close
argument_list|()
expr_stmt|;
name|node
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|elasticDir
operator|!=
literal|null
operator|&&
name|elasticDir
operator|.
name|delete
argument_list|()
condition|)
block|{
name|elasticDir
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createInjector ()
specifier|protected
name|Injector
name|createInjector
parameter_list|()
block|{
name|Config
name|elasticsearchConfig
init|=
operator|new
name|Config
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|InMemoryModule
operator|.
name|setDefaults
argument_list|(
name|elasticsearchConfig
argument_list|)
expr_stmt|;
name|elasticsearchConfig
operator|.
name|setEnum
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|,
name|IndexType
operator|.
name|ELASTICSEARCH
argument_list|)
expr_stmt|;
name|elasticsearchConfig
operator|.
name|setString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"protocol"
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
name|elasticsearchConfig
operator|.
name|setString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"hostname"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|elasticsearchConfig
operator|.
name|setString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|elasticsearchConfig
operator|.
name|setString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"name"
argument_list|,
literal|"gerrit"
argument_list|)
expr_stmt|;
name|elasticsearchConfig
operator|.
name|setBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"elasticsearch"
argument_list|,
literal|"test"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|InMemoryModule
argument_list|(
name|elasticsearchConfig
argument_list|,
name|notesMigration
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createIndexes ()
specifier|private
specifier|static
name|void
name|createIndexes
parameter_list|()
block|{
name|ChangeMapping
name|openChangesMapping
init|=
operator|new
name|ChangeMapping
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
operator|.
name|getLatest
argument_list|()
argument_list|)
decl_stmt|;
name|ChangeMapping
name|closedChangesMapping
init|=
operator|new
name|ChangeMapping
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
operator|.
name|getLatest
argument_list|()
argument_list|)
decl_stmt|;
name|openChangesMapping
operator|.
name|closedChanges
operator|=
literal|null
expr_stmt|;
name|closedChangesMapping
operator|.
name|openChanges
operator|=
literal|null
expr_stmt|;
name|node
operator|.
name|client
argument_list|()
operator|.
name|admin
argument_list|()
operator|.
name|indices
argument_list|()
operator|.
name|prepareCreate
argument_list|(
literal|"gerrit"
argument_list|)
operator|.
name|addMapping
argument_list|(
name|OPEN_CHANGES
argument_list|,
name|gson
operator|.
name|toJson
argument_list|(
name|openChangesMapping
argument_list|)
argument_list|)
operator|.
name|addMapping
argument_list|(
name|CLOSED_CHANGES
argument_list|,
name|gson
operator|.
name|toJson
argument_list|(
name|closedChangesMapping
argument_list|)
argument_list|)
operator|.
name|execute
argument_list|()
operator|.
name|actionGet
argument_list|()
expr_stmt|;
block|}
DECL|method|getHttpPort ()
specifier|private
specifier|static
name|String
name|getHttpPort
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|String
name|nodes
init|=
name|node
operator|.
name|client
argument_list|()
operator|.
name|admin
argument_list|()
operator|.
name|cluster
argument_list|()
operator|.
name|nodesInfo
argument_list|(
operator|new
name|NodesInfoRequest
argument_list|(
literal|"*"
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Gson
name|gson
init|=
operator|new
name|GsonBuilder
argument_list|()
operator|.
name|setFieldNamingPolicy
argument_list|(
name|FieldNamingPolicy
operator|.
name|LOWER_CASE_WITH_UNDERSCORES
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|Info
name|info
init|=
name|gson
operator|.
name|fromJson
argument_list|(
name|nodes
argument_list|,
name|Info
operator|.
name|class
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|info
operator|.
name|nodes
operator|!=
literal|null
operator|&&
name|info
operator|.
name|nodes
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|NodeInfo
argument_list|>
name|values
init|=
name|info
operator|.
name|nodes
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|httpAddress
init|=
name|values
operator|.
name|next
argument_list|()
operator|.
name|httpAddress
decl_stmt|;
name|checkState
argument_list|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|httpAddress
argument_list|)
operator|&&
name|httpAddress
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
return|return
name|httpAddress
operator|.
name|substring
argument_list|(
name|httpAddress
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|,
name|httpAddress
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

