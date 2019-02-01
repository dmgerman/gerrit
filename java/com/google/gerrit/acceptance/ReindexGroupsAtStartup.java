begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|config
operator|.
name|GerritServerConfig
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
name|group
operator|.
name|db
operator|.
name|Groups
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
name|group
operator|.
name|GroupIndexer
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
name|Inject
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
name|Scopes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|errors
operator|.
name|ConfigInvalidException
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

begin_comment
comment|/** Reindex all groups at Gerrit daemon startup. */
end_comment

begin_class
DECL|class|ReindexGroupsAtStartup
specifier|public
class|class
name|ReindexGroupsAtStartup
implements|implements
name|LifecycleListener
block|{
DECL|field|groupIndexer
specifier|private
specifier|final
name|GroupIndexer
name|groupIndexer
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|ReindexGroupsAtStartup
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
DECL|method|ReindexGroupsAtStartup ( GroupIndexer groupIndexer, Groups groups, @GerritServerConfig Config cfg)
specifier|public
name|ReindexGroupsAtStartup
parameter_list|(
name|GroupIndexer
name|groupIndexer
parameter_list|,
name|Groups
name|groups
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|groupIndexer
operator|=
name|groupIndexer
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// Gerrit slaves without a reindex
if|if
condition|(
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"container"
argument_list|,
literal|"slave"
argument_list|,
literal|false
argument_list|)
operator|&&
operator|!
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"scheduledIndexer"
argument_list|,
literal|"runOnStartup"
argument_list|,
literal|true
argument_list|)
condition|)
block|{
return|return;
block|}
name|Stream
argument_list|<
name|GroupReference
argument_list|>
name|allGroupReferences
decl_stmt|;
try|try
block|{
name|allGroupReferences
operator|=
name|groups
operator|.
name|getAllGroupReferences
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to reindex groups, tests may fail"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|allGroupReferences
operator|.
name|forEach
argument_list|(
name|group
lambda|->
block|{
try|try
block|{
name|groupIndexer
operator|.
name|index
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unable to index %s, tests may fail"
argument_list|,
name|group
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{}
block|}
end_class

end_unit

