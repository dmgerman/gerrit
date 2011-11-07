begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|data
operator|.
name|ProjectDetail
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|server
operator|.
name|git
operator|.
name|GitRepositoryManager
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
name|PerRequestProjectControlCache
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmConcurrencyException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
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
name|errors
operator|.
name|RepositoryNotFoundException
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

begin_class
DECL|class|ChangeProjectSettings
class|class
name|ChangeProjectSettings
extends|extends
name|Handler
argument_list|<
name|ProjectDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (@ssisted Project update)
name|ChangeProjectSettings
name|create
parameter_list|(
annotation|@
name|Assisted
name|Project
name|update
parameter_list|)
function_decl|;
block|}
DECL|field|projectDetailFactory
specifier|private
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|userCache
specifier|private
specifier|final
name|Provider
argument_list|<
name|PerRequestProjectControlCache
argument_list|>
name|userCache
decl_stmt|;
DECL|field|update
specifier|private
specifier|final
name|Project
name|update
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeProjectSettings ( final ProjectDetailFactory.Factory projectDetailFactory, final ProjectControl.Factory projectControlFactory, final GitRepositoryManager mgr, final MetaDataUpdate.User metaDataUpdateFactory, final Provider<PerRequestProjectControlCache> uc, @Assisted final Project update)
name|ChangeProjectSettings
parameter_list|(
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
parameter_list|,
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
specifier|final
name|GitRepositoryManager
name|mgr
parameter_list|,
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|PerRequestProjectControlCache
argument_list|>
name|uc
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Project
name|update
parameter_list|)
block|{
name|this
operator|.
name|projectDetailFactory
operator|=
name|projectDetailFactory
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|mgr
operator|=
name|mgr
expr_stmt|;
name|this
operator|.
name|userCache
operator|=
name|uc
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|update
operator|=
name|update
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ProjectDetail
name|call
parameter_list|()
throws|throws
name|NoSuchProjectException
throws|,
name|OrmException
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|update
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|projectControlFactory
operator|.
name|ownerFor
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
specifier|final
name|MetaDataUpdate
name|md
decl_stmt|;
try|try
block|{
name|md
operator|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|projectName
argument_list|)
throw|;
block|}
try|try
block|{
comment|// TODO We really should take advantage of the Git commit DAG and
comment|// ensure the current version matches the old version the caller read.
comment|//
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|config
operator|.
name|getProject
argument_list|()
operator|.
name|copySettingsFrom
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
literal|"Modified project settings\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
condition|)
block|{
name|mgr
operator|.
name|setProjectDescription
argument_list|(
name|projectName
argument_list|,
name|update
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|userCache
operator|.
name|get
argument_list|()
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|OrmConcurrencyException
argument_list|(
literal|"Cannot update "
operator|+
name|projectName
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot read project "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot update project "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
throw|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|projectDetailFactory
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

