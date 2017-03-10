begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|TimeUtil
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
name|restapi
operator|.
name|Response
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
name|restapi
operator|.
name|RestApiException
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
name|restapi
operator|.
name|RestModifyView
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
name|webui
operator|.
name|UiAction
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
name|Change
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
name|Change
operator|.
name|Status
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
name|server
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
name|change
operator|.
name|DeleteChange
operator|.
name|Input
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
name|project
operator|.
name|ChangeControl
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
name|update
operator|.
name|BatchUpdate
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
name|update
operator|.
name|UpdateException
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
name|server
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
name|Singleton
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteChange
specifier|public
class|class
name|DeleteChange
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|Input
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{}
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
DECL|field|opProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|DeleteChangeOp
argument_list|>
name|opProvider
decl_stmt|;
DECL|field|allowDrafts
specifier|private
specifier|final
name|boolean
name|allowDrafts
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteChange ( Provider<ReviewDb> db, BatchUpdate.Factory updateFactory, Provider<DeleteChangeOp> opProvider, @GerritServerConfig Config cfg)
specifier|public
name|DeleteChange
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|Provider
argument_list|<
name|DeleteChangeOp
argument_list|>
name|opProvider
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|opProvider
operator|=
name|opProvider
expr_stmt|;
name|this
operator|.
name|allowDrafts
operator|=
name|DeleteChangeOp
operator|.
name|allowDrafts
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|bu
operator|.
name|setOrder
argument_list|(
name|BatchUpdate
operator|.
name|Order
operator|.
name|DB_BEFORE_REPO
argument_list|)
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
name|opProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDescription (ChangeResource rsrc)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
block|{
try|try
block|{
name|Change
operator|.
name|Status
name|status
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|ChangeControl
name|changeControl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|boolean
name|visible
init|=
name|isActionAllowed
argument_list|(
name|changeControl
argument_list|,
name|status
argument_list|)
operator|&&
name|changeControl
operator|.
name|canDelete
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|status
argument_list|)
decl_stmt|;
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Delete"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Delete change "
operator|+
name|rsrc
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|setVisible
argument_list|(
name|visible
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isActionAllowed (ChangeControl changeControl, Status status)
specifier|private
name|boolean
name|isActionAllowed
parameter_list|(
name|ChangeControl
name|changeControl
parameter_list|,
name|Status
name|status
parameter_list|)
block|{
return|return
name|status
operator|!=
name|Status
operator|.
name|DRAFT
operator|||
name|allowDrafts
operator|||
name|changeControl
operator|.
name|isAdmin
argument_list|()
return|;
block|}
block|}
end_class

end_unit

