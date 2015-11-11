begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|project
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
name|ChangeHooks
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
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|data
operator|.
name|AccessSection
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
name|common
operator|.
name|data
operator|.
name|ProjectAccess
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
name|reviewdb
operator|.
name|client
operator|.
name|Branch
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|IdentifiedUser
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
name|account
operator|.
name|GroupBackend
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
name|AllProjectsNameProvider
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|git
operator|.
name|MetaDataUpdate
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
name|git
operator|.
name|ProjectConfig
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
name|project
operator|.
name|NoSuchProjectException
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectControl
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
name|project
operator|.
name|SetParent
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
name|Provider
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
name|assistedinject
operator|.
name|Assisted
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
name|ObjectId
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
name|revwalk
operator|.
name|RevCommit
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
name|List
import|;
end_import

begin_class
DECL|class|ChangeProjectAccess
class|class
name|ChangeProjectAccess
extends|extends
name|ProjectAccessHandler
argument_list|<
name|ProjectAccess
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ( @ssistedR) Project.NameKey projectName, @Nullable @Assisted ObjectId base, @Assisted List<AccessSection> sectionList, @Nullable @Assisted(R) Project.NameKey parentProjectName, @Nullable @Assisted String message)
name|ChangeProjectAccess
name|create
parameter_list|(
annotation|@
name|Assisted
argument_list|(
literal|"projectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|ObjectId
name|base
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sectionList
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
argument_list|(
literal|"parentProjectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|parentProjectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|String
name|message
parameter_list|)
function_decl|;
block|}
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|gitRefUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|projectAccessFactory
specifier|private
specifier|final
name|ProjectAccessFactory
operator|.
name|Factory
name|projectAccessFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeProjectAccess (ProjectAccessFactory.Factory projectAccessFactory, ProjectControl.Factory projectControlFactory, ProjectCache projectCache, GroupBackend groupBackend, MetaDataUpdate.User metaDataUpdateFactory, AllProjectsNameProvider allProjects, Provider<SetParent> setParent, ChangeHooks hooks, GitReferenceUpdated gitRefUpdated, IdentifiedUser user, @Assisted(R) Project.NameKey projectName, @Nullable @Assisted ObjectId base, @Assisted List<AccessSection> sectionList, @Nullable @Assisted(R) Project.NameKey parentProjectName, @Nullable @Assisted String message)
name|ChangeProjectAccess
parameter_list|(
name|ProjectAccessFactory
operator|.
name|Factory
name|projectAccessFactory
parameter_list|,
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
parameter_list|,
name|AllProjectsNameProvider
name|allProjects
parameter_list|,
name|Provider
argument_list|<
name|SetParent
argument_list|>
name|setParent
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|,
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"projectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|ObjectId
name|base
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sectionList
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
argument_list|(
literal|"parentProjectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|parentProjectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|projectControlFactory
argument_list|,
name|groupBackend
argument_list|,
name|metaDataUpdateFactory
argument_list|,
name|allProjects
argument_list|,
name|setParent
argument_list|,
name|projectName
argument_list|,
name|base
argument_list|,
name|sectionList
argument_list|,
name|parentProjectName
argument_list|,
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectAccessFactory
operator|=
name|projectAccessFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|gitRefUpdated
operator|=
name|gitRefUpdated
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateProjectConfig (ProjectControl ctl, ProjectConfig config, MetaDataUpdate md, boolean parentProjectUpdate)
specifier|protected
name|ProjectAccess
name|updateProjectConfig
parameter_list|(
name|ProjectControl
name|ctl
parameter_list|,
name|ProjectConfig
name|config
parameter_list|,
name|MetaDataUpdate
name|md
parameter_list|,
name|boolean
name|parentProjectUpdate
parameter_list|)
throws|throws
name|IOException
throws|,
name|NoSuchProjectException
throws|,
name|ConfigInvalidException
block|{
name|RevCommit
name|commit
init|=
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|gitRefUpdated
operator|.
name|fire
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|base
argument_list|,
name|commit
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|hooks
operator|.
name|doRefUpdatedHook
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
argument_list|,
name|base
argument_list|,
name|commit
operator|.
name|getId
argument_list|()
argument_list|,
name|user
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|projectAccessFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
block|}
end_class

end_unit

