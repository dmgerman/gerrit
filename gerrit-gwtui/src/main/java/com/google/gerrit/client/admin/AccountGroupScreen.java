begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|Dispatcher
operator|.
name|toGroup
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
name|ui
operator|.
name|MenuScreen
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
name|GroupDetail
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
name|AccountGroup
import|;
end_import

begin_class
DECL|class|AccountGroupScreen
specifier|public
specifier|abstract
class|class
name|AccountGroupScreen
extends|extends
name|MenuScreen
block|{
DECL|field|INFO
specifier|public
specifier|static
specifier|final
name|String
name|INFO
init|=
literal|"info"
decl_stmt|;
DECL|field|MEMBERS
specifier|public
specifier|static
specifier|final
name|String
name|MEMBERS
init|=
literal|"members"
decl_stmt|;
DECL|field|groupDetail
specifier|private
specifier|final
name|GroupDetail
name|groupDetail
decl_stmt|;
DECL|field|membersTabToken
specifier|private
specifier|final
name|String
name|membersTabToken
decl_stmt|;
DECL|method|AccountGroupScreen (final GroupDetail toShow, final String token)
specifier|public
name|AccountGroupScreen
parameter_list|(
specifier|final
name|GroupDetail
name|toShow
parameter_list|,
specifier|final
name|String
name|token
parameter_list|)
block|{
name|setRequiresSignIn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|groupDetail
operator|=
name|toShow
expr_stmt|;
name|this
operator|.
name|membersTabToken
operator|=
name|getTabToken
argument_list|(
name|token
argument_list|,
name|MEMBERS
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|groupTabGeneral
argument_list|()
argument_list|,
name|getTabToken
argument_list|(
name|token
argument_list|,
name|INFO
argument_list|)
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|groupTabMembers
argument_list|()
argument_list|,
name|membersTabToken
argument_list|,
name|groupDetail
operator|.
name|group
operator|.
name|getType
argument_list|()
operator|==
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
argument_list|)
expr_stmt|;
block|}
DECL|method|getTabToken (final String token, final String tab)
specifier|private
name|String
name|getTabToken
parameter_list|(
specifier|final
name|String
name|token
parameter_list|,
specifier|final
name|String
name|tab
parameter_list|)
block|{
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
literal|"/admin/groups/uuid-"
argument_list|)
condition|)
block|{
return|return
name|toGroup
argument_list|(
name|groupDetail
operator|.
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|tab
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toGroup
argument_list|(
name|groupDetail
operator|.
name|group
operator|.
name|getId
argument_list|()
argument_list|,
name|tab
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|M
operator|.
name|group
argument_list|(
name|groupDetail
operator|.
name|group
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|display
argument_list|()
expr_stmt|;
name|display
argument_list|(
name|groupDetail
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final GroupDetail groupDetail)
specifier|protected
specifier|abstract
name|void
name|display
parameter_list|(
specifier|final
name|GroupDetail
name|groupDetail
parameter_list|)
function_decl|;
DECL|method|getGroupId ()
specifier|protected
name|AccountGroup
operator|.
name|Id
name|getGroupId
parameter_list|()
block|{
return|return
name|groupDetail
operator|.
name|group
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|setMembersTabVisible (final boolean visible)
specifier|protected
name|void
name|setMembersTabVisible
parameter_list|(
specifier|final
name|boolean
name|visible
parameter_list|)
block|{
name|setLinkVisible
argument_list|(
name|membersTabToken
argument_list|,
name|visible
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

