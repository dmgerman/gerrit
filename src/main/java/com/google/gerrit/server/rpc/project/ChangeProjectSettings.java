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
DECL|package|com.google.gerrit.server.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|client
operator|.
name|admin
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
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|GerritServer
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|update
specifier|private
specifier|final
name|Project
name|update
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeProjectSettings ( final ProjectDetailFactory.Factory projectDetailFactory, final ProjectControl.Factory projectControlFactory, final ProjectCache projectCache, final ReviewDb db, final GerritServer server, @Assisted final Project update)
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
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|GerritServer
name|server
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
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|server
operator|=
name|server
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
specifier|final
name|ProjectControl
name|projectControl
init|=
name|projectControlFactory
operator|.
name|ownerFor
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
specifier|final
name|Project
name|proj
init|=
name|db
operator|.
name|projects
argument_list|()
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
if|if
condition|(
name|proj
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|projectName
argument_list|)
throw|;
block|}
name|proj
operator|.
name|copySettingsFrom
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|db
operator|.
name|projects
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|proj
argument_list|)
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|proj
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|projectControl
operator|.
name|getProjectState
argument_list|()
operator|.
name|isSpecialWildProject
argument_list|()
condition|)
block|{
name|server
operator|.
name|setProjectDescription
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|,
name|update
operator|.
name|getDescription
argument_list|()
argument_list|)
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

