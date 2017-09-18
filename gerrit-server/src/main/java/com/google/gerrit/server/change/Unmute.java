begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|StarredChangesUtil
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
name|StarredChangesUtil
operator|.
name|IllegalLabelException
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|Unmute
specifier|public
class|class
name|Unmute
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|Unmute
operator|.
name|Input
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Unmute
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{}
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
decl_stmt|;
DECL|field|stars
specifier|private
specifier|final
name|StarredChangesUtil
name|stars
decl_stmt|;
annotation|@
name|Inject
DECL|method|Unmute (Provider<IdentifiedUser> self, StarredChangesUtil stars)
name|Unmute
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
parameter_list|,
name|StarredChangesUtil
name|stars
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|stars
operator|=
name|stars
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDescription (ChangeResource rsrc)
specifier|public
name|Description
name|getDescription
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
block|{
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Unmute"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Unmute the change"
argument_list|)
operator|.
name|setVisible
argument_list|(
operator|!
name|rsrc
operator|.
name|isUserOwner
argument_list|()
operator|&&
name|isUnMuteable
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|String
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
name|OrmException
throws|,
name|IllegalLabelException
block|{
if|if
condition|(
name|rsrc
operator|.
name|isUserOwner
argument_list|()
operator|||
operator|!
name|isMuted
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|)
condition|)
block|{
comment|// early exit for own changes and not muted changes
return|return
name|Response
operator|.
name|ok
argument_list|(
literal|""
argument_list|)
return|;
block|}
name|stars
operator|.
name|unmute
argument_list|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|isMuted (Change change)
specifier|private
name|boolean
name|isMuted
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
try|try
block|{
return|return
name|stars
operator|.
name|isMutedBy
argument_list|(
name|change
argument_list|,
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"failed to check muted star"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|isUnMuteable (Change change)
specifier|private
name|boolean
name|isUnMuteable
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
try|try
block|{
return|return
name|isMuted
argument_list|(
name|change
argument_list|)
operator|&&
operator|!
name|stars
operator|.
name|isIgnoredBy
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"failed to check ignored star"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

